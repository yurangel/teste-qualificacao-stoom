package com.stoom.testequalificacao.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stoom.testequalificacao.model.Address;
import com.stoom.testequalificacao.repository.AddressRepository;
import com.stoom.testequalificacao.service.AddressService;
import com.stoom.testequalificacao.service.GeocodingService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.stoom.testequalificacao.utils.Constants.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@RunWith(SpringRunner.class)
@WebMvcTest(AddressController.class)
public class AddressControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AddressService addressService;
    @MockBean
    private GeocodingService geocodingService;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void getAddress() throws Exception {
        //GIVEN
        Address address = createAddressData(ADDRESS_OK);
        Address addresseReturn = createAddressData(ADDRESS_OK);

        //WHEN
        when(addressService.findById(anyLong())).thenReturn(addresseReturn);
        RequestBuilder request = get("/address/1").contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON_UTF8);
        MvcResult result = mvc.perform(request).andReturn();

        //THEN
        assertEquals(200, result.getResponse().getStatus());
        Address addressResponse = mapper.readValue(result.getResponse().getContentAsString(), Address.class);
        assertThat(address).isEqualToComparingFieldByFieldRecursively(addressResponse);

    }

    @Test
    public void getAllAddress() throws Exception {
        //GIVEN
        Address address = createAddressData(ADDRESS_OK);
        List<Address> addresses = Collections.singletonList(address);

        //WHEN
        when(addressService.findAll()).thenReturn(addresses);
        RequestBuilder request = get("/address").contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON_UTF8);
        MvcResult result = mvc.perform(request).andReturn();

        //THEN
        JSONObject addressJSON = new JSONObject(new JSONArray(result.getResponse().getContentAsString()).get(0).toString());

        assertEquals(200, result.getResponse().getStatus());
        assertEquals(address.getId(), addressJSON.getLong("id"));
        assertEquals(address.getStreetName(), addressJSON.getString("streetName"));
        assertEquals(address.getCity(), addressJSON.getString("city"));
        assertEquals(address.getComplement(), addressJSON.getString("complement"));
        assertEquals(address.getCountry(), addressJSON.getString("country"));
        assertEquals(address.getNeighbourhood(), addressJSON.getString("neighbourhood"));
        assertEquals(address.getState(), addressJSON.getString("state"));
        assertEquals(address.getNumber(), addressJSON.getInt("number"));
        assertEquals(address.getLatitude(), addressJSON.getString("latitude"));
        assertEquals(address.getLongitude(), addressJSON.getString("longitude"));

    }

    @Test
    public void saveAddress() throws Exception {
        //GIVEN
        Address address = createAddressData(ADDRESS_OK);

        //WHEN
        when(addressService.save(any(Address.class))).thenReturn(address);
        RequestBuilder request = post("/address/newaddress")
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(address));
        JSONObject jsonObject;
        //THEN
        MvcResult result = mvc.perform(request).andReturn();
        assertEquals(301, result.getResponse().getStatus());
        assertEquals("/address", result.getResponse().getRedirectedUrl());
    }

    @Test
    public void updateAddress() throws Exception {
        //GIVEN
        Address address = createAddressData(ADDRESS_OK);
        Address addressUpdate = createAddressData(ADDRESS_UPDATE);

        //WHEN
        when(addressService.updateAddressById(anyLong(), any(Address.class))).thenReturn(addressUpdate);
        RequestBuilder request = put("/address/newaddress/1")
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(address));
        //THEN
        MvcResult result = mvc.perform(request).andReturn();
        Address addressResponse = mapper.readValue(result.getResponse().getContentAsString(), Address.class);
        assertEquals(200, result.getResponse().getStatus());
        assertThat(addressUpdate).isEqualToComparingFieldByFieldRecursively(addressResponse);
    }

    @Test
    public void redirectAndMessageTest() throws Exception {
        //GIVEN
        Address address = new Address();
        address.setCity("Santa Bárbara D'Oeste");

        //WHEN
        when(addressService.save(any(Address.class))).thenReturn(address);

        RequestBuilder request = post("/address/newaddress")
                .contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(address));

        MvcResult result = mvc.perform(request).andReturn();

        //THEN
        assertEquals(301, result.getResponse().getStatus());
        assertEquals("/address", result.getResponse().getRedirectedUrl());
        assertEquals("Verifique os campos obrigatórios!", result.getFlashMap().get("message"));
    }

    public Address createAddressData(int Type){
        List<Address> addressList = new ArrayList<>();
        Address address = new Address();
        address.setId(1L);
        address.setStreetName("rua Jade");
        address.setNumber(149);
        address.setComplement("Casa");
        address.setNeighbourhood("Jd. São Fernando");
        address.setCity("Santa Bárbara D'Oeste");
        address.setCountry("Brasil");
        address.setState("SP");
        address.setZipcode("13454-235");
        address.setLongitude("-45.555645");
        address.setLatitude("-68.222545");

        Address addressUpdate = new Address();
        address.setId(1L);
        address.setStreetName("rua Medalhão");
        address.setNumber(658);
        address.setNeighbourhood("California");
        address.setCity("Americana");
        address.setCountry("Brasil");
        address.setState("SP");
        address.setZipcode("13450-000");
        address.setLongitude("-40.6658");
        address.setLatitude("-66.25565");

        Address addressLocation = new Address();
        address.setId(1L);
        address.setStreetName("rua Jade");
        address.setNumber(149);
        address.setComplement("Casa");
        address.setNeighbourhood("Jd. São Fernando");
        address.setCity("Santa Bárbara D'Oeste");
        address.setCountry("Brasil");
        address.setState("SP");
        address.setZipcode("13454-235");

        addressList.add(address);
        addressList.add(addressUpdate);
        addressList.add(addressLocation);

        return addressList.get(Type);
    }
}
package com.stoom.testequalificacao.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stoom.testequalificacao.model.Address;
import com.stoom.testequalificacao.service.AddressService;
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

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@WebMvcTest(AddressController.class)
public class AddressControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AddressService addressService;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void getAddress() throws Exception {
        //GIVEN
        Address address = createAddress();
        Address addresseReturn = createAddress();

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
        Address address = createAddress();
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
        assertEquals(address.getLatitude(), addressJSON.getLong("latitude"));
        assertEquals(address.getLongitude(), addressJSON.getLong("longitude"));

    }

    @Test
    public void saveAddress() throws Exception {
        //GIVEN
        Address address = createAddress();

        //WHEN
        when(addressService.save(any(Address.class))).thenReturn(address);
        RequestBuilder request = post("/address/newaddress")
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(address));

        //THEN
        MvcResult result = mvc.perform(request).andReturn();
        assertEquals("/address", result.getResponse().getRedirectedUrl());
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
        assertEquals("/address", result.getResponse().getRedirectedUrl());
        assertEquals("Verifique os campos obrigatórios!", result.getFlashMap().get("message"));
    }

    public Address createAddress(){
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
        address.setLongitude(123456789L);
        address.setLatitude(123456789L);

        return address;
    }
}
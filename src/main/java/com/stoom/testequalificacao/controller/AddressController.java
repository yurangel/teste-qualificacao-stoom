package com.stoom.testequalificacao.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.stoom.testequalificacao.model.Address;
import com.stoom.testequalificacao.service.AddressService;
import com.stoom.testequalificacao.service.GeocodingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

//@Controller
@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @Autowired
    private GeocodingService geocodingService;

    @GetMapping
    public ResponseEntity<List<Address>> getAllAddress(){
        List<Address> addressList = addressService.findAll();
        return ResponseEntity.ok(addressList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Address> getAddress(@PathVariable("id") long id){
        Address address = addressService.findById(id);
        return ResponseEntity.ok(address);
    }

    @PostMapping("/newaddress")
    public ResponseEntity saveAddress(@RequestBody @Valid Address address, BindingResult result, RedirectAttributes attributes) {
        if(result.hasErrors()){
            attributes.addFlashAttribute("message", "Verifique os campos obrigatórios!");
            return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header(HttpHeaders.LOCATION, "/address").build();
        }

        if(Objects.isNull(address.getLatitude()) || Objects.isNull(address.getLatitude())){
            address = geocodingService.generateLinkGeocoding(address);
            addressService.save(address);
        }else
            addressService.save(address);
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header(HttpHeaders.LOCATION, "/address").build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteAddress(@PathVariable("id") long id) {
        addressService.deleteById(id);
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header(HttpHeaders.LOCATION, "/address").build();
    }

    @PutMapping("/newaddress/{id}")
    public ResponseEntity<Address> updateAddress(@PathVariable("id") long id, @RequestBody @Valid Address address, BindingResult result, RedirectAttributes attributes) throws JsonProcessingException {
        Address addressResponse;
        if(result.hasErrors()){
            attributes.addFlashAttribute("message", "Verifique os campos obrigatórios!");
            return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header(HttpHeaders.LOCATION, "/address").build();
        }

        if(Objects.isNull(address.getLatitude()) || Objects.isNull(address.getLatitude())){
            address = geocodingService.generateLinkGeocoding(address);
            addressResponse = addressService.updateAddressById(id, address);
        }else
            addressResponse = addressService.updateAddressById(id, address);

        return ResponseEntity.ok(addressResponse);
    }


}

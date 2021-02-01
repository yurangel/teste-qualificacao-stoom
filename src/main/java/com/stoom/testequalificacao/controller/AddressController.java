package com.stoom.testequalificacao.controller;

import com.stoom.testequalificacao.model.Address;
import com.stoom.testequalificacao.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

//@Controller
@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired
    AddressService addressService;

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
        addressService.save(address);
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header(HttpHeaders.LOCATION, "/address").build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteAddress(@PathVariable("id") long id) {
        addressService.deleteById(id);
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header(HttpHeaders.LOCATION, "/address").build();
    }
}

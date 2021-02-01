package com.stoom.testequalificacao.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.stoom.testequalificacao.model.Address;

import java.util.List;

public interface AddressService {

    List<Address> findAll();
    Address findById(long id);
    Address save(Address address);
    void deleteById(long id);
    Address updateAddressById(Long id, Address address) throws JsonProcessingException;
}

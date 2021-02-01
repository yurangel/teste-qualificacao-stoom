package com.stoom.testequalificacao.service.serviceImpl;

import com.stoom.testequalificacao.model.Address;
import com.stoom.testequalificacao.repository.AddressRepository;
import com.stoom.testequalificacao.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    AddressRepository addressRepository;

    @Override
    public List<Address> findAll() {
        return addressRepository.findAll();
    }

    @Override
    public Address findById(long id) {
        return addressRepository.findById(id).get();
    }

    @Override
    public Address save(Address address) {
        return addressRepository.save(address);
    }

    @Override
    public void deleteById(long id) {
        addressRepository.deleteById(id);
    }
}

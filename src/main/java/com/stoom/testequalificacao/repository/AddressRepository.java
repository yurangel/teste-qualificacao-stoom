package com.stoom.testequalificacao.repository;

import com.stoom.testequalificacao.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
}

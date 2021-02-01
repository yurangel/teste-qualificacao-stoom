package com.stoom.testequalificacao.repository;

import com.stoom.testequalificacao.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}

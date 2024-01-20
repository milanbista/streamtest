package com.example.streamtest.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.streamtest.model.Customer;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Integer> {

}

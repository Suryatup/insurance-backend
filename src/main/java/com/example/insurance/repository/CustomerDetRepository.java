package com.example.insurance.repository;

import com.example.insurance.model.Customer;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

@Repository
public interface CustomerDetRepository extends MongoRepository<Customer, String> {


}

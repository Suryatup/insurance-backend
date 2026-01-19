package com.example.insurance.controller;


import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.insurance.model.Customer;
import com.example.insurance.service.CustomerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService service;

    @GetMapping
    public List<Customer> getAll() throws Exception {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Customer get(@PathVariable String id) throws Exception {
        return service.getById(id);
    }

    @PostMapping
    public Customer create(@RequestBody Customer customer) throws Exception {
        return service.create(customer);
    }

    @PutMapping("/{id}")
    public Customer update(@PathVariable String id, @RequestBody Customer customer) throws Exception {
        return service.update(id, customer);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}

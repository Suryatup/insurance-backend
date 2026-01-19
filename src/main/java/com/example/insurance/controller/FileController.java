package com.example.insurance.controller;

import java.util.ArrayList;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.insurance.model.Customer;
import com.example.insurance.model.DocumentFile;
import com.example.insurance.service.CustomerService;
import com.example.insurance.service.FileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;
    private final CustomerService customerService;

    @PostMapping(
        value = "/upload/{customerId}",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public Customer uploadFile(
            @PathVariable String customerId,
            @RequestParam("file") MultipartFile file
    ) throws Exception {

        Customer customer = customerService.getById(customerId);

        if (customer == null) {
            throw new RuntimeException("Customer not found: " + customerId);
        }

        if (customer.getDocuments() == null) {
            customer.setDocuments(new ArrayList<>());
        }

        DocumentFile uploadedFile = fileService.upload(customerId, file);

        customer.getDocuments().add(uploadedFile);

        return customerService.update(customerId, customer);
    }
}

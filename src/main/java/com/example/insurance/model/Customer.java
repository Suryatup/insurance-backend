package com.example.insurance.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "customer")
public class Customer {
    @Id
    private String id;

    private String name;
    private String phone;
    private String location;
    private String email;
    private String endDate;

    private String registrationDate;
    private String lastUpdated;

    private List<Vehicle> vehicles;
    private List<DocumentFile> documents;

    private Boolean expiryMailSent;
}



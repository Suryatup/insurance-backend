package com.example.insurance.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

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

    private Boolean expiryMailSent; // ✅ NEW FIELD
}



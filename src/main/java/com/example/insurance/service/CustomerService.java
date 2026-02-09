package com.example.insurance.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.example.insurance.model.UserEntity;
import com.example.insurance.repository.CustomerDetRepository;
import com.google.cloud.firestore.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.insurance.FirebaseConfig;
import com.example.insurance.model.Customer;
import com.google.api.core.ApiFuture;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class CustomerService {

    private final FirebaseConfig firebase;

    @Autowired
    private CustomerDetRepository customerDetRepository;

    private CollectionReference collection(String tableName) {
        return firebase.getDb().collection(tableName);
    }

    public List<Customer> getAll() throws Exception {
        return  customerDetRepository.findAll();
    }

    public Customer getById(String id) throws Exception {
        DocumentSnapshot doc =
                collection("customers").document(id).get().get();

        if (!doc.exists())
            return null;

        Customer c = doc.toObject(Customer.class);
        if (c != null)
            c.setId(doc.getId());

        return c;
    }

    // ✅ NEW customer → mail not sent yet
    public Customer create(Customer customer) throws Exception {

//        DocumentReference ref = collection("customers").document();
//
//        customer.setId(ref.getId());
        customer.setRegistrationDate(now());
        customer.setLastUpdated(now());

        customer.setExpiryMailSent(false);

       // ref.set(customer).get();
        customerDetRepository.save(customer);
        return customer;
    }

    // ✅ UPDATE with SMART mail reset logic
    public Customer update(String id, Customer customer) throws Exception {

        Customer existing = getById(id);
        if (existing == null)
            throw new RuntimeException("Customer not found");

        customer.setId(id);
        customer.setLastUpdated(now());

        // 🔑 Reset mail flag ONLY if policy becomes ACTIVE again
        if (existing.getEndDate() != null && customer.getEndDate() != null) {

            LocalDate today = LocalDate.now();
            LocalDate oldEnd = LocalDate.parse(existing.getEndDate());
            LocalDate newEnd = LocalDate.parse(customer.getEndDate());

            long oldDaysLeft =
                    ChronoUnit.DAYS.between(today, oldEnd);
            long newDaysLeft =
                    ChronoUnit.DAYS.between(today, newEnd);

            boolean wasExpiring = oldDaysLeft <= 7;
            boolean nowActive = newDaysLeft > 7;

            if (wasExpiring && nowActive) {
                // ✅ allow next expiry cycle
                customer.setExpiryMailSent(false);
            } else {
                customer.setExpiryMailSent(
                        existing.getExpiryMailSent());
            }

        } else {
            customer.setExpiryMailSent(
                    existing.getExpiryMailSent());
        }

        collection("customers").document(id).set(customer).get();
        return customer;
    }

    public void delete(String id) {
        collection("customers").document(id).delete();
    }

    private String now() {
        return new Date().toString();
    }


}

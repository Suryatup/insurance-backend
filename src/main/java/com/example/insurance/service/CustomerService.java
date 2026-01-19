package com.example.insurance.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.insurance.FirebaseConfig;
import com.example.insurance.model.Customer;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final FirebaseConfig firebase;

    private CollectionReference collection() {
        return firebase.getDb().collection("customers");
    }

    public List<Customer> getAll() throws Exception {
        List<Customer> list = new ArrayList<>();
        ApiFuture<QuerySnapshot> future = collection().get();

        for (DocumentSnapshot d : future.get().getDocuments()) {
            Customer c = d.toObject(Customer.class);
            if (c != null) {
                c.setId(d.getId());
                list.add(c);
            }
        }
        return list;
    }

    public Customer getById(String id) throws Exception {
        DocumentSnapshot doc =
                collection().document(id).get().get();

        if (!doc.exists())
            return null;

        Customer c = doc.toObject(Customer.class);
        if (c != null)
            c.setId(doc.getId());

        return c;
    }

    // ✅ NEW customer → mail not sent yet
    public Customer create(Customer customer) throws Exception {

        DocumentReference ref = collection().document();

        customer.setId(ref.getId());
        customer.setRegistrationDate(now());
        customer.setLastUpdated(now());

        customer.setExpiryMailSent(false);

        ref.set(customer).get();
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

        collection().document(id).set(customer).get();
        return customer;
    }

    public void delete(String id) {
        collection().document(id).delete();
    }

    private String now() {
        return new Date().toString();
    }
}

package com.example.insurance.repository;

import com.example.insurance.model.Customer;
import com.example.insurance.model.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDetRepository extends MongoRepository<UserEntity, String> {

    public UserEntity findByUserName(String name);

}

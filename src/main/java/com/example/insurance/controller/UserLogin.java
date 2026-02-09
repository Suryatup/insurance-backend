package com.example.insurance.controller;

import com.example.insurance.model.UserEntity;
import com.example.insurance.security.JWtUtil;
import com.example.insurance.service.CustomerService;
import com.mongodb.client.MongoCollection;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class UserLogin {

      @Autowired
      CustomerService customerService;
      @Autowired
      AuthenticationManager authManager;
      @Autowired
      PasswordEncoder passwordEncoder;
      @Autowired
      JWtUtil jWtUtil;
    @Autowired
    private MongoTemplate mongoTemplate;

    @PostMapping("/login")
    private ResponseEntity<Map> userLogin(@RequestBody UserEntity customerJson){
        try {
            System.out.println(passwordEncoder.encode(customerJson.getPassWord()));
            MongoCollection<Document> collection =
                    mongoTemplate.getCollection("movies");
            Authentication authentication= authManager.authenticate(new UsernamePasswordAuthenticationToken(customerJson.getUserName(),customerJson.getPassWord()));
            UserDetails userDetails=(UserDetails) authentication.getPrincipal();

            Map responceMap=jWtUtil.genarateToken(userDetails);
            return ResponseEntity.ok(responceMap);

        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("Error","Invalid user"));
        }
    }

}

package com.example.insurance.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("userEntity")
public class UserEntity {
    @Id
    private String id;
    private String DocumentId;
    private String userName;
    private String passWord;
    private String role;
    private String phone;

}



package com.example.insurance.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Todo {

    private String id;
    private String title;
    private boolean completed;
    private Date createdAt;
}

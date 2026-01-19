package com.example.insurance.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentFile {
    private String id;
    private String name;
    private String url;
    private String type;
    private long size;
}

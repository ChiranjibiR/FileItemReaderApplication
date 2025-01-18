package com.cloudops.FileItemReader.model;

import lombok.Data;

@Data
public class StudentJdbc {
    private Long id;
    private String first_name;
    private String last_name;
    private String email;
}

package com.cloudops.FileItemReader.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true) //This property is used when we want to exclude certain parameters like lastName in this example
public class StudentJson {
    private Long id;
    @JsonProperty("first_Name") //This line is included when json key name is different with bean name
    private String firstName;
    //private String lastName;
    private String email;
}

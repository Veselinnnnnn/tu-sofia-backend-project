package com.universityproject.backendproject.model.dto.animal.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
@Getter
@Setter
public class AnimalRequest {
    private String name;
    private String type;
    private String breed;
    private int age;
    private String description;
    private String slogan;
}

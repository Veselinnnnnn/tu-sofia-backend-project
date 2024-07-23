package com.universityproject.backendproject.model.dto.animal.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class AnimalResponse {

    private Long id;

    private String name;

    private String type;

    private boolean availability;

    private Long userId;
}

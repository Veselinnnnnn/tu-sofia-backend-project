package com.universityproject.backendproject.model.dto.animal.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class AnimalAvailableResponse {

    private Long id;

    private boolean availability;

    private Long userId;
}

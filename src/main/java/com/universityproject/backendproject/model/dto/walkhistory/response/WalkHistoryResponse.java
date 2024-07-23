package com.universityproject.backendproject.model.dto.walkhistory.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class WalkHistoryResponse {

    private String animalName;

    private String animalType;

    private String localDate;
}

package com.universityproject.backendproject.model.dto.user.response;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UserFirstAndLastNameResponse {

    private String firstName;
    private String lastName;
}

package com.universityproject.backendproject.model.dto.user.request;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UserBasicInfoRequest {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}

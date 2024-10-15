package com.universityproject.backendproject.model.dto.user.response;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UserBasicInfoResponse {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private String title;
    private byte[] img;
}

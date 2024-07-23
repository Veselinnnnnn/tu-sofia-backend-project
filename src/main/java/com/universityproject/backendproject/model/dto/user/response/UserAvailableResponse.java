package com.universityproject.backendproject.model.dto.user.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UserAvailableResponse {

    private Long id;

    private String username;
}

package com.universityproject.backendproject.service.user;


import com.universityproject.backendproject.model.dto.user.response.UserAvailableResponse;
import com.universityproject.backendproject.model.dto.user.response.UserIdResponse;
import com.universityproject.backendproject.model.entity.User;

import java.util.List;

public interface UserService {

    User findUserById(Long id);

    UserIdResponse findById(Long id);

    List<UserAvailableResponse> findAllAvailable();

    List<UserAvailableResponse> findAllUsers();

    void takeOnWalk(Long userId, Long animalId) throws Exception;

    void returnFromWalk(Long userId, Long animalId);
}

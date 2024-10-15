package com.universityproject.backendproject.service.user;


import com.universityproject.backendproject.model.dto.user.request.UserAdvancedInfoRequest;
import com.universityproject.backendproject.model.dto.user.request.UserBasicInfoRequest;
import com.universityproject.backendproject.model.dto.user.response.*;
import com.universityproject.backendproject.model.entity.AdvancedUserDetails;
import com.universityproject.backendproject.model.entity.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface UserService {
    UserBasicInfoResponse getBasicUserInfoById(Long id);

    UserAdvancedInfoResponse getAdvancedUserInfoById(Long id);

    byte[] getUserProfileImageById(@RequestParam Long id);

    void updateBasicUserInfo(UserBasicInfoRequest request);

    void updateAdvancedUserInfo(UserAdvancedInfoRequest request);

    void uploadUserProfileImage(Long userId, MultipartFile image);

    UserFirstAndLastNameResponse getFirstAndLastName(Long id);

    List<UserBasicInfoResponse> getRandomUsers();

    User findUserById(Long id);

    UserIdResponse findById(Long id);

    List<UserAvailableResponse> findAllAvailable();

    List<UserAvailableResponse> findAllUsers();

    void takeOnWalk(Long userId, Long animalId) throws Exception;

    void returnFromWalk(Long userId, Long animalId);

    List<UserBasicInfoResponse> getAllMembers();
}

package com.universityproject.backendproject.controller;

import com.universityproject.backendproject.model.dto.user.request.UserAdvancedInfoRequest;
import com.universityproject.backendproject.model.dto.user.request.UserBasicInfoRequest;
import com.universityproject.backendproject.model.dto.user.response.UserAdvancedInfoResponse;
import com.universityproject.backendproject.model.dto.user.response.UserBasicInfoResponse;
import com.universityproject.backendproject.model.dto.user.response.UserFirstAndLastNameResponse;
import com.universityproject.backendproject.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/members")
    public ResponseEntity<List<UserBasicInfoResponse>> getAllMembers() {
        List<UserBasicInfoResponse> members = userService.getAllMembers();
        return new ResponseEntity<>(members, HttpStatus.OK);
    }

    @GetMapping("/basic-info")
    public ResponseEntity<UserBasicInfoResponse> getBasicUserInfoById(@RequestParam Long id) {
        UserBasicInfoResponse userInfo = this.userService.getBasicUserInfoById(id);
        return new ResponseEntity<>(userInfo, HttpStatus.OK);
    }

    @PutMapping("/basic-info")
    public ResponseEntity<Void> updateBasicUserInfo(@RequestBody UserBasicInfoRequest request) {
        this.userService.updateBasicUserInfo(request);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/advanced-info")
    public ResponseEntity<UserAdvancedInfoResponse> getAdvancedUserInfoById(@RequestParam Long id) {
        UserAdvancedInfoResponse userInfo = this.userService.getAdvancedUserInfoById(id);
        return new ResponseEntity<>(userInfo, HttpStatus.OK);
    }

    @PutMapping("/advanced-info")
    public ResponseEntity<Void> updateAdvancedUserInfo(@RequestBody UserAdvancedInfoRequest request) {
        this.userService.updateAdvancedUserInfo(request);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/user-image")
    public ResponseEntity<byte[]> getUserProfileImageById(@RequestParam Long id) {
        byte[] image = this.userService.getUserProfileImageById(id);
        return new ResponseEntity<>(image, HttpStatus.OK);
    }

    @PutMapping("/user-image")
    public ResponseEntity<Void> uploadUserProfileImage(@RequestParam Long id, @RequestPart MultipartFile image) {
        this.userService.uploadUserProfileImage(id, image);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/first-last-name")
    public ResponseEntity<UserFirstAndLastNameResponse> getFirstAndLastName(@RequestParam Long id) {
        UserFirstAndLastNameResponse nameResponse = this.userService.getFirstAndLastName(id);
        return new ResponseEntity<>(nameResponse, HttpStatus.OK);
    }

    @GetMapping("/random")
    public ResponseEntity<List<UserBasicInfoResponse>> getRandomUsers() {
        List<UserBasicInfoResponse> randomUsers = this.userService.getRandomUsers();
        return new ResponseEntity<>(randomUsers, HttpStatus.OK);
    }
}

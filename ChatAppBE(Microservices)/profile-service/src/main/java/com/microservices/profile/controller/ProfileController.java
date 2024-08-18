package com.microservices.profile.controller;

import com.microservices.profile.dto.request.ProfileRequest;
import com.microservices.profile.dto.response.ResponseData;
import com.microservices.profile.dto.response.ResponseError;
import com.microservices.profile.service.impl.ProfileServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileServiceImpl profileService;

    @PostMapping("/internal/new")
    public ResponseData<?> createNewProfile(@RequestBody ProfileRequest request) {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "New profile",
                    profileService.createNewProfile(request));
        } catch (Exception e) {
            return new ResponseError<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseData<?> updateProfile(@RequestBody ProfileRequest request) {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "Update profile",
                    profileService.updateProfile(request));
        } catch (Exception e) {
            return new ResponseError<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @GetMapping("/get-profile")
    public ResponseData<?> getProfile() {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "Get profile", profileService.getOneProfile());
        } catch (Exception e) {
            return new ResponseError<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @GetMapping("/get-profile/{userId}")
    public ResponseData<?> getProfileById(@PathVariable Long userId) {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "Get profile", profileService.getProfileById(userId));
        } catch (Exception e) {
            return new ResponseError<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
    
    @GetMapping("/get-profiles-username/{username}")
    public ResponseData<?> getProfilesByUsername(@PathVariable String username) {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "Get profiles by username",
                    profileService.getListProfilesByUsername(username));
        } catch (Exception e) {
            return new ResponseError<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @GetMapping("/all-profiles")
    public ResponseData<?> getAllProfiles() {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "Get all profiles",
                    profileService.getAllProfiles());
        } catch (Exception e) {
            return new ResponseError<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
}

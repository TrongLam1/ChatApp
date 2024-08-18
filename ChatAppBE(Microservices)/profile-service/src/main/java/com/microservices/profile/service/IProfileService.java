package com.microservices.profile.service;

import com.microservices.profile.dto.request.ProfileRequest;
import com.microservices.profile.dto.response.ProfileResponse;

import java.util.List;

public interface IProfileService {

    ProfileResponse createNewProfile(ProfileRequest request);

    ProfileResponse updateProfile(ProfileRequest request);

    ProfileResponse getOneProfile();

    ProfileResponse getProfileById(Long userId);

    List<ProfileResponse> getListProfilesByUsername(String username);

    List<ProfileResponse> getAllProfiles();
}

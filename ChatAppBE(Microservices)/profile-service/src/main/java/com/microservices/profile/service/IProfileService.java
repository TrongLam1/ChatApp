package com.microservices.profile.service;

import com.microservices.profile.dto.request.ProfileRequest;
import com.microservices.profile.dto.response.ProfileResponse;

public interface IProfileService {

    ProfileResponse createNewProfile(ProfileRequest request);

    ProfileResponse updateProfile(ProfileRequest request);

    ProfileResponse getOneProfile();
}

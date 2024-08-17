package com.microservices.profile.service.impl;

import com.microservices.profile.dto.request.ProfileRequest;
import com.microservices.profile.dto.response.ProfileResponse;
import com.microservices.profile.entity.Profile;
import com.microservices.profile.exception.ProfileException;
import com.microservices.profile.repository.ProfileRepository;
import com.microservices.profile.service.IProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileServiceImpl implements IProfileService {

    private final ProfileRepository profileRepository;

    private final ModelMapper mapper;

    @Override
    public ProfileResponse createNewProfile(ProfileRequest request) {
        try {
            log.info("Create new profile email {}", request.getEmail());
            Profile profile = Profile.builder()
                    .userId(request.getUserId())
                    .email(request.getEmail())
                    .username(request.getUsername())
                    .createdDate(LocalDateTime.now())
                    .modifiedDate(LocalDateTime.now())
                    .build();

            profile = profileRepository.save(profile);

            return mapper.map(profile, ProfileResponse.class);
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    @Override
    public ProfileResponse updateProfile(ProfileRequest request) {
        try {
            log.info("Update profile email {}", request.getEmail());
            Profile profile = profileRepository.findByUserId(request.getUserId())
                    .orElseThrow(() -> new ProfileException("Not found profile"));

            profile.setUsername(request.getUsername());
            profile.setModifiedDate(LocalDateTime.now());

            profile = profileRepository.save(profile);

            return mapper.map(profile, ProfileResponse.class);
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    @Override
    public ProfileResponse getOneProfile() {
        return null;
    }
}

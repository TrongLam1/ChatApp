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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
            Profile profile = findProfileByUserId(request.getUserId());

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
        try {
            Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
            Profile profileUser = findProfileByUserId(userId);
            return mapper.map(profileUser, ProfileResponse.class);
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    @Override
    public ProfileResponse getProfileById(Long userId) {
        try {
            Profile profileUser = findProfileByUserId(userId);
            return mapper.map(profileUser, ProfileResponse.class);
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    @Override
    public List<ProfileResponse> getListProfilesByUsername(String username) {
        try {
            log.info("Get profiles {}", username);
            return profileRepository.findByUsernameContaining(username)
                    .stream().map(profile -> mapper.map(profile, ProfileResponse.class)).toList();
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public List<ProfileResponse> getAllProfiles() {
        try {
            log.info("Get all profiles");
            return profileRepository.findAll()
                    .stream().map(profile -> mapper.map(profile, ProfileResponse.class)).toList();
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    private Profile findProfileByUserId(Long userId) throws ProfileException {
        return profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ProfileException("Not found profile"));
    }
}
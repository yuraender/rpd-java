package com.example.demo.service;

import com.example.demo.entity.Profile;
import com.example.demo.repository.ProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileService {
    private final ProfileRepository profileRepository;

    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public List<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }

    public Profile getById(Long id) {
        return profileRepository.findById(id).orElse(null);
    }
}

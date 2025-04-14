package com.example.demo.service;

import com.example.demo.entity.Profile;
import com.example.demo.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;

    public List<Profile> getAll() {
        return profileRepository.findAllByDisabledFalse()
                .stream()
                .filter(p -> !p.isDisabled())
                .toList();
    }

    public Profile getById(Integer id) {
        return profileRepository.findByIdAndDisabledFalse(id)
                .filter(p -> !p.isDisabled())
                .orElse(null);
    }

    public boolean existsByName(Integer id, String name) {
        return profileRepository.findAllByNameAndDisabledFalse(name)
                .stream()
                .filter(p -> !p.isDisabled())
                .anyMatch(p -> id == null || p.getId() != id);
    }

    public Profile save(Profile profile) {
        return profileRepository.save(profile);
    }
}

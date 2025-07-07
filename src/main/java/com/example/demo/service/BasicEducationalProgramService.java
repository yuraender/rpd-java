package com.example.demo.service;

import com.example.demo.entity.BasicEducationalProgram;
import com.example.demo.entity.EducationType;
import com.example.demo.entity.Profile;
import com.example.demo.repository.BasicEducationalProgramRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BasicEducationalProgramService {

    private final BasicEducationalProgramRepository basicEducationalProgramRepository;

    public List<BasicEducationalProgram> getAll() {
        return basicEducationalProgramRepository.findAllByDisabledFalse()
                .stream()
                .filter(bep -> !bep.isDisabled())
                .sorted(Comparator.comparing(BasicEducationalProgram::getId))
                .toList();
    }

    public BasicEducationalProgram getById(Integer id) {
        return basicEducationalProgramRepository.findByIdAndDisabledFalse(id)
                .filter(bep -> !bep.isDisabled())
                .orElse(null);
    }

    public boolean existsByAcademicYearAndProfileIdAndEducationType(
            Integer id, int academicYear, Profile profile, EducationType educationType
    ) {
        return basicEducationalProgramRepository
                .findAllByAcademicYearAndProfileAndEducationTypeAndDisabledFalse(academicYear, profile, educationType)
                .stream()
                .filter(bep -> !bep.isDisabled())
                .anyMatch(bep -> id == null || bep.getId() != id);
    }

    public BasicEducationalProgram save(BasicEducationalProgram basicEducationalProgram) {
        return basicEducationalProgramRepository.save(basicEducationalProgram);
    }
}

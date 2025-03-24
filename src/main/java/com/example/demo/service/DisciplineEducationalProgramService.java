package com.example.demo.service;

import com.example.demo.entity.DisciplineEducationalProgram;
import com.example.demo.repository.DisciplineEducationalProgramRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DisciplineEducationalProgramService {

    private final DisciplineEducationalProgramRepository disciplineEducationalProgramRepository;

    public List<DisciplineEducationalProgram> getAll() {
        return disciplineEducationalProgramRepository.findAllByDisabledFalse()
                .stream()
                .filter(dep -> !dep.isDisabled())
                .toList();
    }

    public DisciplineEducationalProgram getById(Integer id) {
        return disciplineEducationalProgramRepository.findByIdAndDisabledFalse(id)
                .filter(dep -> !dep.isDisabled())
                .orElse(null);
    }

    public DisciplineEducationalProgram save(DisciplineEducationalProgram disciplineEducationalProgram) {
        return disciplineEducationalProgramRepository.save(disciplineEducationalProgram);
    }
}

package com.example.demo.service;

import com.example.demo.entity.BasicEducationalProgram;
import com.example.demo.entity.BasicEducationalProgramDiscipline;
import com.example.demo.entity.Discipline;
import com.example.demo.repository.BasicEducationalProgramDisciplineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BasicEducationalProgramDisciplineService {

    private final BasicEducationalProgramDisciplineRepository basicEducationalProgramDisciplineRepository;

    public List<BasicEducationalProgramDiscipline> getAll() {
        return basicEducationalProgramDisciplineRepository.findAllByDisabledFalse()
                .stream()
                .filter(bepd -> !bepd.isDisabled())
                .sorted(Comparator.comparing(BasicEducationalProgramDiscipline::getId))
                .toList();
    }

    public BasicEducationalProgramDiscipline getById(Integer id) {
        return basicEducationalProgramDisciplineRepository.findByIdAndDisabledFalse(id)
                .filter(bepd -> !bepd.isDisabled())
                .orElse(null);
    }

    public boolean existsByIndexAndBasicEducationalProgram(
            Integer id, String index, BasicEducationalProgram bep
    ) {
        return basicEducationalProgramDisciplineRepository.findAllByIndexAndBasicEducationalProgram(index, bep)
                .stream()
                .filter(bepd -> !bepd.isDisabled())
                .anyMatch(bepd -> id == null || bepd.getId() != id);
    }

    public boolean existsByBasicEducationalProgramAndDiscipline(
            Integer id, BasicEducationalProgram bep, Discipline discipline
    ) {
        return basicEducationalProgramDisciplineRepository.findAllByBasicEducationalProgramAndDiscipline(bep, discipline)
                .stream()
                .filter(bepd -> !bepd.isDisabled())
                .anyMatch(bepd -> id == null || bepd.getId() != id);
    }

    public BasicEducationalProgramDiscipline save(BasicEducationalProgramDiscipline basicEducationalProgramDiscipline) {
        return basicEducationalProgramDisciplineRepository.save(basicEducationalProgramDiscipline);
    }
}

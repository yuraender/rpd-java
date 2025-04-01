package com.example.demo.service;

import com.example.demo.entity.BasicEducationalProgramDiscipline;
import com.example.demo.repository.BasicEducationalProgramDisciplineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BasicEducationalProgramDisciplineService {

    private final BasicEducationalProgramDisciplineRepository basicEducationalProgramDisciplineRepository;

    public List<BasicEducationalProgramDiscipline> getAll() {
        return basicEducationalProgramDisciplineRepository.findAllByDisabledFalse()
                .stream()
                .filter(bepd -> !bepd.isDisabled())
                .toList();
    }

    public BasicEducationalProgramDiscipline getById(Integer id) {
        return basicEducationalProgramDisciplineRepository.findByIdAndDisabledFalse(id)
                .filter(bepd -> !bepd.isDisabled())
                .orElse(null);
    }

    public BasicEducationalProgramDiscipline save(BasicEducationalProgramDiscipline basicEducationalProgramDiscipline) {
        return basicEducationalProgramDisciplineRepository.save(basicEducationalProgramDiscipline);
    }
}

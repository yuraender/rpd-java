package com.example.demo.service;

import com.example.demo.entity.BasicEducationalProgram;
import com.example.demo.repository.BasicEducationalProgramRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BasicEducationalProgramService {

    private final BasicEducationalProgramRepository basicEducationalProgramRepository;

    public List<BasicEducationalProgram> getAll() {
        return basicEducationalProgramRepository.findAllByDisabledFalse()
                .stream()
                .filter(bep -> !bep.isDisabled())
                .toList();
    }

    public BasicEducationalProgram getById(Integer id) {
        return basicEducationalProgramRepository.findByIdAndDisabledFalse(id)
                .filter(bep -> !bep.isDisabled())
                .orElse(null);
    }

    public BasicEducationalProgram save(BasicEducationalProgram basicEducationalProgram) {
        return basicEducationalProgramRepository.save(basicEducationalProgram);
    }
}

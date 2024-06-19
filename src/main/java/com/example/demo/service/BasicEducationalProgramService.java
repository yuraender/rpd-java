package com.example.demo.service;

import com.example.demo.entity.BasicEducationalProgram;
import com.example.demo.repository.BasicEducationalProgramRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class BasicEducationalProgramService {
    private final BasicEducationalProgramRepository basicEducationalProgramRepository;

    public BasicEducationalProgramService(BasicEducationalProgramRepository basicEducationalProgramRepository) {
        this.basicEducationalProgramRepository = basicEducationalProgramRepository;
    }

    public List<BasicEducationalProgram> getAll() {
        return basicEducationalProgramRepository.findAll();
    }

    public BasicEducationalProgram getById(Long id) {
        return basicEducationalProgramRepository.findById(id).orElse(null);
    }
}

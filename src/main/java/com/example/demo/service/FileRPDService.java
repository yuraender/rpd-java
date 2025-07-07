package com.example.demo.service;

import com.example.demo.entity.BasicEducationalProgramDiscipline;
import com.example.demo.entity.FileRPD;
import com.example.demo.repository.FileRPDRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileRPDService {

    private final FileRPDRepository fileRPDRepository;

    public List<FileRPD> getAll() {
        return fileRPDRepository.findAllByDisabledFalse()
                .stream()
                .filter(f -> !f.isDisabled())
                .sorted(Comparator.comparing(FileRPD::getId))
                .toList();
    }

    public FileRPD getById(Integer id) {
        return fileRPDRepository.findByIdAndDisabledFalse(id)
                .filter(f -> !f.isDisabled())
                .orElse(null);
    }

    public FileRPD getByAcademicYearAndBasicEducationalProgramDiscipline(
            int academicYear, BasicEducationalProgramDiscipline bepDiscipline
    ) {
        return fileRPDRepository
                .findByAcademicYearAndBasicEducationalProgramDisciplineAndDisabledFalse(academicYear, bepDiscipline)
                .filter(f -> !f.isDisabled())
                .orElse(null);
    }

    public FileRPD save(FileRPD fileRPD) {
        return fileRPDRepository.save(fileRPD);
    }
}

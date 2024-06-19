package com.example.demo.service;

import com.example.demo.entity.FileRPD;
import com.example.demo.repository.FileRPDRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileRPDService {
    private final FileRPDRepository fileRPDRepository;

    public FileRPDService(FileRPDRepository fileRPDRepository) {
        this.fileRPDRepository = fileRPDRepository;
    }

    public List<FileRPD> getAll() {
        return fileRPDRepository.findAll();
    }

    public FileRPD getById(Long id) {
        return fileRPDRepository.findById(id).orElse(null);
    }
}

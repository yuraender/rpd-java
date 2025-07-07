package com.example.demo.service;

import com.example.demo.entity.Discipline;
import com.example.demo.repository.DisciplineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DisciplineService {

    private final DisciplineRepository disciplineRepository;

    public List<Discipline> getAll() {
        return disciplineRepository.findAllByDisabledFalse();
    }

    public Discipline getById(Integer id) {
        return disciplineRepository.findByIdAndDisabledFalse(id).orElse(null);
    }

    public boolean existsByName(Integer id, String name) {
        return disciplineRepository.findAllByNameAndDisabledFalse(name)
                .stream()
                .anyMatch(d -> id == null || d.getId() != id);
    }

    public Discipline save(Discipline discipline) {
        return disciplineRepository.save(discipline);
    }
}

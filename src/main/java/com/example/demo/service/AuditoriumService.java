package com.example.demo.service;

import com.example.demo.entity.Auditorium;
import com.example.demo.repository.AuditoriumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditoriumService {

    private final AuditoriumRepository auditoriumRepository;

    public List<Auditorium> getAll() {
        return auditoriumRepository.findAllByDisabledFalse();
    }

    public Auditorium getById(Integer id) {
        return auditoriumRepository.findByIdAndDisabledFalse(id).orElse(null);
    }

    public boolean existsByAuditoriumNumber(Integer id, String auditoriumNumber) {
        return auditoriumRepository.findAllByAuditoriumNumberAndDisabledFalse(auditoriumNumber)
                .stream()
                .anyMatch(a -> id == null || a.getId() != id);
    }

    public Auditorium save(Auditorium auditorium) {
        return auditoriumRepository.save(auditorium);
    }
}

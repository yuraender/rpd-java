package com.example.demo.service;

import com.example.demo.entity.Indicator;
import com.example.demo.repository.IndicatorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IndicatorService {

    private final IndicatorRepository indicatorRepository;

    public List<Indicator> getAll() {
        return indicatorRepository.findAllByDisabledFalse()
                .stream()
                .filter(i -> !i.isDisabled())
                .sorted(Comparator.comparing(Indicator::getId))
                .toList();
    }

    public Indicator getById(Integer id) {
        return indicatorRepository.findByIdAndDisabledFalse(id)
                .filter(i -> !i.isDisabled())
                .orElse(null);
    }

    public boolean existsByText(Integer id, String text) {
        return indicatorRepository.findAllByTextAndDisabledFalse(text)
                .stream()
                .filter(i -> !i.isDisabled())
                .anyMatch(i -> id == null || i.getId() != id);
    }

    public Indicator save(Indicator indicator) {
        return indicatorRepository.save(indicator);
    }
}

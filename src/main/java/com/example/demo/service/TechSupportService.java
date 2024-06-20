package com.example.demo.service;

import com.example.demo.entity.TechSupport;
import com.example.demo.repository.TechSupportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TechSupportService {

    @Autowired
    private TechSupportRepository techSupportRepository;

    public List<TechSupport> getAll() {
        return techSupportRepository.findAll();
    }

    public List<TechSupport> getByDepartmentId(Long departmentId) {
        // Получаем все записи TechSupport
        List<TechSupport> allTechSupports = techSupportRepository.findAll();

        // Фильтруем по departmentId
        List<TechSupport> filteredTechSupports = allTechSupports.stream()
                .filter(techSupport -> Long.valueOf(techSupport.getDiscipline().getDepartment().getId()).equals(departmentId))
                .collect(Collectors.toList());

        return filteredTechSupports;
    }
    public List<TechSupport> getByTeacherId(Long departmentId, Long teacherId) {
        // Получаем все записи TechSupport
        List<TechSupport> allTechSupports = techSupportRepository.findAll();

        // Фильтруем по departmentId и teacherId
        List<TechSupport> filteredTechSupports = allTechSupports.stream()
                .filter(techSupport -> Long.valueOf(techSupport.getDiscipline().getDepartment().getId()).equals(departmentId))
                .filter(techSupport -> Long.valueOf(techSupport.getDiscipline().getDeveloper().getId()).equals(teacherId))
                .collect(Collectors.toList());

        return filteredTechSupports;
    }

    public List<TechSupport> getByDisciplineId(Long departmentId, Long teacherId, Long disciplineId) {
        // Получаем все записи TechSupport
        List<TechSupport> allTechSupports = techSupportRepository.findAll();

        // Фильтруем по departmentId и teacherId
        List<TechSupport> filteredTechSupports = allTechSupports.stream()
                .filter(techSupport -> Long.valueOf(techSupport.getDiscipline().getDepartment().getId()).equals(departmentId))
                .filter(techSupport -> Long.valueOf(techSupport.getDiscipline().getDeveloper().getId()).equals(teacherId))
                .filter(techSupport -> Long.valueOf(techSupport.getDiscipline().getId()).equals(disciplineId))
                .collect(Collectors.toList());

        return filteredTechSupports;
    }
    public TechSupport getById(Long id) {
        return techSupportRepository.findById(id).orElse(null);
    }
    public TechSupport save(TechSupport techSupport) {
        return techSupportRepository.save(techSupport);
    }
}

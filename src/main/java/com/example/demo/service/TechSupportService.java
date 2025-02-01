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
        return techSupportRepository.findAll().stream()
                .filter(techSupport -> !techSupport.isDisabled())
                .collect(Collectors.toList());
    }

    public List<TechSupport> getByDepartmentId(Integer departmentId) {
        // Получаем все записи TechSupport
        List<TechSupport> allTechSupports = techSupportRepository.findAll();

        // Фильтруем по departmentId
        List<TechSupport> filteredTechSupports = allTechSupports.stream()
                .filter(techSupport -> techSupport.getDiscipline().getDepartment().getId() == departmentId)
                .filter(techSupport -> !techSupport.isDisabled())
                .collect(Collectors.toList());

        return filteredTechSupports;
    }

    public List<TechSupport> getByTeacherId(Integer departmentId, Integer teacherId) {
        // Получаем все записи TechSupport
        List<TechSupport> allTechSupports = techSupportRepository.findAll();

        // Фильтруем по departmentId и teacherId
        List<TechSupport> filteredTechSupports = allTechSupports.stream()
                .filter(techSupport -> techSupport.getDiscipline().getDepartment().getId() == departmentId)
                .filter(techSupport -> techSupport.getDiscipline().getDeveloper().getId() == teacherId)
                .filter(techSupport -> !techSupport.isDisabled())
                .collect(Collectors.toList());

        return filteredTechSupports;
    }

    public List<TechSupport> getByDisciplineId(Integer departmentId, Integer teacherId, Integer disciplineId) {
        // Получаем все записи TechSupport
        List<TechSupport> allTechSupports = techSupportRepository.findAll();

        // Фильтруем по departmentId и teacherId
        List<TechSupport> filteredTechSupports = allTechSupports.stream()
                .filter(techSupport -> techSupport.getDiscipline().getDepartment().getId() == departmentId)
                .filter(techSupport -> techSupport.getDiscipline().getDeveloper().getId() == teacherId)
                .filter(techSupport -> techSupport.getDiscipline().getId() == disciplineId)
                .filter(techSupport -> !techSupport.isDisabled())
                .collect(Collectors.toList());

        return filteredTechSupports;
    }

    public List<TechSupport> findByAudienceAndDiscipline(Integer audienceId, Integer disciplineId) {
        // Фильтруем по departmentId и teacherId
        List<TechSupport> filteredTechSupports = techSupportRepository.findAll().stream()
                .filter(techSupport -> techSupport.getAudience().getId() == audienceId)
                .filter(techSupport -> techSupport.getDiscipline().getId() == disciplineId)
                .filter(techSupport -> !techSupport.isDisabled())
                .collect(Collectors.toList());

        return filteredTechSupports;
    }

    public TechSupport getById(Integer id) {
        return techSupportRepository.findById(id).orElse(null);
    }

    public TechSupport save(TechSupport techSupport) {
        return techSupportRepository.save(techSupport);
    }
}

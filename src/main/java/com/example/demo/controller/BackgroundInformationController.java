package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BackgroundInformationController {

    private final DepartmentService departmentService;
    private final DirectionService directionService;
    private final ProfileService profileService;
    private final BasicEducationalProgramService basicEducationalProgramService;
    private final BasicEducationalProgramDisciplineService basicEducationalProgramDisciplineService;
    private final FileRPDService fileRPDService;

    @GetMapping("/background-information")
    public String getBackgroundInformation(
            @SessionAttribute(name = "departmentId", required = false) Integer departmentId,
            @SessionAttribute(name = "directionId", required = false) Integer directionId,
            @SessionAttribute(name = "profileId", required = false) Integer profileId,
            @SessionAttribute(name = "bepId", required = false) Integer bepId,
            @SessionAttribute(name = "bepDisciplineId", required = false) Integer bepDisciplineId,
            Model model
    ) {
        // Получаем все кафедры
        List<Department> departments = departmentService.getAll();
        model.addAttribute("departments", departments);
        // Устанавливаем активную кафедру, если она есть
        if (departmentId != null) {
            Department activeDepartment = departmentService.getById(departmentId);
            model.addAttribute("activeDepartment", activeDepartment);
        }

        // Получаем все направления
        List<Direction> directions = directionService.getAll()
                .stream()
                .filter(d -> departmentId == null || d.getDepartment().getId() == departmentId)
                .toList();
        model.addAttribute("directions", directions);
        // Устанавливаем активное направление, если оно есть
        if (directionId != null) {
            Direction activeDirection = directionService.getById(directionId);
            model.addAttribute("activeDirection", activeDirection);
        }

        // Получаем все профили
        List<Profile> profiles = profileService.getAll()
                .stream()
                .filter(p -> departmentId == null || p.getDirection().getDepartment().getId() == departmentId)
                .filter(p -> directionId == null || p.getDirection().getId() == directionId)
                .toList();
        model.addAttribute("profiles", profiles);
        // Устанавливаем активный профиль, если он есть
        if (profileId != null) {
            Profile activeProfile = profileService.getById(profileId);
            model.addAttribute("activeProfile", activeProfile);
        }

        // Получаем все ООП
        List<BasicEducationalProgram> basicEducationalPrograms = basicEducationalProgramService.getAll()
                .stream()
                .filter(bep -> departmentId == null || bep.getProfile().getDirection().getDepartment().getId() == departmentId)
                .filter(bep -> directionId == null || bep.getProfile().getDirection().getId() == directionId)
                .filter(bep -> profileId == null || bep.getProfile().getId() == profileId)
                .toList();
        model.addAttribute("basicEducationalPrograms", basicEducationalPrograms);
        // Устанавливаем активную ООП, если она есть
        if (bepId != null) {
            BasicEducationalProgram activeBasicEducationalProgram = basicEducationalProgramService.getById(bepId);
            model.addAttribute("activeBasicEducationalProgram", activeBasicEducationalProgram);
        }

        // Получаем все дисциплины ОП
        List<BasicEducationalProgramDiscipline> bepDisciplines = basicEducationalProgramDisciplineService.getAll()
                .stream()
                .filter(bepd -> departmentId == null
                        || bepd.getBasicEducationalProgram().getProfile().getDirection().getDepartment().getId() == departmentId)
                .filter(bepd -> directionId == null
                        || bepd.getBasicEducationalProgram().getProfile().getDirection().getId() == directionId)
                .filter(bepd -> profileId == null || bepd.getBasicEducationalProgram().getProfile().getId() == profileId)
                .filter(bepd -> bepId == null || bepd.getBasicEducationalProgram().getId() == bepId)
                .toList();
        model.addAttribute("bepDisciplines", bepDisciplines);
        // Устанавливаем активную дисциплину ОП, если она есть
        if (bepDisciplineId != null) {
            BasicEducationalProgramDiscipline activeBepDiscipline = basicEducationalProgramDisciplineService.getById(bepDisciplineId);
            model.addAttribute("activeBepDiscipline", activeBepDiscipline);
        }

        // Получаем все РПД
        List<FileRPD> filesRPD = fileRPDService.getAll()
                .stream()
                .filter(rpd -> departmentId == null
                        || rpd.getBasicEducationalProgramDiscipline().getBasicEducationalProgram().getProfile().getDirection().getDepartment().getId() == departmentId)
                .filter(rpd -> directionId == null
                        || rpd.getBasicEducationalProgramDiscipline().getBasicEducationalProgram().getProfile().getDirection().getId() == directionId)
                .filter(rpd -> profileId == null
                        || rpd.getBasicEducationalProgramDiscipline().getBasicEducationalProgram().getProfile().getId() == profileId)
                .filter(rpd -> bepId == null || rpd.getBasicEducationalProgramDiscipline().getBasicEducationalProgram().getId() == bepId)
                .filter(rpd -> bepDisciplineId == null || rpd.getBasicEducationalProgramDiscipline().getId() == bepDisciplineId)
                .toList();
        model.addAttribute("filesRPD", filesRPD);

        return "background-information";
    }
}

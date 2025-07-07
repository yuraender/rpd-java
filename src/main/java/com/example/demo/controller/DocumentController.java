package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.service.BasicEducationalProgramDisciplineService;
import com.example.demo.service.DocumentService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("/api/document")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;
    private final BasicEducationalProgramDisciplineService basicEducationalProgramDisciplineService;

    @PostMapping("/generate")
    public ResponseEntity<byte[]> generate(@RequestBody Map<String, Object> payload) throws IOException {
        Integer bepId = (Integer) payload.get("bepId");
        ObjectMapper mapper = new ObjectMapper();
        List<BasicEducationalProgramDiscipline> bepDisciplines = ((List<Integer>) mapper
                .convertValue(payload.get("bepDisciplines"), new TypeReference<>() {
                }))
                .stream()
                .map(basicEducationalProgramDisciplineService::getById)
                .filter(bepd -> bepd.getBasicEducationalProgram().getId() == bepId)
                .filter(bepd -> !bepd.isDisabled())
                .toList();
        SimpleDateFormat fullSdf = new SimpleDateFormat("«dd» MMMM yyyy г.", new Locale("ru"));
        SimpleDateFormat shortSdf = new SimpleDateFormat("dd.MM.yyyy г.");

        List<File> folders = new ArrayList<>();
        byte[] zipContent = null;
        HttpHeaders headers = new HttpHeaders();

        if (!bepDisciplines.isEmpty()) {
            for (BasicEducationalProgramDiscipline bepDiscipline : bepDisciplines) {
                Protocol.Type type = bepDiscipline.getProtocols().isEmpty()
                        ? Protocol.Type.APPROVE
                        : Protocol.Type.ACTUALIZE;
                Protocol protocol;
                if (type == Protocol.Type.APPROVE) {
                    protocol = bepDiscipline.getBasicEducationalProgram().getProtocol();
                } else {
                    protocol = bepDiscipline.getProtocols().get(bepDiscipline.getProtocols().size() - 1);
                }
                Protocol approveProtocol = bepDiscipline.getBasicEducationalProgram().getProtocol();

                Map<String, Object> data = new HashMap<>();
                data.put("type", type);
                data.put("protocolNumber", protocol.getNumber2());
                data.put("protocolDate", fullSdf.format(protocol.getDate2()));
                data.put("disciplineName", bepDiscipline.getDiscipline().getName());
                data.put("directionCode", bepDiscipline.getBasicEducationalProgram().getProfile().getDirection().getCode());
                data.put("directionName", bepDiscipline.getBasicEducationalProgram().getProfile().getDirection().getName());
                data.put("profileName", bepDiscipline.getBasicEducationalProgram().getProfile().getName());
                data.put("educationTypeNameLower", bepDiscipline.getBasicEducationalProgram().getEducationType().getName().toLowerCase());
                data.put("year", protocol.getDate2().toLocalDate().getYear());

                List<Map<String, Object>> competences = new LinkedList<>();
                bepDiscipline.getIndicators()
                        .stream()
                        .map(Indicator::getCompetence)
                        .distinct()
                        .forEach(competence -> {
                            Map<String, Object> competenceData = new HashMap<>();
                            competenceData.put("competenceIndex", competence.getIndex());
                            competenceData.put("competenceEssence", competence.getEssence());
                            competenceData.put("competenceIndicators", bepDiscipline.getIndicators()
                                    .stream()
                                    .filter(i -> i.getCompetence() == competence)
                                    .sorted(Comparator.comparing(Indicator::getType))
                                    .toList());
                            competences.add(competenceData);
                        });
                data.put("competences", competences);

                data.put("educationTypeName", bepDiscipline.getBasicEducationalProgram().getEducationType().getName());

                data.put("evaluationCompetences", bepDiscipline.getIndicators()
                        .stream()
                        .map(Indicator::getCompetence)
                        .distinct()
                        .sorted(Comparator.comparing(Competence::getType).thenComparing(Competence::getId))
                        .map(c -> c.getEssence() + " (" + c.getIndex() + ")")
                        .collect(Collectors.joining(";\\n"))
                        + ".");
                data.put("evaluationIndicatorsKnow", bepDiscipline.getIndicators()
                        .stream()
                        .filter(i -> i.getType() == Indicator.Type.KNOW)
                        .sorted(Comparator
                                .comparing((Indicator i) -> i.getCompetence().getType())
                                .thenComparing(i -> i.getCompetence().getId())
                                .thenComparing(Indicator::getId))
                        .map(i -> i.getText() + " (" + i.getCompetence().getIndex() + ")")
                        .collect(Collectors.joining(";\\n"))
                        + ".");
                data.put("evaluationIndicatorsBeAble", bepDiscipline.getIndicators()
                        .stream()
                        .filter(i -> i.getType() == Indicator.Type.BE_ABLE)
                        .sorted(Comparator
                                .comparing((Indicator i) -> i.getCompetence().getType())
                                .thenComparing(i -> i.getCompetence().getId())
                                .thenComparing(Indicator::getId))
                        .map(i -> i.getText() + " (" + i.getCompetence().getIndex() + ")")
                        .collect(Collectors.joining(";\\n"))
                        + ".");
                data.put("evaluationIndicatorsOwn", bepDiscipline.getIndicators()
                        .stream()
                        .filter(i -> i.getType() == Indicator.Type.OWN)
                        .sorted(Comparator
                                .comparing((Indicator i) -> i.getCompetence().getType())
                                .thenComparing(i -> i.getCompetence().getId())
                                .thenComparing(Indicator::getId))
                        .map(i -> i.getText() + " (" + i.getCompetence().getIndex() + ")")
                        .collect(Collectors.joining(";\\n"))
                        + ".");

                List<Map<String, Object>> auditoriums = new LinkedList<>();
                for (Auditorium auditorium : bepDiscipline.getAuditoriums()) {
                    Map<String, Object> auditoriumData = new HashMap<>();
                    String auditoriumNumber;
                    try {
                        auditoriumNumber = Integer.parseInt(auditorium.getAuditoriumNumber()) + " Учебная аудитория";
                    } catch (NumberFormatException ignored) {
                        auditoriumNumber = auditorium.getAuditoriumNumber();
                    }
                    auditoriumData.put("auditoriumNumber", auditoriumNumber);
                    auditoriumData.put("auditoriumEquipment", auditorium.getEquipment());
                    auditoriumData.put("auditoriumSoftware", auditorium.getSoftware());
                    auditoriums.add(auditoriumData);
                }
                data.put("auditoriums", auditoriums);

                data.put("developerCount", approveProtocol.getDevelopers().size() > 1 ? "Разработчики" : "Разработчик");
                List<Map<String, Object>> developers = new LinkedList<>();
                for (Teacher developer : approveProtocol.getDevelopers()) {
                    Map<String, Object> developerData = new HashMap<>();
                    List<String> position = new LinkedList<>();
                    position.add(developer.getEmployeePosition().getName() + " кафедры " + developer.getDepartment().getAbbreviation());
                    if (developer.getAcademicDegree() != null) {
                        position.add(developer.getAcademicDegree().getName().toLowerCase());
                    }
                    if (developer.getAcademicRank() != null) {
                        position.add(developer.getAcademicRank().getName().toLowerCase());
                    }
                    developerData.put("developerPosition", String.join(", ", position));
                    developerData.put("developerName", developer.getEmployee().getNameTypeTwo());
                    developers.add(developerData);
                }
                data.put("developers", developers);
                data.put("departmentAbbreviation", bepDiscipline.getBasicEducationalProgram().getProfile().getDirection().getDepartment().getAbbreviation());
                data.put("approveProtocolNumber1", approveProtocol.getNumber1());
                data.put("approveProtocolShortDate1", shortSdf.format(approveProtocol.getDate1()));
                data.put("headName", bepDiscipline.getBasicEducationalProgram().getProfile().getDirection().getDepartment().getHead().getNameTypeTwo());
                data.put("approveProtocolNumber2", approveProtocol.getNumber2());
                data.put("approveProtocolShortDate2", shortSdf.format(approveProtocol.getDate2()));
                List<Map<String, Object>> actualizeProtocols = new LinkedList<>();
                for (Protocol actualizeProtocol : bepDiscipline.getProtocols()) {
                    Map<String, Object> protocolData = new HashMap<>();
                    int year = actualizeProtocol.getDate1().toLocalDate().getYear();
                    protocolData.put("actualizeYear", year + "-" + (year + 1));
                    protocolData.put("actualizeDeveloperCount",
                            actualizeProtocol.getDevelopers().size() > 1 ? "преподавателями" : "преподавателем");
                    protocolData.put("actualizeDevelopers", actualizeProtocol.getDevelopers()
                            .stream()
                            .map(d -> d.getEmployee().getNameTypeTwo())
                            .collect(Collectors.joining(", ")));
                    protocolData.put("actualizeProtocolNumber1", actualizeProtocol.getNumber1());
                    protocolData.put("actualizeProtocolDate1", shortSdf.format(actualizeProtocol.getDate1()));
                    protocolData.put("actualizeProtocolNumber2", actualizeProtocol.getNumber2());
                    protocolData.put("actualizeProtocolDate2", shortSdf.format(actualizeProtocol.getDate2()));
                    actualizeProtocols.add(protocolData);
                }
                data.put("actualizeProtocols", actualizeProtocols);

                FileRPD fileRPD = documentService.generate(data, bepDiscipline);

                String sanitizedPath = bepDiscipline.getDiscipline().getName().replaceAll("[\\\\/:*?\"<>|]", "_");

                File folder = new File("generated_documents/" + sanitizedPath);
                folder.mkdirs();

                saveDocumentPart(folder, "Титульный лист.docx", fileRPD.getSection0());
                saveDocumentPart(folder, "1. Цели освоения дисциплины.docx", fileRPD.getSection1());
                saveDocumentPart(folder, "2. Место дисциплины в составе ООП.docx", fileRPD.getSection2());
                saveDocumentPart(folder, "3. Компетенции обучающегося.docx", fileRPD.getSection3());
                saveDocumentPart(folder, "4. Структура и содержание дисциплины.docx", fileRPD.getSection4());
                saveDocumentPart(folder, "5. Образовательные технологии.docx", fileRPD.getSection5());
                saveDocumentPart(folder, "6. Оценочные средства.docx", fileRPD.getSection6());
                saveDocumentPart(folder, "7. Учебно-методическое и информационное обеспечение дисциплины.docx", fileRPD.getSection7());
                saveDocumentPart(folder, "8. Материально-техническое обеспечение дисциплины.docx", fileRPD.getSection8());
                saveDocumentPart(folder, "Подвал.docx", fileRPD.getSection9());
                folders.add(folder);
            }

            File zipFile = new File("generated_documents/all_documents.zip");
            try (
                    FileOutputStream fos = new FileOutputStream(zipFile);
                    ZipOutputStream zipOut = new ZipOutputStream(fos)
            ) {
                for (File folder : folders) {
                    zipFolder(folder, folder.getName(), zipOut);
                }
            }
            zipContent = Files.readAllBytes(zipFile.toPath());
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=all_documents.zip");

            // Удаление папки generated_documents после создания архива
            File generatedDocumentsFolder = new File("generated_documents");
            deleteFolderRecursively(generatedDocumentsFolder);
        }
        return new ResponseEntity<>(zipContent, headers, HttpStatus.OK);
    }

    private void saveDocumentPart(File folder, String fileName, byte[] content) throws IOException {
        String sanitizedFileName = fileName.replaceAll("[\\\\/:*?\"<>|]", "_");

        File file = new File(folder, sanitizedFileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(content);
        }
    }

    private void zipFolder(File folder, String parentFolder, ZipOutputStream zipOut) throws IOException {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    zipFolder(file, parentFolder + "/" + file.getName(), zipOut);
                } else {
                    try (FileInputStream fis = new FileInputStream(file)) {
                        String entryName = parentFolder + "/" + file.getName();
                        zipOut.putNextEntry(new ZipEntry(entryName));
                        byte[] bytes = new byte[1024];
                        int length;
                        while ((length = fis.read(bytes)) >= 0) {
                            zipOut.write(bytes, 0, length);
                        }
                        zipOut.closeEntry();
                    }
                }
            }
        }
    }

    private void deleteFolderRecursively(File folder) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteFolderRecursively(file);
                } else {
                    file.delete();
                }
            }
        }
        folder.delete();
    }
}

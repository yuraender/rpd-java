package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.repository.FileRPDRepository;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DocumentService {

    @Autowired
    private InstituteService instituteService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private DirectionService directionService;
    @Autowired
    private EducationTypeService educationTypeService;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private DisciplineService disciplineService;
    @Autowired
    private DisciplineEducationalProgramService disciplineEducationalProgramService;
    @Autowired
    private FileRPDRepository fileRPDRepository;

    public byte[] generateDocument(String templatePath, Map<String, String> placeholders) throws IOException {
        try (FileInputStream fis = new FileInputStream(templatePath);
             XWPFDocument document = new XWPFDocument(fis);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            List<XWPFHeader> headers = document.getHeaderList();
            for (XWPFHeader header : headers) {
                for (XWPFParagraph paragraph : header.getParagraphs()) {
                    for (XWPFRun run : paragraph.getRuns()) {
                        String text = run.getText(0);
                        if (text != null) {
                            for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                                if (text.contains(entry.getKey())) {
                                    text = text.replace(entry.getKey(), entry.getValue());
                                    run.setText(text, 0);
                                }
                            }
                        }
                    }
                }
            }

            for (XWPFParagraph paragraph : document.getParagraphs()) {
                for (XWPFRun run : paragraph.getRuns()) {
                    String text = run.getText(0);
                    if (text != null) {
                        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                            if (text.contains(entry.getKey())) {
                                text = text.replace(entry.getKey(), entry.getValue());
                                run.setText(text, 0);
                            }
                        }
                    }
                }
            }
            document.write(baos);
            return baos.toByteArray();
        }
    }

    public void generateAndSaveDocuments(Map<String, String> data, DisciplineEducationalProgram disciplineEducationalProgram) throws IOException {
        // Загрузка данных
        // Создание карты значений для замены
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("instituteName", data.get("instituteName"));
        placeholders.put("instituteCity", data.get("instituteCity"));
        placeholders.put("instituteApprovalText", data.get("instituteApprovalText"));
        placeholders.put("director", data.get("directorName"));
        placeholders.put("employeePosition", data.get("employeePosition"));
        placeholders.put("directionCode", data.get("directionCode"));
        placeholders.put("directionName", data.get("directionName"));
        placeholders.put("educationTypeText", data.get("educationTypeText"));
        placeholders.put("educationTypeLearningPeriod", data.get("educationTypeLearningPeriod"));
        placeholders.put("profileName", data.get("profileName"));
        placeholders.put("disciplineName", data.get("disciplineName"));
        placeholders.put("dateProtocol", data.get("dateProtocol"));

        // Путь к шаблону документа
        String templatePath = "src/main/resources/templates/tempDocs/title.docx";

        // Создание сущности FileRPD и сохранение документа в базу данных
        FileRPD fileRPD = new FileRPD();
        fileRPD.setDisciplineEducationalProgram(disciplineEducationalProgram);
        fileRPD.setDisabled(false);

        byte[] section1 = generateDocument(templatePath, placeholders);
        fileRPD.setSection2(section1);
        fileRPD.setSection2IsLoad(true);

        fileRPDRepository.save(fileRPD);
    }
}
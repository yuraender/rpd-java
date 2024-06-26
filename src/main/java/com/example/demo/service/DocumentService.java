package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.repository.FileRPDRepository;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
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

            // Обработка заголовков
            for (XWPFHeader header : document.getHeaderList()) {
                processParagraphs(header.getParagraphs(), placeholders);
                processTables(header.getTables(), placeholders);
            }

            // Обработка основного текста
            processParagraphs(document.getParagraphs(), placeholders);
            processTables(document.getTables(), placeholders);

            // Обработка текстовых полей (Text Boxes) и других фреймов (если они есть)
            for (IBodyElement element : document.getBodyElements()) {
                if (element instanceof XWPFParagraph) {
                    processTextBoxes((XWPFParagraph) element, placeholders);
                } else if (element instanceof XWPFTable) {
                    for (XWPFTableRow row : ((XWPFTable) element).getRows()) {
                        for (XWPFTableCell cell : row.getTableCells()) {
                            for (XWPFParagraph paragraph : cell.getParagraphs()) {
                                processTextBoxes(paragraph, placeholders);
                            }
                        }
                    }
                }
            }

            document.write(baos);
            return baos.toByteArray();
        }
    }

    private void processParagraphs(List<XWPFParagraph> paragraphs, Map<String, String> placeholders) {
        for (XWPFParagraph paragraph : paragraphs) {
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

    private void processTables(List<XWPFTable> tables, Map<String, String> placeholders) {
        for (XWPFTable table : tables) {
            for (XWPFTableRow row : table.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    processParagraphs(cell.getParagraphs(), placeholders);
                }
            }
        }
    }

    private void processTextBoxes(XWPFParagraph paragraph, Map<String, String> placeholders) {
        for (XWPFRun run : paragraph.getRuns()) {
            List<CTText> textElements = run.getCTR().getTList();
            for (CTText textElement : textElements) {
                String text = textElement.getStringValue();
                if (text != null) {
                    for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                        if (text.contains(entry.getKey())) {
                            text = text.replace(entry.getKey(), entry.getValue());
                            textElement.setStringValue(text);
                        }
                    }
                }
            }
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
        //===========================list2
        placeholders.put("competencyBeAble", data.get("competencyBeAble"));
        placeholders.put("competencyName", data.get("competencyName"));
        placeholders.put("competencyKnow", data.get("competencyKnow"));
        placeholders.put("competencyOwn", data.get("competencyOwn"));

        // Путь к шаблону документа
        String title = "src/main/resources/templates/tempDocs/title.docx";
        String missions = "src/main/resources/templates/tempDocs/missions.docx";
        String placeDisciplineOP = "src/main/resources/templates/tempDocs/placeDisciplineOP.docx";
        // Создание сущности FileRPD и сохранение документа в базу данных
        FileRPD fileRPD = new FileRPD();
        fileRPD.setDisciplineEducationalProgram(disciplineEducationalProgram);
        fileRPD.setDisabled(false);

        //Титульник
        byte[] section0 = generateDocument(title, placeholders);
        fileRPD.setSection0(section0);
        fileRPD.setSection0IsLoad(true);

        byte[] section1 = Files.readAllBytes(Paths.get(missions));
        fileRPD.setSection1(section1);
        fileRPD.setSection1IsLoad(true);

        byte[] section2 = Files.readAllBytes(Paths.get(placeDisciplineOP));
        fileRPD.setSection2(section2);
        fileRPD.setSection2IsLoad(true);


        fileRPDRepository.save(fileRPD);
    }
}
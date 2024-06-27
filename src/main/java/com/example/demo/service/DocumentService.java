package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.repository.FileRPDRepository;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
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

    public void generateAndSaveDocuments(
            Map<String, Object> data,
            DisciplineEducationalProgram disciplineEducationalProgram,
            List<Map<String, String>> competenciesData,
            List<Map<String, String>> techSupportData
            ) throws IOException {
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("instituteName", (String) data.get("instituteName"));
        placeholders.put("instituteCity", (String) data.get("instituteCity"));
        placeholders.put("instituteApprovalText", (String) data.get("instituteApprovalText"));
        placeholders.put("director", (String) data.get("directorName"));
        placeholders.put("employeePosition", (String) data.get("employeePosition"));
        placeholders.put("directionCode", (String) data.get("directionCode"));
        placeholders.put("directionName", (String) data.get("directionName"));
        placeholders.put("educationTypeText", (String) data.get("educationTypeText"));
        placeholders.put("educationTypeLearningPeriod", (String) data.get("educationTypeLearningPeriod"));
        placeholders.put("profileName", (String) data.get("profileName"));
        placeholders.put("disciplineName", (String) data.get("disciplineName"));
        placeholders.put("protocolDate", (String) data.get("protocolDate"));

        placeholders.put("educationTypeName", (String) data.get("educationTypeName"));
        placeholders.put("developerPosition", (String) data.get("developerPosition"));
        placeholders.put("abbreviation", (String) data.get("abbreviation"));
        placeholders.put("developerName", (String) data.get("developerName"));
        placeholders.put("managerName", (String) data.get("managerName"));
        placeholders.put("protocolNumber", (String) data.get("protocolNumber"));
        placeholders.put("directorApprovalDate", (String) data.get("directorApprovalDate"));
        placeholders.put("instituteFooterText", (String) data.get("instituteFooterText"));


        String titlePath = "src/main/resources/templates/tempDocs/title.docx";
        String missionsPath = "src/main/resources/templates/tempDocs/missions.docx";
        String placeDisciplineOPPath = "src/main/resources/templates/tempDocs/placeDisciplineOP.docx";
        String educationTech = "src/main/resources/templates/tempDocs/educationTech.docx";
        String evaluationTools = "src/main/resources/templates/tempDocs/evaluationTools.docx";
        String emiSupport = "src/main/resources/templates/tempDocs/emiSupport.docx";
        String materialTechSuppDiscipline = "src/main/resources/templates/tempDocs/materialTechSuppDiscipline.docx";
        String structureAndContent = "src/main/resources/templates/tempDocs/structureAndContent.docx";
        String footer = "src/main/resources/templates/tempDocs/footer.docx";

        FileRPD fileRPD = new FileRPD();
        fileRPD.setDisciplineEducationalProgram(disciplineEducationalProgram);
        fileRPD.setDisabled(false);

        // Раздел0. Титульник
        byte[] section0 = generateDocument(titlePath, placeholders);
        fileRPD.setSection0(section0);
        fileRPD.setSection0IsLoad(true);

        // Раздел1. Цели
        byte[] section1 = Files.readAllBytes(Paths.get(missionsPath));
        fileRPD.setSection1(section1);
        fileRPD.setSection1IsLoad(true);

        // Раздел2. Место дисциплины
        byte[] section2 = Files.readAllBytes(Paths.get(placeDisciplineOPPath));
        fileRPD.setSection2(section2);
        fileRPD.setSection2IsLoad(true);

        // Раздел3. Компетенции
        byte[] section3 = generateCompetenciesDocument(competenciesData);
        fileRPD.setSection3(section3);
        fileRPD.setSection3IsLoad(true);

        // Раздел4. Структура и содержание дисциплины
        byte[] section4 = Files.readAllBytes(Paths.get(structureAndContent));
        fileRPD.setSection4(section4);
        fileRPD.setSection4IsLoad(true);

        // Раздел4. Образовательные технологии
        byte[] section5 = Files.readAllBytes(Paths.get(educationTech));
        fileRPD.setSection5(section5);
        fileRPD.setSection5IsLoad(true);

        // Раздел5. Место дисциплины
        byte[] section6 = Files.readAllBytes(Paths.get(evaluationTools));
        fileRPD.setSection6(section6);
        fileRPD.setSection6IsLoad(true);

        // Раздел6. Учебно-методическое и информационное обеспечение дисциплины
        byte[] section7 = Files.readAllBytes(Paths.get(emiSupport));
        fileRPD.setSection7(section7);
        fileRPD.setSection7IsLoad(true);

//        // Раздел8. Материально-техническое обеспечение
//        byte[] section8 = generateTechSupportDocument(techSupportData);
//        fileRPD.setSection8(section8);
//        fileRPD.setSection8IsLoad(true);

        // Раздел9. Подвал
        byte[] section9 = generateDocument(footer, placeholders);
        fileRPD.setSection9(section9);
        fileRPD.setSection9IsLoad(true);

        fileRPDRepository.save(fileRPD);
    }

    public byte[] generateCompetenciesDocument(List<Map<String, String>> competenciesData) throws IOException {
        XWPFDocument document = new XWPFDocument();

        // Заголовок "3. КОМПЕТЕНЦИИ ОБУЧАЮЩЕГОСЯ, ФОРМИРУЕМЫЕ В РЕЗУЛЬТАТЕ ОСВОЕНИЯ ДИСЦИПЛИНЫ"
        XWPFParagraph titleParagraph = document.createParagraph();
        titleParagraph.setIndentationFirstLine(32 * 20); // Отступ первой строки в 32 pt
        XWPFRun titleRun = titleParagraph.createRun();
        titleRun.setBold(true);
        titleRun.setFontSize(14);
        titleRun.setFontFamily("Times New Roman");
        titleRun.setText("3. КОМПЕТЕНЦИИ ОБУЧАЮЩЕГОСЯ, ФОРМИРУЕМЫЕ В РЕЗУЛЬТАТЕ ОСВОЕНИЯ ДИСЦИПЛИНЫ");

        // Добавляем пустой абзац после заголовка
        document.createParagraph();

        // Цикл по всем компетенциям
        int index = 1;
        for (Map<String, String> competency : competenciesData) {
            // Название компетенции
            String competencyName = competency.get("competencyName");

            // Создание параграфа для названия компетенции
            XWPFParagraph competencyParagraph = document.createParagraph();
            competencyParagraph.setIndentationFirstLine(32 * 20);
            XWPFRun competencyRun = competencyParagraph.createRun();
            competencyRun.setFontSize(14);
            competencyRun.setFontFamily("Times New Roman");
            competencyRun.setBold(true);
            competencyRun.setText(competencyName);

            // Добавляем пустой абзац после названия компетенции
            document.createParagraph();

            // Описание "В результате освоения дисциплины обучающийся должен демонстрировать следующие результаты образования:"
            XWPFParagraph descriptionParagraph = document.createParagraph();
            descriptionParagraph.setIndentationFirstLine(32 * 20); // Отступ первой строки в 32 pt
            XWPFRun descriptionRun = descriptionParagraph.createRun();
            descriptionRun.setFontSize(14);
            descriptionRun.setFontFamily("Times New Roman");
            descriptionRun.setText("В результате освоения дисциплины обучающийся должен демонстрировать следующие результаты образования:");

            // Добавляем пустой абзац после описания
            document.createParagraph();

            // Пункты "знать:", "уметь:", "владеть:"
            String[] points = {"знать", "уметь", "владеть"};
            for (String point : points) {
                XWPFParagraph pointParagraph = document.createParagraph();
                pointParagraph.setIndentationFirstLine(32 * 20); // Отступ первой строки в 32 pt
                XWPFRun pointRun = pointParagraph.createRun();
                pointRun.setFontSize(14);
                pointRun.setFontFamily("Times New Roman");
                pointRun.setBold(true);
                pointRun.setText(index + ") " + point + ":");

                // Значения для каждой компетенции
                String detail = "";
                if(point.equals("знать")){
                    detail = competency.get("competencyKnow");
                } else if (point.equals("уметь")) {
                    detail = competency.get("competencyBeAble");
                }else{
                    detail = competency.get("competencyOwn");
                }
                XWPFParagraph detailParagraph = document.createParagraph();
                detailParagraph.setIndentationFirstLine(32 * 20); // Отступ первой строки в 32 pt
                XWPFRun detailRun = detailParagraph.createRun();
                detailRun.setFontSize(14);
                detailRun.setFontFamily("Times New Roman");
                detailRun.setText("- " + (detail != null ? detail : ""));
            }

            // Добавляем пустой абзац после каждой компетенции
            document.createParagraph();

            index++;
        }

        // Конвертация документа в массив байтов
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        document.write(baos);
        return baos.toByteArray();
    }


    public byte[] generateTechSupportDocument(List<Map<String, String>> techData) throws IOException {
        XWPFDocument document = new XWPFDocument();

        // Заголовок "8. МАТЕРИАЛЬНО-ТЕХНИЧЕСКОЕ ОБЕСПЕЧЕНИЕ ДИСЦИПЛИНЫ"
        XWPFParagraph titleParagraph = document.createParagraph();
        titleParagraph.setIndentationFirstLine(32 * 20); // Отступ первой строки в 32 pt
        XWPFRun titleRun = titleParagraph.createRun();
        titleRun.setBold(true);
        titleRun.setFontSize(14);
        titleRun.setFontFamily("Times New Roman");
        titleRun.setText("8. МАТЕРИАЛЬНО-ТЕХНИЧЕСКОЕ ОБЕСПЕЧЕНИЕ ДИСЦИПЛИНЫ");

        // Добавляем пустой абзац после заголовка
        document.createParagraph();

        // Основной контент документа
        for (Map<String, String> techSupport : techData) {
            String roomNumber = techSupport.get("roomNumber");
            String specialEquipment = techSupport.get("specialEquipment");
            String softwareLicenses = techSupport.get("softwareLicenses");
            String confirmingDocuments = techSupport.get("confirmingDocuments");

            // Создание параграфа для наименования специальных помещений
            XWPFParagraph roomParagraph = document.createParagraph();
            roomParagraph.setIndentationFirstLine(32 * 20);
            XWPFRun roomRun = roomParagraph.createRun();
            roomRun.setFontSize(14);
            roomRun.setFontFamily("Times New Roman");
            roomRun.setBold(true);
            roomRun.setText(roomNumber);

            // Создание параграфа для оснащенности специальных помещений
            XWPFParagraph equipmentParagraph = document.createParagraph();
            equipmentParagraph.setIndentationFirstLine(32 * 20);
            XWPFRun equipmentRun = equipmentParagraph.createRun();
            equipmentRun.setFontSize(14);
            equipmentRun.setFontFamily("Times New Roman");
            equipmentRun.setText(specialEquipment);

            // Создание параграфа для перечня лицензионного программного обеспечения
            XWPFParagraph softwareParagraph = document.createParagraph();
            softwareParagraph.setIndentationFirstLine(32 * 20);
            XWPFRun softwareRun = softwareParagraph.createRun();
            softwareRun.setFontSize(14);
            softwareRun.setFontFamily("Times New Roman");
            softwareRun.setText(softwareLicenses);

            // Создание параграфа для реквизитов подтверждающего документа
            XWPFParagraph confirmingParagraph = document.createParagraph();
            confirmingParagraph.setIndentationFirstLine(32 * 20);
            XWPFRun confirmingRun = confirmingParagraph.createRun();
            confirmingRun.setFontSize(14);
            confirmingRun.setFontFamily("Times New Roman");
            confirmingRun.setText(confirmingDocuments);

            // Добавляем пустой абзац после каждой секции
            document.createParagraph();
        }

        // Конвертация документа в массив байтов
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        document.write(baos);
        return baos.toByteArray();
    }

}
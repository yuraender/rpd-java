package com.example.demo.service;

import com.example.demo.entity.BasicEducationalProgramDiscipline;
import com.example.demo.entity.FileRPD;
import com.example.demo.entity.Indicator;
import com.example.demo.entity.Protocol;
import com.example.demo.util.WordMerger;
import com.example.demo.util.WordReplacer;
import com.example.demo.util.WordTableRowDuplicator;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final FileRPDService fileRPDService;

    public FileRPD generate(Map<String, Object> data, BasicEducationalProgramDiscipline bepDiscipline) throws IOException {
        String titlePath;
        if (data.get("type") == Protocol.Type.APPROVE) {
            titlePath = "templates/tempDocs/titleApprove.docx";
        } else {
            titlePath = "templates/tempDocs/titleActualize.docx";
        }
        String objectivesPath = "templates/tempDocs/objectives.docx";
        String placePath = "templates/tempDocs/place.docx";
        String competencesPath = "templates/tempDocs/competences.docx";
        String structureAndContentPath = "templates/tempDocs/structureAndContent.docx";
        String educationalTechnologiesPath = "templates/tempDocs/educationalTechnologies.docx";
        String evaluationToolsPath = "templates/tempDocs/evaluationTools.docx";
        String emiSupportPath = "templates/tempDocs/emiSupport.docx";
        String mtSupportPath = "templates/tempDocs/mtSupport.docx";
        String footerApprovePath = "templates/tempDocs/footerApprove.docx";
        String footerActualizePath = "templates/tempDocs/footerActualize.docx";

        FileRPD fileRPD = fileRPDService
                .getByAcademicYearAndBasicEducationalProgramDiscipline(
                        bepDiscipline.getBasicEducationalProgram().getAcademicYear(), bepDiscipline
                );
        boolean existing = true;
        if (fileRPD == null) {
            fileRPD = new FileRPD();
            fileRPD.setAcademicYear(bepDiscipline.getBasicEducationalProgram().getAcademicYear());
            fileRPD.setBasicEducationalProgramDiscipline(bepDiscipline);
            fileRPD.setDisabled(false);
            existing = false;
        }

        // Титульный лист
        byte[] section0 = generate(titlePath, data);
        fileRPD.setSection0(section0);
        fileRPD.setSection0IsLoad(true);

        // Раздел 1. Цели освоения дисциплины
        if (!existing) {
            byte[] section1 = generate(objectivesPath, data);
            fileRPD.setSection1(section1);
            fileRPD.setSection1IsLoad(true);
        }

        // Раздел 2. Место дисциплины в составе ООП
        if (!existing) {
            byte[] section2 = generate(placePath, data);
            fileRPD.setSection2(section2);
            fileRPD.setSection2IsLoad(true);
        }

        // Раздел 3. Компетенции обучающегося
        byte[] section3 = generateCompetences(competencesPath, data);
        fileRPD.setSection3(section3);
        fileRPD.setSection3IsLoad(true);

        // Раздел 4. Структура и содержание дисциплины
        if (!existing) {
            byte[] section4 = generate(structureAndContentPath, data);
            fileRPD.setSection4(section4);
            fileRPD.setSection4IsLoad(true);
        }

        // Раздел 5. Образовательные технологии
        if (!existing) {
            byte[] section5 = generate(educationalTechnologiesPath, data);
            fileRPD.setSection5(section5);
            fileRPD.setSection5IsLoad(true);
        }

        // Раздел 6. Оценочные средства
        if (!existing) {
            byte[] section6 = generate(evaluationToolsPath, data);
            fileRPD.setSection6(section6);
            fileRPD.setSection6IsLoad(true);
        }

        // Раздел 7. Учебно-методическое и информационное обеспечение дисциплины
        if (!existing) {
            byte[] section7 = generate(emiSupportPath, data);
            fileRPD.setSection7(section7);
            fileRPD.setSection7IsLoad(true);
        }

        // Раздел 8. Материально-техническое обеспечение
        byte[] section8 = generateAuditoriums(mtSupportPath, data);
        fileRPD.setSection8(section8);
        fileRPD.setSection8IsLoad(true);

        // Подвал
        byte[] section9 = generateFooter(Protocol.Type.APPROVE, footerApprovePath, data);
        for (Map<String, Object> actualizeProtocol
                : ((List<Map<String, Object>>) data.get("actualizeProtocols"))) {
            Map<String, Object> protocolData = new HashMap<>();
            protocolData.putAll(data);
            protocolData.putAll(actualizeProtocol);
            byte[] protocol = generateFooter(Protocol.Type.ACTUALIZE, footerActualizePath, protocolData);
            section9 = WordMerger.merge(section9, protocol, true);
        }
        fileRPD.setSection9(section9);
        fileRPD.setSection9IsLoad(true);

        fileRPDService.save(fileRPD);
        return fileRPD;
    }

    public byte[] generateCompetences(String templatePath, Map<String, Object> data) throws IOException {
        byte[] competencesData = generate(templatePath, data);
        try (
                ByteArrayInputStream bais = new ByteArrayInputStream(competencesData);
                XWPFDocument document = new XWPFDocument(bais)
        ) {
            // Цикл по всем компетенциям
            for (Map<String, Object> competence : (List<Map<String, Object>>) data.get("competences")) {
                Map<String, String> competenceData = competence.entrySet()
                        .stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> entry.getValue().toString()
                        ));

                // Создание параграфа для индекса компетенции
                XWPFParagraph paragraph = document.createParagraph();
                paragraph.setAlignment(ParagraphAlignment.BOTH);
                paragraph.setIndentationFirstLine(708);
                paragraph.setSpacingBetween(1.5);
                XWPFRun run = paragraph.createRun();
                run.setText(competenceData.get("competenceIndex"));
                run.setFontFamily("Times New Roman");
                run.setFontSize(14);
                run.setBold(true);

                // Создание параграфа для сущности компетенции
                paragraph = document.createParagraph();
                paragraph.setAlignment(ParagraphAlignment.BOTH);
                paragraph.setIndentationFirstLine(708);
                paragraph.setSpacingBetween(1.5);
                run = paragraph.createRun();
                run.setText(competenceData.get("competenceEssence") + ".");
                run.setFontFamily("Times New Roman");
                run.setFontSize(14);

                // Создание параграфа для результатов образования
                paragraph = document.createParagraph();
                paragraph.setAlignment(ParagraphAlignment.BOTH);
                paragraph.setIndentationFirstLine(708);
                paragraph.setSpacingBetween(1.5);
                run = paragraph.createRun();
                run.setText("В результате освоения дисциплины обучающийся должен демонстрировать следующие результаты образования:");
                run.setFontFamily("Times New Roman");
                run.setFontSize(14);

                // Создание списка индикаторов компетенции
                XWPFNumbering numbering = document.createNumbering();
                BigInteger numID = numbering.addNum(BigInteger.valueOf(0));

                // Цикл по всем индикаторам компетенции
                for (Indicator indicator : (List<Indicator>) competence.get("competenceIndicators")) {
                    XWPFParagraph item = document.createParagraph();
                    item.setNumID(numID);
                    item.setAlignment(ParagraphAlignment.BOTH);
                    item.setIndentationLeft(0);
                    item.setIndentationFirstLine(708);
                    item.setSpacingBetween(1.5);
                    XWPFRun itemRun = item.createRun();
                    itemRun.setText(indicator.getType().getName() + " " + indicator.getText() + ".");
                    itemRun.setFontFamily("Times New Roman");
                    itemRun.setFontSize(14);
                }
            }

            // Конвертация документа в массив байтов
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                document.write(baos);
                return baos.toByteArray();
            }
        }
    }

    public byte[] generateAuditoriums(String templatePath, Map<String, Object> data) throws IOException {
        byte[] auditoriumsData = generate(templatePath, data);
        try (
                ByteArrayInputStream bais = new ByteArrayInputStream(auditoriumsData);
                XWPFDocument document = new XWPFDocument(bais)
        ) {
            XWPFTable auditoriumTable = document.getTables().get(0);
            XWPFTableRow firstRow = auditoriumTable.getRow(1);

            List<Map<String, Object>> auditoriums = (List<Map<String, Object>>) data.get("auditoriums");
            for (int i = auditoriums.size() - 1; i >= 0; i--) {
                XWPFTableRow row;
                if (i == 0) {
                    row = firstRow;
                } else {
                    row = WordTableRowDuplicator.duplicateRowWithStyles(auditoriumTable, firstRow);
                }
                Map<String, Object> temp = new HashMap<>();
                temp.putAll(data);
                temp.putAll(auditoriums.get(i));
                Map<String, String> developerData = temp.entrySet()
                        .stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> entry.getValue().toString()
                        ));
                for (XWPFTableCell cell : row.getTableCells()) {
                    for (XWPFParagraph paragraph : cell.getParagraphs()) {
                        WordReplacer.processParagraph(paragraph, developerData);
                    }
                }
            }

            // Конвертация документа в массив байтов
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                document.write(baos);
                return baos.toByteArray();
            }
        }
    }

    public byte[] generateFooter(Protocol.Type type, String templatePath, Map<String, Object> data) throws IOException {
        byte[] footerData = generate(templatePath, data);
        try (
                ByteArrayInputStream bais = new ByteArrayInputStream(footerData);
                XWPFDocument document = new XWPFDocument(bais)
        ) {
            // Обработка таблицы в протоколе утверждения
            if (type == Protocol.Type.APPROVE) {
                XWPFTable developerTable = document.getTables().get(0);
                XWPFTableRow firstRow = developerTable.getRow(0);

                List<Map<String, Object>> developers = (List<Map<String, Object>>) data.get("developers");
                for (int i = developers.size() - 1; i >= 0; i--) {
                    XWPFTableRow row;
                    if (i == 0) {
                        row = firstRow;
                    } else {
                        row = WordTableRowDuplicator.duplicateRowWithStyles(developerTable, firstRow);
                    }
                    Map<String, Object> temp = new HashMap<>();
                    temp.putAll(data);
                    temp.putAll(developers.get(i));
                    Map<String, String> developerData = temp.entrySet()
                            .stream()
                            .collect(Collectors.toMap(
                                    Map.Entry::getKey,
                                    entry -> entry.getValue().toString()
                            ));
                    for (XWPFTableCell cell : row.getTableCells()) {
                        for (XWPFParagraph paragraph : cell.getParagraphs()) {
                            WordReplacer.processParagraph(paragraph, developerData);
                        }
                    }
                }
            }

            // Конвертация документа в массив байтов
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                document.write(baos);
                return baos.toByteArray();
            }
        }
    }

    private byte[] generate(String templatePath, Map<String, Object> data) throws IOException {
        ClassPathResource resource = new ClassPathResource(templatePath);
        return generate(resource.getContentAsByteArray(), data);
    }

    private byte[] generate(byte[] resourceData, Map<String, Object> data) throws IOException {
        Map<String, String> dataMap = data.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().toString()
                ));
        return WordReplacer.replace(resourceData, dataMap);
    }
}

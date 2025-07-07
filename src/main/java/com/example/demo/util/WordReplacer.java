package com.example.demo.util;

import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class WordReplacer {

    public byte[] replace(byte[] file, Map<String, String> replacements) throws IOException {
        try (
                ByteArrayInputStream bais = new ByteArrayInputStream(file);
                XWPFDocument document = new XWPFDocument(bais);
                ByteArrayOutputStream baos = new ByteArrayOutputStream()
        ) {
            // Process paragraphs
            for (XWPFParagraph paragraph : new ArrayList<>(document.getParagraphs())) {
                processParagraph(paragraph, replacements);
            }

            // Process tables
            for (XWPFTable table : document.getTables()) {
                for (XWPFTableRow row : table.getRows()) {
                    for (XWPFTableCell cell : row.getTableCells()) {
                        for (XWPFParagraph paragraph : new ArrayList<>(cell.getParagraphs())) {
                            processParagraph(paragraph, replacements);
                        }
                    }
                }
            }

            // Process headers and footers
            for (XWPFHeader header : document.getHeaderList()) {
                for (XWPFParagraph paragraph : new ArrayList<>(header.getParagraphs())) {
                    processParagraph(paragraph, replacements);
                }
            }
            for (XWPFFooter footer : document.getFooterList()) {
                for (XWPFParagraph paragraph : new ArrayList<>(footer.getParagraphs())) {
                    processParagraph(paragraph, replacements);
                }
            }

            document.write(baos);
            return baos.toByteArray();
        }
    }

    public void processParagraph(XWPFParagraph paragraph, Map<String, String> replacements) {
        List<XWPFRun> runs = paragraph.getRuns();
        if (runs == null || runs.isEmpty()) {
            return;
        }

        // Combine all runs to search for placeholders
        StringBuilder combinedText = new StringBuilder();
        List<TextPosition> textPositions = new ArrayList<>();
        int currentPosition = 0;

        for (XWPFRun run : runs) {
            String runText = run.getText(0);
            if (runText != null && !runText.isEmpty()) {
                textPositions.add(new TextPosition(run, currentPosition, currentPosition + runText.length()));
                combinedText.append(runText);
                currentPosition += runText.length();
            }
        }

        String fullText = combinedText.toString();
        Pattern pattern = Pattern.compile(
                "(" +
                        String.join("|", replacements.keySet()
                                .stream()
                                .map(Pattern::quote)
                                .sorted(Comparator.comparing(String::length).reversed())
                                .toList()) +
                        ")"
        );
        Matcher matcher = pattern.matcher(fullText);

        // Process all matches in the paragraph
        while (matcher.find()) {
            String placeholder = matcher.group();
            String replacement = replacements.get(placeholder);
            if (replacement == null) {
                continue;
            }

            int matchStart = matcher.start();
            int matchEnd = matcher.end();

            // Find all runs that contain parts of this placeholder
            List<TextPosition> affectedRuns = new ArrayList<>();
            for (TextPosition pos : textPositions) {
                if (pos.start < matchEnd && pos.end > matchStart) {
                    affectedRuns.add(pos);
                }
            }
            if (affectedRuns.isEmpty()) {
                continue;
            }

            // Replace text in the first affected run
            TextPosition firstRun = affectedRuns.get(0);
            String originalText = firstRun.run.getText(0);
            if (originalText.isEmpty()) {
                continue;
            }

            // Calculate positions within the first run
            int replaceStart = Math.max(matchStart - firstRun.start, 0);
            int replaceEnd = Math.min(matchEnd - firstRun.start, originalText.length());

            // Build new text for first run
            String newText = originalText.substring(0, replaceStart) + replacement + originalText.substring(replaceEnd);
            String[] split = newText.split("\\\\n");

            firstRun.run.setText(split[0], 0);
            for (int i = split.length - 1; i >= 1; i--) {
                XmlCursor cursor = paragraph.getCTP().newCursor();
                cursor.toEndToken();
                cursor.toNextToken();

                XWPFParagraph newParagraph = paragraph.getBody().insertNewParagraph(cursor);
                WordTableRowDuplicator.copyParagraphProperties(paragraph, newParagraph);
                XWPFRun newRun = newParagraph.createRun();
                WordTableRowDuplicator.copyRunProperties(firstRun.run, newRun);
                newRun.setText(split[i], 0);

                cursor.close();
            }

            // Clear text in subsequent affected runs
            for (int i = 1; i < affectedRuns.size(); i++) {
                affectedRuns.get(i).run.setText("", 0);
            }
        }
    }

    @RequiredArgsConstructor
    private static class TextPosition {

        private final XWPFRun run;
        private final int start;
        private final int end;
    }
}

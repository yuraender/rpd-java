package com.example.demo.util;

import lombok.experimental.UtilityClass;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrPr;

@UtilityClass
public class WordTableRowDuplicator {

    public XWPFTableRow duplicateRowWithStyles(XWPFTable table, XWPFTableRow sourceRow) {
        // Create new row at the end of the table
        XWPFTableRow newRow = table.createRow();

        // Copy row properties
        CTRow ctRow = newRow.getCtRow();
        CTRow sourceCtRow = sourceRow.getCtRow();

        // Copy all row-level properties
        if (sourceCtRow.isSetTrPr()) {
            ctRow.setTrPr(sourceCtRow.getTrPr());
        }

        // Copy cells with all properties
        for (int i = 0; i < sourceRow.getTableCells().size(); i++) {
            XWPFTableCell sourceCell = sourceRow.getCell(i);
            XWPFTableCell newCell = newRow.getCell(i);

            // Copy cell properties
            if (sourceCell.getCTTc().isSetTcPr()) {
                newCell.getCTTc().setTcPr(sourceCell.getCTTc().getTcPr());
            }

            // Copy paragraphs and runs with styles
            for (XWPFParagraph sourceParagraph : sourceCell.getParagraphs()) {
                XWPFParagraph newParagraph = newCell.addParagraph();
                copyParagraphProperties(sourceParagraph, newParagraph);

                for (XWPFRun sourceRun : sourceParagraph.getRuns()) {
                    XWPFRun newRun = newParagraph.createRun();
                    copyRunProperties(sourceRun, newRun);
                    newRun.setText(sourceRun.text());
                }
            }
            if (newCell.getParagraphs().size() > 1) {
                newCell.removeParagraph(0);
            }
        }

        return newRow;
    }

    void copyParagraphProperties(XWPFParagraph source, XWPFParagraph target) {
        CTPPr sourcePPr = source.getCTP().getPPr();
        if (sourcePPr != null) {
            target.getCTP().setPPr(sourcePPr);
        }

        // Copy paragraph alignment
        if (source.getAlignment() != ParagraphAlignment.LEFT) {
            target.setAlignment(source.getAlignment());
        }
        if (source.getVerticalAlignment() != TextAlignment.AUTO) {
            target.setVerticalAlignment(source.getVerticalAlignment());
        }

        // Copy spacing
        if (source.getSpacingBetween() != -1) {
            target.setSpacingBetween(source.getSpacingBetween());
        }

        // Copy indentation
        if (source.getIndentationLeft() != -1) {
            target.setIndentationLeft(source.getIndentationLeft());
        }
        if (source.getIndentationRight() != -1) {
            target.setIndentationRight(source.getIndentationRight());
        }
        if (source.getIndentationHanging() != -1) {
            target.setIndentationHanging(source.getIndentationHanging());
        }
        if (source.getIndentationFirstLine() != -1) {
            target.setIndentationFirstLine(source.getIndentationFirstLine());
        }
    }

    @SuppressWarnings("deprecation")
    void copyRunProperties(XWPFRun source, XWPFRun target) {
        if (source.getCTR().isSetRPr()) {
            target.getCTR().setRPr(source.getCTR().getRPr());
        }

        // Copy properties
        target.setFontFamily(source.getFontFamily());
        if (source.getFontSize() != -1) {
            target.setFontSize(source.getFontSizeAsDouble());
        }
        target.setBold(source.isBold());
        target.setItalic(source.isItalic());
        target.setUnderline(source.getUnderline());
        target.setStrikeThrough(source.isStrikeThrough());
        target.setColor(source.getColor());
    }
}

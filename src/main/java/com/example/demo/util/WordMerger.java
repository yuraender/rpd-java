package com.example.demo.util;

import lombok.experimental.UtilityClass;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.openxmlformats.schemas.drawingml.x2006.main.CTBlip;
import org.openxmlformats.schemas.officeDocument.x2006.math.CTOMath;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAbstractNum;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UtilityClass
public class WordMerger {

    private final Map<BigInteger, BigInteger> numIDs = new HashMap<>();

    public byte[] merge(byte[] firstFile, byte[] secondFile, boolean breakPage) throws IOException {
        try (
                ByteArrayInputStream firstStream = new ByteArrayInputStream(firstFile);
                ByteArrayInputStream secondStream = new ByteArrayInputStream(secondFile);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream()
        ) {
            XWPFDocument resultDocument = new XWPFDocument(firstStream);
            XWPFDocument documentToAppend = new XWPFDocument(secondStream);
            if (breakPage) {
                XWPFParagraph paragraph = resultDocument.createParagraph();
                paragraph.setPageBreak(true);
            }
            traverseBodyElements(documentToAppend.getBodyElements(), resultDocument);
            resultDocument.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    private void traverseBodyElements(List<IBodyElement> bodyElements, IBody resultBody) {
        for (IBodyElement bodyElement : bodyElements) {
            if (bodyElement instanceof XWPFParagraph paragraph) {
                XWPFParagraph resultParagraph = createParagraphWithPPr(paragraph, resultBody);
                traverseRunElements(paragraph.getIRuns(), resultParagraph);
            } else if (bodyElement instanceof XWPFTable table) {
                XWPFTable resultTable = createTableWithTblPrAndTblGrid(table, resultBody);
                traverseTableRows(table.getRows(), resultTable);
            }
        }
    }

    private XWPFParagraph createParagraphWithPPr(XWPFParagraph paragraph, IBody resultBody) {
        if (resultBody instanceof XWPFDocument resultDocument) {
            XWPFParagraph resultParagraph = resultDocument.createParagraph();
            // Simply copy the underlying XML bean to avoid more code
            resultParagraph.getCTP().setPPr(paragraph.getCTP().getPPr());
            handleStyles(resultDocument, paragraph);
            handleNumberings(paragraph, resultParagraph);
            handleOMathParaArray(paragraph, resultParagraph);
            handleOMathArrayIfFirstChild(paragraph, resultParagraph);
            return resultParagraph;
        } else if (resultBody instanceof XWPFTableCell resultTableCell) {
            XWPFParagraph resultParagraph = resultTableCell.addParagraph();
            // Simply copy the underlying XML bean to avoid more code
            resultParagraph.getCTP().setPPr(paragraph.getCTP().getPPr());
            handleStyles(resultTableCell, paragraph);
            handleOMathParaArray(paragraph, resultParagraph);
            handleOMathArrayIfFirstChild(paragraph, resultParagraph);
            return resultParagraph;
        }
        return null;
    }

    private void handleOMathParaArray(XWPFParagraph paragraph, XWPFParagraph resultParagraph) {
        // Simply copy the underlying XML bean to avoid more code
        resultParagraph.getCTP().setOMathParaArray(paragraph.getCTP().getOMathParaArray());
    }

    private void handleOMathArrayIfFirstChild(XWPFParagraph paragraph, XWPFParagraph resultParagraph) {
        try (XmlCursor cursor = paragraph.getCTP().newCursor()) {
            cursor.toChild(0);
            XmlObject xmlObject = cursor.getObject();
            if (xmlObject instanceof CTOMath ctOMath) {
                resultParagraph.getCTP().addNewOMath();
                resultParagraph.getCTP().setOMathArray(resultParagraph.getCTP().getOMathArray().length - 1, ctOMath);
            }
        }
    }

    private void handleNumberings(XWPFParagraph paragraph, XWPFParagraph resultParagraph) {
        // If we have numberings, we need merging the numIDs and abstract numberings of the two different documents
        BigInteger numID = paragraph.getNumID();
        if (numID == null) {
            return;
        }
        BigInteger resultNumID = numIDs.get(numID);
        if (resultNumID == null) {
            XWPFDocument document = paragraph.getDocument();
            XWPFNumbering numbering = document.createNumbering();
            XWPFNum num = numbering.getNum(numID);
            BigInteger abstractNumID = numbering.getAbstractNumID(numID);
            XWPFAbstractNum abstractNum = numbering.getAbstractNum(abstractNumID);
            XWPFAbstractNum resultAbstractNum = new XWPFAbstractNum(abstractNum.getCTAbstractNum());
            XWPFDocument resultDocument = resultParagraph.getDocument();
            XWPFNumbering resultNumbering = resultDocument.createNumbering();
            int pos = resultNumbering.getAbstractNums().size();
            resultAbstractNum.getCTAbstractNum().setAbstractNumId(BigInteger.valueOf(pos));
            BigInteger resultAbstractNumID = resultNumbering.addAbstractNum(resultAbstractNum);
            resultNumID = resultNumbering.addNum(resultAbstractNumID);
            XWPFNum resultNum = resultNumbering.getNum(resultNumID);
            resultNum.getCTNum().setLvlOverrideArray(num.getCTNum().getLvlOverrideArray());
            numIDs.put(numID, resultNumID);
        }
        resultParagraph.setNumID(resultNumID);
    }

    private void handleStyles(IBody resultBody, IBodyElement bodyElement) {
        // If we have bodyElement styles we need merging those styles of the two different documents
        XWPFDocument document = null;
        String styleID = null;
        if (bodyElement instanceof XWPFParagraph paragraph) {
            document = paragraph.getDocument();
            styleID = paragraph.getStyleID();
        } else if (bodyElement instanceof XWPFTable table) {
            if (table.getPart() instanceof XWPFDocument) {
                document = (XWPFDocument) table.getPart();
                styleID = table.getStyleID();
            }
        }
        if (document == null || styleID == null || styleID.isEmpty()) {
            return;
        }
        XWPFDocument resultDocument = null;
        if (resultBody instanceof XWPFDocument) {
            resultDocument = (XWPFDocument) resultBody;
        } else if (resultBody instanceof XWPFTableCell resultTableCell) {
            resultDocument = resultTableCell.getXWPFDocument();
        }
        if (resultDocument != null) {
            XWPFStyles styles = document.getStyles();
            XWPFStyles resultStyles = resultDocument.getStyles();
            XWPFStyle style = styles.getStyle(styleID);
            // Merge each used styles, also the related ones
            for (XWPFStyle relatedStyle : styles.getUsedStyleList(style)) {
                if (resultStyles.getStyle(relatedStyle.getStyleId()) == null) {
                    resultStyles.addStyle(relatedStyle);
                }
            }
        }
    }

    private XWPFTable createTableWithTblPrAndTblGrid(XWPFTable table, IBody resultBody) {
        if (resultBody instanceof XWPFDocument resultDocument) {
            XWPFTable resultTable = resultDocument.createTable();
            resultTable.removeRow(0);
            // Simply copy the underlying XML bean to avoid more code
            resultTable.getCTTbl().setTblPr(table.getCTTbl().getTblPr());
            // Simply copy the underlying XML bean to avoid more code
            resultTable.getCTTbl().setTblGrid(table.getCTTbl().getTblGrid());
            handleStyles(resultDocument, table);
            return resultTable;
        }
        return null;
    }

    private void traverseRunElements(List<IRunElement> runElements, IRunBody resultRunBody) {
        for (IRunElement runElement : runElements) {
            if (runElement instanceof XWPFFieldRun fieldRun) {
                XWPFFieldRun resultFieldRun = createFieldRunWithRPr(fieldRun, resultRunBody);
                traversePictures(fieldRun, resultFieldRun);
            } else if (runElement instanceof XWPFHyperlinkRun hyperlinkRun) {
                XWPFHyperlinkRun resultHyperlinkRun = createHyperlinkRunWithRPr(hyperlinkRun, resultRunBody);
                traversePictures(hyperlinkRun, resultHyperlinkRun);
            } else if (runElement instanceof XWPFRun run) {
                XWPFRun resultRun = createRunWithRPr(run, resultRunBody);
                traversePictures(run, resultRun);
            }
        }
    }

    private void copyTextOfRuns(XWPFRun run, XWPFRun resultRun) {
        // Copy all of the possible T contents of the runs
        for (int i = 0; i < run.getCTR().sizeOfTArray(); i++) {
            resultRun.setText(run.getText(i), i);
        }
    }

    private XWPFFieldRun createFieldRunWithRPr(XWPFFieldRun fieldRun, IRunBody resultRunBody) {
        if (resultRunBody instanceof XWPFParagraph resultParagraph) {
            XWPFFieldRun resultFieldRun = (XWPFFieldRun) resultParagraph.createRun();
            // Simply copy the underlying XML bean to avoid more code
            resultFieldRun.getCTR().setRPr(fieldRun.getCTR().getRPr());
            handleRunStyles(resultParagraph.getDocument(), fieldRun);
            handlePossibleOMath(fieldRun, resultRunBody);
            return resultFieldRun;
        }
        return null;
    }

    private XWPFHyperlinkRun createHyperlinkRunWithRPr(XWPFHyperlinkRun hyperlinkRun, IRunBody resultRunBody) {
        if (resultRunBody instanceof XWPFParagraph resultParagraph) {
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHyperlink resultHyperlink = resultParagraph.getCTP().addNewHyperlink();
            resultHyperlink.addNewR();
            XWPFHyperlinkRun resultHyperlinkRun = new XWPFHyperlinkRun(resultHyperlink, resultHyperlink.getRArray(0), resultParagraph);
            if (hyperlinkRun.getAnchor() != null) {
                resultHyperlinkRun = resultParagraph.createHyperlinkRun(hyperlinkRun.getAnchor());
            }
            // Simply copy the underlying XML bean to avoid more code
            resultHyperlinkRun.getCTR().setRPr(hyperlinkRun.getCTR().getRPr());
            copyTextOfRuns(hyperlinkRun, resultHyperlinkRun);
            handleRunStyles(resultParagraph.getDocument(), hyperlinkRun);
            handlePossibleOMath(hyperlinkRun, resultRunBody);
            return resultHyperlinkRun;
        }
        return null;
    }

    private XWPFRun createRunWithRPr(XWPFRun run, IRunBody resultRunBody) {
        if (resultRunBody instanceof XWPFParagraph resultParagraph) {
            XWPFRun resultRun = resultParagraph.createRun();
            // Simply copy the underlying XML bean to avoid more code
            resultRun.getCTR().setRPr(run.getCTR().getRPr());
            copyTextOfRuns(run, resultRun);
            handleRunStyles(resultParagraph.getDocument(), run);
            handlePossibleOMath(run, resultRunBody);
            return resultRun;
        }
        return null;
    }

    private void handlePossibleOMath(XWPFRun run, IRunBody resultRunBody) {
        if (resultRunBody instanceof XWPFParagraph resultParagraph) {
            try (XmlCursor cursor = run.getCTR().newCursor()) {
                cursor.toNextSibling();
                XmlObject xmlObject = cursor.getObject();
                if (xmlObject instanceof CTOMath ctOMath) {
                    resultParagraph.getCTP().addNewOMath();
                    resultParagraph.getCTP().setOMathArray(resultParagraph.getCTP().getOMathArray().length - 1, ctOMath);
                }
            }
        }
    }

    private void handleRunStyles(IBody resultBody, IRunElement runElement) {
        // If we have runElement styles we need merging those styles of the two different documents
        XWPFDocument document = null;
        String styleID = null;
        if (runElement instanceof XWPFRun run) {
            document = run.getDocument();
            styleID = run.getStyle();
        }
        if (document == null || styleID == null || styleID.isEmpty()) {
            return;
        }
        XWPFDocument resultDocument = null;
        if (resultBody instanceof XWPFDocument) {
            resultDocument = (XWPFDocument) resultBody;
        } else if (resultBody instanceof XWPFTableCell resultTableCell) {
            resultDocument = resultTableCell.getXWPFDocument();
        }
        if (resultDocument != null) {
            XWPFStyles styles = document.getStyles();
            XWPFStyles resultStyles = resultDocument.getStyles();
            XWPFStyle style = styles.getStyle(styleID);
            // Merge each used styles, also the related ones
            for (XWPFStyle relatedStyle : styles.getUsedStyleList(style)) {
                if (resultStyles.getStyle(relatedStyle.getStyleId()) == null) {
                    resultStyles.addStyle(relatedStyle);
                }
            }
        }
    }

    private void traverseTableRows(List<XWPFTableRow> tableRows, XWPFTable resultTable) {
        for (XWPFTableRow tableRow : tableRows) {
            XWPFTableRow resultTableRow = createTableRowWithTrPr(tableRow, resultTable);
            traverseTableCells(tableRow.getTableICells(), resultTableRow);
        }
    }

    private XWPFTableRow createTableRowWithTrPr(XWPFTableRow tableRow, XWPFTable resultTable) {
        XWPFTableRow resultTableRow = resultTable.createRow();
        for (int i = 1; i <= resultTableRow.getTableCells().size(); i++) {
            // Table row should be empty at first
            resultTableRow.removeCell(i - 1);
        }
        // Simply copy the underlying XML bean to avoid more code
        resultTableRow.getCtRow().setTrPr(tableRow.getCtRow().getTrPr());
        return resultTableRow;
    }

    private void traverseTableCells(List<ICell> tableICells, XWPFTableRow resultTableRow) {
        for (ICell tableICell : tableICells) {
            if (tableICell instanceof XWPFTableCell tableCell) {
                XWPFTableCell resultTableCell = createTableCellWithTcPr(tableCell, resultTableRow);
                traverseBodyElements(tableCell.getBodyElements(), resultTableCell);
            }
        }
    }

    private XWPFTableCell createTableCellWithTcPr(XWPFTableCell tableCell, XWPFTableRow resultTableRow) {
        XWPFTableCell resultTableCell = resultTableRow.createCell();
        resultTableCell.removeParagraph(0);
        // Simply copy the underlying XML bean to avoid more code
        resultTableCell.getCTTc().setTcPr(tableCell.getCTTc().getTcPr());
        return resultTableCell;
    }

    private void traversePictures(IRunElement runElement, IRunElement resultRunElement) {
        List<XWPFPicture> pictures = null;
        if (runElement instanceof XWPFFieldRun fieldRun) {
            pictures = fieldRun.getEmbeddedPictures();
        } else if (runElement instanceof XWPFHyperlinkRun) {
            XWPFHyperlinkRun hyperlinkRun = (XWPFHyperlinkRun) resultRunElement;
            pictures = hyperlinkRun.getEmbeddedPictures();
        } else if (runElement instanceof XWPFRun run) {
            pictures = run.getEmbeddedPictures();
        }
        if (pictures != null) {
            for (XWPFPicture picture : pictures) {
                XWPFPictureData pictureData = picture.getPictureData();
                createPictureWithDrawing(runElement, picture, pictureData, resultRunElement);
            }
        }
    }

    private void createPictureWithDrawing(
            IRunElement runElement, XWPFPicture picture, XWPFPictureData pictureData, IRunElement resultRunElement
    ) {
        if (resultRunElement instanceof XWPFFieldRun resultFieldRun) {
            XWPFFieldRun fieldRun = (XWPFFieldRun) runElement;
            createPictureWithDrawing(fieldRun, resultFieldRun, picture, pictureData);
        } else if (resultRunElement instanceof XWPFHyperlinkRun resultHyperlinkRun) {
            XWPFHyperlinkRun hyperlinkRun = (XWPFHyperlinkRun) runElement;
            createPictureWithDrawing(hyperlinkRun, resultHyperlinkRun, picture, pictureData);
        } else if (resultRunElement instanceof XWPFRun resultRun) {
            XWPFRun run = (XWPFRun) runElement;
            createPictureWithDrawing(run, resultRun, picture, pictureData);
        }
    }

    private void createPictureWithDrawing(
            XWPFRun run, XWPFRun resultRun, XWPFPicture picture, XWPFPictureData pictureData
    ) {
        try {
            XWPFPicture resultPicture = resultRun.addPicture(
                    pictureData.getPackagePart().getInputStream(),
                    pictureData.getPictureType(),
                    pictureData.getFileName(),
                    Units.pixelToEMU((int) picture.getWidth()),
                    Units.pixelToEMU((int) picture.getDepth())
            );
            String rId = resultPicture.getCTPicture().getBlipFill().getBlip().getEmbed();
            // Simply copy the underlying XML bean to avoid more code
            resultRun.getCTR().setDrawingArray(0, run.getCTR().getDrawingArray(0));
            // But then correct the rID
            String declareNamespaces = "declare namespace a='http://schemas.openxmlformats.org/drawingml/2006/main'; ";
            XmlObject[] selectedObjects = resultRun.getCTR()
                    .getDrawingArray(0).selectPath(declareNamespaces + "$this//a:blip");
            for (XmlObject blipObject : selectedObjects) {
                if (blipObject instanceof CTBlip blip) {
                    if (blip.isSetEmbed()) {
                        blip.setEmbed(rId);
                    }
                }
            }
            // Remove rIDs to external hyperlinks to avoid corrupt document
            selectedObjects = resultRun.getCTR()
                    .getDrawingArray(0).selectPath(declareNamespaces + "$this//a:hlinkClick");
            for (XmlObject hyperlinkObject : selectedObjects) {
                if (hyperlinkObject instanceof org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink hyperlink) {
                    if (hyperlink.isSetId()) {
                        hyperlink.setId("");
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

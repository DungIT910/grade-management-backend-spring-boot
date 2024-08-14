package com.boolfly.grademanagementrestful.service.base;

import com.boolfly.grademanagementrestful.exception.base.GradeManagementRuntimeException;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

public abstract class AbstractPdfService {
    protected ByteArrayOutputStream writeFile(Paragraph title,
                                              List<String> fieldNames,
                                              List<Map<String, String>> records) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PdfWriter pdfWriter = new PdfWriter(outputStream);
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            Document document = new Document(pdfDocument);

            document.add(title);

            Table table = new Table(fieldNames.size())
                    .setTextAlignment(TextAlignment.CENTER)
                    .setHorizontalAlignment(HorizontalAlignment.CENTER);

            fieldNames.forEach(column -> {
                Paragraph paragraph = new Paragraph(column)
                        .setFontSize(9)
                        .setBold();
                Cell cell = new Cell().add(paragraph);
                table.addHeaderCell(cell);
            });

            records.forEach(row ->
                    fieldNames.forEach(column -> {
                        String value = row.getOrDefault(column, "");
                        table.addCell(new Cell().add(new Paragraph(value)).setFontSize(9));
                    })
            );

            document.add(table);
            document.close();

            return outputStream;
        } catch (Exception e) {
            throw new GradeManagementRuntimeException(e);
        }
    }
}

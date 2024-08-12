package com.boolfly.grademanagementrestful.service.base;

import com.boolfly.grademanagementrestful.exception.base.GradeManagementRuntimeException;
import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.CSVWriterBuilder;
import com.opencsv.ICSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public abstract class AbstractCsvService {
    protected ByteArrayOutputStream writeFile(List<String> fieldNames,
                                              List<Map<String, String>> records) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            ICSVWriter csvWriter = new CSVWriterBuilder(outputStreamWriter)
                    .withQuoteChar(ICSVWriter.NO_QUOTE_CHARACTER)
                    .build();

            csvWriter.writeNext(fieldNames.toArray(new String[0]));

            records.forEach(entry ->
                    csvWriter.writeNext(
                            fieldNames.stream()
                                    .map(fieldName -> entry.getOrDefault(fieldName, ""))
                                    .toArray(String[]::new)
                    )
            );

            csvWriter.close();
            outputStreamWriter.close();

            return outputStream;
        } catch (Exception e) {
            throw new GradeManagementRuntimeException(e);
        }
    }

    protected void readFile(MultipartFile file, Consumer<Map<String, String>> processLine) {
        try (CSVReaderHeaderAware csvReader = new CSVReaderHeaderAware(new InputStreamReader(file.getInputStream()))) {
            Map<String, String> line;
            while ((line = csvReader.readMap()) != null) {
                processLine.accept(line);
            }
        } catch (IOException | CsvValidationException e) {
            throw new GradeManagementRuntimeException(e);
        }
    }
}

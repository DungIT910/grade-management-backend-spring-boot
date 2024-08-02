package com.boolfly.grademanagementrestful.service.impl;

import com.boolfly.grademanagementrestful.api.dto.general.BatchRequest;
import com.boolfly.grademanagementrestful.api.dto.grade.MaingradeUpdateRequest;
import com.boolfly.grademanagementrestful.api.dto.grade.SearchGradeRequest;
import com.boolfly.grademanagementrestful.api.dto.grade.SubgradeUpdateRequest;
import com.boolfly.grademanagementrestful.builder.base.SearchParamsBuilder;
import com.boolfly.grademanagementrestful.builder.grade.GradeSearchParamsBuilder;
import com.boolfly.grademanagementrestful.constant.GenericConstant;
import com.boolfly.grademanagementrestful.constant.maingrade.MaingradeConstant;
import com.boolfly.grademanagementrestful.constant.user.UserConstant;
import com.boolfly.grademanagementrestful.domain.*;
import com.boolfly.grademanagementrestful.domain.model.course.CourseStatus;
import com.boolfly.grademanagementrestful.domain.model.maingrade.MaingradeStatus;
import com.boolfly.grademanagementrestful.domain.model.subcol.SubcolStatus;
import com.boolfly.grademanagementrestful.domain.model.subgrade.SubgradeStatus;
import com.boolfly.grademanagementrestful.exception.base.GradeManagementRuntimeException;
import com.boolfly.grademanagementrestful.exception.course.CourseNotFoundException;
import com.boolfly.grademanagementrestful.exception.grade.MaingradeNotFoundException;
import com.boolfly.grademanagementrestful.exception.grade.UpdateErrorMaingradeBatchException;
import com.boolfly.grademanagementrestful.exception.grade.UpdateErrorSubgradeBatchException;
import com.boolfly.grademanagementrestful.exception.subcol.SubcolNotFoundException;
import com.boolfly.grademanagementrestful.exception.user.StudentNotFoundException;
import com.boolfly.grademanagementrestful.repository.*;
import com.boolfly.grademanagementrestful.service.GradeService;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.CSVWriterBuilder;
import com.opencsv.ICSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import com.querydsl.core.types.dsl.BooleanExpression;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GradeServiceImpl implements GradeService {
    private final MaingradeRepository maingradeRepository;
    private final SubgradeRepository subgradeRepository;
    private final SubcolRepository subcolRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Override
    public Page<Maingrade> getGrades(int page, int size, SearchGradeRequest request) {
        SearchParamsBuilder paramsBuilder = GradeSearchParamsBuilder.from(page, size, request);
        Optional<BooleanExpression> predicate = paramsBuilder.getCommonCriteria();
        Pageable pageable = paramsBuilder.getPageable();
        return predicate.map(pre -> maingradeRepository.findAll(pre, pageable))
                .orElseGet(() -> maingradeRepository.findAll(pageable));
    }

    @Override
    public Maingrade updateMaingrade(MaingradeUpdateRequest request) {
        Long maingradeId = TSID.from(request.getMaingradeId()).toLong();
        return maingradeRepository.findByIdAndStatus(maingradeId, MaingradeStatus.ACTIVE)
                .map(maingrade -> {
                    Optional.ofNullable(request.getMidtermGrade())
                            .ifPresent(maingrade::setMidtermGrade);
                    Optional.ofNullable(request.getFinalGrade())
                            .ifPresent(maingrade::setFinalGrade);
                    return maingradeRepository.save(maingrade);
                }).orElseThrow(MaingradeNotFoundException::new);
    }

    @Override
    public Subgrade updateSubgrade(SubgradeUpdateRequest request) {
        TSID subcolId = TSID.from(request.getSubcolId());
        TSID studentId = TSID.from(request.getStudentId());
        return subcolRepository.findByIdAndStatus(subcolId.toLong(), SubcolStatus.ACTIVE)
                .map(subcol -> userRepository.findByIdAndActiveTrue(studentId.toLong())
                        .map(student -> Optional.ofNullable(addOrUpdateSubgrade(subcol, student, request.getGrade()))
                                .orElseThrow(MaingradeNotFoundException::new))
                        .orElseThrow(StudentNotFoundException::new))
                .orElseThrow(() -> new SubcolNotFoundException(subcolId.toString()));
    }

    @Override
    public List<Maingrade> updateMaingradeBatch(BatchRequest<MaingradeUpdateRequest> request) {
        List<MaingradeUpdateRequest> errorList = new ArrayList<>();
        return Optional.ofNullable(request.getBatch())
                .map(batch -> {
                    List<Maingrade> mgList = new ArrayList<>();
                    batch.forEach(mq -> maingradeRepository.findByIdAndStatus(TSID.from(mq.getMaingradeId()).toLong(), MaingradeStatus.ACTIVE)
                            .ifPresentOrElse(mg -> {
                                if (mq.isUpdatedMidtermGrade()) {
                                    mg.setMidtermGrade(mq.getMidtermGrade());
                                }
                                if (mq.isUpdateFinalGrade()) {
                                    mg.setFinalGrade(mq.getFinalGrade());
                                }
                                mgList.add(mg);
                            }, () -> errorList.add(mq))
                    );
                    List<Maingrade> maingradeList = maingradeRepository.saveAll(mgList);
                    if (!errorList.isEmpty()) {
                        throw new UpdateErrorMaingradeBatchException(errorList.toString());
                    }
                    return maingradeList;
                }).orElseGet(List::of);
    }

    @Override
    public List<Subgrade> updateSubgradeBatch(BatchRequest<SubgradeUpdateRequest> request) {
        List<SubgradeUpdateRequest> errorList = new ArrayList<>();
        return Optional.ofNullable(request.getBatch())
                .map(batch -> {
                    List<Subgrade> subgradeList = new ArrayList<>();
                    batch.forEach(sub -> {
                        TSID subcolId = TSID.from(sub.getSubcolId());
                        TSID studentId = TSID.from(sub.getStudentId());
                        subcolRepository.findByIdAndStatus(subcolId.toLong(), SubcolStatus.ACTIVE)
                                .flatMap(subcol -> userRepository.findByIdAndActiveTrue(studentId.toLong())
                                        .flatMap(student -> Optional.ofNullable(addOrUpdateSubgrade(subcol, student, sub.getGrade()))))
                                .ifPresentOrElse(subgradeList::add, () -> errorList.add(sub));
                    });
                    if (!errorList.isEmpty()) {
                        throw new UpdateErrorSubgradeBatchException(errorList.toString());
                    }
                    return subgradeList;
                }).orElseGet(List::of);
    }

    private Subgrade addOrUpdateSubgrade(Subcol subcol, User student, Double grade) {
        Long studentId = student.getId();
        return subgradeRepository.findBySubcol_IdAndStudent_IdAndStatus(subcol.getId(), studentId, SubgradeStatus.ACTIVE)
                .map(subgrade -> {
                    subgrade.setGrade(grade);
                    return subgradeRepository.save(subgrade);
                }).orElseGet(() -> maingradeRepository.findByCourse_IdAndStudent_IdAndStatus(
                                subcol.getCourse().getId(),
                                studentId,
                                MaingradeStatus.ACTIVE)
                        .map(maingrade -> subgradeRepository.save(
                                Subgrade.builder()
                                        .id(TSID.fast().toLong())
                                        .subcol(subcol)
                                        .student(student)
                                        .grade(grade)
                                        .status(SubgradeStatus.ACTIVE)
                                        .build()))
                        .orElse(null));
    }

    @Override
    public ByteArrayOutputStream getSampleGradeCSV(String courseId) {
        Long courseLongId = TSID.from(courseId).toLong();

        if (courseRepository.findByIdAndStatus(courseLongId, CourseStatus.ACTIVE).isEmpty()) {
            throw new CourseNotFoundException(courseId);
        }

        if (maingradeRepository.findAllByCourse_IdAndStatus(courseLongId, MaingradeStatus.ACTIVE).isEmpty()) {
            throw new MaingradeNotFoundException();
        }

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            ICSVWriter csvWriter = new CSVWriterBuilder(outputStreamWriter)
                    .withQuoteChar(ICSVWriter.NO_QUOTE_CHARACTER)
                    .build();

            List<String> fieldNames = new ArrayList<>(List.of(
                    MaingradeConstant.MAINGRADEID_CSV_FIELD
            ));

            fieldNames.addAll(getGenericFieldNames(courseLongId));
            csvWriter.writeNext(fieldNames.toArray(new String[0]));

            csvWriter.close();
            outputStreamWriter.close();

            return outputStream;
        } catch (Exception e) {
            throw new GradeManagementRuntimeException(e);
        }
    }

    @Override
    public List<Maingrade> updateGradesCSV(String courseId, MultipartFile request) {
        Long courseLongId = TSID.from(courseId).toLong();
        List<Maingrade> maingradeList = new ArrayList<>();

        if (courseRepository.findByIdAndStatus(courseLongId, CourseStatus.ACTIVE).isEmpty()) {
            throw new CourseNotFoundException(courseId);
        }

        Set<String> scNameSet = subcolRepository
                .findAllByCourse_IdAndStatus(courseLongId, SubcolStatus.ACTIVE)
                .stream().map(Subcol::getName)
                .collect(Collectors.toSet());

        try (CSVReaderHeaderAware csvReader = new CSVReaderHeaderAware(new InputStreamReader(request.getInputStream()))) {
            Map<String, String> line;
            while ((line = csvReader.readMap()) != null) {
                Long maingradeId = TSID.from(line.get(MaingradeConstant.MAINGRADEID_CSV_FIELD)).toLong();
                Double midtermGrade = parseDouble(line.get(MaingradeConstant.MIDTERMGRADE_CSV_FIELD));
                Double finalGrade = parseDouble(line.get(MaingradeConstant.FINALGRADE_CSV_FIELD));

                Maingrade maingrade = maingradeRepository.findByIdAndStatus(maingradeId, MaingradeStatus.ACTIVE)
                        .orElseThrow(MaingradeNotFoundException::new);
                maingrade.setMidtermGrade(midtermGrade);
                maingrade.setFinalGrade(finalGrade);
                maingradeList.add(maingradeRepository.save(maingrade));

                for (String scName : scNameSet) {
                    Subcol subcol = subcolRepository.findByNameAndStatus(scName, SubcolStatus.ACTIVE)
                            .orElseThrow(() -> new SubcolNotFoundException(scName));
                    User user = userRepository.findByIdAndActiveTrue(maingrade.getStudent().getId())
                            .orElseThrow(StudentNotFoundException::new);

                    Double subgradeValue = parseDouble(line.get(scName));
                    addOrUpdateSubgrade(subcol, user, subgradeValue);
                }
            }
        } catch (IOException | CsvValidationException e) {
            throw new GradeManagementRuntimeException(e);
        }
        return maingradeList;
    }

    @Override
    public ByteArrayOutputStream getAllGradesCSV(String courseId) {
        Long courseLongId = TSID.from(courseId).toLong();

        if (courseRepository.findByIdAndStatus(courseLongId, CourseStatus.ACTIVE).isEmpty()) {
            throw new CourseNotFoundException(courseId);
        }

        if (maingradeRepository.findAllByCourse_IdAndStatus(courseLongId, MaingradeStatus.ACTIVE).isEmpty()) {
            throw new MaingradeNotFoundException();
        }

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            ICSVWriter csvWriter = new CSVWriterBuilder(outputStreamWriter)
                    .withQuoteChar(ICSVWriter.NO_QUOTE_CHARACTER)
                    .build();

            List<String> fieldNames = new ArrayList<>(List.of(
                    GenericConstant.INDEX,
                    UserConstant.STUDENT_ID,
                    UserConstant.LAST_NAME,
                    UserConstant.FIRST_NAME
            ));
            fieldNames.addAll(getGenericFieldNames(courseLongId));

            csvWriter.writeNext(fieldNames.toArray(new String[0]));

            List<Map<String, String>> gradeRecords = getGradeRecords(courseLongId);

            gradeRecords.forEach(gradeRecord ->
                    csvWriter.writeNext(
                            fieldNames.stream()
                                    .map(fieldName -> gradeRecord.getOrDefault(fieldName, ""))
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

    @Override
    public ByteArrayOutputStream getAllGradesPDF(String courseId) {
        Long courseLongId = TSID.from(courseId).toLong();
        String courseName = courseRepository
                .findByIdAndStatus(courseLongId, CourseStatus.ACTIVE)
                .map(Course::getName)
                .orElseThrow(() -> new CourseNotFoundException(courseId));
        if (maingradeRepository.findAllByCourse_IdAndStatus(courseLongId, MaingradeStatus.ACTIVE).isEmpty()) {
            throw new MaingradeNotFoundException();
        }
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PdfWriter pdfWriter = new PdfWriter(outputStream);
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            Document document = new Document(pdfDocument);

            Paragraph title = new Paragraph("GRADE REPORT FOR COURSE " + courseName.toUpperCase())
                    .setBold()
                    .setFontSize(18)
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(title);

            List<String> fieldNames = new ArrayList<>(List.of(
                    GenericConstant.INDEX,
                    UserConstant.STUDENT_ID,
                    UserConstant.LAST_NAME,
                    UserConstant.FIRST_NAME
            ));
            fieldNames.addAll(getGenericFieldNames(courseLongId));

            Table table = new Table(fieldNames.size())
                    .setTextAlignment(TextAlignment.CENTER)
                    .setHorizontalAlignment(HorizontalAlignment.CENTER);

            fieldNames.forEach(column -> table.addHeaderCell(new Cell().add(new Paragraph(column).setFontSize(9).setBold())));

            List<Map<String, String>> gradeRecords = getGradeRecords(courseLongId);

            gradeRecords.forEach(gradeRecord ->
                    fieldNames.forEach(column -> {
                        String value = gradeRecord.getOrDefault(column, "");
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

    private Double parseDouble(String value) {
        return Optional.ofNullable(value)
                .filter(v -> !v.trim().isEmpty())
                .map(Double::valueOf)
                .orElse(null);
    }

    private List<Map<String, String>> getGradeRecords(Long courseLongId) {
        List<Map<String, String>> gradeRecords = new ArrayList<>();
        AtomicInteger index = new AtomicInteger(1);

        maingradeRepository.findAllByCourse_IdAndStatus(courseLongId, MaingradeStatus.ACTIVE)
                .forEach(mg -> {
                    Map<String, String> gradeRecord = new HashMap<>();
                    gradeRecord.put(GenericConstant.INDEX, String.valueOf(index.get()));
                    gradeRecord.put(UserConstant.STUDENT_ID, TSID.from(mg.getStudent().getId()).toString());
                    gradeRecord.put(UserConstant.LAST_NAME, mg.getStudent().getLastName());
                    gradeRecord.put(UserConstant.FIRST_NAME, mg.getStudent().getFirstName());
                    gradeRecord.put(MaingradeConstant.MIDTERMGRADE_CSV_FIELD, mg.getMidtermGrade() != null ? String.valueOf(mg.getMidtermGrade()) : "");
                    gradeRecord.put(MaingradeConstant.FINALGRADE_CSV_FIELD, mg.getFinalGrade() != null ? String.valueOf(mg.getFinalGrade()) : "");

                    subcolRepository.findAllByCourse_IdAndStatus(courseLongId, SubcolStatus.ACTIVE).forEach(subcol -> {
                        String subcolName = subcol.getName();
                        String grade = subgradeRepository.findBySubcol_IdAndStudent_IdAndStatus(
                                        subcol.getId(),
                                        mg.getStudent().getId(),
                                        SubgradeStatus.ACTIVE
                                ).map(sg -> sg.getGrade() != null ? String.valueOf(sg.getGrade()) : "")
                                .orElse("");
                        gradeRecord.put(subcolName, grade);
                    });

                    gradeRecords.add(gradeRecord);
                    index.getAndIncrement();
                });

        return gradeRecords;
    }

    private List<String> getGenericFieldNames(Long courseId) {
        List<String> fieldNames = new ArrayList<>(List.of(
                MaingradeConstant.MIDTERMGRADE_CSV_FIELD,
                MaingradeConstant.FINALGRADE_CSV_FIELD
        ));
        subcolRepository.findAllByCourse_IdAndStatus(courseId, SubcolStatus.ACTIVE)
                .stream()
                .map(Subcol::getName)
                .forEach(fieldNames::add);
        return fieldNames;
    }
}

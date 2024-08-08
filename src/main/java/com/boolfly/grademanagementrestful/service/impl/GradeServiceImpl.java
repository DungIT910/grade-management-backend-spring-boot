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
import com.boolfly.grademanagementrestful.exception.base.GradeManagementRuntimeException;
import com.boolfly.grademanagementrestful.exception.course.CourseNotFoundException;
import com.boolfly.grademanagementrestful.exception.grade.LockedGradeException;
import com.boolfly.grademanagementrestful.exception.grade.MaingradeNotFoundException;
import com.boolfly.grademanagementrestful.exception.subcol.SubcolNotFoundException;
import com.boolfly.grademanagementrestful.exception.user.StudentNotFoundException;
import com.boolfly.grademanagementrestful.listener.event.MailEvent;
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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
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

@Service
@RequiredArgsConstructor
public class GradeServiceImpl implements GradeService {
    private final ApplicationEventPublisher eventPublisher;
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
        return maingradeRepository.findByIdAndStatusNot(maingradeId, MaingradeStatus.INACTIVE)
                .map(maingrade ->
                        maingradeRepository.save(
                                setMaingradeFromRequest(
                                        maingrade,
                                        request.getMidtermGrade(),
                                        request.getFinalGrade(),
                                        request.isLocked()
                                )
                        )
                )
                .orElseThrow(MaingradeNotFoundException::new);
    }

    @Override
    public Subgrade updateSubgrade(SubgradeUpdateRequest request) {
        TSID subcolId = TSID.from(request.getSubcolId());
        Long studentLongId = TSID.from(request.getStudentId()).toLong();

        Subcol subcol = subcolRepository.findByIdAndStatus(subcolId.toLong(), SubcolStatus.ACTIVE)
                .orElseThrow(() -> new SubcolNotFoundException(subcolId.toString()));

        User student = userRepository.findByIdAndActiveTrue(studentLongId)
                .orElseThrow(StudentNotFoundException::new);

        Course course = subcol.getCourse();
        Long courseId = course.getId();
        if (maingradeRepository.notExistsByCourse_IdAndStudent_IdAndStatusNot(
                courseId,
                studentLongId,
                MaingradeStatus.INACTIVE)) {
            throw new MaingradeNotFoundException();
        }

        if (maingradeRepository.existsByCourse_IdAndStudent_IdAndStatus(
                courseId,
                studentLongId,
                MaingradeStatus.LOCKED)) {
            throw new LockedGradeException();
        }

        return addOrUpdateSubgrade(subcol, student, request.getGrade());
    }

    @Override
    @Transactional
    public List<Maingrade> updateMaingradeBatch(BatchRequest<MaingradeUpdateRequest> request) {
        List<MaingradeUpdateRequest> batch = Optional.ofNullable(request.getBatch())
                .orElseGet(List::of);
        List<Maingrade> mgList = new ArrayList<>();
        batch.forEach(item -> {
                    Long maingradeId = TSID.from(item.getMaingradeId()).toLong();
                    Maingrade mg = maingradeRepository.findByIdAndStatusNot(
                                    maingradeId,
                                    MaingradeStatus.INACTIVE
                            )
                            .orElseThrow(MaingradeNotFoundException::new);

                    mgList.add(
                            setMaingradeFromRequest(
                                    mg,
                                    item.getMidtermGrade(),
                                    item.getFinalGrade(),
                                    item.isLocked()
                            )
                    );
                }
        );
        return maingradeRepository.saveAll(mgList);
    }

    private Maingrade setMaingradeFromRequest(Maingrade mg,
                                              Double midtermGrade,
                                              Double finalGrade,
                                              boolean locked) {
        if (MaingradeStatus.LOCKED.equals(mg.getStatus())) {
            throw new LockedGradeException();
        }

        mg.setMidtermGrade(midtermGrade);
        mg.setFinalGrade(finalGrade);

        if (locked) {
            String courseName = mg.getCourse().getName();
            String toEmail = mg.getStudent().getEmail();
            eventPublisher.publishEvent(
                    MailEvent
                            .builder()
                            .source(this)
                            .courseName(courseName)
                            .toEmail(toEmail)
                            .build()
            );
            mg.setStatus(MaingradeStatus.LOCKED);
        }
        return mg;
    }

    @Override
    public List<Subgrade> updateSubgradeBatch(BatchRequest<SubgradeUpdateRequest> request) {
        List<SubgradeUpdateRequest> batch = Optional.ofNullable(request.getBatch())
                .orElseGet(List::of);
        List<Subgrade> subgradeList = new ArrayList<>();

        batch.forEach(this::updateSubgrade);
        return subgradeList;
    }

    private Subgrade addOrUpdateSubgrade(Subcol subcol, User student, Double grade) {
        Long studentLongId = student.getId();
        Long subcolLongId = subcol.getId();

        Subgrade subgrade = subgradeRepository.findBySubcol_IdAndStudent_Id(subcolLongId, studentLongId)
                .orElseGet(() -> subgradeRepository.save(
                                Subgrade.builder()
                                        .id(TSID.fast().toLong())
                                        .subcol(subcol)
                                        .student(student)
                                        .build()
                        )
                );
        subgrade.setGrade(grade);

        return subgradeRepository.save(subgrade);
    }

    @Override
    public ByteArrayOutputStream getSampleGradeCSV(String courseId) {
        Long courseLongId = TSID.from(courseId).toLong();

        if (courseRepository.notExistsByIdAndStatusNot(courseLongId, CourseStatus.INACTIVE)) {
            throw new CourseNotFoundException(courseId);
        }

        if (maingradeRepository.notExistsByCourse_IdAndStatusNot(courseLongId, MaingradeStatus.INACTIVE)) {
            throw new MaingradeNotFoundException();
        }

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            ICSVWriter csvWriter = new CSVWriterBuilder(outputStreamWriter)
                    .withQuoteChar(ICSVWriter.NO_QUOTE_CHARACTER)
                    .build();

            List<String> fieldNames = new ArrayList<>(List.of(
                    MaingradeConstant.MAINGRADE_ID,
                    MaingradeConstant.LOCKED
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
    @Transactional
    public List<Maingrade> updateGradesCSV(String courseId, MultipartFile request) {
        Long courseLongId = TSID.from(courseId).toLong();
        List<Maingrade> maingradeList = new ArrayList<>();

        if (courseRepository.notExistsByIdAndStatusNot(courseLongId, CourseStatus.INACTIVE)) {
            throw new CourseNotFoundException(courseId);
        }

        List<Subcol> subcolList = subcolRepository.findAllByCourse_IdAndStatus(courseLongId, SubcolStatus.ACTIVE);

        try (CSVReaderHeaderAware csvReader = new CSVReaderHeaderAware(new InputStreamReader(request.getInputStream()))) {
            Map<String, String> line;
            while ((line = csvReader.readMap()) != null) {
                Long maingradeId = TSID.from(line.get(MaingradeConstant.MAINGRADE_ID)).toLong();
                Double midtermGrade = parseDouble(line.get(MaingradeConstant.MIDTERM_GRADE));
                Double finalGrade = parseDouble(line.get(MaingradeConstant.FINAL_GRADE));
                boolean locked = Boolean.parseBoolean(line.get(MaingradeConstant.LOCKED));

                Maingrade maingrade = maingradeRepository
                        .findByIdAndStatusNot(maingradeId, MaingradeStatus.INACTIVE)
                        .orElseThrow(MaingradeNotFoundException::new);

                setMaingradeFromRequest(maingrade, midtermGrade, finalGrade, locked);

                User student = maingrade.getStudent();
                for (Subcol subcol : subcolList) {
                    Double subgradeValue = parseDouble(line.get(subcol.getName()));
                    addOrUpdateSubgrade(subcol, student, subgradeValue);
                }

                maingradeList.add(maingradeRepository.save(maingrade));
            }
        } catch (IOException | CsvValidationException e) {
            throw new GradeManagementRuntimeException(e);
        }
        return maingradeList;
    }

    @Override
    public ByteArrayOutputStream getAllGradesCSV(String courseId) {
        Long courseLongId = TSID.from(courseId).toLong();

        if (courseRepository.notExistsByIdAndStatusNot(courseLongId, CourseStatus.INACTIVE)) {
            throw new CourseNotFoundException(courseId);
        }

        if (maingradeRepository.notExistsByCourse_IdAndStatusNot(courseLongId, MaingradeStatus.INACTIVE)) {
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
                .findNameByIdAndStatus(courseLongId, CourseStatus.ACTIVE)
                .orElseThrow(() -> new CourseNotFoundException(courseId));

        if (maingradeRepository.notExistsByCourse_IdAndStatusNot(courseLongId, MaingradeStatus.INACTIVE)) {
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

            fieldNames.forEach(column -> {
                Paragraph paragraph = new Paragraph(column)
                        .setFontSize(9)
                        .setBold();
                Cell cell = new Cell().add(paragraph);
                table.addHeaderCell(cell);
            });

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
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        return Double.valueOf(value);
    }

    private List<Map<String, String>> getGradeRecords(Long courseLongId) {
        List<Map<String, String>> gradeRecords = new ArrayList<>();
        AtomicInteger index = new AtomicInteger(1);

        maingradeRepository.findAllByCourse_IdAndStatusNot(courseLongId, MaingradeStatus.INACTIVE)
                .forEach(mg -> {
                    User student = mg.getStudent();
                    Map<String, String> gradeRecord = new HashMap<>();
                    gradeRecord.put(GenericConstant.INDEX, String.valueOf(index.get()));
                    gradeRecord.put(UserConstant.STUDENT_ID, TSID.from(student.getId()).toString());
                    gradeRecord.put(UserConstant.LAST_NAME, student.getLastName());
                    gradeRecord.put(UserConstant.FIRST_NAME, student.getFirstName());
                    gradeRecord.put(
                            MaingradeConstant.MIDTERM_GRADE,
                            Optional.ofNullable(mg.getMidtermGrade())
                                    .map(Objects::toString)
                                    .orElse("")
                    );
                    gradeRecord.put(
                            MaingradeConstant.FINAL_GRADE,
                            Optional.ofNullable(mg.getFinalGrade())
                                    .map(Objects::toString)
                                    .orElse("")
                    );

                    subcolRepository.findAllByCourse_IdAndStatus(courseLongId, SubcolStatus.ACTIVE)
                            .forEach(subcol -> {
                                String subcolName = subcol.getName();
                                String grade = subgradeRepository.findBySubcol_IdAndStudent_Id(
                                                subcol.getId(),
                                                student.getId()
                                        )
                                        .map(Subgrade::getGrade)
                                        .map(Objects::toString)
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
                MaingradeConstant.MIDTERM_GRADE,
                MaingradeConstant.FINAL_GRADE
        ));
        subcolRepository.findAllByCourse_IdAndStatus(courseId, SubcolStatus.ACTIVE)
                .stream()
                .map(Subcol::getName)
                .forEach(fieldNames::add);
        return fieldNames;
    }
}

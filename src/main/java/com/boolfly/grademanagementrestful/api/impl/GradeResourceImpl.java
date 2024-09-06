package com.boolfly.grademanagementrestful.api.impl;

import com.boolfly.grademanagementrestful.api.GradeResource;
import com.boolfly.grademanagementrestful.api.dto.general.BatchRequest;
import com.boolfly.grademanagementrestful.api.dto.general.BatchResponse;
import com.boolfly.grademanagementrestful.api.dto.general.PageResponse;
import com.boolfly.grademanagementrestful.api.dto.grade.*;
import com.boolfly.grademanagementrestful.domain.Maingrade;
import com.boolfly.grademanagementrestful.exception.base.GradeManagementRuntimeException;
import com.boolfly.grademanagementrestful.mapper.GradeMapper;
import com.boolfly.grademanagementrestful.mapper.SubgradeMapper;
import com.boolfly.grademanagementrestful.repository.custom.model.PairSubgradeSubcol;
import com.boolfly.grademanagementrestful.service.CsvService;
import com.boolfly.grademanagementrestful.service.GradeService;
import com.boolfly.grademanagementrestful.service.PdfService;
import com.boolfly.grademanagementrestful.service.SubgradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/grades")
@RequiredArgsConstructor
public class GradeResourceImpl implements GradeResource {
    private static final GradeMapper gradeMapper = GradeMapper.INSTANCE;
    private static final SubgradeMapper subgradeMapper = SubgradeMapper.INSTANCE;
    private final GradeService gradeService;
    private final SubgradeService subgradeService;
    private final CsvService csvService;
    private final PdfService pdfService;

    @PostMapping("/search")
    @Override
    public PageResponse<GradeResponse> getGrades(int page, int size, SearchGradeRequest request) {
        try {
            Page<Maingrade> pageMaingrade = gradeService.getGrades(page, size, request);
            List<Maingrade> maingradeList = pageMaingrade.getContent();
            Map<String, List<PairSubgradeSubcol>> mapPairSubgradeSubcol = subgradeService.getPairSubgradeSubcol(maingradeList);

            return PageResponse.<GradeResponse>builder()
                    .content(maingradeList.stream()
                            .map(gradeMapper::toGradeResponse)
                            .map(grade -> {
                                String maingradeId = grade.getMaingradeId();
                                List<SubgradeResponse> subgradeResponseList = mapPairSubgradeSubcol.get(maingradeId)
                                        .stream()
                                        .map(subgradeMapper::toSubgradeResponse)
                                        .toList();
                                return grade.withSubgradeList(subgradeResponseList);
                            })
                            .toList())
                    .build();
        } catch (Exception e) {
            throw new GradeManagementRuntimeException(e);
        }
    }

    @PutMapping("/maingrades")
    @Override
    public MaingradeDetailsResponse updateMaingrade(MaingradeUpdateRequest request) {
        try {
            return gradeMapper.toMaingradeDetailsResponse(gradeService.updateMaingrade(request));
        } catch (Exception e) {
            throw new GradeManagementRuntimeException(e);
        }
    }

    @PutMapping("/maingrades/batch")
    @Override
    public BatchResponse<MaingradeDetailsResponse> updateMaingradeBatch(BatchRequest<MaingradeUpdateRequest> request) {
        try {
            BatchResponse<MaingradeDetailsResponse> response = new BatchResponse<>();
            List<MaingradeDetailsResponse> mgList = gradeService.updateMaingradeBatch(request).stream().map(gradeMapper::toMaingradeDetailsResponse).toList();
            response.setBatch(mgList);
            return response;
        } catch (Exception e) {
            throw new GradeManagementRuntimeException(e);
        }
    }

    @PutMapping("/subgrades/batch")
    @Override
    public BatchResponse<SubgradeDetailsResponse> updateSubgradeBatch(BatchRequest<SubgradeUpdateRequest> request) {
        try {
            BatchResponse<SubgradeDetailsResponse> response = new BatchResponse<>();
            List<SubgradeDetailsResponse> sgList = gradeService.updateSubgradeBatch(request).stream().map(gradeMapper::toSubgradeDetailsResponse).toList();
            response.setBatch(sgList);
            return response;
        } catch (Exception e) {
            throw new GradeManagementRuntimeException(e);
        }
    }

    @GetMapping(value = "/{courseId}/sample-grade-csv")
    @PreAuthorize("@customSecurityExpression.verifyUserInCourseByCourseId(authentication.name, #courseId)")
    @Override
    public HttpEntity<InputStreamResource> getSampleCSV(@PathVariable String courseId) {
        try {
            ByteArrayOutputStream outputStream = csvService.getSampleGradeCSV(courseId, gradeService);
            InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            InputStreamResource resource = new InputStreamResource(inputStream);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=gradeInputStructure.csv");
            headers.add(HttpHeaders.CONTENT_TYPE, "text/csv");

            return new ResponseEntity<>(resource, headers, HttpStatus.OK);
        } catch (Exception e) {
            throw new GradeManagementRuntimeException(e);
        }
    }

    @PutMapping(value = "/{courseId}/csv-update",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("@customSecurityExpression.verifyUserInCourseByCourseId(authentication.name, #courseId)")
    @Override
    public BatchResponse<GradeResponse> updateGradesFromCSV(@PathVariable String courseId, MultipartFile request) {
        try {
            List<Maingrade> maingradeList = csvService.updateGradesCSV(courseId, request, gradeService::processUpdateGradeCsv);
            Map<String, List<PairSubgradeSubcol>> mapPairSubgradeSubcol = subgradeService.getPairSubgradeSubcol(maingradeList);
            BatchResponse<GradeResponse> batchResponse = new BatchResponse<>();

            batchResponse.setBatch(maingradeList.stream()
                    .map(gradeMapper::toGradeResponse)
                    .map(grade -> {
                        String maingradeId = grade.getMaingradeId();
                        List<SubgradeResponse> subgradeResponseList = mapPairSubgradeSubcol.get(maingradeId)
                                .stream()
                                .map(subgradeMapper::toSubgradeResponse)
                                .toList();
                        return grade.withSubgradeList(subgradeResponseList);
                    })
                    .toList());
            return batchResponse;
        } catch (Exception e) {
            throw new GradeManagementRuntimeException(e);
        }
    }

    @GetMapping("/{courseId}/all-grades-csv")
    @PreAuthorize("@customSecurityExpression.verifyUserInCourseByCourseId(authentication.name, #courseId)")
    @Override
    public HttpEntity<InputStreamResource> getAllGradesCSV(@PathVariable String courseId) {
        try {
            ByteArrayOutputStream outputStream = csvService.getAllGradesCSV(courseId, gradeService);
            InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            InputStreamResource resource = new InputStreamResource(inputStream);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=CourseGrade.csv");
            headers.add(HttpHeaders.CONTENT_TYPE, "text/csv");

            return new ResponseEntity<>(resource, headers, HttpStatus.OK);
        } catch (Exception e) {
            throw new GradeManagementRuntimeException(e);
        }
    }

    @GetMapping("/{courseId}/all-grades-pdf")
    @PreAuthorize("@customSecurityExpression.verifyUserInCourseByCourseId(authentication.name, #courseId)")
    @Override
    public HttpEntity<InputStreamResource> getAllGradesPDF(@PathVariable String courseId) {
        try {
            ByteArrayOutputStream outputStream = pdfService.getAllGradesPDF(courseId, gradeService);
            InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            InputStreamResource resource = new InputStreamResource(inputStream);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=CourseGrade.pdf");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");

            return new ResponseEntity<>(resource, headers, HttpStatus.OK);
        } catch (Exception e) {
            throw new GradeManagementRuntimeException(e);
        }
    }

    @PutMapping("/subgrades")
    @Override
    public SubgradeDetailsResponse updateSubgrade(SubgradeUpdateRequest request) {
        try {
            return gradeMapper.toSubgradeDetailsResponse(gradeService.updateSubgrade(request));
        } catch (Exception e) {
            throw new GradeManagementRuntimeException(e);
        }
    }
}

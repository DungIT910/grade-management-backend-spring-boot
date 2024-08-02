package com.boolfly.grademanagementrestful.api.dto.grade;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class GradeCsvUpdateRequest {
    @NotNull
    private MultipartFile file;
}

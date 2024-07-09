package com.boolfly.GradeManagementRestful.service.impl;

import com.boolfly.GradeManagementRestful.api.dto.course.SearchCourseRequest;
import com.boolfly.GradeManagementRestful.builder.base.SearchParamsBuilder;
import com.boolfly.GradeManagementRestful.builder.user.CourseSearchParamsBuilder;
import com.boolfly.GradeManagementRestful.domain.Course;
import com.boolfly.GradeManagementRestful.repository.CourseRepository;
import com.boolfly.GradeManagementRestful.service.CourseService;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;

    @Override
    public Page<Course> getCourses(int page, int size, SearchCourseRequest request) {
        SearchParamsBuilder searchParamsBuilder = CourseSearchParamsBuilder.from(page, size, request);
        Optional<BooleanExpression> expression = searchParamsBuilder.getCommonCriteria();
        Pageable pageable = searchParamsBuilder.getPageable();

        return expression.map(e ->  courseRepository.findAll(e, pageable))
                .orElseGet(() -> courseRepository.findAll(pageable));
    }
}

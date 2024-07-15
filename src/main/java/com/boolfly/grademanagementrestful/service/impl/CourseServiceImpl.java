package com.boolfly.grademanagementrestful.service.impl;

import com.boolfly.grademanagementrestful.api.dto.course.CourseAddRequest;
import com.boolfly.grademanagementrestful.api.dto.course.CourseUpdateRequest;
import com.boolfly.grademanagementrestful.api.dto.course.SearchCourseRequest;
import com.boolfly.grademanagementrestful.builder.base.SearchParamsBuilder;
import com.boolfly.grademanagementrestful.builder.course.CourseSearchParamsBuilder;
import com.boolfly.grademanagementrestful.domain.Course;
import com.boolfly.grademanagementrestful.domain.model.course.CourseStatus;
import com.boolfly.grademanagementrestful.exception.course.CourseNotFoundException;
import com.boolfly.grademanagementrestful.exception.subject.SubjectNotFoundException;
import com.boolfly.grademanagementrestful.exception.user.LecturerNotFoundException;
import com.boolfly.grademanagementrestful.repository.CourseRepository;
import com.boolfly.grademanagementrestful.repository.SubjectRepository;
import com.boolfly.grademanagementrestful.repository.UserRepository;
import com.boolfly.grademanagementrestful.service.CourseService;
import com.querydsl.core.types.dsl.BooleanExpression;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;

    @Override
    public Page<Course> getCourses(int page, int size, SearchCourseRequest request) {
        SearchParamsBuilder searchParamsBuilder = CourseSearchParamsBuilder.from(page, size, request);
        Optional<BooleanExpression> expression = searchParamsBuilder.getCommonCriteria();
        Pageable pageable = searchParamsBuilder.getPageable();

        return expression.map(e -> courseRepository.findAll(e, pageable))
                .orElseGet(() -> courseRepository.findAll(pageable));
    }

    @Override
    public Course addCourse(CourseAddRequest request) {
        TSID subjectId = TSID.from(request.getSubjectId());
        TSID lecturerId = TSID.from(request.getLecturerId());
        return subjectRepository.findById(subjectId.toLong())
                .map(subject -> userRepository.findById(lecturerId.toLong())
                        .map(lec -> courseRepository.save(Course.builder()
                                .id(TSID.fast().toLong())
                                .name(request.getName())
                                .lecturer(lec)
                                .subject(subject)
                                .status(CourseStatus.ACTIVE)
                                .build())
                        )
                        .orElseThrow(() -> new LecturerNotFoundException(lecturerId.toString()))
                )
                .orElseThrow(() -> new SubjectNotFoundException(subjectId.toString()));
    }

    @Override
    public Course updateCourse(CourseUpdateRequest request) {
        return courseRepository.findById(TSID.from(request.getCourseId()).toLong())
                .map(course -> {
                    Optional.ofNullable(request.getName())
                            .filter(name -> !name.isEmpty() && !Objects.equals(course.getName(), name))
                            .ifPresent(course::setName);
                    Optional.ofNullable(request.getLecturerId())
                            .filter(lecId -> !lecId.isEmpty() && !Objects.equals(course.getLecturer().getId(), TSID.from(lecId).toLong()))
                            .ifPresent(lecId -> userRepository.findById(TSID.from(lecId).toLong())
                                    .map(lec -> {
                                        course.setLecturer(lec);
                                        return course;
                                    })
                                    .orElseThrow(() -> new LecturerNotFoundException(request.getLecturerId()))
                            );
                    Optional.ofNullable(request.getSubjectId())
                            .filter(subjectId -> !subjectId.isEmpty() && !Objects.equals(course.getSubject().getId(), TSID.from(subjectId).toLong()))
                            .ifPresent(subjectId -> subjectRepository.findById(TSID.from(subjectId).toLong())
                                    .map(subject -> {
                                        course.setSubject(subject);
                                        return course;
                                    })
                                    .orElseThrow(() -> new SubjectNotFoundException(request.getSubjectId()))
                            );
                    course.setStartTime(request.getStartTime().atStartOfDay().toInstant(ZoneOffset.UTC));
                    course.setEndTime(request.getEndTime().atStartOfDay().toInstant(ZoneOffset.UTC));
                    course.setMinQuantity(request.getMinQuantity());
                    courseRepository.save(course);
                    return course;
                }).orElseThrow(() -> new CourseNotFoundException(request.getCourseId()));
    }

    @Override
    public void deactivateCourse(String courseId) {
        courseRepository.findById(TSID.from(courseId).toLong())
                .ifPresent(course -> {
                    if (CourseStatus.INACTIVE.equals(course.getStatus())) {
                        return;
                    }
                    course.setStatus(CourseStatus.INACTIVE);
                    courseRepository.save(course);
                });
    }
}

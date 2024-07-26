package com.boolfly.grademanagementrestful.service.impl;

import com.boolfly.grademanagementrestful.api.dto.course.CourseAddRequest;
import com.boolfly.grademanagementrestful.api.dto.course.CourseUpdateRequest;
import com.boolfly.grademanagementrestful.api.dto.course.SearchCourseRequest;
import com.boolfly.grademanagementrestful.api.dto.course.student.CourseAddStudentRequest;
import com.boolfly.grademanagementrestful.builder.base.SearchParamsBuilder;
import com.boolfly.grademanagementrestful.builder.course.CourseSearchParamsBuilder;
import com.boolfly.grademanagementrestful.domain.Course;
import com.boolfly.grademanagementrestful.domain.Maingrade;
import com.boolfly.grademanagementrestful.domain.Subcol;
import com.boolfly.grademanagementrestful.domain.User;
import com.boolfly.grademanagementrestful.domain.model.course.CourseStatus;
import com.boolfly.grademanagementrestful.domain.model.maingrade.MaingradeStatus;
import com.boolfly.grademanagementrestful.domain.model.subcol.SubcolStatus;
import com.boolfly.grademanagementrestful.domain.model.subject.SubjectStatus;
import com.boolfly.grademanagementrestful.exception.course.CourseNotFoundException;
import com.boolfly.grademanagementrestful.exception.subject.SubjectNotFoundException;
import com.boolfly.grademanagementrestful.exception.user.LecturerNotFoundException;
import com.boolfly.grademanagementrestful.repository.*;
import com.boolfly.grademanagementrestful.service.CourseService;
import com.querydsl.core.types.dsl.BooleanExpression;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;
    private final MaingradeRepository maingradeRepository;
    private final SubcolRepository subcolRepository;

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
        return courseRepository.findByIdAndStatus(TSID.from(request.getCourseId()).toLong(), CourseStatus.ACTIVE)
                .map(course -> {
                    Optional.ofNullable(request.getName())
                            .filter(name -> !name.isEmpty() && !Objects.equals(course.getName(), name))
                            .ifPresent(course::setName);
                    Optional.ofNullable(request.getLecturerId())
                            .filter(lecId -> !lecId.isEmpty() && !Objects.equals(course.getLecturer().getId(), TSID.from(lecId).toLong()))
                            .ifPresent(lecId -> userRepository.findByIdAndActiveTrue(TSID.from(lecId).toLong())
                                    .map(lec -> {
                                        course.setLecturer(lec);
                                        return course;
                                    })
                                    .orElseThrow(() -> new LecturerNotFoundException(lecId))
                            );
                    Optional.ofNullable(request.getSubjectId())
                            .filter(subjectId -> !subjectId.isEmpty() && !Objects.equals(course.getSubject().getId(), TSID.from(subjectId).toLong()))
                            .ifPresent(subjectId -> subjectRepository.findByIdAndStatus(TSID.from(subjectId).toLong(), SubjectStatus.ACTIVE)
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

    @Override
    public List<User> addStudentsToCourse(String courseId, CourseAddStudentRequest request) {
        return courseRepository.findByIdAndStatus(TSID.from(courseId).toLong(), CourseStatus.ACTIVE)
                .map(course -> {
                            List<User> students = new ArrayList<>();
                            for (String id : request.getStudentIds()) {
                                TSID studentId = TSID.from(id);
                                userRepository.findByIdAndActiveTrue(studentId.toLong())
                                        .ifPresent(student -> {
                                            maingradeRepository.save(
                                                    Maingrade.builder()
                                                            .id(TSID.fast().toLong())
                                                            .course(course)
                                                            .student(student)
                                                            .status(MaingradeStatus.ACTIVE)
                                                            .build()
                                            );
                                            students.add(student);
                                        });
                            }
                            return students;
                        }
                ).orElseThrow(() -> new CourseNotFoundException(courseId));
    }

    @Override
    public void deactivateStudent(String courseId, String studentId) {
        Long courseID = TSID.from(courseId).toLong();
        Long studentID = TSID.from(studentId).toLong();
        maingradeRepository.findByCourse_IdAndStudent_IdAndStatus(courseID, studentID, MaingradeStatus.ACTIVE)
                .ifPresent(maingrade -> {
                    if (MaingradeStatus.ACTIVE.equals(maingrade.getStatus())) {
                        maingrade.setStatus(MaingradeStatus.INACTIVE);
                        maingradeRepository.save(maingrade);
                    }
                });
    }

    @Override
    public List<Subcol> getSubcols(String courseId) {
        return courseRepository.findByIdAndStatus(TSID.from(courseId).toLong(), CourseStatus.ACTIVE)
                .map(course -> subcolRepository.findAllByCourse_IdAndStatus(course.getId(), SubcolStatus.ACTIVE))
                .orElseThrow(() -> new CourseNotFoundException(courseId));
    }
}

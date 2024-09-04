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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Course updateCourse(CourseUpdateRequest request) {
        String courseIdAsString = request.getCourseId();
        return courseRepository.findByIdAndStatus(TSID.from(courseIdAsString).toLong(), CourseStatus.ACTIVE)
                .map(course -> {
                    Optional.ofNullable(request.getName())
                            .filter(name -> !name.isEmpty() && !Objects.equals(course.getName(), name))
                            .ifPresent(course::setName);
                    Optional.ofNullable(request.getLecturerId())
                            .filter(lecId -> !lecId.isEmpty())
                            .map(TSID::from)
                            .filter(lecId -> !Objects.equals(course.getLecturer().getId(), lecId.toLong()))
                            .map(lecId -> userRepository.findByIdAndActiveTrue(lecId.toLong())
                                    .orElseThrow(() -> new LecturerNotFoundException(lecId.toString())))
                            .ifPresent(course::setLecturer);
                    Optional.ofNullable(request.getSubjectId())
                            .filter(subjectId -> !subjectId.isEmpty())
                            .map(TSID::from)
                            .filter(subjectId -> !Objects.equals(course.getSubject().getId(), subjectId.toLong()))
                            .map(subjectId -> subjectRepository.findByIdAndStatus(subjectId.toLong(), SubjectStatus.ACTIVE)
                                    .orElseThrow(() -> new SubjectNotFoundException(subjectId.toString())))
                            .ifPresent(course::setSubject);
                    course.setStartTime(request.getStartTime().atStartOfDay().toInstant(ZoneOffset.UTC));
                    course.setEndTime(request.getEndTime().atStartOfDay().toInstant(ZoneOffset.UTC));
                    course.setMinQuantity(request.getMinQuantity());
                    return course;
                })
                .map(courseRepository::save)
                .orElseThrow(() -> new CourseNotFoundException(courseIdAsString));
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deactivateCourse(String courseId) {
        courseRepository.findByIdAndStatus(TSID.from(courseId).toLong(), CourseStatus.ACTIVE)
                .ifPresent(course -> {
                    course.setStatus(CourseStatus.INACTIVE);
                    courseRepository.save(course);
                });
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<User> addStudentsToCourse(String courseId, CourseAddStudentRequest request) {
        List<String> studentIds = request.getStudentIds();
        if (studentIds == null || studentIds.isEmpty()) {
            return List.of();
        }

        List<Long> studentIdsAsLong = studentIds.stream()
                .filter(id -> id != null && !id.isEmpty())
                .map(id -> TSID.from(id).toLong())
                .toList();

        Course course = courseRepository.findByIdAndStatus(TSID.from(courseId).toLong(), CourseStatus.ACTIVE)
                .orElseThrow(() -> new CourseNotFoundException(courseId));

        List<User> students = userRepository.findAllByIdInAndActiveTrue(studentIdsAsLong);

        students
                .forEach(student -> {
                    Maingrade maingrade = maingradeRepository.findByCourse_IdAndStudent_Id(course.getId(), student.getId())
                            .orElseGet(() -> maingradeRepository.save(
                                    Maingrade.builder()
                                            .id(TSID.fast().toLong())
                                            .course(course)
                                            .student(student)
                                            .status(MaingradeStatus.ACTIVE)
                                            .build()
                            ));

                    if (MaingradeStatus.INACTIVE.equals(maingrade.getStatus())) {
                        maingrade.setStatus(MaingradeStatus.ACTIVE);
                        maingradeRepository.save(maingrade);
                    }
                });

        return students;
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deactivateStudent(String courseId, String studentId) {
        Long courseID = TSID.from(courseId).toLong();
        Long studentID = TSID.from(studentId).toLong();
        maingradeRepository.findByCourse_IdAndStudent_IdAndStatusNot(courseID, studentID, MaingradeStatus.INACTIVE)
                .ifPresent(maingrade -> {
                    maingrade.setStatus(MaingradeStatus.INACTIVE);
                    maingradeRepository.save(maingrade);
                });
    }

    @Override
    public List<Subcol> getSubcols(String courseId) {
        return courseRepository.findByIdAndStatusNot(TSID.from(courseId).toLong(), CourseStatus.INACTIVE)
                .map(course -> subcolRepository.findAllByCourse_IdAndStatus(course.getId(), SubcolStatus.ACTIVE))
                .orElseThrow(() -> new CourseNotFoundException(courseId));
    }
}

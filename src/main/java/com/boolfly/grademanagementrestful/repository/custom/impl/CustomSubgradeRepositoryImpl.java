package com.boolfly.grademanagementrestful.repository.custom.impl;

import com.boolfly.grademanagementrestful.domain.model.maingrade.MaingradeStatus;
import com.boolfly.grademanagementrestful.domain.model.subcol.SubcolStatus;
import com.boolfly.grademanagementrestful.repository.custom.CustomSubgradeRepository;
import com.boolfly.grademanagementrestful.repository.custom.model.PairSubgradeSubcol;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

import static com.boolfly.grademanagementrestful.domain.QMaingrade.maingrade;
import static com.boolfly.grademanagementrestful.domain.QSubcol.subcol;
import static com.boolfly.grademanagementrestful.domain.QSubgrade.subgrade;

public class CustomSubgradeRepositoryImpl implements CustomSubgradeRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<PairSubgradeSubcol> findAllByCourseIdAndStudentIdAndActive(Long courseId, Long studentId) {
        JPAQuery<Tuple> query = new JPAQueryFactory(entityManager)
                .select(subgrade, subcol)
                .from(maingrade)
                .innerJoin(subcol).on(subcol.course.id.eq(maingrade.course.id))
                .innerJoin(subgrade).on(subgrade.subcol.id.eq(subcol.id).and(subgrade.student.id.eq(maingrade.student.id)))
                .where(subcol.course.id.eq(courseId).and(subgrade.student.id.eq(studentId)
                        .and(maingrade.status.ne(MaingradeStatus.INACTIVE))
                        .and(subcol.status.ne(SubcolStatus.INACTIVE))));
        return query.fetch()
                .stream()
                .map(t -> PairSubgradeSubcol.builder()
                        .subgrade(t.get(subgrade))
                        .subcol(t.get(subcol))
                        .build()
                )
                .toList();
    }
}

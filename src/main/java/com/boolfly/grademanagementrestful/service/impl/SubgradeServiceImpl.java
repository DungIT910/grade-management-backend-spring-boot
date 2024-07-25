package com.boolfly.grademanagementrestful.service.impl;

import com.boolfly.grademanagementrestful.domain.Maingrade;
import com.boolfly.grademanagementrestful.repository.SubgradeRepository;
import com.boolfly.grademanagementrestful.repository.custom.model.PairSubgradeSubcol;
import com.boolfly.grademanagementrestful.service.SubgradeService;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SubgradeServiceImpl implements SubgradeService {
    private final SubgradeRepository subgradeRepository;

    @Override
    public Map<String, List<PairSubgradeSubcol>> getPairSubgradeSubcol(List<Maingrade> maingradeList) {
        Map<String, List<PairSubgradeSubcol>> mapPairSubgradeSubcol = new HashMap<>();

        for (Maingrade maingrade : maingradeList) {
            Long courseId = maingrade.getCourse().getId();
            Long studentId = maingrade.getStudent().getId();

            List<PairSubgradeSubcol> pairSubgradeSubcolList = subgradeRepository
                    .findAllByCourseIdAndStudentIdAndActive(courseId, studentId);

            mapPairSubgradeSubcol.put(TSID.from(maingrade.getId()).toString(), pairSubgradeSubcolList);
        }

        return Map.copyOf(mapPairSubgradeSubcol);
    }
}

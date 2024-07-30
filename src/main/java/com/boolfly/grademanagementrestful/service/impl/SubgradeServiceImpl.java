package com.boolfly.grademanagementrestful.service.impl;

import com.boolfly.grademanagementrestful.domain.Maingrade;
import com.boolfly.grademanagementrestful.repository.SubgradeRepository;
import com.boolfly.grademanagementrestful.repository.custom.model.PairSubgradeSubcol;
import com.boolfly.grademanagementrestful.service.SubgradeService;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubgradeServiceImpl implements SubgradeService {
    private final SubgradeRepository subgradeRepository;

    @Override
    public Map<String, List<PairSubgradeSubcol>> getPairSubgradeSubcol(List<Maingrade> maingradeList) {
        Map<String, List<PairSubgradeSubcol>> mapPairSubgradeSubcol = maingradeList.stream()
                .collect(Collectors.toMap(
                        maingrade -> TSID.from(maingrade.getId()).toString(),
                        maingrade -> subgradeRepository.findAllByCourseIdAndStudentIdAndActive(
                                maingrade.getCourse().getId(), maingrade.getStudent().getId())
                ));

        return Map.copyOf(mapPairSubgradeSubcol);
    }
}

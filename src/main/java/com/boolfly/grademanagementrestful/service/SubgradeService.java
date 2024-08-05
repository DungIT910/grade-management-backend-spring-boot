package com.boolfly.grademanagementrestful.service;

import com.boolfly.grademanagementrestful.domain.Maingrade;
import com.boolfly.grademanagementrestful.repository.custom.model.PairSubgradeSubcol;

import java.util.List;
import java.util.Map;

public interface SubgradeService {
    /**
     * Retrieves a map of subgrade and subcol pairs for each maingrade.
     *
     * @param maingradeList the list of maingrades to process
     * @return a map where the key is the maingrade ID as a string and the value is a list of PairSubgradeSubcol
     * @throws NullPointerException     if maingradeList is null
     * @throws IllegalArgumentException if any maingrade in the list is invalid
     * @author Bao Le
     */
    Map<String, List<PairSubgradeSubcol>> getPairSubgradeSubcol(List<Maingrade> maingradeList);
}

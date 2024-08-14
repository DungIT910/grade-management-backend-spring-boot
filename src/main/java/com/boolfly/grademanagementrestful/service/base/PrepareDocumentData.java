package com.boolfly.grademanagementrestful.service.base;

import java.util.List;
import java.util.Map;

public interface PrepareDocumentData {
    List<String> getGenericFieldNames(String id);

    List<Map<String, String>> getData(String id);
}

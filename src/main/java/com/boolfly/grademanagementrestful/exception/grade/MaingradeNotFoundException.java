package com.boolfly.grademanagementrestful.exception.grade;

import com.boolfly.grademanagementrestful.exception.generic.NotFoundException;

public class MaingradeNotFoundException extends NotFoundException {
    public MaingradeNotFoundException() {
        super("Maingrade");
    }
}

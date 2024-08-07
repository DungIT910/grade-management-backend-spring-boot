package com.boolfly.grademanagementrestful.listener.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class MailEvent extends ApplicationEvent {
    private final String courseName;
    private final String toEmail;

    public MailEvent(Object source, String courseName, String toEmail) {
        super(source);
        this.courseName = courseName;
        this.toEmail = toEmail;
    }
}

package com.boolfly.grademanagementrestful.listener.event;

import com.boolfly.grademanagementrestful.domain.User;
import lombok.Builder;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UploadAvatarEvent extends ApplicationEvent {
    private final Long userId;
    private final byte[] avatar;

    @Builder
    public UploadAvatarEvent(Object source, User user, byte[] avatar) {
        super(source);
        this.userId = user.getId();
        this.avatar = avatar;
    }
}

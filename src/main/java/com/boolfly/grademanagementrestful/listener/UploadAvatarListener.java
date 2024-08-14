package com.boolfly.grademanagementrestful.listener;

import com.boolfly.grademanagementrestful.exception.user.AvatarUploadException;
import com.boolfly.grademanagementrestful.exception.user.MissingAvatarException;
import com.boolfly.grademanagementrestful.listener.event.UploadAvatarEvent;
import com.boolfly.grademanagementrestful.repository.UserRepository;
import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UploadAvatarListener implements ApplicationListener<UploadAvatarEvent> {
    private final Cloudinary cloudinary;
    private final UserRepository userRepository;

    @Override
    @Async
    public void onApplicationEvent(UploadAvatarEvent event) {
        byte[] avatarBytes = Optional
                .ofNullable(event.getAvatar())
                .orElseThrow(MissingAvatarException::new);

        try {
            Map<String, String> params = Map.of("resource_type", "auto");
            var res = cloudinary.uploader()
                    .upload(avatarBytes, params);
            String avatarUrl = res.get("secure_url").toString();
            userRepository.updateAvatarByIdAndActiveTrue(avatarUrl, event.getUserId());
        } catch (Exception ex) {
            throw new AvatarUploadException();
        }
    }
}

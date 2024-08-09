package com.boolfly.grademanagementrestful.listener;

import com.boolfly.grademanagementrestful.listener.event.MailEvent;
import com.boolfly.grademanagementrestful.service.email.EmailSenderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MailEventListener implements ApplicationListener<MailEvent> {
    EmailSenderService senderService;

    @Override
    public void onApplicationEvent(MailEvent event) {
        String courseName = event.getCourseName();
        String subject = String.format("The grade announcement of the %s course", courseName);
        String body = String.format(
                """
                        <html><head>
                        <style>
                            .button {
                                display: inline-block;
                                padding: 10px 20px;
                                font-size: 16px;
                                cursor: pointer;
                                text-align: center;
                                text-decoration: none;
                                outline: none;
                                color: #fff;
                                background-color: #4CAF50;
                                border: none;
                                border-radius: 15px;
                                box-shadow: 0 9px #999;
                            }

                            .button:hover {background-color: #3e8e41}

                            .button:active {
                                background-color: #3e8e41;
                                box-shadow: 0 5px #666;
                                transform: translateY(4px);
                            }
                        </style>
                        </head><body>Your grade for the course %s has been updated
                        %nCome in and check it out
                        <a href="https://example.com" class="button">Open Example</a>
                        </body></html>
                        """,
                courseName
        );

        log.info("Received mail event for course - {}", courseName);

        senderService.sendEmail(event.getToEmail(), subject, body);
    }
}

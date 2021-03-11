package com.aga.junit.notification.model;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class Notification {

    private NotificationEnum notificationType;
    private OffsetDateTime offsetDateTime;
    private String profileId;

    public enum NotificationEnum {
        PROFILE_UPDATE, PROFILE_CREATION, PROFILE_REMOVAL;
    }
}

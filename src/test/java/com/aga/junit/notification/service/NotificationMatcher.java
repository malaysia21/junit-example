package com.aga.junit.notification.service;

import com.aga.junit.notification.model.Notification;
import org.mockito.ArgumentMatcher;

public class NotificationMatcher implements ArgumentMatcher<Notification> {

    private final Notification expectedNotification;

    public NotificationMatcher(Notification expectedNotification) {
        this.expectedNotification = expectedNotification;
    }


    @Override
    public boolean matches(Notification actualNotification) {
        return actualNotification.getNotificationType().equals(expectedNotification.getNotificationType()) &&
                actualNotification.getProfileId().equals(expectedNotification.getProfileId()) &&
                actualNotification.getOffsetDateTime() != null;
    }
}
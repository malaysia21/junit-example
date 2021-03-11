package com.aga.junit.notification.service;

import com.aga.junit.notification.model.Notification;
import com.aga.junit.notification.model.Profile;
import com.aga.junit.notification.model.ProfileEvent;
import com.aga.junit.notification.model.ProfileException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileService {

    private NotificationService notificationService;
    private ProfileRepository profileRepository;

    public Profile patchProfile(Profile profile, ProfileEvent profileEvent){
       val notification = Notification.builder()
                .profileId(String.valueOf(profile.getId()))
                .offsetDateTime(OffsetDateTime.now())
                .notificationType(chooseNotificationType(profileEvent))
                .build();

        notificationService.sendNotification(notification);
        notificationService.sendNotification2(notification);
        profileRepository.updateProfile(profile);


        return profile;
    }

    private Notification.NotificationEnum chooseNotificationType(ProfileEvent profileEvent) {
        switch (profileEvent) {
            case PROFILE_ADDED:
                return Notification.NotificationEnum.PROFILE_CREATION;
            case PROFILE_VERIFIED:
            case PROFILE_ACTIVATED:
            case PROFILE_DEACTIVATED:
                return Notification.NotificationEnum.PROFILE_UPDATE;
            case PROFILE_CANCELLED:
                return Notification.NotificationEnum.PROFILE_REMOVAL;
            default:
                throw new ProfileException("Unmapped profile event");
        }
    }
}

package com.aga.junit.notification.service;

import com.aga.junit.notification.model.Notification;
import com.aga.junit.notification.model.Profile;
import com.aga.junit.notification.model.ProfileEvent;
import com.aga.junit.notification.model.ProfileException;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.Mockito.*;

/**
 * Example of:
 * -argumentCaptor
 * -argumentMatcher
 * -doNothing
 * -doThrow
 * -doAnswer
 * -assertThrows
 */

@ExtendWith(SpringExtension.class)
class ProfileServiceTest {

    @InjectMocks
    private ProfileService profileService;

    @Mock
    private NotificationService notificationService;
    @Mock
    private ProfileRepository profileRepository;

    @Captor
    private ArgumentCaptor<Notification> notificationCaptor;
    @Captor
    private ArgumentCaptor<Notification> notification2Captor;

    private static final Long PROFILE_ID = 1000L;

    @Test
    void patchProfile() {
        //given
        val profile = Profile.builder().id(PROFILE_ID).build();

        //when
        doNothing().when(notificationService).sendNotification2(notification2Captor.capture());

        doAnswer(profileArgument -> {
            Profile arg0 = profileArgument.getArgument(0);
            arg0.setProfileStatus(Profile.ProfileStatus.VERIFIED);
            return null;
        }).when(profileRepository).updateProfile(any(Profile.class));


        Profile returnProfile = profileService.patchProfile(profile, ProfileEvent.PROFILE_VERIFIED);

        //then
        Mockito.verify(notificationService, Mockito.times(1)).sendNotification(notificationCaptor.capture());
        Mockito.verify(notificationService, Mockito.times(1)).sendNotification2(isNotNull());
        Mockito.verify(profileRepository, Mockito.times(1)).updateProfile(isNotNull());

        Notification notificationCaptorValue = notificationCaptor.getValue();
        Notification notification2CaptorValue = notification2Captor.getValue();

        assertAll(
                () -> assertEquals(PROFILE_ID, Long.valueOf(notificationCaptorValue.getProfileId())),
                () -> assertEquals(Notification.NotificationEnum.PROFILE_UPDATE, notificationCaptorValue.getNotificationType()),
                () -> assertEquals(PROFILE_ID, Long.valueOf(notification2CaptorValue.getProfileId())),
                () -> assertEquals(Notification.NotificationEnum.PROFILE_UPDATE, notification2CaptorValue.getNotificationType()),
                () -> assertEquals(Profile.ProfileStatus.VERIFIED, returnProfile.getProfileStatus())
        );
    }

    @Test
    void patchProfileArgumentMatcher() {
        //given
        val profile = Profile.builder().id(PROFILE_ID).build();

        //when
        doNothing().when(notificationService).sendNotification2(notification2Captor.capture());

        doAnswer(profileArgument -> {
            Profile arg0 = profileArgument.getArgument(0);
            arg0.setProfileStatus(Profile.ProfileStatus.VERIFIED);
            return null;
        }).when(profileRepository).updateProfile(any(Profile.class));


        Profile returnProfile = profileService.patchProfile(profile, ProfileEvent.PROFILE_VERIFIED);

        //then

        Mockito.verify(notificationService, Mockito.times(1)).sendNotification(argThat(new NotificationMatcher(
                Notification.builder()
                        .profileId(PROFILE_ID.toString())
                        .notificationType(Notification.NotificationEnum.PROFILE_UPDATE)
                        .build())));

        Mockito.verify(notificationService, Mockito.times(1)).sendNotification2(isNotNull());
        Mockito.verify(profileRepository, Mockito.times(1)).updateProfile(isNotNull());

        assertEquals(Profile.ProfileStatus.VERIFIED, returnProfile.getProfileStatus());

    }

    @Test
    void patchProfileUnmappedProfileException() {
        //given
        val profile = Profile.builder().id(PROFILE_ID).build();

        //when
        Exception exception = assertThrows(ProfileException.class, () -> profileService.patchProfile(profile, ProfileEvent.PROFILE_REFRESH));

        //then
        assertTrue(exception.getMessage().contains("Unmapped profile event"));
    }

    @Test
    void patchProfileNotificationException() {
        //given
        val profile = Profile.builder().id(PROFILE_ID).build();
        doThrow(new ProfileException("Exception")).when(notificationService).sendNotification(any(Notification.class));

        //when
        Exception exception = assertThrows(ProfileException.class, () -> profileService.patchProfile(profile, ProfileEvent.PROFILE_ADDED));

        //then
        assertTrue(exception.getMessage().contains("Exception"));
    }

}
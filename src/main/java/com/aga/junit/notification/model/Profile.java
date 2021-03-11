package com.aga.junit.notification.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Profile {

    private Long id;
    private String name;
    private ProfileStatus profileStatus;


    public enum ProfileStatus {
        NEW, VERIFIED, ACTIVATED, DEACTIVATED, CANCELLED;
    }
}

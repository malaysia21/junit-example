package com.aga.junit;

import com.aga.junit.notification.model.Profile;

import java.util.ArrayList;

public class TestList extends ArrayList<Profile> {

    @Override
    public void add(int index, Profile element) {
        if(element == null) {
            throw new NullPointerException("Exception");
        }
        super.add(index, element);
    }

    @Override
    public int size() {
        return 0;
    }
}

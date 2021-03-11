package com.aga.junit;

import com.aga.junit.TestList;
import com.aga.junit.notification.model.Profile;
import lombok.val;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

class TestListTest {

    private static final Long PROFILE_ID = 1000L;
    public static final String EXCEPTION = "Exception";

    /**
     * doNothing, verify
     */
    @RepeatedTest(3)
    public void addToTestListExample1() {
        //given
        TestList myList = mock(TestList.class);
        val profile = Profile.builder().id(PROFILE_ID).build();

        //when
        doNothing().when(myList).add(isA(Integer.class), isA(Profile.class));
        myList.add(0, profile);

        //then
        verify(myList, times(1)).add(0, profile);
        verify(myList, times(0)).size();
    }

    /**
     * verify
     */
    @Test
    public void addToTestListExample2() {
        //given
        TestList myList = mock(TestList.class);
        val profile = Profile.builder().id(PROFILE_ID).build();

        //when
        myList.add(0, profile);

        //then
        verify(myList, times(1)).add(0, profile);
    }

    /**
     * doThrow
     */
    @Test
    public void addToTestListExample3() {
        //given
        TestList myList = mock(TestList.class);

        //when
        doThrow(new NullPointerException(EXCEPTION)).when(myList).add(isA(Integer.class), isNull());

        Exception exception = assertThrows(NullPointerException.class, () -> myList.add(0, null));

        //then
        assertTrue(exception.getMessage().contains(EXCEPTION));
    }

    @Test
    public void addToTestListExample4() {
        //given
        TestList myList = new TestList();

        //when
        Exception exception = assertThrows(NullPointerException.class, () -> myList.add(0, null));

        //then
        assertTrue(exception.getMessage().contains(EXCEPTION));
        assertTrue(myList.isEmpty());
    }

    /**
     * doAnswer
     */
    @Test
    public void addToTestListExample5(){
        //given
        val profile = Profile.builder().id(PROFILE_ID).build();
        TestList myList = mock(TestList.class);

        doAnswer(invocation -> {
            Integer arg0 = invocation.getArgument(0);
            Profile arg1 = invocation.getArgument(1);
            assertEquals(3, arg0);
            assertEquals(PROFILE_ID, arg1.getId());
            return null;
        }).when(myList).add(any(Integer.class), any(Profile.class));

        //when
        myList.add(3, profile);

        //then
        verify(myList, times(1)).add(3, profile);
    }


    /**
     * argumentMatcher
     */
    @Test
    public void addToTestListExample6() {
        //given
        TestList myList = mock(TestList.class);
        val profile = Profile.builder().id(PROFILE_ID).build();

        //when
        myList.add(0, profile);

        //then
        verify(myList, times(1)).add(eq(0), any(Profile.class));
    }

    /**
     * verify orderOfInteraction
     */
    @Test
    public void addToTestListExample7() {
        //given
        TestList myList = mock(TestList.class);
        val profile = Profile.builder().id(PROFILE_ID).build();

        //when
        myList.add(0, profile);
        myList.clear();

        //then

        InOrder inOrder = inOrder(myList);
        inOrder.verify(myList).add(eq(0), any(Profile.class));
        inOrder.verify(myList).clear();

    }

    /**
     * verify atLeast/atMost
     */
    @Test
    public void addToTestListExample8() {
        //given
        TestList myList = mock(TestList.class);
        val profile = Profile.builder().id(PROFILE_ID).build();

        //when
        myList.add(0, profile);
        myList.add(1, profile);
        myList.add(2, profile);
        myList.clear();

        //then
        verify(myList, atLeast(1)).clear();
        verify(myList, atMost(3)).add(any(Integer.class), any(Profile.class));
    }

    /**
     * doReturn
     */
    @Test
    public void addToTestListExample9() {
        //given
        TestList myList = mock(TestList.class);
        val profile = Profile.builder().id(PROFILE_ID).build();

        doReturn(false).when(myList).add(any(Profile.class));

        //when
        boolean addResult = myList.add(profile);

        //then
        assertFalse(addResult);
    }

    /**
     * multiple calls
     */
    @Test
    public void addToTestListExample10() {
        //given
        TestList myList = mock(TestList.class);
        val profile = Profile.builder().id(PROFILE_ID).build();

      when(myList.add(any(Profile.class)))
              .thenReturn(false)
              .thenReturn(true);

        //when
        boolean addResult = myList.add(profile);
        boolean addResult2 = myList.add(profile);

        //then
        assertFalse(addResult);
        assertTrue(addResult2);
    }

    /**
     * callRealMethod
     */
    @Test
    public void addToTestListExample11() {
        //given
        TestList myList = mock(TestList.class);
        when(myList.size()).thenCallRealMethod();

        //when
        int size = myList.size();

        //then
        assertEquals(0, size);
    }
}
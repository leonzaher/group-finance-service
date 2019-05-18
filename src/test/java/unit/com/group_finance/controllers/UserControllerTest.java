package com.group_finance.controllers;

import com.group_finance.TestData;
import com.group_finance.models.Group;
import com.group_finance.models.User;
import com.group_finance.repositories.GroupRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private GroupController groupController;

    @InjectMocks
    private UserController userController;

    private TestData testData;

    @Before
    public void setUp() {
        testData = new TestData();
    }


    @Test
    public void createUserTest() {
        // When
        when(groupController.getByNameWithCheck(eq(testData.DEFAULT_GROUP_NAME))).thenReturn(testData.DEFAULT_GROUP);
        when(groupRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        // Then
        User returnedValue = userController.createUser(testData.DEFAULT_GROUP_NAME, testData.DEFAULT_USER);

        Mockito.verify(groupRepository).save(testData.DEFAULT_GROUP_WITH_USER);
        assertEquals(testData.DEFAULT_USER, returnedValue);
    }

    @Test
    public void getAllUsersTest() {
        // Given
        List<User> expected = Collections.singletonList(testData.DEFAULT_USER);

        // When
        when(groupController.getByNameWithCheck(testData.DEFAULT_GROUP_NAME)).thenReturn(testData.DEFAULT_GROUP_WITH_USER);

        // Then
        List<User> returnedValue = userController.getAllUsers(testData.DEFAULT_GROUP_NAME);

        Mockito.verifyZeroInteractions(groupRepository);
        assertEquals(expected, returnedValue);
    }

    @Test
    public void updateUserTest() {
        // When
        when(groupController.getByNameWithCheck(testData.DEFAULT_GROUP_NAME)).thenReturn(testData.DEFAULT_GROUP_WITH_USER);

        // Then
        User returnedValue = userController.updateUser(testData.DEFAULT_GROUP_NAME, testData.DEFAULT_USERNAME, testData.UPDATED_USER);

        assertEquals(testData.UPDATED_USER, returnedValue);
    }

    @Test
    public void deleteUserTest() {
        // Given
        Group expected = new Group(testData.DEFAULT_GROUP_NAME);

        // When
        when(groupController.getByNameWithCheck(eq(testData.DEFAULT_GROUP_NAME))).thenReturn(testData.DEFAULT_GROUP_WITH_USER);
        when(groupRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        // Then
        Group returnedValue = userController.deleteUser(testData.DEFAULT_GROUP_NAME, testData.DEFAULT_USERNAME);

        Mockito.verify(groupRepository).save(testData.DEFAULT_GROUP);
        assertEquals(expected, returnedValue);
    }

}
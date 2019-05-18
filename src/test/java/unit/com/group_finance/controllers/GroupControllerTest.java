package com.group_finance.controllers;

import com.group_finance.TestData;
import com.group_finance.exceptions.ResourceNotFoundException;
import com.group_finance.models.Group;
import com.group_finance.repositories.GroupRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GroupControllerTest {

    @Mock
    private GroupRepository groupRepository;

    @InjectMocks
    private GroupController groupController;

    private TestData testData;

    @Before
    public void setUp() {
        testData = new TestData();
    }


    @Test
    public void createGroupTest() {
        // When
        when(groupRepository.findByName(testData.DEFAULT_GROUP_NAME)).thenReturn(null);
        when(groupRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        // Then
        Group returnedValue = groupController.createGroup(testData.DEFAULT_GROUP);

        Mockito.verify(groupRepository).findByName(testData.DEFAULT_GROUP_NAME);
        Mockito.verify(groupRepository).save(testData.DEFAULT_GROUP);
        Mockito.verifyNoMoreInteractions(groupRepository);
        assertEquals(testData.DEFAULT_GROUP, returnedValue);
    }

    @Test
    public void getAllGroupsTest() {
        // Given
        List<Group> expected = Collections.singletonList(testData.DEFAULT_GROUP);

        // When
        when(groupRepository.findAll()).thenReturn(expected);

        // Then
        List<Group> returnedValue = groupController.getAllGroups();

        Mockito.verify(groupRepository).findAll();
        Mockito.verifyNoMoreInteractions(groupRepository);
        assertEquals(expected, returnedValue);
    }

    @Test
    public void getByNameWithCheck() {
        // When
        when(groupRepository.findByName(eq(testData.DEFAULT_GROUP_NAME))).thenReturn(testData.DEFAULT_GROUP);

        // Then
        Group group = groupController.getByNameWithCheck(testData.DEFAULT_GROUP_NAME);

        assertEquals(testData.DEFAULT_GROUP, group);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getByNameWithCheck_InvalidGroupName() {
        // When
        when(groupRepository.findByName(eq(testData.INVALID_GROUP_NAME))).thenReturn(null);

        // Then
        groupController.getByNameWithCheck(testData.INVALID_GROUP_NAME);
    }

    @Test
    public void updateGroupTest() {
        // When
        when(groupRepository.findByName(anyString())).thenReturn(testData.DEFAULT_GROUP);
        when(groupRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        // Then
        Group returnedValue = groupController.updateGroup(testData.DEFAULT_GROUP_NAME, testData.DEFAULT_GROUP_UPDATED);

        Mockito.verify(groupRepository).findByName(testData.DEFAULT_GROUP_NAME);
        Mockito.verify(groupRepository).save(testData.DEFAULT_GROUP_UPDATED);
        Mockito.verifyNoMoreInteractions(groupRepository);
        assertEquals(testData.DEFAULT_GROUP_UPDATED, returnedValue);
    }

    @Test
    public void deleteGroupTest() {
        // When
        when(groupRepository.findByName(anyString())).thenReturn(testData.DEFAULT_GROUP);
        Mockito.doNothing().when(groupRepository).delete(any());

        // Then
        groupController.deleteGroup(testData.DEFAULT_GROUP_NAME);

        Mockito.verify(groupRepository).findByName(testData.DEFAULT_GROUP_NAME);
        Mockito.verify(groupRepository).delete(testData.DEFAULT_GROUP);
        Mockito.verifyNoMoreInteractions(groupRepository);
    }
}
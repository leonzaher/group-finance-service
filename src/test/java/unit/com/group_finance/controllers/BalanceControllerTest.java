package com.group_finance.controllers;

import com.group_finance.TestData;
import com.group_finance.exceptions.InvalidInputException;
import com.group_finance.models.Group;
import com.group_finance.models.User;
import com.group_finance.repositories.GroupRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BalanceControllerTest {

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private GroupController groupController;

    @InjectMocks
    private BalanceController balanceController;

    private TestData testData;

    @Before
    public void setUp() {
        testData = new TestData();
    }


    @Test
    public void increaseBalanceTest() {
        // When
        when(groupController.getByNameWithCheck(testData.DEFAULT_GROUP_NAME))
                .thenReturn(testData.DEFAULT_GROUP_WITH_MULTIPLE_USERS);
        when(groupRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        // Then
        User user = balanceController.increaseBalance(testData.DEFAULT_GROUP_NAME, testData.DEFAULT_USERNAME,
                testData.DEFAULT_AMOUNT);

        assertEquals(testData.DEFAULT_AMOUNT, user.getBalance(), testData.AMOUNT_ASSERT_DELTA);
    }

    @Test
    public void decreaseBalanceTest() {
        // When
        when(groupController.getByNameWithCheck(testData.DEFAULT_GROUP_NAME))
                .thenReturn(testData.DEFAULT_GROUP_WITH_MULTIPLE_USERS);
        when(groupRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        // Then
        User user = balanceController.decreaseBalance(testData.DEFAULT_GROUP_NAME, testData.DEFAULT_USERNAME,
                testData.DEFAULT_AMOUNT);

        assertEquals(-testData.DEFAULT_AMOUNT, user.getBalance(), testData.AMOUNT_ASSERT_DELTA);
    }

    @Test
    public void executePaymentTest() {
        // Given
        Group testGroup = testData.DEFAULT_GROUP_WITH_MULTIPLE_USERS;

        // When
        when(groupController.getByNameWithCheck(testData.DEFAULT_GROUP_NAME)).thenReturn(testGroup);

        // Then
        balanceController.executePayment(testData.DEFAULT_GROUP_NAME, testData.DEFAULT_USERNAME,
                testData.DEFAULT_USERNAME2, testData.DEFAULT_AMOUNT);

        assertEquals(testData.DEFAULT_AMOUNT, testGroup.getGroupMembers().get(testData.DEFAULT_USERNAME).getBalance(),
                testData.AMOUNT_ASSERT_DELTA);
        assertEquals(-testData.DEFAULT_AMOUNT, testGroup.getGroupMembers().get(testData.DEFAULT_USERNAME2).getBalance(),
                testData.AMOUNT_ASSERT_DELTA);
    }

    @Test(expected = InvalidInputException.class)
    public void executePaymentTest_PayerEqualsPayee() {
        balanceController.executePayment(testData.DEFAULT_GROUP_NAME, testData.DEFAULT_USERNAME,
                testData.DEFAULT_USERNAME, testData.DEFAULT_AMOUNT);
    }

    @Test
    public void executeGroupPayment() {
        // Given
        Group testGroup = testData.DEFAULT_GROUP_WITH_MULTIPLE_USERS;

        // When
        when(groupController.getByNameWithCheck(testData.DEFAULT_GROUP_NAME)).thenReturn(testGroup);

        // Then
        balanceController.executeGroupPayment(testData.DEFAULT_GROUP_NAME, testData.DEFAULT_USERNAME, testData.DEFAULT_AMOUNT);

        assertEquals(1.0, testGroup.getGroupMembers().get(testData.DEFAULT_USERNAME).getBalance(), testData.AMOUNT_ASSERT_DELTA);
        assertEquals(-0.5, testGroup.getGroupMembers().get(testData.DEFAULT_USERNAME2).getBalance(), testData.AMOUNT_ASSERT_DELTA);
        assertEquals(-0.5, testGroup.getGroupMembers().get(testData.DEFAULT_USERNAME3).getBalance(), testData.AMOUNT_ASSERT_DELTA);
    }

    @Test
    public void executeDetailedPayment() {
        // Given
        Group testGroup = testData.DEFAULT_GROUP_WITH_MULTIPLE_USERS;
        List<BalanceController.Payment> paymentDetails = List.of(
                new BalanceController.Payment(testData.DEFAULT_USERNAME2, 2.2),
                new BalanceController.Payment(testData.DEFAULT_USERNAME3, 7.3)
        );

        // When
        when(groupController.getByNameWithCheck(testData.DEFAULT_GROUP_NAME)).thenReturn(testGroup);

        // Then
        balanceController.executeDetailedPayment(testData.DEFAULT_GROUP_NAME, testData.DEFAULT_USERNAME, paymentDetails);

        assertEquals(9.5, testGroup.getGroupMembers().get(testData.DEFAULT_USERNAME).getBalance(), testData.AMOUNT_ASSERT_DELTA);
        assertEquals(-2.2, testGroup.getGroupMembers().get(testData.DEFAULT_USERNAME2).getBalance(), testData.AMOUNT_ASSERT_DELTA);
        assertEquals(-7.3, testGroup.getGroupMembers().get(testData.DEFAULT_USERNAME3).getBalance(), testData.AMOUNT_ASSERT_DELTA);
    }
}
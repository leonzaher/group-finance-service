package com.group_finance;

import com.group_finance.models.Group;
import com.group_finance.models.User;

import java.util.*;

public class TestData {
    public final String DEFAULT_USERNAME = "User1";
    public final String DEFAULT_USERNAME2 = "User2";
    public final String DEFAULT_USERNAME3 = "User3";
    public final String UPDATED_USERNAME = "Updated";

    public User DEFAULT_USER = new User(DEFAULT_USERNAME);
    public User DEFAULT_USER2 = new User(DEFAULT_USERNAME2);
    public User DEFAULT_USER3 = new User(DEFAULT_USERNAME3);
    public User UPDATED_USER = new User(UPDATED_USERNAME);
    public Map<String, User> DEFAULT_USER_SET = Map.of(DEFAULT_USERNAME, DEFAULT_USER);
    public Map<String, User> DEFAULT_MULTIPLE_USER_SET = Map.of(DEFAULT_USERNAME, DEFAULT_USER,
            DEFAULT_USERNAME2, DEFAULT_USER2, DEFAULT_USERNAME3, DEFAULT_USER3);

    public final String DEFAULT_GROUP_NAME = "Test";
    public final String DEFAULT_GROUP_NAME_UPDATED = "Updated";
    public final String INVALID_GROUP_NAME = "Gibberish";

    public Group DEFAULT_GROUP = new Group(DEFAULT_GROUP_NAME);
    public Group DEFAULT_GROUP_UPDATED = new Group(DEFAULT_GROUP_NAME_UPDATED);
    public Group DEFAULT_GROUP_WITH_USER = new Group(DEFAULT_GROUP_NAME, DEFAULT_USER_SET);
    public Group DEFAULT_GROUP_WITH_MULTIPLE_USERS = new Group(DEFAULT_GROUP_NAME, DEFAULT_MULTIPLE_USER_SET);

    public final double DEFAULT_AMOUNT = 1.5;
    public final double AMOUNT_ASSERT_DELTA = 1e-6;
}

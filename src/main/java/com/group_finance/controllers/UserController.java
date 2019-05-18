package com.group_finance.controllers;

import com.group_finance.exceptions.InvalidInputException;
import com.group_finance.models.Group;
import com.group_finance.models.User;
import com.group_finance.repositories.GroupRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Log4j2
public class UserController {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupController groupController;


    @PostMapping
    public User createUser(@RequestParam("group") String groupName, @RequestBody User user) {
        Group group = groupController.getByNameWithCheck(groupName);

        if (group.getGroupMembers().containsKey(user.getUsername())) {
            String errorMessage = String.format("User with username '%s' already exists in group '%s'", user.getUsername(), group.getName());
            LOGGER.error(errorMessage);
            throw new InvalidInputException(errorMessage);
        }

        group.getGroupMembers().put(user.getUsername(), user);
        groupRepository.save(group);

        LOGGER.debug("Created user: '{}' in group: '{}'", user, groupName);
        return user;
    }

    @GetMapping
    public List<User> getAllUsers(@RequestParam("group") String groupName) {
        return new ArrayList<>(groupController.getByNameWithCheck(groupName).getGroupMembers().values());
    }

    @PutMapping
    public User updateUser(@RequestParam("group") String groupName, @RequestParam String username, @RequestBody User user) {
        Group group = groupController.getByNameWithCheck(groupName);

        checkGroupContainsUser(group, username);

        User existingUser = group.getGroupMembers().get(username);
        if (user.getBalance() != null)
            existingUser.setBalance(user.getBalance());
        if (user.getUsername() != null)
            existingUser.setUsername(user.getUsername());

        // If we're changing the username, we need to delete and add the new user, since username is also key in database
        if (user.getUsername() != null) {
            existingUser.setUsername(user.getUsername());

            Map<String, User> groupMembers = new HashMap<>(group.getGroupMembers());
            groupMembers.remove(username);
            groupMembers.put(existingUser.getUsername(), existingUser);
            group.setGroupMembers(groupMembers);
        } else {
            group.getGroupMembers().replace(existingUser.getUsername(), existingUser);
        }

        groupRepository.save(group);

        LOGGER.debug("Updated user: '{}' in group: '{}' to '{}'", username, groupName, user);
        return existingUser;
    }

    @DeleteMapping
    public Group deleteUser(@RequestParam("group") String groupName, @RequestParam String username) {
        Group group = groupController.getByNameWithCheck(groupName);

        checkGroupContainsUser(group, username);

        Map<String, User> groupMembers = new HashMap<>(group.getGroupMembers());
        groupMembers.remove(username);
        group.setGroupMembers(groupMembers);

        LOGGER.debug("Deleted user: '{}' in group: '{}'", username, groupName);
        return groupRepository.save(group);
    }

    public static void checkGroupContainsUser(Group group, String username) {
        if (group.getGroupMembers().containsKey(username))
            return;

        String errorMessage = String.format("User with username '%s' doesn't exist in group '%s'", username, group.getName());
        LOGGER.error(errorMessage);
        throw new InvalidInputException(errorMessage);
    }
}

package com.group_finance.controllers;

import com.group_finance.exceptions.InvalidInputException;
import com.group_finance.exceptions.ResourceNotFoundException;
import com.group_finance.models.Group;
import com.group_finance.repositories.GroupRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/group")
@Log4j2
public class GroupController {

    @Autowired
    private GroupRepository groupRepository;

    /**
     * POST request handler that creates a new group
     * @param group data to be used for creating the group
     * @return updated Group object
     */
    @PostMapping
    public Group createGroup(@RequestBody Group group) {
        checkGroupAlreadyExists(group.getName());

        LOGGER.debug("Created group: '{}'", group);

        return groupRepository.save(group);
    }

    /**
     * GET request handler that returns a list of all Groups
     * @return List of Group objects
     */
    @GetMapping("/all")
    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    /**
     * GET request handler that returns only one group
     * @param groupName name of the group
     * @return Group object
     */
    @GetMapping
    public Group getByNameWithCheck(@RequestParam("name") String groupName) {
        Group group = groupRepository.findByName(groupName);

        if (group == null) {
            String errorMessage = String.format("Cannot find group with name '%s'", groupName);
            LOGGER.error(errorMessage);
            throw new ResourceNotFoundException(errorMessage);
        }

        return group;
    }

    /**
     * PUT request handler that updates a Group
     * @param groupName group to be updated
     * @param group data to be used to update the group
     * @return updated Group object
     */
    @PutMapping
    public Group updateGroup(@RequestParam("name") String groupName, @RequestBody Group group) {
        checkGroupAlreadyExists(group.getName());

        Group groupEntity = getByNameWithCheck(groupName);
        groupEntity.setName(group.getName());

        LOGGER.debug("Updated group '{}' to '{}'", groupName, group);

        return groupRepository.save(groupEntity);
    }

    /**
     * DELETE request handler, deletes a group
     * @param groupName group to be deleted
     * @return HTTP status OK if successful
     */
    @DeleteMapping
    public ResponseEntity deleteGroup(@RequestParam("name") String groupName) {
        groupRepository.delete(getByNameWithCheck(groupName));
        LOGGER.debug("Deleted group '{}'", groupName);
        return ResponseEntity.ok().build();
    }

    private void checkGroupAlreadyExists(String name) {
        if (groupRepository.findByName(name) != null) {
            String errorMessage = String.format("Group with name '%s' already exists", name);
            LOGGER.error(errorMessage);
            throw new InvalidInputException(errorMessage);
        }
    }
}

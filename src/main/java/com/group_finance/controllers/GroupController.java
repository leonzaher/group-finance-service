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

    @PostMapping
    public Group createGroup(@RequestBody Group group) {
        if (groupRepository.findByName(group.getName()) != null) {
            String errorMessage = String.format("Group with name '%s' already exists", group.getName());
            LOGGER.error(errorMessage);
            throw new InvalidInputException(errorMessage);
        }

        LOGGER.debug("Created group: '{}'", group);

        return groupRepository.save(group);
    }

    @GetMapping("/all")
    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

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

    @PutMapping
    public Group updateGroup(@RequestParam("name") String groupName, @RequestBody Group group) {
        Group groupEntity = getByNameWithCheck(groupName);
        groupEntity.setName(group.getName());

        LOGGER.debug("Updated group '{}' to '{}'", groupName, group);

        return groupRepository.save(groupEntity);
    }

    @DeleteMapping
    public ResponseEntity deleteGroup(@RequestParam("name") String groupName) {
        groupRepository.delete(getByNameWithCheck(groupName));
        LOGGER.debug("Deleted group '{}'", groupName);
        return ResponseEntity.ok().build();
    }
}

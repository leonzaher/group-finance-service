package com.group_finance.controllers;

import com.group_finance.Application;
import com.group_finance.models.Group;
import com.group_finance.repositories.GroupRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
@ActiveProfiles("integtest")
public class EndToEndTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private GroupRepository groupRepository;

    @After
    public void tearDown() throws Exception {
        List<Group> groups = groupRepository.findAll();
        for (Group group : groups) {
            groupRepository.delete(group);
        }
    }

    @Test
    public void groupControllerTest() throws Exception {
        String groupName = "Test";
        String groupNameUpdated = "Updated";

        // create group
        mvc.perform(post("/group")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"" + groupName + "\"}"))
                .andExpect(status().isOk());

        mvc.perform(get("/group")
                .param("name", groupName))
                .andExpect(status().isOk());

        // update group
        mvc.perform(put("/group")
                .param("name", groupName)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"" + groupNameUpdated + "\"}"))
                .andExpect(status().isOk());

        mvc.perform(get("/group")
                .param("name", groupName))
                .andExpect(status().isNotFound());

        mvc.perform(get("/group")
                .param("name", groupNameUpdated))
                .andExpect(status().isOk());

        // delete group
        mvc.perform(delete("/group")
                .param("name", groupNameUpdated))
                .andExpect(status().isOk());

        MvcResult result = mvc.perform(get("/group/all"))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("[]", result.getResponse().getContentAsString());
    }

    @Test
    public void userControllerTest() throws Exception {
        String groupName = "Test";
        String username = "User1";
        String updatedUsername = "NewUser";
        double updatedBalance = 5.5;
        double comparisonDelta = 1e-6;

        // create group
        mvc.perform(post("/group")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"" + groupName + "\"}"))
                .andExpect(status().isOk());

        // create user
        mvc.perform(post("/user")
                .param("group", groupName)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"" + username + "\"}"))
                .andExpect(status().isOk());

        MvcResult result = mvc.perform(get("/user")
                .param("group", groupName))
                .andExpect(status().isOk())
                .andReturn();

        assertNotEquals("[]", result.getResponse().getContentAsString());

        // update user
        mvc.perform(put("/user")
                .param("group", groupName)
                .param("username", username)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"" + updatedUsername + "\", \"balance\": \"" + updatedBalance + "\"}"))
                .andExpect(status().isOk());

        Group group = groupRepository.findByName(groupName);
        assertEquals(updatedUsername, group.getGroupMembers().get(updatedUsername).getUsername());
        assertEquals(updatedBalance, group.getGroupMembers().get(updatedUsername).getBalance(), comparisonDelta);

        // delete user
        mvc.perform(delete("/user")
                .param("group", groupName)
                .param("username", updatedUsername))
                .andExpect(status().isOk());

        result = mvc.perform(get("/user")
                .param("group", groupName))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("[]", result.getResponse().getContentAsString());
    }

    @Test
    public void balanceControllerTest() throws Exception {
        String groupName = "Test";
        String userA = "A";
        String userB = "B";
        String userC = "C";
        double comparisonDelta = 1e-6;

        // create group
        mvc.perform(post("/group")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"" + groupName + "\"}"))
                .andExpect(status().isOk());

        // create users
        mvc.perform(post("/user")
                .param("group", groupName)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"" + userA + "\"}"))
                .andExpect(status().isOk());

        mvc.perform(post("/user")
                .param("group", groupName)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"" + userB + "\"}"))
                .andExpect(status().isOk());

        mvc.perform(post("/user")
                .param("group", groupName)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"" + userC + "\"}"))
                .andExpect(status().isOk());

        // execute payments
        executePayment(groupName, userA, userB, 100);
        executePayment(groupName, userB, userC, 70);
        executePayment(groupName, userC, userA, 120);

        Group group = groupRepository.findByName(groupName);
        assertEquals(-20, group.getGroupMembers().get(userA).getBalance(), comparisonDelta);
        assertEquals(-30, group.getGroupMembers().get(userB).getBalance(), comparisonDelta);
        assertEquals(50, group.getGroupMembers().get(userC).getBalance(), comparisonDelta);

        mvc.perform(put("/balance/payment/group")
                .param("group", groupName)
                .param("payer", userB)
                .param("amount", String.valueOf(63)))
                .andExpect(status().isOk());

        group = groupRepository.findByName(groupName);
        assertEquals(-41, group.getGroupMembers().get(userA).getBalance(), comparisonDelta);
        assertEquals(12, group.getGroupMembers().get(userB).getBalance(), comparisonDelta);
        assertEquals(29, group.getGroupMembers().get(userC).getBalance(), comparisonDelta);
    }

    private void executePayment(String group, String payer, String payee, double amount) throws Exception {
        mvc.perform(put("/balance/payment")
                .param("group", group)
                .param("payer", payer)
                .param("payee", payee)
                .param("amount", String.valueOf(amount)))
                .andExpect(status().isOk());
    }
}
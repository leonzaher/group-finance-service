package com.group_finance.controllers;

import com.group_finance.exceptions.InvalidInputException;
import com.group_finance.models.Group;
import com.group_finance.models.User;
import com.group_finance.repositories.GroupRepository;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/balance")
@Log4j2
public class BalanceController {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupController groupController;

    /**
     * Increases balance for a certain user
     * @param groupName group that the user is in
     * @param username username of the user
     * @param amount amount to increase the balance by
     * @return updated User object
     */
    @PutMapping("/increase")
    public User increaseBalance(@RequestParam("group") String groupName, @RequestParam String username,
                                 @RequestParam double amount) {
        Group group = groupController.getByNameWithCheck(groupName);

        UserController.checkGroupContainsUser(group, username);

        group.getGroupMembers().get(username).increaseBalance(amount);

        groupRepository.save(group);

        LOGGER.debug("Changed balance for user '{}' in group '{}' by '{}'", username, groupName, amount);
        return group.getGroupMembers().get(username);
    }

    /**
     * Decreases balance for a certain user
     * This is just for ease of use, it uses increaseBalance
     * @param groupName group that the user is in
     * @param username username of the user
     * @param amount amount to increase the balance by
     * @return updated User object
     */
    @PutMapping("/decrease")
    public User decreaseBalance(@RequestParam("group") String groupName, @RequestParam String username,
                                 @RequestParam double amount) {
        return increaseBalance(groupName, username, -amount);
    }

    /**
     * Makes a payment from one user to another
     * @param groupName group that the user is in
     * @param payerUsername username of the payer
     * @param payeeUsername username of the payee
     * @param amount payment amount
     * @return HTTP status OK if successful
     */
    @PutMapping("/payment")
    public ResponseEntity executePayment(@RequestParam("group") String groupName, @RequestParam("payer") String payerUsername,
                                    @RequestParam("payee") String payeeUsername, @RequestParam double amount) {
        if (payerUsername.equals(payeeUsername)) {
            String errorMessage = "Payer and payee cannot be the same";
            LOGGER.error(errorMessage);
            throw new InvalidInputException(errorMessage);
        }

        increaseBalance(groupName, payerUsername, amount);
        decreaseBalance(groupName, payeeUsername, amount);

        LOGGER.debug("Executed payment from '{}' to '{}' in group '{}' by amount of '{}'", payerUsername, payeeUsername, groupName, amount);
        return ResponseEntity.ok().build();
    }

    /**
     * Makes a payment from one user to the whole group
     * @param groupName group that the user is in
     * @param payerUsername username of the payer
     * @param amount total payment amount
     * @return HTTP status OK if successful
     */
    @PutMapping("/payment/group")
    public ResponseEntity executeGroupPayment(@RequestParam("group") String groupName,
                                         @RequestParam("payer") String payerUsername,
                                         @RequestParam double amount) {
        Map<String, User> usersInGroup = groupController.getByNameWithCheck(groupName).getGroupMembers();

        double amountPerPerson = amount / usersInGroup.size();

        // "amount - amountPerPerson" because payer also paid for himself
        increaseBalance(groupName, payerUsername, amount - amountPerPerson);

        for (User user : usersInGroup.values()) {
            if (user.getUsername().equals(payerUsername)) {
                continue;
            }

            decreaseBalance(groupName, user.getUsername(), amountPerPerson);
        }

        LOGGER.debug("Executed group payment from '{}' in group '{}' by amount of '{}'", payerUsername, groupName, amount);
        return ResponseEntity.ok().build();
    }

    /**
     * Makes a payment where amount for each payee can be different
     * @param groupName group that the user is in
     * @param payerUsername username of the payer
     * @param paymentDetails list of {username, amount} pairs
     * @return HTTP status OK if successful
     */
    @PutMapping("/payment/detailed")
    public ResponseEntity executeDetailedPayment(@RequestParam("group") String groupName,
                                                 @RequestParam("payer") String payerUsername,
                                                 @RequestBody List<Payment> paymentDetails) {
        for (Payment payment : paymentDetails) {
            executePayment(groupName, payerUsername, payment.getUsername(), payment.getAmount());
        }

        return ResponseEntity.ok().build();
    }

    @Data
    public static class Payment {
        private final String username;
        private final double amount;
    }
}

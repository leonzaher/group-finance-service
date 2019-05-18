package com.group_finance.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document
public class User {
    @Id
    private String id;

    private String username;
    private Double balance = 0.;

    public User(String username) {
        this.username = username;
    }

    public void increaseBalance(double amount) {
        balance += amount;
    }
}

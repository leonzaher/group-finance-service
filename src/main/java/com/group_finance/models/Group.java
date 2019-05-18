package com.group_finance.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@Document
public class Group {
    @Id
    private String id;

    private String name;
    private Map<String, User> groupMembers = new HashMap<>();

    public Group(String name) {
        this.name = name;
    }

    public Group(String name, Map<String, User> groupMembers) {
        this.name = name;
        this.groupMembers = groupMembers;
    }
}

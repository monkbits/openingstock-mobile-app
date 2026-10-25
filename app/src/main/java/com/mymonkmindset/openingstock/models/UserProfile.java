package com.mymonkmindset.openingstock.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserProfile {
    private String name;
    private String handle;
    private String bio;
    private String reputationRating;
    private int totalCalls;
    private String talkTime;
    private int followersCount;
    private int followingCount;
    private List<String> languages;
    private List<String> interests;
}

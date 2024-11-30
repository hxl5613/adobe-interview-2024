package org.example.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.*;

public class CodeChallengeLeads {
    private List<Map<String, String>> leads;

    public List<Map<String, String>> getLeads() {
        return leads;
    }

    public void setLeads(List<Map<String, String>> leads) {
        this.leads = leads;
    }

    @JsonIgnore
    public List<UserInfo> getUserInfoList() {
        List<UserInfo> userInfoList = new ArrayList<>();
        try {
            for (int i = 0; i < leads.size(); i++) {
                Map<String, String> data = leads.get(i);
                userInfoList.add(i, UserInfo.createFrom(i, data));
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse userInfo list from leads.", e);
        }
        return userInfoList;
    }
}

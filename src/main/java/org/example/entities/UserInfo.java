package org.example.entities;

import com.fasterxml.jackson.annotation.*;

import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

@JsonPropertyOrder({ "_id", "email" })
public class UserInfo implements Comparable<UserInfo> {
    private static String ID_KEY = "_id";
    private static String EMAIL_KEY = "email";
    private static String ENTRY_DATE_KEY = "entryDate";

    @JsonProperty(value = "_id")
    String id;

    String email;

    @JsonIgnore // Ignored when deserializing so we can print the original timestamp format stored in properties
    ZonedDateTime entryDate;

    Map<String, String> properties;

    @JsonIgnore
    private Integer index;

    public UserInfo() {
        properties = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public ZonedDateTime getEntryDate() {
        return entryDate;
    }

    @JsonAnyGetter
    public Map<String, String> getProperties() {
        return properties;
    }

    @JsonAnySetter
    public void add(String property, String value) {
        if (ID_KEY.equals(property)) {
            id = value; // Set the id field if key is _id
        } else if (EMAIL_KEY.equals(property)) {
            email = value; // Set the name field if key is name
        } else if (ENTRY_DATE_KEY.equals(property)) {
            entryDate = ZonedDateTime.parse(value); // Assuming the entry is in a string format that can be parsed as ZonedDateTime
            properties.put(property, value);
        } else {
            properties.put(property, value); // Store other properties in the map
        }
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    @Override
    public int compareTo(UserInfo other) {
        return Comparator.comparing((UserInfo lead) -> lead.entryDate.toInstant())
                .thenComparing(lead -> lead.index)
                .compare(this, other);
    }

    @JsonIgnore
    public String getCriticalInfo() {
        return String.format("%s, %s, %s, %s", index, id, email, entryDate);
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", entryDate=" + entryDate +
                ", properties=" + properties +
                ", index=" + index +
                '}';
    }
}

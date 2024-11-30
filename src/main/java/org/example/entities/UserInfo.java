package org.example.entities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;

public class UserInfo implements Comparable<UserInfo> {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final int index;
    private final String id;
    private final String email;
    private final ZonedDateTime zonedDateTime;
    private final String zonedDateTimeStr;

    private final Map<String, String> data;

    private UserInfo(int index, String id, String email, ZonedDateTime zonedDateTime, String zonedDateTimeStr, Map<String, String> data) {
        this.index = index;
        this.id = id;
        this.email = email;
        this.zonedDateTime = zonedDateTime;
        this.zonedDateTimeStr = zonedDateTimeStr;
        this.data = data;
    }

    public static UserInfo createFrom(int index, Map<String, String> data) throws JsonProcessingException {
        String id;
        String email;
        ZonedDateTime zonedDateTime;
        String zonedDateTimeStr;
        try {
            id = data.get("_id");
            email = data.get("email");
            zonedDateTimeStr = data.get("entryDate");
            zonedDateTime = ZonedDateTime.parse(zonedDateTimeStr);
        } catch (Exception e) {
            throw new RuntimeException("Could not parse _id/email/entryDate from input: " + OBJECT_MAPPER.writeValueAsString(data), e);
        }
        return new UserInfo(index, id, email, zonedDateTime, zonedDateTimeStr, data);
    }

    public Map<String, String> getData() {
        return data;
    }
    public int getIndex() {
        return index;
    }
    public String getId() {
        return id;
    }
    public String getEmail()
    {
        return email;
    }
    public String getCriticalInfo() {
        return String.format("%s, %s, %s, %s", index, id, email, zonedDateTimeStr);
    }

    @Override
    public int compareTo(UserInfo other) {
        return Comparator.comparing((UserInfo userInfo) -> userInfo.zonedDateTime.toInstant())
                .thenComparing(userInfo -> userInfo.index)
                .compare(this, other);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserInfo userInfo = (UserInfo) o;
        return index == userInfo.index && Objects.equals(id, userInfo.id) && Objects.equals(email, userInfo.email) && Objects.equals(zonedDateTime, userInfo.zonedDateTime) && Objects.equals(zonedDateTimeStr, userInfo.zonedDateTimeStr) && Objects.equals(data, userInfo.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, id, email, zonedDateTime, zonedDateTimeStr, data);
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "index=" + index +
                ", id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", zonedDateTime=" + zonedDateTime +
                ", zonedDateTimeStr='" + zonedDateTimeStr + '\'' +
                ", data=" + data +
                '}';
    }
}

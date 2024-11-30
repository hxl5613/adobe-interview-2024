package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.entities.CodeChallengeLeads;
import org.example.entities.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class DeduplicationApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeduplicationApplication.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public List<UserInfo> readFromInput(String inputFile) {
        List<UserInfo> userInfoList;

        // Read from input file and parse it to a list of UserInfo objects.
        try {
            CodeChallengeLeads codeChallengeLeads = OBJECT_MAPPER.readValue(new File(inputFile), CodeChallengeLeads.class);
            LOGGER.info("Read {} successfully!", inputFile);
            userInfoList = codeChallengeLeads.getUserInfoList();
            LOGGER.info("Parsed {} into userInfoList!", inputFile);
        } catch (Exception e) {
            throw new RuntimeException("Got Error Parsing file", e);
        }
        return userInfoList;
    }

    public void groupByIdAndEmailSeparately(List<UserInfo> userInfoList, Map<String, List<Integer>> idMap, Map<String, List<Integer>> emailMap) {
        for (UserInfo userInfo : userInfoList) {
            String id = userInfo.getId();
            String email = userInfo.getEmail();
            int index = userInfo.getIndex();

            if (!idMap.containsKey(id)) {
                List<Integer> list = new ArrayList<>();
                idMap.put(id, list);
            }
            if (!emailMap.containsKey(email)) {
                List<Integer> list = new ArrayList<>();
                emailMap.put(email, list);
            }

            idMap.get(id).add(index);
            emailMap.get(email).add(index);
        }
    }

    public void unionSameKey(UnionFind uf, Map<String, List<Integer>> map) {
        for (List<Integer> value : map.values()) {
            int i = value.get(value.size() - 1);
            for (int j = 0; j < value.size() - 1; j++) {
                uf.union(i, value.get(j));
            }
        }
    }

    public CodeChallengeLeads run(String inputFile, String outputFile) {
        // Prepare input
        List<UserInfo> userInfoList = readFromInput(inputFile);
        Map<String, List<Integer>> idMap = new HashMap<>();
        Map<String, List<Integer>> emailMap = new HashMap<>();
        groupByIdAndEmailSeparately(userInfoList, idMap, emailMap);
        LOGGER.info("idMap: {}", idMap);
        LOGGER.info("emailMap: {}", emailMap);

        // Use union find to union input by the same id and by the same email, separately
        UnionFind uf = new UnionFind(userInfoList);
        unionSameKey(uf, idMap);
        unionSameKey(uf, emailMap);

        // Display the changes from input to the output
        List<Map<String, String>> res = new ArrayList<>();
        for (int i = 0; i < userInfoList.size(); i++) {
            int parentIndex = uf.find(i);
            LOGGER.info("\nIndex {} is replaced by index {} :\n{}\n{}", i, parentIndex,
                    userInfoList.get(i).getCriticalInfo(), userInfoList.get(parentIndex).getCriticalInfo());
            if (parentIndex == i) {
                res.add(userInfoList.get(i).getData());
            }
        }

        CodeChallengeLeads codeChallengeLeads = new CodeChallengeLeads();
        codeChallengeLeads.setLeads(res);
        try {
            String str = OBJECT_MAPPER.writeValueAsString(codeChallengeLeads);
            LOGGER.info("Final output: {}", str);
            if (outputFile != null && !outputFile.isEmpty()) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
                    writer.write(str);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize leads to string.", e);
        }
        return codeChallengeLeads;
    }
}

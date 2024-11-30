package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.example.entities.CodeChallengeLeads;
import org.example.entities.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.*;

/**
 * Main logic for deduplication
 */
public class DeduplicationApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeduplicationApplication.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);

    /**
     * Min logic. It reads from inputFile, does the deduplication using Union Find algorithm, and writes to outputFile
     * @param inputFile inputFile to read from
     * @param outputFile outputFile to write results to
     * @return The deduplicated CodeChallenge object
     */
    public CodeChallengeLeads run(String inputFile, String outputFile) {
        // Prepare input
        CodeChallengeLeads inputCodeChallengeLeads = readFromInput(inputFile);

        // We first group the records by id and by email. Then we use union find algorithm to
        // group all the ones with the same id or the same email.
        // NOTE: Technically, we could do this more efficiently by 1 iteration
        //        and no need to store the list of record indexes. We do this by calling union for the email or id
        //        as we iterate. However, this implementation was chosen because it is easier to understand.
        Map<String, List<Integer>> idMap = new HashMap<>();
        Map<String, List<Integer>> emailMap = new HashMap<>();
        groupByIdAndEmailSeparately(inputCodeChallengeLeads, idMap, emailMap);
        LOGGER.info("idMap: {}", idMap);
        LOGGER.info("emailMap: {}", emailMap);

        // Use union find to union input by the same id and by the same email, separately
        UnionFind<UserInfo> uf = new UnionFind<>(inputCodeChallengeLeads.getLeads());
        unionSameKey(uf, idMap);
        unionSameKey(uf, emailMap);

        // Log the changes from input to the output and create the result CodeChallenge
        List<UserInfo> res = new ArrayList<>();
        for (int i = 0; i < inputCodeChallengeLeads.getLeads().size(); i++) {
            int parentIndex = uf.find(i);
            if (parentIndex == i) {
                LOGGER.info("\nIndex {} is unchanged.", parentIndex);
                res.add(inputCodeChallengeLeads.getLeads().get(i));
            } else {
                LOGGER.info("\nIndex {} is replaced by index {} :\n{}\n{}", i, parentIndex,
                        inputCodeChallengeLeads.getLeads().get(i).getCriticalInfo(),
                        inputCodeChallengeLeads.getLeads().get(parentIndex).getCriticalInfo()
                );
            }
        }

        CodeChallengeLeads outputCodeChallengeLeads = new CodeChallengeLeads();
        outputCodeChallengeLeads.setLeads(res);

        try {
            String str = OBJECT_MAPPER.writeValueAsString(outputCodeChallengeLeads);
            LOGGER.info("Final output: {}", str);
            if (outputFile != null && !outputFile.isEmpty()) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
                    writer.write(str);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize leads to string.", e);
        }
        return outputCodeChallengeLeads;
    }

    protected CodeChallengeLeads readFromInput(String inputFile) {

        // Read from input file and parse it to a CodeChallenge.
        try {
            CodeChallengeLeads codeChallengeLeads = OBJECT_MAPPER.readValue(new File(inputFile), CodeChallengeLeads.class);
            LOGGER.info("Read {} successfully!", inputFile);
            // Set indexes properly
            for (int i = 0; i < codeChallengeLeads.getLeads().size(); i++) {
                codeChallengeLeads.getLeads().get(i).setIndex(i);
            }
            LOGGER.info("Read parsed Code Challenge leads from file {}", inputFile);
            return codeChallengeLeads;
        } catch (Exception e) {
            throw new RuntimeException("Got Error Parsing file", e);
        }
    }

    private void groupByIdAndEmailSeparately(CodeChallengeLeads codeChallengeLeads, Map<String, List<Integer>> idMap, Map<String, List<Integer>> emailMap) {
        for (UserInfo userInfo : codeChallengeLeads.getLeads()) {
            String id = userInfo.getId();
            String email = userInfo.getEmail();
            int index = userInfo.getIndex();

            idMap.computeIfAbsent(id, k -> new ArrayList<>()).add(index);
            emailMap.computeIfAbsent(email, k -> new ArrayList<>()).add(index);
        }
    }

    private void unionSameKey(UnionFind<UserInfo> uf, Map<String, List<Integer>> map) {
        for (List<Integer> value : map.values()) {
            int i = value.get(value.size() - 1);
            for (int j : value) {
                // The parent is set as the larger CodeChallengeLead
                // (the one we want to keep after deduplication)
                uf.union(i, j);
            }
        }
    }
}

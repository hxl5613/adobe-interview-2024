package org.example;

import org.example.entities.CodeChallengeLeads;
import org.example.entities.UserInfo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RunWith(Parameterized.class)
public class DeduplicationApplicationTest {
    private final String inputFile;
    private final String expectedFile;

    public DeduplicationApplicationTest(String inputFile, String expectedFile) {
        this.inputFile = inputFile;
        this.expectedFile = expectedFile;
    }

    @Parameterized.Parameters
    public static Collection<?> inputFiles() {
        return Arrays.asList(new Object[][] {
                {"src/test/resources/code_challenge_leads.json",
                        "src/test/resources/code_challenge_leads_expected.json"
                },
                {"src/test/resources/test1.json",
                        "src/test/resources/test1_expected.json"
                },
                {"src/test/resources/test2.json",
                        "src/test/resources/test2_expected.json"
                },
                {"src/test/resources/test3.json",
                        "src/test/resources/test3_expected.json"
                },
                {"src/test/resources/test4.json",
                        "src/test/resources/test4_expected.json"
                }
        });
    }

    @Test
    public void testRun() {
        DeduplicationApplication application = new DeduplicationApplication();

        CodeChallengeLeads expectedOutput = application.readFromInput(expectedFile);
        CodeChallengeLeads output = application.run(inputFile, null);
        List<UserInfo> outputList = output.getLeads();

        Assert.assertEquals("Incorrect output size", expectedOutput.getLeads().size(), outputList.size());

        for (int i = 0 ; i < expectedOutput.getLeads().size(); i++) {
            UserInfo userInfo = output.getLeads().get(i);
            UserInfo expectedUserInfo = expectedOutput.getLeads().get(i);
            Assert.assertEquals(expectedUserInfo.getId(), userInfo.getId());
            Assert.assertEquals(expectedUserInfo.getEmail(), userInfo.getEmail());
            Assert.assertEquals(expectedUserInfo.getEntryDate(), userInfo.getEntryDate());
            Assert.assertEquals(expectedUserInfo.getProperties(), userInfo.getProperties());
        }
    }
}

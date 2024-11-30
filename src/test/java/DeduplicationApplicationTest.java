import org.example.DeduplicationApplication;
import org.example.UnionFind;
import org.example.entities.CodeChallengeLeads;
import org.example.entities.UserInfo;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class DeduplicationApplicationTest {
    private static final String INPUT_FILE_DIRECTORY = "src/test/resources/code_challenge_leads.json";
    @Test
    public void testRun() {
        DeduplicationApplication application = new DeduplicationApplication();

        List<UserInfo> originalList = application.readFromInput(INPUT_FILE_DIRECTORY);
        CodeChallengeLeads outputLeads = application.run(INPUT_FILE_DIRECTORY, null);
        List<UserInfo> outputList = outputLeads.getUserInfoList();

        Set<String> emails = outputList.stream().map(UserInfo::getEmail).collect(Collectors.toSet());
        Set<String> ids = outputList.stream().map(UserInfo::getId).collect(Collectors.toSet());
        Set<Integer> indexes = outputList.stream().map(UserInfo::getIndex).collect(Collectors.toSet());

        // No duplicate emails, no duplicate ids
        Assert.assertEquals("Incorrect output size", 4, outputList.size());
        Assert.assertEquals( "Non unique emails", outputList.size(), emails.size());
        Assert.assertEquals("Non unique ids", outputList.size(), ids.size());
        Assert.assertEquals("Non unique indexes", outputList.size(), indexes.size());

        Assert.assertEquals("wabaj238238jdsnfsj23", outputList.get(0).getId());
        Assert.assertEquals("foo1@bar.com", outputList.get(1).getEmail());
        Assert.assertEquals("2014-05-07T17:33:20+00:00", outputList.get(2).getData().get("entryDate"));
        Assert.assertEquals("jkj238238jdsnfsj23", outputList.get(3).getData().get("_id"));
        Assert.assertEquals("bill@bar.com", outputList.get(3).getData().get("email"));
        Assert.assertEquals("2014-05-07T17:33:20+00:00", outputList.get(3).getData().get("entryDate"));

    }
}

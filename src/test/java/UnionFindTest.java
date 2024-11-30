import org.example.UnionFind;
import org.junit.Test;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class UnionFindTest {
    @Test
    public void testUnionFindBasic() {
        List<String> strList = Arrays.asList(new String[] {"a", "b", "c", "d", "e"});
        UnionFind<String> uf = new UnionFind<String>(strList);

        uf.union(0, 2);
        uf.union(1, 3);

        List<Integer> expectedParentList = Arrays.asList(new Integer[]{2, 3, 2, 3, 4});

        List<Integer> topParents = new ArrayList<>();
        for (int i = 0; i < strList.size(); i++) {
            topParents.add(uf.find(i));
        }

        assertEquals(expectedParentList, topParents);
    }

    @Test
    public void testUnionFindMultipleSets() {
        List<String> strList = Arrays.asList(new String[] {"a", "b", "c", "d", "e", "f", "g"});
        UnionFind<String> uf = new UnionFind<String>(strList);

        uf.union(0, 2);
        uf.union(3, 0);
        uf.union(1, 3);
        uf.union(6, 2);

        List<Integer> expectedParentList = Arrays.asList(new Integer[]{6, 6, 6, 6, 4, 5, 6});

        List<Integer> topParents = new ArrayList<>();
        for (int i = 0; i < strList.size(); i++) {
            topParents.add(uf.find(i));
        }

        assertEquals(expectedParentList, topParents);
    }
}

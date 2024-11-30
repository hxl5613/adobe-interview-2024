package org.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation for the Union Find algorithm where find is amortized inverse ackermann function (grows very slowly)
 * @param <T> the type of object to store in the union find
 */
public class UnionFind<T extends Comparable<T>> {
    private final List<Integer> parentList;
    private final List<T> dataList;

    public UnionFind(List<T> dataList) {
        this.dataList = dataList;
        parentList = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            parentList.add(i);
        }
    }

    /**
     * Find the parent index
     * @param i current index
     * @return top most parent index
     */
    public int find(int i) {
        if (parentList.get(i) != i) {
            parentList.set(i, find(parentList.get(i)));
        }

        return parentList.get(i) ;
    }

    /**
     * When union two indexes, we set the parent as the larger value in the compareTo
     * @param i first index
     * @param j second index
     */
    public void union(int i, int j) {
        int parentI = find(i);
        int parentJ = find(j);
        T parentIObject = dataList.get(parentI);
        T parentJObject = dataList.get(parentJ);

        if (parentIObject.compareTo(parentJObject) > 0) {
            parentList.set(parentJ, parentI);
        } else {
            parentList.set(parentI, parentJ);
        }
    }
}

package org.example;

import java.util.ArrayList;
import java.util.List;

public class UnionFind<T extends Comparable> {
    private final List<Integer> parentList;
    private List<T> dataList;

    public UnionFind(List<T> dataList) {
        this.dataList = dataList;
        parentList = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            parentList.add(i);
        }
    }

    public int find(int i) {
        if (parentList.get(i) != i) {
            parentList.set(i, find(parentList.get(i)));
        }

        return parentList.get(i) ;
    }

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

package org.chernysh.dsu;

public class DSU {

    private final int[] parent;
    private final int[] rank;

    public DSU(int size) {
        parent = new int[size];
        rank = new int[size];

        for (int i = 0; i < size; i++) {
            parent[i] = i;
            rank[i] = 0;
        }
    }
    public void makeSet(String s) {

    }

    public void unionSets(Integer a, Integer b) {
        int rootA = findSet(a);
        int rootB = findSet(b);

        if (rootA == rootB) return;

        if (rank[rootA] < rank[rootB]) {
            parent[rootA] = rootB;
        } else if (rank[rootA] > rank[rootB]) {
            parent[rootB] = rootA;
        } else {
            parent[rootB] = rootA;
            rank[rootA]++;
        }
    }

    public int findSet(int index) {
        if (parent[index] != index) parent[index] = findSet(parent[index]);
        return parent[index];
    }

}

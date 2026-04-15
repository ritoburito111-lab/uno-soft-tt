package org.chernysh;

import java.util.*;

public class SortedBySizeMap {
    private final Map<Integer, List<Integer>> delegate = new HashMap<>();
    private final TreeMap<Integer, List<Integer>> sizeIndex = new TreeMap<>();

    public void put(Integer key, List<Integer> value) {
        delegate.put(key, value);
        sizeIndex.computeIfAbsent(value.size(), k -> new ArrayList<>()).add(key);
    }

    public List<Integer> get(Integer key) {
        return delegate.get(key);
    }

    public Map<Integer, List<Integer>> getSortedBySizeAsc() {
        Map<Integer, List<Integer>> result = new LinkedHashMap<>();
        for (Map.Entry<Integer, List<Integer>> entry : sizeIndex.entrySet()) {
            for (Integer key : entry.getValue()) {
                result.put(key, delegate.get(key));
            }
        }
        return result;
    }

    public Map<Integer, List<Integer>> getSortedBySizeDesc() {
        Map<Integer, List<Integer>> result = new LinkedHashMap<>();
        for (Map.Entry<Integer, List<Integer>> entry : sizeIndex.descendingMap().entrySet()) {
            for (Integer key : entry.getValue()) {
                result.put(key, delegate.get(key));
            }
        }
        return result;
    }
}

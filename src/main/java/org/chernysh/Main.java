package org.chernysh;

import org.chernysh.dsu.DSU;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();

        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("C:\\Users\\mchernysh\\Documents\\sandbox\\lng.txt")));

        List<String> allLines =  reader.lines().toList();
        Pattern pattern = Pattern.compile("^(\"\\d*\";)*\"\\d*\"$");
        SortedBySizeMap sortedMap = new SortedBySizeMap();

        Map<String,Boolean> uniqMap = new HashMap<>();
        allLines.forEach(line -> {
            Matcher matcher = pattern.matcher(line);
            if (!line.trim().isEmpty() && matcher.find()) {
                boolean notUniq = uniqMap.containsKey(line);
                uniqMap.put(line, notUniq);
            }
        });

        List<List<String>> matrix = new ArrayList<>();
        int maxColumnAmount = 0;


        for (Map.Entry<String, Boolean> entry : uniqMap.entrySet()) {
            if (!entry.getValue()) {
                List<String> stringSplit = List.of(entry.getKey().split(";"));
                if (maxColumnAmount < stringSplit.size()) maxColumnAmount = stringSplit.size();
                matrix.add(stringSplit);
            }
        }

        DSU dsu = new DSU(matrix.size());


        for (int col = 0; col < maxColumnAmount; col++) {
            Map<String, List<Integer>> valueToRows = new HashMap<>();

            for (int row = 0; row < matrix.size(); row++) {
                if (matrix.get(row).size() <= col) continue;

                String columnValue = matrix.get(row).get(col);
                Pattern patt = Pattern.compile("\\d+");
                Matcher mat = patt.matcher(columnValue);
                if (mat.find())
                    valueToRows.computeIfAbsent(columnValue, k -> new ArrayList<>()).add(row);
            }

            valueToRows.forEach((key, list) -> {
                if (list.size() > 1) {
                    int firstElem = list.get(0);

                    for (int i = 1; i < list.size(); i++) {
                        dsu.unionSets(firstElem, list.get(i));
                    }
                }
            });
        }

        Map<Integer, List<Integer>> groupsMap = new HashMap<>();
        for (int i = 0; i < matrix.size(); i++) {
            int root = dsu.findSet(i);
            groupsMap.computeIfAbsent(root, k -> new ArrayList<>()).add(i);
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter("result.txt"));

        writer.write("Всего строк: " + matrix.size());
        writer.newLine();
        writer.write("Количество групп: " + groupsMap.size());
        writer.newLine();

        System.out.println("Всего строк: " + matrix.size());
        System.out.println("Количество групп: " + groupsMap.size());
        System.out.println("Файл результата: result.txt");



        groupsMap.forEach(sortedMap::put);
        Map<Integer, List<Integer>> sizeIndex = sortedMap.getSortedBySizeDesc();

        int groupIndx = 1;
        for (Map.Entry<Integer, List<Integer>> entry : sizeIndex.entrySet()) {
            try {
                writer.write("Группа" + groupIndx + "\n");
                entry.getValue().forEach( v -> {
                    try {
                        writer.write("\t" + matrix.get(v).toString() + "\n");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                groupIndx++;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        writer.flush();

        System.out.println("Время работы программы: " + (System.currentTimeMillis() - start));

    }
}

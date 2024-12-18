package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        String filePath = "src/main/resources/source.txt";

        try {
            String[] numbers = fetchNumbersFromFile(filePath);

            if (numbers == null || numbers.length == 0) {
                System.out.println("File is empty or failed to read data.");
                return;
            }

            System.out.println("Loaded numbers: " + String.join(", ", numbers));
            String result = findLongestChain(numbers);
            System.out.println("Longest sequence: " + result);
        } catch (IOException e) {
            System.err.println("Error reading this file: " + e.getMessage());
        }
    }

    public static String[] fetchNumbersFromFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        List<String> lines = Files.readAllLines(path);
        return lines.toArray(new String[0]);
    }

    public static String findLongestChain(String[] numbers) {
        List<String> remaining = new ArrayList<>(Arrays.asList(numbers));
        String longestChain = "";

        Map<String, String> memo = new HashMap<>();

        for (String start : numbers) {
            List<String> used = new ArrayList<>();
            used.add(start);
            String chain = buildChain(start, remaining, used, memo);
            if (chain.length() > longestChain.length()) {
                longestChain = chain;
            }
        }

        return longestChain;
    }

    private static String buildChain(String current, List<String> remaining, List<String> used, Map<String, String> memo) {
        if (memo.containsKey(current)) {
            return memo.get(current);
        }

        StringBuilder chain = new StringBuilder(current);
        List<String> nextCandidates = new ArrayList<>();

        for (String next : remaining) {
            if (!used.contains(next) && current.endsWith(next.substring(0, 2))) {
                nextCandidates.add(next);
            }
        }

        String longestSubChain = "";
        for (String candidate : nextCandidates) {
            List<String> newUsed = new ArrayList<>(used);
            newUsed.add(candidate);

            String subChain = buildChain(candidate, remaining, newUsed, memo);
            if (subChain.length() > longestSubChain.length()) {
                longestSubChain = subChain;
            }
        }

        if (!longestSubChain.isEmpty()) {
            chain.append(longestSubChain.substring(2));
        }

        String result = chain.toString();
        memo.put(current, result);
        return result;
    }
}

//dependencies
package main.java.algorithms.kmp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*-----------------------------------------------------
/*
 * @File.:      Trabalho Pratico - KMP PatternMatch Class
 * @Author.:    Pedro Carbonaro
 * @StartDate.: 2023-11-08
 * @Copyright.: Copyright (c) 2023
*/
//-----------------------------------------------------

public class KMP {

    /*
     * Global attributes
     */
    public static final String DEFAULT_PATH = "main/resources/gamesForTests.csv";
    public static long executionTime;
    public static int comparison = 0;
    public static int[] prefixTable;
    public static String pattern;

    /**
     * Works like a class constructor
     * starts the pattern and search for
     * all patterns and finally prints
     * the results found.
     *
     * @param String pattern -> what will
     * be searched for in text.
    */
    public void search(String pattern) {
        KMP.pattern = pattern;
        List<Integer> occurrences = searchPattern(readTextFromFile(DEFAULT_PATH), pattern);

        printResults(occurrences);
    }

    /**
     * Method to search for all occurrences of the pattern in a text
     *
     * @param String text -> Text that will be used for search
     * @param String pattern -> what character or word will be searched in text
     * @return List<Integer> list of all patterns and how much there are in text
    */
    public List<Integer> searchPattern(String text, String pattern) {

        /*
         * Starting essentials attributes
        */
        List<Integer> occurrences = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        KMP.pattern = pattern;

        /*
         * Build prefix table
        */
        prefixTable = buildPrefixTable(pattern);

        int textIndex = 0;
        int patternIndex = 0;

        /*
         * Search for the pattern in the text
        */
        while (textIndex < text.length()) {
            comparison++;

            if (pattern.charAt(patternIndex) == text.charAt(textIndex)) {
                patternIndex++;
                textIndex++;
            }

            /*
             * If the pattern was found
            */
            if (patternIndex == pattern.length()) {
                // Add occurrence position to list
                occurrences.add(textIndex - patternIndex);

                // Update pattern index based on prefix table
                patternIndex = prefixTable[patternIndex - 1];
            }

            /*
             * If there was a mismatch
            */
            else if (textIndex < text.length() && pattern.charAt(patternIndex) != text.charAt(textIndex)) {
                if (patternIndex != 0) {
                    // Update pattern index based on prefix table
                    patternIndex = prefixTable[patternIndex - 1];
                } else {
                    textIndex++;
                }

            }
        }

        long endTime = System.currentTimeMillis();
        executionTime = endTime - startTime;

        return occurrences;
    }

    /**
     * Method to build the prefix table
     *
     * @param String pattern -> Sequence of character or string
     * that will be searched
     * @return array that contains numbers of occurrences
     * about that pattern
     */
    private int[] buildPrefixTable(String pattern) {
        int[] prefixTable = new int[pattern.length()];
        int prefixIndex = 0;
        int i = 1;

        // Build the table
        while (i < pattern.length()) {

            if (pattern.charAt(i) == pattern.charAt(prefixIndex)) {
                prefixTable[i] = prefixIndex + 1;
                prefixIndex++;
                i++;
            } else {

                if (prefixIndex != 0) {
                    // Update table-based prefix index
                    prefixIndex = prefixTable[prefixIndex - 1];
                } else {
                    prefixTable[i] = 0;
                    i++;
                }
            }
        }

        return prefixTable;
    }

    /**
     * Method to print the prefix table
     */
    public void printPrefixTable() {

        System.out.println("\nPrefix table: ");
        for (int i = 0; i < pattern.length(); i++) {
            System.out.print(i + " ");
        }

        System.out.println();
        for (int i = 0; i < pattern.length(); i++) {
            System.out.print(pattern.charAt(i) + " ");
        }

        System.out.println();
        for (int i : prefixTable) {
            System.out.print(i + " ");
        }
        System.out.println();
    }

    /**
     * Function to display pattern matching results
     *
     * @param List<Integer> occurrences all occurrences that
     * happened in searching that pattern in text
     */
    public void printResults(List<Integer> occurrences) {
        printPrefixTable();
        System.out.println("\nComparisons: " + comparison);
        System.out.println("\nExecution time: " + executionTime + " ms");

        if (occurrences.isEmpty()) {
            System.out.println("\nNo occurrence found.");
        } else {
            System.out.println("\nOccurrence found in the positions:");
            for (int index : occurrences) {
                System.out.print(index + " ");
            }
            System.out.println("\n");
        }
    }

    /**
     * Function to read text from a file and return a String
     *
     * @param String fileName -> FilePath to access the file content
     * @return line that contains everything in file, turned
     * into a big string to be unraveled.
     */
    private String readTextFromFile(String fileName) {
        StringBuilder text = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) { text.append(line); }
        } catch (IOException e) { e.printStackTrace(); }

        return text.toString();
    }
}
package main.java.algorithms.LZW;

public class LzwDictionary {

    private final short MAX_DICTIONARY_SIZE = 32767;
    private String[] dictionary;
    private int tableValue;

    public LzwDictionary() {

        this.dictionary = new String[MAX_DICTIONARY_SIZE];
        this.tableValue = 0;

        for (int i = 0; i < 256; i++) {
            dictionary[i] = Integer.toBinaryString(i);
            this.tableValue++;
        }

    }
}

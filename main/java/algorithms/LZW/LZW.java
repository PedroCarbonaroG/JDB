package main.java.algorithms.LZW;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.HashMap;

import main.java.interfaces.Utility;

public class LZW {

    private HashMap<String, Integer> compMap;
    private static int nextCode;

    public LZW() {
        compMap = new HashMap<String, Integer>();
        nextCode = 0;

        for (int i = 0; i < 256; i++) {
            compMap.put(Integer.toBinaryString(i), i);
            nextCode++;
        }
    }

    public void compression(RandomAccessFile source) throws Exception {

        RandomAccessFile tmpFile = new RandomAccessFile("main/resources/newLzwFile.bin", "rw");
        File newLzwFile = new File("main/resources/newLzwFile.bin");

        source.seek(0);
        tmpFile.setLength(0);

        int currentByte = -1;

        String sequence = "";
        String prefixSequence = "";
        String binarySequence = "";

        while ((currentByte = source.read()) != -1) {
            sequence += (char) currentByte;

            prefixSequence = Utility.stringToBinary(sequence.substring(0, sequence.length() - 1));
            binarySequence = Utility.stringToBinary(sequence);

            if (!compMap.containsKey(binarySequence)) {
                int index = compMap.get(prefixSequence);
                tmpFile.writeInt(index);

                compMap.put(binarySequence, nextCode++);

                sequence = sequence.substring(sequence.length()-1, sequence.length());
            }
        }

        System.out.println("Source Length: " + source.length());
        System.out.println("TmpFile Length: " + tmpFile.length());
    }

}

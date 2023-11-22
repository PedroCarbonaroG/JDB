//Dependencies
package main.java.algorithms.lzw;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.HashMap;

import main.java.interfaces.Utility;

/*-----------------------------------------------------
/*
 * @File.:      Trabalho Pratico - LZW algorithmn Class
 * @Author.:    Pedro Carbonaro
 * @StartDate.: 2023-11-08
 * @Copyright.: Copyright (c) 2023
*/
//-----------------------------------------------------

public class LZW {

    /*
     * Global attributes for LZW compress and decompress
     */
    private HashMap<String, Short> compMap;
    private HashMap<Short, String> decompMap;
    private static short nextCodeAvaliableComp;
    private static short nextCodeAvaliableDecomp;
    private final short TABLE_MAX_LENGTH = 32767;

    /**
     * LZW constructor
     */
    public LZW() {
        /*
         * Starting the map for keys and values
         * and the next codes avaliables for values
         */
        compMap = new HashMap<String, Short>();
        decompMap = new HashMap<Short, String>();
        nextCodeAvaliableComp = 0;
        nextCodeAvaliableDecomp = 0;

        /*
         * Starts the structure with all 256
         * possibilities in binary state (8bits)
         */
        for (int i = 0; i < 256; i++) {
            compMap.put(Integer.toBinaryString(i), (short)i);
            nextCodeAvaliableComp++;
        }
        for (int i = 0; i < 256; i++) {
            decompMap.put((short)i, Integer.toBinaryString(i));
            nextCodeAvaliableDecomp++;
        }
    }

    /**
     * LZW Compression method, used for compress
     * a file.
     *
     * @param RandomAccessFile source -> source that
     * will be compressed
     */
    public void compression(RandomAccessFile source) {

        try {

            RandomAccessFile compLzwRaf = new RandomAccessFile("main/resources/compLzwFile.bin", "rw");
            File compLzwFile = new File("main/resources/compLzwFile.bin");

            source.seek(0);
            compLzwRaf.setLength(0);

            short currentByte;
            String sequence = "";

            while ((currentByte = (short)source.read()) != -1) {
                sequence += (char) currentByte;
                String prefixSequence = Utility.stringToBinary(sequence.substring(0, sequence.length() - 1));
                String binarySequence = Utility.stringToBinary(sequence);

                if (nextCodeAvaliableComp < TABLE_MAX_LENGTH) {
                    if (!compMap.containsKey(binarySequence)) {
                        short prefixIndex = compMap.get(prefixSequence);
                        compLzwRaf.writeShort(prefixIndex);

                        compMap.put(binarySequence, nextCodeAvaliableComp++);
                        sequence = "";
                    }
                } else {
                    if (!compMap.containsKey(binarySequence)) {
                        short prefixIndex = compMap.get(prefixSequence);
                        compLzwRaf.writeShort(prefixIndex);

                        sequence = "";
                    }
                }
            }

            Utility.transferFileContent(compLzwRaf, source, false);
            compLzwRaf.close(); compLzwFile.delete();

        } catch (Exception e) { e.printStackTrace(); }
    }

    /**
     * LZW Decompression method, used for decompress
     * a file.
     *
     * @param RandomAccessFile source -> source that
     * will be decompressed
     */
    public void decompression(RandomAccessFile source) {

        try {
            RandomAccessFile decompLzwRaf = new RandomAccessFile("main/resources/decompLzwFile.bin", "rw");
            File decompLzwFile = new File("main/resources/decompLzwFile.bin");

            source.seek(0);
            decompLzwRaf.seek(0);

            short currentCode = (short)source.read();
            StringBuilder currentSequence = new StringBuilder(decompMap.get(currentCode));
            decompLzwRaf.writeBytes(currentSequence.toString());

            while (source.getFilePointer() < source.length()) {
                currentCode = (short)source.read();

                if (nextCodeAvaliableDecomp < TABLE_MAX_LENGTH) {
                    if (!decompMap.containsKey(currentCode)) {
                        String sequence = currentSequence.toString() + currentSequence.charAt(0);
                        decompMap.put(nextCodeAvaliableDecomp++, Utility.stringToBinary(sequence));
                        decompLzwRaf.writeBytes(sequence);

                        currentSequence = new StringBuilder(sequence);
                    } else {
                        String sequence = Utility.binaryToString(decompMap.get(currentCode));
                        decompLzwRaf.writeBytes(sequence);
                        currentSequence.append(sequence.charAt(0));
                        decompMap.put(nextCodeAvaliableDecomp++, Utility.stringToBinary(currentSequence.toString()));

                        currentSequence = new StringBuilder(Utility.stringToBinary(sequence));
                    }
                }
            }

            Utility.transferFileContent(decompLzwRaf, source, false);

        } catch (Exception e) { e.printStackTrace(); }
    }
}
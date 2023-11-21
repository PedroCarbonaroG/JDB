//Dependencies
package main.java.algorithms.LZW;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.HashMap;

import main.java.interfaces.Utility;

public class LZW {

    /*
     * Global attributes for LZW compress and decompress
    */
    private HashMap<String, Short> compMap;
    private HashMap<Short, String> decompMap;
    private static short nextCodeAvaliableComp;
    private static short nextCodeAvaliableDecomp;
    private final short TABLE_MAX_LENGTH = 32767;

    /*
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

    /*
     * LZW Compression method, used for compress
     * a file.
     * 
     * @param RandomAccessFile source -> source that
     * will be compressed
    */
    public void compression(RandomAccessFile source) {

        try {

            RandomAccessFile newLzwRaf = new RandomAccessFile("src/main/resources/newLzwFile.bin", "rw");
            File newLzwFile = new File("src/main/resources/newLzwFile.bin");

            source.seek(0);
            newLzwRaf.setLength(0);

            short currentByte;
            String sequence = "";

            while ((currentByte = (short)source.read()) != -1) {
                sequence += (char) currentByte;
                String prefixSequence = Utility.stringToBinary(sequence.substring(0, sequence.length() - 1));
                String binarySequence = Utility.stringToBinary(sequence);

                if (nextCodeAvaliableComp < TABLE_MAX_LENGTH) {
                    if (!compMap.containsKey(binarySequence)) {
                        short prefixIndex = compMap.get(prefixSequence);
                        newLzwRaf.writeShort(prefixIndex);

                        compMap.put(binarySequence, ++nextCodeAvaliableComp);
                        sequence = "";
                    }
                } else {
                    if (!compMap.containsKey(binarySequence)) {
                        short prefixIndex = compMap.get(prefixSequence);
                        newLzwRaf.writeShort(prefixIndex);

                        sequence = "";
                    }
                }
            }

            Utility.transferFileContent(newLzwRaf, source, false);
            newLzwRaf.close(); newLzwFile.delete();

        } catch (Exception e) { e.printStackTrace(); }
    }

    /*
     * LZW Decompression method, used for decompress
     * an file.
     * 
     * @param RandomAccessFile source -> source that
     * will be decompressed
    */
    public void decompression(RandomAccessFile source) {

        try {
            //ToCode...
        } catch (Exception e) { e.printStackTrace(); }
    }
}

//Dependencies
package main.java.algorithms.lzw;

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

                        compMap.put(binarySequence, ++nextCodeAvaliableComp);
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

            short currentByte;
            short lastIndex = 0;
            String sequence = "";
            String lastSequence = "";

            currentByte = (short)source.read();
            sequence += (char)currentByte;

            if (decompMap.containsKey(Short.parseShort(sequence))) {
                String s = Utility.binaryToString(decompMap.get(Short.parseShort(sequence)));
                decompLzwRaf.write(Byte.parseByte(s));

                lastSequence = sequence;
                decompMap.put(++nextCodeAvaliableDecomp, (lastSequence += "?"));
                lastIndex = nextCodeAvaliableDecomp;
            }

            sequence = "";
            while ((currentByte = (short)source.read()) != -1) {
                sequence += (char)currentByte;
                lastSequence = lastSequence.substring(0, lastSequence.length()-1);
                lastSequence += sequence;

                decompMap.put(lastIndex, lastSequence);
                lastSequence = sequence + "?";
                decompMap.put(++nextCodeAvaliableDecomp, lastSequence);
                lastIndex = nextCodeAvaliableDecomp;
            }

        } catch (Exception e) { e.printStackTrace(); }
    }
}
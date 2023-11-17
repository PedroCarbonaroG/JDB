package main.java.algorithms.Huffman;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

public class Huffman {
    private final RandomAccessFile source;
    private HashMap<Vector, Integer> hashMap;

    public Huffman(RandomAccessFile source) {
        this.source = source;
        this.hashMap = new HashMap<Vector, Integer>();
    }

    /**
     * Creates a dictionary based on the specified block size from the source data.
     * The dictionary is represented using a HashMap where keys are vectors of bytes.
     *
     * @param blockSize The size of each block to be read from the source.
     * @throws IOException If an I/O error occurs while reading from the source.
     */
    public void createDictionary(int blockSize) throws IOException {
        long pos = source.getFilePointer();
        source.seek(0);
        while(source.getFilePointer()<source.length()){
            byte[] bytes = new byte[blockSize];
            source.read(bytes);
            Vector tmp = new Vector(bytes);
            if(hashMap.containsKey(tmp)){
                hashMap.put(tmp, hashMap.get(tmp)+1);
            }
            else {
                hashMap.put(tmp, 1);
            }
        }
        source.seek(pos);
    }

    /**
     * Prints the contents of the HashMap, displaying each vector and its associated count.
     * Additionally, it prints the total number of unique vectors in the HashMap.
     */
    public void printMap(){
        for(Vector v: hashMap.keySet()){
            System.out.println(Arrays.toString(v.getArray())+ " --> "+hashMap.get(v));
        }
        System.out.println(hashMap.keySet().size());
    }


}

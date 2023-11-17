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
                System.out.println("novo");
            }
        }
        source.seek(pos);
    }
    public void printMap(){
        for(Vector v: hashMap.keySet()){
            System.out.println(Arrays.toString(v.getArray())+ " --> "+hashMap.get(v));
        }
        System.out.println(hashMap.keySet().size());
    }


}

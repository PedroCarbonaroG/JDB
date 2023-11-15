package main.java.algorithms.Huffman;

import main.java.dataBase.Record;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

public class Huffman {
    private final LinkedList<HuffmanTree> list;
    private final RandomAccessFile source;
    private HashMap<Character, Integer> map;

    public LinkedList<HuffmanTree> getList() {
        return list;
    }
    public Huffman(RandomAccessFile source){
        list = new LinkedList<>();
        map = new HashMap<Character, Integer>();
        this.source = source;
    }

    /**
     * Create the Huffman dictionary from the source file.
     *
     * @param blockSize number of records loaded into memory.
     * @throws IOException RandomAccessFile exception.
     */
    public void createDictionary(int blockSize) throws Exception { //set as private
        Record tmp;
        /*
         * Skipping the file header.
         */
        if(source.getFilePointer() == 0){
            source.skipBytes(Integer.BYTES);
        }


        for(int i=0; i<blockSize && source.getFilePointer()<source.length(); i++){
            /*
             * Skipping the record size used for control.
             */
            source.skipBytes(Integer.BYTES);
            tmp = Record.fromByteArray(source, source.getFilePointer());

        }

    }
    private void run() throws Exception { // corrigir (alterei o método createDictionary)
        createDictionary(10); // valores para teste
        for(char key: map.keySet()){
            int value = map.get(key);
            list.add(new HuffmanTree(new Cell(value, key)));
        }
        Collections.sort(list);

        while(list.size()>1){
            HuffmanTree newTree = new HuffmanTree(HuffmanTree.mergeTree(list.get(0), list.get(1)));
            for(int i=0; i<2; i++){
                list.removeFirst();
            }
            list.add(newTree);
            Collections.sort(list);
        }
    }



    public void printMap() throws Exception{
        HuffmanTree hf = new HuffmanTree(new Cell(10));

    }






}

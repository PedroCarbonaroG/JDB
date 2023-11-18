package main.java.algorithms.Huffman;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

/**
 * Huffman class represents a utility for Huffman coding, providing methods for creating a dictionary,
 * constructing a Huffman tree, and printing information about the vector frequencies.
 */
public class Huffman {
    private final RandomAccessFile source;
    private HashMap<Vector, Integer> hashMap;
    private List<Node> priorityQueue;

    public Huffman(RandomAccessFile source) {
        this.source = source;
        this.hashMap = new HashMap<Vector, Integer>();
        this.priorityQueue = new ArrayList<>() {
        };
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
     * Creates a Huffman tree based on the frequency information stored in the hashMap.
     * This method follows the Huffman coding algorithm to build the tree by merging nodes with the lowest frequencies.
     */
    public void createHuffmanTree(){
        /*
        * Create the list containing the Tree leafs.
        */
        for(Vector v: hashMap.keySet()){
            Leaf tmp = new Leaf(v, hashMap.get(v));
            priorityQueue.add(tmp);
        }
        /*
         * Sorting the list.
         */
        Collections.sort(priorityQueue);
        while(priorityQueue.size()>1){
            Node left = priorityQueue.removeFirst();
            Node right = priorityQueue.removeFirst();
            priorityQueue.add(mergeNodes(left,right));
            Collections.sort(priorityQueue);
        }
    }

    /**
     * Merges two nodes into a new node.
     *
     * @param left  The left node to merge.
     * @param right The right node to merge.
     * @return The new merged node.
     */
    private Node mergeNodes(Node left, Node right){
        return new Node(left, right);
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

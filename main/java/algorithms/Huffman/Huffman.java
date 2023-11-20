package main.java.algorithms.Huffman;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

/**
 * Huffman class represents a utility for Huffman coding, providing methods for creating a dictionary,
 * constructing a Huffman tree, and printing information about the vector frequencies.
 */
public class Huffman {


    private int blockSize;
    private RandomAccessFile target;
    private RandomAccessFile huffmanTree;

    private RandomAccessFile decodedHuffman;
    private final RandomAccessFile source;
    private HashMap<Vector, Integer> hashMap;
    private List<Node> priorityQueue;
    private HashMap<Vector, String> encodeDictionary;

    public HashMap<Vector, String> getEncodeDictionary() {
        return encodeDictionary;
    }
    public List<Node> getPriorityQueue() {
        return priorityQueue;
    }

    /**
     * Constructs a Huffman object with the specified RandomAccessFile as the data source.
     * Initializes the Huffman object with an empty frequency map, an empty priority queue,
     * and an empty encode dictionary.
     *
     * @param source The RandomAccessFile representing the source data for Huffman coding.
     */
    public Huffman(int blockSize, RandomAccessFile source, RandomAccessFile target) {
        this.source = source;
        this.hashMap = new HashMap<Vector, Integer>();
        this.priorityQueue = new ArrayList<>();
        this.encodeDictionary = new HashMap<Vector, String>();
        this.target = target;
        this.blockSize = blockSize;
    }

    /**
     * Creates a dictionary based on the specified block size from the source data.
     * The dictionary is represented using a HashMap where keys are vectors of bytes.
     *
     * @throws IOException If an I/O error occurs while reading from the source.
     */
    public void createDictionary() throws IOException {
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
     * Creates an encoding dictionary based on the Huffman tree, assigning binary representations
     * to each vector in the leaf nodes of the tree.
     *
     * @param root The root of the Huffman tree.
     * @param map  The HashMap to store the mappings of vectors to their binary representations.
     * @param bits A StringBuilder to build the binary representation during tree traversal.
     */
    private void createEncodeDictionary(Node root, HashMap<Vector, String> map,StringBuilder bits){
        if(root != null){
            createEncodeDictionary(root.getLeft(), map,bits.append("0"));
            if(root instanceof  Leaf){
                map.put(((Leaf) root).getVector(), bits.toString());
            }
            createEncodeDictionary(root.getRight(), map,bits.append("1"));
        }
        if(bits.length()>0){
            bits.deleteCharAt(bits.length()-1);
        }
    }

    /**
     * Encodes the source data using the Huffman encoding based on the created dictionary,
     * and writes the encoded bits to the specified RandomAccessFile with the given block size.
     *
     * @throws IOException If an I/O error occurs while reading from the source or writing to the target.
     */
    public void encode() throws IOException {
        System.out.println("Encoding");
        createEncodeDictionary(priorityQueue.getFirst(),encodeDictionary,new StringBuilder()); // conferir
        long pos = source.getFilePointer();
        target.seek(0);
        source.seek(0);
        while(source.getFilePointer()<source.length()){
            byte[] bytes = new byte[blockSize];
            source.read(bytes);
            Vector tmp = new Vector(bytes);
            String bits = encodeDictionary.get(tmp);
            boolean[] bitsToWrite = convertStringToBoolean(bits);
            for(Boolean b: bitsToWrite){
                target.writeBoolean(b);
            }
        }
        source.seek(pos);
    }

    /**
     * Decodes the Huffman-encoded data and writes the decoded bytes to a new RandomAccessFile.
     * This method assumes that the target RandomAccessFile contains Huffman-encoded data.
     *
     * @throws IOException If an I/O error occurs while decoding or writing to the output file.
     */
    public void decode() throws IOException {
        System.out.println("decoding");
        decodedHuffman = new RandomAccessFile("main/resources/decodedHuffman.bin", "rw");
        byte[] decoded = new byte[blockSize];
        target.seek(0);
        while(target.getFilePointer()<target.length()){
            Node navigate = priorityQueue.getFirst();
            while(!(navigate instanceof Leaf)){
                if(!target.readBoolean()){
                    navigate = navigate.getLeft();
                }
                else {
                    navigate = navigate.getRight();
                }
            }
            decodedHuffman.write(((Leaf) navigate).getVector().getArray());
        }
    }

    /**
     * Converts a string of bits into a boolean array, where each '1' in the string
     * is represented by true in the boolean array, and each '0' is represented by false.
     *
     * @param bits The string of bits to be converted.
     * @return A boolean array representing the input string of bits.
     */
    private boolean[] convertStringToBoolean(String bits){
        boolean out[] = new boolean[bits.length()];
        for(int i=0; i<bits.length(); i++){
            out[i] = bits.charAt(i) == '1';
        }
        return out;
    }

    /**
     * Prints the binary representations in the encodeDictionary for each vector.
     * Each line displays the binary representation corresponding to a vector.
     */
    public void printEncodeDictionary(){
        for(Vector s:encodeDictionary.keySet()){
            System.out.println(encodeDictionary.get(s));
        }
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

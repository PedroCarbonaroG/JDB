package main.java.algorithms.Huffman;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class HuffmanRunner {
    private Huffman huffman;
    public HuffmanRunner(int blockSize) throws FileNotFoundException {
        this.huffman = new Huffman(blockSize,new RandomAccessFile("main/resources/sequentialFile.bin", "rw"),new RandomAccessFile("/Users/gabrieltodt/JDB/main/resources/sequentialFileHuffman.bin", "rw"),new RandomAccessFile("/Users/gabrieltodt/JDB/main/resources/huffmanTree.bin", "rw"));
    }
    public void encode() throws IOException {
        huffman.createDictionary();
        huffman.createHuffmanTree();
        huffman.encode();
    }
    public void decode() throws IOException {
        huffman.decode();
    }
}

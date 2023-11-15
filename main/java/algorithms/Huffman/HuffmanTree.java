package main.java.algorithms.Huffman;

import java.io.RandomAccessFile;

public class HuffmanTree implements Comparable<HuffmanTree>{

    private Cell root;

    public Cell getRoot() {
        return root;
    }
    public void setRoot(Cell root) {
        this.root = root;
    }

    public HuffmanTree(Cell root){// Constructor using a root node.
        this.root = root;
    }

    /**
     *  Merges two huffman trees.
     *
     * @param tree1 first huffman tree.
     * @param tree2 second huffman tree.
     * @return the new root to the merged tree.
     */
    public static Cell mergeTree(HuffmanTree tree1, HuffmanTree tree2){
        Cell out = new Cell(tree1.getRoot().getFrequency()+tree2.getRoot().getFrequency());
        out.setLeft(tree1.getRoot());
        out.setRight(tree2.getRoot());
        return out;
    }

    @Override
    public int compareTo(HuffmanTree other) {
        return Integer.compare(this.root.getFrequency(), other.getRoot().getFrequency());
    }
}

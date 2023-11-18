package main.java.algorithms.Huffman;

import java.util.Arrays;

public class Leaf extends Node {
    private Vector vector;

    public Vector getVector() {
        return vector;
    }
    public Leaf(Vector vector, int frequency){
        this.vector = vector;
        this.setFrequency(frequency);
        this.setRight(null);
        this.setLeft(null);
    }

    @Override
    public String toString() {
        return Arrays.toString(vector.getArray())+" -> "+this.getFrequency();
    }
}

package main.java.algorithms.Huffman;

public class Cell {
    private int frequency;
    private char element;
    private Cell right;
    private  Cell left;


    public Cell(int frequency, char element){
        this.frequency = frequency;
        this.element = element;
    }
    public Cell(int frequency){
        this.frequency = frequency;
        element = '#';
    }



    public int getFrequency() {
        return frequency;
    }
    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }
    public char getElement() {
        return element;
    }
    public void setElement(char element) {
        this.element = element;
    }
    public Cell getLeft() {
        return left;
    }
    public void setLeft(Cell left) {
        this.left = left;
    }
    public Cell getRight() {
        return right;
    }
    public void setRight(Cell right) {
        this.right = right;
    }
}

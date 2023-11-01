package main.java.compression;

public class Leaf extends  Cell{

    private char element;

    public Leaf(Cell righ, Cell left) {
        super(righ, left);
    }

    public char getElement() {
        return element;
    }
    public void setElement(char element) {
        this.element = element;
    }
}

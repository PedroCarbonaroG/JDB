package main.java.algorithms.huffman;

public class Node implements Comparable<Node>{
    private int frequency;
    private Node left;
    private Node right;

    public void setRight(Node right) {
        this.right = right;
    }

    public Node getRight() {
        return right;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getLeft() {
        return left;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public Node(Node left, Node right){
        frequency = left.getFrequency()+ right.getFrequency();
        this.left = left;
        this.right = right;
    }
    public Node(){
        frequency = -1;
        left = null;
        right = null;
    }
    @Override
    public int compareTo(Node other) {
        return Integer.compare(this.frequency, other.getFrequency());
    }

}

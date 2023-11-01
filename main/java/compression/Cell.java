package main.java.compression;

public class Cell {
    private int frequency;

    public Cell(Cell righ, Cell left){
        frequency = righ.getFrequency() + left.getFrequency();
    }

    public int getFrequency() {
        return frequency;
    }
    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }
    public void printFrequency(){
        System.out.println(frequency);
    }
}

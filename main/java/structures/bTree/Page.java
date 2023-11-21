//Dependencies
package main.java.structures.bTree;

//-----------------------------------------------------
/*
 * @File.:      TrabalhoPratico1 - Node(Page) for Btree's Classes
 * @Author.:    Pedro Carbonaro
 * @Version.:   1.0
 * @Date.:      2023-08-17
 * @Copyright.: Copyright (c) 2023
*/
//-----------------------------------------------------

public class Page {

    private int order;
    private int numElements;
    private long[] downWards;
    private Record[] records;

    public Page(int order) {

        this.order = order;
        this.numElements = 0;

        this.downWards = new long[order-1];
        for (int i = 0; i < downWards.length;i++) { downWards[i] = -1; }

        this.records = new Record[order-1];
        for (int i = 0; i < records.length;i++) { records[i] = null; }
    }

    public int getOrder() {
        return order;
    }
    public int getNumElements() {
        return numElements;
    }
    public long[] getDownWards() {
        return downWards;
    }
    public Record[] getRecords() {
        return records;
    }

    public void setOrder(int order) {
        this.order = order;
    }
    public void setNumElements(int numElements) {
        this.numElements = numElements;
    }
    public void setDownWards(long[] downWards) {
        this.downWards = downWards;
    }
    public void setRecords(Record[] records) {
        this.records = records;
    }

}
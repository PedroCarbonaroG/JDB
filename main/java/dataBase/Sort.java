//Dependencies
package main.java.dataBase;

import java.io.RandomAccessFile;
import java.io.File;
import java.io.IOException;

import main.java.interfaces.Formats;

//-----------------------------------------------------
/*
 * @File.:      TrabalhoPratico1 - Sort Class
 * @Author.:    Pedro Carbonaro
 * @Version.:   1.0
 * @Date.:      2023-08-17
 * @Copyright.: Copyright (c) 2023
 */
//-----------------------------------------------------

public class Sort {

    /*
     * Global auxiliar attributes
    */
    private int recordsByBlock;
    private int numTmpFiles;

    /*
     * Getters - Setters
    */
    public int getRecordsByBlock() {
        return recordsByBlock;
    }
    public int getNumTmpFiles() {
        return numTmpFiles;
    }

    public void setNumTmpFiles(int numTmpFiles) {
        this.numTmpFiles = numTmpFiles;
    }
    public void setRecordsByBlock(int recordsByBlock) {
        this.recordsByBlock = recordsByBlock;
    }

    /*
     * Sorting by Balanced interleaving
     *
     * @param RandomAccessFile raf -> File that will
     * be ordenated.
     * @throws IOException if something goes wrong with file
     * or their content.
    */
    public void sortBI(RandomAccessFile source) throws Exception {

        /*
         * Doubling the amount of files because they
         * would be half for reading and the other for writing.
        */
        setNumTmpFiles(getNumTmpFiles() * 2);

        File[] rafTmpFiles = new File[getNumTmpFiles()];
        RandomAccessFile[] tmpFiles = new RandomAccessFile[getNumTmpFiles()];
        for (int i = 0; i < getNumTmpFiles(); i++) { tmpFiles[i] = new RandomAccessFile("main/resources/tmpFile" + i, "rw"); tmpFiles[i].writeInt(1); }
        for (int i = 0; i < getNumTmpFiles(); i++) { rafTmpFiles[i] = new File("main/resources/tmpFile" + i); }

        //Header remains the same
        source.seek(Integer.BYTES);
        
        /*
         * First part of sorting.
         *
         * untangling the main file into 2 temporary files.
        */
        distribution(source, tmpFiles);

        /*
         * Final merge to sort all file.
        */
        for (int i = 0; !Formats.singleFile(tmpFiles); setRecordsByBlock(getRecordsByBlock() * 2), i+=2) {

            intercalation(
            tmpFiles[i%getNumTmpFiles()],
            tmpFiles[(i+1)%getNumTmpFiles()], 
            tmpFiles[(i+2)%getNumTmpFiles()], 
            tmpFiles[(i+3)%getNumTmpFiles()]
            );
        }

        /*
         * Transferring the file resulting from 
         * the merges to the original file used in the method.
        */
        copyFileContent(Formats.lastFile(tmpFiles), source);

        /*
         * Method for delete all temporary files
         * created by RandomAccessFile.
        */
        for (int i = 0; i < getNumTmpFiles(); i++) { tmpFiles[i].close(); rafTmpFiles[i].delete(); }

    }
    /*
     * Auxiliar method for sorting the file by merging.
     *
     * @param RandomAccessFile raf -> Principal file to untangle
     * in other files for final sorting.
     * @param RandomAccessFile tmpFile1 -> first file used for merging.
     * @param RandomAccessFile tmpFile2 -> second file used for merging.
     * @throws Exception if something goes wrong with file content or sorting them.
    */
    private void distribution(RandomAccessFile source, RandomAccessFile[] tmpFiles) throws Exception {

        int blockSize = getRecordsByBlock();
        RandomAccessFile currentFile;
        boolean control = false;
        long filePointer;
        int numOfRecords;
        
        Record[] tmpRecords = new Record[blockSize];
        
        while (source.getFilePointer() < source.length()) {

            numOfRecords = 0;   //Controller for when file have less records then blockSize and do quickSort correctly
            for (int i = 0; i < blockSize && source.getFilePointer() < source.length(); i++) {
                source.readInt();
                filePointer = source.getFilePointer();
                tmpRecords[i] = Record.fromByteArray(source, filePointer);
                numOfRecords++;
            }

            quickSort(tmpRecords, 0, numOfRecords-1);

            currentFile = control ? tmpFiles[1] : tmpFiles[0];

            for (int i = 0; i < numOfRecords; i++) {
                currentFile.writeInt(Record.toByteArray(tmpRecords[i]).length);
                currentFile.write(Record.toByteArray(tmpRecords[i]));
            }

            control = !control;
        }
    }
    /*
     * Final merging of all files
     * 
     * @param RandomAccessFile firstFile  -> First file to be read
     * @param RandomAccessFile secondFile -> Second file to be read
     * @param RandomAccessFile thirdFile  -> First file to be wrote
     * @param RandomAccessFile fourthFile -> Second file to be wrote
     * @throws Exception if something goes wrong with any of files or
     * their content to be ordenated or intercalled.
    */
    private void intercalation(RandomAccessFile firstFile, RandomAccessFile secondFile,
        RandomAccessFile thirdFile, RandomAccessFile fourthFile) throws Exception {

        //Variable to switch between destination files (thirdFile and fourthFile).
        boolean changerFile = true;

        /*
         * Positioning all files after the Integer.BYTES(size of integer)
         * to skip the header of each file.
        */
        firstFile.seek(Integer.BYTES);
        secondFile.seek(Integer.BYTES);
        thirdFile.seek(Integer.BYTES);
        fourthFile.seek(Integer.BYTES);

        int blockSize = getRecordsByBlock();

        while (!Formats.EOF(firstFile) && !Formats.EOF(secondFile)) {
            
            int firstLimit = 0;
            int secondLimit = 0;

            //Arrays to store records and record sizes.
            Record[] tmpRecords1 = new Record[blockSize];
            Record[] tmpRecords2 = new Record[blockSize];
            int[] recordSizes1 = new int[blockSize];
            int[] recordSizes2 = new int[blockSize];

            //Read records from the first file.
            while (firstLimit < blockSize && !Formats.EOF(firstFile)) {
                recordSizes1[firstLimit] = firstFile.readInt();
                long filePointer1 = firstFile.getFilePointer();
                tmpRecords1[firstLimit] = Record.fromByteArray(firstFile, filePointer1);
                firstLimit++;
            }

            //Read records from the second file.
            while (secondLimit < blockSize && !Formats.EOF(secondFile)) {
                recordSizes2[secondLimit] = secondFile.readInt();
                long filePointer2 = secondFile.getFilePointer();
                tmpRecords2[secondLimit] = Record.fromByteArray(secondFile, filePointer2);
                secondLimit++;
            }

            int i = 0, j = 0;

            //Choose the destination file based on the changerFile variable.
            RandomAccessFile currentFile = changerFile ? thirdFile : fourthFile;

            //Merge and write records.
            while (i < firstLimit && j < secondLimit) {
                if (tmpRecords1[i].getGameId() < tmpRecords2[j].getGameId()) {
                    currentFile.writeInt(recordSizes1[i]);
                    currentFile.write(Record.toByteArray(tmpRecords1[i]));
                    i++;
                } else {
                    currentFile.writeInt(recordSizes2[j]);
                    currentFile.write(Record.toByteArray(tmpRecords2[j]));
                    j++;
                }
            }

            //Write remaining records from the first file.
            while (i < firstLimit) {
                currentFile.writeInt(recordSizes1[i]);
                currentFile.write(Record.toByteArray(tmpRecords1[i]));
                i++;
            }

            //Write remaining records from the second file.
            while (j < secondLimit) {
                currentFile.writeInt(recordSizes2[j]);
                currentFile.write(Record.toByteArray(tmpRecords2[j]));
                j++;
            }

            //Switch between destination files.
            changerFile = !changerFile;
        }

        //Copy any remaining data from the first file to the destination file.
        copyRemainingData(firstFile, changerFile ? thirdFile : fourthFile);
        //Copy any remaining data from the second file to the destination file.
        copyRemainingData(secondFile, changerFile ? thirdFile : fourthFile);

        //Set the length of the input files to zero as they are no longer needed.
        firstFile.setLength(0);
        secondFile.setLength(0);
    }
    /*
     * Method for transfer data from
     * one file to another.
     * @param RandomAccessFile source -> source file that will be transfered.
     * @param RandomAccessFile destination -> file that will receive transfered data.
    */
    private void copyRemainingData(RandomAccessFile source, RandomAccessFile destination) throws IOException {
        while (!Formats.EOF(source)) { destination.write(source.read()); }
    }
    /*
     * Method for copying content from a file with 
     * a header to another.
     * 
     * @param RandomAccessFile source -> source that will be transfered
     * @param RandomAccessFile destination -> destination of transference
    */
    private void copyFileContent(RandomAccessFile source, RandomAccessFile destination) throws Exception {

        destination.seek(0);

        int header = destination.readInt(); //Save the original header
        source.seek(0);
        source.writeInt(header);            //Write the original header into the final file
        
        //Copy the contents of the temporary file back to the original file
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = source.read(buffer)) != -1) {
            destination.write(buffer, 0, bytesRead);
        }

        source.close();
    }

    /*
     * To code
    */
    public void sortIFS(RandomAccessFile source) throws Exception {
        //To code
    }

    /*
     * To code
    */
    public void sortISS(RandomAccessFile source) throws Exception {
        //To code
    }

    /*
     * QuickSort
     *
     * @param Record[] records -> array that will be ordenated.
     * @param int left -> left side of respected array.
     * @param int right -> right side of respected array.
    */
    public void quickSort(Record[] records, int left, int right) {

        int i = left, j = right;
        Record pivot = records[(right + left) / 2];

        while (i <= j) {

            while (records[i].getGameId() < pivot.getGameId()) i++;
            while (records[j].getGameId() > pivot.getGameId()) j--;

            if (i <= j) {

                swap(records, i, j);

                i++;
                j--;
            }
        }

        if(left < j) quickSort(records, left, j);
        if(i < right) quickSort(records, i, right);
    }

    /*
     * Swap method between array indexes.
     *
     * @param Record[] records -> array that will be ordenated.
     * @param int i -> firs element to be replaced.
     * @param int j -> second element to be replaced.
    */
    private void swap(Record[] records, int i, int j) {

        Record tmp = records[i];
        records[i] = records[j];
        records[j] = tmp;
    }
}

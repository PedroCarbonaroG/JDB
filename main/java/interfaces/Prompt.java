//Dependencies
package main.java.interfaces;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import java.nio.charset.StandardCharsets;

import java.util.Scanner;

import main.java.algorithms.LZW.LZW;
import main.java.dataBase.Crud;
import main.java.dataBase.Record;
import main.java.dataBase.Sort;
import main.java.structures.BTree.BTree;
import main.java.structures.Hash.Hash;
import main.java.structures.invertedList.InvertedList;

//-----------------------------------------------------
/*
 * @File.:      TrabalhoPratico1 - Prompt Class
 * @Author.:    Pedro Carbonaro
 * @Version.:   1.0
 * @Date.:      2023-08-17
 * @Copyright.: Copyright (c) 2023
*/
//-----------------------------------------------------

public class Prompt {
    
    private final Scanner sc = new Scanner(System.in, StandardCharsets.UTF_8);
    private static final String dataBaseFilePath = "main/resources/gamesForTests.csv";

    /*
     * Necessary files to handle all types of services offered by 
     * the application and their respective other files for deletion 
     * at the end of the program.
    */
    private static RandomAccessFile sequentialRaf;
    private static RandomAccessFile bTreeRaf;
    private static RandomAccessFile hashRaf;
    private static RandomAccessFile invertedListRaf;
    private static RandomAccessFile lzwRaf;
    private File sequentialFile;
    private File bTreeFile;
    private File hashFile;
    private File invertedListFile;
    private File lzwFile;

    /*
     * Application structures
    */
    BTree bTree = new BTree();
    Hash hash = new Hash();
    InvertedList invertedList = new InvertedList();

    /*
     * Application algorithms
    */
    Sort sort = new Sort();
    Crud crud = new Crud();
    LZW lzw = new LZW();

    public void buildPrompt() throws Exception {

        RandomAccessFile source = new RandomAccessFile(dataBaseFilePath, "rw");

        sequentialRaf = new RandomAccessFile("main/resources/sequentialFile.bin", "rw");
        bTreeRaf = new RandomAccessFile("main/resources/bTreeFile.bin", "rw");
        hashRaf = new RandomAccessFile("main/resources/hashFile.bin", "rw");
        invertedListRaf = new RandomAccessFile("main/resources/invertedlistFile.bin", "rw");
        lzwRaf = new RandomAccessFile("main/resources/lzwFile.bin", "rw");

        sequentialFile = new File("main/resources/sequentialFile.bin");
        bTreeFile = new File("main/resources/bTreeFile.bin");
        hashFile = new File("main/resources/hashFile.bin");
        invertedListFile = new File("main/resources/invertedlistFile.bin");
        lzwFile = new File("main/resources/lzwFile.bin");

        initiateStructures(source);

        while (true) {

            System.out.println(
                "==================== INTERFACE ====================\n"
                + "Options->\n"
                + "(1)-> Crud Operations\n"
                + "(2)-> B Tree Operations\n"
                + "(3)-> Hash Operations\n"
                + "(4)-> InvetedList Operations\n\n"

                + "(5)-> Compression Operations\n"
                + "(6)-> PatternMatching Operations\n\n"

                + "(0)-> Finish Program\n"
                +
                "==================== INTERFACE ====================\n"
            );
            System.out.print("Your option: "); int userChoice = sc.nextInt();
            
            /*
             * Checking if userChoice is valid.
             * 
             * Need to change in method validChoice if
             * increases more choices.
            */
            if (!validChoice(userChoice, 7)) {

                while (!validChoice(userChoice, 7)) {

                    System.out.print("Invalid choice, try again: ");
                    userChoice = sc.nextInt();
                }
            }

            if (getService(userChoice, source) == 1) {
                return;
            }
        }
    }

    /*
     * Large and complex method as it has several branches, but necessary 
     * to execute the application, collects the user's choice and performs 
     * the necessary tasks.
     * 
     * @param int userChoice-> User request
     * @throws Exception if something goes wrong with data or method logic
    */
    private int getService(int userChoice, RandomAccessFile source) throws Exception {
        
        switch (userChoice) {

            case 0:
                endFiles();
                System.out.println("End of program");
            return 1;

            case 1:
                System.out.println("Records from dataBase have already been loaded!");

                int controlCase1 = -1;
                while (controlCase1 != 0) {

                    System.out.println(
                        "==================== INTERFACE ====================\n"
                        + "Options->\n"
                        + "(1)-> Create new Record\n"
                        + "(2)-> Read Record\n"
                        + "(3)-> Update Record\n"
                        + "(4)-> Delete Record\n\n"

                        + "(5)-> Sort DataBase\n"
                        + "(6)-> Show DataBase\n\n"

                        + "(0)-> return\n"
                        +
                        "==================== INTERFACE ====================\n"
                    );
                    System.out.print("Your option: "); int newChoice = sc.nextInt();

                    /*
                    * Checking if userChoice is valid.
                    * 
                    * Need to change in method validChoice if
                    * increases more choices.
                    */
                    if (!validChoice(newChoice, 7)) {

                        while (!validChoice(newChoice, 7)) {

                            System.out.print("Invalid choice, try again: ");
                            newChoice = sc.nextInt();
                        }
                    }

                    switch (newChoice) {

                        case 0:
                            controlCase1 = 0;
                        break;

                        case 1:
                            crud.createSingleRecord(sequentialRaf);
                
                            System.out.println("\nNew Record has been created successfully.");
                        break;

                        case 2:
                            System.out.print("\nWrite the IdRecord you want to check: ");
                
                            Record tmp;
                            do {
                                tmp = crud.select(sc.nextInt(), sequentialRaf);
                                if (tmp == null) { System.out.print("Record does not exist.\nTry another: "); }
                            
                            } while (tmp == null);
                            
                            System.out.println("\n" + tmp);
                        break;

                        case 3:
                            System.out.print("\nWrite the IdRecord you want to update: ");
                
                            if (crud.update(sc.nextInt(), sequentialRaf)) { System.out.println("\nRecord has been updated successfully."); } 
                            else { System.out.println("\nRecord could not be updated. Try another recordId."); }
                        break;

                        case 4:
                            System.out.print("\nWrite the IdRecord you want to delete: ");
                
                            int idToDelete;
                            do {
                                idToDelete = sc.nextInt();
                                if (!crud.delete(idToDelete, sequentialRaf)) {
                                    System.out.print("Record doesn't exist.\nTry to delete another: ");
                                }
                            } while (crud.delete(idToDelete, sequentialRaf));
                            
                            System.out.println("\nRecord has been deleted successfully.");
                        break;

                        case 5:
                            System.out.println("\nWitch sort will be used?");
                            System.out.println("(1)-> Balanced Interleaving");
                            System.out.println("(2)-> Interleaving with flex size");
                            System.out.println("(3)-> Interleaving with selection by substitution\n");
                            System.out.print("Your choice: "); int choice = sc.nextInt();

                            switch (choice) {

                                case 1:
                                    System.out.print("\nType how many files will be used: ");
                                    sort.setNumTmpFiles(sc.nextInt());

                                    System.out.print("Type how many blocks will be used for each step: ");
                                    sort.setRecordsByBlock(sc.nextInt());

                                    sort.sortBI(sequentialRaf);
                                    System.out.println("\nRecords have been sorted successfully.");
                                break;

                                case 2:
                                    System.out.print("Type how many blocks will be used for each step: ");
                                    sort.setRecordsByBlock(sc.nextInt());

                                    sort.sortIFS(sequentialRaf);
                                    System.out.println("\nRecords have been sorted successfully.");
                                break;

                                case 3:
                                    System.out.print("Type how many blocks will be used for each step: ");
                                    sort.setRecordsByBlock(sc.nextInt());

                                    sort.sortISS(sequentialRaf);
                                    System.out.println("\nRecords have been sorted successfully.");
                                break;

                                default:
                                    System.out.println("Wrong choice, try again!");
                                break;
                            }
                        break;

                        case 6:
                            System.out.println("\nRegistered Records at moment: ");
                            Utility.printByteArray(sequentialRaf);
                            System.out.println();
                        break;
                    }

                }

            break;

            case 2:
                //to code
            break;

            case 3:
                //to code
            break;

            case 4:
                //to code
            break;

            case 5:
                System.out.println("Records from dataBase have already been loaded!");

                int controlCase5 = -1;
                while (controlCase5 != 0) {

                    System.out.println(
                        "==================== INTERFACE ====================\n"
                        + "Options->\n"
                        + "(1)-> LZW compression\n"
                        + "(2)-> Huffman compression\n\n"

                        + "(0)-> return\n"
                        +
                        "==================== INTERFACE ====================\n"
                    );
                    System.out.print("Your option: "); int newChoice = sc.nextInt();

                    /*
                    * Checking if userChoice is valid.
                    * 
                    * Need to change in method validChoice if
                    * increases more choices.
                    */
                    if (!validChoice(newChoice, 3)) {

                        while (!validChoice(newChoice, 3)) {

                            System.out.print("Invalid choice, try again: ");
                            newChoice = sc.nextInt();
                        }
                    }

                    switch (newChoice) {

                        case 0:
                            controlCase5 = 0;
                        break;

                        case 1:
                            Utility.copyFileContent(sequentialRaf, lzwRaf);
                            lzw.compression(lzwRaf);
                        break;

                        case 2:
                            //Calls Huffman compression algorithmn
                        break;
                    }    
                }
            break;

            case 6:
                System.out.println("Records from dataBase have already been loaded!");

                int controlCase6 = -1;
                while (controlCase6 != 0) {

                    System.out.println(
                        "==================== INTERFACE ====================\n"
                        + "Options->\n"
                        + "(1)-> XXXXXXX compression\n"
                        + "(2)-> XXXXXXX compression\n\n"

                        + "(0)-> return\n"
                        +
                        "==================== INTERFACE ====================\n"
                    );
                    System.out.print("Your option: "); int newChoice = sc.nextInt();

                    /*
                    * Checking if userChoice is valid.
                    * 
                    * Need to change in method validChoice if
                    * increases more choices.
                    */
                    if (!validChoice(newChoice, 3)) {

                        while (!validChoice(newChoice, 3)) {

                            System.out.print("Invalid choice, try again: ");
                            newChoice = sc.nextInt();
                        }
                    }

                    switch (newChoice) {

                        case 0:
                            controlCase6 = 0;
                        break;

                    }    
                }
            break;
        }

        return 0;
    }

    /*
     * Method to initialize all structures with their respective 
     * private insertions using the chosen database.
     * 
     * @param RandomAcessFile source -> source that data from chose dataBase.
     * @throws Exception if something goes wrong with file access. 
    */
    private void initiateStructures(RandomAccessFile source) throws Exception {

        /*
         * NOTATION: 
         * Change insertion methods when they are complete
        */
        while (source.getFilePointer() < source.length()) { crud.createDataBase(new Record().buildRecord(source.readLine()), sequentialRaf); }
        source.seek(0);
        while (source.getFilePointer() < source.length()) { crud.createDataBase(new Record().buildRecord(source.readLine()), bTreeRaf); }
        source.seek(0);
        while (source.getFilePointer() < source.length()) { crud.createDataBase(new Record().buildRecord(source.readLine()), hashRaf); }
        source.seek(0);
        while (source.getFilePointer() < source.length()) { crud.createDataBase(new Record().buildRecord(source.readLine()), invertedListRaf); }
        source.seek(0);
    }

    /*
     * Validchecker method.
     * 
     * @param int x -> Variable used to check if that option
     * is valid for the application.
     * @param int limit -> Variable used to set the limit
    */
    private boolean validChoice(int x, int limit) { return x >= 0 && x < limit; }

    /*
     * Finishing all files for end of application, turn all to 0 length and close, after
     * that, program delete all of them.
    */
    private void endFiles() throws IOException {

        sequentialRaf.setLength(0); sequentialRaf.close(); sequentialFile.delete();
        bTreeRaf.setLength(0); bTreeRaf.close(); bTreeFile.delete();
        hashRaf.setLength(0); hashRaf.close(); hashFile.delete();
        invertedListRaf.setLength(0); invertedListRaf.close(); invertedListFile.delete();
        lzwRaf.setLength(0); lzwRaf.close(); lzwFile.delete();
    }
}

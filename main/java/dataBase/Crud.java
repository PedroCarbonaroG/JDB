//Dependencies
package main.java.dataBase;

import java.io.IOException;
import java.io.RandomAccessFile;

import java.nio.charset.StandardCharsets;

import java.util.Date;
import java.util.Scanner;

import main.java.interfaces.Utility;

//-----------------------------------------------------
/*
 * @File.:      TrabalhoPratico1 - Used DataBase
 * @Author.:    Pedro Carbonaro
 * @Version.:   1.0
 * @Date.:      2023-08-17
 * @Copyright.: Copyright (c) 2023
*/
//-----------------------------------------------------

/*
 * Responsible class for create, read, update and delete Records(From used dataBase)
 * in a File.
*/
public class Crud {

    //Global attributes
    private final Scanner sc = new Scanner(System.in, StandardCharsets.UTF_8);
    private static int newId = 4136;

    /*
     * Creating a new Record at file.
     * 
     * @param Record record -> Record to be created in File
     * @param RandomAcessFile source -> source in which it will be inserted
     * @throws Exception if something goes wrong with the
     * file reading
    */
    public void createDataBase(Record newRecord, RandomAccessFile source) throws Exception {

        /*
         * Starts setting the last record ID at
         * the begging of file.
        */
        source.seek(0);
        source.writeInt(newRecord.getGameId());

        /*
         * Come backs to final line of the file
         * and starts to build the new Record
         * there.
        */
        long filePointer = source.length();
        source.seek(filePointer);
        byte[] newRecordBytes = Record.toByteArray(newRecord);
        source.writeInt(newRecordBytes.length);
        source.write(newRecordBytes);
    }

    /*
     * Creating an individual Record requested by user.
     * 
     * @param RandomAcessFile source -> source in which it will be inserted
     * @throws Exception if something goes wrong with the
     * file reading
    */
    public final void createSingleRecord(RandomAccessFile source) throws Exception {
        Record tmp = new Record();

        tmp.setValid(true);
        tmp.setGameId(newId++);

        System.out.print("\nRecord name: ");
        tmp.setName(sc.nextLine());

        System.out.println("\nExample of ReleaseDate: Dec 2017");
        System.out.println("Months: Jan. Feb. Mar. Apr. May June July Aug. Sep. Oct. Nov. Dec.");
        System.out.print("Record ReleaseDate: ");
        tmp.setReleaseDate(Utility.convertToDate(sc.nextLine()));

        System.out.print("\nNumber of Record Genres: ");
        int newRecordGenres = sc.nextInt(); sc.nextLine();
        String[] genres = new String[newRecordGenres];
        for (int i = 0, j = 1; i < genres.length; i++, j++) {

            System.out.print(j + " Record Genre: ");
            genres[i] = sc.nextLine();
        }
        tmp.setGenres(genres);

        /*
         *  Setting other attributes to null
         *  for they be useless.
        */
        tmp.setOwners("");
        tmp.setAge(0);
        tmp.setPrice(0);
        tmp.setDlcs(0);
        tmp.setDlcs(0);
        tmp.setLanguages(genres);
        tmp.setWebsite("");
        tmp.setWindows(false);
        tmp.setMac(false);
        tmp.setLinux(false);
        tmp.setUpVotes(0);
        tmp.setDownVotes(0);
        tmp.setAvgPT(0);
        tmp.setDevelopers("");

        /*
         * Starts to build file with the
         * new Record.
        */
        source.seek(0);
        source.writeInt(tmp.getGameId());

        source.seek(source.length());
        byte[] newRecordBytes = Record.toByteArray(tmp);
        source.writeInt(newRecordBytes.length);
        source.write(newRecordBytes);
    }

    /*
     * Reading a specified Record at file.
     * 
     * @param int id -> Record id to be readed
     * @param RandomAcessFile source -> source in which it will be selected
     * @throws Exception if something goes wrong with the
     * file reading
    */
    public final Record select(int id, RandomAccessFile source) throws Exception {
        Record tmp = null;

        int isId;
        int skipRecord;
        long filePointer;

        source.seek(0);
        source.skipBytes(4);

        while (source.getFilePointer() < source.length()) {

            skipRecord = source.readInt();
            filePointer = source.getFilePointer();
            boolean token = source.readBoolean();
            isId = source.readInt();

            if (token) {

                if (isId == id) {

                    tmp = Record.fromByteArray(source, filePointer);
                } else {

                    /* 
                        * Coming back to initial token flag
                        * and skipping to next Record.
                    */
                    source.seek(filePointer);
                    source.skipBytes(skipRecord);
                }
            } else {

                /* 
                    * Coming back to initial token flag
                    * and skipping to next Record.
                */
                source.seek(filePointer);
                source.skipBytes(skipRecord);
            }
        }

        return tmp;
    }

    /*
     * Updating a specified Record at file.
     * 
     * @param int id -> Record id to be updated
     * @param RandomAcessFile source -> source in which it will be updated
     * @throws Exception if something goes wrong with the
     * file reading
    */
    public final boolean update(int id, RandomAccessFile source) throws Exception {
        boolean isUpdated = false;

        /*
         * Necessary attributes for
         * this method.
        */
        Record tmp = new Record();
        int userChoice;
        int isId, skipRecord;
        long filePointer;

        source.seek(0);
        source.skipBytes(4);

        while (source.getFilePointer() < source.length()) {

            skipRecord = source.readInt();
            filePointer = source.getFilePointer();
            boolean token = source.readBoolean();
            isId = source.readInt();

            if (token) {

                if (isId == id) {

                    source.seek(filePointer);

                    System.out.println("\nWitch attribute you want to change: ");
                    System.out.println("(1)-> Record Name.");
                    System.out.println("(2)-> Record ReleaseDate.");
                    System.out.println("(3)-> Record Genres.");
                    System.out.print("\nYour option: "); 
                    userChoice = sc.nextInt(); sc.nextLine(); //nextLine(); to clean the buffer

                    switch (userChoice) {

                        case 1:

                            //Building the new Record
                            tmp = Record.fromByteArray(source, filePointer);

                            /*
                             * Setting the last Record
                             * as false to build the new one.
                            */
                            source.seek(filePointer);
                            source.writeBoolean(false);                            

                            /*
                             * Getting the new name by 
                             * user choice.
                            */
                            System.out.print("\nWrite here the new name: ");
                            tmp.setName(sc.nextLine());

                            createDataBase(tmp, source);
                        break;

                        case 2:
                            System.out.println("\nExample of ReleaseDate: Dec 2017");
                            System.out.println("Months: Jan. Feb. Mar. Apr. May June July Aug. Sep. Oct. Nov. Dec.");
                            System.out.print("New Record ReleaseDate: ");
                            Date newDate = Utility.convertToDate(sc.nextLine());

                            /*
                             * Skipping useless attributes until
                             * the wanted one
                            */
                            source.readBoolean();
                            source.readInt();
                            source.readShort();
                            source.readUTF();

                            source.writeUTF(Utility.formatDate(newDate));
                        break;

                        case 3:

                            //New Record
                            tmp = Record.fromByteArray(source, filePointer);

                            /*
                             * Setting the last Record
                             * as false to build the new one
                            */
                            source.seek(filePointer);
                            source.writeBoolean(true);

                            /*
                             * Getting the new name by 
                             * user choice
                            */
                            System.out.print("\nNumber of Record Genres: ");
                            int newRecordGenres = sc.nextInt(); sc.nextLine();
                            String[] genres = new String[newRecordGenres];
                            for (int i = 0, j = 1; i < genres.length; i++) {

                                System.out.print(j++ + " Record Genre: ");
                                genres[i] = sc.nextLine();
                            }
                            tmp.setGenres(genres);

                            createDataBase(tmp, source);
                        break;
                    }

                    isUpdated = true;
                } else {

                    source.seek(filePointer);
                    source.skipBytes(skipRecord);
                }
            } else {

                source.seek(filePointer);
                source.skipBytes(skipRecord);
            }
        }

        return isUpdated;
    }

    /*
     * Deleting a specified Record at file.
     * 
     * @param int id -> Record id to be deleted
     * @param RandomAcessFile source -> source in which it will be deleted
     * @throws Exception if something goes wrong with the
     * file reading
    */
    public final boolean delete(int id, RandomAccessFile source) {
        boolean isDeleted = false;

        int isId, skipRecord;
        long filePointer;

        try {

            /*
             * Skipping the first 4 bytes for useless information.
            */
            source.seek(0);
            source.skipBytes(4);

            while (source.getFilePointer() < source.length()) {

                skipRecord = source.readInt();
                filePointer = source.getFilePointer();
                boolean token = source.readBoolean();
                isId = source.readInt();

                if (token) {

                    if (isId == id) {

                        source.seek(filePointer);
                        source.writeBoolean(false);
                        isDeleted = true;
                    } else {

                        /* 
                         * Coming back to initial token flag
                         * and skipping to next Record.
                        */
                        source.seek(filePointer);
                        source.skipBytes(skipRecord);
                    }
                } else {

                    /* 
                     * Coming back to initial token flag
                     * and skipping to next Record.
                    */
                    source.seek(filePointer);
                    source.skipBytes(skipRecord);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return isDeleted;
    }
}

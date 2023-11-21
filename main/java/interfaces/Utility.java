//Dependencies
package main.java.interfaces;

import java.io.IOException;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Locale;

import main.java.dataBase.Record;

//-----------------------------------------------------
/*
 * @File.:      TrabalhoPratico1 - Format Class
 * @Author.:    Pedro Carbonaro
 * @Version.:   1.0
 * @Date.:      2023-08-17
 * @Copyright.: Copyright (c) 2023
*/
//-----------------------------------------------------

public abstract class Utility {

    /*
     * Encoding used for printing and writing 
     * by keyboard in specified encoding. 
    */
    private static final String DEFAULT_ENCODING = "UTF-8";

    /**
     * Method for converting Date type into a String
     * 
     * @param Date date-> date to be transformed
     * into String type.
    */
    public static String formatDate(Date date) {
        return new SimpleDateFormat("MMM/yyyy", Locale.US).format(date);
    }

    /**
     * Method for converting String to Date type
     * 
     * @param String strDate -> String used to 
     * convert to a Date type
    */
    public static Date convertToDate(String strDate) {
        Date convertedDate = null;

        try { convertedDate = new SimpleDateFormat("MMM dd, yyyy", Locale.US).parse(strDate); } catch (ParseException e) {

            try { convertedDate = new SimpleDateFormat("MMM yyyy", Locale.US).parse(strDate); } 

            catch(ParseException pe) {
                
                try { convertedDate = new SimpleDateFormat("MMM/yyyy", Locale.US).parse(strDate); }
                catch (ParseException e1) { e1.printStackTrace(); }
            }
        }

        return convertedDate;
    }

    /**
     * Method for formating Record price
     * 
     * @param double price -> used for format price
     * in the right way.
    */
    public static String formatPrice(double price) {
        return new DecimalFormat("0.00").format(price);
    }

    /**
     * Method for calculate and format percentage
     * 
     * @param double upVotes -> used to calculate the avarage.
     * @param double downVotes -> used to calculate the avarage.
    */
    public static String formatUpVotes(double upVotes, double downVotes) {
        return ((int)Math.round((upVotes / (upVotes + downVotes)) * 100) + "%");
    }

    /**
     * Method for formating game avgPT (Hours)
     * 
     * @param int seconds -> seconds to be transformed.
    */
    public static String formatTime(int seconds) {

        if (seconds <= 0) { return "null"; }

        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        String formattedTime = "";

        if (hours > 0) { formattedTime += hours + "h "; }

        if (minutes > 0) { formattedTime += minutes + "m"; }

        return formattedTime.trim(); //Remove extra spaces if is necessary
    }

    /**
     * Method for show Languages and Genres array.
     * 
     * @param String[] array -> array that will be showed.
    */
    public static String[] showArray(String[] array) {

        myPrint("[ ");
        for (String s : array) { myPrint(s + " "); }
        myPrint("]");

        return array;
    }

    /**
     * Method for print an array of bytes.
     * 
     * @param RandomAcessFile raf -> File who will be used
     * to print everything in there.
     * @throws Exception if something goes wrong with file path
     * or file content.
    */
    public static void printByteArray(RandomAccessFile raf, boolean hasHeader) throws Exception {

        Record tmp = new Record();
        long filePointer;
        int recordSize;

        raf.seek(0);

        if (hasHeader) {

            //Skipping useless information (header)
            raf.skipBytes(4);

            while (raf.getFilePointer() < raf.length()) {

                recordSize = raf.readInt();
                filePointer = raf.getFilePointer();

                /*
                * Reading Record flag to know
                * if is a valid or not Record to be readed.
                */
                if (raf.readBoolean()) {

                    raf.seek(filePointer);

                    /*
                    * Assigning the attributes into the object sequentially
                    */
                    tmp.setValid(raf.readBoolean());
                    tmp.setGameId(raf.readInt());

                    /*
                    * Skipping 2 bytes (Short type) for cleaning the String size for control
                    */
                    raf.skipBytes(2);
                    tmp.setName(raf.readUTF());

                    tmp.setReleaseDate(Utility.convertToDate(raf.readUTF()));

                    /*
                    * Skipping 4 bytes for cleaning the String size for control
                    */
                    raf.skipBytes(4);
                    tmp.setOwners(raf.readUTF());

                    tmp.setAge(raf.readInt());
                    tmp.setPrice(raf.readDouble());
                    tmp.setDlcs(raf.readInt());

                    /*
                    * Needs to skip 4 bytes for each String in vect
                    * for cleaning the String size for control
                    */
                    int languagesSize = (int) raf.readShort();
                    String[] languages = new String[languagesSize];
                    for (int i = 0; i < languagesSize; i++) {

                        raf.skipBytes(2);
                        languages[i] = raf.readUTF();
                    }
                    tmp.setLanguages(languages);

                    /*
                    * Skipping 2 bytes for string length
                    */
                    raf.skipBytes(2);
                    tmp.setWebsite(raf.readUTF());

                    tmp.setWindows(raf.readBoolean());
                    tmp.setMac(raf.readBoolean());
                    tmp.setLinux(raf.readBoolean());
                    tmp.setUpVotes(raf.readInt());
                    tmp.setAvgPT(raf.readInt());

                    /*
                    * Skipping 2 bytes for string length
                    */
                    raf.skipBytes(2);
                    tmp.setDevelopers(raf.readUTF());

                    /*
                    * Needs to skip 4 bytes for each String
                    * for cleaning the String size for control
                    */
                    int genresSize = (int) raf.readShort();
                    String[] genres = new String[genresSize];
                    for (int i = 0; i < genresSize; i++) {

                        raf.skipBytes(2);
                        genres[i] = raf.readUTF();
                    }
                    tmp.setGenres(genres);

                    System.out.println("\n" + tmp);

                } else { raf.seek(filePointer); raf.skipBytes(recordSize); }
            }   
        } else {

            while (raf.getFilePointer() < raf.length()) {

                recordSize = raf.readInt();
                filePointer = raf.getFilePointer();

                /*
                * Reading Record flag to know
                * if is a valid or not Record to be readed.
                */
                if (raf.readBoolean()) {

                    raf.seek(filePointer);

                    /*
                    * Assigning the attributes into the object sequentially
                    */
                    tmp.setValid(raf.readBoolean());
                    tmp.setGameId(raf.readInt());

                    /*
                    * Skipping 2 bytes (Short type) for cleaning the String size for control
                    */
                    raf.skipBytes(2);
                    tmp.setName(raf.readUTF());

                    tmp.setReleaseDate(Utility.convertToDate(raf.readUTF()));

                    /*
                    * Skipping 4 bytes for cleaning the String size for control
                    */
                    raf.skipBytes(4);
                    tmp.setOwners(raf.readUTF());

                    tmp.setAge(raf.readInt());
                    tmp.setPrice(raf.readDouble());
                    tmp.setDlcs(raf.readInt());

                    /*
                    * Needs to skip 4 bytes for each String in vect
                    * for cleaning the String size for control
                    */
                    int languagesSize = (int) raf.readShort();
                    String[] languages = new String[languagesSize];
                    for (int i = 0; i < languagesSize; i++) {

                        raf.skipBytes(2);
                        languages[i] = raf.readUTF();
                    }
                    tmp.setLanguages(languages);

                    /*
                    * Skipping 2 bytes for string length
                    */
                    raf.skipBytes(2);
                    tmp.setWebsite(raf.readUTF());

                    tmp.setWindows(raf.readBoolean());
                    tmp.setMac(raf.readBoolean());
                    tmp.setLinux(raf.readBoolean());
                    tmp.setUpVotes(raf.readInt());
                    tmp.setAvgPT(raf.readInt());

                    /*
                    * Skipping 2 bytes for string length
                    */
                    raf.skipBytes(2);
                    tmp.setDevelopers(raf.readUTF());

                    /*
                    * Needs to skip 4 bytes for each String
                    * for cleaning the String size for control
                    */
                    int genresSize = (int) raf.readShort();
                    String[] genres = new String[genresSize];
                    for (int i = 0; i < genresSize; i++) {

                        raf.skipBytes(2);
                        genres[i] = raf.readUTF();
                    }
                    tmp.setGenres(genres);

                    System.out.println("\n" + tmp);

                } else { raf.seek(filePointer); raf.skipBytes(recordSize); }
            }
        }
    }

    /**
     * EndOfFile checking method.
     * 
     * @param RandomAcessFile raf -> File used for checking while
     * file was not finished.
     * @throws IOException if something goes wrong while
     * this file is checking for the EOF.
    */
    public static boolean EOF(RandomAccessFile raf) throws IOException {

        try { return raf.getFilePointer() >= raf.length();}
        catch (IOException e) { throw new IOException("Error while was checking the EOF.", e); }
    }

    /**
     * Checking if in a RandomAccessFile[]
     * has only one file with content
     * 
     * @param RandomAccessFile[] files -> array of RandomAccessFile
     * to be checked
     * @throws Exception if something goes wrong with file.
    */
    public static boolean isSingleFile(RandomAccessFile[] files) throws Exception {
        int filesEmpty = 0;

        for (RandomAccessFile file : files) { if (file.length() == 0) { filesEmpty++; } }

        return filesEmpty == files.length - 1;
    }

    /**
     * Last file verification
     *
     * @param RandomAccessFile[] files -> Array of rafs to be checked
     * and if in that array, just one file has content, will return it.
     * @throws Exception if something goes wrong with file.
    */
    public static RandomAccessFile getLastFile(RandomAccessFile[] files) throws Exception {
        RandomAccessFile tmp = null;

        for (RandomAccessFile file : files) { if (file.length() != 0) { tmp = file; } }

        return tmp;
    }

    /**
     * Method for transfer data from
     * one file to another.
     * @param RandomAccessFile source -> source file that will be transfered.
     * @param RandomAccessFile destination -> file that will receive transfered data.
    */
    public static void copyRemainingData(RandomAccessFile source, RandomAccessFile destination) throws IOException {
        while (!Utility.EOF(source)) { destination.write(source.read()); }
    }

    /**
     * Method for copying content from a file with 
     * a header to another.
     * 
     * @param RandomAccessFile source -> source that will be transfered
     * @param RandomAccessFile destination -> destination of transference
    */
    public static void copyFileContent(RandomAccessFile source, RandomAccessFile destination, boolean hasHeader) throws Exception {

        if (hasHeader) {
            source.seek(0);
            destination.seek(0);

            int header = source.readInt();           //Save the original header
            destination.writeInt(header);            //Write the original header into the final file

            //Copy the contents of the temporary file back to the original file
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = source.read(buffer)) != -1) {
                destination.write(buffer, 0, bytesRead);
            }
        }
    }

    /**
     * transferFileContent Method
     * 
     * @param RandomAccessFile source -> source that will transfer
     * all content.
     * @param RandomAccessFile destination -> file that will receive
     * all content by source.
     * @param boolean hasHeader -> boolean flag to know if one or other
     * file has an header.
    */
    public static void transferFileContent(RandomAccessFile source, RandomAccessFile destination, boolean hasHeader) {

        try {

            if (hasHeader) {
                if (hasHeader) {
                    source.seek(0);
                    destination.setLength(0);
                    destination.seek(0);

                    int header = source.readInt();           //Save the original header
                    destination.writeInt(header);            //Write the original header into the final file

                    //Copy the contents of the temporary file back to the original file
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = source.read(buffer)) != -1) {
                        destination.write(buffer, 0, bytesRead);
                    }
                }
            } else {

                destination.setLength(0);
                source.seek(0);
                destination.seek(0);

                //Copy the contents of the temporary file back to the original file
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = source.read(buffer)) != -1) {
                    destination.write(buffer, 0, bytesRead);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    /**
     * Method to turn a char into your
     * respective binary value
     * 
     * @param char character -> character that will be transformed
    */
    public static String charToBinary(char character) {
        return Integer.toBinaryString(character);
    }

    /**
     * Method to transform an entire string
     * into your binary value, character by character
     * 
     * @param String inputString -> string that will be
     * transformed into binary code
    */
    public static String stringToBinary(String inputString) {
        StringBuilder binaryString = new StringBuilder();
        for (int i = 0; i < inputString.length(); i++) {
            char character = inputString.charAt(i);
            String binaryValue = charToBinary(character);
            binaryString.append(binaryValue);
        }
        return binaryString.toString();
    }

    /**
     * Method to convert a binary string to its corresponding character
     *
     * @param String binaryString -> binary string to be transformed
     */
    public static char binaryToChar(String binaryString) {
        int charCode = Integer.parseInt(binaryString, 2);
        return (char) charCode;
    }

    /**
     * Method to convert a binary string to its corresponding string of characters
     *
     * @param String binaryString -> binary string to be transformed into characters
     */
    public static String binaryToString(String binaryString) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < binaryString.length(); i += 8) {
            String binaryChar = binaryString.substring(i, Math.min(i + 8, binaryString.length()));
            int charCode = Integer.parseInt(binaryChar, 2);
            result.append((char) charCode);
        }
        return result.toString();
    }

    /**
     * myPrint method.
     * 
     * @param String line -> String to be converted to 
     * expected encoding without breakline.
    */
    public static void myPrint(String line) {

        try {

            PrintStream psOut = new PrintStream(System.out, true, DEFAULT_ENCODING);
            psOut.print(line);
        }
        catch (UnsupportedEncodingException e) { e.printStackTrace(); }
    }

    /**
     * myPrintln method.
     * 
     * @param String line -> String to be converted to 
     * expected encoding with breakline.
    */
    public static void myPrintln(String line) {

        try {

            PrintStream psOut = new PrintStream(System.out, true, DEFAULT_ENCODING);
            psOut.println(line);
        }
        catch (UnsupportedEncodingException e) { e.printStackTrace(); }
    }
}

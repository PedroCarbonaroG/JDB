//Dependencies
package main.java.dataBase;

import main.java.interfaces.Formats;

import java.util.Date;
import java.util.Arrays;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.RandomAccessFile;

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
 *  Games class that has its attributes.
*/
public class Record {

    //Private attributes
    private boolean valid;
    private int gameId;
    private String name;
    private Date releaseDate;
    private String owners;
    private int age;
    private double price;
    private int dlcs;
    private String[] languages;
    private String website;
    private boolean windows;
    private boolean mac;
    private boolean linux;
    private double upVotes;
    private double donwVotes;
    private int avgPT;
    private String developers;
    private String[] genres;

    //Constructor without parameters.
    public Record() {

        this.valid = false;
        this.gameId = 0;
        this.name = "";
        this.releaseDate = null;
        this.owners = "";
        this.age = 0;
        this.price = 0.0;
        this.dlcs = 0;
        this.languages = null;
        this.website = "";
        this.windows = false;
        this.mac = false;
        this.linux = false;
        this.upVotes = 0.0;
        this.donwVotes = 0.0;
        this.avgPT = 0;
        this.developers = "";
        this.genres = null;
    }
    //Constructor with parameters.
    public Record(boolean valid, int gameId, String name, Date releaseDate, String owners, int age, double price,
    int dlcs, String[] languages, String website, boolean windows, boolean mac, boolean linux, double upvotes, 
    double donwVotes, int avgPT, String developers, String[] genres) {

        //Assigning the values.
        this.valid = true;
        this.gameId = gameId;
        this.name = name;
        this.releaseDate = releaseDate;
        this.owners = owners;
        this.age = age;
        this.price = price;
        this.dlcs = dlcs;
        this.languages = languages;
        this.website = website;
        this.windows = windows;
        this.mac = mac;
        this.linux = linux;
        this.upVotes = upvotes;
        this.donwVotes = donwVotes;
        this.avgPT = avgPT;
        this.developers = developers;
        this.genres = genres;
    }

    //Clone method (used for clone a game).
    public Record clone() {

        Record clone = new Record();

        clone.setValid(this.valid);
        clone.setGameId(this.gameId);
        clone.setName(this.name);
        clone.setReleaseDate(this.releaseDate);
        clone.setOwners(this.owners);
        clone.setAge(this.age);
        clone.setPrice(this.price);
        clone.setDlcs(this.dlcs);
        clone.setLanguages(this.languages);
        clone.setWebsite(this.website);
        clone.setWindows(this.windows);
        clone.setMac(this.mac);
        clone.setLinux(this.linux);
        clone.setUpVotes(this.upVotes);
        clone.setDownVotes(donwVotes);
        clone.setAvgPT(this.avgPT);
        clone.setDevelopers(this.developers);
        clone.setGenres(this.genres);

        return clone;
    }

    //Getters.
    public final boolean getValid() { return this.valid; }
    public final int getGameId() { return this.gameId; }
    public final String getName() { return this.name; }
    public final Date getReleaseDate() { return this.releaseDate; }
    public final String getOwners() { return this.owners; }
    public final int getAge() { return this.age; }
    public final double getPrice() { return this.price; }
    public final int getDlcs() { return this.dlcs; }
    public final String[] getLanguages() { return this.languages; }
    public final String getWebsite() { return this.website; }
    public final boolean getWindows() { return this.windows; }
    public final boolean getMac() { return this.mac; }
    public final boolean getLinux() { return this.linux; }
    public final double getUpVotes() { return this.upVotes; }
    public final double getDownVotes() { return this.donwVotes; }
    public final int getAvgPT() { return this.avgPT; }
    public final String getDevelopers() { return this.developers; }
    public final String[] getGenres() { return this.genres; }

    //Setters.
    public final void setValid(boolean valid) { this.valid = valid; }
    public final void setGameId(int gameId) { this.gameId = gameId; }
    public final void setName(String name) { this.name = name; }
    public final void setReleaseDate(Date releaseDate) { this.releaseDate = releaseDate; }
    public final void setOwners(String owners) { this.owners = owners; }
    public final void setAge(int age) { this.age = age; }
    public final void setPrice(double price) { this.price = price; }
    public final void setDlcs(int dlcs) { this.dlcs = dlcs; }
    public final void setLanguages(String[] languages) { this.languages = languages; }
    public final void setWebsite(String website) { this.website = website; }
    public final void setWindows(boolean windows) { this.windows = windows; }
    public final void setMac(boolean mac) { this.mac = mac; }
    public final void setLinux(boolean linux) { this.linux = linux; }
    public final void setUpVotes(double upvotes) { this.upVotes = upvotes; }
    public final void setDownVotes(double donwVotes) { this.donwVotes = donwVotes; }
    public final void setAvgPT(int avgPT) { this.avgPT = avgPT; }
    public final void setDevelopers(String developers) { this.developers = developers; }
    public final void setGenres(String[] genres) { this.genres = genres; }

    /*
     * Parser method.
     * 
     * @param String line -> line extracted from csv file containing
     * all the information about the Record object.
    */
    public final Record buildRecord(String line) {

        //Atributes.
        int key = 0;
        char controler = ',';
        String auxString = "";
        Record tmp = new Record();
        
        for (int i = 0; i < line.length(); i++) {

            if (i == line.length() - 1) {

                setAttributes(tmp, key++, auxString);
            } else if (line.charAt(i) != controler) {

                auxString += line.charAt(i);
            } else {
                
                if (line.charAt(i + 1) == '"') {

                    setAttributes(tmp, key++, auxString);
                    auxString = "";
                    controler = '"'; i++;
                } else if (controler == '"') {

                    setAttributes(tmp, key++, auxString);
                    auxString = "";
                    controler = ','; i++;

                    //Way to treat the dataBase correctly at final attribute.
                    if (key == 16) { controler = '"'; i++; }

                } else {

                    setAttributes(tmp, key++, auxString);
                    auxString = "";
                }

            }
        }

        /*
         * Turn into a valid game.
        */
        tmp.setValid(true);

        return tmp;
    }
    /*
     * Setting the Record attributes by an key.
     * 
     * @param Record game -> record to be setted by given attributes.
     * @param int key -> used to split the attributes by index.
     * @param String attribute -> attribute in a string to be converted in their
     * respective field.
    */
    private final void setAttributes(Record game, int key, String attribute) {

        switch (key) {

            //GameID
            case 0 -> game.setGameId(Integer.parseInt(attribute));

            //Game Name
            case 1 -> game.setName(attribute);

            //Game ReleaseDate
            case 2 -> game.setReleaseDate(Formats.convertToDate(attribute));

            //Game Owner
            case 3 -> game.setOwners(attribute);

            //Game Age
            case 4 -> game.setAge(Integer.parseInt(attribute));

            //Game Price
            case 5 -> game.setPrice(Double.parseDouble(attribute));

            //Game Dlcs
            case 6 -> game.setDlcs(Integer.parseInt(attribute));

            //Game Languages
            case 7 -> game.setLanguages(attribute.replaceAll("'", "").replaceAll("\"", "")
                    .replace('[', ' ').replace(']', ' ').replaceAll(" ", "")
            .split(","));

            //Game WebSite
            case 8 -> game.setWebsite(attribute.equals("") ? "null" : attribute);

            //Game Windows ON-OFF
            case 9 -> game.setWindows(Boolean.parseBoolean(attribute));

            //Game Mac ON-OFF
            case 10 -> game.setMac(Boolean.parseBoolean(attribute));

            //Game Linux ON-OFF
            case 11 -> game.setLinux(Boolean.parseBoolean(attribute));

            //Game Upvotes
            case 12 -> game.setUpVotes(Integer.parseInt(attribute));

            //Game DonwVotes
            case 13 -> game.setDownVotes(Integer.parseInt(attribute));

            //Game AvgPT
            case 14 -> game.setAvgPT(Integer.parseInt(attribute));

            //Game Developers
            case 15 -> game.setDevelopers(attribute);

            //Game Genres
            case 16 -> game.setGenres(attribute.replaceAll("\"", "").split(","));
        }
    }

    /*
     * Convert the Record object into byte array.
     * 
     * @param Record record -> element to be converted.
     * @throws Exception if can't convert into byte array.
    */
    public static final byte[] toByteArray(Record record) throws Exception {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        //Converting dataAttributes into bytes
        //Checking the file

        dos.writeBoolean(record.getValid());

        //Writing the gameId as a short for memory economy
        dos.writeInt(record.getGameId());

        //Writing GameName
        dos.writeShort((short)record.getName().length());
        dos.writeUTF(record.getName());

        //Writing GameDate
        dos.writeUTF(Formats.formatDate(record.getReleaseDate()));

        //Writing GameOwners
        dos.writeInt(record.getOwners().length());
        dos.writeUTF(record.getOwners());

        //Writing GameAge - GamePrice - GameDlcs
        dos.writeInt(record.getAge());
        dos.writeDouble(record.getPrice());
        dos.writeInt(record.getDlcs());

        //Writing GameLanguages
        String[] languages = record.getLanguages();
        dos.writeShort((short) languages.length);
        for (String language : languages) {

            dos.writeShort((short) language.length());
            dos.writeUTF(language);
        }

        //Writing GameWebSite
        dos.writeShort((short) record.getWebsite().length());
        dos.writeUTF(record.getWebsite());

        //Writing GameWindows - GameMac - GameLinux
        dos.writeBoolean(record.getWindows());
        dos.writeBoolean(record.getMac());
        dos.writeBoolean(record.getLinux());

        //Writing GameUpvotes
        dos.writeInt(Integer.parseInt(Formats.formatUpVotes(record.getUpVotes(), record.getDownVotes()).replaceAll("%", "")));

        //Writing GameAvgPT
        dos.writeInt(record.getAvgPT());

        //Writing GameDevelopers
        dos.writeShort((short) record.getDevelopers().length());
        dos.writeUTF(record.getDevelopers());

        //Writing GameGenres
        String[] genres = record.getGenres();
        dos.writeShort((short) genres.length);
        for (String genre : genres) {

            dos.writeShort((short) genre.length());
            dos.writeUTF(genre);
        }

        return baos.toByteArray();
    }

    /*
     * Convert an byte array into Record object.
     * 
     * @param RandomAcessFile raf -> File used for get the bytes 
     * and turn into a Record object.
     * @param long filePointer -> Pointer index of file to get
     * the right byte array.
     * 
     * @throws Exception if can't convert into Record object.
    */
    public static final Record fromByteArray(RandomAccessFile raf, long filePointer) throws Exception {

        Record tmp = new Record();

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
        
        tmp.setReleaseDate(Formats.convertToDate(raf.readUTF()));

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

        return tmp;
    }

    /*
     * Used record have more attributes
     * but for the application is only needed
     * those attributes in toString.
     * 
     * If it is necessary to check any other 
     * attribute, simply include it in the method.
    */
    @Override
    public String toString() {

        return  "Id: " + gameId + " || " + " Name: " + name
                + " || " + " ReleaseDate: " + Formats.formatDate(releaseDate)
                + " || " + " Genres: " + Arrays.asList(genres);
    }
}

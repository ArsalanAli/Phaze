package teamb.minicap.phaze;

/**
 * Created by Kareem on 2016-03-05.
 */
public class Tracks{

    private long identification;
    private String title;
    private String artist;
    private String album;
    private String year;
    private byte[] albumart;

    public Tracks(long ID, String trackName, String trackArtist, String trackAlbum, String trackYear, byte[] albumstuff){

        identification = ID;
        title = trackName;
        artist = trackArtist;
        album = trackAlbum;
        year = trackYear;
        albumart = albumstuff;
    }
    public long getID(){return identification;}
    public String getTitle(){return title;}
    public String getArtist(){return artist;}
    public String getAlbum(){return album;}
    public String getYear(){return year;}
    public byte[] getAlbumArt(){return albumart;}
}

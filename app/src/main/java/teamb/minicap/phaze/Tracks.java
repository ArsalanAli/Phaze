package teamb.minicap.phaze;

//Retrieve and store songs

public class Tracks{

    private long identification;
    private String title;
    private String artist;
    private String album;
    private String year;

    public Tracks(long ID, String trackName, String trackArtist, String trackAlbum, String trackYear){

        identification = ID;
        title = trackName;
        artist = trackArtist;
        album = trackAlbum;
        year = trackYear;
    }
    public long getID(){return identification;}
    public String getTitle(){return title;}
    public String getArtist(){return artist;}
    public String getAlbum(){return album;}
    public String getYear(){return year;}
}

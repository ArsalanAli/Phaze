package teamb.minicap.phaze;

//Retrieve and store songs

public class Tracks{

    private long identification;
    private String title;
    private String artist;


    public Tracks(long ID, String trackName, String trackArtist){

        identification = ID;
        title = trackName;
        artist = trackArtist;

    }
    public long getID(){return identification;}
    public String getTitle(){return title;}
    public String getArtist(){return artist;}

}

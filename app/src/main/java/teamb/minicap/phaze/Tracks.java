package teamb.minicap.phaze;

/**
 * Created by Kareem on 2016-03-05.
 */
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

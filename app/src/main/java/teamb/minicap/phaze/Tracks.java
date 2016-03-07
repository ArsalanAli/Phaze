package teamb.minicap.phaze;

/** Created by Kareem on 2016-03-05.*/
public class Tracks{

    //Defines variables to be assigned values when song is fetched
    private long identification;
    private String title;
    private String artist;
    private String album;
    private String year;

    //Constructor method so that we instantiate the variables
    public Tracks(long ID, String trackName, String trackArtist, String trackAlbum, String trackYear){

        identification = ID;
        title = trackName;
        artist = trackArtist;
        album = trackAlbum;
        year = trackYear;
    }
    //Fetches the appropriate vales for each of the criterion
    public long getID(){return identification;}
    public String getTitle(){return title;}
    public String getArtist(){return artist;}
    public String getAlbum(){return album;}
    public String getYear(){return year;}
}

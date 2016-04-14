package teamb.minicap.phaze;

import java.io.Serializable;

/**
 * Created by Kareem on 2016-03-25.
 */
public class pics implements Serializable{
    private long identification;
    private String title;
    private int image;

    public pics(long ID, String Name, int img){
        super();
        image = img;
        identification = ID;
        title = Name;
    }
    public long getID(){return identification;}
    public String getTitle(){return title;}
    public int getImg(){return image;}

}
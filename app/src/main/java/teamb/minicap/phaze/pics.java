package teamb.minicap.phaze;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by Kareem on 2016-03-25.
 */
public class pics{
    private long identification;
    private String title;
    private Bitmap image;

    public pics(long ID, String Name, Bitmap image){
        super();
        this.image = image;
        identification = ID;
        title = Name;
    }
    public Bitmap getImage(){ return image;}
    public long getID(){return identification;}
    public String getTitle(){return title;}
    public void setImage(Bitmap image){this.image = image;}

}
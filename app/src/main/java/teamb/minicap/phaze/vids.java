package teamb.minicap.phaze;

import android.graphics.Bitmap;
import android.media.Image;

import java.io.Serializable;

/**
 * Created by arsalan on 3/23/2016.
 */
public class vids implements Serializable {
    private long identification;
    private String title;
    private Bitmap Tnail;
    public vids(long ID, String Name, Bitmap tnail){

        identification = ID;
        title = Name;
        Tnail = tnail;
    }
    public long getID(){return identification;}
    public String getTitle(){return title;}
    public Bitmap getTnail(){return Tnail;}
}

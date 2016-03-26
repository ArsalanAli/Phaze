package teamb.minicap.phaze;

import java.io.Serializable;

/**
 * Created by arsalan on 3/23/2016.
 */
public class vids implements Serializable {
    private long identification;
    private String title;

    public vids(long ID, String Name){

        identification = ID;
        title = Name;
    }
    public long getID(){return identification;}
    public String getTitle(){return title;}
}

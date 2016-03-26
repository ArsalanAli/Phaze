package teamb.minicap.phaze;

import java.io.Serializable;

/**
 * Created by Kareem on 2016-03-25.
 */
public class pics implements Serializable {
    private long identification;
    private String title;

    public pics(long ID, String Name){

        identification = ID;
        title = Name;
    }
    public long getID(){return identification;}
    public String getTitle(){return title;}
}

package teamb.minicap.phaze;


import java.io.Serializable;

/**
 * Created by arsalan on 3/23/2016.
 */
public class vids implements Serializable {
    private long identification;
    private String title;
    private int Tnail;

    public vids(long ID, String Name, int tnail){

        identification = ID;
        title = Name;
        Tnail = tnail;
    }
    public long getID(){return identification;}
    public String getTitle(){return title;}
    public int getTnail(){return Tnail;}
}

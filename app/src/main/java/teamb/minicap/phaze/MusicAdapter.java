package teamb.minicap.phaze;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

//Whenever a listview is created
//an adapter is required in order
//to modify each entry separately

public class MusicAdapter extends BaseAdapter {

    private ArrayList<Tracks> tracks;
    private Context C;
    private LayoutInflater details;

    public MusicAdapter(Context c, ArrayList<Tracks> theTracks){
        tracks = theTracks;
        details = LayoutInflater.from(c);
        C = c;
    }

    @Override
    public int getCount() {
        return tracks.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //map to song layout
        LinearLayout trackLayout = (LinearLayout)details.inflate
                (R.layout.tracks, parent, false);
        TextView trackView = (TextView)trackLayout.findViewById(R.id.track_title);
        TextView artistView = (TextView)trackLayout.findViewById(R.id.track_artist);
        ImageView albumArt = (ImageView)trackLayout.findViewById(R.id.albumArt);
        Tracks current = tracks.get(position);
        trackView.setText(current.getTitle());
        artistView.setText(current.getArtist());
        byte[] aa = current.getAlbumArt();
        if (aa != null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(aa, 0, aa.length);
            int width = 300;
            int height = 300;
            Bitmap resized = Bitmap.createScaledBitmap(bitmap,width,height,true);
            albumArt.setImageBitmap(resized);
        }
        else{
            Resources res = C.getResources();
            Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.defaultart);
            int width = 300;
            int height = 300;
            Bitmap resized = Bitmap.createScaledBitmap(bitmap,width,height,true);
            albumArt.setImageBitmap(resized);
        }
        trackLayout.setTag(position);
        return trackLayout;
    }
}



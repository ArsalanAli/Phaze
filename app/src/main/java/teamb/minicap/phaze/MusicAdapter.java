package teamb.minicap.phaze;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Kareem on 2016-03-05.
 */
public class MusicAdapter extends BaseAdapter {

    private ArrayList<Tracks> tracks;
    private LayoutInflater details;

    public MusicAdapter(Context c, ArrayList<Tracks> theTracks){
        tracks = theTracks;
        details = LayoutInflater.from(c);
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
        Tracks current = tracks.get(position);
        trackView.setText(current.getTitle());
        artistView.setText(current.getArtist());
        trackLayout.setTag(position);
        return trackLayout;
    }
}



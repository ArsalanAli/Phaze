package teamb.minicap.phaze;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by arsalan on 3/23/2016.
 */
public class videoAdapter extends BaseAdapter{
    private ArrayList<vids> videos;
    private LayoutInflater details;

    public videoAdapter(Context c, ArrayList<vids> thevids){
        videos = thevids;
        details = LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return videos.size();
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
        LinearLayout vidsLayout = (LinearLayout)details.inflate
                (R.layout.vids, parent, false);
        TextView vidView = (TextView)vidsLayout.findViewById(R.id.vids_title);
        vids current = videos.get(position);
        vidView.setText(current.getTitle());
        vidsLayout.setTag(position);
        return vidsLayout;
    }
}

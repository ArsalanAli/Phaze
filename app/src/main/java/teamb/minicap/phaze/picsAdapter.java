package teamb.minicap.phaze;

import android.content.ContentUris;
import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Kareem on 2016-03-25.
 */
public class picsAdapter extends BaseAdapter{
    private ArrayList<pics> pictures;
    private LayoutInflater details;

    public picsAdapter(Context c, ArrayList<pics> thepics){
        pictures = thepics;
        details = LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return pictures.size();
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
        LinearLayout picsLayout = (LinearLayout)details.inflate
                (R.layout.pics, parent, false);
        TextView picsView = (TextView)picsLayout.findViewById(R.id.pics_title);
        pics current = pictures.get(position);
        picsView.setText(current.getTitle());
        picsLayout.setTag(position);
        return picsLayout;
    }
}

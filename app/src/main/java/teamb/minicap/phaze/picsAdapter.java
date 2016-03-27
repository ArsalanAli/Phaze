package teamb.minicap.phaze;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Kareem on 2016-03-25.
 */
public class picsAdapter extends BaseAdapter {

    private ArrayList<pics> pictures;
    private LayoutInflater details;

    public picsAdapter(Context c, ArrayList<pics> images) {
        pictures = images;
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
        LinearLayout picsLayout = (LinearLayout) details.inflate
                (R.layout.pics, parent, false);
        ImageView image = (ImageView) picsLayout.findViewById(R.id.images);
        pics current = pictures.get(position);
        image.setImageBitmap(current.getImage());
        picsLayout.setTag(position);
        return picsLayout;
    }
}


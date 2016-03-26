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
public class picsAdapter extends ArrayAdapter {

    private Context c;
    private int layoutResourceId;
    private ArrayList pictures = new ArrayList();

    public picsAdapter(Context c, int layoutResourceId, ArrayList data) {
        super(c, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.c = c;
        pictures = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) c).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView) row.findViewById(R.id.image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        pics item = pictures.get(position);
        holder.image.setImageBitmap(item.getImage());
        return row;
    }

    static class ViewHolder {
        ImageView image;
    }
}

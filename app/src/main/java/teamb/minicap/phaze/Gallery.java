package teamb.minicap.phaze;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

//Gallery

public class Gallery extends AppCompatActivity {
    private ArrayList<pics> picsList;
    private GridView picsView;
    private picsAdapter gridAdapt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        int request = 0;
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, request);
            }
        }
        picsView = (GridView) findViewById(R.id.gridView);
        gridAdapt = new picsAdapter(this, R.layout.activity_gallery, picsList);
        picsView.setAdapter(gridAdapt);

        retrieveMedia();

        Collections.sort(picsList, new Comparator<pics>() {
            public int compare(pics one, pics two) {
                return one.getTitle().compareTo(two.getTitle());
            }
        });

    }



    public void retrieveMedia() {
        ContentResolver pictureResolver = getContentResolver();
        Uri pictureUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor pictureCursor = pictureResolver.query(pictureUri, null, null, null, null);



        if (pictureCursor != null && pictureCursor.moveToFirst()) {

            int title = pictureCursor.getColumnIndex
                    (MediaStore.Images.Media.TITLE);
            int idColumn = pictureCursor.getColumnIndex
                    (MediaStore.Images.Media._ID);
            do {
                long thisId = pictureCursor.getLong(idColumn);
                String thisTitle = pictureCursor.getString(title);
                Bitmap bitmap = BitmapFactory.decodeFile(ContentUris.withAppendedId(pictureUri, thisId).toString()) ;
                picsList.add(new pics(thisId, thisTitle, bitmap));
            }
            while (pictureCursor.moveToNext());
        }
    }
    public void picChosen(View view){
        Intent intent = new Intent(this, PicView.class);
        intent.putExtra("pics", picsList);
        intent.putExtra("location", Integer.parseInt(view.getTag().toString()));
        startActivity(intent);
    }
}
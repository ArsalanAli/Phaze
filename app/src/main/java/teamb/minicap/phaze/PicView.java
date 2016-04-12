package teamb.minicap.phaze;

import android.content.ContentUris;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import java.util.ArrayList;

/**
 * Created by Kareem on 2016-03-25.
 */
public class PicView extends AppCompatActivity {

    private ArrayList<pics> picsList;
    private int currPic;
    private ImageView pictures;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_viewer);
        pictures = (ImageView) findViewById(R.id.imageView);
        if (getIntent().hasExtra("pics")) {
            picsList = (ArrayList<pics>) getIntent().getSerializableExtra("pics");
        }
        if (getIntent().hasExtra("location")) {
            currPic = getIntent().getIntExtra("location", 0);
        }

        displayPicture(picsList.get(currPic).getID());

    }

    public void displayPicture(long id) {
        Uri pictureUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                id);
        pictures.setImageURI(pictureUri);
    }

}


package com.lavish.indiscan;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ImageViewer extends AppCompatActivity {

    private Toolbar mToolbar;
    private byte[] img;
    private String img_number;
    private ImageView iiii;
    private SQLiteHelper mSQLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);
        Intent intent =getIntent();
        img_number =intent.getStringExtra("PIC_NUMBER");
        img=intent.getByteArrayExtra("PIC_BITMAP");


        mToolbar=findViewById(R.id.toolbar2);
        iiii=findViewById(R.id.viewer_img);
        mSQLiteHelper = new SQLiteHelper(this);

        mToolbar.setTitle(img_number);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        mToolbar.inflateMenu(R.menu.viewer_menu);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        iiii.setImageBitmap(getImage(img));

        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {

                 if(item.getItemId()== R.id.viewer_download)
                {
                    Toast.makeText(ImageViewer.this,"Download",Toast.LENGTH_SHORT).show();
                }
                else if(item.getItemId()== R.id.viewer_share){
                    Toast.makeText(ImageViewer.this,"Share",Toast.LENGTH_SHORT).show();
                }

                return false;
            }
        });
    }

    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);

    }
}

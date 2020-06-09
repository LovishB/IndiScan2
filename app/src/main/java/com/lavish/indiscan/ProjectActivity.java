package com.lavish.indiscan;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scanlibrary.ScanActivity;
import com.scanlibrary.ScanConstants;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ProjectActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ImageView mImageView;
    private ImageButton btn;
    int REQUEST_CODE = 99;

    private RecyclerView mRecyclerView;
    private AddPicAdapter mAddPicAdapter;
    private ArrayList<ModelAddPic> list;
    private String title, id;

    private SQLiteHelper mSQLiteHelper;
    private ArrayList<String> ids;
    private ArrayList<Integer> pic_nos;
    private ArrayList<byte[]> imgs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        Intent intent = getIntent();
        title = intent.getStringExtra("PROJECT_NAME");
        id = intent.getStringExtra("PROJECT_ID");

        mToolbar = findViewById(R.id.project_toolbar);
        btn = findViewById(R.id.project_btn);
        mImageView = findViewById(R.id.project_img);
        mRecyclerView = findViewById(R.id.project_container);
        list = new ArrayList<>();
        ids = new ArrayList<>();
        pic_nos = new ArrayList<>();
        imgs = new ArrayList<>();
        mSQLiteHelper = new SQLiteHelper(this);

        addimagestolist();


        mToolbar.setTitle(title);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        mToolbar.inflateMenu(R.menu.project_menu);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if(item.getItemId()==R.id.project_delete_btn)
                {
                   deleteproject();
                }
                else if(item.getItemId()== R.id.project_download)
                {
                    Toast.makeText(ProjectActivity.this,"Download",Toast.LENGTH_SHORT).show();
                }
                else if(item.getItemId()== R.id.project_share){
                    Toast.makeText(ProjectActivity.this,"Share",Toast.LENGTH_SHORT).show();
                }

                return false;
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });


    }

    public void openCamera() {
        int preference = ScanConstants.OPEN_CAMERA;
        Intent intent = new Intent(this, ScanActivity.class);
        intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getExtras().getParcelable(ScanConstants.SCANNED_RESULT);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                getContentResolver().delete(uri, null, null);
                addimagetolist(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addimagetolist(Bitmap bitmap) {
        addimagetodatabase(bitmap);
        list.clear();
        ids.clear();
        imgs.clear();
        pic_nos.clear();
        addimagestolist();
    }

    public void addimagestolist() {

        Cursor cursor = mSQLiteHelper.getImage(id);

        if(cursor!=null && cursor.getCount()>0 && cursor.moveToFirst()){
            do{
                pic_nos.add(cursor.getInt(cursor.getColumnIndex(mSQLiteHelper.Table_Primary_TWO)));
                imgs.add(cursor.getBlob(cursor.getColumnIndex(mSQLiteHelper.IMAGE)));
            }while (cursor.moveToNext());
            cursor.close();
        }

       for(int i=0;i<pic_nos.size();i++){
            list.add(new ModelAddPic(id,pic_nos.get(i)+ "",getImage(imgs.get(i))));
        }

        mAddPicAdapter = new AddPicAdapter(list, ProjectActivity.this);
        if (mAddPicAdapter.getItemCount() == 0) {
            mImageView.setVisibility(View.VISIBLE);
        } else {
            mImageView.setVisibility(View.INVISIBLE);
        }
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.setAdapter(mAddPicAdapter);

        mAddPicAdapter.setOnItmeClickListener(new AddPicAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                Intent intent = new Intent(ProjectActivity.this, ImageViewer.class);
                intent.putExtra("PIC_NUMBER", list.get(position).PicId);
                intent.putExtra("PIC_BITMAP",getBytes(list.get(position).mBitmap));
                startActivity(intent);
            }
        });

        mAddPicAdapter.setOnItemLongClickListener(new AddPicAdapter.OnItemLongClickListener() {
            @Override
            public boolean OnItemLongClick(int position) { ;
                return true;
            }
        });

    }

    public void addimagetodatabase(Bitmap bitmap) {
        byte[] img = getBytes(bitmap);
        mSQLiteHelper.addPic(id, img);
    }

    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);

    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    public void deleteproject(){
        AlertDialog.Builder builder2 = new AlertDialog.Builder(ProjectActivity.this);
        builder2.setTitle("Delete project");
        builder2.setCancelable(false);
        builder2.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mSQLiteHelper.deleteProject(id);
                finish();
            }
        });

        builder2.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog dialog2 = builder2.create();
        dialog2.show();
    }
}

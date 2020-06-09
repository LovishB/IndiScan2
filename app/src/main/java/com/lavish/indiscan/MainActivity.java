package com.lavish.indiscan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ImageButton btn;
    private ImageView mImageView;

    private RecyclerView mRecyclerView;
    private AddProjectAdapter mAddProjectAdapter;
    private ArrayList<ModelAddProject> list;

    private SQLiteHelper mSQLiteHelper;
    private Cursor cursor;

    private ArrayList<String> ID_Array;
    private ArrayList<String> Name_Array;
    private ArrayList<String> Date_Array;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = findViewById(R.id.toolbar);
        btn=findViewById(R.id.main_btn);
        mImageView=findViewById(R.id.main_picback);
        mRecyclerView=findViewById(R.id.main_container);
        list=new ArrayList<>();
        ID_Array = new ArrayList<>();
        Name_Array = new ArrayList<>();
        Date_Array = new ArrayList<>();
        mSQLiteHelper = new SQLiteHelper(this);

        addingprojectstolist();

        mToolbar.setTitle("Projects");
        // mToolbar.setTitleTextColor(getResources().getColor(R.color.));
        setSupportActionBar(mToolbar);
        new SlidingRootNavBuilder(MainActivity.this)
                .withToolbarMenuToggle(mToolbar)
                .withMenuOpened(false)
                .withMenuLayout(R.layout.drawer)
                .withDragDistance(130)
                .inject();


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
                SimpleDateFormat ddf = new SimpleDateFormat("dd-MM-yyyy");
                final String formattedDate = "Project-"+df.format(c.getTime());
                final String date=ddf.format(c.getTime());

                LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
                View promptView = layoutInflater.inflate(R.layout.change_name, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setView(promptView);
                final EditText input =  promptView.findViewById(R.id.userInput);
                input.setText(formattedDate);
                alertDialogBuilder.setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                addprojecttodatabase(formattedDate,input.getText()+"",date);
                                Intent intent = new Intent(MainActivity.this, ProjectActivity.class);
                                intent.putExtra("PROJECT_ID", formattedDate);
                                intent.putExtra("PROJECT_NAME",input.getText()+"");
                                startActivity(intent);
                            }
                        })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alertD = alertDialogBuilder.create();
                alertD.show();


            }
        });



    }
    public void addprojecttodatabase(String id,String name,String date){
        boolean result = mSQLiteHelper.addData(id,name,date);
    }

    public void addingprojectstolist(){
        readdatabse();
        mAddProjectAdapter=new AddProjectAdapter(list,MainActivity.this);
        if(mAddProjectAdapter.getItemCount()==0){
            mImageView.setVisibility(View.VISIBLE);
        }else{
            mImageView.setVisibility(View.INVISIBLE);
        }
        mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        mRecyclerView.setAdapter(mAddProjectAdapter);

        mAddProjectAdapter.setOnItmeClickListener(new AddProjectAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                Intent intent = new Intent(MainActivity.this, ProjectActivity.class);
                intent.putExtra("PROJECT_ID", list.get(position).project_id);
                intent.putExtra("PROJECT_NAME",list.get(position).project_name);
                startActivity(intent);
            }
        });
    }

    public void readdatabse(){

        list.clear();
        Date_Array.clear();
        Name_Array.clear();
        ID_Array.clear();

        cursor=mSQLiteHelper.getProjects();

        while (cursor.moveToNext()){
            ID_Array.add(cursor.getString(cursor.getColumnIndex(mSQLiteHelper.Table_Column_ID)));
            Name_Array.add(cursor.getString(cursor.getColumnIndex(mSQLiteHelper.Table_Column_Name)));
            Date_Array.add(cursor.getString(cursor.getColumnIndex(mSQLiteHelper.Table_Column_Date)));
        }

        for(int i=Name_Array.size()-1;i>=0;i--){
            list.add(new ModelAddProject(ID_Array.get(i),Name_Array.get(i),Date_Array.get(i)));
        }
        cursor.close();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        addingprojectstolist();
    }
}

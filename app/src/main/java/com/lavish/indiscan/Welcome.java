package com.lavish.indiscan;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Welcome extends AppCompatActivity {

    private ImageView welcomepic,mobile1,mobile2,mobile3,text1,text2,perms,terms,box1,box2;
    private Animation welcomeone,welcometwo,welcomethree;
    private ImageButton welcomebtn1,welcomebtn2;
    private int flag1,flag2;
    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        welcomepic=findViewById(R.id.welcomepic);
        mobile1=findViewById(R.id.mobileone);
        mobile2=findViewById(R.id.mobiletwo);
        mobile3=findViewById(R.id.mobilethree);
        text1=findViewById(R.id.welcometext1);
        welcomebtn1=findViewById(R.id.welcomebtnone);
        welcomebtn2=findViewById(R.id.welcomebtntwo);
        text2=findViewById(R.id.welcometext2);
        box1=findViewById(R.id.boxone);
        box2=findViewById(R.id.box2);
        perms=findViewById(R.id.permtext);
        terms=findViewById(R.id.termstext);
        flag1=0;
        flag2=0;

        welcomeone= AnimationUtils.loadAnimation(this,R.anim.welcomeone);
        welcometwo= AnimationUtils.loadAnimation(this,R.anim.welcometwo);
        welcomethree= AnimationUtils.loadAnimation(this,R.anim.welcomethree);


        welcomepic.startAnimation(welcomeone);
        mobile1.startAnimation(welcometwo);
        mobile2.startAnimation(welcometwo);
        mobile3.startAnimation(welcometwo);
        text1.startAnimation(welcometwo);
        welcomebtn1.startAnimation(welcomethree);



        welcomebtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                welcomebtn1.setVisibility(View.INVISIBLE);
                welcomebtn2.setVisibility(View.VISIBLE);
                text1.setVisibility(View.INVISIBLE);
                text2.setVisibility(View.VISIBLE);
                mobile1.setVisibility(View.INVISIBLE);
                mobile2.setVisibility(View.INVISIBLE);
                mobile3.setVisibility(View.INVISIBLE);
                text2.setVisibility(View.VISIBLE);
                box1.setVisibility(View.VISIBLE);
                box2.setVisibility(View.VISIBLE);
                perms.setVisibility(View.VISIBLE);
                terms.setVisibility(View.VISIBLE);
                text2.startAnimation(welcometwo);
                box2.startAnimation(welcometwo);
                box1.startAnimation(welcometwo);
                perms.startAnimation(welcometwo);
                terms.startAnimation(welcometwo);
            }
        });

        box1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(flag1==1){
                    flag1=0;
                    box1.setImageResource(R.drawable.uncheck);
                }else{
                    checkPermissionWrite(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            STORAGE_PERMISSION_CODE);
                }

            }
        });

        box2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag2==0) {
                    box2.setImageResource(R.drawable.check);
                    flag2=1;
                }else {
                    box2.setImageResource(R.drawable.uncheck);
                    flag2=0;
                }
            }
        });

        welcomebtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag2==0 || flag1==0){
                    Toast.makeText(Welcome.this,"Check all the fields to continue",Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent=new Intent(Welcome.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    public void checkPermissionWrite(String permission, int requestCode){
        if (ContextCompat.checkSelfPermission(Welcome.this, permission)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(Welcome.this,
                    new String[] { permission },
                    requestCode);

        }else{
            checkPermissionCamera(Manifest.permission.CAMERA,
                    CAMERA_PERMISSION_CODE);
        }

        }

    public void checkPermissionCamera(String permission, int requestCode){
        if (ContextCompat.checkSelfPermission(Welcome.this, permission)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(Welcome.this,
                    new String[] { permission },
                    requestCode);

        }else{
                box1.setImageResource(R.drawable.check);
                flag1=1;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super
                .onRequestPermissionsResult(requestCode,
                        permissions,
                        grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                box1.setImageResource(R.drawable.check);
                flag1=1;
            } else {

        }
         }else if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkPermissionCamera(Manifest.permission.CAMERA,
                        CAMERA_PERMISSION_CODE);
            }
            else {

            }
            }
        }

    }

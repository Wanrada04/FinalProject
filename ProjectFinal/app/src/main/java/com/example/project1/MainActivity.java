package com.example.project1;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private CardView btnCheck, btnActivity, btnResult, btnLogout, btnExport, btnSetting;
    SessionManager ses;
    String name, username;
    TextView txtName, txtUsername;
    SharedPreferences prf;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCheck = (CardView) findViewById(R.id.CVCheck);
        btnActivity = (CardView) findViewById(R.id.CVActivity);
        btnResult = (CardView) findViewById(R.id.CVResult);
        btnLogout = (CardView) findViewById(R.id.CVLogout);
        btnExport = (CardView) findViewById(R.id.CVExport);
        btnSetting = (CardView) findViewById(R.id.CVSet);

        txtName = (TextView) findViewById(R.id.textName);
//        txtUsername = (TextView) findViewById(R.id.textUsername);


        // SharedPreferences sp=getSharedPreferences("LogInfo", Context.MODE_PRIVATE);
        // SharedPreferences sharedpreferences = getSharedPreferences(LogActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        // SharedPreferences.Editor editor = sharedpreferences.edit();


        Intent intent = getIntent();
//        String TempNameFull = getIntent().getStringExtra("FullName");
//        String TempUsername = getIntent().getStringExtra("UserID");

//----------------Set Text from session-------
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        final String Fullname = user.get(SessionManager.KEY_NAME);
//        final String UserID = user.get(SessionManager.KEY_ID);
        txtName.setText(Fullname);
//        txtUsername.setText(UserID);
//-------------------------------------------------

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CheckActivity.class);
                startActivity(intent);
            }
        });
        btnActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ActivityActivity.class);
                startActivity(intent);
            }
        });

        btnResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                startActivity(intent);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this).
                        setMessage("คุณต้องการออกจากระบบใช่หรือไม่ ?")
                        .setPositiveButton("ใช่", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }

                        })
                        .setNegativeButton("ไม่ใช่", null).show();
//                Intent intent = new Intent(MainActivity.this, LogActivity.class);
//                startActivity(intent);
//                finish();

            }
        });

        btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ExportActivity.class);
                startActivity(intent);

            }
        });
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Setting1Activity.class);
                startActivity(intent);

            }
        });


    }

    public void onBackPressed() {
        new AlertDialog.Builder(MainActivity.this).
                setMessage("คุณต้องการออกจากระบบใช่หรือไม่ ?")
                .setPositiveButton("ใช่", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("ไม่ใช่", null).show();

    }

}

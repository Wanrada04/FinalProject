package com.example.project1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.HashMap;

public class STMainActivity extends AppCompatActivity {
    private CardView btnSub,btnActivity,btnResult,btnLogout,btnScan;
    TextView txtName,txtUsername;
    public static final int REQUEST_QR_SCAN = 4;
    public static   String FullName,UserID;
    SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stmain);

        btnSub = (CardView)findViewById(R.id.CVCStSub);
        btnActivity = (CardView)findViewById(R.id.CVActivitySt);
        btnResult =(CardView)findViewById(R.id.CVResultSt);
        btnLogout =(CardView)findViewById(R.id.CVLogoutSt);
        btnScan =(CardView)findViewById(R.id.CVCScan);

        txtName = (TextView) findViewById(R.id.textUserName);
        txtUsername = (TextView) findViewById(R.id.textNameID);

        Intent intent = getIntent();
//        FullName = getIntent().getStringExtra("FullName");
//        UserID = getIntent().getStringExtra("UserID");
//
//        txtName.setText(FullName);
//        txtUsername.setText(UserID);

        //----------------Set Text from session-------
        session = new SessionManager(getApplicationContext());
        HashMap<String,String> user = session.getUserDetails();
        final String Fullname = user.get(SessionManager.KEY_NAME);
        final String UserID = user.get(SessionManager.KEY_ID);
        txtName.setText(Fullname);
        txtUsername.setText(UserID);

        btnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(STMainActivity.this, SubStActivity.class);
                startActivity(intent);
            }
        });
        btnActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(STMainActivity.this, AcStActivity.class);
                startActivity(intent);
            }
        });

        btnResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(STMainActivity.this, ResultStActivity.class);
                startActivity(intent);
            }
        });


        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(STMainActivity.this).
                        setMessage("คุณต้องการออกจากระบบใช่หรือไม่ ?")
                        .setPositiveButton("ใช่", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }

                        })
                        .setNegativeButton("ไม่ใช่", null).show();
//                Intent intent = new Intent(STMainActivity.this, LogActivity.class);
//                startActivity(intent);
//                finish();
            }
        });

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intents = new Intent(STMainActivity.this,ScanStActivity.class);
                intents.putExtra("FullName",FullName);
                intents.putExtra("UserID",UserID);
                startActivity(intents);

//                IntentIntegrator integrator = new IntentIntegrator(STMainActivity.this);
//                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
//                integrator.setPrompt("Scan");
//                integrator.setCameraId(0);
//                integrator.setBeepEnabled(false);
//                integrator.setBarcodeImageEnabled(false);
//                integrator.initiateScan();

            }
        });



    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        super.onActivityResult(requestCode, resultCode, intent);
//        if (requestCode == REQUEST_QR_SCAN && resultCode == RESULT_OK) {
//            String contents = intent.getStringExtra("SCAN_RESULT");
//        }
//    }
public void onBackPressed() {
    new AlertDialog.Builder(STMainActivity.this).
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

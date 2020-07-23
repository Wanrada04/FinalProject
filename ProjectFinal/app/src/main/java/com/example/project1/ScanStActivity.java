package com.example.project1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.Result;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class ScanStActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    private String TAG = LogActivity.class.getSimpleName();
    String ServerURL = "http://"+IPConnect.IP_CONNECT+"/android/insert_result.php";
    String ServerURLAC  = "http://"+IPConnect.IP_CONNECT+"/android/insert_result_activity.php";
    String TempID;
    String Temp;
    String ActivityID;
    String[] Courselist;
    int Status = 0;
    public static   String FullName,UserID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);

        Intent intent = getIntent();
          FullName = getIntent().getStringExtra("FullName");
          UserID = getIntent().getStringExtra("UserID");
    }
  @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
      final String   CourseID,TypeActivity,InstructorID;
        Log.v("TAG", rawResult.getText()); // Prints scan results
        // Prints the scan format (qrcode, pdf417 etc.)
        Log.v("TAG", rawResult.getBarcodeFormat().toString());
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Scan Result");

      final String ScanValue = rawResult.getText();

         Courselist = ScanValue.split("-");
          TypeActivity=Courselist[3];
          String  CREATE_DATE=Courselist[2];

      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
      Calendar cal = Calendar.getInstance();
      Date CurrentDate =cal.getTime();
      Date DateCreate = new Date();
      try {
            DateCreate = dateFormat.parse(CREATE_DATE);


         // long minutes = different / 1;

      }
      catch (Exception  e) {
          Log.e(TAG, "Json parsing error" + e.getMessage());
          e.printStackTrace();
      }
      long different =   CurrentDate.getTime()-DateCreate.getTime();
      int i=(int)different;

       double diff= ((double) i/(60*1000));

//      AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage(diff);
//        AlertDialog alert1 = builder.create();
//        alert1.show();

        // If you would like to resume scanning, call this method below:
        mScannerView.resumeCameraPreview(this);
if(diff <= IPConnect.Barcode_Expire )
{
    if(TypeActivity.equals("01")) {
        CourseID=Courselist[0];
        InstructorID = Courselist[4];
        GetData();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("คุณต้องการเช็คชื่อใช่หรือไม่ ?");
        builder.setPositiveButton("ไม่", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mScannerView.resumeCameraPreview(ScanStActivity.this);
            }
        });
        builder.setNeutralButton("ใช่", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                GetData();
                InsertData(UserID,CourseID,ScanValue,InstructorID,Status);
            }
        });
        AlertDialog alert1 = builder.create();
        alert1.show();

//        InsertData(UserID,CourseID,ScanValue);

    }else if (TypeActivity.equals("02")){
        ActivityID=Courselist[0];
//        GetDataActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("คุณต้องการเช็คชื่อใช่หรือไม่ ?");
        builder.setPositiveButton("ไม่", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mScannerView.resumeCameraPreview(ScanStActivity.this);
            }
        });
        builder.setNeutralButton("ใช่", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    checkAc(ActivityID,UserID);
                    GetDataActivity();
                    InsertDataActivity(UserID, ActivityID, ScanValue);

            }
        });
        AlertDialog alert1 = builder.create();
        alert1.show();

//        InsertDataActivity(UserID,ActivityID,ScanValue );
    }
}
else{
          AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage("หมดเวลาการเช็คชื่อ");
    AlertDialog alert1 = builder.create();
    alert1.show();
    }


}



    @Override
    public void onResume() {
        super.onResume();
        // Register ourselves as a handler for scan results.
        mScannerView.setResultHandler(this);
        // Start camera on resume
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        // Stop camera on pause
        mScannerView.stopCamera();
    }

    private void checkAc(final String ActivityID, final String UserID){
//        if (.equals("false"){
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setMessage("มีรายชื่อนี้อยู่แล้ว");
//            AlertDialog alert1 = builder.create();
//            alert1.show();
//        }
    }



    private void GetDataActivity() {
        }

    public void InsertDataActivity( final String UserID,  final String ActivityID,final String ScanValue )

    {

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {




                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("ActivityID", ActivityID));
                nameValuePairs.add(new BasicNameValuePair("StudentID", UserID));
//                nameValuePairs.add(new BasicNameValuePair("QRcode", ScanValue));
//                nameValuePairs.add(new BasicNameValuePair("UserID", UserID));



                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(ServerURLAC);

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));

                    HttpResponse httpResponse = httpClient.execute(httpPost);

//                    HttpEntity httpEntity = httpResponse.getEntity();
                    InputStream inputStream = httpResponse.getEntity().getContent();

                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    StringBuilder stringBuilder = new StringBuilder();

                    String bufferedStrChunk = null;

                    while((bufferedStrChunk = bufferedReader.readLine()) != null){
                        stringBuilder.append(bufferedStrChunk);
                    }

                    return stringBuilder.toString();


                } catch (ClientProtocolException e) {
                    return null;

                } catch (IOException e) {
                    return null;

                }


            }

            @Override
            protected void onPostExecute(String result) {

                super.onPostExecute(result);
                if (result.equals("dup"))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ScanStActivity.this)
                            .setIcon(android.R.drawable.ic_delete)
                            .setTitle("มีรายชื่อนี้อยู่ในรายการแล้ว")
                            .setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(ScanStActivity.this, STMainActivity.class);
                            startActivity(intent);
                            dialog.dismiss();
                        }
                    });
                     AlertDialog alert1 = builder.create();
                     alert1.show();
//                    Toast.makeText(ScanStActivity.this, "มีรายชื่อนี้อยู่ในรายการแล้ว", Toast.LENGTH_LONG).show();
//                    Intent intent = new Intent(ScanStActivity.this, STMainActivity.class);
////
//                    startActivity(intent);

                }
                else if (result.equals("over"))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ScanStActivity.this)
                            .setIcon(android.R.drawable.ic_delete)
                            .setTitle("จำนวนผู้เข้าร่วมเต็มแล้ว")
                            .setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(ScanStActivity.this, STMainActivity.class);
                            startActivity(intent);
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alert1 = builder.create();
                    alert1.show();
//                    Toast.makeText(ScanStActivity.this, "จำนวนผู้เข้าร่วมเต็มแล้ว", Toast.LENGTH_LONG).show();
//                    Intent intent = new Intent(ScanStActivity.this, STMainActivity.class);
////
//                    startActivity(intent);

                }
                else
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ScanStActivity.this)
                                .setIcon(android.R.drawable.checkbox_on_background)
                                .setTitle("เช็คชื่อสำเร็จ")
                                .setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(ScanStActivity.this, STMainActivity.class);
                                startActivity(intent);
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alert1 = builder.create();
                        alert1.show();
////
//                    Toast.makeText(ScanStActivity.this, "เช็คชื่อสำเร็จ", Toast.LENGTH_LONG).show();
//                    Intent intent = new Intent(ScanStActivity.this, STMainActivity.class);
////                intent.putExtra("FullName",FullName);
////                intent.putExtra("UserID",UserID);
//                    startActivity(intent);
                }



            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();

        sendPostReqAsyncTask.execute(ActivityID );
    }




       private void GetData() {


    }


    public void InsertData( final String UserID,  final String CourseID,final String ScanValue ,final  String InstructorID ,final  int Status)

    {

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {




                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

//                nameValuePairs.add(new BasicNameValuePair("Type", TypeHolder));
                nameValuePairs.add(new BasicNameValuePair("CourseID", CourseID));
                nameValuePairs.add(new BasicNameValuePair("StudentID", UserID));
                nameValuePairs.add(new BasicNameValuePair("InstructorID", InstructorID));
                nameValuePairs.add(new BasicNameValuePair("Status",  Integer.toString(Status)));




                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(ServerURL);

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));

                    HttpResponse httpResponse = httpClient.execute(httpPost);

                    InputStream inputStream = httpResponse.getEntity().getContent();

                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    StringBuilder stringBuilder = new StringBuilder();

                    String bufferedStrChunk = null;

                    while((bufferedStrChunk = bufferedReader.readLine()) != null){
                        stringBuilder.append(bufferedStrChunk);
                    }

                    return stringBuilder.toString();


                } catch (ClientProtocolException e) {
                    return null;

                } catch (IOException e) {
                    return null;

                }
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if (result.equals("true")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ScanStActivity.this)
                            .setIcon(android.R.drawable.ic_delete).setTitle("นักศึกษาไม่ได้ลงทะเบียนเรียนวิชานี้")
                            .setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(ScanStActivity.this, STMainActivity.class);
                                    startActivity(intent);
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog alert1 = builder.create();
                    alert1.show();

                }else {
                    if (result.equals("dup")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ScanStActivity.this)
                                .setIcon(android.R.drawable.ic_delete).setTitle("มีรายชื่อนี้อยู่ในรายการแล้ว")
                                .setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(ScanStActivity.this, STMainActivity.class);
                                        startActivity(intent);
                                        dialog.dismiss();
                                    }
                                });
                        AlertDialog alert1 = builder.create();
                        alert1.show();
//                    Toast.makeText(ScanStActivity.this, "มีรายชื่อนี้อยู่ในรายการแล้ว", Toast.LENGTH_LONG).show();
//                    Intent intent = new Intent(ScanStActivity.this, STMainActivity.class);
////
//                    startActivity(intent);
                    } else {

                        AlertDialog.Builder builder = new AlertDialog.Builder(ScanStActivity.this)
                                .setIcon(android.R.drawable.checkbox_on_background)
                                .setTitle("เช็คชื่อสำเร็จ")
                                .setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(ScanStActivity.this, STMainActivity.class);
                                        startActivity(intent);
                                        dialog.dismiss();
                                    }
                                });
                        AlertDialog alert1 = builder.create();
                        alert1.show();

//                    Toast.makeText(ScanStActivity.this, "เช็คชื่อสำเร็จ", Toast.LENGTH_LONG).show();
//                    Intent intent = new Intent(ScanStActivity.this, STMainActivity.class);
////                intent.putExtra("FullName",FullName);
////                intent.putExtra("UserID",UserID);
//                    startActivity(intent);
                    }
                }
            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();

        sendPostReqAsyncTask.execute(CourseID);
    }
}

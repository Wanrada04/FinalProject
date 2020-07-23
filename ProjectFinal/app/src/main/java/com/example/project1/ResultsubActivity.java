package com.example.project1;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


public class ResultsubActivity extends AppCompatActivity {
    private String TAG = ResultsubActivity.class.getSimpleName();
    private ListView listView;
    ArrayList<HashMap<String, String>> contactList;
    SessionManager session;
    private static String ID;
    TextView Countst,date,time;
    public static int deletePos;
    private Context context;
    private String[] values;
    private List<Boolean> checked;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    Button selectDate,selectTime;
    int year;
    int month;
    int dayOfMonth;
    int hour, minute, seconds;
    Calendar calendar;
    String TempDate,TempTime;
    String URL = "http://"+IPConnect.IP_CONNECT+"/android/count_resultsub.php";
    String ServerURL = "http://"+IPConnect.IP_CONNECT+"/android/update_status.php";
    String URLUPDATE = "http://"+IPConnect.IP_CONNECT+"/android/update_statusall.php";



    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultsub);
        contactList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.list_result_sub);
        Countst = (TextView) findViewById(R.id.countst);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        ID = user.get(SessionManager.KEY_ID);
        String TempHolder = getIntent().getStringExtra("CourseID");
        String TempSec = getIntent().getStringExtra("Section");

        new GetPlaces().execute();
        InsertData(ID,TempHolder,TempSec);
//        new retrievedata().execute();


    }

    private void InsertData(final String ID,final String TempHolder,final String TempSec) {

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {


                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("InstructorID", ID));
                nameValuePairs.add(new BasicNameValuePair("CourseID", TempHolder));
                nameValuePairs.add(new BasicNameValuePair("Section", TempSec));


                 try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(URL);

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
                Countst = (TextView) findViewById(R.id.countst);
                Countst.setText(result);

            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(ID,TempHolder,TempSec );

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1, menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                new AlertDialog.Builder(ResultsubActivity.this).
                            setIcon(android.R.drawable.ic_delete)
                            .setTitle("คุณแน่ใจ...")
                            .setMessage("คุณต้องการลบรายการทั้งหมดนี้หรือไม่ ?")
                            .setPositiveButton("ใช่", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String CourseID =  getIntent().getStringExtra("CourseID");
                                    String Section = getIntent().getStringExtra("Section");
                                    session = new SessionManager(getApplicationContext());
                                    HashMap<String, String> user = session.getUserDetails();
                                    ID = user.get(SessionManager.KEY_ID);
//                                    listView.removeView(selected_item);
//                                    listViewAdapter.removeSelectedPersons();
//                                    contactList.remove(position);
//                                    adapter.notifyDataSetChanged();
                                    UpdateStatus(CourseID,ID,Section);

                                }
                            })
                            .setNegativeButton("ไม่ใช่", null).show();
                return true;
            case R.id.CheckSt:
                Intent intent = new Intent(ResultsubActivity.this, ResultCheckstActivity.class);
                String CourseID =  getIntent().getStringExtra("CourseID");
                String Section =  getIntent().getStringExtra("Section");
                    intent.putExtra("CourseID",CourseID);
                    intent.putExtra("Section",Section);
                startActivity(intent);
                return true;
//
//            case  R.id.Times:
//                PopupDialog();



//                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

//    private void PopupDialog() {
//
//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
//        View view = this.getLayoutInflater().inflate(R.layout.switch_on, null);
//        dialogBuilder.setView(view);
////        dialogBuilder.setView(getLayoutInflater().inflate(R.layout.switch_on, null));
//        dialogBuilder.setTitle("ตั้งค่าวันเวลา");
//        date = view.findViewById(R.id.textViewDay);
//        selectDate = view.findViewById(R.id.btnDate);
//            selectDate.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    calendar = Calendar.getInstance();
//                                                       year = calendar.get(Calendar.YEAR);
//                                                       month = calendar.get(Calendar.MONTH);
//                                                       dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
//                                                       datePickerDialog = new DatePickerDialog(ResultsubActivity.this,
//                                                               new DatePickerDialog.OnDateSetListener() {
//                                                                   @Override
//                                                                   public void onDateSet(DatePicker datePicker, int year, int month, int day) {
//                                                                       date.setText(year + "-" + (month + 1) + "-" + day);
//                                                                   }
//                                                               }, year, month, dayOfMonth);
//                                                       datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
//                                                       datePickerDialog.show();
//                }
//            });
//                selectTime = view.findViewById(R.id.btnTimes);
//                time = view.findViewById(R.id.textViewTimes);
//                        selectTime.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        calendar = Calendar.getInstance();
//                        hour = calendar.get(Calendar.HOUR_OF_DAY);
//                        minute = calendar.get(Calendar.MINUTE);
//                        timePickerDialog = new TimePickerDialog(ResultsubActivity.this,
//                                new TimePickerDialog.OnTimeSetListener() {
//
//                                    @Override
//                                    public void onTimeSet(TimePicker view, int hourOfDay,
//                                                          int minute) {
//
//                                        time.setText(hourOfDay + ":" + minute);
//
//
//                                    }
//                                }, hour, minute, false);
//                        timePickerDialog.show();
//                    }
//
//                });
//                dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        String currentDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault()).format(new Date());
//                        TempDate = date.getText().toString();
//                        TempTime = time.getText().toString();
//                    }
//                });
//        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
////                dialog.dismiss();
//            }
//        });
////        dialogBuilder.setPositiveButton("Clear", new DialogInterface.OnClickListener() {
////            @Override
////            public void onClick(DialogInterface dialog, int which) {
////                date.setText("");
////                time.setText("");
////
////            }
////        });
//        dialogBuilder.create().show();
//
//    }

    private void UpdateStatus( final String CourseID, final String ID, final String Section) {
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {


                String TempCourse = CourseID;
                String TempID = ID;
                String TempStatus = Section;


                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("InstructorID", TempID));
                nameValuePairs.add(new BasicNameValuePair("CourseID", TempCourse));
                nameValuePairs.add(new BasicNameValuePair("Section",TempStatus));
//                nameValuePairs.add(new BasicNameValuePair("Status", Integer.toString(status)));

                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(URLUPDATE);

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

                    HttpResponse httpResponse = httpClient.execute(httpPost);

                    HttpEntity httpEntity = httpResponse.getEntity();


                } catch (ClientProtocolException e) {

                } catch (IOException e) {

                }

                return "Data Inserted Successfully";
            }


            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                Toast.makeText(ResultsubActivity.this, "เรียบร้อย", Toast.LENGTH_LONG).show();
                Intent intent = getIntent();
                finish();
                startActivity(intent);


            }

        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();

        sendPostReqAsyncTask.execute();
    }




//
//    private class retrievedata extends AsyncTask<String, String, String> {
//
//        @SuppressLint("WrongThread")
//        @Override
//        protected String doInBackground(String... params) {
//            HttpHandler sh = new HttpHandler();
//            String url1 = "http://" + IPConnect.IP_CONNECT + "/android/count_resultsub.php";
//            session = new SessionManager(getApplicationContext());
//            HashMap<String, String> user = session.getUserDetails();
//            ID = user.get(SessionManager.KEY_ID);
//
//            String TempHolder = getIntent().getStringExtra("CourseID");
//            String TempSec = getIntent().getStringExtra("Section");
//
//
//            url1 += "?InstructorID=" + ID + "&CourseID=" + TempHolder + "&Section=" + TempSec;
//
//            String jsonStr = sh.makeServiceCall(url1);
//            Log.e(TAG, "Response from url:" + jsonStr);
//            if (jsonStr != null) {
//                try {
//                    JSONObject jsonObj = new JSONObject(jsonStr);
//                    JSONArray result = jsonObj.getJSONArray("result");
//                    for (int i = 0; i < result.length(); i++) {
//                        JSONObject c = result.getJSONObject(i);
//                        String countst = c.getString("countst");
//                        Countst.setText(countst);
//                    }
//                } catch (final JSONException e) {
//                    Log.e(TAG, "Json parsing error" + e.getMessage());
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(getApplicationContext(),
//                                    "json from server" + e.getMessage(), Toast.LENGTH_LONG).show();
//                        }
//                    });
//
//                }
//            } else {
//                Log.e(TAG, "from server");
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(getApplicationContext(),
//                                "Couldn't get json from server", Toast.LENGTH_LONG).show();
//
//                    }
//                });
//            }
//            return null;
//        }
//
//    }



    private class  GetPlaces extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(ResultsubActivity.this, "ผลการเข้าเรียนรายวิชา", Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();

            String url = "http://" + IPConnect.IP_CONNECT + "/android/get_result_sub.php";
            session = new SessionManager(getApplicationContext());
            HashMap<String, String> user = session.getUserDetails();
            ID = user.get(SessionManager.KEY_ID);

            String TempHolder = getIntent().getStringExtra("CourseID");
            String TempSec = getIntent().getStringExtra("Section");


            url += "?InstructorID=" + ID + "&CourseID=" + TempHolder + "&Section=" + TempSec;


            String jsonStr = sh.makeServiceCall(url);
            Log.e(TAG, "Response from url:" + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray result = jsonObj.getJSONArray("result");
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject c = result.getJSONObject(i);
                        String CourseID = c.getString("CourseID");
                        String StudentName = c.getString("StudentName");
                        String CourseName = c.getString("CourseName");
                        String StudentID = c.getString("StudentID");
                        String InstructorID = c.getString("InstructorID");

                        String CreateDate = c.getString("CreateDate");
                        HashMap<String, String> contact = new HashMap<>();
                        contact.put("CourseID", CourseID);
                        contact.put("CourseName", CourseName);
                        contact.put("StudentID", StudentID);
                        contact.put("StudentName", StudentName);
                        contact.put("InstructorID", InstructorID);
                        contact.put("CreateDate", CreateDate);
                        //contact.put("color","#A9290E" );
//                        contact.put("color", String.valueOf(R.color.colorP));
                        contactList.add(contact);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error" + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "json from server" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "from server");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server", Toast.LENGTH_LONG).show();

                    }
                });
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            final SimpleAdapter adapter = new SimpleAdapter(ResultsubActivity.this, contactList,
                    R.layout.list_result_sub, new String[]{"CourseID", "CourseName", "StudentID", "StudentName", "CreateDate"}, new int[]{
                    R.id.textID, R.id.textTitle, R.id.textStID, R.id.textSTName, R.id.textCreate});
            listView.setAdapter(adapter);



//
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {

//                    final CheckBox cbx = (CheckBox)findViewById(R.id.chk_selected);
//                    cbx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                        @Override
//                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                            checked.set(position,);
//                        }
//                    });

                    new AlertDialog.Builder(ResultsubActivity.this).
                            setIcon(android.R.drawable.ic_delete)
                            .setTitle("คุณแน่ใจ...")
                            .setMessage("คุณต้องการลบรายการนี้หรือไม่ ?")
                            .setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                                private int Status = 1;
                                String StudentID = contactList.get(position).get("StudentID");
                                String CourseID = contactList.get(position).get("CourseID");
                                String InstructorID = contactList.get(position).get("InstructorID");

                                @Override
                                public void onClick(DialogInterface dialog, int which) {

//                                    listView.removeView(selected_item);
//                                    listViewAdapter.removeSelectedPersons();
//                                    contactList.remove(position);
//                                    adapter.notifyDataSetChanged();
                                    Update(position, CourseID, StudentID, InstructorID, Status);

                                }
                            })
                            .setNegativeButton("ไม่ใช่", null).show();

                    return;
                }
            });

        }
    }


    private void Update(int position, final String CourseID, final String StudentID,final String InstructorID, final int Status) {

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                String TempCourse = CourseID;
                String TempID = StudentID;
                String TempTID = InstructorID;
                int TempStatus = Status;

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();


                nameValuePairs.add(new BasicNameValuePair("CourseID", TempCourse));
                nameValuePairs.add(new BasicNameValuePair("StudentID", TempID));
                nameValuePairs.add(new BasicNameValuePair("InstructorID", TempTID));
                nameValuePairs.add(new BasicNameValuePair("Status", Integer.toString(TempStatus)));

                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(ServerURL);

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

                    HttpResponse httpResponse = httpClient.execute(httpPost);

                    HttpEntity httpEntity = httpResponse.getEntity();


                } catch (ClientProtocolException e) {

                } catch (IOException e) {

                }

                return "Data Inserted Successfully";
            }


            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                Toast.makeText(ResultsubActivity.this, "เรียบร้อย", Toast.LENGTH_LONG).show();
                Intent intent = getIntent();
                finish();
                startActivity(intent);


            }

        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();

        sendPostReqAsyncTask.execute(CourseID ,StudentID);
    }




}


package com.example.project1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActivityActivity extends AppCompatActivity {
    private String TAG = ActivityActivity.class.getSimpleName();
    private ListView listView;
    ArrayList<HashMap<String, String>> contactList;
    Button btnEdit, btnView, btnQR;
    private ImageView imageView;
    Bitmap bitmap;
    SessionManager session;
    private static String ID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity);
        //imageView =(ImageView)findViewById(R.id.movieImg);
        contactList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.list_Ac);

//        btnView = (Button) findViewById(R.id.buttonView);
//        btnEdit = (Button) findViewById(R.id.buttonEdit);
//        btnQR = (Button) findViewById(R.id.buttonQR);

        new GetPlaces().execute();

    }




    public void clickEdit(View view) {

        final int i = listView.getPositionForView((View) view.getParent());
        String ActivityID = contactList.get(i).get("ActivityID");
        String ActivityTitle = contactList.get(i).get("ActivityTitle");
        String DateTo = contactList.get(i).get("DateTo");
        String DateFrom = contactList.get(i).get("DateFrom");
        String TimeFrom = contactList.get(i).get("TimeFrom");
        String TimeTo = contactList.get(i).get("TimeTo");
        String Number = contactList.get(i).get("Number");
        String HourAC = contactList.get(i).get("HourAC");
        String TimeAc = contactList.get(i).get("TimeAc");
        String Type = contactList.get(i).get("Type");
        String TypeName = contactList.get(i).get("TypeName");
        String PicActivity = contactList.get(i).get("PicActivity");
        String ActivityDetail = contactList.get(i).get("ActivityDetail");

        Intent intent = new Intent(ActivityActivity.this, UpdateActivity.class);
//                    Log.v("MYTAG","before");
        intent.putExtra("ActivityID",ActivityID);
        intent.putExtra("ActivityTitle", ActivityTitle);
        intent.putExtra("DateTo", DateTo);
        intent.putExtra("DateFrom", DateFrom);
        intent.putExtra("TimeFrom", TimeFrom);
        intent.putExtra("TimeTo", TimeTo);
        intent.putExtra("Number", Number);
        intent.putExtra("HourAC", HourAC);
        intent.putExtra("TimeAc", TimeAc);
        intent.putExtra("Type", Type);
        intent.putExtra("TypeName", TypeName);
        intent.putExtra("PicActivity", PicActivity);
        intent.putExtra("ActivityDetail", ActivityDetail);
        Log.v(TimeFrom,"after");
        startActivity(intent);

    }



    public void clickView(View v) {
                final int i = listView.getPositionForView((View) v.getParent());
                String ActivityTitle = contactList.get(i).get("ActivityTitle");
                String DateTo = contactList.get(i).get("DateTo");
                String DateFrom = contactList.get(i).get("DateFrom");
                String TimeFrom = contactList.get(i).get("TimeFrom");
                String TimeTo = contactList.get(i).get("TimeTo");
                String Number = contactList.get(i).get("Number");
                String HourAC = contactList.get(i).get("HourAC");
                String Type = contactList.get(i).get("Type");
                String TypeName = contactList.get(i).get("TypeName");
                String InstructorName = contactList.get(i).get("InstructorName");
                String PicActivity = contactList.get(i).get("PicActivity");
                String ActivityDetail = contactList.get(i).get("ActivityDetail");

                Intent intent = new Intent(getApplicationContext(), ViewActivity.class);
//                    Log.v("MYTAG","before");
                intent.putExtra("ActivityTitle", ActivityTitle);
                intent.putExtra("DateTo", DateTo);
                intent.putExtra("DateFrom", DateFrom);
                intent.putExtra("TimeFrom", TimeFrom);
                intent.putExtra("TimeTo", TimeTo);
                intent.putExtra("Number", Number);
                intent.putExtra("HourAC", HourAC);
                intent.putExtra("Type", Type);
                intent.putExtra("TypeName", TypeName);
                intent.putExtra("InstructorName", InstructorName);
                intent.putExtra("PicActivity", PicActivity);
                intent.putExtra("ActivityDetail", ActivityDetail);
                Log.v(TimeFrom,"after");
                startActivity(intent);





    }

    public void clickQR(View view) {
        final int i = listView.getPositionForView((View) view.getParent());
        String ActivityID = contactList.get(i).get("ActivityID");
        String ActivityTitle = contactList.get(i).get("ActivityTitle");
        Intent intent = new Intent(ActivityActivity.this, ActivityQRActivity.class);
        intent.putExtra("ActivityID", ActivityID);
        intent.putExtra("ActivityTitle", ActivityTitle);
//        intent.putExtra("ActivityID",contactList.get(i).get("ActivityID"));
//        intent.putExtra("ActivityTitle", contactList.get(i).get("ActivityTitle"));
        startActivity(intent);
    }

//
//    public Bitmap StringToBitMap(String encodedString) {
//
//        byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
//        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
//        return bitmap;
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.CreateAc:
                Intent newActivity = new Intent(ActivityActivity.this, NewActivity.class);
                startActivity(newActivity);
                return true;
            case R.id.history:
                Intent intent = new Intent(ActivityActivity.this, AcStActivity.class);
                startActivity(intent);
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }


    private class GetPlaces extends AsyncTask<Void, Void, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(ActivityActivity.this, "รายการกิจกรรม", Toast.LENGTH_LONG).show();

        }



        @SuppressLint("WrongThread")
        @Override
        protected Bitmap doInBackground(Void... params) {
            HttpHandlerAc sh = new HttpHandlerAc();
            //"+IPConnect.IP_CONNECT+"
            session = new SessionManager(getApplicationContext());
            HashMap<String,String> user = session.getUserDetails();
            ID = user.get(SessionManager.KEY_ID);

           String url = "http://"+IPConnect.IP_CONNECT+"/android/get_activity.php";
            url += "?InstructorID=" + ID ;
            String jsonStr = sh.makeServiceCall(url);
            Log.e(TAG, "Response from url:" + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray result = jsonObj.getJSONArray("result");
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject c = result.getJSONObject(i);
                        String ActivityID = c.getString("ActivityID");
                        String ActivityTitle = c.getString("ActivityTitle");
                        String CreateDate = c.getString("CreateDate");
                        String DateFrom = c.getString("DateFrom");
                        String DateTo = c.getString("DateTo");
                        String TimeFrom = c.getString("TimeFrom");
                        String TimeTo = c.getString("TimeTo");
                        String Number = c.getString("Number");
                        String HourAC = c.getString("HourAC");
                        String TimeAc = c.getString("TimeAc");
                        String InstructorName = c.getString("InstructorName");
                        String Type = c.getString("Type");
                        String TypeName = c.getString("TypeName");

//                        String PicActivity = c.getString("PicActivity");
//
                        String ActivityDetail = c.getString("ActivityDetail");

//                        HashMap<String, String> contact = new HashMap<>();
//                        HashMap<String, Bitmap> map = new HashMap<>();
                        Map map = new HashMap<String, Bitmap>();
                        String PicActivity =c.getString("PicActivity");
                         byte decodedString[]= android.util.Base64.decode(PicActivity, android.util.Base64.DEFAULT);

                       Bitmap bmp = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);


                        map.put("ActivityID", ActivityID);
                        map.put("ActivityTitle", ActivityTitle);
                        map.put("CreateDate", CreateDate);
                        map.put("DateFrom", DateFrom);
                        map.put("DateTo", DateTo);
                        map.put("TimeFrom", TimeFrom);
                        map.put("TimeTo", TimeTo);
                        map.put("Number", Number);
                        map.put("HourAC", HourAC);
                        map.put("TimeAc", TimeAc);
                        map.put("Type", Type);
                        map.put("TypeName", TypeName);
                        map.put("InstructorName", InstructorName);
                        map.put("PicActivity",PicActivity);
                        Log.v("MYTAG",PicActivity);
                        map.put("ActivityDetail", ActivityDetail);
                        contactList.add((HashMap<String, String>) map);;
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
        protected void onPostExecute(Bitmap bitmap ) {
            super.onPostExecute(bitmap);


           final ListAdapter adapter = new SimpleAdapter(ActivityActivity.this, contactList,
                     R.layout.list_activity, new String[]{"ActivityTitle", "CreateDate", "DateFrom", "DateTo","InstructorName"},
                     new int[]{ R.id.textTitle, R.id.textCreate, R.id.day1, R.id.day3,R.id.textIns});
            listView.setAdapter(adapter);


            // Setting the adapter to the listView
//            listView.setAdapter(adapter);
//            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> adapter, View view, int i, long id) {
//
//                        Toast.makeText(ActivityActivity.this, "ListView clicked" + id, Toast.LENGTH_SHORT).show();
//                    }
//
//
//            });
        }

    }
}


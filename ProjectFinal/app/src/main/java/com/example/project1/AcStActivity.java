package com.example.project1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AcStActivity extends AppCompatActivity {
    private String TAG = AcStActivity.class.getSimpleName();
    private ListView listView;
    ArrayList<HashMap<String, String>> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ac_st);

        contactList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.list_Ac_St);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new GetPlaces().execute();
    }


    private class GetPlaces extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(AcStActivity.this, "รายการกิจกรรม", Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandlerAc sh = new HttpHandlerAc();
            String url = "http://"+IPConnect.IP_CONNECT+"/android/get_activityST.php";
            String jsonStr = sh.makeServiceCall(url);
            Log.e(TAG, "Response from url:" + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray result = jsonObj.getJSONArray("result");
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject c = result.getJSONObject(i);
                        String ActivityTitle = c.getString("ActivityTitle");
                        String CreateDate = c.getString("CreateDate");
                        String DateFrom = c.getString("DateFrom");
                        String DateTo = c.getString("DateTo");
                        String TimeFrom = c.getString("TimeFrom");
                        String TimeTo = c.getString("TimeTo");
                        String Number = c.getString("Number");
                        String HourAC = c.getString("HourAC");
                        String Type = c.getString("Type");
                        String TypeName = c.getString("TypeName");
                        String PicActivity = c.getString("PicActivity");
                        String InstructorName = c.getString("InstructorName");
                        String ActivityDetail = c.getString("ActivityDetail");

                        HashMap<String, String> contact = new HashMap<>();

                        Map map = new HashMap<String, Bitmap>();
//                        String PicActivity =c.getString("PicActivity");
                        byte decodedString[]= android.util.Base64.decode(PicActivity, android.util.Base64.DEFAULT);

                        Bitmap bmp = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        contact.put("ActivityTitle", ActivityTitle);
                        contact.put("CreateDate", CreateDate);
                        contact.put("DateFrom", DateFrom);
                        contact.put("DateTo", DateTo);
                        contact.put("TimeFrom", TimeFrom);
                        contact.put("TimeTo", TimeTo);
                        contact.put("Number", Number);
                        contact.put("HourAC", HourAC);
                        contact.put("Type", Type);
                        contact.put("TypeName", TypeName);
                        contact.put("PicActivity", PicActivity);
                        contact.put("InstructorName", InstructorName);
                        contact.put("ActivityDetail", ActivityDetail);
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
            ListAdapter adapter = new SimpleAdapter(AcStActivity.this, contactList,
                    R.layout.list_st_activity, new String[]{"ActivityTitle", "CreateDate", "DateFrom", "DateTo","InstructorName"}, new int[]{
                    R.id.textTitle1, R.id.textCreate, R.id.day1, R.id.day3,R.id.tInName});
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapter, View view, int i, long id) {
//                    Intent i = new Intent(AcStActivity.this, ViewActivity.class);
//                    startActivity(i);

                    String ActivityTitle = contactList.get(i).get("ActivityTitle");
                    String DateTo = contactList.get(i).get("DateTo");
                    String DateFrom = contactList.get(i).get("DateFrom");
                    String TimeFrom = contactList.get(i).get("TimeFrom");
                    String TimeTo = contactList.get(i).get("TimeTo");
                    String Number = contactList.get(i).get("Number");
                    String HourAC = contactList.get(i).get("HourAC");
                    String Type = contactList.get(i).get("Type");
                    String TypeName = contactList.get(i).get("TypeName");
                    String PicActivity = contactList.get(i).get("PicActivity");
                    String InstructorName = contactList.get(i).get("InstructorName");
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
                    intent.putExtra("PicActivity", PicActivity);
                    intent.putExtra("InstructorName", InstructorName);
                    intent.putExtra("ActivityDetail", ActivityDetail);
                    Log.v(TimeFrom,"after");
                    startActivity(intent);
                }
            });
        }
    }
}

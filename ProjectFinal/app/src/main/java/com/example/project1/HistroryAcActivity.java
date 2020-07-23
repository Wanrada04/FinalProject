package com.example.project1;

import android.content.Intent;
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

public class HistroryAcActivity extends AppCompatActivity {
    private String TAG = HistroryAcActivity.class.getSimpleName();
    private ListView listView;
    ArrayList<HashMap<String, String>> contactList;
    SessionManager session;
    private static String ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_histrory_ac);
        contactList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.list_his);
        new GetPlaces().execute();

    }
    private class GetPlaces extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(HistroryAcActivity.this, "ประวัติการเข้ากิจกรรม", Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();

            session = new SessionManager(getApplicationContext());
            HashMap<String, String> user = session.getUserDetails();
            ID = user.get(SessionManager.KEY_ID);
            String url = "http://" + IPConnect.IP_CONNECT + "/android/get_histrory_ac.php";

            url += "?InstructorID=" + ID;
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
                        String InstructorName = c.getString("InstructorName");

//
                        HashMap<String, String> contact = new HashMap<>();


                        contact.put("ActivityID", ActivityID);
                        contact.put("ActivityTitle", ActivityTitle);
                        contact.put("CreateDate", CreateDate);
                        contact.put("DateFrom", DateFrom);
                        contact.put("DateTo", DateTo);
                        contact.put("InstructorName", InstructorName);

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
        //        public Object getItem(int position) {
//
//            return position;
//        }
//        public long getItemId(int position) {
//            return position;
//        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            ListAdapter adapter = new SimpleAdapter(HistroryAcActivity.this, contactList,
                    R.layout.list_histrory_ac, new String[]{"ActivityTitle", "CreateDate", "DateFrom", "DateTo", "InstructorName"},
                    new int[]{R.id.textTitle, R.id.textCreate, R.id.day1, R.id.day3, R.id.textIns});
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(final AdapterView<?> adapter, final View v, final int position, long arg) {
                    String ActivityID = contactList.get(position).get("ActivityID");
                    Intent intent = new Intent(HistroryAcActivity.this, ResultListHisAcActivity.class);
                    //  intent.putExtra("Position",position+1+"");
                    intent.putExtra("ActivityID", ActivityID);
                    startActivity(intent);

                }


            });
        }

    }

}

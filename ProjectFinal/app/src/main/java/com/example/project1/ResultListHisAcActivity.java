package com.example.project1;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

public class ResultListHisAcActivity extends AppCompatActivity {
    private String TAG = ResultListHisAcActivity.class.getSimpleName();
    private ListView listView;
    ArrayList<HashMap<String, String>> contactList;
    SessionManager session;
    private static String ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_list_his_ac);
        contactList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.list_hisAc);


        new GetPlaces().execute();

    }
    private class GetPlaces extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(ResultListHisAcActivity.this, "ประวัติผลการเข้าร่วมกิจกรรม", Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();


            String url = "http://" + IPConnect.IP_CONNECT + "/android/get_result_activity.php";
            session = new SessionManager(getApplicationContext());
            HashMap<String, String> user = session.getUserDetails();
            ID = user.get(SessionManager.KEY_ID);
            String TempHolder = getIntent().getStringExtra("ActivityID");

            url += "?InstructorID=" + ID + "&ActivityID=" + TempHolder;

            String jsonStr = sh.makeServiceCall(url);
            Log.e(TAG, "Response from url:" + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray result = jsonObj.getJSONArray("result");
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject c = result.getJSONObject(i);
                        String ActivityID = c.getString("ActivityID");
                        String StudentName = c.getString("StudentName");
                        String ActivityTitle = c.getString("ActivityTitle");
                        String StudentID = c.getString("StudentID");
                        String CreateDate = c.getString("CreateDate");
                        HashMap<String, String> contact = new HashMap<>();
                        contact.put("ActivityID", ActivityID);
                        contact.put("ActivityTitle", ActivityTitle);
                        contact.put("StudentID", StudentID);
                        contact.put("StudentName", StudentName);
                        contact.put("CreateDate", CreateDate);
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
            ListAdapter adapter = new SimpleAdapter(ResultListHisAcActivity.this, contactList,
                    R.layout.list_result_ac, new String[]{"ActivityID", "ActivityTitle", "StudentID", "StudentName", "CreateDate"}, new int[]{
                    R.id.textID, R.id.textTitle, R.id.textStID, R.id.textSTName, R.id.textCreate});
            listView.setAdapter(adapter);
        }
    }

}

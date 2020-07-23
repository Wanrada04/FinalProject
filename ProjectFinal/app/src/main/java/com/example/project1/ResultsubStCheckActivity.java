package com.example.project1;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ResultsubStCheckActivity extends AppCompatActivity {
    private String TAG = ResultsubStCheckActivity.class.getSimpleName();
    private ListView listView;
    ArrayList<HashMap<String, String>> contactList;
    SessionManager session;
    private static String ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultsub_st_check);
        contactList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.list_st_checksub);
        new GetPlaces().execute();
    }

    private class GetPlaces extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();

            session = new SessionManager(getApplicationContext());
            HashMap<String, String> user = session.getUserDetails();
            ID = user.get(SessionManager.KEY_ID);
            String url = "http://" + IPConnect.IP_CONNECT + "/android/get_count_st.php";
            url += "?StudentID=" + ID;
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
                        String InstructorName = c.getString("InstructorName");
                        String Countst = c.getString("Countst");

                        HashMap<String, String> contact = new HashMap<>();
                        contact.put("Countst", Countst);
                        contact.put("CourseID", CourseID);
                        contact.put("CourseName", CourseName);
                        contact.put("StudentID", StudentID);
                        contact.put("StudentName", StudentName);
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

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(ResultsubStCheckActivity.this, "ผลการเข้าเรียนรายวิชาทั้งหมด", Toast.LENGTH_LONG).show();

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            final SimpleAdapter adapter = new SimpleAdapter(ResultsubStCheckActivity.this, contactList,
                    R.layout.list_resultsubc_st, new String[]{"Countst", "StudentID", "StudentName", "CourseID", "CourseName","InstructorName"}, new int[]{
                    R.id.textView17, R.id.textStID, R.id.textSTName, R.id.textID, R.id.textTitle, R.id.textTitle1});
            listView.setAdapter(adapter);


        }
    }

}

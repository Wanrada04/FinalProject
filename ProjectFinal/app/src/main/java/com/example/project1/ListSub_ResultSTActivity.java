package com.example.project1;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

public class ListSub_ResultSTActivity extends AppCompatActivity {
    private String TAG = ListSub_ResultSTActivity.class.getSimpleName();
    private ListView listView;
    ArrayList<HashMap<String, String>> contactList;
    SessionManager session;
    private static String ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_sub__result_st);

        contactList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listView2);

        new GetPlaces().execute();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu2, menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return super.onCreateOptionsMenu(menu);

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.CheckSt:
                Intent intent = new Intent(ListSub_ResultSTActivity.this, ResultsubStCheckActivity.class);
//                String CourseID =  getIntent().getStringExtra("CourseID");
//                intent.putExtra("CourseID",CourseID);
                startActivity(intent);
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    private class GetPlaces extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(ListSub_ResultSTActivity.this, "ข้อมูลรายวิชา", Toast.LENGTH_LONG).show();


        }


        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();
            session = new SessionManager(getApplicationContext());
            HashMap<String, String> user = session.getUserDetails();
            ID = user.get(SessionManager.KEY_ID);
            String url = "http://" + IPConnect.IP_CONNECT + "/android/get_subst.php";
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
                        String CourseName = c.getString("CourseName");
                        String InstructorID = c.getString("InstructorID");
                        String InstructorName = c.getString("InstructorName");
                        HashMap<String, String> contact = new HashMap<>();
                        contact.put("CourseID", CourseID);
                        contact.put("CourseName", CourseName);
                        contact.put("InstructorID", InstructorID);
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
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ListAdapter adapter = new SimpleAdapter(ListSub_ResultSTActivity.this, contactList,
                    R.layout.list_subst, new String[]{"CourseID", "CourseName", "InstructorName"}, new int[]{
                    R.id.textID, R.id.textTitle, R.id.tInName});
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapter, View v, int position, long arg) {
//                    final int i = listView.getPositionForView((View) v.getParent());
                    String InstructorID = contactList.get(position).get("InstructorID");
                    Intent intent = new Intent(ListSub_ResultSTActivity.this, ResultSTsubActivity.class);
                    //  intent.putExtra("Position",position+1+"");
                    intent.putExtra("CourseID",contactList.get(position).get("CourseID"));
                    intent.putExtra("InstructorID",InstructorID);
                    startActivity(intent);
                }
            });


        }

    }
}

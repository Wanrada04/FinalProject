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

public class ListResultSubActivity extends AppCompatActivity {
    private String TAG = ListResultSubActivity.class.getSimpleName();
    private ListView listView;
    ArrayList<HashMap<String, String>> contactList;
    SessionManager session;
    private static String ID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_result_sub);


        contactList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listResultC);
        new GetPlaces().execute();


    }
    private class GetPlaces extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(ListResultSubActivity.this, "ข้อมูลรายวิชา", Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();

            session = new SessionManager(getApplicationContext());
            HashMap<String,String> user = session.getUserDetails();
            ID = user.get(SessionManager.KEY_ID);
            String url = "http://"+IPConnect.IP_CONNECT+"/android/get_sub.php";
//
//            String TempSec = getIntent().getStringExtra("Section");
//            Intent intent = new Intent(ListResultSubActivity.this, ResultsubActivity.class);
//            intent.putExtra("Section", TempSec);
//            startActivity(intent);

            url += "?InstructorID=" + ID  ;
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
                        String Countst = c.getString("Countst");
                        HashMap<String, String> contact = new HashMap<>();
                        contact.put("CourseID", CourseID);
                        contact.put("CourseName", CourseName);
                        contact.put("Countst", Countst);
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
            ListAdapter adapter = new SimpleAdapter(ListResultSubActivity.this, contactList,
                    R.layout.list_row, new String[]{"CourseID", "CourseName","Countst"}, new int[]{
                    R.id.textSub, R.id.textViewSub,R.id.textCounts});
            listView.setAdapter(adapter);
//            String Section = getIntent().getStringExtra("Section");
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapter, View v, int position, long arg) {
//                    final int i = listView.getPositionForView((View) v.getParent());
                    String CourseID = contactList.get(position).get("CourseID");
                    String Section = getIntent().getStringExtra("Section");

                    Intent intent = new Intent(ListResultSubActivity.this, ResultsubActivity.class);
                    //  intent.putExtra("Position",position+1+"");
                    intent.putExtra("CourseID",CourseID);
                    intent.putExtra("Section", Section);
                    startActivity(intent);




                }
            });

        }

    }
}

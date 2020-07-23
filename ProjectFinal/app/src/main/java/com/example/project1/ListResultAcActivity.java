package com.example.project1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListResultAcActivity extends AppCompatActivity {
    private String TAG = ListResultAcActivity.class.getSimpleName();
    private ListView listView;
//    private CheckBox checkBox;
    ArrayList<HashMap<String, String>> contactList;
    SessionManager session;
    private static String ID;
    String ServerURL =  "http://"+IPConnect.IP_CONNECT+"/android/update_status_ac.php";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_result_ac);
        contactList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.list_AcRe);
        new GetPlaces().execute();


    }

    public void clickView(View view){
            final int i = listView.getPositionForView((View) view.getParent());
            String ActivityID = contactList.get(i).get("ActivityID");
            Intent intent = new Intent(ListResultAcActivity.this, ResultAcActivity.class);
            intent.putExtra("ActivityID", ActivityID);
            startActivity(intent);

    }

    public void clickDelete(final View view){
        final int i = listView.getPositionForView((View) view.getParent());
        new AlertDialog.Builder(ListResultAcActivity.this).
                setIcon(android.R.drawable.ic_delete)
                .setTitle("คุณแน่ใจ...")
                .setMessage("คุณต้องการลบรายการนี้หรือไม่ ?")
                .setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                    int Status = 2;
                    String ActivityID = contactList.get(i).get("ActivityID");

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Remove(i,ActivityID,Status);
                    }
                })
                .setNegativeButton("ไม่ใช่", null).show();


    }

        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_his, menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.history:
                Intent intent = new Intent(ListResultAcActivity.this, HistroryAcActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void Remove(int i,final String ActivityID, final int Status) {

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                ;
                String TempID = ActivityID;
                int TempStatus = Status;

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();


                nameValuePairs.add(new BasicNameValuePair("ActivityID", TempID));
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
                Toast.makeText(ListResultAcActivity.this, "ลบเรียบร้อย", Toast.LENGTH_LONG).show();
                Intent newActivity = new Intent(ListResultAcActivity.this,ListResultAcActivity.class);
                startActivity(newActivity);

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();

        sendPostReqAsyncTask.execute(ActivityID );

    }
    private class GetPlaces extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(ListResultAcActivity.this, "รายการกิจกรรม", Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();

            session = new SessionManager(getApplicationContext());
            HashMap<String, String> user = session.getUserDetails();
            ID = user.get(SessionManager.KEY_ID);
            String url = "http://" + IPConnect.IP_CONNECT + "/android/get_activity.php";

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
        public boolean isSelected() {
             return Selected;
        }
        public void setSelected(boolean selected) {
            Selected = selected;
        }

        private boolean Selected = false;

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            ListAdapter adapter = new SimpleAdapter(ListResultAcActivity.this, contactList,
                    R.layout.list_resultac, new String[]{"ActivityTitle", "CreateDate", "DateFrom", "DateTo", "InstructorName"},
                    new int[]{R.id.textTitle, R.id.textCreate, R.id.day1, R.id.day3, R.id.textIns});
            listView.setAdapter(adapter);

//            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//                @Override
//                public void onItemClick(final AdapterView<?> adapter, final View v, final int position, long arg) {
//                    String ActivityID = contactList.get(position).get("ActivityID");
//                    Intent intent = new Intent(ListResultAcActivity.this, ResultAcActivity.class);
//                    //  intent.putExtra("Position",position+1+"");
//                    intent.putExtra("ActivityID", ActivityID);
//                    startActivity(intent);

//                    final CheckBox checkBox = (CheckBox) v.findViewById(R.id.chk_selected);
//                    checkBox.setFocusable(false);
//                    checkBox.setFocusableInTouchMode(false);
//                    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                        @Override
//                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                            int getPosition = (Integer) buttonView.getTag();
//                            Log.e("test", "inside check...position" + getPosition);
//                            String check = contactList.get(position).get("ActivityID");
//                            CheckBox cb = (CheckBox) v.getTag();
//                            if (cb.isChecked()) {

//                            } else {
//                            }
//                        }
//                    });


//
//                }
//
//
//            });
        }

    }

}

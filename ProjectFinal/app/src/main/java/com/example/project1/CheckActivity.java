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

public class CheckActivity extends AppCompatActivity {
    private String TAG = CheckActivity.class.getSimpleName();
    private ListView listView;
    ArrayList<HashMap<String, String>> contactList;
    SessionManager session;
    private static String ID;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        contactList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listView1);


        new GetPlaces().execute();
    }

    private class GetPlaces extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(CheckActivity.this, "ข้อมูลรายวิชา", Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();

            session = new SessionManager(getApplicationContext());
            HashMap<String,String> user = session.getUserDetails();
            ID = user.get(SessionManager.KEY_ID);
          String url = "http://"+IPConnect.IP_CONNECT+"/android/get_post.php";

            url += "?InstructorID=" + ID ;
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
                        HashMap<String, String> contact = new HashMap<>();
                        contact.put("CourseID", CourseID);
                        contact.put("CourseName", CourseName);
                        contact.put("InstructorID", InstructorID);
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
            ListAdapter adapter = new SimpleAdapter(CheckActivity.this, contactList,
                    R.layout.list_row, new String[]{"CourseID", "CourseName"}, new int[]{
                    R.id.textSub, R.id.textViewSub});
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapter, View v, int position, long arg) {
//                    final int i = listView.getPositionForView((View) v.getParent());
                    String InstructorID = contactList.get(position).get("InstructorID");
                    String CourseName = contactList.get(position).get("CourseName");
                    Intent intent = new Intent(CheckActivity.this, SubQRActivity.class);
                  //  intent.putExtra("Position",position+1+"");
                    intent.putExtra("CourseID",contactList.get(position).get("CourseID"));
                    intent.putExtra("InstructorID",InstructorID);
                    intent.putExtra("CourseName", CourseName);
                    startActivity(intent);
                }
            });
        }

    }
}




//    String urladdress = "http://"+IPConnect.IP_CONNECT+"/android/get_post.php";
//    String[] CourseID;
//    String[] CourseName;
//    ListView listView;
//    BufferedInputStream is;
//    String line=null;
//    String result=null;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_check);
//
//        listView=(ListView)findViewById(R.id.listView1);
//
//        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));
//        collectData();
//        CustomListView customListView=new CustomListView(this,CourseID,CourseName);
//        listView.setAdapter(customListView);
//
//    }
//
//
//    private void collectData()
//    {
////Connection
//        try{
//
//            URL url=new URL(urladdress);
//            HttpURLConnection con=(HttpURLConnection)url.openConnection();
//            con.setRequestMethod("http://"+IPConnect.IP_CONNECT+"/android/get_post.php");
//            is=new BufferedInputStream(con.getInputStream());
//
//        }
//        catch (Exception ex)
//        {
//            ex.printStackTrace();
//        }
//        //content
//        try{
//            BufferedReader br=new BufferedReader(new InputStreamReader(is));
//            StringBuilder sb=new StringBuilder();
//            while ((line=br.readLine())!=null){
//                sb.append(line+"\n");
//            }
//            is.close();
//            result=sb.toString();
//
//        }
//        catch (Exception ex)
//        {
//            ex.printStackTrace();
//
//        }
//
////JSON
//        try{
//            JSONArray ja=new JSONArray(result);
//            JSONObject jo=null;
//            CourseID=new String[ja.length()];
//            CourseName=new String[ja.length()];
//
//
//            for(int i=0;i<=ja.length();i++){
//                jo=ja.getJSONObject(i);
//                CourseID[i]=jo.getString("CourseID");
//                CourseName[i]=jo.getString("CourseName");
//            }
//        }
//        catch (Exception ex)
//        {
//
//            ex.printStackTrace();
//        }
//
//
//    }
//    ListView listView;
//    ProgressDialog dialog;
//    ProgressBar progressBar;
////    String url = "http://"+IPConnect.IP_CONNECT+"/android/get_post.php";
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_check);
////        dialog = new ProgressDialog(this);
////        dialog.setMessage("Loading....");
////        dialog.show();
//
//        listView = (ListView) findViewById(R.id.listView1);
////            progressBar = (ProgressBar)findViewById(R.id.progressbar);
//
//        getJSON("http://"+IPConnect.IP_CONNECT+"/android/get_post.php");
//
////        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
////                @Override
////            public void onResponse(String string) {
////                try {
////                    loadIntoListView(string);
////
////                    } catch (JSONException e) {
////                    e.printStackTrace();
////                }
////            }
////        }, new Response.ErrorListener() {
////            @Override
////            public void onErrorResponse(VolleyError volleyError) {
////                Toast.makeText(getApplicationContext(), "Some error occurred!!", Toast.LENGTH_SHORT).show();
////                dialog.dismiss();
////            }
////        });
//
////        RequestQueue rQueue = Volley.newRequestQueue(CheckActivity.this);
////        rQueue.add(request);
//    }
//
//
//
//
//    private void getJSON(final String urlWebService) {
//
//        class GetJSON extends AsyncTask<Void, Void, String> {
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//            }
//
//
//            @Override
//            protected void onPostExecute(String s) {
//                super.onPostExecute(s);
//                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
//                try {
//                    loadIntoListView(s);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            protected String doInBackground(Void... voids) {
//                try {
//                    URL url = new URL(urlWebService);
//                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
//                    StringBuilder sb = new StringBuilder();
//                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
//                    String json;
//                    while ((json = bufferedReader.readLine()) != null) {
//                        sb.append(json + "\n");
//                    }
//                    return sb.toString().trim();
//                } catch (Exception e) {
//                    return null;
//                }
//            }
//        }
//        GetJSON getJSON = new GetJSON();
//        getJSON.execute();
//    }
//
//
//    private void loadIntoListView(String json) throws JSONException {
//
////        JSONArray jsonArray = new JSONArray(json);
////        String[] CourseName = new String[jsonArray.length()];
////        for (int i = 0; i < jsonArray.length(); i++) {
////            JSONObject obj = jsonArray.getJSONObject(i);
////            CourseName[i] = obj.getString("result");
////        }
//
//            JSONObject object = new JSONObject(json);
//            JSONArray jsonArray = object.getJSONArray("CourseID");
//            ArrayList al = new ArrayList();
//
//            for (int i = 0; i < jsonArray.length(); ++i) {
//                al.add(jsonArray.getString(i));
//            }
////            progressBar.setVisibility(View.INVISIBLE);
//            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(CheckActivity.this, android.R.layout.activity_list_item,al);
//            listView.setAdapter(arrayAdapter);
//    }



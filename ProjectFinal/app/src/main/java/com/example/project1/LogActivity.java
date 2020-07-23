package com.example.project1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class LogActivity extends AppCompatActivity {

    EditText User, Pass;
    CardView bt;
    String user,pass;
    private static String URL = "http://" + IPConnect.IP_CONNECT + "/android/login.php";
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private String TAG = LogActivity.class.getSimpleName();
    public static String FullName, UserID;

    public static String Status;
    SharedPreferences pref;

    public static final String MyPREFERENCES = "LogInfo";
    ArrayList<HashMap<String, String>> contactList;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        contactList = new ArrayList<>();

        User = (EditText) findViewById(R.id.editUser);
        Pass = (EditText) findViewById(R.id.editPass);
        bt = (CardView) findViewById(R.id.CVLogout);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
//                if (User.getText().toString().equals("1") && Pass.getText().toString().equals("1")) {
//                    Toast.makeText(LogActivity.this, "เข้าสู่ระบบ", Toast.LENGTH_SHORT).show();
//                    Intent i = new Intent(LogActivity.this, MainActivity.class);
//                    startActivity(i);
//                } else if (User.getText().toString().equals("2") && Pass.getText().toString().equals("2")) {
//                    Toast.makeText(LogActivity.this, "เข้าสู่ระแบบ", Toast.LENGTH_SHORT).show();
//                    Intent i = new Intent(LogActivity.this, STMainActivity.class);
//                    startActivity(i);
//
//                } else {
//                    Toast.makeText(LogActivity.this, "รหัสผ่านไม่ถูกต้อง", Toast.LENGTH_SHORT).show();
//                }
////
//                 userLoginOrAdd(URL);

//                getlogin();

//                String user = User.getText().toString();
//                String pass = Pass.getText().toString();
//
//                if (user.isEmpty() || pass.isEmpty()) {
//                    Toast.makeText(LogActivity.this, "โปรดใส่ชื่อผู้ใช้และรหัสผ่าน", Toast.LENGTH_LONG).show();
//                } else {
//                Login(user,pass);
//                }
                if (User.getText().toString().isEmpty() || Pass.getText().toString().isEmpty()) {
                    Toast.makeText(LogActivity.this, "โปรดใส่ชื่อผู้ใช้และรหัสผ่าน", Toast.LENGTH_LONG).show();
                } else {

                    new GetPlaces().execute();
                }
            }
        });

        session = new SessionManager(getApplicationContext());
    }


//    public void Login(String user, String pass) {
//        HttpHandler sh = new HttpHandler();
//
//        String url = "http://" + IPConnect.IP_CONNECT + "/android/login.php";
//        //---------- ส่งค่า --------------------
//        url += "?Username=" + user + "&Password=" + pass;
//        String jsonStr = sh.makeServiceCall(url);
//        Log.e(TAG, "Response from url:" + jsonStr);
//        if (jsonStr != null) {
//            try {
//                JSONObject jsonObj = new JSONObject(jsonStr);
//                JSONArray result = jsonObj.getJSONArray("result");
//                for (int i = 0; i < result.length(); i++) {
//                    JSONObject c = result.getJSONObject(i);
//                    UserID = c.getString("ID");
//                    FullName = c.getString("Name");
//                    Status = c.getString("Status");
//
//
//
//
//                    HashMap<String, String> contact = new HashMap<>();
//                    contact.put("ID", UserID);
//                    contact.put("Name", FullName);
//                    contactList.add(contact);
//                }
//
//                //set Value for session
//                session.createLoginSession(UserID, FullName);
//
//            }
//            catch (final JSONException e) {
//                Log.e(TAG, "Json parsing error" + e.getMessage());
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(getApplicationContext(),
//                                "json from server" + e.getMessage(), Toast.LENGTH_LONG).show();
//                    }
//                });
//
//            }
//        } else {
//            Log.e(TAG, "from server");
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Toast.makeText(getApplicationContext(),
//                            "Couldn't get json from server", Toast.LENGTH_LONG).show();
//
//                }
//            });
//        }
//    }
//
//}










//        private void getlogin() {
//
//            String user = User.getText().toString();
//            String pass = Pass.getText().toString();
//
//            if (user.isEmpty() || pass.isEmpty()) {
//                Toast.makeText(LogActivity.this, "โปรดใส่ชื่อผู้ใช้และรหัสผ่าน", Toast.LENGTH_LONG).show();
//            } else {
//                HttpHandler sb = new HttpHandler();
//                String url = "http://" + IPConnect.IP_CONNECT + "/android/login.php";
//                //---------- ส่งค่า --------------------
//                url += "?Username=" + user + "&Password=" + pass;
//                String jsonStr = sb.makeServiceCall(url);
//                Log.e(TAG, "Response from url:" + jsonStr);
//                if (jsonStr != null) {
//                    try {
//                        JSONObject jsonObj = new JSONObject(jsonStr);
//                        JSONArray result = jsonObj.getJSONArray("result");
//                        for (int i = 0; i < result.length(); i++) {
//                            JSONObject c = result.getJSONObject(i);
//                            UserID = c.getString("ID");
//                            FullName = c.getString("Name");
//                            Status = c.getString("Status");
//
//
//                            HashMap<String, String> contact = new HashMap<>();
//                            contact.put("ID", UserID);
//                            contact.put("Name", FullName);
//                            contactList.add(contact);
//                        }
//                    } catch (final JSONException e) {
//                        Log.e(TAG, "Json parsing error" + e.getMessage());
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(getApplicationContext(),
//                                        "json from server" + e.getMessage(), Toast.LENGTH_LONG).show();
//                            }
//                        });
//
//                    }
//                } else {
//                    Log.e(TAG, "from server");
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(getApplicationContext(),
//                                    "Couldn't get json from server", Toast.LENGTH_LONG).show();
//
//                        }
//                    });
//                }
//                return ;
//            }
//        }
//
////    }
//
    private class GetPlaces extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Toast.makeText(LogActivity.this, "เข้าสู่ระบบ", Toast.LENGTH_LONG).show();

        }


        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {

         user = User.getText().toString();
         pass = Pass.getText().toString();

        HttpHandler sh = new HttpHandler();

        String url = "http://" + IPConnect.IP_CONNECT + "/android/login.php";
        //---------- ส่งค่า --------------------
        url += "?Username=" + user + "&Password=" + pass;
        String jsonStr = sh.makeServiceCall(url);
        Log.e(TAG, "Response from url:" + jsonStr);
        if (jsonStr != null) {

            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                JSONArray result = jsonObj.getJSONArray("result");

                if (result.length() > 0) {
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject c = result.getJSONObject(i);
                        UserID = c.getString("ID");
                        FullName = c.getString("Name");
                        Status = c.getString("Status");


                        HashMap<String, String> contact = new HashMap<>();
                        contact.put("ID", UserID);
                        contact.put("Name", FullName);
                        contactList.add(contact);
                        session.createLoginSession(UserID, FullName);

                    }
                } else {

                    UserID = null;
                    FullName = null;
                    Status = null;

                    contactList.clear();


//                    Toast.makeText(LogActivity.this, "ชื่อผู้ใช้และรหัสผ่านไม่ถูกต้อง", Toast.LENGTH_LONG).show();

                }
            }
            catch (final JSONException e) {
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
        Intent intent=new Intent();

        if(Status!=null&&Status.equals("1"))
        {
            intent = new Intent(LogActivity.this, MainActivity.class);
        }
        else if(Status!=null&&Status.equals("0"))
        {
            intent = new Intent(LogActivity.this, STMainActivity.class);
        }

        if(Status!=null) {
            Toast.makeText(LogActivity.this, "เข้าสู่ระบบ", Toast.LENGTH_LONG).show();
            intent.putExtra("FullName", FullName);
            intent.putExtra("UserID", UserID);
            startActivity(intent);
        }
        else{  Toast.makeText(LogActivity.this, "ชื่อผู้ใช้หรือรหัสผ่านไม่ถูกต้อง", Toast.LENGTH_LONG).show();}

//            ListAdapter adapter = new SimpleAdapter(LogActivity.this, contactList,
//                    R.layout.activity_main, new String[]{"ID", "Name"}, new int[]{
//                    R.id.textName , R.id.textUsername});
//            .setAdapter(adapter);


    }
}
}


















package com.example.project1;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SettingActivity extends AppCompatActivity {
    private Chronometer chronometer;
    private long pauseOffset;
    private boolean running;
    Button start,set;
    EditText editText;
    TextView text;
    String TempTime;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mStartTimeInMillis;
    private long mTimeLeftInMillis;
    private long mEndTime;
    SessionManager session;
    private static String ID;
    String URLUPDATE = "http://"+IPConnect.IP_CONNECT+"/android/set_time_update.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        ID = user.get(SessionManager.KEY_ID);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Update(ID);
            }
        }, 0);
    }


    public void Update(final String InstructorID) {

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {


                String TempTID = InstructorID;


                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();



                nameValuePairs.add(new BasicNameValuePair("InstructorID", TempTID));

                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(URLUPDATE);

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
                Toast.makeText(getApplicationContext(), "เรียบร้อย", Toast.LENGTH_LONG).show();
                finish();



            }

        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();

        sendPostReqAsyncTask.execute(InstructorID);
    }




}

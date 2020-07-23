package com.example.project1;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class NewActivity extends AppCompatActivity {
    private String TAG = NewActivity.class.getSimpleName();
    private static final int PICK_IMAGE_REQUEST = 1;
    private Button ButtonChooseImage;
    private ImageView ImageView;
    int bitmap_size = 50;
    private Uri filePath;
    String ServerURL = "http://" + IPConnect.IP_CONNECT + "/android/ActivityInsert.php";
    String TempName;
    String TempType;
    String TempImage;
    String TempDate1;
    String TempDate2;
    String TempTime1;
    String TempTime2;
    String TempDetail;
    int TempTimeAc;
    int TempNam;
    double TempHour;
    double diff;

    Button selectDate, selectDate1, selectTime, selectTime1, Clear, Save;
    TextView date, date1, time, time1;
    TextView timediff;
    EditText editText1, editText2, editText3, editText4, editText5;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog, timePickerDialog1;
    int year;
    int month;
    int dayOfMonth;
    int hour, minute, seconds, hour1, minute1, seconds1;
    Calendar calendar;
    private AdapterView spinner;
    ArrayList<String> listItems = new ArrayList<>();
    ArrayAdapter<String> adapter;
    Spinner sp;
    Bitmap bitmap, decoded;
    ArrayList<String> list;
    ArrayList<String> listValue;
    SessionManager session;
    private static String UserID;
    private static String timefrom;
    private static String timeto;

    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editNum);
        editText3 = findViewById(R.id.editText3);
        editText3.setEnabled(false);

        editText4 = findViewById(R.id.editTextTime);
        editText5 = findViewById(R.id.editText5);


        ButtonChooseImage = findViewById(R.id.BtnUp);
        ImageView = findViewById(R.id.ImgView);
        selectDate = findViewById(R.id.btnDate);
        date = findViewById(R.id.textViewDay);

        selectDate1 = findViewById(R.id.btnDateTo);
        date1 = findViewById(R.id.textViewToDay);

        selectTime = findViewById(R.id.btnTime);
        time = findViewById(R.id.textViewTime);

        selectTime1 = findViewById(R.id.btnTimeTo);
        time1 = findViewById(R.id.textViewToTime);

        Clear = findViewById(R.id.BtnClear);
        Save = findViewById(R.id.okBtn2);


        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(NewActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                date.setText(year + "-" + (month + 1) + "-" + day);
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.show();


            }
        });


        selectDate1.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               calendar = Calendar.getInstance();
                                               year = calendar.get(Calendar.YEAR);
                                               month = calendar.get(Calendar.MONTH);
                                               dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                                               datePickerDialog = new DatePickerDialog(NewActivity.this,
                                                       new DatePickerDialog.OnDateSetListener() {
                                                           @Override
                                                           public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                                               date1.setText(year + "-" + (month + 1) + "-" + day);
                                                           }
                                                       }, year, month, dayOfMonth);
                                               datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                                               datePickerDialog.show();
                                           }
                                       }

        );


            selectTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    calendar = Calendar.getInstance();
                    hour = calendar.get(Calendar.HOUR_OF_DAY);
                    minute = calendar.get(Calendar.MINUTE);
                    timePickerDialog = new TimePickerDialog(NewActivity.this,
                            new TimePickerDialog.OnTimeSetListener() {

                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay,
                                                      int minute) {

                                    time.setText(hourOfDay + ":" + minute);
                                    getDiffTime();


                                }
                            }, hour, minute, false);
                    timePickerDialog.show();
                }

            });


            selectTime1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    calendar = Calendar.getInstance();
                    hour = calendar.get(Calendar.HOUR_OF_DAY);
                    minute = calendar.get(Calendar.MINUTE);
                    timePickerDialog1 = new TimePickerDialog(NewActivity.this,
                            new TimePickerDialog.OnTimeSetListener() {

                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay,
                                                      int minute) {

                                    time1.setText(hourOfDay + ":" + minute);
                                    getDiffTime();

                                }

                            }, hour, minute, false);
                    timePickerDialog1.show();

                }
            });



//--------------------------------------------------------------------------------------------

        sp = (Spinner) findViewById(R.id.spinner1);
//        adapter=new ArrayAdapter<String>(this,R.layout.spinner_layout,R.id.txt,listItems);
//        sp.setAdapter(adapter);
//        spinner = (Spinner) findViewById(R.id.spinner1);
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(NewActivity.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.Type));
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(dataAdapter);


        Clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date.setText("");
                date1.setText("");
                time.setText("");
                time1.setText("");
                editText1.setText("");
                editText2.setText("");
                editText3.setText("");
                editText4.setText("");
                editText5.setText("");
                sp.setAdapter(null);
                ImageView.setImageBitmap(null);
                Toast.makeText(NewActivity.this, "ล้างข้อมูล", Toast.LENGTH_SHORT).show();
            }
        });

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetData();

//                if (TempName.equals("") || TempDetail.equals("")  || TempDate1.equals("") ||
//                        TempDate2.equals("") || TempTime1.equals("")||TempTime2.equals("")|| TempNam == 0 || TempHour ==0 ||TempTimeAc==0)
//                {
//                    Toast.makeText(getApplicationContext(), "กรอกข้อมูลไม่ครบ", Toast.LENGTH_LONG).show();
//                }else
                if (TempName.equals("")) {
                    Toast.makeText(getApplicationContext(), "กรุณากรอกชื่อกิจกรรม", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TempDetail.equals("")) {
                    Toast.makeText(getApplicationContext(), "กรุณากรอกรายละเอียด", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TempDate1.equals("") || (TempDate2.equals(""))) {
                    Toast.makeText(getApplicationContext(), "กรุณากรอกวันที่", Toast.LENGTH_LONG).show();
                    return;
                }

                if (TempTime1.equals("") || (TempTime2.equals(""))) {
                    Toast.makeText(getApplicationContext(), "กรุณากรอกเวลา", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TempNam == 0) {
                    Toast.makeText(getApplicationContext(), "กรุณากรอกจำนวน", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TempHour == 0) {
                    Toast.makeText(getApplicationContext(), "กรุณากรอกจำนวนชั่วโมง", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TempTimeAc == 0) {
                    Toast.makeText(getApplicationContext(), "กรุณากรอกจำนวนชั่วโมงลบกิจกรรม", Toast.LENGTH_LONG).show();
                    return;
                }

                InsertData(TempName, TempDetail, TempNam, TempHour, TempDate1, TempDate2, TempTime1, TempTime2, TempImage, TempType, TempTimeAc);
//

            }
        });


        ButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseImage();
            }
        });


        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        UserID = user.get(SessionManager.KEY_ID);


    }
    //---------------------------------------------------------------------------------------------

    //

    public void getDiffTime () {

String DateStart=date.getText().toString();
        String DateEnd=date1.getText().toString();
            String start = time.getText().toString();
            String end = time1.getText().toString();

if(!start.isEmpty()&&!end.isEmpty()&&!DateStart.isEmpty()&&!DateEnd.isEmpty())
{
    String date1=DateStart+" "+start+":00";
    String date2=DateEnd+" "+end+":00";
    Date DatetimeStart= new Date();
    Date DatetimeEnd = new Date();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    try {
        DatetimeStart = dateFormat.parse(date1);
        DatetimeEnd = dateFormat.parse(date2);

        double different =   DatetimeEnd.getTime()-DatetimeStart.getTime();
       int i=(int)different;

         diff= ((double) i/(60*1000));
        //Double diffHours = (double) (different / (60 *60*1000 ));
       // double diff= ((double) i/(60*1000));
        //SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        String minutes = Integer.toString((int)diff % 60);
        minutes = minutes.length() == 1 ? "0" + minutes : minutes;
        String sdifTime= ((int)diff / 60) + ":" + minutes;
        //String time = df.format(new Date((int)diff * 60 * 1000L));

      //  String sDiff= Double.toString(sdiffTime);
        editText3.setText(sdifTime);
    }
    catch (Exception  e) {
        Log.e(TAG, "Json parsing error" + e.getMessage());
        e.printStackTrace();
    }
}


//            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
//
//            Time timestart = new Time(hour, minute, seconds);
//            Time timeend = new Time(hour, minute, seconds);
//
//            try {
//                timestart = (Time) format.parse(start);
//                timeend = (Time) format.parse(end);
//
//            } catch (Exception e) {
//                Log.e(TAG, "Json parsing error" + e.getMessage());
//                e.printStackTrace();
//            }
////
////        SimpleDateFormat format1 = new SimpleDateFormat("HH:mm:ss");
//////        diff = difference(timestart, timeend);
////        Time timeend = new Time(hour,minute,seconds);
////        try {
////            timeend = (Time) format1.parse(timeto);
////
////        } catch (Exception e) {
////            Log.e(TAG, "Json parsing error" + e.getMessage());
////            e.printStackTrace();
////        }
//            long difference = timestart.getTime() - timeend.getTime();
//            long diffHours = (int) (difference / (60 * 60 * 1000));




    }

//    private Time difference(Time timestart, Time timeend) {
//        Time diff = new Time(0, 0, 0);
//        long difference = timestart.getTime() - timeend.getTime();
//        long diffHours = (int) (difference / (60 * 60 * 1000));
//        return(diff);
//    }


    private void ChooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
//            Uri ImageUri = data.getData() ;
////            filePath = getPath(ImageUri);
//            ImageView.setImageURI(ImageUri);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                setToImageView(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, baos);

        byte[] imageBytes = baos.toByteArray();
        String encodedImage = android.util.Base64.encodeToString(imageBytes, Base64.DEFAULT);


        return encodedImage;
//
//        imageBytes = Base64.decode(encodedImage, Base64.DEFAULT);
//        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
//        ImageView.setImageBitmap(decodedImage);

    }
    private void setToImageView(Bitmap bmp) {
        //compress image
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, bytes);
        decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
        ImageView.setImageBitmap(decoded);
    }


    public void onStart(){
        super.onStart();
        BackTask bt=new BackTask();
        bt.execute();
    }
    class BackTask extends AsyncTask<Void,Void,Void> {

        protected void onPreExecute(){
            super.onPreExecute();
            //if(list==null || list.isEmpty() )
            //{
                list=new ArrayList<>();
                listValue=new ArrayList<>();
            //}

        }
        protected Void doInBackground(Void...params){
            InputStream is=null;
            String result="";
            try{
                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost= new HttpPost("http://"+IPConnect.IP_CONNECT+"/android/get_type.php");
                HttpResponse response=httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                // Get our response as a String.
                is = entity.getContent();
            }catch(IOException e){
                e.printStackTrace();
            }

            //convert response to string
            try{
                BufferedReader reader = new BufferedReader(new InputStreamReader(is,"utf-8"));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    result+=line;
                }
                is.close();
                //result=sb.toString();
            }catch(Exception e){
                e.printStackTrace();
            }
            // parse json data
            try{
                JSONArray jArray =new JSONArray(result);
                if(!list.isEmpty())
                {
                    list.clear();
                    listValue.clear();
                }
//
//
                for(int i=0;i<jArray.length();i++)
                {
                    JSONObject jsonObject=jArray.getJSONObject(i);

                    list.add(jsonObject.getString("TypeName"));
                    listValue.add(jsonObject.getString("TypeID"));


                }
            }
            catch(JSONException e){
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(Void result){
            listItems.clear();
            listItems.addAll(list);
            adapter = new ArrayAdapter<String>(NewActivity.this, android.R.layout.simple_spinner_item,listItems);
            sp.setAdapter(adapter);
        }
    }



    public void GetData() {
        TempName = editText1.getText().toString();
        TempDetail = editText5.getText().toString();
        Log.v("MYTAG", "before");
        Log.v("MYTAG", editText2.getText().toString());
        Log.v("MYTAG", editText3.getText().toString());
        if (editText2.getText().toString().isEmpty()) {
            TempNam = 0;
        } else {
            TempNam = Integer.parseInt(editText2.getText().toString());
        }

            TempHour = (diff);


        if (editText4.getText().toString().isEmpty()) {
            TempTimeAc = 0;
        } else {
            TempTimeAc = Integer.parseInt(editText4.getText().toString());
        }

//        Log.v("MYTAG","after");

        long indexType = sp.getSelectedItemId();

        Log.v("MYTAG", "before date");
        TempDate1 = date.getText().toString();
        Log.v("MYTAG", TempDate1);
        Log.v("MYTAG", "after");
        TempDate2 = date1.getText().toString();
        Log.v("MYTAG", TempDate2);
        Log.v("MYTAG", "after");
        Log.v("MYTAG", "before spinner");
        TempType = listValue.get((int) indexType);
        Log.v("MYTAG", TempType);
//        Log.v("MYTAG","after");
        TempTime1 = time.getText().toString();
        TempTime2 = time1.getText().toString();
//        TempImage = ImageView.toString();
        if (bitmap == null) {
            TempImage = null;
        } else {
            TempImage = getStringImage(bitmap);
        }


        Log.v("MYTAG", "after");


    }




    public void InsertData(final String ActivityTitle, final String ActivityDetail,final int Number, final double HourAC,final String DateFrom,
                           final String DateTo, final String TimeFrom, final String TimeTo,final String PicActivity,final String Type,final int TimeAc)
    {

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {



                String TypeHolder = Type ;
                String PicHolder = PicActivity;
                String TitleHolder = ActivityTitle ;
                String DetailHolder = ActivityDetail ;
                int NamHolder = Number;
                double HourHolder = HourAC;
                String DateFromHolder = DateFrom ;
                String  DateToHolder = DateTo ;
                int TimeAcHolder = TimeAc;

                String TimeFromHolder = TimeFrom ;
                String TimeToHolder = TimeTo ;

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

//                nameValuePairs.add(new BasicNameValuePair("Type", TypeHolder));
                nameValuePairs.add(new BasicNameValuePair("ActivityTitle", TitleHolder));
                nameValuePairs.add(new BasicNameValuePair("ActivityDetail", DetailHolder));
                nameValuePairs.add(new BasicNameValuePair("Number", Integer.toString(NamHolder)));
                nameValuePairs.add(new BasicNameValuePair("HourAC",Double.toString(HourHolder)));
                nameValuePairs.add(new BasicNameValuePair("DateFrom", DateFromHolder));
                nameValuePairs.add(new BasicNameValuePair("DateTo", DateToHolder));
                nameValuePairs.add(new BasicNameValuePair("TimeFrom", TimeFromHolder));
                nameValuePairs.add(new BasicNameValuePair("TimeTo", TimeToHolder));
                nameValuePairs.add(new BasicNameValuePair("PicActivity", PicHolder));
                nameValuePairs.add(new BasicNameValuePair("Type", TypeHolder));
                nameValuePairs.add(new BasicNameValuePair("TimeAc",Integer.toString(TimeAcHolder)));
                nameValuePairs.add(new BasicNameValuePair("InstructorID", UserID));



                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(ServerURL);

                     httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));

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

                Toast.makeText(NewActivity.this, "บันทึกเรียบร้อย", Toast.LENGTH_LONG).show();
                Intent newActivity = new Intent(NewActivity.this,ActivityActivity.class);
                startActivity(newActivity);

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();

        sendPostReqAsyncTask.execute(ActivityTitle ,ActivityDetail );
    }




}

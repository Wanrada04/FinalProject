package com.example.project1;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ResultExportSubActivity extends AppCompatActivity  {
    private String TAG = ResultExportSubActivity.class.getSimpleName();
    private ListView listView;
    ArrayList<HashMap<String, String>> contactList;
    SessionManager session;
    private static String ID;
    TextView Countst;
    public static int deletePos;
    private Context context;
    private String[] values;
    private List<Boolean> checked;
    Button export;
    String CourseName;
    String TempSec;
    //    String URL = "http://"+IPConnect.IP_CONNECT+"/android/export_sub.php";
    private static String[] columns = {"fah", "123" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_export_sub);
        contactList = new ArrayList<>();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        new GetPlaces().execute();
//
    }
    private class GetPlaces extends AsyncTask<Void, Void, Void> implements View.OnClickListener {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(ResultExportSubActivity.this, "สรุปผลการเข้าเรียน", Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();

            session = new SessionManager(getApplicationContext());
            HashMap<String, String> user = session.getUserDetails();
            ID = user.get(SessionManager.KEY_ID);
            String TempHolder = getIntent().getStringExtra("CourseID");
             TempSec = getIntent().getStringExtra("Section");
            String url = "http://" + IPConnect.IP_CONNECT + "/android/export_sub.php";
            url += "?InstructorID=" + ID + "&CourseID=" + TempHolder + "&Section=" + TempSec;

            String jsonStr = sh.makeServiceCall(url);
            Log.e(TAG, "Response from url:" + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray result = jsonObj.getJSONArray("result");
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject c = result.getJSONObject(i);
                        String CourseID = c.getString("CourseID");
                         CourseName = c.getString("CourseName");
                        String StudentID = c.getString("StudentID");
                        String StudentName = c.getString("StudentName");
                        String CreateDate = c.getString("CreateDate");
                        HashMap<String, String> contact = new HashMap<>();
                        contact.put("CourseID", CourseID);
                        contact.put("CourseName", CourseName);
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
            export = (Button) findViewById(R.id.btnEx);
            export.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            HSSFWorkbook wb = new HSSFWorkbook();
            Cell cell = null;
            CellStyle cellStyle = wb.createCellStyle();
            cellStyle.setFillForegroundColor(HSSFColor.LIGHT_BLUE.index);
            cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

            //Now we are creating sheet
            Sheet sheet = null;
            sheet = wb.createSheet("สรุปผลการเข้าเรียนรายวิชา"+CourseName);
            //Now column and row

            //Create Header
            Row row = sheet.createRow(0);

            cell = row.createCell(0);
            cell.setCellValue("รหัสวิชา");
            cell.setCellStyle(cellStyle);

            cell = row.createCell(1);
            cell.setCellValue("ชื่อวิชา");
            cell.setCellStyle(cellStyle);

            cell = row.createCell(2);
            cell.setCellValue("รหัสนักศึกษา");
            cell.setCellStyle(cellStyle);

            cell = row.createCell(3);
            cell.setCellValue("ชื่อนักศึกษา");
            cell.setCellStyle(cellStyle);

            cell = row.createCell(4);
            cell.setCellValue("เข้าเรียนเมื่อวันที่และเวลา");
            cell.setCellStyle(cellStyle);

            for(int i=0; i<=contactList.size()-1; i++){
                //loop create detail
                Row rowDetail = sheet.createRow(i+1);

                cell = rowDetail.createCell(0);
                cell.setCellValue(contactList.get(i).get("CourseID"));
//                cell.setCellStyle(cellStyle);

                cell = rowDetail.createCell(1);
                cell.setCellValue(contactList.get(i).get("CourseName"));
//                cell.setCellStyle(cellStyle);

                cell = rowDetail.createCell(2);
                cell.setCellValue(contactList.get(i).get("StudentID"));
//                cell.setCellStyle(cellStyle);

                cell = rowDetail.createCell(3);
                cell.setCellValue(contactList.get(i).get("StudentName"));

                cell = rowDetail.createCell(4);
                cell.setCellValue(contactList.get(i).get("CreateDate"));
//                cell.setCellStyle(cellStyle);

            }



            sheet.setColumnWidth(0, (10 * 200));
            sheet.setColumnWidth(1, (10 * 200));

//            String TempHolder = getIntent().getStringExtra("CourseID");
//            String TempSec = getIntent().getStringExtra("Section");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            File file = new File(getExternalFilesDir(null), "รหัสวิชา "+TempHolder+"ห้อง"+" "+TempSec+df.format(new Date())+".xls");
            File file = new File(getExternalFilesDir(null), "สรุปผลการเข้าเรียนวิชา"+CourseName+" "+"กลุ่ม"+TempSec+" "+df.format(new Date())+".xls");
            FileOutputStream outputStream = null;

            try {
                outputStream = new FileOutputStream(file);
                wb.write(outputStream);
                Toast.makeText(getApplicationContext(), "ดาวน์โหลดเรียบร้อย", Toast.LENGTH_LONG).show();
            } catch (java.io.IOException e) {
                e.printStackTrace();

                Toast.makeText(getApplicationContext(), "ไม่สามารถดาวน์โหลดได้", Toast.LENGTH_LONG).show();
                try {
                    outputStream.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }





//    private void InsertData(final String ID, final String TempHolder, final String TempSec) {
//        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
//            @Override
//            protected String doInBackground(String... params) {
//
//
//                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//
//                nameValuePairs.add(new BasicNameValuePair("InstructorID", ID));
//                nameValuePairs.add(new BasicNameValuePair("CourseID", TempHolder));
//                nameValuePairs.add(new BasicNameValuePair("Section", TempSec));
//
//
//                try {
//                    HttpClient httpClient = new DefaultHttpClient();
//
//                    HttpPost httpPost = new HttpPost(URL);
//
//                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
//
//
//                    HttpResponse httpResponse = httpClient.execute(httpPost);
//
//
//                    InputStream inputStream = httpResponse.getEntity().getContent();
//
//                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//
//                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//
//                    StringBuilder stringBuilder = new StringBuilder();
//
//                    String bufferedStrChunk = null;
//
//                    while((bufferedStrChunk = bufferedReader.readLine()) != null){
//                        stringBuilder.append(bufferedStrChunk);
//                    }
//
//                    return stringBuilder.toString();
//
//
//                } catch (ClientProtocolException e) {
//                    return null;
//
//                } catch (IOException e) {
//                    return null;
//
//                }
//            }
//
//            @Override
//            protected void onPostExecute(String result) {
//                super.onPostExecute(result);
//
//
//
//            }
//        }
//        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
//        sendPostReqAsyncTask.execute(ID,TempHolder,TempSec );
//    }
}


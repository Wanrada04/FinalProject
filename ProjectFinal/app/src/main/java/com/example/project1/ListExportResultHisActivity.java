package com.example.project1;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
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

public class ListExportResultHisActivity extends AppCompatActivity {
    private String TAG = ListExportResultHisActivity.class.getSimpleName();
    private ListView listView;
    ArrayList<HashMap<String, String>> contactList;
    SessionManager session;
    private static String ID;
    String ActivityTitle;
    Button export;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_export_result_his);
        contactList = new ArrayList<>();



        new GetPlaces().execute();
    }
    private class GetPlaces extends AsyncTask<Void, Void, Void> implements View.OnClickListener {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(ListExportResultHisActivity.this, "ประวัติผลการเข้าร่วมกิจกรรม", Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();

            session = new SessionManager(getApplicationContext());
            HashMap<String, String> user = session.getUserDetails();
            ID = user.get(SessionManager.KEY_ID);
            String TempHolder = getIntent().getStringExtra("ActivityID");

            String url = "http://" + IPConnect.IP_CONNECT + "/android/export_activity.php";

            url += "?InstructorID=" + ID + "&ActivityID= " + TempHolder ;

            String jsonStr = sh.makeServiceCall(url);
            Log.e(TAG, "Response from url:" + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray result = jsonObj.getJSONArray("result");
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject c = result.getJSONObject(i);
                        String ActivityID = c.getString("ActivityID");
                         ActivityTitle = c.getString("ActivityTitle");
                        String StudentID = c.getString("StudentID");
                        String StudentName = c.getString("StudentName");
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
            export = (Button) findViewById(R.id.btnExAc);
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
            sheet = wb.createSheet("สรุปผลการเข้าเรียนรายวิชา");
            //Now column and row

            //Create Header
            Row row = sheet.createRow(0);

            cell = row.createCell(0);
            cell.setCellValue("รหัสกิจกรรม");
            cell.setCellStyle(cellStyle);

            cell = row.createCell(1);
            cell.setCellValue("ชื่อกิจกรรม");
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
                cell.setCellValue(contactList.get(i).get("ActivityID"));
//                cell.setCellStyle(cellStyle);

                cell = rowDetail.createCell(1);
                cell.setCellValue(contactList.get(i).get("ActivityTitle"));
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

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            File file = new File(getExternalFilesDir(null), "สรุปผลการเข้ากิจกรรม"+ActivityTitle+" "+df.format(new Date())+".xls");
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

}

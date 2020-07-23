package com.example.project1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class ExportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);
        String str[]  = { "รายวิชา", "กิจกรรม" };

        ListView listView1 = (ListView)findViewById(R.id.list_export);
        listView1.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, str));

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch(position) {
                    case 0 :
                        Intent newActivity = new Intent(getApplicationContext(),Export_Sec_Activity.class);
                        startActivity(newActivity);
                        break;
                    case 1 :
                        Intent intent = new Intent(getApplicationContext(),ListExportAcActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
    }
}

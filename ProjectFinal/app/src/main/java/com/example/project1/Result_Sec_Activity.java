package com.example.project1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class Result_Sec_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result__sec_);
        String str[]  = { "Section 1 ", "Section 2" };

        ListView listView1 = (ListView)findViewById(R.id.listViewSec);
        listView1.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, str));

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent;
                switch(position) {
                    case 0 :
                        Intent newActivity = new Intent(getApplicationContext(), ListResultSubActivity.class);
                        newActivity.putExtra("Section","1");
                        startActivity(newActivity);
                        break;
                    case 1 :
                        intent = new Intent(getApplicationContext(), ListResultSubActivity.class);
                        intent.putExtra("Section","2");
                        startActivity(intent);
                        break;
                }
            }
        });
    }
}

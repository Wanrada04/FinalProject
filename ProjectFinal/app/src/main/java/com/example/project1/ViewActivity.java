package com.example.project1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import static java.lang.Integer.valueOf;

public class ViewActivity extends AppCompatActivity {
    TextView txtView,txtDateTo,txtDateFrom,txtTimeTo,txtTimeFrom,txtHc,txtNum,txtDetail,txtPic,txtType,textName;
    ImageView ImageView;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);


        txtView = (TextView) findViewById(R.id.Viewtitle);
        txtDateTo = (TextView) findViewById(R.id.ViewDateto);
        txtDateFrom = (TextView) findViewById(R.id.ViewDateFrom);
        txtTimeTo = (TextView) findViewById(R.id.ViewTimeTo);
        txtTimeFrom = (TextView) findViewById(R.id.ViewTimeFrom);
        txtHc = (TextView) findViewById(R.id.ViewHC);
        txtNum = (TextView) findViewById(R.id.ViewNum);
        ImageView = (ImageView) findViewById(R.id.ImgView);
        txtType = (TextView) findViewById(R.id.Viewtype);
        txtDetail = (TextView) findViewById(R.id.ViewDetail);
        textName = (TextView) findViewById(R.id.Viewtec);


        Intent intent = getIntent();;
        String TempHolder = getIntent().getStringExtra("ActivityTitle");
        String TempDateTo = getIntent().getStringExtra("DateTo");
        String TempDateFrom = getIntent().getStringExtra("DateFrom");
        String TempTimeTo = getIntent().getStringExtra("TimeTo");
        String TempTimeFrom = getIntent().getStringExtra("TimeFrom");
        String TempHC = getIntent().getStringExtra("HourAC");
        String TempNum = getIntent().getStringExtra("Number");
        String TempDetail = getIntent().getStringExtra("ActivityDetail");
        String TempType = getIntent().getStringExtra("Type");
        String TempTypeName = getIntent().getStringExtra("TypeName");
        String TempPic = getIntent().getStringExtra("PicActivity");
        String TempName = getIntent().getStringExtra("InstructorName");
        Log.v(TempPic,"after");

        String PicActivity =TempPic;
        byte decodedString[]= android.util.Base64.decode(PicActivity, android.util.Base64.DEFAULT);
        Bitmap bmp = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

//        String clearUrl = TempPic.substring(TempPic.indexOf(",")+1);
//        byte[] decodingString = Base64.decode(clearUrl, Base64.DEFAULT);
//        Bitmap bitmap = BitmapFactory.decodeByteArray(decodingString, 0 , decodingString.length);
//
//      ImageView showImage = (ImageView) findViewById(R.id.movieImg);
//        showImage.setImageBitmap(bitmap);

        int diff= valueOf(TempHC);

        String minutes = Integer.toString(diff % 60);
        minutes = minutes.length() == 1 ? "0" + minutes : minutes;
        String sdifTime= (diff / 60) + ":" + minutes;

        textName.setText(TempName);
        txtType.setText(TempTypeName);
        ImageView.setImageBitmap(bmp);
//
//        String minutes = Double.toString((double)TempHC % 60);
//        minutes = minutes.length() == 1 ? "0" + minutes : minutes;
//        String sdifTime= ((Double)TempHC / 60) + ":" + minutes;
//        //String time = df.format(new Date((int)diff * 60 * 1000L));

        txtHc.setText(sdifTime);
//        txtNum.setText(TempNum);
//        txtHc.setText(TempHC);
        txtNum.setText(TempNum);
        txtTimeTo.setText(TempTimeTo);
        txtTimeFrom.setText(TempTimeFrom);
        txtDateTo.setText(TempDateTo);
        txtDateFrom.setText(TempDateFrom);
        txtDetail.setText(TempDetail);
        txtView.setText(TempHolder);




    }
}

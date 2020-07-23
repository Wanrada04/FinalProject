//package com.example.project1;
//
//import android.app.Activity;
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.AsyncTask;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//
//import java.io.InputStream;
//import java.util.List;
//
///**
// * Created by jaiso on 13-02-2018.
// */
//
//
//public class CustomListView extends ArrayAdapter<String>{
//
//    private String[] CourseID;
//    private String[] CourseName;
//    private Activity context;
//    Bitmap bitmap;
//
//    public CustomListView(Activity context,String[] CourseID,String[] CourseName) {
//        super(context, R.layout.list_row);
//        this.context=context;
//        this.CourseID=CourseID;
//        this.CourseName=CourseName;
//
//    }
//
//    @NonNull
//    @Override
//
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
//        View r=convertView;
//        ViewHolder viewHolder=null;
//        if(r==null){
//            LayoutInflater layoutInflater=context.getLayoutInflater();
//            r=layoutInflater.inflate(R.layout.list_row,null,true);
//            viewHolder=new ViewHolder(r);
//            r.setTag(viewHolder);
//        }
//        else {
//            viewHolder=(ViewHolder)r.getTag();
//
//        }
//
//        viewHolder.tvw1.setText(CourseID[position]);
//        viewHolder.tvw2.setText(CourseName[position]);
//
//        return r;
//    }
//
//    class ViewHolder{
//
//        TextView tvw1;
//        TextView tvw2;
//
//
//        ViewHolder(View v){
//            tvw1=(TextView)v.findViewById(R.id.textSub);
//            tvw2=(TextView)v.findViewById(R.id.textViewSub);
//
//        }
//
//    }
//
//    public class GetImageFromURL extends AsyncTask<String,Void,Bitmap>
//    {
//
//        @Override
//        protected Bitmap doInBackground(String... url) {
//            String urldisplay=url[0];
//            bitmap=null;
//
//            try{
//
//                InputStream ist=new java.net.URL(urldisplay).openStream();
//                bitmap= BitmapFactory.decodeStream(ist);
//            }
//            catch (Exception ex)
//            {
//                ex.printStackTrace();
//            }
//
//            return bitmap;
//        }
//
//        @Override
//        protected void onPostExecute(Bitmap bitmap){
//
//            super.onPostExecute(bitmap);
//
//        }
//    }
//
//
//}

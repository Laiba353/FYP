package com.example.chatting;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class review extends AppCompatActivity {
EditText et;
ListView lv;
    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    Activity activity;
    ArrayList<ReviewList> rev=new ArrayList<ReviewList>();
    String str,str1,str2;
    String lang;
    Context context;
    Resources resources;
    String languages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        Intent intent=getIntent();
        str1=intent.getStringExtra("milkman");
        str2=intent.getStringExtra("customer");
        languages= intent.getExtras().getString("language");

        et=(EditText)findViewById(R.id.edt);

        if(languages.equals("ENGLISH"))
        {

            context = LocalHelper.setLocale(review.this, "en");
            resources = context.getResources();
            et.setText(resources.getString(R.string.typereview));
            getSupportActionBar().setTitle("Reviews");

            lang="ENGLISH";
        }
        if(languages.equals("اردو"))
        {
            context = LocalHelper.setLocale(review.this, "an");
            resources = context.getResources();
            et.setText(resources.getString(R.string.typereview));
            getSupportActionBar().setTitle("جائزے");
            lang="اردو";

        }





        activity = this;
        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getReadableDatabase();
        lv = (ListView) findViewById(R.id.list);
        String[] colm={DatabaseContract.Review.COL_REVIEW};
        Cursor cr=db.query(DatabaseContract.Review.TABLE_NAME,colm,"PlacedBy = ? And PlacedTo = ?", new String[] {str2,str1}
                , null, null, null, null);
        if (cr.getCount()==0) {
            Toast.makeText(getApplicationContext(),"No Record exist",Toast.LENGTH_LONG).show();
        }
        else
        {
            rev.clear();
            while(cr.moveToNext())
            {
                str=cr.getString(0);
                ReviewList mObj = new ReviewList(str);
                rev.add(mObj);
            }
            reviewHolder rList = new reviewHolder(activity, rev);


            lv.setAdapter(rList);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                    Toast.makeText(getApplicationContext(), "You Selected " + rev.get(position).getName() + " as Country", Toast.LENGTH_LONG).show();
                }
            });
        }
        db.close();


    }
    public void onbtn(View v)
    {

        if(et.getText().toString().equals(" "))
        {
            Toast.makeText(getApplicationContext(), "Please Enter Your Review", Toast.LENGTH_LONG).show();
        }
        else {
            db = dbHelper.getWritableDatabase();




            str=et.getText().toString();
            ReviewList mObj = new ReviewList(str);
            rev.add(mObj);
            reviewHolder rList = new reviewHolder(activity, rev);
            lv.setAdapter(rList);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                    Toast.makeText(getApplicationContext(), "You Selected " + rev.get(position).getName() + " as Country", Toast.LENGTH_LONG).show();
                }
            });
            ContentValues values = new ContentValues();
            values.put(DatabaseContract.Review.COL_PLACED_BY, str2);
            values.put(DatabaseContract.Review.COL_PLACED_TO, str1);
            values.put(DatabaseContract.Review.COL_REVIEW, str);
            long newRowId = db.insert(DatabaseContract.Review.TABLE_NAME, null, values);
            if (newRowId > 0) {
                Toast.makeText(getApplicationContext(), "New Record Inserted: " + newRowId, Toast.LENGTH_LONG).show();
            }
            db.close();

     et.setText(" ");
        }
    }
}
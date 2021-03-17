package com.example.chatting;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MilkManList2 extends AppCompatActivity {
    ListView lv;
    ArrayList<MilkMan> arrayList=new ArrayList<>();
    DatabaseHelper dbh;
    SQLiteDatabase db;
    Activity activity;
    String str,s1,s2,s3, s4;
    private ProgressDialog progressDialog;
    TextView tv;
    MenuItem chatwithothers, ordershistory;

    Context context;
    Resources resources;
    String str1;
    String languages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_milk_man_list2);
        activity = this;
        dbh = new DatabaseHelper(this);
        Intent inten = getIntent();
        str = inten.getStringExtra("val");
        languages = inten.getExtras().getString("language");
        Toast.makeText(getApplicationContext(), "Record id" + str, Toast.LENGTH_LONG).show();
        db = dbh.getReadableDatabase();
        tv = (TextView) findViewById(R.id.txt);
        if(languages.equals("ENGLISH")) {

            context = LocalHelper.setLocale(MilkManList2.this, "en");
            resources = context.getResources();
            tv.setText(resources.getString(R.string.milkmans1));
            str1="ENGLISH";
        }

        if(languages.equals("اردو")) {

            context = LocalHelper.setLocale(MilkManList2.this, "an");
            resources = context.getResources();
            tv.setText(resources.getString(R.string.milkmans1));
            str1="اردو";
        }



        //move activity
        String[] colm = {DatabaseContract.MilkMan._ID, DatabaseContract.MilkMan.COL_NAME, DatabaseContract.MilkMan.COL_LOCATION, DatabaseContract.MilkMan.COL_QUALITY};
        Cursor c = db.query(DatabaseContract.MilkMan.TABLE_NAME, colm, null, null
                , null, null, null, null);
        if (c.getCount() > 0) {

            Toast.makeText(getApplicationContext(), "No Record exist", Toast.LENGTH_LONG).show();
            if(languages.equals("ENGLISH")) {

                context = LocalHelper.setLocale(MilkManList2.this, "en");
                resources = context.getResources();
                while (c.moveToNext()) {
                    long id = c.getLong(0);
                    s1 = c.getString(1);
                    s2 = c.getString(2);
                    s4 = c.getString(3);
                    s3 = String.valueOf(id);
                    MilkMan mObj = new MilkMan(s1, "Category : " + s4 + ", Loc : " + s2, s3);
                    arrayList.add(mObj);

                }


                lv = (ListView) findViewById(R.id.list1);
                milk1 customList = new milk1(activity, arrayList);


                lv.setAdapter(customList);

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        String ss = arrayList.get(position).getOrderNo();
                        String ss2 = arrayList.get(position).getName();
                        Intent intent = new Intent(MilkManList2.this, othersReviews.class);
                        intent.putExtra("val", ss);
                        intent.putExtra("val2", ss2);
                        intent.putExtra("language",str1);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "You Selected " + arrayList.get(position).getName() + " as Country", Toast.LENGTH_LONG).show();
                    }
                });

            }
            if(languages.equals("اردو")) {

                context = LocalHelper.setLocale(MilkManList2.this, "an");
                resources = context.getResources();
                while (c.moveToNext()) {
                    long id = c.getLong(0);
                    s1 = c.getString(1);
                    s2 = c.getString(2);
                    s4 = c.getString(3);
                    s3 = String.valueOf(id);
                    MilkMan mObj = new MilkMan(s1, "دودھ کی قسم : " + s4 + ", جگہ : " + s2, s3);
                    arrayList.add(mObj);

                }


                lv = (ListView) findViewById(R.id.list1);
                milk1 customList = new milk1(activity, arrayList);


                lv.setAdapter(customList);

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        String ss = arrayList.get(position).getOrderNo();
                        String ss2 = arrayList.get(position).getName();
                        Intent intent = new Intent(MilkManList2.this, othersReviews.class);
                        intent.putExtra("val", ss);
                        intent.putExtra("val2", ss2);

                        intent.putExtra("language",str1);

                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "You Selected " + arrayList.get(position).getName() + " as Country", Toast.LENGTH_LONG).show();
                    }
                });


            }


        } else {


            if(languages.equals("ENGLISH")) {

                context = LocalHelper.setLocale(MilkManList2.this, "en");
                resources = context.getResources();
                tv.setText(resources.getString(R.string.nomilkmans1));
                str1="ENGLISH";
            }

            if(languages.equals("اردو")) {

                context = LocalHelper.setLocale(MilkManList2.this, "an");
                resources = context.getResources();
                tv.setText(resources.getString(R.string.nomilkmans1));
                str1="اردو";
            }
        }


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main1, menu);

        chatwithothers = menu.findItem(R.id.Chat);
        ordershistory= menu.findItem(R.id.Ordersh);
        if(languages.equals("ENGLISH"))
        {

            context = LocalHelper.setLocale(MilkManList2.this, "en");
            resources = context.getResources();
            chatwithothers.setTitle(resources.getString(R.string.chatwithothers));
            ordershistory.setTitle(resources.getString(R.string.ordershistory));
            str1="ENGLISH";

        }

        if(languages.equals("اردو"))
        {

            context = LocalHelper.setLocale(MilkManList2.this, "an");
            resources = context.getResources();
            chatwithothers.setTitle(resources.getString(R.string.chatwithothers));
            ordershistory.setTitle(resources.getString(R.string.ordershistory));
            str1="اردو";

        }


        return true;




    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.Ordersh:
                Toast.makeText(getApplicationContext(),"Record id"+str,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, CustomerHistory.class);
                intent.putExtra("language",str1);
                intent.putExtra("var", str);

                startActivity(intent);
                return true;
            case R.id.Chat:

                Intent inten = new Intent(this, PhoneNumberActivity.class);
                //inten.putExtra("var", str);
                inten.putExtra("language",str1);
                startActivity(inten);
                return true;
            default:
                return super.onOptionsItemSelected(item);


        }
    }
}
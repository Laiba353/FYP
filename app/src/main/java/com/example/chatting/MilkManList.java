package com.example.chatting;

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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MilkManList extends AppCompatActivity {
    ListView lv;
    ArrayList<MilkMan> arrayList=new ArrayList<>();
    DatabaseHelper dbh;
    SQLiteDatabase db;
    Activity activity;
    String str,s1,s2,s3, s4;
    MenuItem chatwithothers, ordershistory;
    EditText edt,searchbar;
    ImageButton img;
    private ProgressDialog progressDialog;
    TextView tv;
    String lang;
    Context context;
    Resources resources;
    String languages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_milk_man_list);
        activity = this;
        dbh = new DatabaseHelper(this);
        Intent inten = getIntent();
        str = inten.getStringExtra("val");
        languages= inten.getExtras().getString("language");
        img=findViewById(R.id.search);
        db = dbh.getReadableDatabase();
        tv = (TextView) findViewById(R.id.txt);
        edt=(EditText) findViewById(R.id.searchbar);


        Context context;
        Resources resources;
        if(languages.equals("ENGLISH"))
        {

            context = LocalHelper.setLocale(MilkManList.this, "en");
            resources = context.getResources();
            tv.setText(resources.getString(R.string.ListOfMilkMans));
            edt.setHint(resources.getString(R.string.searchbylocation));
            lang="ENGLISH";
        }
        if(languages.equals("اردو"))
        {
            context = LocalHelper.setLocale(MilkManList.this, "an");
            resources = context.getResources();
            tv.setText(resources.getString(R.string.ListOfMilkMans));
            edt.setHint(resources.getString(R.string.searchbylocation));
            lang="اردو";

        }


        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // db = dbh.getReadableDatabase();
                Toast.makeText(MilkManList.this, "message: "+edt.getText().toString(), Toast.LENGTH_LONG).show();
                if (!edt.getText().toString().equals(" ")) {
                    String strs = edt.getText().toString();
                    String[] colm = {DatabaseContract.MilkMan._ID, DatabaseContract.MilkMan.COL_NAME, DatabaseContract.MilkMan.COL_LOCATION, DatabaseContract.MilkMan.COL_QUALITY};
                    Cursor c = db.query(DatabaseContract.MilkMan.TABLE_NAME, colm, DatabaseContract.MilkMan.COL_LOCATION+"=?", new String[] { strs}
                            , null, null, null, null);
                    if (c.getCount() > 0) {

                        if(languages.equals("ENGLISH")) {
                            arrayList.clear();
                            while (c.moveToNext()) {

                                long id = c.getLong(0);
                                s1 = c.getString(1);
                                s2 = c.getString(2);
                                s4 = c.getString(3);
                                s3 = String.valueOf(id);
                                Toast.makeText(MilkManList.this, "message222: " + edt.getText().toString(), Toast.LENGTH_LONG).show();


                                MilkMan mObj = new MilkMan(s1, "Category : " + s4 + ", Loc : " + s2, s3);

                                arrayList.add(mObj);


                            }

                            milk1 customList = new milk1(activity, arrayList);


                            lv.setAdapter(customList);

                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                    String ss = arrayList.get(position).getOrderNo();
                                    Intent intent = new Intent(MilkManList.this, MilkManDetails.class);
                                    intent.putExtra("val", ss);
                                    intent.putExtra("val2", str);
                                    intent.putExtra("language",lang);
                                    startActivity(intent);
                                    Toast.makeText(getApplicationContext(), "You Selected " + arrayList.get(position).getName() + " as Country", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        if(languages.equals("اردو")) {
                            arrayList.clear();
                            while (c.moveToNext()) {

                                long id = c.getLong(0);
                                s1 = c.getString(1);
                                s2 = c.getString(2);
                                s4 = c.getString(3);
                                s3 = String.valueOf(id);
                                Toast.makeText(MilkManList.this, "message222: " + edt.getText().toString(), Toast.LENGTH_LONG).show();


                                MilkMan mObj = new MilkMan(s1, "قسم : " + s4 + ", جگہ : " + s2, s3);

                                arrayList.add(mObj);


                            }

                            milk1 customList = new milk1(activity, arrayList);


                            lv.setAdapter(customList);

                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                    String ss = arrayList.get(position).getOrderNo();
                                    Intent intent = new Intent(MilkManList.this, MilkManDetails.class);
                                    intent.putExtra("val", ss);
                                    intent.putExtra("val2", str);
                                    startActivity(intent);
                                    Toast.makeText(getApplicationContext(), "You Selected " + arrayList.get(position).getName() + " as Country", Toast.LENGTH_LONG).show();
                                }
                            });
                        }


                    }
                    else {
                        if(languages.equals("ENGLISH")) {
                            tv.setText("No MilkMan Found ");
                        }
                        if(languages.equals("اردو")){
                            tv.setText("کوئی دودھ فروش دستیاب نہیں ہے ");
                        }
                        Toast.makeText(MilkManList.this, "No MilkMan Found", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(MilkManList.this, "Please Enter Any Location ", Toast.LENGTH_LONG).show();
                }
            }
        });
        Toast.makeText(getApplicationContext(), "Record id" + str, Toast.LENGTH_LONG).show();


        lv = (ListView) findViewById(R.id.list1);
        //move activity

        String[] colm = {DatabaseContract.MilkMan._ID, DatabaseContract.MilkMan.COL_NAME, DatabaseContract.MilkMan.COL_LOCATION, DatabaseContract.MilkMan.COL_QUALITY};
        Cursor c = db.query(DatabaseContract.MilkMan.TABLE_NAME, colm, null, null
                , null, null, null, null);
        if (c.getCount() > 0) {
            if(languages.equals("ENGLISH")) {


                Toast.makeText(getApplicationContext(), "No Record exist", Toast.LENGTH_LONG).show();


                while (c.moveToNext()) {
                    long id = c.getLong(0);
                    s1 = c.getString(1);
                    s2 = c.getString(2);
                    s4 = c.getString(3);
                    s3 = String.valueOf(id);

                    MilkMan mObj = new MilkMan(s1, "Category : " + s4 + ", Loc : " + s2, s3);
                    arrayList.add(mObj);

                }

                milk1 customList = new milk1(activity, arrayList);
                lv.setAdapter(customList);

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        String ss = arrayList.get(position).getOrderNo();
                        Intent intent = new Intent(MilkManList.this, MilkManDetails.class);
                        intent.putExtra("val", ss);
                        intent.putExtra("val2", str);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "You Selected " + arrayList.get(position).getName() + " as Country", Toast.LENGTH_LONG).show();
                    }
                });

            }
            if(languages.equals("اردو")) {


                Toast.makeText(getApplicationContext(), "No Record exist", Toast.LENGTH_LONG).show();


                while (c.moveToNext()) {
                    long id = c.getLong(0);
                    s1 = c.getString(1);
                    s2 = c.getString(2);
                    s4 = c.getString(3);
                    s3 = String.valueOf(id);

                    MilkMan mObj = new MilkMan(s1, "قسم : " + s4 + ", جگہ : " + s2, s3);
                    arrayList.add(mObj);

                }

                milk1 customList = new milk1(activity, arrayList);
                lv.setAdapter(customList);

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        String ss = arrayList.get(position).getOrderNo();
                        Intent intent = new Intent(MilkManList.this, MilkManDetails.class);
                        intent.putExtra("val", ss);
                        intent.putExtra("val2", str);
                        intent.putExtra("language",lang);
                        startActivity(intent);
                    }
                });

            }

        } else {

            if(languages.equals("ENGLISH")) {
                tv.setText("No MilkMan Found ");
            }
            if(languages.equals("اردو")){
                tv.setText("کوئی دودھ فروش دستیاب نہیں ہے ");
            }
            Toast.makeText(MilkManList.this, "No Record exist", Toast.LENGTH_LONG).show();
        }


    }


    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main1, menu);
        chatwithothers = menu.findItem(R.id.Chat);
        ordershistory= menu.findItem(R.id.Ordersh);
        if(languages.equals("ENGLISH"))
        {

            context = LocalHelper.setLocale(MilkManList.this, "en");
            resources = context.getResources();
            chatwithothers.setTitle(resources.getString(R.string.chatwithothers));
            ordershistory.setTitle(resources.getString(R.string.ordershistory));


        }

        if(languages.equals("اردو"))
        {

            context = LocalHelper.setLocale(MilkManList.this, "an");
            resources = context.getResources();
            chatwithothers.setTitle(resources.getString(R.string.chatwithothers));
            ordershistory.setTitle(resources.getString(R.string.ordershistory));


        }


        return true;


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.Ordersh:
                Toast.makeText(getApplicationContext(),"Record id"+str,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MilkManList.this, CustomerHistory.class);
                intent.putExtra("language",lang);
                intent.putExtra("var", str);

                startActivity(intent);
                return true;
            case R.id.Chat:

                Intent inten = new Intent(this, PhoneNumberActivity.class);
                //inten.putExtra("var", str);
                startActivity(inten);
                inten.putExtra("language",lang);
                return true;
            default:
                return super.onOptionsItemSelected(item);


        }
    }
   /* public void Onsearch(View v)
    {
        db = dbh.getReadableDatabase();
        Toast.makeText(MilkManList.this, "message: "+edt.getText().toString(), Toast.LENGTH_LONG).show();
       if (edt.getText().toString().equals(" ")) {
            String str = edt.getText().toString();
            String[] colm = {DatabaseContract.MilkMan._ID, DatabaseContract.MilkMan.COL_NAME, DatabaseContract.MilkMan.COL_LOCATION, DatabaseContract.MilkMan.COL_QUALITY};
            Cursor c = db.query(DatabaseContract.MilkMan.TABLE_NAME, colm, DatabaseContract.MilkMan.COL_LOCATION+"=?", new String[] { str}
                    , null, null, null, null);
            if (c.getCount() > 0) {
                arrayList.clear();
                while (c.moveToNext()) {



                    long id = c.getLong(0);
                    s1 = c.getString(1);
                    s2 = c.getString(2);
                    s4 = c.getString(3);
                    s3 = String.valueOf(id);
                    MilkMan mObj = new MilkMan(s1, "Category : " + s4 + ", Loc : " + s2, s3);
                    arrayList.add(mObj);

                }

                milk1 customList = new milk1(activity, arrayList);


                lv.setAdapter(customList);

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        String ss = arrayList.get(position).getOrderNo();
                        Intent intent = new Intent(MilkManList.this, MilkManDetails.class);
                        intent.putExtra("val", ss);
                        intent.putExtra("val2", str);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "You Selected " + arrayList.get(position).getName() + " as Country", Toast.LENGTH_LONG).show();
                    }
                });

            }
            else {

                tv.setText("No MilkMan Found ");
                Toast.makeText(MilkManList.this, "No MilkMan Found", Toast.LENGTH_LONG).show();
            }
        }
        else{

        }
    }*/
}
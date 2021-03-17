package com.example.chatting;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MilkManDetails extends AppCompatActivity {
    TextView t1,t2,t3,t4,t5,t6,name,location,quality,quantily,price,heading,number;
    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    String str,str2,s1,s2,s3,s4,s5,s6;
    String lang;
    Context context;
    Resources resources;
    String languages;
    Button givereview, placeorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_milk_man_details);

        Intent intent=getIntent();
        str=intent.getStringExtra("val");//milkman id
        str2=intent.getStringExtra("val2");//customer id
        languages= intent.getExtras().getString("language");
        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getReadableDatabase();
        t1=(TextView)findViewById(R.id.txt1);
        t2=(TextView)findViewById(R.id.txt2);
        t3=(TextView)findViewById(R.id.txt3);
        t4=(TextView)findViewById(R.id.txt4);
        t5=(TextView)findViewById(R.id.txt5);
        t6=(TextView)findViewById(R.id.txt6);
        givereview=findViewById(R.id.givereview);
        placeorder=findViewById(R.id.placeorder);
        name=findViewById(R.id.name);
        location=findViewById(R.id.location);
        number=findViewById(R.id.number);
        quality=findViewById(R.id.milkquality);
        quantily=findViewById(R.id.milkquantity);
        price=findViewById(R.id.milkprice);
        heading=findViewById(R.id.milkmandetail);





        if(languages.equals("ENGLISH"))
        {

            context = LocalHelper.setLocale(MilkManDetails.this, "en");
            resources = context.getResources();
            heading.setText(resources.getString(R.string.milkmandetail));
            name.setText(resources.getString(R.string.named));
            location.setText(resources.getString(R.string.locationd));
            number.setText(resources.getString(R.string.numberd));
            quantily.setText(resources.getString(R.string.quantityd));
            quality.setText(resources.getString(R.string.qualityd));
            price.setText(resources.getString(R.string.priced));
            placeorder.setText(resources.getString(R.string.placeorder));
            givereview.setText(resources.getString(R.string.givereview));
            lang="ENGLISH";
        }
        if(languages.equals("اردو"))
        {
            context = LocalHelper.setLocale(MilkManDetails.this, "an");
            resources = context.getResources();
            heading.setText(resources.getString(R.string.milkmandetail));
            name.setText(resources.getString(R.string.named));
            location.setText(resources.getString(R.string.locationd));
            number.setText(resources.getString(R.string.numberd));
            quantily.setText(resources.getString(R.string.quantityd));
            quality.setText(resources.getString(R.string.qualityd));
            price.setText(resources.getString(R.string.priced));
            placeorder.setText(resources.getString(R.string.placeorder));
            givereview.setText(resources.getString(R.string.givereview));
            lang="اردو";

        }
        String[] colm={DatabaseContract.MilkMan.COL_NAME, DatabaseContract.MilkMan.COL_LOCATION, DatabaseContract.MilkMan.COL_CONTACT,
       DatabaseContract.MilkMan.COL_QUALITY, DatabaseContract.MilkMan.COL_QUANTITY, DatabaseContract.MilkMan.COL_PRICE};
        Cursor c = db.query(DatabaseContract.MilkMan.TABLE_NAME,colm, DatabaseContract.MilkMan._ID + "=?", new String[] {str}
                , null, null, null, null);
        if (c.getCount()==0) {
            Toast.makeText(getApplicationContext(),"No Record exist",Toast.LENGTH_LONG).show();
        }
        c.moveToFirst();
        s1=c.getString(0);
        s2=c.getString(1);
        s3=c.getString(2);
        s4=c.getString(3);
        long qnt=c.getLong(4);
        long prc=c.getLong(5);
        db.close();
        s5=String.valueOf(qnt);
        s6=String.valueOf(prc);
        t1.setText(s1);
t2.setText(s2);
t3.setText(s3);
t4.setText(s4);
t5.setText(s5);
t6.setText(s6);
    }
    public void onplace(View v)
    {
Intent intn=new Intent(this,MapsActivity.class);
intn.putExtra("milkman",str);
intn.putExtra("customer",str2);
intn.putExtra("milkmanL",s2);
intn.putExtra("language",lang);
startActivity(intn);
    }
    public void onReview(View v)
    {
        Intent intnte=new Intent(this,review.class);
        intnte.putExtra("milkman",str);
        intnte.putExtra("customer",str2);
        intnte.putExtra("language",lang);
        startActivity(intnte);
    }
}
package com.example.chatting;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity2 extends AppCompatActivity {
    Button milkman,customer,owner,rider;
    TextView txt1,txt2;
    Context context;
    Resources resources;
    String str;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        milkman=findViewById(R.id.milkman);
        customer=findViewById(R.id.customer);
        owner=findViewById(R.id.owner);
        rider=findViewById(R.id.rider);
        txt1=findViewById(R.id.txt1);
        txt2=findViewById(R.id.txt2);


        Intent intent = getIntent();
        String languages = intent.getExtras().getString("language");
        Toast.makeText(this, languages, Toast.LENGTH_SHORT).show();
        if(languages.equals("ENGLISH"))
        {

            context = LocalHelper.setLocale(MainActivity2.this, "en");
            resources = context.getResources();
            txt1.setText(resources.getString(R.string.heading));
            txt2.setText(resources.getString(R.string.question));
            milkman.setText(resources.getString(R.string.milkman));
            customer.setText(resources.getString(R.string.customer));
            rider.setText(resources.getString(R.string.rider));
            owner.setText(resources.getString(R.string.owner));


            str="ENGLISH";




        }

        if(languages.equals("اردو"))
        {
            context = LocalHelper.setLocale(MainActivity2.this, "an");
            resources = context.getResources();
            txt1.setText(resources.getString(R.string.heading));
            txt2.setText(resources.getString(R.string.question));
            milkman.setText(resources.getString(R.string.milkman));
            customer.setText(resources.getString(R.string.customer));
            rider.setText(resources.getString(R.string.rider));
            owner.setText(resources.getString(R.string.owner));

            str="اردو";


        }



    }
    public void Login(View v)
    {
        String check;
        if(v.getId()==R.id.milkman)
        {
            check="MilkMan";
        }
        else if(v.getId()==R.id.customer)
        {
            check="Customer";
        }
        else
        {
            check="Rider";
        }
        Intent intent = new Intent(MainActivity2.this, LogInPage.class);
        intent.putExtra("Check",check);
        intent.putExtra("language",str);
        startActivity(intent);
    }
    public void Onowner(View v)
    {
        Intent intent = new Intent(MainActivity2.this, OwnerMain.class);
        startActivity(intent);
    }



}
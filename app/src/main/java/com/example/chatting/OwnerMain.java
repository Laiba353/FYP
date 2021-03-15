package com.example.chatting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class OwnerMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_main);
    }
    public void MilkmanList(View v)
    {
        Intent intent=new Intent(this,MilkManList2.class);
        intent.putExtra("val","Owner");
        startActivity(intent);
    }
    public void orederHistory(View v)
    {
        Intent intent=new Intent(this,orders.class);
        startActivity(intent);
    }
}
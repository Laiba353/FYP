package com.example.chatting;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class orderlist extends AppCompatActivity {
    Activity activity;
    TextView tv;
    ArrayList<history1> customer;
    private ProgressDialog progressDialog;
    ListView listView;
    String str1,str2,str3,str4,str5;
    String RiderId;
 OrderListHolder customhList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderlist);
    Intent intent=getIntent();
    RiderId=intent.getStringExtra("val");
        tv=(TextView)findViewById(R.id.Text);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("Orderlocation");
        listView=(ListView)findViewById(R.id.list);
        activity = this;
        customer=new ArrayList<history1>();


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot h1: snapshot.getChildren()) {
                //history1 h1=snapshot1.getValue(history1.class);
                history1 mObj = new history1( ""+h1.child("CustomerContact").getValue(),""+h1.child("MilkmanLoc").getValue() ,   ""+h1.child("CustomerName").getValue() , ""+h1.child("DropOffLoc").getValue());
                    customer.add(mObj);


                }
                if(!customer.isEmpty())
                {
                customhList = new OrderListHolder(activity,customer);
                listView.setAdapter(customhList);
                customhList.notifyDataSetChanged();

                }else
                {
                    tv.setText("No Customer Here ");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
      //  customhList = new history2(activity,customer);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String pickUp=customer.get(position).getQuantity();
                String dropOff=customer.get(position).getPrice();
                String cn=customer.get(position).getOrderNo();
                String mContact=customer.get(position).getName();
                Toast.makeText(getApplicationContext(),"You Selected "+pickUp+ " as Country", Toast.LENGTH_LONG).show();

                Intent intent=new Intent(orderlist.this, MapsActivity1.class);
                intent.putExtra("PickUp",pickUp);
                intent.putExtra("DropOff",dropOff);
                intent.putExtra("RiderId",RiderId);
                intent.putExtra("customer",cn);
                intent.putExtra("Contact",mContact);
                startActivity(intent);
                Toast.makeText(getApplicationContext(),"You Selected "+customer.get(position).getName()+ " as Country", Toast.LENGTH_LONG).show();        }
        });
    }
}
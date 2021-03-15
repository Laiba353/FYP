package com.example.chatting;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

public class OrderPage extends AppCompatActivity {
String str,str1;
EditText ed1,ed2,ed3;
DatabaseHelper dbh;
SQLiteDatabase db;
double prc;
double distance;
    String pickup,dropoff;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_page);
        Intent intn=getIntent();
        str=intn.getStringExtra("milkman");
        str1=intn.getStringExtra("customer");
        pickup=intn.getStringExtra("PickUp");
        dropoff=intn.getStringExtra("DropOff");
        distance=intn.getDoubleExtra("Distance",0.00);
        dbh=new DatabaseHelper(this);
        ed1=(EditText)findViewById(R.id.edt1);
        ed2=(EditText)findViewById(R.id.edt2);
        ed3=(EditText)findViewById(R.id.edt3);
    }
    public void onprice(View v)
    {
        db=dbh.getReadableDatabase();
        int qnt=Integer.parseInt(ed1.getText().toString());
        String[] colm={DatabaseContract.MilkMan.COL_PRICE};
        Cursor cr=db.query(DatabaseContract.MilkMan.TABLE_NAME,colm,DatabaseContract.MilkMan._ID+"=?", new String[] {str}
                , null, null, null, null);
        if (cr.getCount()==0) {
            Toast.makeText(getApplicationContext(),"No Record exist",Toast.LENGTH_LONG).show();
        }
        cr.moveToFirst();
        long price=cr.getLong(0);
        db.close();
        double distn=(distance*10)+50;
        prc=price*qnt;
        prc=prc+distn;
        ed3.setText("Total Price : "+prc);
    }
    public void onconfirm(View v)
    {
        db = dbh.getWritableDatabase();


        ContentValues values = new ContentValues();
        values.put(DatabaseContract.OrderT.COL_QUALITY, ed1.getText().toString());
        values.put(DatabaseContract.OrderT.COL_PLACED_BY, str1);
        values.put(DatabaseContract.OrderT.COL_PLACED_TO, str);
        values.put(DatabaseContract.OrderT.COL_QUANTITY, ed2.getText().toString());
        values.put(DatabaseContract.OrderT.COL_PRICE, String.valueOf(prc));
        long newRowId = db.insert(DatabaseContract.OrderT.TABLE_NAME, null, values);
        if (newRowId > 0) {
            Toast.makeText(getApplicationContext(), "New Record Inserted: " + newRowId, Toast.LENGTH_LONG).show();
        }
        db.close();
        String title = "Imp notfication";
        String message = "Some one has Placed order";
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("YOUR_CHANNEL_ID", "YOUR_CHANNEL_NAME",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DISCRIPTION");
            mNotificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "YOUR_CHANNEL_ID")
                .setSmallIcon(R.mipmap.ic_launcher_round) // notification icon
                .setContentTitle(title) // title for notification
                .setContentText(message)// message for notification
                .setAutoCancel(true); // clear notification after click
        Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);
        mNotificationManager.notify(0, mBuilder.build());
        Intent intentt=new Intent(OrderPage.this, MapsActivity3.class);
        intentt.putExtra("PickUp",pickup);
        intentt.putExtra("DropOff",dropoff);
        intentt.putExtra("Id",str+str1);
        intentt.putExtra("customerId",str1);
        startActivity(intentt);
    }
}
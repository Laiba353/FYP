package com.example.chatting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapsActivity3 extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LatLng previousLatLng;
    LatLng currentLatLng;
    LatLng latLng1, latLng2;
    private Polyline polyline1,polyline2,p3;
    ProgressDialog progressDialog;
    private List<LatLng> polylinePoints = new ArrayList<>();
    private Marker mCurrLocationMarker;
    String pickup,dropoff,Id,cId,RiderContact;
    Button succes,cancel;
    FirebaseDatabase database;
    DatabaseReference ref;
    private Chronometer mChronometer;
    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    RelativeLayout rl;
    TextView Name,Contact;
    EditText text;
    ListView listview;
    Activity activity;
    ArrayList<RiderClass> array=new ArrayList<RiderClass>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps3);
        Intent intent = getIntent();
        pickup = intent.getStringExtra("PickUp");
        dropoff = intent.getStringExtra("DropOff");
        //Id = intent.getStringExtra("Id");
        cId = intent.getStringExtra("customerId");
        Toast.makeText(MapsActivity3.this, pickup + dropoff, Toast.LENGTH_SHORT).show();

        succes = (Button) findViewById(R.id.succesfull);
        cancel = (Button) findViewById(R.id.cancel);
        activity=this;
        rl=(RelativeLayout) findViewById(R.id.messageBox);
        Name=(TextView)findViewById(R.id.cname) ;
        Contact=(TextView)findViewById(R.id.ccontact) ;
        text=(EditText) findViewById(R.id.edt);
        listview=(ListView)findViewById(R.id.list) ;
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getReadableDatabase();
        String[] colm={DatabaseContract.Customers.COL_CONTACT};
        Cursor cr=db.query(DatabaseContract.Customers.TABLE_NAME,colm,DatabaseContract.Customers._ID+"=?", new String[] {cId}
                , null, null, null, null);
        if (cr.getCount()==0) {
            Toast.makeText(getApplicationContext(),"No Record exist",Toast.LENGTH_LONG).show();
        }
        else {

            cr.moveToFirst();
           Id = cr.getString(0);
           Toast.makeText(MapsActivity3.this,Id,Toast.LENGTH_SHORT).show();
        }
        mChronometer = new Chronometer(this);
        mChronometer.setBase(SystemClock.elapsedRealtime());
        mChronometer.start();
        long elapsedMillis = SystemClock.elapsedRealtime()
                - mChronometer.getBase();
        int hours = (int) (elapsedMillis / 3600000);
        int minutes = (int) (elapsedMillis - hours * 3600000) / 60000;
        if (minutes > 4) {
            cancel.setVisibility(View.INVISIBLE);
            mChronometer.stop();
        }
        database = FirebaseDatabase.getInstance();
         ref = database.getReference();
        ref.child("Orderlocation").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (snapshot.child("667").child("Cancel").getValue().equals("Complete")) {


                    AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity3.this);

                    // Set a title for alert dialog
                    builder.setTitle("Order Completed");

                    // Ask the final question
                    builder.setMessage("Your Order Has Been Successfully Completed");

                    // Set the alert dialog yes button click listener
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ref.child("Orderlocation").child(Id).removeValue();
                            ref.child("Orderlocation").child(Id).child("Cancel").setValue("Successfull");
                            Intent in = new Intent(MapsActivity3.this, MilkManList.class);
                            startActivity(in);

                        }
                    });
                    AlertDialog dialog = builder.create();
                    // Display the alert dialog on interface
                    dialog.show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        progressDialog = new ProgressDialog(MapsActivity3.this);
        progressDialog.setMessage("Searching for Rider..."); // Setting Message
        progressDialog.setTitle("Selecting Rider"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//if(ref.child("Rider").child(Id).)
        ref.child("Rider").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressDialog.show(); // Display Progress Dialog
                progressDialog.setCancelable(false);
                if(!snapshot.hasChild(Id))
                {
                    //progressDialog = new ProgressDialog(MapsActivity3.this);
                     // Progress Dialog Style Spinner

                }
                else
                {
                    RiderContact=snapshot.child(Id).child("Contact").getValue().toString();
                    Name.setText("You have Been Assign Ride Of "+snapshot.child(Id).child("Name").getValue().toString());
                    Contact.setText("Contact "+snapshot.child(Id).child("Contact").getValue().toString());
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //ref=database.getReference().child("Messages");
        ref.child("Messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(Id))
                {
                    array.clear();
                    String mm=""+snapshot.child(Id).child("msg").getValue();
                    RiderClass rc=new RiderClass(mm,"White");
                    array.add(rc);
                    RiderAdapter ra=new RiderAdapter(activity,array);
                    listview.setAdapter(ra);
                    ra.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        List<Address> addressList1=null;
        Geocoder geocoder=new Geocoder(MapsActivity3.this);
        try {
            addressList1=geocoder.getFromLocationName(pickup,1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Address address=addressList1.get(0);
        latLng1=new LatLng(address.getLatitude(),address.getLongitude());
        mMap.addMarker(new MarkerOptions().position(latLng1).title("Milkman Location"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng1,10));


        List<Address> addressList2=null;
        Geocoder geocoder2=new Geocoder(MapsActivity3.this);
        try {
            addressList2=geocoder2.getFromLocationName(dropoff,1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Address address2=addressList2.get(0);
        latLng2=new LatLng(address2.getLatitude(),address2.getLongitude());
        mMap.addMarker(new MarkerOptions().position(latLng2).title("Drop Off Location"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng1,10));
        // Add a marker  and move the camera
        polyline2=mMap.addPolyline(new PolylineOptions().clickable(true).add(latLng1,latLng2));
        polyline1 = mMap.addPolyline(new PolylineOptions().addAll(polylinePoints));
        fetchLocationUpdates();

    }

    private void fetchLocationUpdates() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("location").child("device1");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Log.i("tag", "New location updated:" + dataSnapshot.getKey());
                updateMap(dataSnapshot);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateMap(DataSnapshot dataSnapshot) {
        double latitude = 0, longitude = 0;

        Iterable<DataSnapshot> data = dataSnapshot.getChildren();
        for(DataSnapshot d: data){
            if(d.getKey().equals("latitude")){
                latitude = (Double) d.getValue();
            }else if(d.getKey().equals("longitude")){
                longitude = (Double) d.getValue();
            }
        }

        currentLatLng = new LatLng(latitude, longitude);

        if(previousLatLng ==null || previousLatLng != currentLatLng){
            // add marker line
            if(mMap!=null) {
                previousLatLng  = currentLatLng;
                polylinePoints.add(currentLatLng);
                polyline1.setPoints(polylinePoints);
                Log.w("tag", "Key:" + currentLatLng);
                if(mCurrLocationMarker!=null){
                    mCurrLocationMarker.setPosition(currentLatLng);
                }else{
                    mCurrLocationMarker = mMap.addMarker(new MarkerOptions()
                            .position(currentLatLng)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
                            .title("Delivery"));
                }
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 16));
                p3=mMap.addPolyline(new PolylineOptions().clickable(true).add(latLng1,currentLatLng));
            }

        }
    }

    public void Successfull(View v)
    {
        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
     //ref.child("Orderlocation").child(Id).removeValue();

        ref.child("Orderlocation").child(Id).child("Cancel").setValue("Successfull");
    }
    public void CancelOrder(View v)
    {
        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        ref.child("Orderlocation").child(Id).child("Cancel").setValue("Cancel");
    }
    public void onbtn(View v)
    {
        if(!text.getText().toString().equals(null))
        {
            //array.clear();
            String ss=text.getText().toString();
            RiderClass rc=new RiderClass(ss,"Green");
            array.add(rc);
            RiderAdapter ra=new RiderAdapter(activity,array);
            listview.setAdapter(ra);
            ref=database.getReference();
            ref.child("Messages").child(RiderContact).child("msg").setValue(text.getText().toString());
        }

    }
}
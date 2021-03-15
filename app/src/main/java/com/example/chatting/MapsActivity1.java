package com.example.chatting;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

public class MapsActivity1 extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LatLng previousLatLng;
    LatLng currentLatLng;
    LatLng latLng2,latLng1;
    private Polyline polyline1,polyline2,p3;
    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    private List<LatLng> polylinePoints = new ArrayList<>();
    private Marker mCurrLocationMarker;
String pickup,dropoff,riderId,customer,ccontact,contact,name;
Button btn,complete;
    FirebaseDatabase database;
    DatabaseReference ref;
    private static final int RC_LOCATION_REQUEST = 1234;
    private int RC_LOCATION_ON_REQUEST = 1235;
    private LocationRequest locationRequest;
    RelativeLayout rl;
    TextView Name,Contact;
    EditText text;
    ListView listview;
    Activity activity;
    ArrayList<RiderClass> array=new ArrayList<RiderClass>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps1);
        Intent intent=getIntent();
        activity = this;
        pickup=intent.getStringExtra("PickUp");
        dropoff=intent.getStringExtra("DropOff");
        riderId=intent.getStringExtra("RiderId");
      customer=intent.getStringExtra("customer");
        ccontact=intent.getStringExtra("Contact");
        //Toast.makeText(MapsActivity1.this,pickup+dropoff,Toast.LENGTH_SHORT).show();
        btn=(Button)findViewById(R.id.confirm) ;
        complete=(Button)findViewById(R.id.Complete) ;
        listview = (ListView) findViewById(R.id.list);
        dbHelper = new DatabaseHelper(this);
rl=(RelativeLayout) findViewById(R.id.messageBox);
Name=(TextView)findViewById(R.id.cname) ;
Contact=(TextView)findViewById(R.id.ccontact) ;
text=(EditText) findViewById(R.id.edt);
Name.setText(customer);
Contact.setText(ccontact);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        db = dbHelper.getReadableDatabase();
        String[] colm={DatabaseContract.Riders.COL_NAME,DatabaseContract.Riders.COL_CONTACT};
        Cursor cr=db.query(DatabaseContract.Riders.TABLE_NAME,colm,DatabaseContract.Riders._ID+"=?", new String[] {riderId}
                , null, null, null, null);
        if (cr.getCount()==0) {
            Toast.makeText(getApplicationContext(),"No Record exist",Toast.LENGTH_LONG).show();
        }
        else {

            cr.moveToFirst();
            name = cr.getString(0);
            contact = cr.getString(1);
        }
        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                    if (snapshot.child("Orderlocation").child(ccontact).child("Cancel").getValue().equals("Cancel")) {


                        AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity1.this);

                        // Set a title for alert dialog
                        builder.setTitle("Order Cancelled");

                        // Ask the final question
                        builder.setMessage("Your Order Has Been Cancelled By Customer");

                        // Set the alert dialog yes button click listener
                        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                stopService(new Intent(MapsActivity1.this, LocatioTrackerService.class));
                                ref.child("Rider").child(ccontact).removeValue();
                                ref.child("Orderlocation").child(ccontact).removeValue();
                                ref.child("Messages").child(contact).child("msg").removeValue();
                                ref.child("Messages").child(ccontact).child("msg").removeValue();
                                Intent in = new Intent(MapsActivity1.this, orderlist.class);
                                startActivity(in);

                            }
                        });
                        AlertDialog dialog = builder.create();
                        // Display the alert dialog on interface
                        dialog.show();
                    }
                if (snapshot.child("Cancel").getValue().equals("Successfull")) {


                    AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity1.this);

                    // Set a title for alert dialog
                    builder.setTitle("Order SuccessFull");

                    // Ask the final question
                    builder.setMessage("Congratulations ! This Order Ended SuccessFully");

                    // Set the alert dialog yes button click listener
                    builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            stopService(new Intent(MapsActivity1.this, LocatioTrackerService.class));
                            ref.child("Rider").child(ccontact).removeValue();
                            ref.child("Orderlocation").child(ccontact).removeValue();
                            ref.child("Messages").child(contact).child("msg").removeValue();
                            ref.child("Messages").child(ccontact).child("msg").removeValue();
                            //ref.child("Orderlocation").child(ccontact).child("Cancel").setValue("Unknown");
                            Intent in = new Intent(MapsActivity1.this, orderlist.class);
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
       // ref=database.getReference().child("Messages");
        ref.child("Messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(contact))
                {
                    array.clear();
                    String mm=""+snapshot.child(contact).child("msg").getValue();
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

        //checkLocationSettingsRequest();
        //setUpLocationRequest();


    }
protected void onPause()
{
    super.onPause();
    //stopService(new Intent(MapsActivity.this, LocationTrackerService.class));
}
    protected void onStart()
    {
        super.onStart();

        startService(new Intent(MapsActivity1.this, LocatioTrackerService.class));
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

            List<Address> addressList1=null;
            Geocoder geocoder=new Geocoder(MapsActivity1.this);
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
        Geocoder geocoder2=new Geocoder(MapsActivity1.this);
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
        polyline2=mMap.addPolyline(new PolylineOptions().add(latLng1,latLng2));
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
                p3=mMap.addPolyline(new PolylineOptions().add(latLng1,currentLatLng));
            }

        }
    }
    public void ConfirmRide(View v)
    {
        //String name;

        ref=database.getReference().child("Rider").child(ccontact);
            Map<String, Object> data = new HashMap<>();
            data.put("Name", name);
            data.put("Contact", contact);
            ref.setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    //
                    Log.i("tag", "Location update saved");
                }
            });
            btn.setVisibility(View.INVISIBLE);
            complete.setVisibility(View.VISIBLE);
            rl.setVisibility(View.VISIBLE);

    }
    public void RideComplete(View v)
    {
        ref=database.getReference();
        ref.child("Orderlocation").child(ccontact).child("Cancel").setValue("Complete");
    }
    public void onbtn(View v)
    {
   if(!text.getText().toString().equals(null))
   {
       //array.clear();
       RiderClass rc=new RiderClass(text.getText().toString(),"Green");
       array.add(rc);
       RiderAdapter ra=new RiderAdapter(activity,array);
       listview.setAdapter(ra);
       ref=database.getReference();
       ref.child("Messages").child(ccontact).child("msg").setValue(text.getText().toString());
   }

    }
}
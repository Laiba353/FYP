package com.example.chatting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.maps.android.SphericalUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;
    GoogleMap map;
    ImageButton img;
    EditText location;
    LatLng latLng1;
    LatLng latLng2;
    LatLng latLng3;
    String milkman,customer,milkmanLoc;
    Polyline polyline1;
    Polyline polyline2;
    PolylineOptions pp;
    String str;
    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    Double distance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        img=(ImageButton) findViewById(R.id.search);
        location=(EditText)findViewById(R.id.searchbar);
        Intent intent=getIntent();
        dbHelper = new DatabaseHelper(this);

        milkman=intent.getStringExtra("milkman");
        customer=intent.getStringExtra("customer");
        milkmanLoc=intent.getStringExtra("milkmanL");
        client = LocationServices.getFusedLocationProviderClient(this);
        //permissions check ho rahey hain
        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //after checking permission, calling methos
            getCurrentLocation();
        } else {
            //request permissions
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44
            );

        }
        supportMapFragment=(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               str=location.getText().toString();
                List<Address> addressList=null;
                if(location!=null || !location.equals(""))
                {
                    Geocoder geocoder=new Geocoder(MapsActivity.this);
                    try {
                        addressList=geocoder.getFromLocationName(str,1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address address=addressList.get(0);
                    latLng2=new LatLng(address.getLatitude(),address.getLongitude());
                    distance = SphericalUtil.computeDistanceBetween(latLng1, latLng2);
                    distance=(distance/1000);//km
                    map.addMarker(new MarkerOptions().position(latLng2).title("Your Drop Off Location"));
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng2,10));
               pp=new PolylineOptions().clickable(true).add(latLng1,latLng2);
                    polyline2=map.addPolyline(pp);
                }
            }
        });

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map=googleMap;
        if(milkmanLoc!=null || !milkmanLoc.equals(""))
        {
            List<Address> addressList1=null;
            Geocoder geocoder=new Geocoder(MapsActivity.this);
            try {
                addressList1=geocoder.getFromLocationName("Islamabad",1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address=addressList1.get(0);
            latLng1=new LatLng(address.getLatitude(),address.getLongitude());
            map.addMarker(new MarkerOptions().position(latLng1).title("Milkman Location"));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng1,10));


        }

       // polyline2=map.addPolyline(pp);

    }
    private void getCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null)
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                           latLng3=new LatLng(location.getLatitude(),location.getLongitude());
                            MarkerOptions options=new MarkerOptions().position(latLng3).title("your current location");
                            //for zooming app;
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng3,10));
                            googleMap.addMarker(options);
                            polyline1=googleMap.addPolyline(new PolylineOptions().clickable(true).add(latLng1,latLng3));
                        }
                    });
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
        {
            getCurrentLocation();
        }
    }


    public void Continue(View v)
    {
        db = dbHelper.getReadableDatabase();
        String[] colm={DatabaseContract.Customers.COL_NAME,DatabaseContract.Customers.COL_CONTACT};
        Cursor c = db.query(DatabaseContract.Customers.TABLE_NAME,colm, DatabaseContract.Customers._ID + "=?", new String[] {customer}
                , null, null, null, null);
        if (c.getCount()==0) {
            Toast.makeText(getApplicationContext(),"No Record exist",Toast.LENGTH_LONG).show();
        }
        c.moveToFirst();
       String s1=c.getString(0);
        String s2=c.getString(1);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("Orderlocation").child(s2);


        Map<String, Object> data = new HashMap<>();
        data.put("MilkmanLoc", milkmanLoc);
        data.put("DropOffLoc", str);
        data.put("CustomerName",s1);
        data.put("CustomerContact",s2);
        data.put("Cancel","Unknown");
        ref.setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //
                Log.i("tag", "Location update saved");
                Intent intn=new Intent(MapsActivity.this,OrderPage.class);
                intn.putExtra("milkman",milkman);
                intn.putExtra("customer",customer);
                intn.putExtra("Distance",distance );
                intn.putExtra("PickUp",milkmanLoc);
                intn.putExtra("DropOff",str);

                startActivity(intn);
            }
        });
    }
}
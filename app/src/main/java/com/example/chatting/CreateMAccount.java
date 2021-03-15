package com.example.chatting;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class CreateMAccount extends AppCompatActivity {
    EditText name, location, email, password, contact;
    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    Context context;
    Resources resources;
    TextView createaccount, details;
    Button btn;
    String str,check;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_m_account);
        name = findViewById(R.id.name);
        location = findViewById(R.id.location);
        dbHelper = new DatabaseHelper(this);



        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        contact = findViewById(R.id.contactnumber);
        createaccount=findViewById(R.id.createaccount);
        details=findViewById(R.id.details);
        btn=findViewById(R.id.btn);
        Intent intent = getIntent();
        String languages = intent.getExtras().getString("language");
        check= intent.getStringExtra("Check");
        if(languages.equals("ENGLISH"))
        {

            context = LocalHelper.setLocale(CreateMAccount.this, "en");
            resources = context.getResources();
            createaccount.setText(resources.getString(R.string.account1));
            details.setText(resources.getString(R.string.details));
            name.setText(resources.getString(R.string.name1));
            location.setText(resources.getString(R.string.location));
            email.setText(resources.getString(R.string.email1));
            password.setText(resources.getString(R.string.password1));
            contact.setText(resources.getString(R.string.number));
            btn.setText(resources.getString(R.string.savedetails));




            str="ENGLISH";




        }

        if(languages.equals("اردو"))
        {
            context = LocalHelper.setLocale(CreateMAccount.this, "an");
            resources = context.getResources();
            createaccount.setText(resources.getString(R.string.account1));
            details.setText(resources.getString(R.string.details));
            name.setText(resources.getString(R.string.name1));
            location.setText(resources.getString(R.string.location));
            email.setText(resources.getString(R.string.email1));
            password.setText(resources.getString(R.string.password1));
            contact.setText(resources.getString(R.string.number));
            btn.setText(resources.getString(R.string.savedetails));

            str="اردو";


        }


    }

    public void SaveDetails(View view) {
        db = dbHelper.getWritableDatabase();
        String Name = name.getText().toString();
        String Location = location.getText().toString();
        String Email = email.getText().toString();
        String Password = password.getText().toString();
        String ContactNumber = contact.getText().toString();

        if (Name.equals("") || Location.equals("") || Email.equals("") || Password.equals("") || ContactNumber.equals("")) {
            Toast.makeText(this, "Please Fill All Fields", Toast.LENGTH_SHORT).show();
        } else {

if(check=="MilkMan")
{

            ContentValues values = new ContentValues();
            values.put(DatabaseContract.MilkMan.COL_NAME, Name);
            values.put(DatabaseContract.MilkMan.COL_CONTACT, ContactNumber);
            values.put(DatabaseContract.MilkMan.COL_LOCATION, Location);
            values.put(DatabaseContract.MilkMan.COL_EMAIL, Email);
            values.put(DatabaseContract.MilkMan.COL_PASSWORD, Password);
            long newRowId = db.insert(DatabaseContract.MilkMan.TABLE_NAME, null, values);
            if (newRowId > 0) {
                Toast.makeText(this, "New Record Inserted: " + newRowId, Toast.LENGTH_LONG).show();
            }


            db.close();
            Intent intent = new Intent(this, AddMilkInfo.class);
            intent.putExtra("val1", Email);
            startActivity(intent);

}else if(check=="Customer")
{
    ContentValues values = new ContentValues();
    values.put(DatabaseContract.Customers.COL_NAME, Name);
    values.put(DatabaseContract.Customers.COL_CONTACT, ContactNumber);
    values.put(DatabaseContract.Customers.COL_LOCATION, Location);
    values.put(DatabaseContract.Customers.COL_EMAIL, Email);
    values.put(DatabaseContract.Customers.COL_PASSWORD, Password);
    long newRowId = db.insert(DatabaseContract.Customers.TABLE_NAME, null, values);
    if (newRowId > 0) {
        Toast.makeText(this, "New Record Inserted: " + newRowId, Toast.LENGTH_LONG).show();
    }
    String[] columns = {DatabaseContract.Customers._ID, DatabaseContract.Customers.COL_EMAIL};
    Cursor c = db.query(DatabaseContract.Customers.TABLE_NAME, columns, DatabaseContract.Customers.COL_EMAIL + "=?", new String[]{Email}
            , null, null, null, null);
    if (c.getCount() > 0) {
        c.moveToFirst();
        String vall = String.valueOf(c.getLong(0));
        Toast.makeText(getApplicationContext(), "Record id" + vall, Toast.LENGTH_SHORT).show();

        db.close();

        Intent intent = new Intent(this, MilkManList.class);
        intent.putExtra("val", vall);
        startActivity(intent);
    }
}else
{
    ContentValues values = new ContentValues();
    values.put(DatabaseContract.Riders.COL_NAME, Name);
    values.put(DatabaseContract.Riders.COL_CONTACT, ContactNumber);
    values.put(DatabaseContract.Riders.COL_LOCATION, Location);
    values.put(DatabaseContract.Riders.COL_EMAIL, Email);
    values.put(DatabaseContract.Riders.COL_PASSWORD, Password);
    long newRowId = db.insert(DatabaseContract.Riders.TABLE_NAME, null, values);
    if (newRowId > 0) {
        Toast.makeText(this, "New Record Inserted: " + newRowId, Toast.LENGTH_LONG).show();
    }
    String[] columns = {DatabaseContract.Riders._ID, DatabaseContract.Riders.COL_EMAIL};
    Cursor c = db.query(DatabaseContract.Riders.TABLE_NAME, columns, DatabaseContract.Riders.COL_EMAIL + "=?", new String[]{Email}
            , null, null, null, null);
    if (c.getCount() > 0) {
        c.moveToFirst();
        String vall = String.valueOf(c.getLong(0));
        Toast.makeText(getApplicationContext(), "Record id" + vall, Toast.LENGTH_SHORT).show();

        db.close();

        Intent intent = new Intent(this, orderlist.class);
        intent.putExtra("val", vall);
        startActivity(intent);
    }
}
        }


    }
}



package com.monstercode.contactsapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class FinanceActivity extends AppCompatActivity {
    private TextView textViewName, textViewDuty, textViewRoom, textViewContact;
    public static final String TAG = "Finance Activity";
    private Finance finance;

    private static final int REQUEST_CODE_CALL_PHONE = 1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance);

        finance = (Finance) getIntent().getSerializableExtra("Finance");

        textViewName = findViewById(R.id.name);
        textViewDuty = findViewById(R.id.duty);
        textViewContact = findViewById(R.id.contact);
        textViewRoom = findViewById(R.id.room);

        textViewName.setText(textViewName.getText() + " " + finance.getName());
        textViewDuty.setText(textViewDuty.getText() + " " + finance.getDuty());
        textViewContact.setText(textViewContact.getText() + " " + finance.getContact());
        textViewRoom.setText(textViewRoom.getText() + " " + finance.getRoom());

        textViewContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall();
            }
        });
    }

    public void makePhoneCall() {
        String phoneNumber = finance.getContact();
        Intent phoneIntent;
        phoneIntent = new Intent(Intent.ACTION_CALL);
        if(ContextCompat.checkSelfPermission(FinanceActivity.this, Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "onClick: has call permissions");
            phoneIntent.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(phoneIntent);
        } else {
            Log.d(TAG, "onClick: no call permissions, requesting");
            ActivityCompat.requestPermissions(FinanceActivity.this, new String[] {Manifest.permission.CALL_PHONE}, REQUEST_CODE_CALL_PHONE);
            return;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: requesting permissions");
        switch (requestCode) {
            case REQUEST_CODE_CALL_PHONE: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.d(TAG, "onRequestPermissionsResult: granted permissions");
                    makePhoneCall();

                } else {
                    Toast.makeText(this, "We need permissions to allow calls", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}

package com.monstercode.contactsapp;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;


import java.util.List;
import java.util.Set;

public class DetailActivity extends AppCompatActivity implements ContactDialogFragment.MyDialogListener {
    private String TAG = "DetailActivity";
    private TextView name, sitename, email, tel1, tel2, category;
    private static final int REQUEST_CODE_CALL_PHONE = 1;
    private static boolean CALL_PHONE_GRANTED = false;
    private LinearLayout layout_tel1, layout_tel2;
    private boolean existsInDB;
    private int hasCallPermission;
    private LinearLayout getLayout_tel1;
    private LinearLayout getLayout_tel2, getLayout_email;
    private boolean isCallTel1 = false;
    private Detail detail;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: requesting permissions");
        switch (requestCode) {
            case REQUEST_CODE_CALL_PHONE: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.d(TAG, "onRequestPermissionsResult: granted permissions");
                    hasCallPermission = PackageManager.PERMISSION_GRANTED;

                    CALL_PHONE_GRANTED = true;
                } else {
                    Log.d(TAG, "onRequestPermissionsResult: granted permissions");
                    CALL_PHONE_GRANTED = false;
                }
            }
        }
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menu_delete) {
            Log.d(TAG, "onOptionsItemSelected: selected delete");
//            deleteDetail(detail);
            return true;
        }
        else return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        name = findViewById(R.id.textViewName);
        sitename = findViewById(R.id.textViewSitename);
        email = findViewById(R.id.textViewEmail);
        tel1 = findViewById(R.id.textViewTel1);
        tel2 = findViewById(R.id.textViewTel2);
        getLayout_tel1 = findViewById(R.id.layout_tel1);
        getLayout_tel2 = findViewById(R.id.layout_tel2);
        getLayout_email = findViewById(R.id.layout_email);

        detail = (Detail) getIntent().getSerializableExtra("Detail");

        name.setText(detail.getFirstname() + " " + detail.getLastname());
        sitename.setText(detail.getSitename());
        tel1.setText(detail.getTel1());
        tel2.setText(detail.getTel2());
        email.setText(detail.getEmail());


        getLayout_tel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCallTel1 = true;
                sendDialog();

            }
        });

            getLayout_tel2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isCallTel1 = false;
                    sendDialog();

                }
            });

            getLayout_email.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto", detail.getEmail(), null
                    ));
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {detail.getEmail()});
                    startActivity(emailIntent);
                }
            });
    }

    @Override
    public void onDialogCall(DialogFragment df) {
        Intent phoneIntent;
        phoneIntent = new Intent(Intent.ACTION_CALL);
        if (ContextCompat.checkSelfPermission(DetailActivity.this, Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "onClick: has call permissions");
            if(isCallTel1) {
                phoneIntent.setData(Uri.parse("tel:" + detail.getTel1()));
            } else {
                phoneIntent.setData(Uri.parse("tel:" + detail.getTel2()));
            }
            startActivity(phoneIntent);
        } else {
            Log.d(TAG, "onClick: no call permissions, requesting");
            ActivityCompat.requestPermissions(DetailActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE_CALL_PHONE);
            return;
        }
    }

    @Override
    public void onDialogSms(DialogFragment df) {
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        if(isCallTel1) {
            smsIntent.setData(Uri.parse("sms:" + detail.getTel1()));
            startActivity(smsIntent);
        } else {
            smsIntent.setData(Uri.parse("sms:" + detail.getTel2()));
            startActivity(smsIntent);
        }

    }

    public void sendDialog() {
        DialogFragment dialogFragment = new ContactDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "My Contact Dialog Fragment");
    }


}

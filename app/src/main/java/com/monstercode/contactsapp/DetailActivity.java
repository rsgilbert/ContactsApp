package com.monstercode.contactsapp;

import android.Manifest;
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

import com.monstercode.contactsapp.data.OneDetail;
import com.monstercode.contactsapp.data.Settings;

import java.util.List;
import java.util.Set;

public class DetailActivity extends AppCompatActivity {
    private String TAG = "DetailActivity";
    private TextView name, sitename, email, tel1, tel2, category;
    private Button actionButton, callButton1, callButton2, deleteButton;
    private static final int REQUEST_CODE_CALL_PHONE = 1;
    private static boolean CALL_PHONE_GRANTED = false;
    private LinearLayout layout_tel1, layout_tel2;
    private boolean existsInDB;
    private int hasCallPermission;
    private LinearLayout getLayout_tel1;
    private LinearLayout getLayout_tel2, getLayout_email;


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
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        String formerFragment = getIntent().getStringExtra("formerFragment");
        if(formerFragment.equals("saved")) {
            getMenuInflater().inflate(R.menu.menu_details_delete, menu);
        } else if(formerFragment.equals("online")) {
            getMenuInflater().inflate(R.menu.menu_details_save, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menu_delete) {
            Log.d(TAG, "onOptionsItemSelected: selected delete");
            deleteDetail();
            return true;
        } else if (item.getItemId() == R.id.menu_save) {
            Log.d(TAG, "onOptionsItemSelected: selected save");
            saveDetail();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
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

        final int hasCallPermissions = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);



        name.setText(OneDetail.getName());
        sitename.setText(OneDetail.getSitename());
        tel1.setText(OneDetail.getTel1());
        tel2.setText(OneDetail.getTel2());
        email.setText(OneDetail.getEmail());

        Toast.makeText(this, "settings: " + Settings.isClickToCall(), Toast.LENGTH_SHORT).show();


        getLayout_tel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = OneDetail.getTel1();
                Intent phoneIntent;
                if (Settings.isClickToCall()) {

                    phoneIntent = new Intent(Intent.ACTION_CALL);
                    if(hasCallPermissions == PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "onClick: has call permissions");
                        CALL_PHONE_GRANTED = true;
                    } else {
                        Log.d(TAG, "onClick: no call permissions, requesting");
                        ActivityCompat.requestPermissions(DetailActivity.this, new String[] {Manifest.permission.CALL_PHONE}, REQUEST_CODE_CALL_PHONE);
                        return;
                    }
                }
                else {
                    phoneIntent = new Intent(Intent.ACTION_DIAL);
                }
                phoneIntent.setData(Uri.parse("tel:" + phoneNumber));
                startActivity(phoneIntent);
            }
        });

            getLayout_tel2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String phoneNumber = OneDetail.getTel2();
                    Intent phoneIntent;
                    if (Settings.isClickToCall()) {
                        phoneIntent = new Intent(Intent.ACTION_CALL);
                        if(hasCallPermissions == PackageManager.PERMISSION_GRANTED) {
                            Log.d(TAG, "onClick: has call permissions");
                            CALL_PHONE_GRANTED = true;
                        } else {
                            Log.d(TAG, "onClick: no call permissions, requesting");
                            ActivityCompat.requestPermissions(DetailActivity.this, new String[] {Manifest.permission.CALL_PHONE}, REQUEST_CODE_CALL_PHONE);
                            return;
                        }
                    }
                    else {
                        phoneIntent = new Intent(Intent.ACTION_DIAL);
                    }
                    phoneIntent.setData(Uri.parse("tel:" + phoneNumber));
                    startActivity(phoneIntent);
                }
            });

            getLayout_email.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto", OneDetail.getEmail(), null
                    ));
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {OneDetail.getEmail()});
                    startActivity(emailIntent);
                }
            });
    }

    private void saveDetail() {
        class SaveTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Intent i = new Intent(DetailActivity.this, MainActivity.class);
                startActivity(i);
            }

            @Override
            protected Void doInBackground(Void... voids) {
                AppDatabase appDatabase = DatabaseClient.getInstance(DetailActivity.this)
                        .getAppDatabase();
                appDatabase.detailDao().insertOne(OneDetail.getDetail());
                return null;
            }
        }
        SaveTask saveTask = new SaveTask();
        saveTask.execute();
    }
    private void deleteDetail() {
        class DeleteTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Intent i = new Intent(DetailActivity.this, MainActivity.class);
                startActivity(i);
            }

            @Override
            protected Void doInBackground(Void... voids) {
                AppDatabase appDatabase = DatabaseClient.getInstance(DetailActivity.this)
                        .getAppDatabase();
                appDatabase.detailDao().deleteOne(OneDetail.getDetail());
                return null;
            }
        }
        DeleteTask deleteTask = new DeleteTask();
        deleteTask.execute();
    }

}

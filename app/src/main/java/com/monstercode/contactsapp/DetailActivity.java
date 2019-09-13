package com.monstercode.contactsapp;

import android.Manifest;
import android.content.Intent;
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
import androidx.core.content.ContextCompat;

import com.monstercode.contactsapp.data.OneDetail;

import java.util.List;

public class DetailActivity extends AppCompatActivity {
    private String TAG = "DetailActivity";
    private TextView name, sitename, email, tel1, tel2, category;
    private Button actionButton, callButton1, callButton2, deleteButton;
    private static final int REQUEST_CODE_CALL_PHONE = 1;
    private static boolean CALL_PHONE_GRANTED = false;
    private LinearLayout layout_tel1, layout_tel2;
    private boolean existsInDB;



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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

        int hasCallPermissions = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);

        callButton1 = findViewById(R.id.callButton1);
        callButton2 = findViewById(R.id.callButton2);

        name.setText(OneDetail.getName());
        sitename.setText(OneDetail.getSitename());
        tel1.setText(OneDetail.getTel1());
        tel2.setText(OneDetail.getTel2());
        email.setText(OneDetail.getEmail());

        if((OneDetail.getTel1().length() < 9)) {
            callButton1.setEnabled(false);
            tel1.setText("N/A");
        }
        if((OneDetail.getTel2().length() < 9)) {
            callButton2.setEnabled(false);
            tel2.setText("N/A");
        }
//        if(Settings.isClickToCall()) {
//            callButton1.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    checkPermissions();
//                    String phoneNumber = OneDetail.getTel1();
//                    Intent phoneIntent = new Intent(Intent.ACTION_CALL);
//                    phoneIntent.setData(Uri.parse("tel:" + phoneNumber));
////                    if(CALL_PHONE_GRANTED) {
////                        startActivity(phoneIntent);
////                    }
//                    // TO BE CONTINUED
//
//                }
//            });
//
//            callButton2.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    String phoneNumber = OneDetail.getTel2();
//                    Intent phoneIntent = new Intent(Intent.ACTION_DIAL);
//                    phoneIntent.setData(Uri.parse("tel:" + phoneNumber));
//                    startActivity(phoneIntent);
//                }
//            });
//        } else {
            callButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String phoneNumber = OneDetail.getTel1();
                    Intent phoneIntent = new Intent(Intent.ACTION_DIAL);
                    phoneIntent.setData(Uri.parse("tel:" + phoneNumber));
                    startActivity(phoneIntent);
                }
            });

            callButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String phoneNumber = OneDetail.getTel2();
                    Intent phoneIntent = new Intent(Intent.ACTION_DIAL);
                    phoneIntent.setData(Uri.parse("tel:" + phoneNumber));
                    startActivity(phoneIntent);
                }
            });
//        }


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
//    private void checkPermissions() {
//        if(hasCallPermissions == PackageManager.PERMISSION_GRANTED){
//            Toast.makeText(DetailActivity.this, "permissions present" + hasCallPermissions, Toast.LENGTH_SHORT).show();;
//            CALL_PHONE_GRANTED = true;
//
//        } else {
//            CALL_PHONE_GRANTED = false;
//            Toast.makeText(DetailActivity.this, "permissions not there, requesting" + hasCallPermissions, Toast.LENGTH_SHORT).show();;
//            Log.d(TAG, "onClick: no call permissions" + hasCallPermissions);
//            ActivityCompat.requestPermissions(DetailActivity.this, new String[] {Manifest.permission.CALL_PHONE}, REQUEST_CODE_CALL_PHONE);
//        }
//    }
}

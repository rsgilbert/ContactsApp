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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Date;


public class FinanceActivity extends AppCompatActivity {
    private TextView textViewName, textViewDuty, textViewRoom, textViewContact;
    public static final String TAG = "Finance Activity";
    private Finance finance;
    private Long currentTime = new Date().getTime();

    private static final int REQUEST_CODE_CALL_PHONE = 1;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete: {
                deleteFinance();
                return true;
            }
            case R.id.menu_save: {
                saveFinance();
                return true;
            }
            case android.R.id.home: {
                super.onBackPressed();
            }
            default: return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        finance = (Finance) getIntent().getSerializableExtra("Finance");

        textViewName = findViewById(R.id.name);
        textViewDuty = findViewById(R.id.duty);
        textViewContact = findViewById(R.id.contact);
        textViewRoom = findViewById(R.id.room);

        textViewName.setText(finance.getName());
        textViewDuty.setText(finance.getDuty());
        textViewContact.setText(finance.getContact());
        textViewRoom.setText(finance.getRoom());

        textViewContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final Menu finalMenu = menu;
        class GetTask extends AsyncTask<Void, Void, Finance> {
            @Override
            protected void onPostExecute(Finance finance) {
                super.onPostExecute(finance);
                if(finance.getLastChecked() != null) {
                    finance.setLastChecked(currentTime);
                    getMenuInflater().inflate(R.menu.menu_details_delete, finalMenu);
                } else {
                    getMenuInflater().inflate(R.menu.menu_details_save, finalMenu);
                }
            }

            @Override
            protected Finance doInBackground(Void... voids) {
                AppDatabase db = DatabaseClient.getInstance(FinanceActivity.this).getAppDatabase();
                Finance newFinance = db.financeDao().getOne(finance.getId());
                if(newFinance != null) {
                    Date date = new Date();
                    Long time = date.getTime();
                    newFinance.setLastChecked(time);
                    db.financeDao().updateOne(newFinance);
                    return newFinance;
                } else return finance;
            }
        }
        if (finance.getLastChecked() == null) {
            GetTask getTask = new GetTask();
            getTask.execute();
        } else {
            getMenuInflater().inflate(R.menu.menu_details_delete, finalMenu);
        }
        return true;
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

    private void deleteFinance() {
        class DeleteTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                FinanceActivity.this.onBackPressed();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                AppDatabase appDatabase = DatabaseClient.getInstance(FinanceActivity.this)
                        .getAppDatabase();
                appDatabase.financeDao().deleteOne(finance);
                return null;
            }
        }
        DeleteTask deleteTask = new DeleteTask();
        deleteTask.execute();
    }
    private void saveFinance() {
        class SaveTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                FinanceActivity.this.onBackPressed();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                AppDatabase appDatabase = DatabaseClient.getInstance(FinanceActivity.this)
                        .getAppDatabase();
                finance.setLastChecked(currentTime);
                appDatabase.financeDao().insertOne(finance);
                return null;
            }
        }
        SaveTask saveTask = new SaveTask();
        saveTask.execute();
    }

}

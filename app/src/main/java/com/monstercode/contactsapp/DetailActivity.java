package com.monstercode.contactsapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import java.util.Date;

public class DetailActivity extends AppCompatActivity implements ContactDialogFragment.MyDialogListener {
    private String TAG = "DetailActivity";
    private TextView name, sitename, email, tel1, tel2, designation;
    private static final int REQUEST_CODE_CALL_PHONE = 1;
    private static boolean CALL_PHONE_GRANTED = false;
    private boolean existsInDB;
    private int hasCallPermission;
    private LinearLayout getLayout_tel1, getLayout_tel2, getLayout_email;
    private boolean isCallTel1 = false;
    private Detail detail;
    private Long currentTime = new Date().getTime();

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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = findViewById(R.id.textViewName);
        email = findViewById(R.id.textViewEmail);
        tel1 = findViewById(R.id.textViewTel1);
        tel2 = findViewById(R.id.textViewTel2);
        sitename = findViewById(R.id.site);
        designation = findViewById(R.id.designation);
        getLayout_tel1 = findViewById(R.id.layoutContact1);
        getLayout_tel2 = findViewById(R.id.layoutContact2);
        getLayout_email = findViewById(R.id.layoutEmail);

        detail = (Detail) getIntent().getSerializableExtra("Detail");

        name.setText(detail.getFirstname() + " " + detail.getLastname());
        tel1.setText(detail.getTel1());
        tel2.setText(detail.getTel2());
        email.setText(detail.getEmail());
        sitename.setText(detail.getSitename());
        designation.setText(detail.getJob());

        if(detail.getTel1() == null || detail.getTel1().length() < 9) {
            getLayout_tel1.setVisibility(View.GONE);
        }

        getLayout_tel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeColor(getLayout_tel1);
                isCallTel1 = true;
                sendDialog();
            }
        });

        if(detail.getTel1() == null || detail.getTel2().length() < 9) {
            getLayout_tel2.setVisibility(View.GONE);
         }

        getLayout_tel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeColor(getLayout_tel2);
                isCallTel1 = false;
                sendDialog();

            }
        });

        getLayout_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeColor(getLayout_email);
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", detail.getEmail(), null
                ));
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {detail.getEmail()});
                startActivity(emailIntent);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final Menu finalMenu = menu;
        class GetTask extends AsyncTask<Void, Void, Detail> {
            @Override
            protected void onPostExecute(Detail detail) {
                super.onPostExecute(detail);
                if(detail.getLastChecked() != null) {
                    detail.setLastChecked(currentTime);
                    getMenuInflater().inflate(R.menu.menu_details_delete, finalMenu);
                } else {
                    getMenuInflater().inflate(R.menu.menu_details_save, finalMenu);
                }
            }

            @Override
            protected Detail doInBackground(Void... voids) {
                AppDatabase db = DatabaseClient.getInstance(DetailActivity.this).getAppDatabase();
                Detail newDetail = db.detailDao().getOne(detail.getId());
                if(newDetail != null) {
                    Date date = new Date();
                    Long time = date.getTime();
                    newDetail.setLastChecked(time);
                    db.detailDao().updateOne(newDetail);
                    return newDetail;
                } else return detail;
            }
        }
        if (detail.getLastChecked() == null) {
            GetTask getTask = new GetTask();
            getTask.execute();
        } else {
            getMenuInflater().inflate(R.menu.menu_details_delete, finalMenu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_delete: {
                deleteDetail();
                return true;
            }
            case R.id.menu_save: {
                Log.d(TAG, "onOptionsItemSelected: selected save");
                saveDetail();
                return true;
            }
            case android.R.id.home: {
                super.onBackPressed();
            }
            default: return super.onOptionsItemSelected(item);
        }
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

    private void deleteDetail() {
        class DeleteTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                DetailActivity.this.onBackPressed();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                AppDatabase appDatabase = DatabaseClient.getInstance(DetailActivity.this)
                        .getAppDatabase();
                appDatabase.detailDao().deleteOne(detail);
                return null;
            }
        }
        DeleteTask deleteTask = new DeleteTask();
        deleteTask.execute();
    }

    private void saveDetail() {
        class SaveTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                DetailActivity.this.onBackPressed();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                AppDatabase appDatabase = DatabaseClient.getInstance(DetailActivity.this)
                        .getAppDatabase();
                detail.setLastChecked(currentTime);
                appDatabase.detailDao().insertOne(detail);
                return null;
            }
        }
        SaveTask saveTask = new SaveTask();
        saveTask.execute();
    }

    private void changeColor(LinearLayout layout) {
//        ColorDrawable background = (ColorDrawable) layout.getBackground();
//        int colorId = background.getColor();
//        Toast.makeText(this, "color is " + colorId, Toast.LENGTH_SHORT).show();
//        background.setColor(getResources().getColor(android.R.color.darker_gray));
//        Toast.makeText(this, "changed color is " + getResources().getColor(android.R.color.darker_gray), Toast.LENGTH_SHORT).show();
//        background.setColor(colorId);
    }

}

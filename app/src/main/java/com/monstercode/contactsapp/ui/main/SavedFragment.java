package com.monstercode.contactsapp.ui.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.monstercode.contactsapp.AppDatabase;
import com.monstercode.contactsapp.DatabaseClient;
import com.monstercode.contactsapp.Detail;
import com.monstercode.contactsapp.DetailsAdapter;
import com.monstercode.contactsapp.R;
import com.monstercode.contactsapp.SettingsActivity;
import com.monstercode.contactsapp.data.OneDetail;
import com.monstercode.contactsapp.data.Settings;

import java.util.ArrayList;
import java.util.List;

public class SavedFragment extends Fragment {
    private String TAG = "SavedFragment";
    private RecyclerView recyclerView;
    private DetailsAdapter detailsAdapter;
    private SearchView searchView;
    private int hasReadContactPermissions;
    private boolean READ_CONTACTS_GRANTED;
    private final int REQUEST_READ_CONTACTS_CODE = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_saved, container, false);
        setHasOptionsMenu(true);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        hasReadContactPermissions = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS);
        Log.d(TAG, "onViewCreated: hasPermissions: " + hasReadContactPermissions);
        Toast.makeText(getActivity(), "hasPermissions: " + hasReadContactPermissions, Toast.LENGTH_SHORT).show();


        recyclerView = view.findViewById(R.id.recyclerview_details);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // adding divider
        DividerItemDecoration itemDecorator =  new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.divider));
        recyclerView.addItemDecoration(itemDecorator);
        loadSavedDetails();




    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        final MenuItem searchItem = menu.findItem(R.id.menu_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                detailsAdapter.filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                detailsAdapter.filter(s);
                return false;
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: Requesting read contact permission");
        switch (requestCode) {
            case REQUEST_READ_CONTACTS_CODE: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "onRequestPermissionsResult: Permission granted");
                    READ_CONTACTS_GRANTED = true;
                    loadSavedDetails();
                } else {
                    READ_CONTACTS_GRANTED = false;
                    Log.d(TAG, "onRequestPermissionsResult: Read contacts permission denied");
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_settings:
                Intent i = new Intent(getActivity(), SettingsActivity.class);
                startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }


    // for handing over parameters
    public static SavedFragment newInstance(String text) {
        SavedFragment f = new SavedFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }

    private void loadSavedDetails() {
        Log.d(TAG, "loadDetails: ");
        class LoadTask extends AsyncTask<Void, Void, List<Detail>> {
            @Override
            protected void onPostExecute(List<Detail> details) {
                super.onPostExecute(details);
                if(Settings.isAddPhoneContacts()) {
                    details.addAll(getPersonalContacts());
                }
                detailsAdapter = new DetailsAdapter(getActivity(), details);
                detailsAdapter.setFormerFragment("saved");
                recyclerView.setAdapter(detailsAdapter);
            }

            @Override
            protected List<Detail> doInBackground(Void... voids) {
                AppDatabase appDatabase = DatabaseClient.getInstance(getContext())
                        .getAppDatabase();
                return appDatabase.detailDao().getAll();
            }
        }
        LoadTask loadTask = new LoadTask();
        loadTask.execute();
    }

    private List<Detail> getPersonalContacts() {
        List<Detail> details = new ArrayList<>();
        if(hasReadContactPermissions == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "getPersonalContacts: has permissions: " + PackageManager.PERMISSION_GRANTED);
            READ_CONTACTS_GRANTED = true;
        } else {
            Log.d(TAG, "getPersonalContacts: No permissions");
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.READ_CONTACTS}, REQUEST_READ_CONTACTS_CODE);
            return null;
        }
        Cursor cursor= getActivity().getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        List<String> contacts = new ArrayList<>();
        while (cursor.moveToNext()) {
            OneDetail.setName(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)));
            OneDetail.setTel1(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.Data.DATA1)));
            details.add(OneDetail.getDetail());
        }
        return details;
    }


}

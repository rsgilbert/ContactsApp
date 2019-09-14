package com.monstercode.contactsapp.ui.main;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.monstercode.contactsapp.AppDatabase;
import com.monstercode.contactsapp.DatabaseClient;
import com.monstercode.contactsapp.Detail;
import com.monstercode.contactsapp.DetailService;
import com.monstercode.contactsapp.DetailsAdapter;
import com.monstercode.contactsapp.R;
import com.monstercode.contactsapp.SettingsActivity;

import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;

public class OnlineFragment extends Fragment {
    private String TAG = "OnlineFragment";
    private RecyclerView recyclerView;
    private DetailsAdapter detailsAdapter;
    private final String API_BASE_URL = "https://contactsapi01.herokuapp.com";
    private SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_online, container, false);
        setHasOptionsMenu(true);
        return v;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

        final MenuItem searchItem = menu.findItem(R.id.menu_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                queryOnlineDetails(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                queryOnlineDetails(s);
                return false;
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.recyclerview_details);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // adding divider
        DividerItemDecoration itemDecorator =  new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.divider));
        recyclerView.addItemDecoration(itemDecorator);


        if(isNetworkAvailable()) {
            queryOnlineDetails("");
        } else  {
            Toast.makeText(getActivity(), "Poor internet connection", Toast.LENGTH_LONG).show();
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

    public static OnlineFragment newInstance(String text) {
        OnlineFragment f = new OnlineFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }

    // check for network connectivity
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    // connect to ContactsAPI online, download contacts and put them into db
    private void queryOnlineDetails(String query) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.client(httpClient.build()).build();
        DetailService detailService = retrofit.create(DetailService.class);

        Call<List<Detail>> call = detailService.getDetails(query);

        call.enqueue(new Callback<List<Detail>>() {
            @Override
            public void onResponse(Call<List<Detail>> call, Response<List<Detail>> response) {
                List<Detail> details = response.body();
                detailsAdapter = new DetailsAdapter(getContext(), details);
                detailsAdapter.setFormerFragment("online");
                recyclerView.setAdapter(detailsAdapter);
            }

            @Override
            public void onFailure(Call<List<Detail>> call, Throwable t) {
                Toast.makeText(getContext(), "Poor connection", Toast.LENGTH_SHORT).show();
            }
        });

    }

}

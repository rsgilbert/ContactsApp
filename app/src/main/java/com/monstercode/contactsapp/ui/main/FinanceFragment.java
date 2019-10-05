package com.monstercode.contactsapp.ui.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.monstercode.contactsapp.AppDatabase;
import com.monstercode.contactsapp.DatabaseClient;
import com.monstercode.contactsapp.Detail;
import com.monstercode.contactsapp.DetailsAdapter;
import com.monstercode.contactsapp.Finance;
import com.monstercode.contactsapp.Finance;
import com.monstercode.contactsapp.FinanceAdapter;
import com.monstercode.contactsapp.FinanceSavedAdapter;
import com.monstercode.contactsapp.FinanceService;
import com.monstercode.contactsapp.R;
import com.monstercode.contactsapp.SettingsActivity;
import com.monstercode.contactsapp.data.OneDetail;
import com.monstercode.contactsapp.data.Settings;

import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;

public class FinanceFragment extends Fragment {
    private String TAG = "FinanceFragment";
    private RecyclerView recyclerView, recyclerViewSaved;
    private FinanceAdapter adapter, savedAdapter;
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

        Toast.makeText(getActivity(), "Howdy", Toast.LENGTH_SHORT).show();

        final MenuItem searchItem = menu.findItem(R.id.menu_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                queryOnlineDetails(s);
                savedAdapter.filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                queryOnlineDetails(s);
                savedAdapter.filter(s);
                return false;
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Toast.makeText(getActivity(), "loading saved", Toast.LENGTH_SHORT).show();
        loadSavedDetails();
        Toast.makeText(getActivity(), " saved", Toast.LENGTH_SHORT).show();
        recyclerView = view.findViewById(R.id.recyclerview_details);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // adding divider
        DividerItemDecoration itemDecorator =  new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.divider));
        recyclerView.addItemDecoration(itemDecorator);

        recyclerViewSaved = view.findViewById(R.id.recyclerview_saved);
        recyclerViewSaved.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewSaved.addItemDecoration(itemDecorator);


        if(isNetworkAvailable()) {
            queryOnlineDetails("");
        } else {
            Toast.makeText(getActivity(), "No connection", Toast.LENGTH_SHORT).show();
        }
    }


    public static FinanceFragment newInstance(String text) {
        FinanceFragment f = new FinanceFragment();
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
        class HeaderInterceptor
                implements Interceptor {
            @Override
            public Response intercept(Chain chain)
                    throws IOException {
                Request request = chain.request();
                request = request.newBuilder()
                        .addHeader("Authorization", "hello")
                        .addHeader("deviceplatform", "android")
                        .removeHeader("User-Agent")
                        .addHeader("User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:38.0) Gecko/20100101 Firefox/38.0")
                        .build();
                Response response = chain.proceed(request);
                return response;
            }
        }


        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new HeaderInterceptor());
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.client(httpClient.build()).build();

        FinanceService service = retrofit.create(FinanceService.class);

        Call<List<Finance>> call = service.getFinance(query);

        call.enqueue(new Callback<List<Finance>>() {
            @Override
            public void onResponse(Call<List<Finance>> call, retrofit2.Response<List<Finance>> response) {
                List<Finance> finances = response.body();
                adapter = new FinanceAdapter(getContext(), finances);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Finance>> call, Throwable t) {
                Toast.makeText(getContext(), "Very Poor connection", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void loadSavedDetails() {
        Log.d(TAG, "loadSavedDetails: ");
        class LoadTask extends AsyncTask<Void, Void, List<Finance>> {
            @Override
            protected void onPostExecute(List<Finance> finances) {
                super.onPostExecute(finances);
                savedAdapter = new FinanceAdapter(getActivity(), finances);
                recyclerViewSaved.setAdapter(savedAdapter);
            }

            @Override
            protected List<Finance> doInBackground(Void... voids) {
                AppDatabase appDatabase = DatabaseClient.getInstance(getContext())
                        .getAppDatabase();
                return appDatabase.financeDao().getAll();
            }
        }
        LoadTask loadTask = new LoadTask();
        loadTask.execute();
    }



}

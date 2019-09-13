package com.monstercode.contactsapp.ui.main;

import android.os.AsyncTask;
import android.os.Bundle;
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

import java.util.List;

public class SavedFragment extends Fragment {
    private String TAG = "SavedFragment";
    private RecyclerView recyclerView;
    private DetailsAdapter detailsAdapter;
    private SearchView searchView;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_saved, container, false);
        setHasOptionsMenu(true);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

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
}

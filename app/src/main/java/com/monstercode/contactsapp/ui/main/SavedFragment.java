package com.monstercode.contactsapp.ui.main;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated: ");
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_saved, container, false);
        Log.d(TAG, "onCreateView: ");
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
        Log.d(TAG, "onCreateOptionsMenu: ");
        inflater.inflate(R.menu.menu_main, menu);
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

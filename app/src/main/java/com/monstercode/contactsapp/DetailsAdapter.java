package com.monstercode.contactsapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.detailsViewHolder> {
    private Context context;
    private List<Detail> detailsList;
    private List<Detail> detailsListCopy = new ArrayList<>();


    public DetailsAdapter(Context context, List<Detail> detailsList) {
        this.context = context;
        this.detailsList = detailsList;
        this.detailsListCopy.addAll(detailsList);
    }
    @NonNull
    @Override
    public detailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_details, parent, false);
        return new detailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull detailsViewHolder holder, int position) {
        Detail detail = detailsList.get(position);
        holder.textViewName.setText(detail.getFirstname() + " " + detail.getLastname());
        holder.textViewJob.setText(detail.getJob() +", " + detail.getSitename());
        if (detail.getSitename().toLowerCase().contains("energy")) {
            holder.profileImage.setImageResource(R.drawable.ministry_of_finance);
        } else if(detail.getSitename().toLowerCase().contains("lang")) {
            holder.profileImage.setImageResource(R.drawable.court_of_arms);
        }
    }

    @Override
    public int getItemCount() {
        return detailsList.size();
    }

    class detailsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewName, textViewJob;
        CircleImageView profileImage;

        public detailsViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewJob = itemView.findViewById(R.id.textViewJob);
            profileImage = itemView.findViewById(R.id.profile_image);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            final Detail detail = detailsList.get(getAdapterPosition());
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("Detail", detail);
            context.startActivity(intent);
        }
    }

    // filter, for redisplaying page while querying
    public void filter(String searchWord) {
        this.detailsList.clear();
        if(searchWord.isEmpty()){
            this.detailsList.addAll(detailsListCopy);
        } else {
            searchWord = searchWord.toLowerCase();
            for (Detail detail: detailsListCopy) {
                if(detail.getFirstname().toLowerCase().contains(searchWord)
                        || detail.getLastname().toLowerCase().contains(searchWord)
                        || detail.getJob().toLowerCase().contains(searchWord)
                        || detail.getSitename().toLowerCase().contains(searchWord)
                        || detail.getCategory().toLowerCase().contains(searchWord)) {
                    this.detailsList.add(detail);
                }
            }
        }
        notifyDataSetChanged();
    }


}


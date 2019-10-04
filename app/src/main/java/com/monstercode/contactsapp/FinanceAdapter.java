package com.monstercode.contactsapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.monstercode.contactsapp.data.OneDetail;

import java.util.ArrayList;
import java.util.List;


public class FinanceAdapter extends RecyclerView.Adapter<FinanceAdapter.FinancesViewHolder> {
    private Context context;
    private List<Finance> financesList;
    private List<Finance> financesListCopy = new ArrayList<>();
    public FinanceAdapter(Context context, List<Finance> financesList) {
        this.context = context;
        this.financesList = financesList;
        this.financesListCopy.addAll(financesList);
    }
    @NonNull
    @Override
    public FinancesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_details, parent, false);
        return new FinancesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FinancesViewHolder holder, int position) {
        Finance finance = financesList.get(position);
        holder.textViewName.setText(finance.getName());
        holder.textViewJob.setText(finance.getRoom());
    }

    @Override
    public int getItemCount() {
        return financesList.size();
    }


    class FinancesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewName, textViewJob;

        public FinancesViewHolder(View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.textViewName);
            textViewJob = itemView.findViewById(R.id.textViewJob);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            Finance finance = financesList.get(getAdapterPosition());
            Intent intent = new Intent(context, DetailActivity.class);
            context.startActivity(intent);

        }
    }

    // filter, for redisplaying page while querying
    public void filter(String searchWord) {
        this.financesList.clear();
        if(searchWord.isEmpty()){
            this.financesList.addAll(financesListCopy);
        } else {
            searchWord = searchWord.toLowerCase();
            for (Finance finance: financesListCopy) {
                if(finance.getName().toLowerCase().contains(searchWord)
                        || finance.getContact().toLowerCase().contains(searchWord)
                        || finance.getDuty().toLowerCase().contains(searchWord)
                        || finance.getRoom().toLowerCase().contains(searchWord)) {
                    this.financesList.add(finance);
                }
            }
        }
        notifyDataSetChanged();
    }



}


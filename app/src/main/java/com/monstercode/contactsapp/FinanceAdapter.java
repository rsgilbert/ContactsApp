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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



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
            final Finance finance = financesList.get(getAdapterPosition());
            Toast.makeText(context, "updates: or inserted" , Toast.LENGTH_SHORT).show();
            class SaveTask extends AsyncTask<Void, Void, Void> {
                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    Intent intent = new Intent(context, FinanceActivity.class);
                    intent.putExtra("Finance", finance); // finance must be serializable
                    context.startActivity(intent);
                }

                @Override
                protected Void doInBackground(Void... voids) {
                    AppDatabase db = DatabaseClient.getInstance(context).getAppDatabase();
                    int update = db.financeDao().updateOne(finance);
                    if(update == 0) {
                        db.financeDao().insertOne(finance);
                    }
                    return null;
                }
            }
            SaveTask saveTask = new SaveTask();
            saveTask.execute();
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


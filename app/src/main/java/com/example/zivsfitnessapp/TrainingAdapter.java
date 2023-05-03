package com.example.zivsfitnessapp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TrainingAdapter extends RecyclerView.Adapter<TrainingAdapter.MiniTrainingHolder> {

    public ArrayList<Training> mList;
    private final Context context;

    public TrainingAdapter(Context context, ArrayList<Training> mList) {
        this.context = context;
        this.mList = mList;

    }

    @NonNull
    @Override
    public MiniTrainingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.train_tile, parent, false);
        return new MiniTrainingHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MiniTrainingHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tvDate.setText(mList.get(position).getDate());
        holder.tvDuration.setText(mList.get(position).getFormattedDuration());
        holder.tvCalories.setText(String.valueOf(mList.get(position).getCalories()));
        holder.lBackground.setOnClickListener(view -> showDialog(mList.get(position)));
    }

    private void showDialog(Training training) {
        Dialog d = new Dialog(context);
        d.setContentView(R.layout.training_dialog);

        d.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        d.setCancelable(true);

        TextView tvName = d.findViewById(R.id.tvName);
        TextView tvRoomNumber = d.findViewById(R.id.tvRoomNumber);
        TextView tvTrainerName = d.findViewById(R.id.tvTrainerName);
        TextView tvDuration = d.findViewById(R.id.tvDuration);
        TextView tvCalories = d.findViewById(R.id.tvCalories);
        TextView tvDate = d.findViewById(R.id.tvDate);
        Button btnExit = d.findViewById(R.id.btnExit);

        tvName.setText(String.valueOf(training.getName()));
        tvRoomNumber.setText(String.valueOf(training.getRoomNumber()));
        tvTrainerName.setText(String.valueOf(training.getTrainerName()));
        tvDuration.setText(String.valueOf(training.getFormattedDuration()));
        tvCalories.setText(String.valueOf(training.getCalories()));
        tvDate.setText(String.valueOf(training.getDate()));

        btnExit.setOnClickListener(view -> d.dismiss());

        d.show();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MiniTrainingHolder extends RecyclerView.ViewHolder {

        TextView tvDate, tvDuration, tvCalories;
        LinearLayout lBackground;

        public MiniTrainingHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            tvCalories = itemView.findViewById(R.id.tvCalories);
            lBackground = itemView.findViewById(R.id.lBackground);
        }
    }

}
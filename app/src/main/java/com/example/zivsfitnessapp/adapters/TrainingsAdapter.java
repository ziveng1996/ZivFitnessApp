package com.example.zivsfitnessapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.zivsfitnessapp.R;
import com.example.zivsfitnessapp.SessionManager;
import com.example.zivsfitnessapp.Utils;
import com.example.zivsfitnessapp.models.Training;

import java.util.ArrayList;
import java.util.List;

public class TrainingsAdapter extends RecyclerView.Adapter<TrainingsAdapter.TrainingViewHolder> {


    List<Training> trainings = new ArrayList<>();

    @NonNull
    @Override
    public TrainingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.training_item_layout, parent, false);
        return new TrainingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrainingViewHolder holder, int position) {
        Training training = trainings.get(position);

        holder.name.setText(SessionManager.isPersonalTrainer() ? training.getClient().getFullName() : training.getPersonalTrainer().getFullName());
        String hour;
        if (training.getHour() > 9) {
            hour = String.valueOf(training.getHour());
        } else {
            hour = "0" + training.getHour();
        }

        if (SessionManager.isPersonalTrainer()) {
            holder.title.setText(hour + ":00");
            Glide.with(holder.itemView.getContext()).load(training.getClient().getImageUrl()).circleCrop().placeholder(R.drawable.baseline_supervisor_account_24).into(holder.image);
            holder.subtitle.setVisibility(View.GONE);
        } else {
            holder.title.setText(Utils.dateToStr(training.getDate()));
            holder.subtitle.setText(hour + ":00");
            Glide.with(holder.itemView.getContext()).load(training.getPersonalTrainer().getImageUrl()).circleCrop().placeholder(R.drawable.baseline_supervisor_account_24).into(holder.image);
        }
    }

    @Override
    public int getItemCount() {
        return trainings.size();
    }

    public void setTrainings(List<Training> trainings) {
        this.trainings = trainings;
        notifyDataSetChanged();
    }

    static class TrainingViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView title;
        AppCompatTextView subtitle;
        AppCompatTextView name;
        AppCompatImageView image;

        public TrainingViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            subtitle = view.findViewById(R.id.subtitle);
            name = view.findViewById(R.id.name);
            image = view.findViewById(R.id.image);
        }
    }
}

package com.example.zivsfitnessapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zivsfitnessapp.R;
import com.example.zivsfitnessapp.models.TrainingSlot;

import java.util.ArrayList;
import java.util.List;

public class TrainingSlotAdapter extends RecyclerView.Adapter<TrainingSlotAdapter.TrainingSlotViewHolder> {

public interface Callback {
    void onSlotClicked(TrainingSlot slot);
}

    public Callback callback;
    List<TrainingSlot> slots = new ArrayList<>();

    @NonNull
    @Override
    public TrainingSlotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_training_slot, parent, false);
        return new TrainingSlotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrainingSlotViewHolder holder, int position) {
        TrainingSlot slot = slots.get(position);

        holder.textView.setText(slot.getHour() + ":00");
        if (slot.isAvailable()) {
            holder.container.setBackgroundColor(holder.itemView.getContext().getColor(R.color.green));
            holder.container.setOnClickListener(v -> {
                callback.onSlotClicked(slot);
            });
        } else {
            holder.container.setBackgroundColor(holder.itemView.getContext().getColor(R.color.red));
            holder.container.setOnClickListener(null);
        }
    }

    @Override
    public int getItemCount() {
        return slots.size();
    }

    public void setSlots(List<TrainingSlot> slots) {
        this.slots = slots;
        notifyDataSetChanged();
    }

static class TrainingSlotViewHolder extends RecyclerView.ViewHolder {

    AppCompatTextView textView;
    View container;

    public TrainingSlotViewHolder(View view) {
        super(view);
        textView = view.findViewById(R.id.text);
        container = view.findViewById(R.id.container);
    }
}
}

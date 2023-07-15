package com.example.zivsfitnessapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.zivsfitnessapp.R;
import com.example.zivsfitnessapp.SessionManager;
import com.example.zivsfitnessapp.callbacks.OnPersonalTrainerCtaClickListener;
import com.example.zivsfitnessapp.models.MemberShip;
import com.example.zivsfitnessapp.models.MembershipStatus;
import com.example.zivsfitnessapp.models.PersonalTrainerData;
import com.google.android.material.button.MaterialButton;

import java.text.DecimalFormat;
import java.util.List;

public class FindPersonalTrainerAdapter extends RecyclerView.Adapter<FindPersonalTrainerAdapter.PersonalTrainerViewHolder> {

    public List<PersonalTrainerData> personalTrainerDataList;
    public OnPersonalTrainerCtaClickListener listener;
    public AppCompatActivity activity;

    @NonNull
    @Override
    public PersonalTrainerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.personal_trainer_item_layout, parent, false);
        return new PersonalTrainerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonalTrainerViewHolder holder, int position) {
        @StringRes int ctaText = R.string.send_request;
        boolean isCtaEnabled = true;
        MembershipStatus status = MembershipStatus.NONE;
        for (MemberShip memberShip : SessionManager.client.getMembersShip()) {
            if (memberShip.user.getUid().equals(personalTrainerDataList.get(position).getPersonalTrainer().getUid())) {
                switch (memberShip.status) {
                    case DENIED:
                        ctaText = R.string.request_denied;
                        isCtaEnabled = false;
                        status = MembershipStatus.DENIED;
                        break;
                    case APPROVED:
                        ctaText = R.string.schedule_training;
                        status = MembershipStatus.APPROVED;
                        break;
                    case PENDING:
                        ctaText = R.string.pending_request;
                        status = MembershipStatus.PENDING;
                        isCtaEnabled = false;
                        break;
                    default:
                        status = MembershipStatus.NONE;
                        ctaText = R.string.ask_membership;
                }
                break;
            }
        }
        holder.name.setText(personalTrainerDataList.get(position).getPersonalTrainer().getFullName());
        holder.companyName.setText(personalTrainerDataList.get(position).getPersonalTrainer().getCompanyName());
        DecimalFormat df = new DecimalFormat("#.#");
        String distance = personalTrainerDataList.get(position).getDistance() == -1 ? activity.getResources().getString(R.string.distance_not_available) : df.format(personalTrainerDataList.get(position).getDistance()) + " km";
        holder.distance.setText(distance);
        holder.sendRequest.setText(ctaText);
        holder.sendRequest.setEnabled(isCtaEnabled);
        Glide.with(holder.itemView.getContext())
                .load(personalTrainerDataList.get(position).getPersonalTrainer().getImageUrl())
                .circleCrop()
                .placeholder(R.drawable.baseline_supervisor_account_24)
                .into(holder.image);
        MembershipStatus finalStatus = status;
        holder.sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onPersonalTrainerCtaClicked(position, finalStatus);
            }
        });
    }

    @Override
    public int getItemCount() {
        return personalTrainerDataList.size();
    }

    public void setPersonalTrainerDataList(List<PersonalTrainerData> personalTrainerDataList) {
        this.personalTrainerDataList = personalTrainerDataList;
        notifyDataSetChanged();
    }

    static class PersonalTrainerViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView name;
        AppCompatTextView companyName;
        AppCompatTextView distance;
        MaterialButton sendRequest;
        ImageView image;

        public PersonalTrainerViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            companyName = view.findViewById(R.id.company_name);
            distance = view.findViewById(R.id.distance);
            sendRequest = view.findViewById(R.id.cta);
            image = view.findViewById(R.id.image);

        }

    }
}
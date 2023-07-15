package com.example.zivsfitnessapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.zivsfitnessapp.R;
import com.example.zivsfitnessapp.SessionManager;
import com.example.zivsfitnessapp.callbacks.PendingRequestCallback;
import com.example.zivsfitnessapp.models.MemberShip;
import com.example.zivsfitnessapp.models.MembershipStatus;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class PendingRequestsAdapter extends RecyclerView.Adapter<PendingRequestsAdapter.PendingRequestsViewHolder> {

    private List<MemberShip> memberShips;

    private AppCompatActivity activity;
    PendingRequestCallback pendingRequestListener ;

    public PendingRequestsAdapter(List<MemberShip> membersShip, PendingRequestCallback pendingRequestListener) {
        this.memberShips = membersShip;
        this.pendingRequestListener = pendingRequestListener;
    }

    @NonNull
    @Override
    public PendingRequestsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_request_item_layout, parent, false);
        return new PendingRequestsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingRequestsViewHolder holder, int position) {
        if (SessionManager.isPersonalTrainer()) {
            if (memberShips.get(position).status == MembershipStatus.PENDING) {
                holder.ctaContainer.setVisibility(View.VISIBLE);
                holder.status.setVisibility(View.GONE);
            } else {
                holder.ctaContainer.setVisibility(View.INVISIBLE);
                holder.status.setVisibility(View.VISIBLE);
            }
        } else {
            holder.ctaContainer.setVisibility(View.GONE);
            holder.status.setVisibility(View.VISIBLE);
            holder.status.setText(R.string.pending);
        }

        holder.name.setText(memberShips.get(position).user.getFullName());
        String companyName = memberShips.get(position).user.getCompanyName();
        holder.companyName.setText(companyName);
        if (companyName == null || companyName.length() == 0) {
            holder.companyName.setVisibility(View.GONE);
        } else {
            holder.companyName.setVisibility(View.VISIBLE);
        }

        Glide.with(holder.itemView.getContext())
                .load(memberShips.get(position).user.getImageUrl())
                .circleCrop()
                .placeholder(R.drawable.baseline_supervisor_account_24)
                .into(holder.image);

        switch (memberShips.get(position).status) {
            case DENIED: {
                holder.status.setText(R.string.denied);
                holder.status.setTextColor(holder.itemView.getContext().getColor(R.color.red));
                break;
            }
            case APPROVED: {
                holder.status.setText(R.string.approved);
                holder.status.setTextColor(holder.itemView.getContext().getColor(R.color.green));
                break;
            }
            default:
                holder.status.setText(R.string.pending);
                holder.status.setTextColor(holder.itemView.getContext().getColor(R.color.grey));
        }

        holder.approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pendingRequestListener.onClicked(MembershipStatus.APPROVED, position, memberShips.get(position));
            }
        });

        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pendingRequestListener.onClicked(MembershipStatus.DENIED, position, memberShips.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return memberShips.size();
    }

    static class PendingRequestsViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        AppCompatTextView name;
        AppCompatTextView companyName;
        AppCompatTextView status;
        MaterialButton approve;
        MaterialButton cancel;
        LinearLayoutCompat ctaContainer;


        public PendingRequestsViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            image = itemView.findViewById(R.id.image);
            companyName = itemView.findViewById(R.id.company_name);
            status = itemView.findViewById(R.id.status);
            approve = itemView.findViewById(R.id.approve);
            cancel = itemView.findViewById(R.id.cancel);
            ctaContainer = itemView.findViewById(R.id.cta_container);

        }
    }
}
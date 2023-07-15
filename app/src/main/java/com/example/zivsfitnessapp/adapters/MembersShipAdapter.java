package com.example.zivsfitnessapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.zivsfitnessapp.R;
import com.example.zivsfitnessapp.models.MemberShip;

import java.util.List;

public class MembersShipAdapter extends RecyclerView.Adapter<MembersShipAdapter.MembersShipViewHolder> {

    private List<MemberShip> memberShips;

    public MembersShipAdapter(List<MemberShip> membersShip) {
        this.memberShips = membersShip;
    }

    @NonNull
    @Override
    public MembersShipViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_ship_item_layout, parent, false);
        return new MembersShipAdapter.MembersShipViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MembersShipViewHolder holder, int position) {
        holder.companyName.setText(memberShips.get(position).user.getCompanyName());
        holder.name.setText(memberShips.get(position).user.getFullName());
        Glide.with(holder.itemView.getContext())
                .load(memberShips.get(position).user.getImageUrl())
                .circleCrop()
                .placeholder(R.drawable.baseline_supervisor_account_24)
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return memberShips.size();
    }

    static class MembersShipViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        AppCompatTextView name;
        AppCompatTextView companyName;

        public MembersShipViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            image = itemView.findViewById(R.id.image);
            companyName = itemView.findViewById(R.id.company_name);

        }
    }
}

package com.example.hestia_app.presentation.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hestia_app.R;
import com.example.hestia_app.domain.models.Member;

import java.util.List;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberViewHolder> {

    private Context context;
    private List<Member> memberList;

    public MemberAdapter(Context context, List<Member> memberList) {
        this.context = context;
        this.memberList = memberList;
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.member_shape, parent, false);
        return new MemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder holder, int position) {
        Member member = memberList.get(position);
        holder.nameTextView.setText(member.getName());
        holder.genderTextView.setText(member.getGender());
        holder.ageTextView.setText(member.getAge());
        holder.profileImageView.setImageResource(member.getProfileImageResId());
    }

    @Override
    public int getItemCount() {
        return memberList.size();
    }

    public static class MemberViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImageView;
        TextView nameTextView;
        TextView genderTextView;
        TextView ageTextView;

        public MemberViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImageView = itemView.findViewById(R.id.image_profile);
            nameTextView = itemView.findViewById(R.id.text_name);
            genderTextView = itemView.findViewById(R.id.text_gender);
            ageTextView = itemView.findViewById(R.id.text_age);
        }
    }
}
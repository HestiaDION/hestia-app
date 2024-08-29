package com.example.hestia_app.presentation.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.hestia_app.R;
import com.example.hestia_app.domain.OnboardingItem;

import java.util.List;

public class OnboardingAdapter extends RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder> {

    private List<OnboardingItem> onboardingItems;

    public OnboardingAdapter(List<OnboardingItem> onboardingItems){
        this.onboardingItems = onboardingItems;
    }

    @NonNull
    @Override
    public OnboardingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OnboardingViewHolder(

                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_container_onboarding, parent, false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull OnboardingViewHolder holder, int position) {
        holder.setOnboardingData(onboardingItems.get(position));
    }

    @Override
    public int getItemCount() {
        return onboardingItems.size();
    }

    static class OnboardingViewHolder extends RecyclerView.ViewHolder{

        private TextView textExplanation;
        private LottieAnimationView lottieGif;

        OnboardingViewHolder(@NonNull View itemView) {
            super(itemView);
            textExplanation = itemView.findViewById(R.id.textExplanation);
            lottieGif= itemView.findViewById(R.id.lottieAnimation);


        }

        void setOnboardingData(OnboardingItem onboardingItem){
            textExplanation.setText(onboardingItem.getExplanationText());
            lottieGif.setAnimation(onboardingItem.getLottieGif());
        }
    }
}

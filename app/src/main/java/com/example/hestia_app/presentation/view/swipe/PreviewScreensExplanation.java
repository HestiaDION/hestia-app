package com.example.hestia_app.presentation.view.swipe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.hestia_app.R;
import com.example.hestia_app.presentation.view.LoginActivity;
import com.example.hestia_app.utils.OnboardingAdapter;
import com.example.hestia_app.utils.OnboardingItem;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class PreviewScreensExplanation extends AppCompatActivity {

    private OnboardingAdapter onboardingAdapter;
    private LinearLayout layoutOnboardingIndicators;
    private MaterialButton buttonNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_screens_explanation);

        layoutOnboardingIndicators = findViewById(R.id.layoutViewPagerIndicators);
        buttonNext = findViewById(R.id.buttonNext);

        setupOnboardingItems();

        ViewPager2 onboardingViewPager = findViewById(R.id.viewPager);
        onboardingViewPager.setAdapter(onboardingAdapter);

        setupOnboardingIndicators();
        setCurrentOnboardingIndicator(0);

        onboardingViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentOnboardingIndicator(position);
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onboardingViewPager.getCurrentItem() + 1 < onboardingAdapter.getItemCount()) {
                    onboardingViewPager.setCurrentItem(onboardingViewPager.getCurrentItem() + 1);

                } else{
                    startActivity(new android.content.Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                }
            }
        });
    }

    private void setupOnboardingItems(){

        List<OnboardingItem> onboardingItems = new ArrayList<>();

        OnboardingItem explanation1 = new OnboardingItem();
        explanation1.setExplanationText("O Héstia é um aplicativo de aluguel de moradias para universitários");
        explanation1.setLottieGif(R.raw.teste);

        OnboardingItem explanation2 = new OnboardingItem();
        explanation2.setExplanationText("Encontre casas com mais pessoas, com segurança e qualidade");
        explanation2.setLottieGif(R.raw.clipboard_animation);

        OnboardingItem explanation3 = new OnboardingItem();
        explanation3.setExplanationText("Ache as pessoas ideais com base no  nosso sistema de matching");
        explanation3.setLottieGif(R.raw.teste);

        onboardingItems.add(explanation1);
        onboardingItems.add(explanation2);
        onboardingItems.add(explanation3);

        onboardingAdapter = new OnboardingAdapter(onboardingItems);
    }

    private void setupOnboardingIndicators(){
        ImageView[] indicators = new ImageView[onboardingAdapter.getItemCount()];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );

        layoutParams.setMargins(0, 0, 0, 0);
        for(int i = 0; i < indicators.length; i++){
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(
                    ContextCompat.getDrawable(
                            getApplicationContext(),
                            R.drawable.onboarding_indicator_inactive
                    )
            );

            indicators[i].setLayoutParams(layoutParams);
            layoutOnboardingIndicators.addView(indicators[i]);
        }
    }

    private void setCurrentOnboardingIndicator(int index){
        int childCount = layoutOnboardingIndicators.getChildCount();

        for (int i = 0; i < childCount; i++){

            ImageView imageView = (ImageView) layoutOnboardingIndicators.getChildAt(i);
            if(i == index){
                imageView.setImageDrawable(
                      ContextCompat.getDrawable(getApplicationContext(), R.drawable.onboarding_indicate_active)
                );
                }
             else{

                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.onboarding_indicator_inactive)
                );
            }

        }

        if (index == onboardingAdapter.getItemCount() - 1){
            buttonNext.setText("Começar");
        }else{
            buttonNext.setText("Próximo");
        }
    }
}
package com.example.hestia_app.presentation.view.swipe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.LinearLayout;

import com.example.hestia_app.R;
import com.example.hestia_app.presentation.view.LoginActivity;
import com.example.hestia_app.presentation.view.adapter.OnboardingAdapter;
import com.example.hestia_app.domain.OnboardingItem;
import com.example.hestia_app.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

public class PreviewScreensExplanation extends AppCompatActivity {

    private OnboardingAdapter onboardingAdapter;
    private LinearLayout layoutOnboardingIndicators;
    private Button buttonNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_screens_explanation);

        layoutOnboardingIndicators = findViewById(R.id.layoutViewPagerIndicators);
        buttonNext = findViewById(R.id.buttonNext);

        setupOnboardingItems();

        ViewPager2 onboardingViewPager = findViewById(R.id.viewPager);
        onboardingViewPager.setAdapter(onboardingAdapter);

        // métodos de gerenciamento das bolinhas
        ViewUtils.setupOnboardingIndicators(this, layoutOnboardingIndicators, onboardingAdapter);
        ViewUtils.setCurrentOnboardingIndicator(this, 0, layoutOnboardingIndicators, onboardingAdapter, buttonNext);
        onboardingViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                ViewUtils.setCurrentOnboardingIndicator(PreviewScreensExplanation.this, position, layoutOnboardingIndicators, onboardingAdapter, buttonNext);
            }
        });

        // botão para mudar a tela
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

    // método para configurar os itens
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
        explanation3.setLottieGif(R.raw.watching_animation);

        onboardingItems.add(explanation1);
        onboardingItems.add(explanation2);
        onboardingItems.add(explanation3);

        onboardingAdapter = new OnboardingAdapter(onboardingItems);
    }

}
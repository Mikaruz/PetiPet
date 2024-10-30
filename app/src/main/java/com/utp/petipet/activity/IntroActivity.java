package com.utp.petipet.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;

import com.utp.petipet.AuthActivity;
import com.utp.petipet.R;
import com.utp.petipet.databinding.ActivityIntroBinding;

public class IntroActivity extends BaseActivity {



    private ActivityIntroBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivityIntroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.introStartButton.setOnClickListener(v -> {
            Intent intent = new Intent(IntroActivity.this, AuthActivity.class);
            startActivity(intent);
        });

        binding.loginTextView.setOnClickListener(v -> {
            Intent intent = new Intent(IntroActivity.this, AuthActivity.class);
            startActivity(intent);
        });
    }
}
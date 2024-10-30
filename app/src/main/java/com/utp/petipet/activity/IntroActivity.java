package com.utp.petipet.activity;

import android.content.Intent;
import android.os.Bundle;

import com.utp.petipet.MainActivity;
import com.utp.petipet.databinding.ActivityIntroBinding;

public class IntroActivity extends BaseActivity {

    private ActivityIntroBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityIntroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.introStartButton.setOnClickListener(v -> {
            Intent intent = new Intent(IntroActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }
}
package com.utp.petipet.activity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.utp.petipet.MainActivity;
import com.utp.petipet.ProviderType;
import com.utp.petipet.databinding.ActivityIntroBinding;

public class IntroActivity extends BaseActivity {
    private FirebaseAuth mAuth;

    private ActivityIntroBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        binding = ActivityIntroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.introStartButton.setOnClickListener(v -> {
            Intent intent = new Intent(IntroActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        binding.loginTextView.setOnClickListener(v -> {
            Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        // Verificar si ya hay un usuario conectado.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            showHome(currentUser.getEmail(), ProviderType.BASIC);
        }
    }

    private void showHome(String email, ProviderType provider) {
        Intent homeIntent = new Intent(this, MainActivity.class);
        homeIntent.putExtra("email", email);
        homeIntent.putExtra("provider", provider.name());
        startActivity(homeIntent);
        finish();
    }
}
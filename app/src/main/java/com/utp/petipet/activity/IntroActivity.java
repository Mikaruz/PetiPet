package com.utp.petipet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.utp.petipet.MainActivity;
import com.utp.petipet.ProviderType;
import com.utp.petipet.databinding.ActivityIntroBinding;
import com.utp.petipet.model.UserApp;

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
        FirebaseFirestore db = FirebaseFirestore.getInstance();




        if (currentUser != null) {
            String userId = currentUser.getUid();
            db.collection("user").document(userId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            // Convierte el documento en un objeto User
                            UserApp userApp = documentSnapshot.toObject(UserApp.class);
                            showHome(currentUser.getEmail(), ProviderType.BASIC, userApp.getName(), userApp.getPhoneNumber());
                        } else {
                            Log.e("Login", "No se encontraron datos del usuario.");
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("Login", "Error al obtener datos del usuario: " + e.getMessage());
                    });
        }
    }

    private void showHome(String email, ProviderType provider, String name, String phoneNumber) {
        Intent homeIntent = new Intent(this, MainActivity.class);
        homeIntent.putExtra("email", email);
        homeIntent.putExtra("provider", provider.name());
        homeIntent.putExtra("name", name);
        homeIntent.putExtra("phoneNumber", phoneNumber);
        startActivity(homeIntent);
    }
}
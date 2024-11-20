package com.utp.petipet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.utp.petipet.MainActivity;
import com.utp.petipet.ProviderType;
import com.utp.petipet.R;
import com.utp.petipet.model.UserApp;


public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        setup();
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
    private void setup() {
        Button registerButton = findViewById(R.id.registerButton);

        EditText emailEditText = findViewById(R.id.email_edit_text);
        EditText passwordEditText = findViewById(R.id.password_edit_text);

        EditText nameEditText = findViewById(R.id.name_edit_text);
        EditText phoneNumberEditText = findViewById(R.id.phone_number_edit_text);

        TextView goLogin = findViewById(R.id.go_login_text_view);

        registerButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String name = nameEditText.getText().toString().trim();
            String phoneNumber = phoneNumberEditText.getText().toString().trim();

            register(email, password, name, phoneNumber);
        });

        goLogin.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

    private void register(String email, String password, String name, String phoneNumber) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            FirebaseFirestore db = FirebaseFirestore.getInstance();

                            String userId = user.getUid();

                            // Crea un objeto User con los datos
                            UserApp userApp = new UserApp(name, email, phoneNumber);

                            // Guarda el objeto User en Firestore
                            db.collection("user").document(userId)
                                    .set(userApp)
                                    .addOnSuccessListener(aVoid -> {
                                        Log.d("Register", "Usuario registrado y datos guardados en Firestore.");
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e("Register", "Error al guardar datos: " + e.getMessage());
                                    });

                            showHome(user.getEmail(), ProviderType.BASIC, name, phoneNumber);
                        } else {
                            showAlert();
                        }
                    }
                });
    }


    private void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage("Se ha producido un error autenticando al usuario");
        builder.setPositiveButton("Aceptar", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showHome(String email, ProviderType provider, String name, String phoneNumber) {
        Intent homeIntent = new Intent(this, MainActivity.class);
        homeIntent.putExtra("email", email);
        homeIntent.putExtra("provider", provider.name());
        homeIntent.putExtra("name", name);
        homeIntent.putExtra("phoneNumber", phoneNumber);
        startActivity(homeIntent);
        finish();
    }
}


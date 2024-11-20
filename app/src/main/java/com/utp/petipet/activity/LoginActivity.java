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

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        setup();
    }


    private void setup() {
        Button loginButton = findViewById(R.id.loginButton);

        EditText emailEditText = findViewById(R.id.login_email_edit_text);
        EditText passwordEditText = findViewById(R.id.login_password_edit_text);

        TextView goRegister = findViewById(R.id.go_register_text_view);

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            login(email, password);
        });

        goRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            FirebaseFirestore db = FirebaseFirestore.getInstance();


                            String userId = user.getUid();

                            // Recupera los datos del usuario desde Firestore
                            db.collection("user").document(userId)
                                    .get()
                                    .addOnSuccessListener(documentSnapshot -> {
                                        if (documentSnapshot.exists()) {
                                            // Convierte el documento en un objeto User
                                            UserApp userApp = documentSnapshot.toObject(UserApp.class);
                                            showHome(email, ProviderType.BASIC, userApp.getName(), userApp.getPhoneNumber());
                                        } else {
                                            Log.e("Login", "No se encontraron datos del usuario.");
                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e("Login", "Error al obtener datos del usuario: " + e.getMessage());
                                    });

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

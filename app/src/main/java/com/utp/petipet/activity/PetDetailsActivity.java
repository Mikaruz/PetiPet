package com.utp.petipet.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.utp.petipet.R;

public class PetDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_details);


        // Referencias a las vistas
        TextView textNombre = findViewById(R.id.textDetalleNombre);
        TextView textRaza = findViewById(R.id.textDetalleRaza);
        ImageView imageMascota = findViewById(R.id.imageDetalleMascota);
        TextView textViewPetDescription = findViewById(R.id.text_view_pet_description);
        TextView textViewPetAge = findViewById(R.id.text_view_pet_age);

        // Obtener los datos pasados desde el Intent
        String nombre = getIntent().getStringExtra("petName");
        String raza = getIntent().getStringExtra("petSpecie");
        String imagenUrl = getIntent().getStringExtra("petImagenUrl");
        String petDescription = getIntent().getStringExtra("petDescription");
        String petAge = getIntent().getStringExtra("petAge");

        // Mostrar los datos en las vistas
        textNombre.setText(nombre);
        textRaza.setText(raza);
        textViewPetDescription.setText(petDescription);
        textViewPetAge.setText(petAge);

        Glide.with(this)
                .load(imagenUrl)
                .placeholder(R.drawable.pet_placeholder)
                .error(R.drawable.error_placeholder)
                .into(imageMascota);
    }
}
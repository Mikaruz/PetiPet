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
        ImageView imageMascota = findViewById(R.id.imageDetalleMascota);
        TextView textViewPetDescription = findViewById(R.id.text_view_pet_description);
        TextView textViewPetAge = findViewById(R.id.text_view_pet_age);
        ImageView imageViewPetSpecie = findViewById(R.id.pet_specie_image);

        // Obtener los datos pasados desde el Intent
        String nombre = getIntent().getStringExtra("petName");
        String raza = getIntent().getStringExtra("petSpecie");
        String imagenUrl = getIntent().getStringExtra("petImagenUrl");
        String petDescription = getIntent().getStringExtra("petDescription");
        String petAge = getIntent().getStringExtra("petAge");
        String gender = getIntent().getStringExtra("petGender");

        // Mostrar los datos en las vistas
        textNombre.setText(nombre);
        textViewPetDescription.setText(petDescription);
        textViewPetAge.setText(petAge);


        // Asignar la imagen según el género
        if ("Masculino".equals(gender)) { // Aquí 'specie' se usa como género; asegúrate de que sea correcto
            imageViewPetSpecie.setImageResource(R.drawable.male); // Reemplaza con tu imagen de recurso para masculino
        } else if ("Femenino".equals(gender)) {
            imageViewPetSpecie.setImageResource(R.drawable.female); // Reemplaza con tu imagen de recurso para femenino
        } else {
            imageViewPetSpecie.setImageResource(R.drawable.pet_placeholder); // Imagen por defecto si no es masculino ni femenino
        }

        Glide.with(this)
                .load(imagenUrl)
                .placeholder(R.drawable.pet_placeholder)
                .error(R.drawable.error_placeholder)
                .into(imageMascota);
    }
}
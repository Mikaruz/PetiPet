package com.utp.petipet.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.utp.petipet.R;

import org.w3c.dom.Text;

public class PetDetailsActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private String ownerPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_details);
        db = FirebaseFirestore.getInstance();

        // Referencias a las vistas
        TextView textNombre = findViewById(R.id.textDetalleNombre);
        ImageView imageMascota = findViewById(R.id.imageDetalleMascota);
        TextView nameOwner = findViewById(R.id.name_owner);
        TextView textViewPetDescription = findViewById(R.id.text_view_pet_description);
        TextView textViewPetAge = findViewById(R.id.text_view_pet_age);
        ImageView imageViewPetSpecie = findViewById(R.id.pet_specie_image);
        ImageView ownerImage = findViewById(R.id.owner_image);

        // Obtener los datos pasados desde el Intent
        String nombre = getIntent().getStringExtra("petName");
        String raza = getIntent().getStringExtra("petSpecie");
        String imagenUrl = getIntent().getStringExtra("petImagenUrl");
        String petDescription = getIntent().getStringExtra("petDescription");
        String petAge = getIntent().getStringExtra("petAge");
        String gender = getIntent().getStringExtra("petGender");
        String userId = getIntent().getStringExtra("userId");
        Button adoptButton = findViewById(R.id.adopt_button);


        // Mostrar los datos en las vistas
        textNombre.setText(nombre);
        textViewPetDescription.setText(petDescription);
        textViewPetAge.setText(petAge);


        if (userId != null) {
            db.collection("user").document(userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            DocumentSnapshot userSnapshot = task.getResult();
                            if (userSnapshot.exists()) {
                                String ownerName = userSnapshot.getString("name");
                                ownerPhoneNumber = userSnapshot.getString("phoneNumber");
                                String ownerPhotoUrl = userSnapshot.getString("imageUrl");

                                // Mostrar datos del dueño
                                nameOwner.setText(ownerName);
                                Glide.with(this).load(ownerPhotoUrl).into(ownerImage);
                            } else {
                                Toast.makeText(this, "No se encontró información del dueño.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, "Error al obtener datos del dueño.", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "No se proporcionó el ID del dueño.", Toast.LENGTH_SHORT).show();
        }


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

        // Funcionalidad del botón Adoptar
        adoptButton.setOnClickListener(v -> {
            if (ownerPhoneNumber != null && !ownerPhoneNumber.isEmpty()) {
                openWhatsApp(ownerPhoneNumber, "Hola, estoy interesado en adoptar a " + nombre);
            } else {
                Toast.makeText(this, "No se encontró el número del propietario.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    // Método para abrir WhatsApp
    private void openWhatsApp(String phoneNumber, String message) {
        try {
            String url = "https://api.whatsapp.com/send?phone=" + phoneNumber + "&text=" + Uri.encode(message);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "No se pudo abrir WhatsApp.", Toast.LENGTH_SHORT).show();
        }
    }
}
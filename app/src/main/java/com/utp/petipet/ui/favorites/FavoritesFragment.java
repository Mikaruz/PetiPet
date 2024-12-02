package com.utp.petipet.ui.favorites;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.utp.petipet.databinding.FragmentFavoritesBinding;
import com.utp.petipet.model.Pet;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

public class FavoritesFragment extends Fragment {

    private FragmentFavoritesBinding binding;
    private static final int REQUEST_IMAGE_PICK = 1001;

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private Uri selectedImageUri;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        FavoritesViewModel dashboardViewModel =
                new ViewModelProvider(this).get(FavoritesViewModel.class);

        binding = FragmentFavoritesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();


        // Configurar botón para seleccionar foto
        binding.selectPhotoButton.setOnClickListener(v -> openGallery());

        // Configurar botón para subir datos
        binding.addPetButton.setOnClickListener(v -> uploadPet());


        return root;
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            binding.petImageView.setImageURI(selectedImageUri);
        }
    }


    private void uploadPet() {
        String petName = binding.petNameEditText.getText().toString();
        String petDescription = binding.petDescriptionEditText.getText().toString();
        String petAge = binding.petAgeEditText.getText().toString();
        String petSpecies = binding.speciesSpinner.getSelectedItem().toString();
        String petGender = binding.genderSpinner.getSelectedItem().toString();

        if (TextUtils.isEmpty(petName) || TextUtils.isEmpty(petDescription) || TextUtils.isEmpty(petAge) || selectedImageUri == null) {
            Toast.makeText(getContext(), "Por favor complete todos los campos y seleccione una foto", Toast.LENGTH_SHORT).show();
            return;
        }

        // Subir imagen a Firebase Storage
        String imageFileName = UUID.randomUUID().toString() + ".jpg";
        StorageReference imageRef = storage.getReference().child("pet/" + imageFileName);

        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageData = baos.toByteArray();

            imageRef.putBytes(imageData).addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                // Guardar datos en Firestore
                savePetToFirestore(petName, petDescription, petAge, petSpecies, petGender, uri.toString());
            })).addOnFailureListener(e -> {
                Toast.makeText(getContext(), "Error al subir imagen: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void savePetToFirestore(String name, String description, String age, String species, String gender, String imageUrl) {
        String userId = auth.getCurrentUser().getUid();
        Pet pet = new Pet(name, description, species,  gender, Integer.parseInt(age), imageUrl, userId);

        db.collection("pet").add(pet).addOnSuccessListener(documentReference -> {
            Toast.makeText(getContext(), "Mascota puesta en adopción", Toast.LENGTH_SHORT).show();
            getActivity().onBackPressed();
        }).addOnFailureListener(e -> {
            Toast.makeText(getContext(), "Error al guardar datos: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
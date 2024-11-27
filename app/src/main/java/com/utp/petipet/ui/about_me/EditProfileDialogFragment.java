package com.utp.petipet.ui.about_me;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import com.utp.petipet.R;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.utp.petipet.databinding.DialogEditProfileBinding;

import java.util.HashMap;
import java.util.Map;

public class EditProfileDialogFragment extends DialogFragment {
    private DialogEditProfileBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private StorageReference storageRef;
    private Uri selectedImageUri; // Almacena la imagen seleccionada

    public EditProfileDialogFragment() {
        // Constructor vacío
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DialogEditProfileBinding.inflate(LayoutInflater.from(getContext()));
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference().child("user");

        // Configurar el botón de selección de imagen
        binding.selectImageButton.setOnClickListener(v -> selectImage());

        // Inflar el layout del título personalizado
        View customTitle = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_title, null);
        AlertDialog dialog = new AlertDialog.Builder(requireActivity(), R.style.CustomAlertDialogTheme)
                .setCustomTitle(customTitle)
                .setView(binding.getRoot())
                .setPositiveButton("Guardar", (dialog1, which) -> saveProfile())
                .setNegativeButton("Cancelar", (dialog1, which) -> dialog1.dismiss())
                .create();

        // Ajustar el tamaño del diálogo
        dialog.setOnShowListener(d -> {
            Window window = dialog.getWindow();
            if (window != null) {
                // Cambia el ancho del diálogo
                window.setLayout((int) (getResources().getDisplayMetrics().widthPixels * 0.9), // 90% del ancho de la pantalla
                        WindowManager.LayoutParams.WRAP_CONTENT); // Altura ajustada al contenido
            }
        });

        return dialog;
    }

    private void selectImage() {
        // Abrir un selector de imágenes
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Seleccionar Imagen"), 101);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            binding.imagePreview.setImageURI(selectedImageUri);
        }
    }

    private void saveProfile() {
        String name = binding.nameEditText.getText().toString().trim();
        String phone = binding.phoneEditText.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone)) {
            Toast.makeText(getContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = mAuth.getCurrentUser().getUid();
        if (selectedImageUri != null) {
            // Subir imagen al Storage
            StorageReference imageRef = storageRef.child(userId + ".jpg");
            imageRef.putFile(selectedImageUri).addOnSuccessListener(taskSnapshot ->
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        updateFirestore(name, phone, imageUrl);
                    })
            ).addOnFailureListener(e ->
                    Toast.makeText(getContext(), "Error al subir la imagen", Toast.LENGTH_SHORT).show());
        } else {
            // Actualizar solo datos sin imagen
            updateFirestore(name, phone, null);
        }
    }

    private void updateFirestore(String name,String phone, @Nullable String imageUrl) {
        String userId = mAuth.getCurrentUser().getUid();
        Map<String, Object> userUpdates = new HashMap<>();
        userUpdates.put("name", name);
        userUpdates.put("phoneNumber", phone);
        if (imageUrl != null) {
            userUpdates.put("imageUrl", imageUrl);
        }

        db.collection("user").document(userId).update(userUpdates)
                .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Perfil actualizado", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error al actualizar el perfil", Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        binding = null;
    }
}

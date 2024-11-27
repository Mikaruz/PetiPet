package com.utp.petipet.ui.about_me;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.utp.petipet.R;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.utp.petipet.activity.IntroActivity;
import com.utp.petipet.databinding.FragmentAboutMeBinding;

public class AboutMeFragment extends Fragment {
    private FirebaseAuth mAuth;
    private FragmentAboutMeBinding binding;
    private FirebaseFirestore db;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AboutMeViewModel aboutMeViewModel =
                new ViewModelProvider(requireActivity()).get(AboutMeViewModel.class);
        binding = FragmentAboutMeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mAuth = FirebaseAuth.getInstance();

        binding.logoutButton.setOnClickListener(v -> logout());

        db = FirebaseFirestore.getInstance();
        String userId = mAuth.getCurrentUser().getUid();

        getUserData(userId);



        binding.editMeButton.setOnClickListener(v -> {
            EditProfileDialogFragment dialog = new EditProfileDialogFragment();
            dialog.show(getParentFragmentManager(), "EditProfileDialog");
        });




        return root;
    }

    private void getUserData(String userId) {
        // Referencia al documento del usuario
        DocumentReference userDocRef = db.collection("user").document(userId);

        userDocRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Extraer datos del documento
                        String name = documentSnapshot.getString("name");
                        String email = documentSnapshot.getString("email");
                        String phoneNumber = documentSnapshot.getString("phoneNumber");
                        String imageUrl = documentSnapshot.getString("imageUrl");

                        // Actualizar la interfaz con los datos
                        binding.aboutMeNameTextView.setText(name != null ? name : "No disponible");
                        binding.aboutMeEmailTextView.setText(email != null ? email : "No disponible");
                        binding.aboutMePhoneNumberTextView.setText(phoneNumber != null ? phoneNumber : "No disponible");

                        // Cargar la imagen (usa Glide o Picasso)
                        if (imageUrl != null && !imageUrl.isEmpty()) {
                            Glide.with(requireContext())
                                    .load(imageUrl)
                                    .placeholder(R.drawable.user_placeholder)
                                    .into(binding.shapeableImageView);
                        }
                    } else {
                        Log.e("Firestore", "No se encontró el documento del usuario.");
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error al obtener datos del usuario", e));
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void logout() {
        mAuth.signOut();
        Intent authIntent = new Intent(requireActivity(), IntroActivity.class);
        startActivity(authIntent);
    }
}
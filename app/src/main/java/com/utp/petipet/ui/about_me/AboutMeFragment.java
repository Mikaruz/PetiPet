package com.utp.petipet.ui.about_me;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.utp.petipet.activity.IntroActivity;
import com.utp.petipet.databinding.FragmentAboutMeBinding;

public class AboutMeFragment extends Fragment {
    private FirebaseAuth mAuth;
    private FragmentAboutMeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AboutMeViewModel aboutMeViewModel =
                new ViewModelProvider(requireActivity()).get(AboutMeViewModel.class);
        binding = FragmentAboutMeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mAuth = FirebaseAuth.getInstance();

        binding.logoutButton.setOnClickListener(v -> logout());


        aboutMeViewModel.getEmail().observe(getViewLifecycleOwner(), email -> {
            binding.aboutMeEmailTextView.setText(email);
        });

        aboutMeViewModel.getPhoneNumber().observe(getViewLifecycleOwner(), phoneNumber -> {
            binding.aboutMePhoneNumberTextView.setText(phoneNumber);
        });

        aboutMeViewModel.getName().observe(getViewLifecycleOwner(), name -> {
            binding.aboutMeNameTextView.setText(name);
        });






        return root;
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
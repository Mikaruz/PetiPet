package com.utp.petipet.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.utp.petipet.MainActivity;
import com.utp.petipet.activity.IntroActivity;
import com.utp.petipet.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FragmentHomeBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        HomeViewModel homeViewModel =
                new ViewModelProvider(requireActivity()).get(HomeViewModel.class); // Asegúrate de usar requireActivity()

        binding = FragmentHomeBinding.inflate(getLayoutInflater() );
        View root = binding.getRoot();

        ((MainActivity) getActivity()).getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();

/*
        // Observa los datos en el ViewModel y actualiza los TextViews
        homeViewModel.getEmail().observe(getViewLifecycleOwner(), email -> {
            binding.USER.setText(email);
        });

        homeViewModel.getProvider().observe(getViewLifecycleOwner(), provider -> {
            binding.PROVIDER.setText(provider);
        });*/

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



}

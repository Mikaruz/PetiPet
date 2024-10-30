package com.utp.petipet.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.utp.petipet.AuthActivity;
import com.utp.petipet.MainActivity;
import com.utp.petipet.R;
import com.utp.petipet.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FragmentHomeBinding binding;
    Button logoutButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        logoutButton = root.findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(v -> logout());
        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void logout(){
        mAuth.signOut();
        Intent authIntent = new Intent(requireActivity(), AuthActivity.class);
        startActivity(authIntent);
    }
}
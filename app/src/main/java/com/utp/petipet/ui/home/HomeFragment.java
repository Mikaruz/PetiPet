package com.utp.petipet.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.utp.petipet.MainActivity;
import com.utp.petipet.R;
import com.utp.petipet.activity.IntroActivity;
import com.utp.petipet.activity.PetDetailsActivity;
import com.utp.petipet.adapter.PetAdapter;
import com.utp.petipet.databinding.FragmentHomeBinding;
import com.utp.petipet.model.Pet;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FragmentHomeBinding binding;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        HomeViewModel homeViewModel =
                new ViewModelProvider(requireActivity()).get(HomeViewModel.class); // Asegúrate de usar requireActivity()

        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();

        ((MainActivity) getActivity()).getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();

        binding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        List<Pet> petList = new ArrayList<>();
        PetAdapter adapter = new PetAdapter(petList);
        binding.recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(pet -> {
            Intent intent = new Intent(getContext(), PetDetailsActivity.class);
            intent.putExtra("petName", pet.getName());
            intent.putExtra("petSpecie", pet.getSpecie());
            intent.putExtra("petImagenUrl", pet.getImageUrl());
            intent.putExtra("petDescription", pet.getDescription());
            intent.putExtra("petAge", pet.getAge() + " meses");
            startActivity(intent);
        });

        db.collection("pet")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Pet pet = document.toObject(Pet.class);
                            petList.add(pet);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.w("Firestore", "Error al obtener documentos.", task.getException());
                    }
                });


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

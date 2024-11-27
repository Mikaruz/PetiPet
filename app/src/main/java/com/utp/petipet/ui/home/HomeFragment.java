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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.utp.petipet.MainActivity;
import com.utp.petipet.R;
import com.utp.petipet.activity.PetDetailsActivity;
import com.utp.petipet.adapter.PetAdapter;
import com.utp.petipet.databinding.FragmentHomeBinding;
import com.utp.petipet.model.Pet;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FragmentHomeBinding binding;
    private FirebaseFirestore db;
    private List<Pet> originalPetList = new ArrayList<>();
    private List<Pet> filteredPetList = new ArrayList<>();
    private PetAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        HomeViewModel homeViewModel =
                new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();

        ((MainActivity) getActivity()).getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Configuración del RecyclerView
        binding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter = new PetAdapter(filteredPetList);
        binding.recyclerView.setAdapter(adapter);

        // Configurar el listener para clics en los elementos
        adapter.setOnItemClickListener(pet -> {
            Intent intent = new Intent(getContext(), PetDetailsActivity.class);
            intent.putExtra("petName", pet.getName());
            intent.putExtra("petSpecie", pet.getSpecie());
            intent.putExtra("petImagenUrl", pet.getImageUrl());
            intent.putExtra("petDescription", pet.getDescription());
            intent.putExtra("petGender", pet.getGender());
            intent.putExtra("petAge", pet.getAge() + " meses");
            startActivity(intent);
        });

        // Establecer color o estilo visual inicial del botón "Todos"
        binding.btnTodos.setBackgroundColor(getResources().getColor(R.color.main_purple));
        binding.btnGatos.setBackgroundColor(getResources().getColor(R.color.black));
        binding.btnPerros.setBackgroundColor(getResources().getColor(R.color.black));

        // Configuración de botones
        binding.btnGatos.setOnClickListener(v -> {
            filtrarMascotas("Gato");
            updateButtonStyles("Gato");
        });

        binding.btnPerros.setOnClickListener(v -> {
            filtrarMascotas("Perro");
            updateButtonStyles("Perro");
        });

        binding.btnTodos.setOnClickListener(v -> {
            filtrarMascotas("Todos");
            updateButtonStyles("Todos");
        });

        // Cargar datos desde Firestore y filtrar por "todos" al inicio
        cargarMascotasDesdeFirestore();

        return root;
    }

    private void cargarMascotasDesdeFirestore() {
        db.collection("pet")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        originalPetList.clear(); // Limpiar la lista original para evitar duplicados
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Pet pet = document.toObject(Pet.class);
                            originalPetList.add(pet); // Agregar cada mascota a la lista original
                        }
                        filtrarMascotas("Todos"); // Filtrar automáticamente con "todos" al cargar
                    } else {
                        Log.w("Firestore", "Error al obtener documentos.", task.getException());
                    }
                });
    }

    private void filtrarMascotas(String especie) {
        filteredPetList.clear();

        if (especie.equals("Todos")) {
            filteredPetList.addAll(originalPetList);
        } else {
            for (Pet pet : originalPetList) {
                if (pet.getSpecie().equalsIgnoreCase(especie)) {
                    filteredPetList.add(pet);
                }
            }
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void updateButtonStyles(String selectedType) {
        int selectedColor = getResources().getColor(R.color.main_purple);
        int unselectedColor = getResources().getColor(R.color.black);

        binding.btnTodos.setBackgroundColor(selectedType.equals("Todos") ? selectedColor : unselectedColor);
        binding.btnGatos.setBackgroundColor(selectedType.equals("Gato") ? selectedColor : unselectedColor);
        binding.btnPerros.setBackgroundColor(selectedType.equals("Perro") ? selectedColor : unselectedColor);
    }
}

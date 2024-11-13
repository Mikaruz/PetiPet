package com.utp.petipet;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.utp.petipet.activity.IntroActivity;
import com.utp.petipet.databinding.ActivityMainBinding;
import com.utp.petipet.ui.about_me.AboutMeFragment;
import com.utp.petipet.ui.about_me.AboutMeViewModel;
import com.utp.petipet.ui.home.HomeFragment;
import com.utp.petipet.ui.home.HomeViewModel;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        // Configuración de la barra de navegación
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);


        // Obtener datos del usuario
        String email = getIntent().getStringExtra("email");
        String provider = getIntent().getStringExtra("provider");

        // Crear el ViewModel y pasar los datos
        HomeViewModel viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        viewModel.setEmail(email);
        viewModel.setProvider(provider);

        AboutMeViewModel aboutMeViewModel = new ViewModelProvider(this).get(AboutMeViewModel.class);
        aboutMeViewModel.setEmail(email);
        aboutMeViewModel.setProvider(provider);


        /*getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_home, new HomeFragment())
                .commit();*/
    }

    @Override
    public void onStart() {
        super.onStart();

        // Verificar si ya hay un usuario conectado.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            Intent intent = new Intent(this, IntroActivity.class);
            startActivity(intent);
        }
    }
}
package com.example.power_home;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;

public class DrawerActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private String email; // stocker l’email globalement

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationView);

        // Récupérer l’email passé par LoginActivity
        email = getIntent().getStringExtra("email");
        Habitat habitat = HabitatRepository.getHabitatByEmail(email);

        // Mettre à jour les infos du header
        View headerView = navigationView.getHeaderView(0);
        TextView navEmail = headerView.findViewById(R.id.header_email);
        TextView navName = headerView.findViewById(R.id.header_nom);

        navEmail.setText(email);
        if (habitat != null) {
            navName.setText(habitat.residentName);
        }

        // Toggle Drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Ouvrir par défaut la liste des habitats
        if (savedInstanceState == null) {
            openFragmentWithEmail(new ListHabitatFragment(), email);
            navigationView.setCheckedItem(R.id.nav_liste_habitats);
        }

        // Navigation entre les fragments
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_liste_habitats) {
                    openFragmentWithEmail(new ListHabitatFragment(), email);
                } else if (id == R.id.nav_mon_habitat) {
                    openFragmentWithEmail(new MonHabitatFragment(), email);
                } else if (id == R.id.nav_notifications) {
                    openFragmentWithEmail(new MesNotifFragment(), email);
                } else if (id == R.id.nav_preferences) {
                    openFragmentWithEmail(new MesPreferenceFragment(), email);
                } else if (id == R.id.nav_apropos) {
                    showAProposDialog();
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    // Méthode pour passer un fragment avec l'email
    private void openFragmentWithEmail(Fragment fragment, String email) {
        Bundle bundle = new Bundle();
        bundle.putString("email", email);
        fragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, fragment)
                .commit();
    }

    // Affichage de la boîte "À propos"
    private void showAProposDialog() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("À propos")
                .setMessage("Application PowerHome\nDéveloppée par Ismail Atchekzai\nVersion 1.0")
                .setPositiveButton("OK", null)
                .show();
    }

    // Gestion du bouton retour
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}

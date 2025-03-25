package com.example.myapplication;

import android.os.Bundle;
import android.view.MenuItem;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer); // ← Ton layout personnalisé

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationView);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Fragment par défaut
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment, new ListHabitatFragment())
                    .commit();
            navigationView.setCheckedItem(R.id.nav_liste_habitats);
        }



        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                int id = item.getItemId();

                if (id == R.id.nav_liste_habitats) {
                    selectedFragment = new ListHabitatFragment();
                } else if (id == R.id.nav_mon_habitat) {
                    selectedFragment = new MonHabitatFragment();
                } else if (id == R.id.nav_notifications) {
                    selectedFragment = new MesNotifFragment();
                } else if (id == R.id.nav_preferences) {
                    selectedFragment = new MesPreferenceFragment();
                } else if (id == R.id.nav_apropos) {
                    showAProposDialog();
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment, selectedFragment)
                            .commit();
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }

    private void showAProposDialog() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("À propos")
                .setMessage("Application PowerHome\nDéveloppée par Ismail Atchekzai\nVersion 1.0")
                .setPositiveButton("OK", null)
                .show();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}

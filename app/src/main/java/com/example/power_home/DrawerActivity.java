package com.example.power_home;

import android.content.SharedPreferences;
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

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DrawerActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    private String email; // récupéré via SharedPreferences

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer);

        // 🔒 Récupérer l'email stocké après connexion
        SharedPreferences prefs = getSharedPreferences("power_home_prefs", MODE_PRIVATE);
        email = prefs.getString("email", null);

        // 🛠️ UI setup
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationView);

        // 📥 Header : nom, email, étage
        View headerView = navigationView.getHeaderView(0);
        TextView navName = headerView.findViewById(R.id.header_nom);
        TextView navEmail = headerView.findViewById(R.id.header_email);
        TextView navEtage = headerView.findViewById(R.id.header_etage);

        navEmail.setText(email); // On affiche l’email directement

        // 🌐 Charger nom & étage depuis la BDD
        new Thread(() -> {
            try {
                URL url = new URL("http://10.0.2.2/api/getUserInfo.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("email", email);

                OutputStream os = conn.getOutputStream();
                os.write(jsonParam.toString().getBytes("UTF-8"));
                os.close();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();
                JSONObject json = new JSONObject(response.toString());

                if (json.getBoolean("success")) {
                    String name = json.getString("name");
                    String etage = json.get("etage").toString(); // Peut être "2" ou "N/A"

                    runOnUiThread(() -> {
                        navName.setText(name);
                        navEtage.setText("Étage : " + etage);
                    });
                }

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    navName.setText("Erreur");
                    navEtage.setText("N/A");
                });
            }
        }).start();

        // 🎛️ Drawer toggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // 📦 Ouvrir par défaut la liste des habitats
        if (savedInstanceState == null) {
            openFragmentWithEmail(new ListHabitatFragment(), email);
            navigationView.setCheckedItem(R.id.nav_liste_habitats);
        }

        // 📚 Navigation
        navigationView.setNavigationItemSelectedListener(item -> {
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
        });
    }

    // Fragment passer avec email
    private void openFragmentWithEmail(Fragment fragment, String email) {
        Bundle bundle = new Bundle();
        bundle.putString("email", email);
        fragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, fragment)
                .commit();
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

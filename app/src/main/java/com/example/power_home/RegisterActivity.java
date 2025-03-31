package com.example.power_home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterActivity extends AppCompatActivity {
    EditText emailInput, passwordInput, prenomInput, nomInput, telephoneInput;
    Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Récupération des champs
        emailInput = findViewById(R.id.emailinput);
        passwordInput = findViewById(R.id.passwordinput);
        prenomInput = findViewById(R.id.prenominput);
        nomInput = findViewById(R.id.nominput);
        telephoneInput = findViewById(R.id.telephoneinput);
        EditText etageInput = findViewById(R.id.etageinput);
        registerButton = findViewById(R.id.loginbutton); // Change l’ID plus tard si besoin

        registerButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            String prenom = prenomInput.getText().toString().trim();
            String nom = nomInput.getText().toString().trim()+ " "+ prenom;
            String telephone = telephoneInput.getText().toString().trim();
            String etageStr = etageInput.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty() || prenom.isEmpty() || nom.isEmpty() || telephone.isEmpty()|| etageStr.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            registerUser( nom, email, password, telephone , etageStr);

        });
        ImageView backbutton = findViewById(R.id.retourlogin);
        backbutton.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

    private void registerUser(String name, String email, String password, String telephone , String etageStr) {
        new Thread(() -> {
            try {
                URL url = new URL("http://10.0.2.2/api/register.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                // Préparer les données JSON
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("name", name);
                jsonParam.put("email", email);
                jsonParam.put("password", password);
                jsonParam.put("telephone", telephone);
                jsonParam.put("etage", etageStr);


                // Envoyer le JSON au serveur
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(jsonParam.toString());
                writer.flush();
                writer.close();
                os.close();

                // Lire la réponse
                int responseCode = conn.getResponseCode();
                InputStream is = (responseCode >= 400) ? conn.getErrorStream() : conn.getInputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(is));

                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();

                JSONObject jsonResponse = new JSONObject(response.toString());
                boolean success = jsonResponse.getBoolean("success");
                String message = jsonResponse.getString("message");

                runOnUiThread(() -> {
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    if (success) {
                        // Sauvegarder l'email
                        getSharedPreferences("power_home_prefs", MODE_PRIVATE)
                                .edit()
                                .putString("email", email)
                                .apply();


                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            Intent intent = new Intent(RegisterActivity.this, DrawerActivity.class);
                            startActivity(intent);
                            finish();
                        }, 1000);
                    }
                });

            } catch (Exception e) {
                Log.e("REGISTER_ERROR", "Erreur réseau : " + e.getMessage());
                runOnUiThread(() ->
                        Toast.makeText(getApplicationContext(), "Erreur réseau", Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }
    }

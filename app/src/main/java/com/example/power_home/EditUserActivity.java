package com.example.power_home;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class EditUserActivity extends AppCompatActivity {

    EditText nomInput, prenomInput, emailInput, telephoneInput, passwordInput;
    Button saveButton;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        // Initialisation des champs
        nomInput = findViewById(R.id.nominput);
        prenomInput = findViewById(R.id.prenominput);
        emailInput = findViewById(R.id.emailinput);
        telephoneInput = findViewById(R.id.telephoneinput);
        passwordInput = findViewById(R.id.passwordinput);
        saveButton = findViewById(R.id.saveButton);

        // Récupère l'email passé depuis l'activité précédente
        SharedPreferences prefs = getSharedPreferences("power_home_prefs", MODE_PRIVATE);
        userEmail = prefs.getString("email", null);
        if (userEmail != null && !userEmail.isEmpty()) {
            fetchUserData(userEmail);
        }

        saveButton.setOnClickListener(v -> updateUser());
    }

    private void fetchUserData(String email) {
        new Thread(() -> {
            try {
                URL url = new URL("http://10.0.2.2/api/get_user.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("email", email);

                OutputStream os = conn.getOutputStream();
                os.write(jsonParam.toString().getBytes("UTF-8"));
                os.close();

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();

                JSONObject json = new JSONObject(response.toString());
                if (json.getBoolean("success")) {
                    String name = json.optString("name");
                    String etage = json.optString("etage");
                    String telephone = json.optString("telephone");
                    String[] parts = name.split(" ", 2);
                    String prenom = parts.length > 0 ? parts[0] : "";
                    String nom = parts.length > 1 ? parts[1] : "";

                    runOnUiThread(() -> {
                        nomInput.setText(nom);
                        prenomInput.setText(prenom);
                        emailInput.setText(email);
                        telephoneInput.setText(telephone);
                    });


                } else {
                    runOnUiThread(() -> {
                        try {
                            Toast.makeText(this, "Erreur : " + json.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() ->
                        Toast.makeText(this, "Erreur de chargement des données", Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }
    private void updateUser() {
        String nom = nomInput.getText().toString().trim();
        String prenom = prenomInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String telephone = telephoneInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        new Thread(() -> {
            try {
                URL url = new URL("http://10.0.2.2/api/update_user.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("nom", nom);
                jsonParam.put("prenom", prenom);
                jsonParam.put("email", email);
                jsonParam.put("telephone", telephone);
                if (!password.isEmpty()) {
                    jsonParam.put("password", password); // facultatif
                }

                OutputStream os = conn.getOutputStream();
                os.write(jsonParam.toString().getBytes("UTF-8"));
                os.close();

                InputStream is = (conn.getResponseCode() >= 400) ? conn.getErrorStream() : conn.getInputStream();
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

                runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_LONG).show());

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() ->
                        Toast.makeText(this, "Erreur réseau ou serveur", Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }
}

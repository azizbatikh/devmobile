package com.example.power_home;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
        registerButton = findViewById(R.id.loginbutton); // Change l’ID plus tard si besoin

        registerButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            String prenom = prenomInput.getText().toString().trim();
            String nom = nomInput.getText().toString().trim()+ " "+ prenom;
            String telephone = telephoneInput.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty() || prenom.isEmpty() || nom.isEmpty() || telephone.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            registerUser( nom, email, password, telephone);
        });
    }

    private void registerUser(String nom, String email, String password, String telephone) {
        new Thread(() -> {
            try {
                URL url = new URL("http://10.0.2.2/api/register.php"); // ⬅️ Le bon script ici
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("name", nom);
                jsonParam.put("email", email);
                jsonParam.put("password", password);
                jsonParam.put("telephone", telephone);

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

                JSONObject jsonResponse = new JSONObject(response.toString());
                boolean success = jsonResponse.getBoolean("success");
                String message = jsonResponse.getString("message");

                runOnUiThread(() -> {
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    if (success) {
//                        Intent intent = new Intent(RegisterActivity.this, .class);
//                        startActivity(intent);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() ->
                        Toast.makeText(getApplicationContext(), "Erreur réseau", Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }
}

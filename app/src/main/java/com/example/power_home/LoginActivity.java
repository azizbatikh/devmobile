package com.example.power_home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    EditText emailInput, passwordInput;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordinput);
        loginButton = findViewById(R.id.connectionbutton);
        Button mdpoublie = findViewById(R.id.MdpOublieButton);

        loginButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            loginUser(email, password);
        });

        mdpoublie.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });
    }

    private void loginUser(String email, String password) {
        // Simule une "latence" réseau
        new Thread(() -> {
            try {
                Thread.sleep(1000); // optionnel, juste pour le réalisme

                runOnUiThread(() -> {
                    // Simule une réponse JSON avec success = true
                    boolean success = true;
                    String message = "Connexion simulée réussie";

                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                    if (success) {
                        Intent intent = new Intent(LoginActivity.this, DrawerActivity.class);
                        intent.putExtra("email", email);
                        startActivity(intent);
                        finish();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() ->
                        Toast.makeText(getApplicationContext(), "Erreur réseau simulée", Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }

}

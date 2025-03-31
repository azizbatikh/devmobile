package com.example.power_home;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText emailInput;
    Button recoverButton;
    TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailInput = findViewById(R.id.emailInput);
        recoverButton = findViewById(R.id.recoverButton);
        resultText = findViewById(R.id.resultText);

        recoverButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(this, "Veuillez entrer un email", Toast.LENGTH_SHORT).show();
                return;
            }

            recoverPassword(email);
        });
        Button retourlogin = findViewById(R.id.retourlogin);
        retourlogin.setOnClickListener(v -> {
            finish();
        });
    }

    private void recoverPassword(String email) {
        new Thread(() -> {
            try {
                URL url = new URL("http://10.0.2.2/api/forgot_password.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                JSONObject jsonParam = new JSONObject();
                Log.d("EmailSent", email);
                jsonParam.put("email", email);

                OutputStream os = conn.getOutputStream();
                os.write(jsonParam.toString().getBytes("UTF-8"));
                os.close();

                InputStream is = conn.getInputStream();
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
                    if (success) {
                        resultText.setText("Mot de passe : " + message);
                    } else {
                        resultText.setText("Erreur : " + message);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> resultText.setText("Erreur réseau ou serveur"));
            }
        }).start();
    }
}

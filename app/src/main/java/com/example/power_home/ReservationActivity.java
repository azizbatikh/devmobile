package com.example.power_home;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReservationActivity extends AppCompatActivity {
    Spinner equipmentSpinner, timeSlotSpinner;
    Button reserveButton;
    String selectedDate = "";

    String userEmail ;

    List<String> equipmentNames = new ArrayList<>();
    Map<String, Integer> equipmentMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        equipmentSpinner = findViewById(R.id.equipmentSpinner);
        timeSlotSpinner = findViewById(R.id.timeSlotSpinner);
        reserveButton = findViewById(R.id.reserveButton);

        setupTimeSlots();
        showDatePicker();
        loadEquipments();
        SharedPreferences prefs = getSharedPreferences("power_home_prefs", MODE_PRIVATE);
        userEmail = prefs.getString("email", null);

        reserveButton.setOnClickListener(v -> {
            String selectedEquipment = (String) equipmentSpinner.getSelectedItem();
            String selectedTimeSlot = (String) timeSlotSpinner.getSelectedItem();

            if (selectedEquipment == null || selectedTimeSlot == null || selectedDate.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            int equipmentId = equipmentMap.get(selectedEquipment);
            reserveSlot(userEmail, equipmentId, selectedDate, selectedTimeSlot);
        });
    }

    private void setupTimeSlots() {
        String[] timeSlots = {
                "08:00 - 09:00", "09:00 - 10:00", "10:00 - 11:00",
                "11:00 - 12:00", "13:00 - 14:00", "15:00 - 16:00",
                "17:00 - 18:00", "18:00 - 19:00"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, timeSlots);
        timeSlotSpinner.setAdapter(adapter);
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void loadEquipments() {
        new Thread(() -> {
            try {
                URL url = new URL("http://10.0.2.2/api/get_equipment.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();

                JSONObject json = new JSONObject(response.toString());
                if (json.getBoolean("success")) {
                    JSONArray equipmentArray = json.getJSONArray("equipments");

                    for (int i = 0; i < equipmentArray.length(); i++) {
                        JSONObject equipment = equipmentArray.getJSONObject(i);
                        String name = equipment.getString("nom");
                        int id = equipment.getInt("id");
                        equipmentNames.add(name);
                        equipmentMap.put(name, id);
                    }

                    runOnUiThread(() -> {
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, equipmentNames);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        equipmentSpinner.setAdapter(adapter);
                    });
                } else {
                    runOnUiThread(() -> Toast.makeText(this, "Erreur: Pas d'équipements trouvés", Toast.LENGTH_SHORT).show());
                }

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Erreur réseau", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void reserveSlot(String email, int equipmentId, String date, String timeSlot) {
        new Thread(() -> {
            try {
                URL url = new URL("http://10.0.2.2/api/add_reservation.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setDoOutput(true);

                JSONObject json = new JSONObject();
                json.put("email", email);
                json.put("equipment_id", equipmentId);
                json.put("date", date);
                json.put("time_slot", timeSlot);

                OutputStream os = conn.getOutputStream();
                os.write(json.toString().getBytes("UTF-8"));
                os.close();

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) response.append(line);
                in.close();

                JSONObject jsonResponse = new JSONObject(response.toString());
                Log.d("DEBUG_RESPONSE", response.toString());
                boolean success = jsonResponse.getBoolean("success");
                String message = jsonResponse.getString("message");

                runOnUiThread(() -> {
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                    if (success) {
                        Intent intent = new Intent(ReservationActivity.this, CalendarActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Erreur de réservation", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}

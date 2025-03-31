package com.example.power_home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CalendarActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CalendarAdapter calendarAdapter;
    private List<DayItem> days = new ArrayList<>();
    private Map<String, Integer> consommationMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 7));


        calendarAdapter = new CalendarAdapter(days, consommationMap);
        recyclerView.setAdapter(calendarAdapter);
        Log.d("Calendar", "Adapter attaché avec " + days.size() + " jours");

        fetchConsumptionData();

        Button btn = findViewById(R.id.reservationButton);
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(CalendarActivity.this, DrawerActivity.class);
            startActivity(intent);
            finish();
        }, 3000);
    }

    private void fetchConsumptionData() {
        new Thread(() -> {
            try {
                URL url = new URL("http://10.0.2.2/api/get_consumption.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();


                Log.d("API Response", response.toString());

                JSONObject jsonResponse = new JSONObject(response.toString());
                if (jsonResponse.getBoolean("success")) {
                    JSONArray consumptionArray = jsonResponse.getJSONArray("consumption");
                    runOnUiThread(() -> {
                        processConsumptionData(consumptionArray);
                    });
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(getApplicationContext(), "Aucune consommation trouvée", Toast.LENGTH_SHORT).show();
                    });
                }

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(getApplicationContext(), "Erreur réseau", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    private void processConsumptionData(JSONArray consumptionDataList) {
        consommationMap.clear();
        days.clear();

        for (int i = 0; i < consumptionDataList.length(); i++) {
            try {
                JSONObject data = consumptionDataList.getJSONObject(i);
                String date = data.getString("date");
                int consommation = data.getInt("consommation");
                consommationMap.put(date, consommation);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        generateDays();


        runOnUiThread(() -> calendarAdapter.notifyDataSetChanged());
    }


    private void generateDays() {
        days.clear();
        for (int i = 1; i <= 31; i++) {
            days.add(new DayItem(i, 3, 2025));
        }
        days.add(0, null); // pour décaler le 1er mars au bon jour
        Log.d("Calendar", "Nombre de jours générés: " + days.size());
    }

}
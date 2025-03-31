package com.example.power_home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MonHabitatFragment extends Fragment {

    public MonHabitatFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mon_habitat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Mon habitat");
        }

        String email = getArguments() != null ? getArguments().getString("email") : "";

        // Récupérer les vues
        TextView nameView = view.findViewById(R.id.nomResident);
        TextView etageView = view.findViewById(R.id.etage);
        TextView consoView = view.findViewById(R.id.consoTotale);
        LinearLayout appareilsLayout = view.findViewById(R.id.listeAppareils);

        // Appel API
        new Thread(() -> {
            try {
                URL url = new URL("http://10.0.2.2/api/getMonHabitat.php");
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
                    int etage = json.getInt("etage");
                    int consommation = json.getInt("consommation");
                    JSONArray appareils = json.getJSONArray("equipments");

                    requireActivity().runOnUiThread(() -> {
                        nameView.setText(name);
                        etageView.setText("Étage : " + etage);
                        consoView.setText("Consommation estimée : " + consommation + " kWh");

                        appareilsLayout.removeAllViews();

                        for (int i = 0; i < appareils.length(); i++) {
                            try {
                                JSONObject obj = appareils.getJSONObject(i);
                                String nom = obj.getString("nom");

                                ImageView image = new ImageView(getContext());
                                image.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
                                image.setPadding(10, 10, 10, 10);

                                switch (nom) {
                                    case "aspirateur":
                                        image.setImageResource(R.drawable.ic_aspirateur);
                                        break;
                                    case "machine_a_laver":
                                        image.setImageResource(R.drawable.ic_machine_a_laver);
                                        break;
                                    case "climatiseur":
                                        image.setImageResource(R.drawable.ic_climatiseur);
                                        break;
                                    case "fer_a_repasser":
                                        image.setImageResource(R.drawable.ic_fer_a_repasser);
                                        break;
                                    default:
                                        image.setImageResource(R.drawable.ic_launcher_background);
                                        break;
                                }

                                appareilsLayout.addView(image);
                            } catch (Exception ignored) {}
                        }
                    });
                } else {
                    requireActivity().runOnUiThread(() ->
                            nameView.setText("Habitat non trouvé")
                    );
                }

            } catch (Exception e) {
                e.printStackTrace();
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Erreur lors du chargement", Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }
}

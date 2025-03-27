package com.example.power_home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ListHabitatFragment extends Fragment {

    private final boolean MODE_DEBUG = false;

    public ListHabitatFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_listhabitat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Liste des habitats");
        }

        String emailConnecte = getArguments() != null ? getArguments().getString("email") : "";
        ListView listView = view.findViewById(R.id.listView);

        if (MODE_DEBUG) {
            List<Habitat> allHabitats = HabitatRepository.getAll();
            List<Habitat> filtered = new ArrayList<>();
            for (Habitat h : allHabitats) {
                if (!h.email.equalsIgnoreCase(emailConnecte)) {
                    filtered.add(h);
                }
            }

            HabitatAdapter adapter = new HabitatAdapter(getActivity(), filtered, R.layout.item_habitat);
            listView.setAdapter(adapter);
            return;
        }

        // 🛰️ Mode normal : appel à l'API
        new Thread(() -> {
            try {
                URL url = new URL("http://10.0.2.2/api/getHabitats.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JSONArray jsonArray = new JSONArray(response.toString());
                List<Habitat> habitatList = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    String name = obj.getString("name");
                    String email = obj.getString("email");
                    int etage = obj.getInt("etage");
                    int count = obj.getInt("equipment_count");

                    Habitat h = new Habitat(name, etage, email);
                    for (int j = 0; j < count; j++) {
                        h.addAppliance(new Appliance(0, "Équipement", "GEN", 0));
                    }

                    habitatList.add(h);
                }

                requireActivity().runOnUiThread(() -> {
                    HabitatAdapter adapter = new HabitatAdapter(getActivity(), habitatList, R.layout.item_habitat);
                    listView.setAdapter(adapter);
                });

            } catch (Exception e) {
                e.printStackTrace();
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Erreur lors du chargement des habitats", Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }
}

package com.example.power_home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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
import java.util.ArrayList;
import java.util.List;

public class MesNotifFragment extends Fragment {

    public MesNotifFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mes_notif, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Mes notifications");

        ListView listView = view.findViewById(R.id.notification_list);
        String email = getArguments() != null ? getArguments().getString("email") : null;

        List<NotificationItem> list = new ArrayList<>();

        if (email != null) {
            new Thread(() -> {
                try {
                    URL url = new URL("http://10.0.2.2/api/getNotifications.php");
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
                        JSONArray notifArray = json.getJSONArray("notifications");

                        for (int i = 0; i < notifArray.length(); i++) {
                            JSONObject n = notifArray.getJSONObject(i);
                            list.add(new NotificationItem(
                                    n.getString("titre"),
                                    n.getString("message"),
                                    n.getInt("type")
                            ));
                        }
                    } else {
                        list.add(new NotificationItem("Info", "Aucune notification trouvée.", 0));
                    }

                    requireActivity().runOnUiThread(() -> {
                        NotificationAdapter adapter = new NotificationAdapter(getActivity(), list, R.layout.item_notification);
                        listView.setAdapter(adapter);
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    requireActivity().runOnUiThread(() -> {
                        list.add(new NotificationItem("Erreur", "Impossible de charger les notifications.", 2));
                        NotificationAdapter adapter = new NotificationAdapter(getActivity(), list, R.layout.item_notification);
                        listView.setAdapter(adapter);
                    });
                }
            }).start();
        }
    }
}

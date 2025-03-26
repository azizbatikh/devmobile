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
        String email = getActivity().getIntent().getStringExtra("email");

        List<NotificationItem> list = new ArrayList<>();

        if (email != null) {
            switch (email) {
                case "ismail@gmail.com":
                    list.add(new NotificationItem("Bravo Ismail !", "Vous avez gagné 10 éco-coins hier soir.", 1));
                    list.add(new NotificationItem("Alerte", "Votre aspirateur a consommé +20% hier.", 2));
                    break;

                case "aziz@gmail.com":
                    list.add(new NotificationItem("Info", "Un nouveau créneau éco est dispo demain à 21h.", 0));
                    list.add(new NotificationItem("Attention Aziz !", "Le lave-linge a été utilisé en période rouge.", 2));
                    break;

                case "halima@gmail.com":
                    list.add(new NotificationItem("Halima, bravo !", "Votre clim a été bien utilisée, conso optimisée.", 1));
                    list.add(new NotificationItem("Info", "Vous avez accès à un bonus de 5 éco-coins.", 0));
                    break;

                default:
                    list.add(new NotificationItem("Bienvenue !", "Aucune nouvelle notification pour le moment.", 0));
                    break;
            }
        }

        NotificationAdapter adapter = new NotificationAdapter(getActivity(), list, R.layout.item_notification);
        listView.setAdapter(adapter);
    }
}

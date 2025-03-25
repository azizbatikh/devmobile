package com.example.myapplication;

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

    public MesNotifFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("First title");
        View rootView=inflater.inflate(R.layout.fragment_mes_notif, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Mes notifications");

        ListView listView = view.findViewById(R.id.notification_list);

        List<NotificationItem> list = new ArrayList<>();
        list.add(new NotificationItem("Bravo !", "Vous avez respecté votre engagement d’hier soir.", 1));
        list.add(new NotificationItem("Alerte conso", "Votre habitat a dépassé les 70% à 19h.", 2));
        list.add(new NotificationItem("Info", "Un nouveau créneau éco est disponible.", 0));

        NotificationAdapter adapter = new NotificationAdapter(getActivity(), list, R.layout.item_notification);
        listView.setAdapter(adapter);
    }


}

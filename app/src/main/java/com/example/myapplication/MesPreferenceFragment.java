package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.Arrays;
import java.util.List;

public class MesPreferenceFragment extends Fragment {

    public MesPreferenceFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("First title");
        View rootView=inflater.inflate(R.layout.fragment_mes_preference, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Mes préférences");

        Switch switchEco = view.findViewById(R.id.switch_eco);
        Switch switchNotif = view.findViewById(R.id.switch_notif);
        Spinner spinner = view.findViewById(R.id.spinner_creneau);
        Button btnSave = view.findViewById(R.id.btn_save);

        // Liste de créneaux factices
        List<String> creneaux = Arrays.asList("Matin (6h-12h)", "Après-midi (12h-18h)", "Soir (18h-23h)");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, creneaux);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        btnSave.setOnClickListener(v -> {
            boolean modeEco = switchEco.isChecked();
            boolean notifActive = switchNotif.isChecked();
            String creneau = spinner.getSelectedItem().toString();

            String message = "Préférences enregistrées :\n"
                    + (modeEco ? "✅ Mode éco activé\n" : "❌ Mode éco désactivé\n")
                    + (notifActive ? "🔔 Notifications activées\n" : "🔕 Notifications désactivées\n")
                    + "🕒 Créneau : " + creneau;

            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        });
    }


}

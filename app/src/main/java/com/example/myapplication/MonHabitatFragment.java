package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class MonHabitatFragment extends Fragment {

    public MonHabitatFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("First title");
        View rootView=inflater.inflate(R.layout.fragment_mon_habitat, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Mon habitat");

        Habitat monHabitat = getHabitatPerso(); // Simule le résident connecté (ex. h1)

        TextView nom = view.findViewById(R.id.nom_resident);
        TextView etage = view.findViewById(R.id.etage_resident);
        TextView puissance = view.findViewById(R.id.puissance_totale);
        LinearLayout equipementsLayout = view.findViewById(R.id.liste_equipements);
        Button reserver = view.findViewById(R.id.btn_reserver);

        nom.setText("Nom : " + monHabitat.residentName);
        etage.setText("Étage : " + monHabitat.floor);

        int totalW = 0;
        equipementsLayout.removeAllViews();

        for (Appliance a : monHabitat.list) {
            totalW += a.wattage;
            ImageView img = new ImageView(getContext());
            img.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
            img.setImageResource(a.getApplicance());
            equipementsLayout.addView(img);
        }

        puissance.setText("Consommation totale : " + totalW + "W");

        reserver.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Créneau réservé !", Toast.LENGTH_SHORT).show();
        });
    }

    private Habitat getHabitatPerso() {
        Habitat h = new Habitat("Ismail Atchekzai", 2);
        h.addAppliance(new Appliance(1, "aspirateur", "as44", 30));
        h.addAppliance(new Appliance(2, "fer_a_repasser", "fer78", 80));
        return h;
    }


}

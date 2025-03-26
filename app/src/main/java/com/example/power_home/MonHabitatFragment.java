package com.example.power_home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class MonHabitatFragment extends Fragment {

    public MonHabitatFragment() {
        // Constructeur vide
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mon_habitat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Mon habitat");
        }

        // Récupérer l’email depuis les arguments
        String email = getArguments() != null ? getArguments().getString("email") : "";

        // Récupérer le résident via l’email
        Habitat habitat = HabitatRepository.getHabitatByEmail(email);

        // Références des vues
        TextView nameView = view.findViewById(R.id.nomResident);
        TextView etageView = view.findViewById(R.id.etage);
        LinearLayout appareilsLayout = view.findViewById(R.id.listeAppareils);

        if (habitat != null) {
            nameView.setText(habitat.residentName);
            etageView.setText("Étage : " + habitat.floor);

            appareilsLayout.removeAllViews();
            for (Appliance a : habitat.list) {
                ImageView image = new ImageView(getContext());
                image.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
                image.setImageResource(a.getApplicance());
                image.setPadding(10, 10, 10, 10);
                appareilsLayout.addView(image);
            }
        } else {
            nameView.setText("Habitat non trouvé");
        }
    }
}

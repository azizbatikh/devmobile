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

public class ListHabitatFragment extends Fragment {

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

        // ✅ Récupère l'email du résident connecté
        String emailConnecte = getArguments() != null ? getArguments().getString("email") : "";

        // ✅ Récupère tous les habitats simulés
        List<Habitat> allHabitats = HabitatRepository.getAll();

        // ✅ Supprime celui de l'utilisateur connecté
        List<Habitat> habitatsSansMoi = new ArrayList<>();
        for (Habitat h : allHabitats) {
            if (!h.email.equalsIgnoreCase(emailConnecte)) {
                habitatsSansMoi.add(h);
            }
        }

        ListView listView = view.findViewById(R.id.listView);
        HabitatAdapter adapter = new HabitatAdapter(getActivity(), habitatsSansMoi, R.layout.item_habitat);
        listView.setAdapter(adapter);
    }
}

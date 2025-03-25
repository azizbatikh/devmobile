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

    public ListHabitatFragment() {

    }

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

        List<Habitat> list = new ArrayList<>();

        Habitat h1 = new Habitat("Gaëtan Leclair", 1);
        Habitat h2 = new Habitat("Cédric Boudet", 1);
        Habitat h3 = new Habitat("Gaylord Thibodeaux", 2);
        Habitat h4 = new Habitat("Adam Jacquinot", 3);
        Habitat h5 = new Habitat("Abel Fresnel", 3);

        list.add(h1);
        list.add(h2);
        list.add(h3);
        list.add(h4);
        list.add(h5);

        Appliance aspi = new Appliance(1, "aspirateur", "as447", 30);
        Appliance fer = new Appliance(2, "fer_a_repasser", "fer2890", 80);
        Appliance clim = new Appliance(3, "climatiseur", "clim890", 90);
        Appliance machine = new Appliance(4, "machine_a_laver", "machine879", 87);

        h1.addAppliance(aspi);
        h1.addAppliance(fer);
        h1.addAppliance(clim);
        h1.addAppliance(machine);
        h2.addAppliance(machine);
        h3.addAppliance(fer);
        h3.addAppliance(aspi);
        h4.addAppliance(aspi);
        h4.addAppliance(fer);
        h4.addAppliance(machine);
        h5.addAppliance(aspi);

        ListView ls = view.findViewById(R.id.listView);
        HabitatAdapter adapter = new HabitatAdapter(getActivity(), list, R.layout.item_habitat);
        ls.setAdapter(adapter);
    }

}

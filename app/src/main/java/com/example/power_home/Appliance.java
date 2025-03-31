package com.example.power_home;

import com.example.power_home.R;

public class Appliance {

    int id;
    String name;
    String ref;
    int wattage;

    public Appliance(int id, String name, String ref, int wattage) {
        this.id = id;
        this.name = name;
        this.ref = ref;
        this.wattage = wattage;
    }

    public int getApplicance() {
        String n = name.toLowerCase();
        if (n.contains("aspirateur")) return R.drawable.ic_aspirateur;
        if (n.contains("machine")) return R.drawable.ic_machine_a_laver;
        if (n.contains("climatiseur")) return R.drawable.ic_climatiseur;
        if (n.contains("fer")) return R.drawable.ic_fer_a_repasser;
        return R.drawable.ic_launcher_background;
    }
}

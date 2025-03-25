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
        switch (this.name) {
            case "aspirateur":
                return R.drawable.ic_aspirateur;
            case "machine_a_laver":
                return R.drawable.ic_machine_a_laver;
            case "climatiseur":
                return R.drawable.ic_climatiseur;
            case "fer_a_repasser":
                return R.drawable.ic_fer_a_repasser;
            default:
                return R.drawable.ic_launcher_background;
        }
    }
}

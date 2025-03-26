package com.example.power_home;

import java.util.ArrayList;
import java.util.List;

public class Habitat {


    public String email;
    public String residentName;
    public int floor;
    public List<Appliance> list;

    public Habitat(String residentName, int floor, String email) {
        this.residentName = residentName;
        this.floor = floor;
        this.email = email;
        this.list = new ArrayList<>();
    }


    public void addAppliance(Appliance a){

        this.list.add(a);

    }

    public int getApplicance(Appliance a){

        switch(a.name) {
            case "aspirateur":
                return R.drawable.ic_aspirateur;
            case "machine_a_laver":
                return R.drawable.ic_machine_a_laver;
            case "climatiseur":
                return R.drawable.ic_climatiseur;
            case "fer_a_repasser":
                return R.drawable.ic_fer_a_repasser;
            default:
                return R.drawable.ic_climatiseur;
        }

    }



}

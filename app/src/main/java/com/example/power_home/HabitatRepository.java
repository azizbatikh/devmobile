package com.example.power_home;

import java.util.ArrayList;
import java.util.List;

public class HabitatRepository {

    private static final List<Habitat> allHabitats = new ArrayList<>();

    static {

        Habitat h1 = new Habitat("Ismail Atchekzai", 1, "ismail@gmail.com");
        Habitat h2 = new Habitat("Aziz Batikh", 2, "aziz@gmail.com");
        Habitat h3 = new Habitat("Halima Bouhlam", 3, "halima@gmail.com");

        h1.addAppliance(new Appliance(1, "aspirateur", "A100", 30));
        h1.addAppliance(new Appliance(2, "fer_a_repasser", "F200", 80));

        h2.addAppliance(new Appliance(3, "machine_a_laver", "M300", 100));
        h2.addAppliance(new Appliance(2, "fer_a_repasser", "F300", 80));

        h3.addAppliance(new Appliance(4, "climatiseur", "C400", 90));

        allHabitats.add(h1);
        allHabitats.add(h2);
        allHabitats.add(h3);
    }

    public static List<Habitat> getAll() {
        return allHabitats;
    }

    public static Habitat getHabitatByEmail(String email) {
        for (Habitat h : allHabitats) {
            if (h.email.equalsIgnoreCase(email)) return h;
        }
        return null;
    }
}

package com.example.myapplication;

public class NotificationItem {
    private String titre;
    private String description;
    private int type; // 0 = info, 1 = positif, 2 = alerte

    public NotificationItem(String titre, String description, int type) {
        this.titre = titre;
        this.description = description;
        this.type = type;
    }

    public String getTitre() { return titre; }
    public String getDescription() { return description; }
    public int getType() { return type; }
}

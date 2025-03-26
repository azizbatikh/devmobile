package com.example.power_home;

public class DayItem {
    private int day;
    private int month;
    private int year;

    public DayItem(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }
}

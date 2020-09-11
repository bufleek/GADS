package com.gadsleaderboard.models;

public class LearningLeader {
    public String name;
    public String hours;
    public String country;
    public String badgeUrl;

    public LearningLeader(String name, String hours, String country, String badgeUrl){
        this.badgeUrl = badgeUrl;
        this.country = country;
        this.hours = hours;
        this.name = name;
    }
}

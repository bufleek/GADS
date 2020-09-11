package com.gadsleaderboard.models;

public class SkillIqLeader {
    public String name;
    public String score;
    public String country;
    public String badgeUrl;

    public SkillIqLeader(String name, String score, String country, String badgeUrl){
        this.badgeUrl = badgeUrl;
        this.country = country;
        this.score = score;
        this.name = name;
    }
}

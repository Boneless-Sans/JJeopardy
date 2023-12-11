package com.boneless;

public class Player {
    private String teamName;
    private int score;

    public void addToScore(int score){
        this.score += score;
    }
    public void setTeamName(String name){
        this.teamName = name;
    }

    public String getTeamName(){
        return teamName;
    }
    public int getScore(){
        return score;
    }
    public String toString(){
        return "Team Name: " + teamName + "\nScore: " + score;
    }
}

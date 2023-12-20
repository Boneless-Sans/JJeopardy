package com.boneless;

import javax.swing.*;
import java.awt.*;

public class Team{
    private final String teamName;
    private int points;
    public Team(String teamName){
        this.teamName = teamName;
    }
    public String getTeamName(){
        return teamName;
    }
    public int getPoints(){
        return points;
    }
}

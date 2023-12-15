package com.boneless;

import javax.swing.*;
import java.awt.*;

public class Team{
    private String teamName;
    private int points;
    public Team(String teamName){
        this.teamName = teamName;
    }
    public String getTeamName(){
        return teamName;
    }
}

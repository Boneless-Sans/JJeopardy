package com.boneless;

import javax.swing.*;
import java.awt.*;

public class Team extends JPanel {
    private static int teamCount;
    private int score = 0;
    private String name;
    public Team(){
        teamCount++;
    }
    public int getScore(){
        return score;
    }
    public String getTeamName(){
        return name;
    }
}

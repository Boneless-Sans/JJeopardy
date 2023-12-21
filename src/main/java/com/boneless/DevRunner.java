package com.boneless;

import javax.swing.*;
import java.awt.*;

public class DevRunner extends JFrame{
    public static void main(String[] args) {
        Game game = new Game();
        game.initUI(false, 3);
    }
}

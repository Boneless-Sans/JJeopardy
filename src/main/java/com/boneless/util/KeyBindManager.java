package com.boneless.util;

import java.awt.event.KeyEvent;

public class KeyBindManager {
    private KeyBindManager(){}

    public String parseKeyStrokeInput(String keyStrokeCode){
        return switch (keyStrokeCode){
            case "Esc" -> "\u001B";
            case "Space" -> " ";
            case "Enter" -> "\n";
            case "Backspace" -> "\b";
            default -> keyStrokeCode.toLowerCase();
        };
    }
    //take key input and return what the bind is too via string
    public static String getKeyBindFor(String keyInput){

        System.out.println(keyInput);
        return null;
    }
}

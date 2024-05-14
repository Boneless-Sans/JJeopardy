package com.boneless.util;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.security.Key;
import java.util.Arrays;
import java.util.Objects;

public class KeyBindManager implements KeyListener {
    public static String parseKeyStrokeInput(KeyEvent key){
        return switch (key.getKeyCode()){
            case KeyEvent.VK_ESCAPE -> "esc";
            case KeyEvent.VK_SPACE -> "space";
            case KeyEvent.VK_ENTER -> "enter";
            case KeyEvent.VK_BACK_SPACE -> "backspace";
            default -> String.valueOf(key.getKeyChar());
        };
    }
    //take key input and return what the bind is too via string
    public static String getKeyBindFor(KeyEvent keyInput){
        String fileName = "settings.json";

        String key = parseKeyStrokeInput(keyInput).toLowerCase();
        System.out.println("Parsed Key: " + key); // Debug output
        //System.out.println(key);
        String keyBindESC = JsonFile.read(fileName, "key_binds", "exit");
        String keyBindFullScreen = JsonFile.read(fileName, "key_binds", "full_screen");
        String keyBindContinue = JsonFile.read(fileName, "key_binds", "continue");

        //System.out.println("Key Pressed: " + key);
        if (key.equals(keyBindESC)) {
            return "esc";
        } else if (key.equals(keyBindFullScreen)) {
            return "full";
        } else if (key.equals(keyBindContinue)) {
            return "continue";
        }

        return "";
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}

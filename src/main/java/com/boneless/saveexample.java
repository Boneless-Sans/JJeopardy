package com.boneless;

import com.sun.tools.javac.Main;
import java.util.Scanner;

import com.boneless.util.JsonFile;

import java.io.IOException;
import java.util.Scanner;

public class saveexample {
    private String teamName;
    private int score;
    private int playerCount;

    public static void main(String[] args){
        Scanner scan = new Scanner(System.in);

        System.out.println("What color\nBackground (1), Buttons (2), Foreground (3)");

        switch (scan.nextInt()){
            case 1:
                String backgroundColor = getScan();
                saveColor("background", backgroundColor);
                break;
            case 2:
                String buttonColor = getScan();
                saveColor("button", buttonColor);
                break;
            case 3:
                String foregroundColor = getScan();
                saveColor("foreground", foregroundColor);
                break;
            default:
                System.out.println("Invalid choice dumbass");
                break;
        }
        scan.close();
        System.out.println(
                JsonFile.read("settings.json", "colors", "background") + "\n" +
                        JsonFile.read("settings.json", "colors", "button") + "\n" +
                        JsonFile.read("settings.json", "colors", "foreground") + "\n"
        );
    }
    private static String getScan(){
        Scanner scan = new Scanner(System.in);
        System.out.println("Color");

        return scan.next();
    }

    private static void saveColor(String key, String color){
        JsonFile.writeln("settings.json", "colors", key, color);
    }
}

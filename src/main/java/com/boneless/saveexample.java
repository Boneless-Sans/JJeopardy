package com.boneless;

import com.boneless.util.JsonFile;

public class saveexample{
    public static void main(String[] args){
        int sizeX = 6;
        String filename = "main_board.json";
        String[] titles = new String[sizeX]; // Declare the array of sizeX elements

        for (int i = 1; i <= sizeX; i++) {
            titles[i-1] = JsonFile.read(filename, "column_" + i, "title"); // Read the title and store it in the array
            System.out.println(titles[i -1]);
        }

    }
}
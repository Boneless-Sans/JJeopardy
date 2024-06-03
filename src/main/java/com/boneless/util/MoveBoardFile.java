package com.boneless.util;

import java.io.*;

public class MoveBoardFile {
    public static void main(String[] args) {
        File originalFile = new File("src/main/resources/data/template.json");

        File file = new File(System.getProperty("user.home") + "/template.json");

        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            FileReader fileReader = new FileReader(originalFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            String line;
            while ((line = bufferedReader.readLine()) != null){
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }

            bufferedReader.close();
            bufferedWriter.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

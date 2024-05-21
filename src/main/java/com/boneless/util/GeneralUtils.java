package com.boneless.util;

import javax.swing.*;
import java.awt.*;

import static com.boneless.Main.fileName;

public class GeneralUtils {

    public static Color parseColor(String color){
        String[] split = color.split(",");
        int red = Integer.parseInt(split[0]);
        int green = Integer.parseInt(split[1]);
        int blue = Integer.parseInt(split[2]);
        return new Color(red,green,blue);
    }
    public static Font generateFont(int fontSize){
        try {
            if(fileName != null && !fileName.isEmpty()) {
                return new Font(
                        JsonFile.read(fileName, "data", "font"),
                        Font.PLAIN,
                        fontSize
                );
            }
        } catch (NullPointerException e){
            return new Font(
                    "Arial",
                    Font.PLAIN,
                    fontSize
            );
        }
        return new Font(
                "Arial",
                Font.PLAIN,
                fontSize
        );
    }
    public static void changeCurrentPanel(JPanel panelToSet, JComponent self) {
        Container parent = self.getParent();

        parent.remove(self);
        parent.add(panelToSet);

        parent.revalidate();
        parent.repaint();
    }
}

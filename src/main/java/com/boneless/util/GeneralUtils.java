package com.boneless.util;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

import static com.boneless.GameBoard.mainColor;
import static com.boneless.Main.fileName;

public class GeneralUtils {

    public static Color parseColor(String color){
        String[] split = color.split(",");
        int red = Integer.parseInt(split[0]);
        int green = Integer.parseInt(split[1]);
        int blue = Integer.parseInt(split[2]);
        return new Color(red,green,blue);
    }
    public static Color parseColorFade(String color, int alpha){
        String[] split = color.split(",");
        int red = Integer.parseInt(split[0]);
        int green = Integer.parseInt(split[1]);
        int blue = Integer.parseInt(split[2]);
        return new Color(red,green,blue,alpha);
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
            return new Font("Arial", Font.PLAIN, fontSize);
        }
        return new Font("Arial", Font.PLAIN, fontSize);
    }
    public static void changeCurrentPanel(JPanel panelToSet, JComponent self) {
        Container parent = self.getParent();

        parent.remove(self);
        parent.add(panelToSet);

        parent.revalidate();
        parent.repaint();
    }
    public static BufferedImage makeRoundedCorner(BufferedImage image, int cornerRadius) {
        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = output.createGraphics();

        g2.setComposite(AlphaComposite.Src);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fill(new RoundRectangle2D.Float(0, 0, w, h, cornerRadius, cornerRadius));

        g2.setComposite(AlphaComposite.SrcAtop);
        g2.drawImage(image, 0, 0, null);

        g2.dispose();

        return output;
    }
    public static JButton createCheckButton(){
        JButton button = new JButton(){
            @Override
            protected void paintComponent(Graphics g){
                //super.paintComponent(g); disable super for complete render control

                Graphics2D g2d = (Graphics2D) g;

                //enable antialiasing, not really needed but cool to have
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int width = getWidth();
                int height = getHeight();

                int centerX = width / 2;
                int centerY = height / 2;

                int lineThickness = 4; //im not explaining this

                int lineLength = Math.min(width, height) / 4; //sets line length, higher is smaller

                g2d.setStroke(new BasicStroke(lineThickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                //draw circle
                int ovalDiameter = (Math.min(width, height) / 2) + 10;
                int ovalX = centerX - ovalDiameter /2;
                int ovalY = centerY - ovalDiameter /2;
                g2d.setColor(mainColor);
                g2d.fillOval(ovalX, ovalY, ovalDiameter, ovalDiameter);

                g2d.setColor(Color.white);
                //draw vertical line if subtract is false
                if(!subtract) g2d.drawLine(centerX, centerY - lineLength, centerX, centerY + lineLength);
                //horizontal line
                g2d.drawLine(centerX - lineLength, centerY, centerX + lineLength, centerY);
            }
        };
    }
}

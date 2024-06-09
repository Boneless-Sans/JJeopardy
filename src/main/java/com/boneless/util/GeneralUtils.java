package com.boneless.util;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static com.boneless.Main.fileName;

public class GeneralUtils {
    public static final GridBagConstraints gbc = new GridBagConstraints(){{
        gridx = 0;
        gridy = 0;
        fill = 0;
    }};

    public static int clamp(int value) {
        return Math.max(0, Math.min(255, value));
    }

    public static BufferedImage renderIcon(int iconSize){
        boolean OSIsWindows = System.getProperty("os.name").toLowerCase().contains("windows");
        int size = OSIsWindows? iconSize : 103;
        int posX = OSIsWindows? 0 : 12;
        int posY = OSIsWindows? 0 : 12;
        int arc = 40;
        int fontSize = 80;
        Color color;
        Font font;

        System.out.println(fileName);
        if(fileName == null) {
            color = new Color(70,70,255);
            font = new Font("Arial", Font.PLAIN, fontSize);
        } else {
            color = parseColor(JsonFile.read(fileName, "data", "global_color"));
            font = generateFont(fontSize);
        }

        BufferedImage image = new BufferedImage(128,128, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        g2d.setPaint(new GradientPaint(0,0,color,128,128,ScrollGridPanel.adjustColor(color)));
        g2d.fillRoundRect(posX,posY,size,size,arc,arc);

        Color forgroundColor = ScrollGridPanel.adjustColor(color);
        int smallerSize = (int)(size * 0.8);
        int smallerPosX = posX + (size - smallerSize) / 2;
        int smallerPosY = posY + (size - smallerSize) / 2;
        int smallerArc = 20;

        g2d.setPaint(new GradientPaint(0,0,forgroundColor,128,128,ScrollGridPanel.adjustColor(forgroundColor)));
        g2d.fillRoundRect(smallerPosX,smallerPosY,smallerSize,smallerSize,smallerArc,smallerArc);

        g2d.setFont(font);

        FontMetrics fmB = g2d.getFontMetrics();
        int textWidthBackground = fmB.stringWidth("J");
        int textHeightBackground = fmB.getHeight();

        int xB = posX + (size - textWidthBackground) / 2;
        int yB = posY + (size - textHeightBackground) / 2 + fmB.getAscent();

        g2d.setColor(new Color(0,0,0,70));
        g2d.drawString("J", xB + 2, yB + 7);

        g2d.setFont(new Font(font.getFontName(), font.getStyle(), font.getSize() + 10));

        FontMetrics fmF = g2d.getFontMetrics();
        int textWidthForeground = fmF.stringWidth("J");
        int textHeightForeground = fmF.getHeight();

        int xF = posX + (size - textWidthForeground) / 2;
        int yF = posY + (size - textHeightForeground) / 2 + fmF.getAscent();

        g2d.setFont(font);
        g2d.setColor(Color.white);
        g2d.drawString("J", xF, yF);

        g2d.dispose();

        return image;
    }

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

    public static void changeCurrentPanel(JPanel panelToAdd, JPanel self, boolean moveDown, int... extraMoveDistance) {

        int selfStartY = self.getY();
        int selfTargetY;
        int panelToAddStartY;

        int extraMove = 0;
        if(extraMoveDistance != null && extraMoveDistance.length > 0) {
            extraMove = extraMoveDistance[0];
        }

        if (!moveDown) {
            selfTargetY = -(self.getHeight() + extraMove);
            panelToAddStartY = (self.getHeight() + extraMove);
        } else {
            selfTargetY = (self.getHeight() + extraMove);
            panelToAddStartY = -(self.getHeight() + extraMove);
        }

        int duration = 700;
        int interval = 10;

        Timer timer = new Timer(interval, null);

        long startTime = System.currentTimeMillis();

        timer.addActionListener(e -> {
            long elapsed = System.currentTimeMillis() - startTime;
            double progress = Math.min(1.0, (double) elapsed / duration);

            double easeProgress = -Math.pow(progress - 1, 2) + 1;

            int selfNewY = (int) (selfStartY + easeProgress * (selfTargetY - selfStartY));
            int panelToAddNewY = (int) (panelToAddStartY + easeProgress * (selfStartY - panelToAddStartY));

            self.setBounds(self.getX(), selfNewY, self.getWidth(), self.getHeight());
            panelToAdd.setBounds(panelToAdd.getX(), panelToAddNewY, panelToAdd.getWidth(), panelToAdd.getHeight());

            if (progress >= 1.0) {
                timer.stop();
                self.getParent().remove(self);
            }

            // Refresh the UI
            try {
                self.repaint();
                panelToAdd.repaint();
                self.getParent().revalidate();
                self.getParent().repaint();
            } catch (NullPointerException ignore){}
        });

        if (self.getParent().getLayout() != null) {
            self.getParent().setLayout(null);
        }

        if (panelToAdd.getBounds().isEmpty()) {
            panelToAdd.setBounds(self.getX(), panelToAddStartY, self.getWidth(), self.getHeight());
        }

        if (panelToAdd.getParent() == null) {
            self.getParent().add(panelToAdd);
        }

        //ensure panels are visible!
        self.setVisible(true);
        panelToAdd.setVisible(true);

        timer.start();
    }

    public static JPanel createGap(int size, Color color) {
        JPanel panel = new JPanel();

        if(color == null){
            panel.setOpaque(false);
        }

        panel.setBackground(color);
        panel.setPreferredSize(new Dimension(size, size));
        return panel;
    }

    public static class HiddenScroller extends JScrollPane {
        public HiddenScroller(Component view, boolean doHorizontal) {
            super(view);
            if(doHorizontal) {
                setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            } else {
                setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            }
            JScrollBar horizontalScrollBar = getHorizontalScrollBar();
            horizontalScrollBar.setUI(new HiddenScrollUI());
            horizontalScrollBar.setPreferredSize(new Dimension(0, 0)); //hide
        }

        //hide scrollbar ui
        private static class HiddenScrollUI extends BasicScrollBarUI { //todo: fix sluggish scrolling, only happens on track pad
            @Override protected void configureScrollBarColors() {}
            @Override protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {}
            @Override protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {}
        }
    }

    public static String checkFileExists() throws IOException { //stupid, i hate it but its required todo: fix jar packing
        String tmpDir = System.getProperty("java.io.tmpdir");
        File file = new File(tmpDir + "temp.json");

        if (new File("src/main/resources/data/template.json").exists()) {
            if (file.createNewFile()) {
                System.out.println("File Created In Temp");
            } else {
                System.out.println("Error: Failed To Create File! (Does it already exist??) File Name: " + file.getAbsolutePath());
            }

            // JSON content to be written to the file
            String jsonContent = "{\n" +
                    "  \"data\": {\n" +
                    "    \"board_name\": \"Template\",\n" +
                    "    \"categories\": 5,\n" +
                    "    \"rows\": 5,\n" +
                    "    \"font\": \"Arial\",\n" +
                    "    \"font_color\": \"255,255,255\",\n" +
                    "    \"global_color\": \"20,20,255\",\n" +
                    "    \"disabled_button_color\": \"80,80,80\"\n" +
                    "  },\n" +
                    "  \"board\": {\n" +
                    "    \"categories\": {\n" +
                    "      \"cat_0\": \"Test 1\",\n" +
                    "      \"cat_1\": \"Test 2\",\n" +
                    "      \"cat_2\": \"Test 3\",\n" +
                    "      \"cat_3\": \"Test 4\",\n" +
                    "      \"cat_4\": \"Test 5\"\n" +
                    "    },\n" +
                    "    \"scores\": {\n" +
                    "      \"row_0\": \"100\",\n" +
                    "      \"row_1\": \"200\",\n" +
                    "      \"row_2\": \"300\",\n" +
                    "      \"row_3\": \"400\",\n" +
                    "      \"row_4\": \"500\"\n" +
                    "    },\n" +
                    "    \"col_0\": {\n" +
                    "        \"question_0\": \"Column 0 Question 0\",\n" +
                    "        \"question_1\": \"Column 0 Question 1\",\n" +
                    "        \"question_2\": \"Column 0 Question 2\",\n" +
                    "        \"question_3\": \"Column 0 Question 3\",\n" +
                    "        \"question_4\": \"Column 0 Question 4\",\n" +
                    "        \"answer_0\": \"Column 0 Answer 0\",\n" +
                    "        \"answer_1\": \"Column 0 Answer 1\",\n" +
                    "        \"answer_2\": \"Column 0 Answer 2\",\n" +
                    "        \"answer_3\": \"Column 0 Answer 3\",\n" +
                    "        \"answer_4\": \"Column 0 Answer 4\"\n" +
                    "    },\n" +
                    "    \"col_1\": {\n" +
                    "        \"question_0\": \"Column 1 Question 0\",\n" +
                    "        \"question_1\": \"Column 1 Question 1\",\n" +
                    "        \"question_2\": \"Column 1 Question 2\",\n" +
                    "        \"question_3\": \"Column 1 Question 3\",\n" +
                    "        \"question_4\": \"Column 1 Question 4\",\n" +
                    "        \"answer_0\": \"Column 1 Answer 0\",\n" +
                    "        \"answer_1\": \"Column 1 Answer 1\",\n" +
                    "        \"answer_2\": \"Column 1 Answer 2\",\n" +
                    "        \"answer_3\": \"Column 1 Answer 3\",\n" +
                    "        \"answer_4\": \"Column 1 Answer 4\"\n" +
                    "    },\n" +
                    "    \"col_2\": {\n" +
                    "        \"question_0\": \"Column 2 Question 0\",\n" +
                    "        \"question_1\": \"Column 2 Question 1\",\n" +
                    "        \"question_2\": \"Column 2 Question 2\",\n" +
                    "        \"question_3\": \"Column 2 Question 3\",\n" +
                    "        \"question_4\": \"Column 2 Question 4\",\n" +
                    "        \"answer_0\": \"Column 2 Answer 0\",\n" +
                    "        \"answer_1\": \"Column 2 Answer 1\",\n" +
                    "        \"answer_2\": \"Column 2 Answer 2\",\n" +
                    "        \"answer_3\": \"Column 2 Answer 3\",\n" +
                    "        \"answer_4\": \"Column 2 Answer 4\"\n" +
                    "    },\n" +
                    "    \"col_3\": {\n" +
                    "        \"question_0\": \"Column 3 Question 0\",\n" +
                    "        \"question_1\": \"Column 3 Question 1\",\n" +
                    "        \"question_2\": \"Column 3 Question 2\",\n" +
                    "        \"question_3\": \"Column 3 Question 3\",\n" +
                    "        \"question_4\": \"Column 3 Question 4\",\n" +
                    "        \"answer_0\": \"Column 3 Answer 0\",\n" +
                    "        \"answer_1\": \"Column 3 Answer 1\",\n" +
                    "        \"answer_2\": \"Column 3 Answer 2\",\n" +
                    "        \"answer_3\": \"Column 3 Answer 3\",\n" +
                    "        \"answer_4\": \"Column 3 Answer 4\"\n" +
                    "    },\n" +
                    "    \"col_4\": {\n" +
                    "        \"question_0\": \"Column 4 Question 0\",\n" +
                    "        \"question_1\": \"Column 4 Question 1\",\n" +
                    "        \"question_2\": \"Column 4 Question 2\",\n" +
                    "        \"question_3\": \"Column 4 Question 3\",\n" +
                    "        \"question_4\": \"Column 4 Question 4\",\n" +
                    "        \"answer_0\": \"Column 4 Answer 0\",\n" +
                    "        \"answer_1\": \"Column 4 Answer 1\",\n" +
                    "        \"answer_2\": \"Column 4 Answer 2\",\n" +
                    "        \"answer_3\": \"Column 4 Answer 3\",\n" +
                    "        \"answer_4\": \"Column 4 Answer 4\"\n" +
                    "    }\n" +
                    "  }\n" +
                    "}";

            try (FileWriter writer = new FileWriter(file)) {
                writer.write(jsonContent);
                System.out.println("JSON content written to file.");
            }
        }
        return file.getAbsolutePath();
    }
}
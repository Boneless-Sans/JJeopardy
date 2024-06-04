package com.boneless;

import com.boneless.util.JsonFile;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Random;

import static com.boneless.Main.*;
import static com.boneless.util.GeneralUtils.*;

public class BoardFactory extends JPanel {
    public boolean factoryIsActive;
    private final JFrame parent;
    private Color mainColor;
    private Color fontColor;
    private boolean changesMade;
    private final int fontSize = 20;
    private final String tempDir = System.getProperty("java.io.tmpdir");

    public BoardFactory(JFrame parent){
        factoryIsActive = true;
        this.parent = parent;

        if(fileName != null){
            mainColor = parseColor(JsonFile.read(fileName, "data", "global_color"));
            fontColor = parseColor(JsonFile.read(fileName, "data", "font_color"));
        } else {
            System.out.println("File is null, resetting with new template...");
            fileName = createNewFile().getAbsolutePath();
            System.out.println("File Name: " + fileName);
        }
        setLayout(new BorderLayout());
        parent.setJMenuBar(menuBar());

        reload();
    }

    private void reload(){
        removeAll();

        add(boardPanel(), BorderLayout.CENTER);
        add(controlPanel(), BorderLayout.EAST);

        parent.revalidate();
        parent.repaint();
    }
    private JMenuBar menuBar(){
        //use macOS's system menu bar instead of a frame. Windows will default
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Jeopardy Creator"); //don "t think this works

        JMenuBar menuBar = new JMenuBar();

        //tabs
        JMenu fileMenu = new JMenu("File");
        JMenu helpMenu = new JMenu("Help");

        //sub tabs - file
        JMenuItem newItem = new JMenuItem("New Board");
        newItem.addActionListener(e -> {
            //text if changes have been made, use checkPop > field pop for name > create file via template (use devBoard?) > save file() > load file()
            JPanel fileNamePanel = new JPanel();

            JTextField textField = new JTextField(10);

            fileNamePanel.add(textField);

            int userInput = JOptionPane.showConfirmDialog(null, fileNamePanel, "", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if(userInput == JOptionPane.OK_OPTION){
                System.out.println("Insert code for creating a new file in temp dir\n" + tempDir);
            }
        });

        JMenuItem openItem = new JMenuItem("Open Board");
        openItem.addActionListener(e -> {
            //open dialog > load file()
        });

        JMenuItem saveItem = new JMenuItem("Save Board");
        saveItem.addActionListener(e -> {
            //save file();
        });

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> {
            //use bool for change confirmation > exit()
            exit();
        });

        //sub tabs - help
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> showAboutPanel());

        JMenuItem helpItem = new JMenuItem("Get Help");
        helpItem.addActionListener(e -> {
            try {
                Desktop.getDesktop().browse(new URI(getRandomWebsite()));
            } catch (IOException | URISyntaxException ex) {
                throw new RuntimeException(ex);
            }
        });

        //add File tabs
        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(exitItem);

        //add Help tabs
        helpMenu.add(aboutItem);
        helpMenu.add(helpItem);

        //add to main bar
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);

        return menuBar;
    }

    private JPanel boardPanel(){
        JPanel panel = new JPanel();
        panel.setBackground(Color.black);

        int boardX = Integer.parseInt(JsonFile.read(fileName, "data", "categories"));
        int boardY = Integer.parseInt(JsonFile.read(fileName, "data", "rows")) + 1;
        panel.setLayout(new GridLayout(boardY, boardX, 0, 0));

        for (int i = 0; i < boardX; i++) {
            panel.add(createCatPanel(i));
        }

        //setup board
        for (int i = 0; i < boardY - 1; i++) {
            for (int j = 0; j < boardX; j++) {
                String scoreString = JsonFile.readWithThreeKeys(fileName, "board", "scores", "row_" + i);
                int score = Integer.parseInt(scoreString);
                String question = JsonFile.readWithThreeKeys(fileName, "board", "col_" + j, "question_" + i);
                String answer = JsonFile.readWithThreeKeys(fileName, "board", "col_" + j, "answer_" + i);

                MockBoardButton button = new MockBoardButton(score, question, answer);
                button.setBackground(mainColor);
                button.setForeground(fontColor);
                button.setFont(generateFont(fontSize));
                button.setOpaque(true);
                panel.add(button);
            }
        }
        return panel;
    }

    private JPanel createCatPanel(int index) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createBevelBorder(0));
        panel.setBackground(mainColor);

        JLabel label = new JLabel(JsonFile.readWithThreeKeys(fileName, "board", "categories", "cat_" + index));
        label.setFont(generateFont(fontSize));
        label.setForeground(fontColor);

        panel.add(label, gbc);
        return panel;
    }

    private JScrollPane controlPanel(){
        /*
        Sub-roadmap
            -Board name | X
            -global color | X
            -font | X
            -font color | X
            -rows | X
            -cols | X
            -scores? | X
                -for rows, create a section with text fields for scores

            -have MockButton go to a emulated JCard with text fields instead. no animations, just show question / answer
         */
        JPanel panel = new JPanel();
        panel.setBackground(Color.red);
        panel.setPreferredSize(new Dimension(120,getHeight()));


        HiddenScroller scroller = new HiddenScroller(panel, false);
        scroller.setPreferredSize(new Dimension(120, getHeight()));
        return scroller;
    }

    private void showAboutPanel(){
        JFrame frame = new JFrame("About");
        frame.setSize(280, 520); //replicate macOS "s about panel
        frame.setLocationRelativeTo(null);

        //todo: fill out this section
        frame.setVisible(true);
    }

    private File createNewFile(){
        File file = new File(tempDir + "/test.json");

        try {
            if(!file.exists() && file.createNewFile()){
                System.out.println("File created at: " + file.getAbsoluteFile());
            }

            try (FileWriter writer = new FileWriter(file)){
                FileReader reader = new FileReader("src/main/resources/data/template.json");

                int character;
                while((character = reader.read()) != -1){
                    writer.write(character);
                }

                writer.close();
                reader.close();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error creating or writing to file: " + file.getAbsoluteFile(), e);
        }

        return file;
    }

    private void save(){
        //
    }

    private void load(){
        //
    }

    public void exit(){
        changeCurrentPanel(mainMenu, this);
    }
    
    private String getRandomWebsite(){
        String[] siteIndex = { //useless web mega list
                 "https://sliding.toys/mystic-square/8-puzzle/daily/","https://longdogechallenge.com/","https://maze.toys/mazes/mini/daily/",
                "https://optical.toys ","https://paint.toys/","https://puginarug.com ","https://alwaysjudgeabookbyitscover.com ",
                "https://clicking.toys/flip-grid/neat-nine/3-holes/","https://weirdorconfusing.com/","https://checkbox.toys/scale/",
                "https://memory.toys/classic/easy/","https://binarypiano.com/","https://mondrianandme.com/","https://onesquareminesweeper.com/",
                "https://cursoreffects.com ","https://floatingqrcode.com/","https://thatsthefinger.com/","https://cant-not-tweet-this.com/",
                "https://heeeeeeeey.com/","https://corndog.io/","https://eelslap.com/","https://www.staggeringbeauty.com/","https://burymewithmymoney.com/",
                "https://smashthewalls.com/","https://jacksonpollock.org/","https://endless.horse/","https://drawing.garden/","https://www.trypap.com/",
                "https://www.republiquedesmangues.fr/","https://www.movenowthinklater.com/","https://sliding.toys/klotski/easy-street/",
                "https://paint.toys/calligram/","https://checkboxrace.com/","https://www.rrrgggbbb.com/","https://www.koalastothemax.com/",
                "https://rotatingsandwiches.com/","https://www.everydayim.com/","https://randomcolour.com/","https://maninthedark.com/",
                "https://cat-bounce.com/","https://chrismckenzie.com/","https://thezen.zone/","https://ninjaflex.com/","https://ihasabucket.com/",
                "https://corndogoncorndog.com/","https://www.hackertyper.com/","https://pointerpointer.com ","https://imaninja.com/",
                "https://www.partridgegetslucky.com/","https://www.ismycomputeron.com/","https://www.nullingthevoid.com/",
                "https://www.muchbetterthanthis.com/","https://www.yesnoif.com/","https://lacquerlacquer.com ","https://potatoortomato.com/",
                "https://iamawesome.com/","https://strobe.cool/","https://thisisnotajumpscare.com/","https://doughnutkitten.com/","https://crouton.net/",
                "https://corgiorgy.com/","https://www.wutdafuk.com/","https://unicodesnowmanforyou.com/","https://chillestmonkey.com/",
                "https://scroll-o-meter.club/","https://www.crossdivisions.com/","https://tencents.info/","https://boringboringboring.com/",
                "https://www.patience-is-a-virtue.org/","https://pixelsfighting.com/","https://isitwhite.com/","https://existentialcrisis.com/",
                "https://onemillionlols.com/","https://www.omfgdogs.com/","https://oct82.com/","https://chihuahuaspin.com/","https://www.blankwindows.com/",
                "https://tunnelsnakes.com/","https://www.trashloop.com/","https://spaceis.cool/","https://www.doublepressure.com/",
                "https://www.donothingfor2minutes.com/","https://buildshruggie.com/","https://yeahlemons.com/","https://wowenwilsonquiz.com ",
                "https://notdayoftheweek.com/","https://www.amialright.com/","https://optical.toys/thatcher-effect/","https://greatbignothing.com/",
                "https://zoomquilt.org/","https://dadlaughbutton.com/","https://remoji.com/","https://papertoilet.com/","https://loopedforinfinity.com/",
                "https://end.city/","https://www.bouncingdvdlogo.com/", "https://clicking.toys/peg-solitaire/english/","https://toms.toys "
        };
        return siteIndex[new Random().nextInt(siteIndex.length)];
    }
    private class MockBoardButton extends JButton {
        public MockBoardButton(int score, String question, String answer){
            setText(String.valueOf(score));
            addActionListener(e -> changeCurrentPanel(new MockJCard(question, answer), boardPanel()));
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Background
            g2d.setColor(getBackground());
            Shape backgroundShape = new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 0,0);
            g2d.fill(backgroundShape);

            // Text
            if(isEnabled()) {
                g2d.setColor(fontColor);
            } else {
                g2d.setColor(parseColor(JsonFile.read(fileName, "data", "disabled_button_color")));
            }
            Font font = getFont();
            FontMetrics metrics = g2d.getFontMetrics(font);
            int x = (getWidth() - metrics.stringWidth(getText())) / 2;
            int y = ((getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
            g2d.setFont(font);
            g2d.drawString(getText(), x, y);

            g2d.dispose();
        }

        @Override
        protected void paintBorder(Graphics g) {
            super.paintBorder(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Border
            g2d.setColor(Color.black);
            g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 0,0);

            g2d.dispose();
        }
    }
    private static class MockJCard extends JPanel {
        public MockJCard(String question, String answer){
            //
        }
    }
}
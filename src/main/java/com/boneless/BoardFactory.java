package com.boneless;

import com.boneless.util.JsonFile;

import javax.swing.*;
import java.awt.*;

import static com.boneless.Main.fileName;
import static com.boneless.util.GeneralUtils.*;

public class BoardFactory extends JPanel {
    private final Color mainColor = parseColor(JsonFile.read(fileName, "data", "global_color"));
    private final Color fontColor = parseColor(JsonFile.read(fileName, "data","font_color"));
    private final int fontSize = 20;

    public BoardFactory(JFrame parent){
        setLayout(new BorderLayout());

        parent.setJMenuBar(menuBar());
        add(boardPanel(), BorderLayout.CENTER);
        add(controlPanel(), BorderLayout.EAST);

        parent.revalidate();
        parent.repaint();
    }

    private JMenuBar menuBar(){
        JMenuBar menuBar = new JMenuBar();

        //tabs
        JMenu fileMenu = new JMenu("File");
        JMenu helpMenu = new JMenu("Help");

//        -File
//                -New
//                -Open
//                -Save
//                -Exit
//                -Help?
        //sub tabs - file
        JMenuItem newItem = new JMenuItem("New Board");
        JMenuItem openItem = new JMenuItem("Open Board");
        JMenuItem saveItem = new JMenuItem("Save Board");
        JMenuItem exitItem = new JMenuItem("Exit");

        //sub tabs - help
        JMenuItem aboutItem = new JMenuItem("About");
        JMenuItem helpItem = new JMenuItem("Get Help");

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

                MockBoardButton button = new MockBoardButton();
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

    private JPanel controlPanel(){
        /*
        Sub-roadmap
            -Board name | X
            -global color | X
            -font | X
            -font color | X
            -rows | X
            -cols | X
            -scores? | X (not sure how to implement yet)

            -have MockButton go to a emulated JCard with text fields instead. no animations, just show question / answer
         */
        JPanel panel = new JPanel();
        panel.setBackground(Color.red);
        return panel;
    }

    private class MockBoardButton extends JButton {
        //
    }
}

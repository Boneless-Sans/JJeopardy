package com.boneless;

import com.boneless.util.GeneralUtils;
import com.boneless.util.JsonFile;
import com.boneless.util.ScrollGridPanel;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.util.ArrayList;

import static com.boneless.Main.GAME_BOARD;
import static com.boneless.Main.fileName;
import static com.boneless.util.GeneralUtils.*;

public class MainMenu extends ScrollGridPanel {
    public boolean menuIsActive;
    private final ArrayList<JButton> buttonsList = new ArrayList<>();
    private final JLabel currentFile;
    private final String[] dropDownList = {
            "1 Team", "2 Teams", "3 Teams", "4 Teams", "5 Teams", "Other",
    };

    public MainMenu(){
        menuIsActive = true;
        setLayout(new BorderLayout());

        //title text, pretty self-explanatory
        JPanel titlePanel = new JPanel(new GridBagLayout());
        titlePanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = 0;

        JLabel title = new JLabel("Jeopardy!");
        title.setFont(generateFont(150));
        title.setForeground(Color.white);

        //title.add()
        titlePanel.add(title, gbc);

        //button setup
        JPanel menuParentPanel = new JPanel(new GridBagLayout());
        menuParentPanel.setOpaque(false);

        JPanel buttonsPanel = new JPanel(new FlowLayout());
        buttonsPanel.setOpaque(false);
        buttonsPanel.setPreferredSize(new Dimension(150,250));

        buttonsPanel.add(createMenuButton("Start Game", 0));
        buttonsPanel.add(createMenuButton("Choose Board File", 1));
        buttonsPanel.add(createMenuButton("Board Creator", 2));
        buttonsPanel.add(createMenuButton("Settings", 3));
        buttonsPanel.add(createMenuButton("Exit", 4));

        add(titlePanel, BorderLayout.NORTH);

        menuParentPanel.add(buttonsPanel, gbc);

        add(menuParentPanel, BorderLayout.CENTER);

        currentFile = new JLabel(fileName == null ? "No Board Selected" : "Current Board: " + fileName);
        currentFile.setFont(generateFont(15));
        currentFile.setForeground(Color.white);

        JPanel filePanel = new JPanel(new FlowLayout());
        filePanel.setOpaque(false);
        filePanel.add(currentFile);

        add(filePanel, BorderLayout.SOUTH);
    }
    private JButton createMenuButton(String text, int UUID){
        JButton button = new JButton(text){
            @Override
            public Dimension getPreferredSize() {
                Dimension size = super.getPreferredSize();
                size.width = 170;
                return size;
            }
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (!isEnabled()) {
                    g.setColor(Color.lightGray);
                    g.setFont(getFont());
                    FontMetrics fm = g.getFontMetrics();
                    int textWidth = fm.stringWidth(getText());
                    int textHeight = fm.getAscent();
                    int x = (getWidth() - textWidth) / 2;
                    int y = (getHeight() + textHeight) / 2 - 4;
                    g.drawString(getText(), x, y);
                }
            }
        };
        button.setFocusable(false);
        button.setFont(generateFont(15));
        try { //im not sure why != null isn't working, but whatever this works
            if (UUID == 0 && fileName.isEmpty()) { //disable start button if there is no current board file
                button.setEnabled(false);
            }
        } catch (NullPointerException ignore){
            System.out.println("File is null, using defaults...");
            button.setEnabled(false);
        }
        buttonsList.add(button);
        button.addActionListener(e -> {
            menuIsActive = true;
            switch (UUID){
                case 0: { //start
                    GAME_BOARD = new GameBoard(4); //todo: add ui for teams
                    //changeCurrentPanel(GAME_BOARD, this);
                    Color color = GeneralUtils.parseColor(JsonFile.read(fileName, "data","global_color"));
                    JPanel teamChoosePanel = new JPanel(new GridBagLayout()){
                        @Override
                        protected void paintComponent(Graphics g) {
                            super.paintComponent(g);
                            Graphics2D g2d = (Graphics2D) g;

                            //draw background
                            GradientPaint gradientPaint = new GradientPaint(0,0,color,getWidth(),getHeight(),ScrollGridPanel.adjustColor(color));
                            g2d.setPaint(gradientPaint);
                            g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 0,0));
                        }
                    };

                    GridBagConstraints gbc = new GridBagConstraints();
                    gbc.gridx = 0;
                    gbc.gridy = 0;
                    gbc.fill = 0;

                    //main body
                    JPanel contentPane = new JPanel(new FlowLayout(FlowLayout.CENTER));
                    contentPane.setPreferredSize(new Dimension(300,200));
                    contentPane.setBackground(Color.white);

                    JLabel numTeamText = new JLabel("Number of Teams");
                    numTeamText.setFont(generateFont(35));

                    JComboBox<String> dropDown = new JComboBox<>(dropDownList);
                    dropDown.setFont(generateFont(20));
                    dropDown.setFocusable(false);

                    //todo: add 2 buttons, sound / start use checkbox gen


                    contentPane.add(numTeamText);
                    contentPane.add(dropDown);
                    teamChoosePanel.add(contentPane, gbc);
                    changeCurrentPanel(teamChoosePanel, this);
                    break;
                }
                case 1: { //board file
                    JFileChooser chooser = new JFileChooser();
                    chooser.setFileFilter(new FileNameExtensionFilter("Json File", "json"));

                    if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                        File file = chooser.getSelectedFile();
                        changeFileName(String.valueOf(file));
                        changeColor(parseColor(JsonFile.read(fileName, "data", "global_color")));
                    }
                    break;
                }
                case 2: { //board creator
                    changeCurrentPanel(new BoardFactory(), this);
                    break;
                }
                case 3: { //settings
                    changeCurrentPanel(new Settings(), this);
                    break;
                }
                case 4: { //exit
                    System.exit(0);
                    break;
                }
            }
        });

        return button;
    }
    private void changeFileName(String newFile){
        fileName = newFile.substring(newFile.lastIndexOf("\\") + 1);
        currentFile.setText("Current Board: " + fileName);
        buttonsList.get(0).setEnabled(!fileName.isEmpty());
    }
}

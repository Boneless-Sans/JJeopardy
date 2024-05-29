package com.boneless;

import com.boneless.util.JsonFile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static com.boneless.Main.*;
import static com.boneless.util.GeneralUtils.*;

public class GameBoard extends JPanel {
    public boolean GameIsActive;
    public boolean jCardIsActive = false;
    public static final Color mainColor = parseColor(JsonFile.read(fileName, "data", "global_color"));
    public static final Color fontColor = parseColor(JsonFile.read(fileName, "data", "font_color"));
    public final JPanel boardPanel;
    private final int teamCount;
    public int scoreToAdd = 0;

    public GameBoard(int teamCount) {
        GameIsActive = true;
        MAIN_MENU.menuIsActive = false;
        this.teamCount = teamCount;

        setLayout(new BorderLayout());
        setBackground(mainColor);
        setFocusable(true);
        HeaderPanel headerPanel = new HeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        boardPanel = mainBoard();
        add(boardPanel, BorderLayout.CENTER);
        add(createTeamsPanel(), BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    // panel to contain the main board grid
    private JPanel mainBoard() {
        JPanel panel = new JPanel();

        int boardX = Integer.parseInt(JsonFile.read(fileName, "data", "categories"));
        int boardY = Integer.parseInt(JsonFile.read(fileName, "data", "rows")) + 1;
        panel.setLayout(new GridLayout(boardY, boardX, 1, 1));

        for (int i = 0; i < boardX; i++) {
            panel.add(createCatPanel(i));
        }

        for (int i = 0; i < boardY - 1; i++) {
            for (int j = 0; j < boardX; j++) {
                try {
                    String scoreString = JsonFile.readWithThreeKeys(fileName, "board", "scores", "row_" + i);
                    int score = Integer.parseInt(scoreString);
                    String question = JsonFile.readWithThreeKeys(fileName, "board", "col_" + j, "question_" + i);
                    String answer = JsonFile.readWithThreeKeys(fileName, "board", "col_" + j, "answer_" + i);

                    BoardButton button = new BoardButton(score, question, answer, mainColor);
                    button.setBackground(mainColor);
                    panel.add(button);
                } catch (Exception e) {
                    // ignore
                }
            }
        }
        return panel;
    }

    private JPanel createCatPanel(int index) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createBevelBorder(0));
        panel.setBackground(mainColor);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = 0;

        panel.add(new JLabel(JsonFile.readWithThreeKeys(fileName, "board", "categories", "cat_" + index)), gbc);
        return panel;
    }
    private JScrollPane createTeamsPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBackground(mainColor);
        panel.setBorder(null);

        for(int i = 0; i < teamCount; i++){
            Team team = new Team();
            panel.add(gapPanel(80));
            panel.add(team);
        }
        panel.add(gapPanel(80));

        JScrollPane pane = new JScrollPane(panel); // Set the panel as the viewport view
        pane.setPreferredSize(new Dimension(getWidth(), 120));
        pane.setBorder(null);
        pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        return pane;
    }

    public static JPanel gapPanel(int size){
        JPanel panel = new JPanel();
        panel.setBackground(mainColor);
        panel.setPreferredSize(new Dimension(size,size));
        return panel;
    }
    public void exit() {
        int size = 32;

        BufferedImage bufferedImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = bufferedImage.createGraphics();

        g2d.fillRect(0, 0, size, size);

        g2d.setColor(Color.black);

        g2d.setFont(generateFont(30));

        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth("?");
        int textHeight = fm.getHeight();
        int x = (size - textWidth) / 2;
        int y = (size - textHeight) / 2 + fm.getAscent();

        g2d.drawString("?", x, y);

        g2d.dispose();

        String[] responses = {"Exit", "Resume"};
        int answer = JOptionPane.showOptionDialog(
                null,
                "Really gonna dip out?",
                "",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                new ImageIcon(bufferedImage), responses, 0);
        if (answer == 0) {
            GAME_BOARD.GameIsActive = false;
            MAIN_MENU.menuIsActive = true;
            changeCurrentPanel(MAIN_MENU, GAME_BOARD);
        }
    }

    static class HeaderPanel extends JPanel {
        public static JLabel leftText;
        public static JPanel rightPanel;
        public static JLabel rightText;
        private JPanel rightInfoPanel;
        private JPanel rightInfoParentPanel;
        public static int fontSize = 20;

        public HeaderPanel() {
            setBackground(mainColor);
            setLayout(new BorderLayout());

            leftText = new JLabel("Exit");
            leftText.setForeground(fontColor);
            leftText.setFont(generateFont(fontSize));

            JButton exitButton = createHeaderButton("exit", true);

            JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            leftPanel.setOpaque(false);
            leftPanel.add(exitButton);
            leftPanel.add(leftText);

            JLabel title = new JLabel(JsonFile.read(fileName, "data", "board_name"));
            title.setForeground(fontColor);
            title.setFont(generateFont(fontSize));

            JPanel titlePanel = new JPanel(new GridBagLayout());
            titlePanel.setOpaque(false);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.fill = 0;

            rightText = new JLabel("Reveal Correct Answer");
            rightText.setForeground(fontColor);
            rightText.setFont(generateFont(fontSize));

            titlePanel.add(title, gbc);

            rightPanel = createRightPanel(rightText, createHeaderButton("continue", false));

            add(leftPanel, BorderLayout.WEST);
            add(titlePanel, BorderLayout.CENTER);
            add(createRightPanel(rightText, createHeaderButton("continue", false)), BorderLayout.EAST);
        }

        private JPanel createRightPanel(JLabel label, JButton button) {
            rightInfoParentPanel = new JPanel(null);
            rightInfoParentPanel.setBackground(mainColor);

            rightInfoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            rightInfoPanel.add(label);
            rightInfoPanel.add(button);
            rightInfoPanel.setOpaque(false);

            Dimension parentSize = rightInfoParentPanel.getSize();
            Dimension panelSize = rightInfoPanel.getPreferredSize();
            rightInfoPanel.setBounds(parentSize.width - panelSize.width, 0, panelSize.width, panelSize.height);

            rightInfoParentPanel.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    Dimension parentSize = rightInfoParentPanel.getSize();
                    Dimension panelSize = rightInfoPanel.getPreferredSize();
                    rightInfoPanel.setBounds(parentSize.width - panelSize.width, 0, panelSize.width, panelSize.height);
                }
            });

            rightInfoParentPanel.add(rightInfoPanel);
            return rightInfoParentPanel;
        }

        public void movePanel(boolean isOpen) {
            int tickSpeed = 1;
            Thread thread = new Thread(() -> {
                while (isOpen ? rightInfoPanel.getX() > 0 : rightInfoPanel.getX() < rightInfoParentPanel.getWidth()) {
                    try {
                        rightInfoPanel.setBounds(rightInfoPanel.getX() + (isOpen ? -tickSpeed : tickSpeed),
                                rightInfoPanel.getY(), rightInfoPanel.getWidth(), rightInfoPanel.getHeight());
                        Thread.sleep(tickSpeed);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }

        public static JButton createHeaderButton(String text, boolean isExit) {
            String rawKeyBind = JsonFile.read("settings.json", "keyBinds", text);
            String keyBind = rawKeyBind.substring(0, 1).toUpperCase() + rawKeyBind.substring(1);
            JButton button = new JButton(keyBind);
            button.setFocusable(false);
            button.setFont(generateFont(20));
            button.addActionListener(e -> {
                if (isExit) {
                    if (GAME_BOARD.GameIsActive)
                        GAME_BOARD.exit();
                    else
                        jCard.exit();
                } else {
                    jCard.advance();
                }
            });

            return button;
        }
    }

    private class BoardButton extends JButton {
        private final int score;
        private final String question;
        private final String answer;
        private final Color mainColor;

        public BoardButton(int score, String question, String answer, Color mainColor) {
            this.score = score;
            this.question = question;
            this.answer = answer;
            this.mainColor = mainColor;
            setText(String.valueOf(score));
            setBackground(mainColor);
            setFocusable(false);
            addActionListener(listener());
        }

        private ActionListener listener() {
            return e -> {
                HeaderPanel.leftText.setText("Back");

                JPanel parentPanel = (JPanel) getParent();
                jCard = new JCard(question, answer);
                jCardIsActive = true;
                GameIsActive = false;
                scoreToAdd = score;
                changeCurrentPanel(jCard, parentPanel);
            };
        }
    }
}

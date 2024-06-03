package com.boneless;

import com.boneless.util.JsonFile;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

import static com.boneless.GameBoard.HeaderPanel.*;
import static com.boneless.Main.*;
import static com.boneless.util.GeneralUtils.*;

public class GameBoard extends JPanel {
    public boolean GameIsActive;
    public boolean jCardIsActive = false;
    public static final Color mainColor = parseColor(JsonFile.read(fileName, "data", "global_color"));
    public static final Color fontColor = parseColor(JsonFile.read(fileName, "data", "font_color"));
    public static int fontSize = 20;
    public final JPanel boardPanel;
    public int scoreToAdd = 0;
    private final int teamCount;

    public GameBoard(int teamCount){
        this.teamCount = teamCount;
        GameIsActive = true;
        mainMenu.menuIsActive = false;

        setLayout(new BorderLayout());
        setBackground(mainColor);
        setFocusable(true);
        HeaderPanel headerPanel = new HeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        boardPanel = mainBoard();
        add(boardPanel, BorderLayout.CENTER);
        if(teamCount > 0) add(createTeamsPanel(), BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    // panel to contain the main board grid
    private JPanel mainBoard() {
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

                BoardButton button = new BoardButton(score, question, answer, 0);
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

    private JScrollPane createTeamsPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBackground(mainColor);
        panel.setBorder(null);

        int teamPanelWidth = 150;
        int totalTeamsWidth = teamCount * teamPanelWidth;
        int availableWidth = getWidth();

        //gap size calc
        int dynamicGapSize = (availableWidth - totalTeamsWidth) / (teamCount + 1);
        if (dynamicGapSize < 0) {
            dynamicGapSize = 0;
        }

        for (int i = 0; i < teamCount; i++) {
            panel.add(createGap(dynamicGapSize, mainColor));
            panel.add(new Team());
        }
        panel.add(createGap(dynamicGapSize, mainColor));

        HiddenScroller pane = new HiddenScroller(panel);
        pane.setPreferredSize(new Dimension(getWidth(), 120));
        pane.setBorder(null);
        pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        return pane;
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
                "Is your departure truly inevitable?",
                "",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                new ImageIcon(bufferedImage), responses, 0);
        if (answer == 0) {
            gameBoard.GameIsActive = false;
            mainMenu.menuIsActive = true;
            mainMenu.timer.start();
            Team.teamCount = 0;
            changeCurrentPanel(mainMenu, gameBoard);
        }
    }

    public static class HiddenScroller extends JScrollPane {

        public HiddenScroller(Component view) {
            super(view);
            setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            JScrollBar horizontalScrollBar = getHorizontalScrollBar();
            horizontalScrollBar.setUI(new HiddenScrollUI());
            horizontalScrollBar.setPreferredSize(new Dimension(0, 0)); //hide
        }

        // Custom ScrollBarUI to customize scrollbar appearance
        private static class HiddenScrollUI extends BasicScrollBarUI {
            @Override protected void configureScrollBarColors() {}
            @Override protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {}
            @Override protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {}
        }
    }

    public static class HeaderPanel extends JPanel {
        public static JLabel leftText;
        public static JPanel rightPanel;

        public HeaderPanel() {
            setBackground(mainColor);
            setLayout(new GridLayout());

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

            titlePanel.add(title, gbc);

            rightPanel = createRightPanel(true);

            add(leftPanel);
            add(titlePanel);
            add(rightPanel);
        }

        static JPanel createRightPanel(boolean blank) {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
            panel.setBackground(mainColor);

            if(!blank) {
                JLabel rightText = new JLabel("Reveal Correct Answer");
                rightText.setForeground(fontColor);
                rightText.setFont(generateFont(fontSize));

                panel.add(rightText);
                panel.add(createHeaderButton("continue", false));
            }
            return panel;
        }

        public static JButton createHeaderButton(String text, boolean isExit) {
            String rawKeyBind = JsonFile.read("settings.json", "keyBinds", text);
            String keyBind = rawKeyBind.substring(0, 1).toUpperCase() + rawKeyBind.substring(1);
            JButton button = new JButton(keyBind);
            button.setFocusable(false);
            button.setFont(generateFont(20));
            button.addActionListener(e -> {
                if (isExit) {
                    if (gameBoard.GameIsActive)
                        gameBoard.exit();
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
        private final int arcSize;

        public BoardButton(int score, String question, String answer, int arcSize) {
            this.score = score;
            this.question = question;
            this.answer = answer;
            this.arcSize = arcSize;
            setText(String.valueOf(score));
            setBackground(mainColor);
            setFocusable(false);
            addActionListener(listener());
        }

        private ActionListener listener() {
            return e -> {
                leftText.setText("Back");

                rightPanel.removeAll();
                rightPanel.add(createRightPanel(false));
                rightPanel.revalidate();
                rightPanel.repaint();

                JPanel parentPanel = (JPanel) getParent();
                jCard = new JCard(question, answer, this);
                jCardIsActive = true;
                GameIsActive = false;
                scoreToAdd = score;
                setEnabled(false);
                changeCurrentPanel(jCard, parentPanel);
            };
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Background
            g2d.setColor(getBackground());
            Shape backgroundShape = new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), arcSize, arcSize);
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
            g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arcSize, arcSize);

            g2d.dispose();
        }
    }
}

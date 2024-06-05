package com.boneless;

import com.boneless.util.GeneralUtils;
import com.boneless.util.JsonFile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static com.boneless.GameBoard.*;
import static com.boneless.GameBoard.HeaderPanel.*;
import static com.boneless.Main.*;
import static com.boneless.util.GeneralUtils.*;

public class JCard extends JPanel {
    private JLabel questionLabel;
    private JLabel answerLabel;
//    private JLabel questionQuestion;
//    private JLabel answerAnswer;
    private JLabel moron;
    private boolean hasFaded = false;
    private boolean hasFadedIn = true;
    private final static Color parseColorFadeComplete = GeneralUtils.parseColorFade(JsonFile.read(fileName, "data", "font_color"), 0);
    private final JPanel moversPanel;
    private final JPanel fadePanel;
    private final JPanel fadePanel2;
    private final JPanel dottedLinePanel;
    private final JButton sourceButton;


    public JCard(String question, String answer, JButton sourceButton) {
        this.sourceButton = sourceButton;
        setLayout(null);

        setBackground(mainColor);

        moversPanel = new JPanel(new GridBagLayout());
        moversPanel.setBackground(mainColor);

        fadePanel = new JPanel(new GridBagLayout());
        fadePanel.setBackground(mainColor);

        fadePanel2 = new JPanel(new GridBagLayout());
        fadePanel2.setBackground(mainColor);

        questionLabel = new JLabel(question);
        questionLabel.setForeground(fontColor);
        questionLabel.setOpaque(false);

        answerLabel = new JLabel(answer);
        answerLabel.setForeground(GeneralUtils.parseColorFade(JsonFile.read(fileName, "data", "font_color"), 0));
        answerLabel.setOpaque(false);

        moron = new JLabel();
        moron.setForeground(GeneralUtils.parseColorFade(JsonFile.read(fileName, "data", "font_color"), 0));
        moron.setOpaque(false);

        dottedLinePanel = createDottedLinePanel();

        moversPanel.add(questionLabel);
        fadePanel.add(answerLabel);
        //fadePanel2.add(moron);

        //add(fadePanel2);
        add(moversPanel);
        add(fadePanel);
        add(dottedLinePanel);


        centerTestPanel();


        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                centerTestPanel();
                dottedLinePanel.setBounds(0, getHeight() / 2, getWidth(), 10);
            }
        });

        setupMouseListeners();
        setUpCharacters();
    }

    private JPanel createDottedLinePanel() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawDottedLine(g, getWidth());
            }
        };
    }

    private void drawDottedLine(Graphics g, int width) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLACK); // Change to your desired color

        int y = getHeight() / 2;
        int dotSize = 2;
        int gapSize = 2;

        for (int x = 0; x < width; x += dotSize + gapSize) {
            g2d.fillRect(x, y, dotSize, dotSize);
        }
    }

    private void centerTestPanel() {
        int factor = 2;
        int sizeX2 = getWidth() - (getWidth() / factor);
        int sizeY = (getHeight() - (getHeight() / factor)) / factor;
        int x = (getWidth() / 2) - (sizeX2 / 2);
        int y = (getHeight() / 2) - (sizeY / 2);
        int sixth = (getHeight() - (getHeight() / 2)) / 6;
        moversPanel.setBounds(x, y, sizeX2, sizeY);
        fadePanel.setBounds(x, y + sixth, sizeX2, sizeY);
        fadePanel2.setBounds(x, y - sixth, sizeX2, sizeY);
        revalidate();
        repaint();
    }

    private void setUpCharacters() {
        questionLabel.setFont(GeneralUtils.generateFont(30));
        answerLabel.setFont(GeneralUtils.generateFont(30));
        moron.setFont(GeneralUtils.generateFont(10));
        setBackground(mainColor);
    }

    private void setupMouseListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!hasFaded) {
                    moveQuestion();
                }
            }
        });

        setFocusable(true);
        requestFocusInWindow();
    }

    public void moveQuestion() {//todo: on the right header panel, on advance text needs to change to continue
        if (hasFaded) {
            advanceExit();
            return;
        }
        hasFaded = true;

        int sizeX = 400;
        int sizeY = 200;
        int x = (getWidth() - sizeX) / 2;
        int yQuestion = (getHeight() - sizeY) / 2;
        int targetY = 50; // Target Y position for question label

        Timer q = new Timer(7, null);
        q.addActionListener(new ActionListener() {
            private int currentY = yQuestion;

            @Override
            public void actionPerformed(ActionEvent e) {
                currentY -= 1; // Adjust this value to control the speed of movement
                if (currentY <= targetY) {
                    currentY = targetY;
                    q.stop();
                    fadeInAnswer();
                }
                moversPanel.setBounds(x, currentY, sizeX, sizeY);
                revalidate();
                repaint();
            }
        });
        q.start();
    }

    private void fadeInAnswer() {
        if (!hasFadedIn) {
            return;
        }
        hasFadedIn = false;

        Timer j = new Timer(50, null);
        j.addActionListener(new ActionListener() {
            private float opacity2 = 0.0f;

            @Override
            public void actionPerformed(ActionEvent e) {
                opacity2 += 0.05f;
                if (opacity2 >= 1.0f) {
                    opacity2 = 1.0f;
                    j.stop();
                }

                Color fadedColor = GeneralUtils.parseColorFade(JsonFile.read(fileName, "data", "font_color"), (int)(opacity2 * 255));
                answerLabel.setForeground(fadedColor);
                moron.setForeground(fadedColor);
                System.out.println("Opacity: " + opacity2 + ", Color: " + fadedColor);

                revalidate();
                repaint();
            }
        });
        j.start();
    }

    private void advanceExit(){
        exit();
        sourceButton.setEnabled(false);
    }
    public void exit() {
        leftText.setText("Exit");
        rightPanel.removeAll();
        rightPanel.add(createRightPanel(true));
        rightPanel.revalidate();
        rightPanel.repaint();
        sourceButton.setEnabled(true);
        gameBoard.jCardIsActive = false;
        gameBoard.GameIsActive = true;
        hasFaded = false;
        changeCurrentPanel(gameBoard.boardPanel, this);
    }
}
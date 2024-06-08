package com.boneless;

import com.boneless.util.AnimeJLabel;
import com.boneless.util.JsonFile;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import static com.boneless.GameBoard.*;
import static com.boneless.GameBoard.HeaderPanel.*;
import static com.boneless.Main.*;
import static com.boneless.util.GeneralUtils.*;

public class JCard extends JPanel {
    private final AnimeJLabel test;
    private final JLabel questionLabel;
    private final JLabel answerLabel;
    private boolean hasFaded = false;
    private boolean hasFadedIn = true;
    private final static Color parseColorFadeComplete = parseColorFade(JsonFile.read(fileName, "data", "font_color"), 0);
    private final JPanel moversPanel;
    private final JPanel fadePanel;
    private final JPanel fadePanel2;
    private final JButton sourceButton;
    private static final Random random = new Random();

    // Class variables for opacity and faded state
    private float opacity2 = 0.0f;
    private float opacity3 = 0.0f;
    private final Stroke dashedLineStroke = getDashedLineStroke(1);

    public JCard(String question, String answer, JButton sourceButton) {
        this.sourceButton = sourceButton;
        setLayout(null);

        setBackground(mainColor);

        moversPanel = new JPanel(new GridBagLayout());
        moversPanel.setBackground(mainColor);
        moversPanel.setOpaque(false);

        fadePanel = new JPanel(new GridBagLayout());
        fadePanel.setBackground(mainColor);
        fadePanel.setOpaque(false);

        fadePanel2 = new JPanel(new GridBagLayout());
        fadePanel2.setBackground(mainColor);

        questionLabel = new JLabel(question);
        questionLabel.setForeground(fontColor);
        questionLabel.setOpaque(false);

        test = new AnimeJLabel(fontColor, fontColor, 1);
        test.setForeground(fontColor);
        test.setOpaque(false);

        answerLabel = new JLabel(answer);
        answerLabel.setForeground(parseColorFadeComplete);
        answerLabel.setOpaque(false);

        moversPanel.add(test);
        fadePanel.add(answerLabel);
        add(moversPanel);
        add(fadePanel);


        switch (generateRandomNumber()){
            case 0: {
                //
                String[] arr = new String[question.length()];
                for (int i = 0; i < question.length(); i++) {
                    arr[i] = question.substring(0, i + 1);
                }
                test.setTxtAniam(arr, 100);
                break;
            }
            case 1: {

                moversPanel.add(questionLabel);
                fadeInQuestion();
                break;
            }
            default: {
                moversPanel.add(questionLabel);
            }
        }


        centerTestPanel();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                centerTestPanel();
            }
        });

        setupMouseListeners();
        setUpCharacters();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity2));
        g2.setColor(fontColor);
        g2.setStroke(dashedLineStroke);

        int factor = 2;
        int sizeY = (getHeight() - (getHeight() / factor)) / factor;
        int sixth = (getHeight() - (getHeight() / 2)) / 6;
        int y = (getHeight() / 2) - (sizeY / 2);
        int y3 = (getHeight() / 2) - (sizeY / 2);
        int yComplete = ((y + sixth) + y3) / 2;

        g2.drawLine(10, yComplete, this.getWidth() - 10, yComplete);

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }

    public static int generateRandomNumber() {
        return new Random().nextInt(0,2);
    }

    public Stroke getDashedLineStroke(int width) {
        float[] dashPattern = {5, 5}; //Setting the length of dot and spacing of dot: {dot length, space width}
        return new BasicStroke(width, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 2, dashPattern, 0);
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
        questionLabel.setFont(generateFont(30));
        answerLabel.setFont(generateFont(30));
        test.setFont(generateFont(30));
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

    public void moveQuestion() {
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

        Timer questionMoveUp = new Timer(7, null);
        questionMoveUp.addActionListener(new ActionListener() {
            private int currentY = yQuestion;

            @Override
            public void actionPerformed(ActionEvent e) {
                currentY -= 1; // Adjust this value to control the speed of movement
                if (currentY <= targetY) {
                    currentY = targetY;
                    questionMoveUp.stop();
                    fadeInAnswer();
                }
                moversPanel.setBounds(x, currentY, sizeX, sizeY);
                revalidate();
                repaint();
            }
        });
        questionMoveUp.start();
    }

    private void fadeInAnswer() {
        if (!hasFadedIn) {
            return;
        }
        hasFadedIn = false;

        Timer opacityFadeUp = new Timer(50, null);
        opacityFadeUp.addActionListener(e -> {
            opacity2 += 0.05f;
            if (opacity2 >= 1.0f) {
                opacity2 = 1.0f;
                ((Timer) e.getSource()).stop();
            }

            Color fadedColor = parseColorFade(JsonFile.read(fileName, "data", "font_color"), (int)(opacity2 * 255));
            answerLabel.setForeground(fadedColor);
            System.out.println("Opacity: " + opacity2 + ", Color: " + fadedColor);

            revalidate();
            repaint();
        });
        opacityFadeUp.start();
    }

    private void fadeInQuestion() {
        if (!hasFadedIn) {
            return;
        }
        hasFadedIn = false;

        Timer opacityFadeUp = new Timer(50, null);
        opacityFadeUp.addActionListener(e -> {
            opacity3 += 0.05f;
            if (opacity3 >= 1.0f) {
                opacity3 = 1.0f;
                ((Timer) e.getSource()).stop();
            }

            Color fadedColor = parseColorFade(JsonFile.read(fileName, "data", "font_color"), (int)(opacity3 * 255));
            questionLabel.setForeground(fadedColor);
            System.out.println("Opacity: " + opacity3 + ", Color: " + fadedColor);

            revalidate();
            repaint();
        });
        opacityFadeUp.start();
    }

    private void advanceExit() {
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
        changeCurrentPanel(gameBoard.boardPanel, this, false);
    }
}

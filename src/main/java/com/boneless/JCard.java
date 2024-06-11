package com.boneless;

import com.boneless.util.JsonFile;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.util.Random;

import static com.boneless.GameBoard.*;
import static com.boneless.GameBoard.HeaderPanel.*;
import static com.boneless.Main.*;
import static com.boneless.util.GeneralUtils.*;

public class JCard extends JPanel {
    private final SpinningLabel animatedQuestion;
    private final JLabel answerLabel;
    private boolean hasFaded = false;
    private boolean hasFadedIn = true;
    private boolean hasFadedInQuestion = true;
    private final static Color parseColorFadeComplete = parseColorFade(JsonFile.read(fileName, "data", "font_color"), 0);
    private final JPanel moversPanel;
    private final JPanel fadePanel;
    private final JPanel fadePanel2;
    private final JButton sourceButton;

    public int animationJCardBound = 1;
    // Set animationJCardBound to 1 for no animations
    // Set animationJCardBound to # of cases + 1 in animationSelect for all animations

    // Class variables for opacity and faded state
    private float opacity2 = 0.0f; // Question opacity
    private float opacity3 = 0.0f; // Question opacity
    private float opacity4 = 0.0f; // FlashBang opacity
    private float opacity5 = 0.0f; // FlashBangReverse opacity
    private final Stroke dashedLineStroke = getDashedLineStroke(1);

    private final String question;

    public JCard(String question, String answer, JButton sourceButton) {
        this.question = question;
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

        animatedQuestion = new SpinningLabel(question);
        animatedQuestion.setForeground(fontColor);
        animatedQuestion.setOpaque(false);

        answerLabel = new JLabel(answer);
        answerLabel.setForeground(parseColorFadeComplete);
        answerLabel.setOpaque(false);

        moversPanel.add(animatedQuestion);
        fadePanel.add(answerLabel);
        add(moversPanel);
        add(fadePanel);

        try {
            animationSelect();
        } catch (InterruptedException ignored) {
        }

        centerTestPanel();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                centerTestPanel();
            }
        });

        setupMouseListeners();
        setUpCharacters(35);
    }

    private void animationSelect() throws InterruptedException {
        switch (generateRandomNumber(animationJCardBound)) {
            case 0: {
                animatedQuestion.startAnimation();
                break;
            }
            // Uncomment and add other cases here as needed
//            case 1: {
//                moversPanel.add(questionLabel);
//                fadeInQuestion();
//                break;
//            }
//            case 2: {
//                if (mainMenu.hasNotBanged) {
//                    mainMenu.hasNotBanged = false;
//                    moversPanel.add(questionLabel);
//                    startFlashBangTimingSolution();
//                }
//                moversPanel.add(questionLabel);
//                break;
//            }
        }
    }

    private static int generateRandomNumber(int bound) {
        return new Random().nextInt(bound);
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

    public Stroke getDashedLineStroke(int width) {
        float[] dashPattern = {5, 5}; // Setting the length of dot and spacing of dot: {dot length, space width}
        return new BasicStroke(width, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 2, dashPattern, 0);
    }

    private void centerTestPanel() {
        int factor = 2;
        int sizeX2 = getWidth() - (getWidth() / factor);
        int sizeY = (getHeight() - (getHeight() / factor)) / factor;
        int x = (getWidth() / 2) - (sizeX2 / 2);
        int y = (getHeight() / 2) - (sizeY / 2);
        int sixth = (getHeight() - (getHeight() / factor)) / 6;
        moversPanel.setBounds(x, y, sizeX2, sizeY);
        fadePanel.setBounds(x, y + sixth, sizeX2, sizeY);
        fadePanel2.setBounds(x, y - sixth, sizeX2, sizeY);
        revalidate();
        repaint();
    }

    @SuppressWarnings("SameParameterValue")
    private void setUpCharacters(int size) {
        animatedQuestion.setFont(generateFont(size));
        answerLabel.setFont(generateFont(size));
        setBackground(mainColor);
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

    private void fadeInQuestion() {
        if (!hasFadedInQuestion) {
            return;
        }
        hasFadedInQuestion = false;

        Timer opacityFadeUp2 = new Timer(50, null);
        opacityFadeUp2.addActionListener(e -> {
            opacity3 += 0.05f;
            if (opacity3 >= 1.0f) {
                opacity3 = 1.0f;
                ((Timer) e.getSource()).stop();
            }

            Color fadedColor = parseColorFade(JsonFile.read(fileName, "data", "font_color"), (int) (opacity3 * 255));
            animatedQuestion.setForeground(fadedColor);

            revalidate();
            repaint();
        });
        opacityFadeUp2.start();
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

            Color fadedColor = parseColorFade(JsonFile.read(fileName, "data", "font_color"), (int) (opacity2 * 255));
            answerLabel.setForeground(fadedColor);

            revalidate();
            repaint();
        });
        opacityFadeUp.start();
    }

    public void startFlashBangTimingSolution() {

        opacity4 = 0.0f;
        opacity5 = 0.0f;

        Timer opacityFadeUp2 = new Timer(500, null);
        opacityFadeUp2.addActionListener(e -> flashBang());
        opacityFadeUp2.setRepeats(false);
        opacityFadeUp2.start();
    }

    private void flashBang() {
        Timer opacityFadeUp = new Timer(5, null);
        opacityFadeUp.addActionListener(e -> {
            opacity4 += 0.05f;
            if (opacity4 >= 1.0f) {
                opacity4 = 1.0f;
                ((Timer) e.getSource()).stop();
                flashBangReverse();
            }

            Color fadedColor = parseColorFade("255,255,255", (int) (opacity4 * 255));
            setBackground(fadedColor);

            revalidate();
            repaint();
        });
        opacityFadeUp.start();
    }

    private void flashBangReverse() {
        Timer opacityFadeDown = new Timer(100, null);
        opacityFadeDown.addActionListener(e -> {
            opacity5 += 0.01f;
            if (opacity5 >= 1.0f) {
                opacity5 = 1.0f;
                ((Timer) e.getSource()).stop();
                flashBangReverseQuestion();
            }

            Color fadedColor = parseColorFade(JsonFile.read(fileName, "data", "global_color"), (int) (opacity5 * 255));
            Color fadedFontColor = parseColorFade(JsonFile.read(fileName, "data", "font_color"), (int) (opacity5 * 255));

            animatedQuestion.setForeground(fadedFontColor);

            setBackground(fadedColor);

            revalidate();
            repaint();
        });
        opacityFadeDown.start();
    }

    private void flashBangReverseQuestion() {
        Timer opacityFadeUp = new Timer(5, null);
        opacityFadeUp.addActionListener(e -> {
            if (opacity5 >= 0.6f) {
                animatedQuestion.setForeground(fontColor);
            }
        });
        opacityFadeUp.start();
        opacityFadeUp.setRepeats(false);
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
        setBackground(mainColor);
    }

    // Custom JPanel for spinning characters
    private static class SpinningLabel extends JPanel {
        private final String text;
        private final double[] angles;

        public SpinningLabel(String text) {
            this.text = text;
            angles = new double[text.length()];
            setPreferredSize(new Dimension(400, 100)); // Adjust as needed
        }

        public void startAnimation() {
            Timer timer = new Timer(100, e -> {
                for (int i = 0; i < angles.length; i++) {
                    angles[i] += Math.PI / 8; // Adjust rotation speed as needed
                }
                repaint();
            });
            timer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            FontMetrics fm = g2d.getFontMetrics();
            int x = 10;
            int y = getHeight() / 2;

            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);
                int charWidth = fm.charWidth(c);
                AffineTransform original = g2d.getTransform();
                g2d.rotate(angles[i], x + (double) charWidth / 2, y);
                g2d.drawString(String.valueOf(c), x, y);
                g2d.setTransform(original);
                x += charWidth;
            }
        }
    }
}

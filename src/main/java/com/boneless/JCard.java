package com.boneless;

import com.boneless.util.AnimeJLabel;
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
    private final SpinningLabel spinningQuestion;
    private final AnimeJLabel typeQuestion;
    private final JLabel answerLabel;
    private final JLabel questionLabel;
    private boolean hasFaded = false;
    private boolean hasFadedIn = true;
    private boolean hasFadedInQuestion = true;
    private final static Color parseColorFadeComplete = parseColorFade(JsonFile.read(fileName, "data", "font_color"), 0);
    private final JPanel moversPanel;
    private final JPanel fadePanel;
    private final JPanel fadePanel2;
    private final JButton sourceButton;


    private final boolean animationCheck = Boolean.parseBoolean(JsonFile.read(settingsFile, "misc", "play_card_animations"));
    public int animationJCardBound = 4;
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

        spinningQuestion = new SpinningLabel(question);
        spinningQuestion.setForeground(fontColor);
        spinningQuestion.setOpaque(false);

        typeQuestion = new AnimeJLabel(fontColor, fontColor, 1);
        typeQuestion.setForeground(fontColor);
        typeQuestion.setOpaque(false);

        questionLabel = new JLabel(question);
        questionLabel.setForeground(fontColor);
        questionLabel.setOpaque(false);

        answerLabel = new JLabel(answer);
        answerLabel.setForeground(parseColorFadeComplete);
        answerLabel.setOpaque(false);

        fadePanel.add(answerLabel);
        add(moversPanel);
        add(fadePanel);

        try {
            if(animationCheck){
                System.out.println("its running bozo");
                animationSelect();
            }else{
                moversPanel.add(questionLabel);
            }
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
        setUpCharacters(32);
    }

    private void animationSelect() throws InterruptedException {
        switch (generateRandomNumber(animationJCardBound)) {
            case 0: {
                moversPanel.add(spinningQuestion);
                spinningQuestion.startAnimation(2500);
                break;
            }
            case 1: {
                moversPanel.add(questionLabel);
                fadeInQuestion();
                break;
            }
            case 2: {
                if (mainMenu.hasNotBanged) {
                    moversPanel.add(questionLabel);
                    mainMenu.hasNotBanged = false;
                    startFlashBangTimingSolution();
                }
                moversPanel.add(questionLabel);
                break;
            }
            case 3: {
                moversPanel.add(typeQuestion);
                String[] arr = new String[question.length()];
                for (int i = 0; i < question.length(); i++) {
                    arr[i] = question.substring(0, i + 1);
                }
                typeQuestion.setTxtAnim(arr, 100);
                break;
            }
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
        typeQuestion.setFont(generateFont(size));
        spinningQuestion.setFont(generateFont(size));
        questionLabel.setFont(generateFont(size));
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
            questionLabel.setForeground(fadedColor);

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

            questionLabel.setForeground(fadedFontColor);

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
                questionLabel.setForeground(fontColor);
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

    private static class SpinningLabel extends JPanel {
        private final String text;
        private final double[] angles;
        private final Timer timer;

        public SpinningLabel(String text) {
            this.text = text;
            angles = new double[text.length()];
            setPreferredSize(new Dimension(350, 100));
            setLayout(new GridBagLayout());
            timer = new Timer(100, e -> {
                for (int i = 0; i < angles.length; i++) {
                    angles[i] += Math.PI / 12; // Adjust rotation speed as needed
                }
                repaint();
            });
        }

        public void startAnimation(int durationInMilliseconds) {
            timer.start();
            new Timer(durationInMilliseconds, e -> timer.stop()).start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            FontMetrics fm = g2d.getFontMetrics();

            // Calculate the total width of the text
            int totalTextWidth = 0;
            for (int i = 0; i < text.length(); i++) {
                totalTextWidth += fm.charWidth(text.charAt(i));
            }

            // Calculate the starting x position to center the text
            int x = (getWidth() - totalTextWidth) / 2;
            int y = getHeight() / 2 + fm.getAscent() / 2;

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
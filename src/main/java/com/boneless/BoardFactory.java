package com.boneless;

import com.boneless.util.JsonFile;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Random;

import static com.boneless.Main.*;
import static com.boneless.util.GeneralUtils.*;

public class BoardFactory extends JPanel {
    private final JFrame parent;
    private JPanel boardPanel;
    private MockJCard card;

    private Color mainColor;
    public Color accentColor;
    private Color fontColor;

    public boolean factoryIsActive;
    private boolean changesMade = false;
    private boolean inJCard = false;

    private final int fontSize = 20;
    private final String tempDir = System.getProperty("java.io.tmpdir");

    private final ArrayList<JTextField> catFields = new ArrayList<>();
    private final ArrayList<MockJCard.TextBox> questionLabels = new ArrayList<>();
    private final ArrayList<MockJCard.TextBox> answerLabels = new ArrayList<>();

    public BoardFactory(JFrame parent){
        factoryIsActive = true;
        this.parent = parent;

        if(fileName != null){
            loadColors();
        } else {
            System.out.println("File is null, creating new template...");
            fileName = createNewFile("temp").getAbsolutePath();
            System.out.println("File Name: " + fileName);
            loadColors();
        }
        setLayout(new BorderLayout());
        parent.setJMenuBar(menuBar());
        setBackground(mainColor);

        reload();
    }

    private void loadColors(){ //not really needed, but its cleaner
        mainColor = parseColor(JsonFile.read(fileName, "data", "global_color"));
        accentColor = new Color(
                clamp(mainColor.getRed()   - 40),
                clamp(mainColor.getGreen() - 40),
                clamp(mainColor.getBlue()  - 40));
        fontColor = parseColor(JsonFile.read(fileName, "data", "font_color"));
    }

    private void reload(){
        removeAll();

        JPanel panel = new JPanel(new BorderLayout()); //stupid work around
        panel.setBackground(mainColor);

        panel.add(headerPanel(), BorderLayout.NORTH);
        panel.add(boardPanel = boardPanel(), BorderLayout.CENTER);

        add(panel, BorderLayout.CENTER);
        add(controlPanel(), BorderLayout.EAST);

        revalidate();
        repaint();
        parent.revalidate();
        parent.repaint();
    }

    private JMenuBar menuBar(){
        //use macOS's system menu bar instead of a frame. Windows will default
        System.setProperty("apple.laf.useScreenMenuBar", "true");

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
                File file = new File(tempDir + "/" + textField.getText());
            }
        });

        JMenuItem openItem = new JMenuItem("Open Board");
        openItem.addActionListener(e -> {
            //open dialog > load file()
            JPanel panel = new JPanel();

            JTextField textField = new JTextField(10);

            panel.add(textField);

            int userInput = JOptionPane.showConfirmDialog(null, panel, "", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

            if(userInput == JOptionPane.OK_OPTION){
                createNewFile(textField.getText());
            }
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

                MockBoardButton button = new MockBoardButton(score, question, answer, 20, i, j);
                button.setBackground(mainColor);
                button.setForeground(fontColor);
                button.setFont(generateFont(fontSize));
                panel.add(button);
            }
        }

        return panel;
    }

    private JPanel createCatPanel(int index) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createBevelBorder(0));
        panel.setBackground(accentColor);

        //setup field
        JTextField field = new JTextField(10);
        field.setFont(generateFont(fontSize));
        field.setForeground(fontColor);
        field.setBackground(accentColor);
        field.setBorder(BorderFactory.createBevelBorder(1));
        field.setHorizontalAlignment(JTextField.CENTER);
        field.setText(JsonFile.readWithThreeKeys(fileName, "board", "categories", "cat_" + index));
        field.setCaretColor(fontColor);

        panel.add(field, gbc);
        catFields.add(field);

        return panel;
    }

    private JPanel headerPanel(){
        JPanel panel = new JPanel(new GridLayout());
        panel.setBackground(accentColor);

        //left panel
        JLabel leftText = new JLabel("Exit");
        leftText.setForeground(fontColor);
        leftText.setFont(generateFont(fontSize));

        String rawKeyBind = JsonFile.read("settings.json", "keyBinds", "exit");
        String keyBind = rawKeyBind.substring(0, 1).toUpperCase() + rawKeyBind.substring(1);

        JButton headerExitButton = new JButton(keyBind);

        headerExitButton.setFocusable(false);
        headerExitButton.setFont(generateFont(20));
        headerExitButton.setForeground(Color.black);
        headerExitButton.addActionListener(e -> {
            if (inJCard) changeCurrentPanel(boardPanel, card, false);
            else exit();
        });

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setOpaque(false);
        leftPanel.add(headerExitButton);
        leftPanel.add(leftText);

        //center panel
        JLabel title = new JLabel(JsonFile.read(fileName, "data", "board_name"));
        title.setForeground(fontColor);
        title.setFont(generateFont(fontSize));

        JPanel titlePanel = new JPanel(new GridBagLayout());
        titlePanel.setOpaque(false);

        titlePanel.add(title, gbc);

        panel.add(leftPanel, gbc);
        panel.add(titlePanel, gbc);
        panel.add(new JPanel(){{ //need to center title
            setOpaque(false);
        }});

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
         */
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(500,getHeight()));


        HiddenScroller scroller = new HiddenScroller(panel, false);
        scroller.setPreferredSize(new Dimension(120, getHeight()));
        return scroller;
    }

    private void showAboutPanel(){
        JFrame frame = new JFrame("About");
        frame.setSize(280, 520); //replicate macOS's about panel
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new FlowLayout());
        frame.getContentPane().setBackground(mainColor);
        frame.setResizable(false);

        JPanel contentPanel = new JPanel(new FlowLayout()){
            @Override
            protected void paintComponent(Graphics g){
                super.paintComponent(g);

                Graphics2D g2d = (Graphics2D) g;
                g2d.setPaint(new GradientPaint(0,0,mainColor,frame.getWidth(),frame.getHeight(),accentColor));
                g2d.fillRoundRect(0,0,frame.getWidth(),frame.getHeight(),0,0);
            }
        };
        contentPanel.setPreferredSize(new Dimension(frame.getWidth(), frame.getHeight()));

        JLabel label = new JLabel(new ImageIcon(renderIcon(150)));

        JLabel titleLabel = new JLabel("JJeopardy!");
        titleLabel.setFont(generateFont(50));
        titleLabel.setForeground(fontColor);

        contentPanel.add(label);
        contentPanel.add(titleLabel);

        frame.add(contentPanel);

        frame.setVisible(true);
    }

    private File createNewFile(String fileName){
        File file = new File(tempDir + "/" + fileName + ".json");
        System.out.println("test");

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

    private void quickTempSave(){
        for(MockJCard.TextBox qLabels : questionLabels){
            JsonFile.writeln3Keys(
                    fileName,
                    "board",
                    "col_" + qLabels.col,
                    qLabels.isQuestion ? "question_" + qLabels.row : "answer_" + qLabels.row,
                    qLabels.getText());

            System.out.println(qLabels.getText());
        }

        for(JTextField cats : catFields){
            //
        }

        System.out.println("Saved at: " + tempDir);
    }
    private void save(){
        //
    }

    private void load(String filePath){
        //
    }

    public void exit(){
        if(!changesMade) {
            changeCurrentPanel(mainMenu, this, false);
        }
    }

    private ActionListener onChangeListener(){
        return e -> changesMade = true;
    }

    private class MockBoardButton extends JButton {
        private final int arc;

        public MockBoardButton(int score, String question, String answer, int arc, int row, int col){
            this.arc = arc;

            setText(String.valueOf(score));

            addActionListener(e -> {
                inJCard = true;
                changeCurrentPanel(card = new MockJCard(question, answer, row, col), boardPanel, true, 200);
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Background
            g2d.setColor(getBackground());
            Shape backgroundShape = new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), arc,arc);
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

    private class MockJCard extends JPanel {
        String question;
        String answer;
        int row;
        int col;
        public MockJCard(String question, String answer, int row, int col){
            this.question = question;
            this.answer = answer;
            this.row = row;
            this.col = col;

            setLayout(new GridBagLayout());
            setBackground(mainColor);

            JPanel fieldPanels = new JPanel(new FlowLayout());
            fieldPanels.setPreferredSize(new Dimension(750,600));
            fieldPanels.setOpaque(false);

            fieldPanels.add(createGap(55, null));
            fieldPanels.add(createTextField(question, true, row, col));
            fieldPanels.add(createGap(80, null));
            fieldPanels.add(createTextField(answer, false, row, col));

            add(fieldPanels, gbc);
        }

        private JPanel createTextField(String text, boolean isQuestion, int row, int col){
            JPanel panel = new JPanel();

            panel.setOpaque(false);

            TextBox textField = new TextBox(text, isQuestion, row, col);

            questionLabels.add(textField);

            panel.add(textField);
            add(panel);
            return panel;
        }

        @Override
        protected void paintComponent(Graphics g){
            super.paintComponent(g);

            float[] dashPattern = {5, 5}; //Setting the length of dot and spacing of dot: {dot length, space width}
            Graphics2D g2 = (Graphics2D) g;
            //g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 100));
            g2.setColor(fontColor);
            g2.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 2, dashPattern, 0));

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
            float[] dashPattern = {5, 5}; //Setting the length of dot and spacing of dot: {dot length, space width}
            return new BasicStroke(width, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 2, dashPattern, 0);
        }

        private class TextBox extends JTextField {
            private final boolean isQuestion;
            private final int col;
            private final int row;

            public TextBox(String text, boolean isQuestion, int row, int col){
                super(text);
                this.isQuestion = isQuestion;
                this.col = col;
                this.row = row;

                setFont(generateFont(30));
                setCaretColor(fontColor);
                setForeground(fontColor);
                setBackground(accentColor);
                setHorizontalAlignment(JTextField.CENTER);
                setBorder(BorderFactory.createBevelBorder(1));
                setPreferredSize(new Dimension(700,128));

                getDocument().addDocumentListener(new DocumentListener() {
                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        quickTempSave();
                    }
                    @Override public void removeUpdate(DocumentEvent e) {}
                    @Override public void changedUpdate(DocumentEvent e) {}
                });
            }
        }
    }

    //just going to tuck this away
    private String getRandomWebsite(){
        String[] siteIndex = { //useless web mega list
                "https://sliding.toys/mystic-square/8-puzzle/daily/","https://longdogechallenge.com/","https://maze.toys/mazes/mini/daily/","https://optical.toys",
                "https://paint.toys/","https://puginarug.com","https://alwaysjudgeabookbyitscover.com","https://clicking.toys/flip-grid/neat-nine/3-holes/",
                "https://weirdorconfusing.com/","https://checkbox.toys/scale/","https://memory.toys/classic/easy/","https://binarypiano.com/","https://mondrianandme.com/",
                "https://onesquareminesweeper.com/","https://cursoreffects.com","https://floatingqrcode.com/","https://thatsthefinger.com/",
                "https://cant-not-tweet-this.com/","https://heeeeeeeey.com/","https://eelslap.com/","https://www.staggeringbeauty.com/","https://burymewithmymoney.com/",
                "https://smashthewalls.com/","https://jacksonpollock.org/","https://endless.horse/","https://drawing.garden/","https://www.trypap.com/",
                "https://www.movenowthinklater.com/","https://sliding.toys/klotski/easy-street/","https://paint.toys/calligram/","https://checkboxrace.com/",
                "https://www.rrrgggbbb.com/","https://www.koalastothemax.com/","https://rotatingsandwiches.com/","https://randomcolour.com/","https://maninthedark.com/",
                "https://cat-bounce.com/","https://chrismckenzie.com/","https://thezen.zone/","https://ihasabucket.com/","https://corndogoncorndog.com/",
                "https://www.hackertyper.com/","https://pointerpointer.com","https://imaninja.com/","https://www.nullingthevoid.com/","https://www.muchbetterthanthis.com/",
                "https://www.yesnoif.com/","https://lacquerlacquer.com","https://potatoortomato.com/","https://iamawesome.com/","https://strobe.cool/",
                "https://thisisnotajumpscare.com/","https://doughnutkitten.com/","https://crouton.net/","https://corgiorgy.com/","https://www.wutdafuk.com/",
                "https://unicodesnowmanforyou.com/","https://chillestmonkey.com/","https://www.crossdivisions.com/","https://boringboringboring.com/",
                "https://www.patience-is-a-virtue.org/","https://pixelsfighting.com/","https://isitwhite.com/","https://existentialcrisis.com/",
                "https://onemillionlols.com/","https://www.omfgdogs.com/","https://oct82.com/","https://chihuahuaspin.com/","https://www.blankwindows.com/",
                "https://tunnelsnakes.com/","https://www.trashloop.com/","https://spaceis.cool/","https://www.doublepressure.com/","https://www.donothingfor2minutes.com/",
                "https://buildshruggie.com/","https://wowenwilsonquiz.com","https://notdayoftheweek.com/","https://optical.toys/thatcher-effect/",
                "https://greatbignothing.com/","https://zoomquilt.org/","https://dadlaughbutton.com/","https://remoji.com/","https://papertoilet.com/",
                "https://loopedforinfinity.com/","https://end.city/","https://www.bouncingdvdlogo.com/","https://clicking.toys/peg-solitaire/english/","https://toms.toys"
        };
        return siteIndex[new Random().nextInt(siteIndex.length)];
    }
}
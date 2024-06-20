package com.boneless;

import com.boneless.util.JPopUp;
import com.boneless.util.JRoundedButton;
import com.boneless.util.JsonFile;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import static com.boneless.Main.*;
import static com.boneless.util.GeneralUtils.*;

public class BoardFactory extends JPanel {
    private final JFrame parent;
    private JPanel boardPanel;
    private MockJCard card;
    private JPopUp popUp;

    private Color mainColor;
    public Color accentColor;
    private Color fontColor;

    public boolean factoryIsActive;
    private boolean changesMade = false;
    private boolean inJCard = false;
    private boolean usingTempFile;

    private final int fontSize = 20;

    private String fileName;
    private String fileToSaveAt;

    private final HashMap<HashMap<Boolean, HashMap<Integer, Integer>>, QATextBox> labelList = new HashMap<>();
    private final ArrayList<JComponent> boardButtonList = new ArrayList<>();

    public BoardFactory(JFrame parent, String mainFile){
        factoryIsActive = true;
        this.parent = parent;

        if(mainFile != null){
            usingTempFile = false;
            fileName = mainFile;
            fileToSaveAt = mainFile;
        } else {
            usingTempFile = true;
            fileName = createTempFile("temp_board.json");
            fileToSaveAt = "File Not Set (Using Temporary Board File)";
        }
        mainMenu.changeFileName(fileName);
        setLayout(null);
        parent.setJMenuBar(menuBar());
        setBackground(mainColor);

        JPanel main = new JPanel(new BorderLayout());

        reload();
    }

    private void loadColors(){
        mainColor = parseColor(JsonFile.read(fileName, "data", "global_color"));
        accentColor = new Color(
                clamp(mainColor.getRed()   - 40),
                clamp(mainColor.getGreen() - 40),
                clamp(mainColor.getBlue()  - 40));
        fontColor = parseColor(JsonFile.read(fileName, "data", "font_color"));
    }

    private void reload(){
        loadColors();

        removeAll();
        boardButtonList.clear();
        labelList.clear();
        changesMade = false;
        inJCard = false;
        usingTempFile = false;

        add(popUp = new JPopUp(parent));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBounds(0,0,parent.getWidth(), parent.getHeight());

        JPanel boardHeaderPanel = new JPanel(new BorderLayout());
        boardHeaderPanel.setBackground(mainColor);

        boardHeaderPanel.add(headerPanel(), BorderLayout.NORTH);
        boardHeaderPanel.add(this.boardPanel = boardPanel(), BorderLayout.CENTER);

        mainPanel.add(boardHeaderPanel, BorderLayout.CENTER);
        mainPanel.add(controlPanel(), BorderLayout.EAST);
        add(mainPanel);

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
        newItem.addActionListener(e -> newFile());

        JMenuItem openItem = new JMenuItem("Open Board");
        openItem.addActionListener(e -> load());

        JMenuItem saveItem = new JMenuItem("Save Board");
        saveItem.addActionListener(e -> save());

        JMenuItem saveAsItem = new JMenuItem("Save Board At...");
        saveAsItem.addActionListener(e -> saveAs());

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> exit(false));

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
        fileMenu.add(saveAsItem);
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
                String question = JsonFile.readWithThreeKeys(fileName, "board", "col_" + j, "question_" + i);
                String answer = JsonFile.readWithThreeKeys(fileName, "board", "col_" + j, "answer_" + i);
                int score = Integer.parseInt(scoreString);

                putInLabelList(labelList, true, i, j, new QATextBox(answer, true, i, j));
                putInLabelList(labelList, false, i, j, new QATextBox(question, false, i, j));

                MockBoardButton button = new MockBoardButton(score, question, answer, 20, i, j);
                boardButtonList.add(button);

                button.setBackground(mainColor);
                button.setForeground(fontColor);
                button.setFont(generateFont(fontSize));

                panel.add(button);
            }
        }

        return panel;
    }

    private void putInLabelList(HashMap<HashMap<Boolean, HashMap<Integer, Integer>>, QATextBox> labelList, boolean isQuestion, int row, int col, QATextBox QATextBox){ //ass
        HashMap<Boolean, HashMap<Integer, Integer>> outerKey = new HashMap<>();
        HashMap<Integer, Integer> innerKey = new HashMap<>();
        innerKey.put(row, col);
        outerKey.put(isQuestion, innerKey);

        labelList.put(outerKey, QATextBox);
    }

    private QATextBox getFromLabelList(HashMap<HashMap<Boolean, HashMap<Integer, Integer>>, QATextBox> labelList, boolean isQuestion, int row, int col){
        for(HashMap<Boolean, HashMap<Integer, Integer>> outerKey : labelList.keySet()){
            if(outerKey.containsKey(isQuestion)){
                HashMap<Integer, Integer> innerKey = outerKey.get(isQuestion);
                if(innerKey.containsKey(row) && innerKey.get(row).equals(col)){
                    return labelList.get(outerKey);
                }
            }
        }
        return null;
    }

    private JPanel createCatPanel(int index) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createBevelBorder(0));
        panel.setBackground(accentColor);

        //setup field
        JTextField field = new JTextField(10);
        boardButtonList.add(field);

        field.setFont(generateFont(fontSize));
        field.setForeground(fontColor);
        field.setBackground(accentColor);
        field.setBorder(BorderFactory.createBevelBorder(1));
        field.setHorizontalAlignment(JTextField.CENTER);
        field.setText(JsonFile.readWithThreeKeys(fileName, "board", "categories", "cat_" + index));
        field.setCaretColor(fontColor);
        field.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                changesMade = true;
                JsonFile.writeln3Keys(fileName, "board","categories","cat_" + index, field.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changesMade = true;
                JsonFile.writeln3Keys(fileName, "board","categories","cat_" + index, field.getText());
            }
            @Override public void changedUpdate(DocumentEvent e) {}
        });

        panel.add(field, gbc);

        return panel;
    }

    private JPanel headerPanel(){
        JPanel panel = new JPanel(new GridLayout());
        panel.setBackground(accentColor);

        //left panel
        JLabel leftText = new JLabel("Exit");
        leftText.setForeground(fontColor);
        leftText.setFont(generateFont(fontSize));

        String rawKeyBind = JsonFile.read(settingsFile, "key_binds", "exit");
        String keyBind = rawKeyBind.substring(0, 1).toUpperCase() + rawKeyBind.substring(1);

        JButton headerExitButton = new JButton(keyBind);

        headerExitButton.setFocusable(false);
        headerExitButton.setFont(generateFont(20));
        headerExitButton.setForeground(Color.black);
        headerExitButton.addActionListener(e -> {
            if (inJCard) {
                inJCard = false;
                setButtonsEnabled(true);
                changeCurrentPanel(boardPanel, card, false);
            }
            else exit(false);
        });

        boardButtonList.add(headerExitButton);

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setOpaque(false);
        leftPanel.add(headerExitButton);
        leftPanel.add(leftText);

        //center panel
        JTextField title = new JTextField(20);
        title.setText(JsonFile.read(fileName, "data", "board_name"));
        title.setBackground(accentColor);
        title.setForeground(fontColor);
        title.setFont(generateFont(fontSize));
        title.setBorder(BorderFactory.createBevelBorder(1));
        title.setHorizontalAlignment(JTextField.CENTER);
        title.setCaretColor(fontColor);
        ((AbstractDocument) title.getDocument()).setDocumentFilter(new DocumentFilter() { //char limiter
            @Override
            public void insertString(FilterBypass fb, int offset, String text, AttributeSet attrs) throws BadLocationException {
                if(text == null){
                    return;
                }
                if((fb.getDocument().getLength() + text.length()) <= 30){
                    super.insertString(fb, offset, text, attrs);
                }
            }
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if(text == null){
                    return;
                }
                if((fb.getDocument().getLength() + text.length()) <= 30){
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });
        title.getDocument().addDocumentListener(new DocumentListener() { //save to temp
            @Override
            public void insertUpdate(DocumentEvent e) {
                JsonFile.writeln(fileName, "data","board_name",title.getText());
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                JsonFile.writeln(fileName, "data","board_name",title.getText());
            }
            @Override public void changedUpdate(DocumentEvent e) {}
        });

        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);

        titlePanel.add(title, gbc);

        panel.add(leftPanel, gbc);
        panel.add(titlePanel, gbc);
        panel.add(new JPanel(){{
            setOpaque(false);
        }});

        return panel;
    }

    private JPanel controlPanel(){
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBackground(accentColor);
        panel.setPreferredSize(new Dimension(360,getHeight()));

        //panel.add(sectionLabel("Board Settings", 30));

        NumberedTextBox catCountBox = new NumberedTextBox("categories");
        NumberedTextBox rowCountBox = new NumberedTextBox("rows");

        //panel.add(divider());
        panel.add(sectionLabel("Board Size"));
        panel.add(itemPanel("Categories", catCountBox));
        panel.add(itemPanel("Rows", rowCountBox));

        panel.add(divider());
        panel.add(sectionLabel("Colors & Text"));
        panel.add(colorPicker("Global Color", "global_color"));
        panel.add(colorPicker("Font Color","font_color"));
        panel.add(colorPicker("Disabled Color", "disabled_button_color"));

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

        String[] fonts = ge.getAvailableFontFamilyNames();

        JComboBox<String> comboBox = new JComboBox<>(fonts);
        if(fonts[0].contains(".")){ //removes macOS's wierd font '.AppleSystemUIFont'
            comboBox.removeItemAt(0);
        }

        panel.add(itemPanel("Font", comboBox));


        return panel;
    }

    private final GridBagConstraints leftGBC = new GridBagConstraints(){{
        gridx = 0;
        gridy = 0;
        weightx = 1;
        weighty = 1;
        anchor = GridBagConstraints.WEST;
        insets = new Insets(0,20,0,0);
    }};

    private final GridBagConstraints rightGBC = new GridBagConstraints(){{
        gridx = 0;
        gridy = 0;
        weightx = 1;
        weighty = 1;
        anchor = GridBagConstraints.EAST;
        insets = new Insets(0,0,0,20);
    }};

    private JPanel divider(){
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(250,25));
        panel.setOpaque(false);

        return panel;
    }

    private JPanel sectionLabel(String text, int... fontSize) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setPreferredSize(new Dimension(250, 35));
        panel.setOpaque(false);

        JLabel label = new JLabel(text);
        label.setFont(generateFont(fontSize.length > 0 ? fontSize[0] : 25));
        label.setForeground(fontColor);

        panel.add(label);

        return panel;
    }

    private JPanel colorPicker(String text, String key){
        int width = 350;
        int height = 40;
        JPanel panel = new JPanel(new GridLayout(1,2)){
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(Color.white);
                g2d.fillRoundRect(0, 0, width, height, 25,25);
            }
        };
        panel.setPreferredSize(new Dimension(width, height));

        JPanel textPanel = new JPanel(new GridBagLayout());
        textPanel.setOpaque(false);

        JLabel label = new JLabel(text);
        label.setFont(generateFont(20));

        textPanel.add(label, leftGBC);

        JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        itemPanel.setOpaque(false);

        NumberedTextBox redBox = new NumberedTextBox(0, key);
        NumberedTextBox greenBox = new NumberedTextBox(1, key);
        NumberedTextBox blueBox = new NumberedTextBox(2, key);

        JLabel comma = new JLabel(",");
        comma.setFont(generateFont(10));

        itemPanel.add(redBox);
        itemPanel.add(greenBox);
        itemPanel.add(blueBox);

        panel.add(textPanel);
        panel.add(itemPanel);

        return panel;
    }

    private JPanel itemPanel(String text, JComponent component, int... fontSize){
        int width = 350;
        int height = 40;
        JPanel panel = new JPanel(new GridLayout(1,2)){
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(Color.white);
                g2d.fillRoundRect(0, 0, width, height, 25,25);
            }
        };
        panel.setPreferredSize(new Dimension(width, height));

        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setOpaque(false);

        JLabel label = new JLabel(text);
        label.setFont(generateFont(fontSize.length > 0 ? fontSize[0] : 20));
        label.setForeground(Color.BLACK);

        leftPanel.add(label, leftGBC);

        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setOpaque(false);
        rightPanel.add(component, rightGBC);

        panel.add(leftPanel);
        panel.add(rightPanel);

        return panel;
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

    private String createTempFile(String file){
        String tempDir = System.getProperty("java.io.tmpdir");

        File tempFile = new File(tempDir + file);

        try {
            if(tempFile.exists()){
                try (FileWriter writer = new FileWriter(tempFile, false)){ //write to file
                    writer.write(jsonContent());
                }
                return tempFile.getAbsolutePath();
            } else {
                if(tempFile.createNewFile()){
                    return createTempFile(file);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return tempFile.getAbsolutePath();
    }

    private void newFile(){
        String file = saveAs(true);
        try(FileWriter writer = new FileWriter(file)){
            writer.write(jsonContent());
        } catch (IOException ignore){}

        load(file);
    }

    private void load(String... filePath){
        //todo: get file browser input, filename = selected file, reload()
        setButtonsEnabled(false);

        String filePathItem = null;
        if(filePath.length == 0){
            JFileChooser chooser = new JFileChooser();

            int returnVal = chooser.showOpenDialog(this);

            if(returnVal == JFileChooser.APPROVE_OPTION){
                filePathItem = chooser.getSelectedFile().getAbsolutePath();
            }
        } else {
            filePathItem = filePath[0];
        }
        fileToSaveAt = filePathItem;
        fileName = filePathItem;
        mainMenu.changeFileName(filePathItem);

        reload();
        setButtonsEnabled(true);
    }

    private void save(){
        if(usingTempFile) {
            usingTempFile = false;
            saveAs();
        } else {
            overrideFile(fileToSaveAt);
        }
    }

    private String saveAs(boolean... returnName){
        setButtonsEnabled(false);

        JFileChooser chooser = new JFileChooser();

        FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON files", "json");
        chooser.setFileFilter(filter);

        int userSelection = chooser.showSaveDialog(parent);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            try {
                fileToSaveAt = chooser.getSelectedFile().getAbsolutePath().contains(".json") ? chooser.getSelectedFile().getAbsolutePath() : chooser.getSelectedFile().getAbsolutePath() + ".json";
                if (new File(fileToSaveAt).createNewFile()) {
                    if(returnName.length > 0) return fileToSaveAt; //sloppy return method, out of time, don't care
                    overrideFile(fileToSaveAt);
                    setButtonsEnabled(true);
                } else {
                    JRoundedButton cancel = new JRoundedButton("Load");
                    cancel.addActionListener(e -> {
                        setButtonsEnabled(true);
                        popUp.hidePopUp();
                    });

                    JRoundedButton override = new JRoundedButton("Override (Will delete file!)");
                    override.addActionListener(e -> {
                        setButtonsEnabled(true);
                        overrideFile(fileToSaveAt);
                        popUp.hidePopUp();
                    });

                    popUp.showPopUp("File Conflict", "File already exists!",null, JPopUp.MESSAGE, cancel, override);
                }
            } catch (IOException ioException) {
                JOptionPane.showMessageDialog(parent, "Error creating file: " + ioException.getMessage());
            }
        } else {
            setButtonsEnabled(true);
        }
        return fileToSaveAt;
    }

    public void overrideFile(String saveDir) {
        try (FileReader reader = new FileReader(fileName);
             FileWriter writer = new FileWriter(saveDir, false)) {

            int c;
            while ((c = reader.read()) != -1) {
                writer.write(c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exit(boolean skipCheck){
        if(changesMade && !skipCheck) {
            setButtonsEnabled(false);

            JRoundedButton cancel = new JRoundedButton("Cancel");
            cancel.addActionListener(e -> {
                setButtonsEnabled(true);
                popUp.hidePopUp();
            });

            JRoundedButton exitWS = new JRoundedButton("Exit and Save");
            exitWS.addActionListener(e -> {
                popUp.hidePopUp();
                save();
                exit(false);
            });

            JRoundedButton exitWOS = new JRoundedButton("Exit Without Saving");
            exitWOS.addActionListener(e -> {
                popUp.hidePopUp();
                exit(true);
            });

            popUp.showPopUp("Changes Made!", "Changes have been made!",null, JPopUp.MESSAGE, cancel, exitWS, exitWOS);
        } else {
            factoryIsActive = false;
            mainMenu.timer.start();
            changeCurrentPanel(mainMenu, this, false);
            parent.setJMenuBar(null);
        }
    }

    private void setButtonsEnabled(boolean isEnabled){
        for(JComponent button : boardButtonList){
            button.setEnabled(isEnabled);
        }
    }

    private DocumentListener documentListener(){
        return new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                changesMade = true;
            }
            @Override public void removeUpdate(DocumentEvent e) {}
            @Override public void changedUpdate(DocumentEvent e) {}
        };
    }

    private class MockBoardButton extends JButton {
        private final int arc;

        public MockBoardButton(int score, String question, String answer, int arc, int row, int col){
            this.arc = arc;

            setText(String.valueOf(score));

            addActionListener(e -> {
                inJCard = true;
                setButtonsEnabled(false);

                Timer timer = new Timer(1000, a -> { //stop user from spamming the button, fixes bug where it would go from jCard to MainMenu, instead of jCard to GameBoard
                    boardButtonList.get(0).setEnabled(true); //ensure header exit button is enabled
                });
                timer.start();
                timer.setRepeats(false);

                changeCurrentPanel(card = new MockJCard(row, col), boardPanel, true, 200);
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            //super.paintComponent(g);
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
        public MockJCard(int row, int col){
            setLayout(new GridBagLayout());
            setBackground(mainColor);

            JPanel fieldPanels = new JPanel(new FlowLayout());
            fieldPanels.setPreferredSize(new Dimension(frameWidth - frameWidth / 5,600));
            fieldPanels.setOpaque(false);

            fieldPanels.add(createGap(55, null));
            fieldPanels.add(getFromLabelList(labelList, true, row, col));
            fieldPanels.add(createGap(205, null));
            fieldPanels.add(getFromLabelList(labelList, false, row, col));

            add(fieldPanels, gbc);
        }

        @Override
        protected void paintComponent(Graphics g){
            super.paintComponent(g);

            //stolen from dante >:)
            float[] dashPattern = {5, 5}; //Setting the length of dot and spacing of dot: {dot length, space width}
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(fontColor);
            g2.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 2, dashPattern, 0));

            g2.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2);

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        }
    }

    private class QATextBox extends JTextField {
        public int row;
        public int col;
        public boolean isQuestion;

        public QATextBox(String text, boolean isQuestion, int row, int col){
            super(JsonFile.readWithThreeKeys(fileName, "board", "col_" + col, isQuestion ? "question_" + row : "answer_" + row));

            this.row = row;
            this.col = col;
            this.isQuestion = isQuestion;

            setFont(generateFont(30));
            setCaretColor(fontColor);
            setForeground(fontColor);
            setBackground(accentColor);
            setHorizontalAlignment(JTextField.CENTER);
            setBorder(BorderFactory.createBevelBorder(1));
            setPreferredSize(new Dimension(1500,128));

            getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    changesMade = true;
                    JsonFile.writeln3Keys(fileName, "board", "col_" + col, isQuestion ? "question_" + row : "answer_" + row, getText());
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    changesMade = true;
                    JsonFile.writeln3Keys(fileName, "board", "col_" + col, isQuestion ? "question_" + row : "answer_" + row, getText());
                }
                @Override public void changedUpdate(DocumentEvent e) {}
            });
        }

        @Override
        protected void paintBorder(Graphics g) {
            super.paintBorder(g);
        }
    }

    private class NumberedTextBox extends JTextField {
        public NumberedTextBox(String key){
            super(2);
            setText(JsonFile.read(fileName, "data", key));

            setBackground(Color.LIGHT_GRAY);
            setHorizontalAlignment(JTextField.CENTER);

            setFont(generateFont(20));
            setBorder(BorderFactory.createBevelBorder(1));

            ((AbstractDocument) getDocument()).setDocumentFilter(new NumberFilter());
            getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    changesMade = true;
                    JsonFile.writeln(fileName, "data", key, getText());
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    changesMade = true;
                    JsonFile.writeln(fileName, "data", key, getText());
                }
                @Override public void changedUpdate(DocumentEvent e) {}
            });
        }
        public NumberedTextBox(int pos, String key){
            super(3);
            String rgbValue = JsonFile.read(fileName, "data", key);
            setText(rgbValue.split(",")[pos]);

            setBackground(Color.LIGHT_GRAY);
            setHorizontalAlignment(JTextField.CENTER);

            setFont(generateFont(18));
            setBorder(BorderFactory.createBevelBorder(1));

            ((AbstractDocument) getDocument()).setDocumentFilter(new DocumentFilter(){ //length limiter
                private AttributeSet attributeSet = null;
                @Override
                public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr) throws BadLocationException {
                    attributeSet = attr;
                    if (text == null) {
                        return;
                    }
                    if (isNumeric(text) && (fb.getDocument().getLength() + text.length()) <= 3) {
                        super.insertString(fb, offset, text, attr);
                        loadColors();
                    }
                }

                @Override
                public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                    if (text == null) {
                        return;
                    }
                    if (isNumeric(text) && (fb.getDocument().getLength() + text.length()) <= 3) {
                        super.replace(fb, offset, length, text, attrs);
                        loadColors();
                    }
                }

                @Override
                public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
                    System.out.println("String Length: " + fb.getDocument().getLength());
                    if(fb.getDocument().getLength() - 1 > 0) {
                        super.remove(fb, offset, length);
                    } else {
                        replace(fb, offset, length, "0", attributeSet);
                    }
                }

                private boolean isNumeric(String text) {
                    for (char c : text.toCharArray()) {
                        System.out.println("Char: " + c);
                        if (!Character.isDigit(c)) {
                            return false;
                        }
                    }
                    return true;
                }
            });
            getDocument().addDocumentListener(new DocumentListener() { //rgb clamp
                @Override
                public void insertUpdate(DocumentEvent e) {
                    changesMade = true;
                    if(Integer.parseInt(getText()) > 0 && Integer.parseInt(getText()) < 256){
                        String[] rgb = rgbValue.split(",");
                        String data = switch (pos) {
                            case 0 -> getText() + "," + rgb[1] + "," + rgb[2];
                            case 1 -> rgb[0] + "," + getText() + "," + rgb[2];
                            case 2 -> rgb[0] + "," + rgb[1] + "," + getText();
                            default -> rgbValue;
                        };
                        JsonFile.writeln(fileName, "data", key, data);
                        reload();
                    }
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    changesMade = true;
                    if(Integer.parseInt(getText()) > 0 && Integer.parseInt(getText()) < 256){
                        String[] rgb = rgbValue.split(",");
                        try {
                            String data = switch (pos) {
                                case 0 -> getText() + "," + rgb[1] + "," + rgb[2];
                                case 1 -> rgb[0] + "," + getText() + "," + rgb[2];
                                case 2 -> rgb[0] + "," + rgb[1] + "," + getText();
                                default -> rgbValue;
                            };
                            JsonFile.writeln(fileName, "data", key, data);
                            reload();
                        } catch (NumberFormatException exception) {
                            setText("0");
                        }
                    }
                }
                @Override public void changedUpdate(DocumentEvent e) {}
            });
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
    private String jsonContent(){
        return """
                {
                  "data" : {
                    "board_name": "Template",
                    "categories": 5,
                    "rows": 5,
                    "font": "Arial",
                    "font_color": "255,255,255",
                    "global_color": "20,20,255",
                    "disabled_button_color": "80,80,80"
                  },
                  "board": {
                    "categories": {
                      "cat_0": "Test 1",
                      "cat_1": "Test 2",
                      "cat_2": "Test 3",
                      "cat_3": "Test 4",
                      "cat_4": "Test 5"
                    },
                    "scores": {
                      "row_0": "100",
                      "row_1": "200",
                      "row_2": "300",
                      "row_3": "400",
                      "row_4": "500"
                    },
                    "col_0": {
                      "question_0": "Column 0 Question 0",
                      "question_1": "Column 0 Question 1",
                      "question_2": "Column 0 Question 2",
                      "question_3": "Column 0 Question 3",
                      "question_4": "Column 0 Question 4",
                      "answer_0": "Column 0 Answer 0",
                      "answer_1": "Column 0 Answer 1",
                      "answer_2": "Column 0 Answer 2",
                      "answer_3": "Column 0 Answer 3",
                      "answer_4": "Column 0 Answer 4"
                    },
                    "col_1": {
                      "question_0": "Column 1 Question 0",
                      "question_1": "Column 1 Question 1",
                      "question_2": "Column 1 Question 2",
                      "question_3": "Column 1 Question 3",
                      "question_4": "Column 1 Question 4",
                      "answer_0": "Column 1 Answer 0",
                      "answer_1": "Column 1 Answer 1",
                      "answer_2": "Column 1 Answer 2",
                      "answer_3": "Column 1 Answer 3",
                      "answer_4": "Column 1 Answer 4"
                    },
                    "col_2": {
                      "question_0": "Column 2 Question 0",
                      "question_1": "Column 2 Question 1",
                      "question_2": "Column 2 Question 2",
                      "question_3": "Column 2 Question 3",
                      "question_4": "Column 2 Question 4",
                      "answer_0": "Column 2 Answer 0",
                      "answer_1": "Column 2 Answer 1",
                      "answer_2": "Column 2 Answer 2",
                      "answer_3": "Column 2 Answer 3",
                      "answer_4": "Column 2 Answer 4"
                    },
                    "col_3": {
                      "question_0": "Column 3 Question 0",
                      "question_1": "Column 3 Question 1",
                      "question_2": "Column 3 Question 2",
                      "question_3": "Column 3 Question 3",
                      "question_4": "Column 3 Question 4",
                      "answer_0": "Column 3 Answer 0",
                      "answer_1": "Column 3 Answer 1",
                      "answer_2": "Column 3 Answer 2",
                      "answer_3": "Column 3 Answer 3",
                      "answer_4": "Column 3 Answer 4"
                    },
                    "col_4": {
                      "question_0": "Column 4 Question 0",
                      "question_1": "Column 4 Question 1",
                      "question_2": "Column 4 Question 2",
                      "question_3": "Column 4 Question 3",
                      "question_4": "Column 4 Question 4",
                      "answer_0": "Column 4 Answer 0",
                      "answer_1": "Column 4 Answer 1",
                      "answer_2": "Column 4 Answer 2",
                      "answer_3": "Column 4 Answer 3",
                      "answer_4": "Column 4 Answer 4"
                    }
                  }
                }
                """;
    }
}
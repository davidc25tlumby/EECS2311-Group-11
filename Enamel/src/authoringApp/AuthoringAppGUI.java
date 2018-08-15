package authoringApp;

import java.awt.Component;
import java.awt.Container;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;

/**
 * The GUI of the authoring application.
 * 
 * @author Xiahan Chen, Huy Hoang Minh Cu, Qasim Mahir
 */
public class AuthoringAppGUI extends javax.swing.JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 103899240155095320L;

	/**
	 * Initializes the JFrame. Calls methods to initialize components and create
	 * an Array of components within this JFrame.
	 */
	public AuthoringAppGUI() {
		initComponents();
		
		BufferedImage img = null;
		try{
			img = ImageIO.read(new File("Images/sound.png"));
		} catch (IOException e){
			e.printStackTrace();
		}
		
		Image scaledImg = img.getScaledInstance(14, 14, Image.SCALE_SMOOTH);
		playImg = new ImageIcon(scaledImg);
		
		try{
			img = ImageIO.read(new File("Images/soundDisabled.png"));
		} catch (IOException e){
			e.printStackTrace();
		}
	
		
		scaledImg = img.getScaledInstance(14, 14, Image.SCALE_SMOOTH);
		playDisabledImg = new ImageIcon(scaledImg);
		
		recordButton.setIcon(recordDisabledImg);
		blankLabel.setIcon(blankImg);
		playButton.setIcon(playDisabledImg);
	}

	/**
	 * Initializes all the components within this JFrame.
	 */
	private void initComponents() {
		
        //recordButton = new recordButton("resources/mic14.png");
        //stopButton = new recordButton("resources/stop.png");
		
		recordButton = new javax.swing.JLabel();
		blankLabel = new javax.swing.JLabel();
		playButton = new javax.swing.JLabel();

        scenarioScrollPane = new javax.swing.JScrollPane();
        scenarioPane = new javax.swing.JTextPane();
        inputTextField = new javax.swing.JTextField();
        editRemoveLine = new javax.swing.JButton();
        insertText = new javax.swing.JButton();
        insertSkipButton = new javax.swing.JButton();
        insertRepeatButton = new javax.swing.JButton();
        insertRepeat = new javax.swing.JButton();
        insertUserInput = new javax.swing.JButton();
        insertResetButtons = new javax.swing.JButton();
        insertEndRepeat = new javax.swing.JButton();
        insertSound = new javax.swing.JButton();
        insertLabel = new javax.swing.JLabel();
        insertPause = new javax.swing.JButton();
        displayComboBox = new javax.swing.JComboBox<>();
        displayAddButton = new javax.swing.JButton();
        insertSkip = new javax.swing.JButton();
        consoleScrollPane = new javax.swing.JScrollPane();
        consoleTextPane = new javax.swing.JTextPane();
        editLabel = new javax.swing.JLabel();
        authoringAppMenuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        newMenuItem = new javax.swing.JMenuItem();
        loadScenarioMenuItem = new javax.swing.JMenuItem();
        fileMenuSeperator1 = new javax.swing.JPopupMenu.Separator();
        saveMenuItem = new javax.swing.JMenuItem();
        saveAsMenuItem = new javax.swing.JMenuItem();
        fileMenuSeperator2 = new javax.swing.JPopupMenu.Separator();
        exitMenuItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        undoMenuItem = new javax.swing.JMenuItem();
        redoMenuItem = new javax.swing.JMenuItem();
        editMenuSeperator1 = new javax.swing.JPopupMenu.Separator();
        cutMenuItem = new javax.swing.JMenuItem();
        copyMenuItem = new javax.swing.JMenuItem();
        pasteMenuItem = new javax.swing.JMenuItem();
        runMenu = new javax.swing.JMenu();
        runMenuItem = new javax.swing.JMenuItem();
        loadAndRunMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        userManualMenuItem = new javax.swing.JMenuItem();
        helpMenuSeperator1 = new javax.swing.JPopupMenu.Separator();
        aboutMenuItem = new javax.swing.JMenuItem();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Authoring App");
        setSize(new java.awt.Dimension(1500, 1000));


        scenarioPane.setEditable(false);
        scenarioPane.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        scenarioScrollPane.setViewportView(scenarioPane);
        scenarioScrollPane.setAutoscrolls(true);
        
        scenarioScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

        inputTextField.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        inputTextField.setForeground(new java.awt.Color(204, 204, 204));
        inputTextField.setText("(Insert text/argument)");

        editRemoveLine.setText("Remove Line (Del)");
        editRemoveLine.setToolTipText("");
        editRemoveLine.setEnabled(false);

        insertText.setText("Text (Enter)");
        insertText.setEnabled(false);

        insertSkipButton.setText("/~skip-button:int string");
        insertSkipButton.setEnabled(false);

        insertRepeatButton.setText("/~repeat-button:int");
        insertRepeatButton.setEnabled(false);

        insertRepeat.setText("/~repeat");
        insertRepeat.setEnabled(false);

        insertUserInput.setText("/~user-input");
        insertUserInput.setEnabled(false);

        insertResetButtons.setText("/~reset-buttons");
        insertResetButtons.setEnabled(false);

        insertEndRepeat.setText("/~endrepeat");
        insertEndRepeat.setEnabled(false);

        insertSound.setText("/~sound:filename.ext");
        insertSound.setEnabled(false);

        insertLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        insertLabel.setText("Insert:");

        insertPause.setText("/~pause:int");
        insertPause.setEnabled(false);

        displayComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "/~disp-string:string", "/~disp-clearAll", "/~disp-clear-cell:int", "/~disp-cell-pins:int string", "/~disp-cell-char:int char", "/~disp-cell-raise:int1 int2", "/~disp-cell-lower:int1 int2" }));
        displayComboBox.setToolTipText("");
        displayComboBox.setEnabled(false);
        displayComboBox.setLightWeightPopupEnabled(false);

        displayAddButton.setText("+");
        displayAddButton.setEnabled(false);

        insertSkip.setText("/~skipString");
        insertSkip.setEnabled(false);

        consoleTextPane.setEditable(false);
        consoleTextPane.setName("consolePane");
        consoleScrollPane.setViewportView(consoleTextPane);
        consoleScrollPane.setName("consoleScrollPane");

        editLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        editLabel.setText("Edit:");


        fileMenu.setText("File");

        newMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        newMenuItem.setText("New");
        fileMenu.add(newMenuItem);

        loadScenarioMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        loadScenarioMenuItem.setText("Load scenario");
        fileMenu.add(loadScenarioMenuItem);

        fileMenu.add(fileMenuSeperator1);

        saveMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        saveMenuItem.setText("Save");
        saveMenuItem.setToolTipText("");
        saveMenuItem.setEnabled(false);
        fileMenu.add(saveMenuItem);

        saveAsMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        saveAsMenuItem.setText("Save as..");
        saveAsMenuItem.setEnabled(false);
        fileMenu.add(saveAsMenuItem);

        fileMenu.add(fileMenuSeperator2);

        exitMenuItem.setText("Exit");
        fileMenu.add(exitMenuItem);

        authoringAppMenuBar.add(fileMenu);

        editMenu.setText("Edit");

        undoMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_MASK));
        undoMenuItem.setText("Undo");
        undoMenuItem.setEnabled(false);
        editMenu.add(undoMenuItem);

        redoMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Y, java.awt.event.InputEvent.CTRL_MASK));
        redoMenuItem.setText("Redo");
        redoMenuItem.setEnabled(false);
        editMenu.add(redoMenuItem);

        editMenu.add(editMenuSeperator1);

        cutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        cutMenuItem.setText("Cut");
        cutMenuItem.setEnabled(false);
        editMenu.add(cutMenuItem);

        copyMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        copyMenuItem.setText("Copy");
        copyMenuItem.setEnabled(false);
        editMenu.add(copyMenuItem);

        pasteMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_MASK));
        pasteMenuItem.setText("Paste");
        pasteMenuItem.setEnabled(false);
        editMenu.add(pasteMenuItem);

        authoringAppMenuBar.add(editMenu);

        runMenu.setText("Run");

        runMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F11, 0));
        runMenuItem.setText("Run");
        runMenuItem.setEnabled(false);
        runMenu.add(runMenuItem);

        loadAndRunMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F11, java.awt.event.InputEvent.CTRL_MASK));
        loadAndRunMenuItem.setText("Load & run");
        loadAndRunMenuItem.setToolTipText("");
        runMenu.add(loadAndRunMenuItem);

        authoringAppMenuBar.add(runMenu);

        helpMenu.setText("Help");

        userManualMenuItem.setText("User Manual");
        helpMenu.add(userManualMenuItem);

        helpMenu.add(helpMenuSeperator1);

        aboutMenuItem.setText("About");
        helpMenu.add(aboutMenuItem);

        authoringAppMenuBar.add(helpMenu);
        
        recordButton.setName("recordButton");
        authoringAppMenuBar.add(recordButton);
        
        authoringAppMenuBar.add(blankLabel);
        
        playButton.setName("playButton");
        authoringAppMenuBar.add(playButton);

        
        jButton1.setText("use this button for testing");

        setJMenuBar(authoringAppMenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(consoleScrollPane, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(inputTextField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 899, Short.MAX_VALUE)
                    .addComponent(scenarioScrollPane))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addComponent(displayComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(displayAddButton))
                        .addComponent(insertRepeat, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(insertSkipButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(insertEndRepeat, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(insertSkip, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(insertUserInput, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(insertText, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(insertPause, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(insertRepeatButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(insertResetButtons, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(insertSound, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(insertLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(editLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(editRemoveLine, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(scenarioScrollPane)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(consoleScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(inputTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(insertLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(insertText, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(insertPause, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(insertSkipButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(insertSkip, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(insertUserInput, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(insertRepeatButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(insertRepeat, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(insertEndRepeat, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(insertResetButtons, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(insertSound, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(displayAddButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(displayComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(editLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(editRemoveLine, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 32, Short.MAX_VALUE))))
        );

        pack();
	}

	/**
	 * Sets the style of the GUI and calls the constructor.
	 * 
	 * @param args
	 *            unused
	 */
	public static void main(String args[]) {

		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new AuthoringApp().setVisible(true);
			}
		});
	}
    //recordButton recordButton; 
    //recordButton stopButton; 
    
    JLabel recordButton;
    JLabel playButton;
    JLabel blankLabel;
    ImageIcon recordImg = new ImageIcon("Images/mic14.png"), stopImg = new ImageIcon("Images/stop.png"), recordDisabledImg = new ImageIcon("Images/micDisabled14.png"), blankImg = new ImageIcon("Images/blank14.png");
    ImageIcon playImg = new ImageIcon("Images/sound512.png"), playDisabledImg = new ImageIcon("Images/soundDisabled512.png");
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JMenuBar authoringAppMenuBar;
    protected javax.swing.JScrollPane consoleScrollPane;
    protected javax.swing.JTextPane consoleTextPane;
    protected javax.swing.JMenuItem copyMenuItem;
    protected javax.swing.JMenuItem cutMenuItem;
    protected javax.swing.JButton displayAddButton;
    javax.swing.JComboBox<String> displayComboBox;
    private javax.swing.JLabel editLabel;
    private javax.swing.JMenu editMenu;
    private javax.swing.JPopupMenu.Separator editMenuSeperator1;
    protected javax.swing.JButton editRemoveLine;
    protected javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JPopupMenu.Separator fileMenuSeperator1;
    private javax.swing.JPopupMenu.Separator fileMenuSeperator2;
    private javax.swing.JMenuItem userManualMenuItem;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JPopupMenu.Separator helpMenuSeperator1;
    protected javax.swing.JTextField inputTextField;
    protected javax.swing.JButton insertEndRepeat;
    private javax.swing.JLabel insertLabel;
    protected javax.swing.JButton insertPause;
    protected javax.swing.JButton insertRepeat;
    protected javax.swing.JButton insertRepeatButton;
    protected javax.swing.JButton insertResetButtons;
    protected javax.swing.JButton insertSkip;
    protected javax.swing.JButton insertSkipButton;
    protected javax.swing.JButton insertSound;
    protected javax.swing.JButton insertText;
    protected javax.swing.JButton insertUserInput;
    protected javax.swing.JMenuItem loadAndRunMenuItem;
    protected javax.swing.JMenuItem loadScenarioMenuItem;
    protected javax.swing.JMenuItem newMenuItem;
    protected javax.swing.JMenuItem pasteMenuItem;
    protected javax.swing.JMenuItem redoMenuItem;
    private javax.swing.JMenu runMenu;
    protected javax.swing.JMenuItem runMenuItem;
    protected javax.swing.JMenuItem saveAsMenuItem;
    protected javax.swing.JMenuItem saveMenuItem;
    protected javax.swing.JTextPane scenarioPane;
    javax.swing.JScrollPane scenarioScrollPane;
    protected javax.swing.JMenuItem undoMenuItem;
    protected javax.swing.JButton jButton1;
}

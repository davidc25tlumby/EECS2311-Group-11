package authoringApp;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.util.*;
import javax.swing.*;
import org.apache.commons.io.FilenameUtils;
import enamel.ToyAuthoring;

/**
 * An application to create scenario files that are compatible with a Treasure Braille Box simulation.
 * @author Xiahan Chen, Huy Hoang Minh Cu, Qasim Mahir
 */
public class AuthoringApp {

	private static JFrame gui;
	private static JFileChooser fc = new JFileChooser();
	private static File f, currentFile, error;
	private static LinkedList<String> fileStr, consoleStr;
	private static LinkedList<Integer> id;
	private static JPanel errorPanel;
	private static HashMap<String, Component> compMap;
	private static JTextPaneController controller, consoleController;
	
	private static int currentLine = 2, cell = 0, col = 0;
	private static boolean isSaved = true, isOpened = false;
	private static String currentID;


	/**
	 * Initializes the application by drawing the GUI and initializing a controller for the JTextPane. LinkedLists are created for an id+string pair that represents elements on the JTextPane.
	 * @param args unused
	 */
	public static void main(String[] args) {
		
		java.awt.EventQueue.invokeLater(new Runnable() {
			
			public void run() {
				
				gui = new AuthoringAppGUI();
				gui.setVisible(true);
				compMap = ((AuthoringAppGUI) gui).getCompMap();
				fileStr = new LinkedList<String>();
				id = new LinkedList<Integer>();
				id.add(0);
				controller = new JTextPaneController((JTextPane) compMap.get("scenarioPane"), (JScrollPane) compMap.get("scenarioScrollPane"));
				consoleController = new JTextPaneController((JTextPane) compMap.get("consolePane"), (JScrollPane) compMap.get("consoleScrollPane"));
				addActionListeners();
				addEditorButtons();
			}
			
		});
		
	}

	protected static void addEditorButtons() {
		// TODO Auto-generated method stub
		((JButton) compMap.get("insertText")).addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				String temp = getInputText();
				int v = validString(temp);
				
				if (v == 0){
					nullArgumentException();
				}
				else if (v == 1){
					illegalArgumentException("string");
				}
				else if (v == 2){
					id.add(id.getLast() + 1);
					fileStr.add(temp);
					controller.addElement(temp, currentLine - 1);
				}
			}

		});
		
		
		
		((JButton) compMap.get("insertPause")).addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String temp = getInputText();
				String[] s = temp.split("\\s+");
				int n;
				int v = validString(temp);
				
				if (v == 0) {
					nullArgumentException();
				}
				else if (v == 1){
					illegalArgumentException("int");
				}
				else if (v == 2){
					if (s.length != 1){
						illegalArgumentException("int");
					}
					else{
						if (isNumeric(temp)){
							n = Integer.parseInt(temp);
							if (n >= 0){
								id.add(id.getLast()+1);
								fileStr.add("/~pause:" + n);
								controller.addElement("/~pause:" + n, id.getLast());
							}
							else {
								illegalArgumentException("int");
							}
						}
						else{
							illegalArgumentException("int");
						}
					}
				}

			}

		});
		
		((JButton) compMap.get("insertSkipButton")).addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				String temp = getInputText();
				String[] s = temp.split("\\s+");
				int n;
				int v = validString(temp);
				
				if (v == 0) {
					nullArgumentException();
				}
				else if (v == 1){
					illegalArgumentException("int string");
				}
				else if (v == 2){
					if (s.length != 2){
						illegalArgumentException("int string");
					}
					else{
						if (isNumeric(s[0]) && !isNumeric(s[1])){
							n = Integer.parseInt(s[0]);
							if (n >= 0 && n < col){
								id.add(id.getLast() + 1);
								fileStr.add("/~skip-button:" + temp);
								controller.addElement("/~skip-button:" + temp, id.getLast());
							}
							else {
								indexOutOfBoundsException("Button");
							}
						}
						else{
							illegalArgumentException("int string");
						}
					}
				}
			}
			
		});
		
		((JButton) compMap.get("insertSkip")).addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				String temp = getInputText();
				String[] s = temp.split("\\s+");
				int v = validString(temp);
				
				if (v == 0) {
					nullArgumentException();
				}
				else if (v == 1){
					illegalArgumentException("string");
				}
				else if (v == 2){
					if (s.length != 1){
						illegalArgumentException("string");
					}
					else{
						if (!isNumeric(temp)){
							id.add(id.getLast() + 1);
							fileStr.add("/~" + temp);
							controller.addElement("/~" + temp, id.getLast());
						}
						else{
							illegalArgumentException("string");
						}
					}
				}
			}
			
		});
		
		((JButton) compMap.get("insertUserInput")).addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				id.add(id.getLast() + 1);
				fileStr.add("/~user-input");
				controller.addElement("/~user-input", id.getLast());
			}
			
		});
		
		((JButton) compMap.get("insertRepeatButton")).addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				String temp = getInputText();
				String[] s = temp.split("\\s+");
				int n;
				int v = validString(temp);
				
				if (v == 0) {
					nullArgumentException();
				}
				else if (v == 1){
					illegalArgumentException("int");
				}
				else if (v == 2){
					if (s.length != 1){
						illegalArgumentException("int");
					}
					else{
						if (isNumeric(temp)){
							n = Integer.parseInt(temp);
							if (n >= 0 && n < col){
								id.add(id.getLast()+1);
								fileStr.add("/~pause:" + n);
								controller.addElement("/~repeat-button:" + n, id.getLast());
							}
							else {
								indexOutOfBoundsException("Button");
							}
						}
						else{
							illegalArgumentException("int");
						}
					}
				}
			}
			
		});
		
		((JButton) compMap.get("insertRepeat")).addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				id.add(id.getLast()+1);
				fileStr.add("/~repeat");
				controller.addElement("/~repeat", id.getLast());
			}

		});

		((JButton) compMap.get("insertEndRepeat")).addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				id.add(id.getLast()+1);
				fileStr.add("/~endrepeat");
				controller.addElement("/~endrepeat", id.getLast());
			}

		});
		
		((JButton) compMap.get("insertResetButtons")).addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				id.add(id.getLast()+1);
				fileStr.add("/~reset-buttons");
				controller.addElement("/~reset-buttons", id.getLast());		
			}
		}); 
		
		((JButton) compMap.get("insertSound")).addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				f = openFileChooser(new File("FactoryScenarios/AudioFiles"), "wav");
				if (f != null)
				{
					fileStr.add("/~sound:"+f.getName());
					id.add(id.getLast()+1);
					controller.addElement("/~sound:"+f.getName(), id.getLast());
				}
			}
		});
		
		((JButton) compMap.get("displayAddButton")).addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("rawtypes")
				JComboBox cb = (JComboBox) compMap.get("displayComboBox");
				String cbStr = (String) cb.getItemAt(cb.getSelectedIndex());
				String temp = getInputText();
				String[] s = temp.split("\\s+");
				int n, m;
				int v = validString(temp);

				if (cbStr.equals("/~disp-string:string")) {
					if (v == 0) {
						nullArgumentException();
					}
					else if (v == 1){
						illegalArgumentException("string");
					}
					else if (v == 2){
						if (temp.length() > cell){
							illegalArgumentException("string");
						}
						else{
							id.add(id.getLast()+1);
							fileStr.add("/~disp-string:" + temp);
							controller.addElement("/~disp-string:" + temp, id.getLast());
						}
					}
				}
				else if (cbStr.equals("/~disp-clearAll")) {
					fileStr.add("/~disp-clearAll");
					id.add(id.getLast() + 1);
					controller.addElement("/~disp-clearAll", id.getLast());
				}		
				else if (cbStr.equals("/~disp-clear-cell:int")) {
					if (v == 0) {
						nullArgumentException();
					}
					else if (v == 1){
						illegalArgumentException("int");
					}
					else if (v == 2){
						if (s.length != 1){
							illegalArgumentException("int");
						}
						else{
							if (isNumeric(temp)){
								n = Integer.parseInt(temp);
								if (n >= 0 && n < cell){
									id.add(id.getLast()+1);
									fileStr.add("/~disp-clear-cell:" + temp);
									controller.addElement("/~disp-clear-cell:" + temp, id.getLast());
								}
								else{
									indexOutOfBoundsException("Cell");
								}
							}
							else{
								illegalArgumentException("int");
							}
						}
					}
				}			
				else if (cbStr.equals("/~disp-cell-pins:int string")) {
					if (v == 0) {
						nullArgumentException();
					}
					else if (v == 1){
						illegalArgumentException("int string");
					}
					else if (v == 2){
						if (s.length != 2){
							illegalArgumentException("int string");
						}
						else{
							if (isNumeric(s[0]) && isBinaryChar(s[1])){
								n = Integer.parseInt(s[0]);
								if (n >= 0 && n < cell){
									id.add(id.getLast() + 1);
									fileStr.add("/~disp-cell-pins:" + temp);
									controller.addElement("/~disp-cell-pins:" + temp, id.getLast());
								}
								else{
									indexOutOfBoundsException("Cell");
								}
							}
							else{
								illegalArgumentException("int string");
							}
						}
					}
				}			
				else if (cbStr.equals("/~disp-cell-char:int char")) {
					if (v == 0) {
						nullArgumentException();
					}
					else if (v == 1){
						illegalArgumentException("int char");
					}
					else if (v == 2){
						if (s.length != 2){
							illegalArgumentException("int char");
						}
						else{
							if (isNumeric(s[0]) && isChar(s[1])){
								n = Integer.parseInt(s[0]);
								if (n >= 0 && n < cell){
									id.add(id.getLast() + 1);
									fileStr.add("/~disp-cell-char:" + temp);
									controller.addElement("/~disp-cell-char:" + temp, id.getLast());
								}
								else{
									indexOutOfBoundsException("Cell");
								}
							}
							else{
								illegalArgumentException("int char");
							}
						}
					}
				}
				else if (cbStr.equals("/~disp-cell-raise:int1 int2")) {
					if (v == 0) {
						nullArgumentException();
					}
					else if (v == 1){
						illegalArgumentException("int1 int2");
					}
					else if (v == 2){
						if (s.length != 2){
							illegalArgumentException("int1 int2");
						}
						else{
							if (isNumeric(s[0]) && isNumeric(s[1]) && s[0].length() == 1 && s[1].length() == 1){
								n = Integer.parseInt(s[0]);
								m = Integer.parseInt(s[1]);
								if (n >= 0 && n < cell && m >= 0 && m < 8){
									id.add(id.getLast() + 1);
									fileStr.add("/~disp-cell-raise:" + temp);
									controller.addElement("/~disp-cell-raise:" + temp, id.getLast());
								}
								else{
									indexOutOfBoundsException("Cell");
								}
							}
							else{
								illegalArgumentException("int1 int2");
							}
						}
					}
				}
				
				if (cbStr.equals("/~disp-cell-lower:int1 int2")) {
					if (v == 0) {
						nullArgumentException();
					}
					else if (v == 1){
						illegalArgumentException("int1 int2");
					}
					else if (v == 2){
						if (s.length != 2){
							illegalArgumentException("int1 int2");
						}
						else{
							if (isNumeric(s[0]) && isNumeric(s[1]) && s[0].length() == 1 && s[1].length() == 1){
								n = Integer.parseInt(s[0]);
								m = Integer.parseInt(s[1]);
								if (n >= 0 && n < cell && m >= 0 && m < 8){
									id.add(id.getLast() + 1);
									fileStr.add("/~disp-cell-lower:" + temp);
									controller.addElement("/~disp-cell-lower:" + temp, id.getLast());
								}
								else{
									indexOutOfBoundsException("Cell");
								}
							}
							else{
								illegalArgumentException("int1 int2");
							}
						}
					}
				}


			}

		});
	}

	/**
	 * Implements all the action listeners for various components within the GUI.
	 */
	protected static void addActionListeners() {
		((JFrame) gui).addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				int keyCode = e.getKeyCode();
				switch (keyCode){
					case KeyEvent.VK_UP:
						//handle up
						if (currentLine == 2) {
							System.out.println("Line " + 2);
						}
						else {
							currentLine--;
							System.out.println("Line " + currentLine);
						}
					break;
					case KeyEvent.VK_DOWN:
						//handle down
						if (currentLine == fileStr.size()) {
							System.out.println("Line " + fileStr.size());
						}
						else {
							currentLine++;
							System.out.println("Line " + currentLine);
						}
					break;
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
			}
			
		});
		
		((JMenuItem) compMap.get("newMenuItem")).addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (!isSaved) {
					JOptionPane.showMessageDialog(gui, "please save first");
				}

				isSaved = false;
				NewFileGUI temp = new NewFileGUI();
				temp.setVisible(true);
				HashMap<String, Component> tempMap = ((NewFileGUI) temp).getCompMap();
				JTextField numCell = (JTextField) tempMap.get("numCell");
				JTextField numCol = (JTextField) tempMap.get("numCol");
				fileStr = new LinkedList<String>();
				id = new LinkedList<Integer>();
				((JButton) tempMap.get("createButton")).addActionListener(new ActionListener() {

					public void actionPerformed(java.awt.event.ActionEvent evt) {

						cell = Integer.parseInt(numCell.getText());
						col = Integer.parseInt(numCol.getText());
						isOpened = true;
						stateChanged();
						
						fileStr.add("Cell " + cell );
						fileStr.add("Button " + col );
						((JTextField) compMap.get("inputTextField")).setText("");
						id = controller.newDocCreated(fileStr);

						temp.dispose();
					}
					
					
				});

				((JButton) tempMap.get("cancelButton")).addActionListener(new ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						//cell = Integer.parseInt(numCell.getText());
						//col = Integer.parseInt(numCol.getText());;
						isOpened = false;
						temp.dispose();
					}
				});

			}

		});
		((JMenuItem) compMap.get("loadScenarioMenuItem")).addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource().equals("loadScenarioMenuItem")) {
					isOpened = true;
					stateChanged();
				}

				if (!isSaved) {
				}
				try {
					f = openFileChooser(new File("FactoryScenarios/"), "txt");
					if (f != null) {
						currentFile = f;
						gui.setTitle("Authoring App - " + currentFile.getName());
						FileParser fp = new FileParser(f);
						fileStr = fp.getArray();
					}
					id=controller.newDocCreated(fileStr);
					isOpened = true;
					((JTextField) compMap.get("inputTextField")).setText("");
					stateChanged();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		((JMenuItem) compMap.get("saveMenuItem")).addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				SaveAsFile save = new SaveAsFile("txt", new File(currentFile.getAbsolutePath()));
				try {
					save.stringArrayToFile(fileStr);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

		});
		
		((JMenuItem) compMap.get("saveAsMenuItem")).addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				f = saveFileChooser(new File("FactoryScenarios/"), "txt");
				if (f != null) {
					currentFile = f;
					gui.setTitle("Authoring App - " + currentFile.getName());
					SaveAsFile save = new SaveAsFile("txt", new File(currentFile.getAbsolutePath()));
					try {
						save.stringArrayToFile(fileStr);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}

		});
		((JMenuItem) compMap.get("loadAndRunMenuItem")).addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				f = openFileChooser(new File("FactoryScenarios/"), "txt");
				if (f != null) {
					currentFile = f;
					ToyAuthoring ta = new ToyAuthoring(f.getAbsolutePath());
					ta.start();

				}
			}

		});
		
		((JMenuItem) compMap.get("exitMenuItem")).addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
			
		}); 
	}
	
	/**
	 * Enables the components when a scenario file is opened in the application.
	 */
	protected static void stateChanged() {
		if (isOpened) { 
			//System.out.println(true);
			compMap.get("saveAsMenuItem").setEnabled(true);
			compMap.get("insertText").setEnabled(true);
			compMap.get("insertPause").setEnabled(true);
			compMap.get("insertSkipButton").setEnabled(true);
			compMap.get("insertSkip").setEnabled(true);
			compMap.get("insertUserInput").setEnabled(true);
			compMap.get("insertRepeatButton").setEnabled(true);
			compMap.get("insertRepeat").setEnabled(true);
			compMap.get("insertEndRepeat").setEnabled(true);
			compMap.get("insertResetButtons").setEnabled(true);
			compMap.get("insertSound").setEnabled(true);
			compMap.get("displayComboBox").setEnabled(true);
			compMap.get("displayAddButton").setEnabled(true);
			compMap.get("editRemoveLine").setEnabled(true);
			compMap.get("upButton").setEnabled(true);
			compMap.get("downButton").setEnabled(true);
		}
	}

	/**
	 * Opens a FileChooser interface for the user to select a file.
	 * 
	 * @param currentDir
	 *            The directory that will first appear on the FileChooser.
	 * @param ext
	 *            Expected file extension to be returned.
	 * @return The file selected by the user and is of the appropriate
	 *         extension, otherwise null.
	 */
	public static File openFileChooser(File currentDir, String ext) {
		fc.setCurrentDirectory(currentDir);
		int returnVal = fc.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			String selectedExt = selectedextension();                                    //Used refactoring here
			if (!ext.equals(selectedExt)) {
				errorPanel = new JPanel();
				JOptionPane.showMessageDialog(errorPanel, "Could not open file, Wrong file type", "Error",
						JOptionPane.ERROR_MESSAGE);
				return null;
			} else {
				return fc.getSelectedFile();
			}
		}
		return null;
	}

	private static String selectedextension() {
		String selectedExt = FilenameUtils.getExtension(fc.getSelectedFile().getName());
		return selectedExt;
	}
	
	public static File saveFileChooser(File currentDir, String ext) {
		fc.setCurrentDirectory(currentDir);
		int returnVal = fc.showSaveDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			String selectedExt = selectedextension();
			if (!ext.equals(selectedExt)) {
				/* final JPanel */errorPanel = new JPanel();
				JOptionPane.showMessageDialog(errorPanel, "Could not save file, Wrong file type", "Error",
						JOptionPane.ERROR_MESSAGE);
				return null;
			} else {
				return fc.getSelectedFile();
			}
		}
		return null;
	}
	
	public static String getInputText(){
		return ((JTextField) compMap.get("inputTextField")).getText();
	}
	
	public static void nullArgumentException(){
		consoleController.addElement("NullArgumentException: No input detected. See \"Help\" for user manual.");
	}
	
	public static void illegalArgumentException(String expected){
		consoleController.addElement("IllegalArgumentException, Expected: " + expected + ". See \"Help\" for user manual.");
	}
	
	public static void indexOutOfBoundsException(String type){
		String range = "";
		int n;
		if (type == "Button"){
			n = col - 1;
			range = "[0," + n + "]";
		}
		else if (type == "Cell"){
			n = cell - 1;
			range = "[0," + n + "]";
		}
		consoleController.addElement("indexOutOfBoundsException: " + type + " value should have a range of " + range + ". See \"Help\" for user manual.");
	}
	
	public static int validString(String s){
		if (s.isEmpty()) {
			return 0;
		}
		else if (s.contains("/~")){
			return 1;
		}
		else{
			return 2;
		}
	}
	
	public static boolean validFileFormat(String ext){
		if (ext.contains("ext")){
			return true;
		}
		return false;
	}
	
	public static boolean isNumeric(String s){
		  try  
		  {  
		    int n = Integer.parseInt(s);  
		  }  
		  catch(NumberFormatException e)  
		  {  
		    return false;  
		  }  
		  return true;  
	}
	
	public static boolean isBinaryChar(String s){
		if (s.length()!=8 || !s.matches("[01]+"))
		{
			return false;
		}  
		return true;
	}
	
	public static boolean isChar(String s){
		if (s.length() != 1)
		{
			return false;
		}
		else{
			char c = s.charAt(0);
			if (Character.isLetter(c)){
				return true;
			}
			return false;
		}
	}
}

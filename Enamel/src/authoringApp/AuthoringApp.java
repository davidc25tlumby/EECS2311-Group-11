package authoringApp;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.text.BadLocationException;

import enamel.ToyAuthoring;

/**
 * An application to create scenario files that are compatible with a Treasure
 * Braille Box simulation.
 * 
 * @author Xiahan Chen, Huy Hoang Minh Cu, Qasim Mahir
 */
public class AuthoringApp {

	private static JFrame gui;
	private static File f, currentFile;// , error;
	private static LinkedList<String> fileStr;// , consoleStr;
	private static LinkedList<Integer> id;
	private static HashMap<String, Component> compMap;
	private static JTextPaneController controller, consoleController;
	private static int idCount;
	private static String state;
	private static int currentLine = 0, cell = 0, col = 0;
	private static boolean isSaved = true, isOpened = false;
	private static AudioRecorder recorder;
	private static boolean checkRecord = false;
	private static LinkedList<DataNode> undoNode = new LinkedList<DataNode>();
	private static LinkedList<DataNode> redoNode = new LinkedList<DataNode>();
	private static int lineFocus = 0, topPadding, bottomPadding;
	private static int TP_HEIGHT = 30;
	private static JScrollPane sp, consoleSP;

	/**
	 * Initializes the application by drawing the GUI and initializing a controller
	 * for the JTextPane. LinkedLists are created for an id+string pair that
	 * represents elements on the JTextPane.
	 * 
	 * @param args
	 *            unused
	 */
	public static void main(String[] args) {

		java.awt.EventQueue.invokeLater(new Runnable() {

			public void run() {

				gui = new AuthoringAppGUI();
				gui.setVisible(true);
				compMap = ((AuthoringAppGUI) gui).getCompMap();
				addActionListeners();
				addEditorButtons();
			}

		});

	}


	/**
	 * Initializes and implements the KeyBindings that will be associated with the
	 * GUIs JFrame.
	 */
	protected static void addKeyBindings() {
		
		JRootPane rp = gui.getRootPane();
		JTextPane sp = ((JTextPane) compMap.get("scenarioPane"));
		JTextPane cp = ((JTextPane) compMap.get("consolePane"));
		JTextField tf = ((JTextField) compMap.get("inputTextField"));
		
		rp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("UP"), "navUp");
		rp.getActionMap().put("navUp", navigateUp);
		
		rp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("DOWN"), "navDown");
		rp.getActionMap().put("navDown", navigateDown);
		
		rp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ENTER"), "newLine");
		rp.getActionMap().put("newLine", newLine);
		
		rp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("DELETE"), "delLine");
		rp.getActionMap().put("delLine", deleteLine);

		sp.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("UP"), "navUp");
		sp.getActionMap().put("navUp", navigateUp);
		
		sp.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("DOWN"), "navDown");
		sp.getActionMap().put("navDown", navigateDown);
		
		sp.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("ENTER"), "newLine");
		sp.getActionMap().put("newLine", newLine);
		
		sp.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("DELETE"), "delLine");
		sp.getActionMap().put("delLine", deleteLine);
		
		cp.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("UP"), "navUp");
		cp.getActionMap().put("navUp", navigateUp);
		
		cp.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("DOWN"), "navDown");
		cp.getActionMap().put("navDown", navigateDown);
		
		cp.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("ENTER"), "newLine");
		cp.getActionMap().put("newLine", newLine);
		
		cp.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("DELETE"), "delLine");
		cp.getActionMap().put("delLine", deleteLine);
		
		tf.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("UP"), "navUp");
		tf.getActionMap().put("navUp", navigateUp);
		
		tf.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("DOWN"), "navDown");
		tf.getActionMap().put("navDown", navigateDown);
		
		tf.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("ENTER"), "newLine");
		tf.getActionMap().put("newLine", newLine);
		
		tf.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("DELETE"), "delLine");
		tf.getActionMap().put("delLine", deleteLine);
	}

	/**
	 * Initializes and implements the JButtons that the users will interact with.
	 */
	protected static void addEditorButtons() {

		((JButton) compMap.get("testButton")).addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				sp.getVerticalScrollBar().setValue(lineFocus);
			}

		});

		((JButton) compMap.get("insertText")).addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				addLine(getInputText());
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
				} else if (v == 1) {
					illegalArgumentException("int");
				} else if (v == 2) {
					if (s.length != 1) {
						illegalArgumentException("int");
					} else {
						if (isNumeric(temp)) {
							n = Integer.parseInt(temp);
							if (n >= 0) {
								addLine("/~pause:" + n);
							} else {
								illegalArgumentException("int");
							}
						} else {
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
				} else if (v == 1) {
					illegalArgumentException("int string");
				} else if (v == 2) {
					if (s.length != 2) {
						illegalArgumentException("int string");
					} else {
						if (isNumeric(s[0]) && !isNumeric(s[1])) {
							n = Integer.parseInt(s[0]);
							if (n >= 0 && n < col) {
								addLine("/~skip-button:" + temp);
							} else {
								indexOutOfBoundsException("Button");
							}
						} else {
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
				} else if (v == 1) {
					illegalArgumentException("string");
				} else if (v == 2) {
					if (s.length != 1) {
						illegalArgumentException("string");
					} else {
						if (!isNumeric(temp)) {
							addLine("/~" + temp);
						} else {
							illegalArgumentException("string");
						}
					}
				}
			}

		});

		((JButton) compMap.get("insertUserInput")).addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				addLine("/~user-input");

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
				} else if (v == 1) {
					illegalArgumentException("int");
				} else if (v == 2) {
					if (s.length != 1) {
						illegalArgumentException("int");
					} else {
						if (isNumeric(temp)) {
							n = Integer.parseInt(temp);
							if (n >= 0 && n < col) {
								addLine("/~repeat-button:" + n);
							} else {
								indexOutOfBoundsException("Button");
							}
						} else {
							illegalArgumentException("int");
						}
					}
				}
			}

		});

		((JButton) compMap.get("insertRepeat")).addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addLine("/~repeat");
			}

		});

		((JButton) compMap.get("insertEndRepeat")).addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addLine("/~endrepeat");
			}

		});

		((JButton) compMap.get("insertResetButtons")).addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addLine("/~reset-buttons");
			}
		});

		((JButton) compMap.get("insertSound")).addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				f = fileChooser.openFileChooser(new File("FactoryScenarios/AudioFiles"), "wav");
				if (f != null) {
					addLine("/~sound:" + f.getName());
				}
			}
		});

		((JButton) compMap.get("editRemoveLine")).addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				deleteLine();
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
					} else if (v == 1) {
						illegalArgumentException("string");
					} else if (v == 2) {
						if (temp.length() > cell) {
							illegalArgumentException("string");
						} else {
							addLine("/~disp-string:" + temp);
						}
					}
				} else if (cbStr.equals("/~disp-clearAll")) {
					addLine("/~disp-clearAll");
				} else if (cbStr.equals("/~disp-clear-cell:int")) {
					if (v == 0) {
						nullArgumentException();
					} else if (v == 1) {
						illegalArgumentException("int");
					} else if (v == 2) {
						if (s.length != 1) {
							illegalArgumentException("int");
						} else {
							if (isNumeric(temp)) {
								n = Integer.parseInt(temp);
								if (n >= 0 && n < cell) {
									addLine("/~disp-clear-cell:" + temp);
								} else {
									indexOutOfBoundsException("Cell");
								}
							} else {
								illegalArgumentException("int");
							}
						}
					}
				} else if (cbStr.equals("/~disp-cell-pins:int string")) {
					if (v == 0) {
						nullArgumentException();
					} else if (v == 1) {
						illegalArgumentException("int string");
					} else if (v == 2) {
						if (s.length != 2) {
							illegalArgumentException("int string");
						} else {
							if (isNumeric(s[0]) && isBinaryChar(s[1])) {
								n = Integer.parseInt(s[0]);
								if (n >= 0 && n < cell) {
									addLine("/~disp-cell-pins:" + temp);
								} else {
									indexOutOfBoundsException("Cell");
								}
							} else {
								illegalArgumentException("int string");
							}
						}
					}
				} else if (cbStr.equals("/~disp-cell-char:int char")) {
					if (v == 0) {
						nullArgumentException();
					} else if (v == 1) {
						illegalArgumentException("int char");
					} else if (v == 2) {
						if (s.length != 2) {
							illegalArgumentException("int char");
						} else {
							if (isNumeric(s[0]) && isChar(s[1])) {
								n = Integer.parseInt(s[0]);
								if (n >= 0 && n < cell) {
									addLine("/~disp-cell-char:" + temp);
								} else {
									indexOutOfBoundsException("Cell");
								}
							} else {
								illegalArgumentException("int char");
							}
						}
					}
				} else if (cbStr.equals("/~disp-cell-raise:int1 int2")) {
					if (v == 0) {
						nullArgumentException();
					} else if (v == 1) {
						illegalArgumentException("int1 int2");
					} else if (v == 2) {
						if (s.length != 2) {
							illegalArgumentException("int1 int2");
						} else {
							if (isNumeric(s[0]) && isNumeric(s[1]) && s[0].length() == 1 && s[1].length() == 1) {
								n = Integer.parseInt(s[0]);
								m = Integer.parseInt(s[1]);
								if (n >= 0 && n < cell && m >= 0 && m < 8) {
									addLine("/~disp-cell-char:" + temp);
								} else {
									indexOutOfBoundsException("Cell");
								}
							} else {
								illegalArgumentException("int1 int2");
							}
						}
					}
				}

				if (cbStr.equals("/~disp-cell-lower:int1 int2")) {
					if (v == 0) {
						nullArgumentException();
					} else if (v == 1) {
						illegalArgumentException("int1 int2");
					} else if (v == 2) {
						if (s.length != 2) {
							illegalArgumentException("int1 int2");
						} else {
							if (isNumeric(s[0]) && isNumeric(s[1]) && s[0].length() == 1 && s[1].length() == 1) {
								n = Integer.parseInt(s[0]);
								m = Integer.parseInt(s[1]);
								if (n >= 0 && n < cell && m >= 0 && m < 8) {
									addLine("/~disp-cell-lower:" + temp);
								} else {
									indexOutOfBoundsException("Cell");
								}
							} else {
								illegalArgumentException("int1 int2");
							}
						}
					}
				}

			}

		});
	}

	/**
	 * Initializes and implements the JMenuItems that the user will interact with.
	 */
	protected static void addActionListeners() {
		
		gui.addComponentListener(new ComponentListener(){

			@Override
			public void componentHidden(ComponentEvent arg0) {
				//unused
			}

			@Override
			public void componentMoved(ComponentEvent arg0) {
				//unused
			}

			@Override
			public void componentResized(ComponentEvent arg0) {
				if (isOpened){
					state = "RESIZE";
					Rectangle rectangle = ((JScrollPane) compMap.get("scenarioScrollPane")).getVisibleRect();
					int height = rectangle.height;
					TP_HEIGHT = height / 19;
					refocus();
				}
			}

			@Override
			public void componentShown(ComponentEvent arg0) {
				//unused
			}
			
		});

		((JMenuItem) compMap.get("newMenuItem")).addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (!isSaved) {
					// execute save then resume action
				} else {
					initializeComponents();
					isSaved = false;
					NewFileGUI temp = new NewFileGUI();
					temp.setVisible(true);
					HashMap<String, Component> tempMap = ((NewFileGUI) temp).getCompMap();
					JTextField numCell = (JTextField) tempMap.get("numCell");
					JTextField numCol = (JTextField) tempMap.get("numCol");

					((JButton) tempMap.get("createButton")).addActionListener(new ActionListener() {

						public void actionPerformed(java.awt.event.ActionEvent evt) {

							String cellStr = numCell.getText();
							String colCell = numCol.getText();
							if (isNumeric(cellStr) && isNumeric(colCell)) {
								cell = Integer.parseInt(numCell.getText());
								col = Integer.parseInt(numCol.getText());

								isOpened = true;
								isSaved = false;
								stateChanged();

								fileStr.add("Cell " + cell);
								fileStr.add("Button " + col);
								((JTextField) compMap.get("inputTextField")).setText("");
								id = controller.newDocCreated(fileStr);
								idCount = id.getLast();
								currentLine = 0;
								controller.setAttribute(id.get(currentLine));
								controller.addElement("", Integer.toString(id.getLast()), -2);
								fileStr.addLast("");
								id.add(-2);
								temp.dispose();
							} else {
								// throw invalid input
							}
						}

					});

					((JButton) tempMap.get("cancelButton")).addActionListener(new ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent evt) {
							isOpened = false;
							temp.dispose();
						}
					});
				}

			}

		});
		((JMenuItem) compMap.get("loadScenarioMenuItem")).addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (!isSaved) {
					// execute save then resume action
				} else {
					try {
						initializeComponents();
						state = "NEW";
						f = fileChooser.openFileChooser(new File("FactoryScenarios/"), "txt");
						if (f != null) {
							currentFile = f;
							gui.setTitle("Authoring App - " + currentFile.getName());
							FileParser fp = new FileParser(f);
							fileStr = fp.getArray();
						}
						id = controller.newDocCreated(fileStr);
						idCount = id.getLast();
						currentLine = 0;
						controller.setAttribute(id.get(currentLine));
						controller.addElement("", Integer.toString(id.getLast()), -2);
						fileStr.addLast("");
						id.add(-2);
						isSaved = false;
						isOpened = true;
						((JTextField) compMap.get("inputTextField")).setText("");
						stateChanged();
						

					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});

		((JMenuItem) compMap.get("saveMenuItem")).addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				gui.setTitle("Authoring App - " + currentFile.getName());
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
				f = fileChooser.saveFileChooser(new File("FactoryScenarios/"), "txt");
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
				f = fileChooser.openFileChooser(new File("FactoryScenarios/"), "txt");
				if (f != null) {
					currentFile = f;
					ToyAuthoring ta = new ToyAuthoring(f.getAbsolutePath());
					ta.start();

				}
			}

		});

		((JMenuItem) compMap.get("exitMenuItem")).addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}

		});

		((JMenuItem) compMap.get("undoMenuItem")).addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				performUndo();
			}

		});

		((JMenuItem) compMap.get("redoMenuItem")).addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				performRedo();
			}

		});

		((recordButton) compMap.get("recordButton")).addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				File record = fileChooser.saveFileChooser(new File("FactoryScenarios/AudioFiles"), "wav");
				if (record != null) {
					JOptionPane.showMessageDialog(null, "Start recording", "InfoBox: " + "warning",
							JOptionPane.INFORMATION_MESSAGE);
					checkRecord = true;
					recorder = new AudioRecorder(record);
					recorder.start();
					((recordButton) compMap.get("stopButton")).addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							if (checkRecord) {
								recorder.finish();
								JOptionPane.showMessageDialog(null, "Record stoped", "InfoBox: " + "Finish",
										JOptionPane.INFORMATION_MESSAGE);
							}

						}

					});
				}
			}

		});

	}

	protected static void refocus() {
		switch (state) {
			case "ADD_LINE":
				if (topPadding < TP_HEIGHT - 1){
					topPadding++;
					bottomPadding--;
				}
				else{
					lineFocus += 19;
				}
				break;
			case "DELETE_LINE":
				if (topPadding > 0){
					topPadding--;
					bottomPadding++;
				}
				else{
					if (currentLine != 0){
						lineFocus -= 19;
					}
				}
				break;
			default:
				if (currentLine < TP_HEIGHT){
					topPadding = currentLine;
					bottomPadding = TP_HEIGHT - 1 - topPadding;
					lineFocus = 0;
				}
				else if (currentLine > fileStr.size() - TP_HEIGHT){
					bottomPadding = fileStr.size() - 2 - currentLine;
					topPadding = TP_HEIGHT - bottomPadding - 1;
					lineFocus = (fileStr.size() - TP_HEIGHT - 1) * 19;
				}
				else{
					topPadding = 0;
					bottomPadding = TP_HEIGHT - 1 - topPadding;
					lineFocus = currentLine * 19;
				}
		}
		sp.getVerticalScrollBar().setValue(lineFocus);
	}

	/**
	 * Initializes various objects associated with the JTextPanes. Calling this
	 * method resets the JTextPanes and the LinkedLists of strings and ID's.
	 */
	protected static void initializeComponents() {
		addKeyBindings();
		fileStr = new LinkedList<String>();
		id = new LinkedList<Integer>();
		id.add(0);
		((JTextPane) compMap.get("scenarioPane")).setText("");
		((JTextPane) compMap.get("consolePane")).setText("");
		controller = new JTextPaneController((JTextPane) compMap.get("scenarioPane"),
				(JScrollPane) compMap.get("scenarioScrollPane"));
		consoleController = new JTextPaneController((JTextPane) compMap.get("consolePane"),
				(JScrollPane) compMap.get("consoleScrollPane"));
		sp = (JScrollPane) compMap.get("scenarioScrollPane");
		consoleSP = (JScrollPane) compMap.get("consoleScrollPane");
		
		JTextPane scenarioPane = ((JTextPane) compMap.get("scenarioPane"));
		((JTextField) compMap.get("inputTextField")).requestFocus();
		scenarioPane.addComponentListener(new ComponentListener(){

			@Override
			public void componentHidden(ComponentEvent e) {
				//unused
			}

			@Override
			public void componentMoved(ComponentEvent e) {
				//unused
			}

			@Override
			public void componentResized(ComponentEvent e) {
				if (isOpened){
					refocus();
				}
			}

			@Override
			public void componentShown(ComponentEvent e) {
				//unused
			}
			
		});
		topPadding = 0;
		bottomPadding = TP_HEIGHT - 1;
	}

	/**
	 * Enables the components when a scenario file is opened in the application.
	 */
	protected static void stateChanged() {
		if (isOpened) {
			compMap.get("saveMenuItem").setEnabled(true);
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
			compMap.get("undoMenuItem").setEnabled(true);
			compMap.get("redoMenuItem").setEnabled(true);
		}
	}

	public static String getInputText() {
		return ((JTextField) compMap.get("inputTextField")).getText();
	}

	/**
	 * Prints into the console a NullArgumentException
	 */
	public static void nullArgumentException() {
		printInConsole("NullArgumentException: No input detected. See \"Help\" for user manual.");
		((JTextField) compMap.get("inputTextField")).requestFocus();
		((JTextField) compMap.get("inputTextField")).setText("");
		
	}

	/**
	 * Prints into the console a illegalArgumentException
	 * 
	 * @param expected
	 *            the expected input types.
	 */
	public static void illegalArgumentException(String expected) {
		printInConsole("IllegalArgumentException, Expected: " + expected + ". See \"Help\" for user manual.");
		((JTextField) compMap.get("inputTextField")).requestFocus();
		((JTextField) compMap.get("inputTextField")).setText("");
	}

	/**
	 * Prints into the console an IndexOutOfBoundsException.
	 * 
	 * @param type
	 *            The variable in which the exception occurred.
	 */
	public static void indexOutOfBoundsException(String type) {
		String range = "";
		int n;
		if (type == "Button") {
			n = col - 1;
			range = "[0," + n + "]";
		} else if (type == "Cell") {
			n = cell - 1;
			range = "[0," + n + "]";
		}
		printInConsole("indexOutOfBoundsException: " + type + " value should have a range of " + range
				+ ". See \"Help\" for user manual.");
		((JTextField) compMap.get("inputTextField")).requestFocus();
		((JTextField) compMap.get("inputTextField")).setText("");
	}

	private static void printInConsole(String s) {
		// TODO Auto-generated method stub
		consoleController.addElement(s);
		JScrollBar vertical = consoleSP.getVerticalScrollBar();
		vertical.setValue(vertical.getMaximum());
	}

	/**
	 * Checks for the minimum requirements of JTextField inputs. The string should
	 * not be empty and should not contain the token "/~".
	 * 
	 * @param s
	 *            The String to check
	 * @return 0 if the string is empty, 1 if it contains "/~", 2 if valid.
	 */
	public static int validString(String s) {
		if (s.isEmpty()) {
			return 0;
		} else if (s.contains("/~")) {
			return 1;
		} else {
			return 2;
		}
	}

	/**
	 * TODO?? Currently unused and I don't know who put this here.
	 * 
	 * @param ext
	 * @return
	 */
	public static boolean validFileFormat(String ext) {
		if (ext.contains("ext")) {
			return true;
		}
		return false;
	}

	/**
	 * Checks if a string is an integer.
	 * 
	 * @param s
	 *            The string to check.
	 * @return True if the string is an integer, otherwise false.
	 */
	public static boolean isNumeric(String s) {
		try {
			int n = Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	/**
	 * Checks if the string is an 8 bit binary value.
	 * 
	 * @param s
	 *            The string to check.
	 * @return True if the string is binary 8 bit, otherwise false.
	 */
	public static boolean isBinaryChar(String s) {
		if (s.length() != 8 || !s.matches("[01]+")) {
			return false;
		}
		return true;
	}

	/**
	 * Checks if the string is a single character.
	 * 
	 * @param s
	 *            The string to check.
	 * @return True if the string is a character, otherwise false.
	 */
	public static boolean isChar(String s) {
		if (s.length() != 1) {
			return false;
		} else {
			char c = s.charAt(0);
			if (Character.isLetter(c)) {
				return true;
			}
			return false;
		}
	}

	public static void addLine(String temp) {
		state = "ADD_LINE";
		redoNode.clear();
		if (fileStr.size() == 1) {
			fileStr.addFirst(temp);
			idCount++;
			controller.removeAttribute(id.get(currentLine));
			id.add(currentLine, idCount);
			controller.addElement(temp, "main", idCount);
			controller.setAttribute(id.get(currentLine));
		} else {
			fileStr.add(currentLine + 1, temp);
			idCount++;
			id.add(currentLine + 1, idCount);
			controller.addElement(temp, Integer.toString(id.get(currentLine)), idCount);

			DataNode tempNode = new DataNode(id.get(currentLine), idCount, temp, currentLine + 1, true);
			undoNode.push(tempNode);

			controller.removeAttribute(id.get(currentLine));
			currentLine++;
			controller.setAttribute(id.get(currentLine));
		}
		((JTextField) compMap.get("inputTextField")).requestFocus();
		((JTextField) compMap.get("inputTextField")).setText("");
	}

	protected static void performRedo() {
		state = "REDO_OR_UNDO";
		if (!redoNode.isEmpty()) {
			controller.removeAttribute(id.get(currentLine));
			DataNode tempNode = redoNode.pop();
			undoNode.push(tempNode);
			if (tempNode.adding) {
				addLineAfter(tempNode.element, tempNode.currentLine, tempNode.currentID, tempNode.prevID);
			} else {
				deleteLine(tempNode.currentID, tempNode.currentLine);
			}
		}
	}

	protected static void performUndo() {
		state = "REDO_OR_UNDO";
		if (!undoNode.isEmpty()) {
			controller.removeAttribute(id.get(currentLine));
			DataNode tempNode = undoNode.pop();
			redoNode.push(tempNode);
			if (tempNode.adding) {
				deleteLine(tempNode.currentID, tempNode.currentLine);
			} else {
				addLineAfter(tempNode.element, tempNode.currentLine, tempNode.currentID, tempNode.prevID);
			}
		}
	}

	protected static void navigateUp() {
		if (fileStr.size() > 2) {
			if (currentLine == 0) {
				controller.removeAttribute(id.getFirst());
				currentLine = id.size() - 2;
				controller.setAttribute(id.get(currentLine));
				setScrollBarValue(2);
			} else {
				controller.removeAttribute(id.get(currentLine));
				currentLine--;
				controller.setAttribute(id.get(currentLine));
				setScrollBarValue(0);
			}
		}
	}

	protected static void navigateDown() {
		if (fileStr.size() > 2) {
			if (currentLine == fileStr.size() - 2) {
				controller.removeAttribute(id.get(currentLine));
				currentLine = 0;
				controller.setAttribute(id.getFirst());
				setScrollBarValue(3);
			} else {
				controller.removeAttribute(id.get(currentLine));
				currentLine++;
				controller.setAttribute(id.get(currentLine));
				setScrollBarValue(1);
			}
		}
	}
	
	private static void setScrollBarValue(int state) {
		//move down
		if (state == 0){
			if (topPadding > 0){
				topPadding--;
				bottomPadding++;
			}
			else{
				lineFocus -= 19;
			}
		}
		//move up
		else if (state == 1){
			if (bottomPadding > 0){
				bottomPadding--;
				topPadding++;
			}
			else{
				lineFocus += 19;
			}
		}
		//move to bottom
		else if (state == 2){
			topPadding = TP_HEIGHT - 1;
			bottomPadding = 0;
			lineFocus = (fileStr.size() - TP_HEIGHT - 1) * 19;
		}
		//move to top
		else if (state == 3){
			topPadding = 0;
			bottomPadding = TP_HEIGHT - 1;
			lineFocus = 0;
		}
		sp.getVerticalScrollBar().setValue(lineFocus);
	}

	protected static void deleteLine() {
		state = "DELETE_LINE";
		redoNode.clear();
		if (fileStr.size() > 1) {
			if (currentLine == 0) {
				DataNode tempNode = new DataNode(-1, id.getFirst(), fileStr.getFirst(), 0, false);
				undoNode.push(tempNode);
				controller.removeElement(id.getFirst());
				fileStr.removeFirst();
				id.removeFirst();
				controller.setAttribute(id.getFirst());
			} else {
				DataNode tempNode = new DataNode(id.get(currentLine - 1), id.get(currentLine), fileStr.get(currentLine),
						currentLine, false);
				undoNode.push(tempNode);
				controller.removeElement(id.get(currentLine));
				fileStr.remove(currentLine);
				id.remove(currentLine);
				currentLine--;
				controller.setAttribute(id.get(currentLine));
			}
		}
	}

	public static void addLineAfter(String element, int curLine, int currentID, int prevID) {
		if (prevID == -1) {
			fileStr.addFirst(element);
			id.addFirst(currentID);
			controller.addElement(element, "main", currentID);
			currentLine = curLine;
			controller.setAttribute(currentID);
		} else {
			fileStr.add(curLine, element);
			id.add(curLine, currentID);
			controller.addElement(element, Integer.toString(prevID), currentID);
			currentLine = curLine;
			controller.setAttribute(id.get(currentLine));
		}
	}

	public static void deleteLine(int currentID, int curLine) {
		controller.removeElement(currentID);
		fileStr.remove(curLine);
		id.remove(curLine);
		if (curLine == 0) {
			currentLine = 0;
		} else {
			currentLine = curLine - 1;
		}
		controller.setAttribute(id.get(currentLine));
	}

	protected static void newLine() {
		if (currentLine + 1 != fileStr.size()) {
			addLine("");
		} else if (fileStr.getLast() != "") {
			addLine("");
		}
	}
	
	static Action navigateUp = new AbstractAction() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			navigateUp();
		}
	};
	
	static Action navigateDown = new AbstractAction() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			navigateDown();
		}
	};
	
	static Action newLine = new AbstractAction() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			newLine();
		}
	};
	
	static Action deleteLine = new AbstractAction() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e){
			deleteLine();
		}
	};
}
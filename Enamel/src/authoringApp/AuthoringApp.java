package authoringApp;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
	//private static JFileChooser fc = new JFileChooser();
	private static File f, currentFile;//, error;
	private static LinkedList<String> fileStr;//, consoleStr;
	private static LinkedList<Integer> id;
	//private static JPanel errorPanel;
	private static HashMap<String, Component> compMap;
	private static JTextPaneController controller, consoleController;
	private static int idCount;

	private static int currentLine = 2, cell = 0, col = 0;
	private static boolean isSaved = true, isOpened = false;
	//private static String currentID;
	private static AudioRecorder recorder;
	private static boolean checkRecord = false;
	private static LinkedList<DataNode> undoNode = new LinkedList<DataNode>();
	private static LinkedList<DataNode> redoNode = new LinkedList<DataNode>();

	/**
	 * Initializes the application by drawing the GUI and initializing a
	 * controller for the JTextPane. LinkedLists are created for an id+string
	 * pair that represents elements on the JTextPane.
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
				addKeyBindings();
			}

		});

	}

	/**
	 * Initializes and implements the KeyBindings that will be associated with
	 * the GUIs JFrame.
	 */
	protected static void addKeyBindings() {
		// TODO Auto-generated method stub
		JRootPane rp = gui.getRootPane();
		InputMap im = rp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap am = rp.getActionMap();

		JTextPane tp = ((JTextPane) compMap.get("scenarioPane"));
		InputMap im2 = tp.getInputMap(JComponent.WHEN_FOCUSED);
		ActionMap am2 = tp.getActionMap();

		JTextPane tp2 = ((JTextPane) compMap.get("consolePane"));
		InputMap im3 = tp2.getInputMap(JComponent.WHEN_FOCUSED);
		ActionMap am3 = tp2.getActionMap();

		im.put(KeyStroke.getKeyStroke("UP"), "navUp");
		im.put(KeyStroke.getKeyStroke("DOWN"), "navDown");
		im.put(KeyStroke.getKeyStroke("ENTER"), "newLine");
		im.put(KeyStroke.getKeyStroke("DELETE"), "delLine");

		im2.put(KeyStroke.getKeyStroke("UP"), "navUp");
		im2.put(KeyStroke.getKeyStroke("DOWN"), "navDown");
		im2.put(KeyStroke.getKeyStroke("ENTER"), "newLine");
		im2.put(KeyStroke.getKeyStroke("DELETE"), "delLine");

		im3.put(KeyStroke.getKeyStroke("UP"), "navUp");
		im3.put(KeyStroke.getKeyStroke("DOWN"), "navDown");
		im3.put(KeyStroke.getKeyStroke("ENTER"), "newLine");
		im3.put(KeyStroke.getKeyStroke("DELETE"), "delLine");

		am3.put("navUp", new AbstractAction() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -5971069686488190353L;

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				navigateUp();
			}

		});

		am3.put("navDown", new AbstractAction() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 6683017700153087856L;

			@Override
			public void actionPerformed(ActionEvent e) {
				navigateDown();
			}

		});

		am3.put("newLine", new AbstractAction() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -7879264567501986325L;

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.out.println("new ln");
				if (currentLine + 1 != fileStr.size()){
					addLine("");
				}
				else if (fileStr.getLast() != ""){
					addLine("");
				}
			}

		});

		am3.put("delLine", new AbstractAction() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -7593553052034429973L;

			@Override
			public void actionPerformed(ActionEvent e) {
				deleteLine();
			}

		});

		am2.put("navUp", new AbstractAction() {

			private static final long serialVersionUID = -5971069686488190353L;

			@Override
			public void actionPerformed(ActionEvent e) {
				navigateUp();
			}

		});

		am2.put("navDown", new AbstractAction() {

			private static final long serialVersionUID = 6683017700153087856L;

			@Override
			public void actionPerformed(ActionEvent e) {
				navigateDown();
			}

		});

		am2.put("newLine", new AbstractAction() {

			private static final long serialVersionUID = -7879264567501986325L;

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("new ln");
				if (currentLine + 1 != fileStr.size()){
					addLine("");
				}
				else if (fileStr.getLast() != ""){
					addLine("");
				}
			}

		});

		am2.put("delLine", new AbstractAction() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -7593553052034429973L;

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				deleteLine();
			}

		});

		am.put("navUp", new AbstractAction() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -5971069686488190353L;

			@Override
			public void actionPerformed(ActionEvent e) {
				navigateUp();
			}

		});

		am.put("navDown", new AbstractAction() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 6683017700153087856L;

			@Override
			public void actionPerformed(ActionEvent e) {
				navigateDown();
			}

		});

		am.put("newLine", new AbstractAction() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -7879264567501986325L;

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (currentLine + 1 != fileStr.size()){
					addLine("");
				}
				else if (fileStr.getLast() != ""){
					addLine("");
				}
			}

		});

		am.put("delLine", new AbstractAction() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -7593553052034429973L;

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				deleteLine();
			}

		});
	}

	/**
	 * Initializes and implements the JButtons that the users will interact
	 * with.
	 */
	protected static void addEditorButtons() {
		
		((JButton) compMap.get("testButton")).addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.removeAttribute(id.get(currentLine));
			}
			
		});
		
		// TODO Auto-generated method stub
		((JButton) compMap.get("insertText")).addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String temp = getInputText();
				int v = validString(temp);

				if (v == 0) {
					nullArgumentException();
				} else if (v == 1) {
					illegalArgumentException("string");
				} else if (v == 2) {
					addLine(temp);
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
			public void actionPerformed(ActionEvent e){
				if (currentLine == 0){
					controller.removeElement(id.getFirst());
					fileStr.removeFirst();
					id.removeFirst();
					controller.setAttribute(id.getFirst());
				}
				else{
					controller.removeElement(id.get(currentLine));
					fileStr.remove(currentLine);
					id.remove(currentLine);
					currentLine--;
					controller.setAttribute(id.get(currentLine));
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
	 * Initializes and implements the JMenuItems that the user will interact
	 * with.
	 */
	protected static void addActionListeners() {

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
				// TODO Auto-generated method stub

			}

		});

	}

	/**
	 * Initializes various objects associated with the JTextPanes. Calling this
	 * method resets the JTextPanes and the LinkedLists of strings and ID's.
	 */
	protected static void initializeComponents() {
		// TODO Auto-generated method stub
		fileStr = new LinkedList<String>();
		id = new LinkedList<Integer>();
		id.add(0);
		((JTextPane) compMap.get("scenarioPane")).setText("");
		((JTextPane) compMap.get("consolePane")).setText("");
		controller = new JTextPaneController((JTextPane) compMap.get("scenarioPane"),
				(JScrollPane) compMap.get("scenarioScrollPane"));
		consoleController = new JTextPaneController((JTextPane) compMap.get("consolePane"),
				(JScrollPane) compMap.get("consoleScrollPane"));
	}

	/**
	 * Enables the components when a scenario file is opened in the application.
	 */
	protected static void stateChanged() {
		if (isOpened) {
			// System.out.println(true);
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
		consoleController.addElement("NullArgumentException: No input detected. See \"Help\" for user manual.");
	}

	/**
	 * Prints into the console a illegalArgumentException
	 * 
	 * @param expected
	 *            the expected input types.
	 */
	public static void illegalArgumentException(String expected) {
		consoleController
				.addElement("IllegalArgumentException, Expected: " + expected + ". See \"Help\" for user manual.");
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
		consoleController.addElement("indexOutOfBoundsException: " + type + " value should have a range of " + range
				+ ". See \"Help\" for user manual.");
	}

	/**
	 * Checks for the minimum requirements of JTextField inputs. The string
	 * should not be empty and should not contain the token "/~".
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
		redoNode.clear();

		fileStr.add(currentLine + 1, temp);
		idCount++;
		id.add(currentLine + 1, idCount);
		controller.addElement(temp, id.get(currentLine), idCount);
		
		DataNode tempNode = new DataNode(id.get(currentLine), idCount, temp, currentLine + 1);
		undoNode.push(tempNode);
		
		controller.removeAttribute(id.get(currentLine));
		currentLine++;
		controller.setAttribute(id.get(currentLine));
		
	}
	
	protected static void performRedo() {
		if (!redoNode.isEmpty()){
			controller.removeAttribute(id.get(currentLine));
			DataNode tempNode = redoNode.pop();
			undoNode.push(tempNode);
			addLineAfter(tempNode.element, tempNode.currentLine, tempNode.currentID, tempNode.prevID);
		}
	}

	protected static void performUndo() {
		if (!undoNode.isEmpty()){
			controller.removeAttribute(id.get(currentLine));
			DataNode tempNode = undoNode.pop();
			redoNode.push(tempNode);
			deleteLine(tempNode.currentID, tempNode.currentLine);
		}
	}
	
	protected static void navigateUp() {
		System.out.println("nav up");
		if (currentLine == 0){
			controller.removeAttribute(id.getFirst());
			currentLine = id.size() - 1;
			controller.setAttribute(id.getLast());
		}
		else{
			controller.removeAttribute(id.get(currentLine));
			currentLine--;
			controller.setAttribute(id.get(currentLine));
		}
	}
	
	protected static void navigateDown() {
		System.out.println("nav down");
		if (currentLine + 1 == fileStr.size()){
			controller.removeAttribute(id.getLast());
			currentLine = 0;
			controller.setAttribute(id.getFirst());
		}
		else {
			controller.removeAttribute(id.get(currentLine));
			currentLine++;
			controller.setAttribute(id.get(currentLine));
		}
	}
	

	protected static void deleteLine() {
		// TODO Auto-generated method stub
		if (currentLine == 0){
			controller.removeElement(id.getFirst());
			fileStr.removeFirst();
			id.removeFirst();
			controller.setAttribute(id.getFirst());
		}
		else{
			controller.removeElement(id.get(currentLine));
			fileStr.remove(currentLine);
			id.remove(currentLine);
			currentLine--;
			controller.setAttribute(id.get(currentLine));
		}
	}
	
	public static void addLineAfter(String element, int curLine, int currentID, int prevID){
		fileStr.add(curLine, element);
		id.add(curLine, currentID);
		controller.addElement(element, prevID, currentID);
		
		currentLine = curLine;
		controller.setAttribute(id.get(currentLine));
		
	}
	
	public static void deleteLine(int currentID, int curLine){
		controller.removeElement(currentID);
		fileStr.remove(curLine);
		id.remove(curLine);
		currentLine = curLine - 1;
		controller.setAttribute(id.get(currentLine));
	}
}

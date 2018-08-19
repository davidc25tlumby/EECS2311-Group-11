package authoringApp;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import enamel.ToyAuthoring;

public class AuthoringApp extends AuthoringAppGUI {

	/**
	 * Contains the functionality of the App and the various interactions with
	 * GUI components.
	 * 
	 * @author Xiahan Chen, Huy Hoang Minh Cu, Qasim Mahir
	 */
	private static final long serialVersionUID = 1L;
	private static File f, currentFile;
	private static LinkedList<String> fileStr;
	private static LinkedList<Integer> id;
	private static JTextPaneController controller, consoleController;
	private static int idCount;
	private static String state;
	private static int currentLine = 0, cell = 0, col = 0;
	private static boolean isSaved = true, isOpened = false;
	private static boolean checkRecord = false;
	private static LinkedList<DataNode> undoNode = new LinkedList<DataNode>();
	private static LinkedList<DataNode> redoNode = new LinkedList<DataNode>();
	private static int lineFocus = 0, topPadding, bottomPadding;
	private static int TP_HEIGHT = 25;
	static Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	static String paste = null;
	private static int recordCounter = 0;
	private static boolean hasFileName = false;
	Timer t = null;
	AudioRecorder audio;

	/**
	 * Initializes the text panes and overrides default JFrame exit method.
	 */
	AuthoringApp() {
		this.setVisible(true);
		controller = new JTextPaneController(scenarioPane, scenarioScrollPane);
		consoleController = new JTextPaneController(consoleTextPane, consoleScrollPane);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (exit() == 1) {
					System.exit(0);
				}
			}
		});
		addListeners();
	}

	/**
	 * Adds key bindings for shortcuts.
	 */
	protected void addKeyBindings() {

		KeyStroke ctrlX = KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK);
		KeyStroke ctrlC = KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK);
		KeyStroke ctrlV = KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK);

		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ctrlX, "cut");
		getRootPane().getActionMap().put("cut", cutLine);

		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ctrlC, "copy");
		getRootPane().getActionMap().put("copy", copyLine);

		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ctrlV, "paste");
		getRootPane().getActionMap().put("paste", pasteLine);

		scenarioPane.getInputMap(JComponent.WHEN_FOCUSED).put(ctrlX, "cut");
		scenarioPane.getActionMap().put("cut", cutLine);

		scenarioPane.getInputMap(JComponent.WHEN_FOCUSED).put(ctrlC, "copy");
		scenarioPane.getActionMap().put("copy", copyLine);

		scenarioPane.getInputMap(JComponent.WHEN_FOCUSED).put(ctrlV, "paste");
		scenarioPane.getActionMap().put("paste", pasteLine);

		consoleTextPane.getInputMap(JComponent.WHEN_FOCUSED).put(ctrlX, "cut");
		consoleTextPane.getActionMap().put("cut", cutLine);

		consoleTextPane.getInputMap(JComponent.WHEN_FOCUSED).put(ctrlC, "copy");
		consoleTextPane.getActionMap().put("copy", copyLine);

		consoleTextPane.getInputMap(JComponent.WHEN_FOCUSED).put(ctrlV, "paste");
		consoleTextPane.getActionMap().put("paste", pasteLine);

		inputTextField.getInputMap(JComponent.WHEN_FOCUSED).put(ctrlX, "cut");
		inputTextField.getActionMap().put("cut", cutLine);

		inputTextField.getInputMap(JComponent.WHEN_FOCUSED).put(ctrlC, "copy");
		inputTextField.getActionMap().put("copy", copyLine);

		inputTextField.getInputMap(JComponent.WHEN_FOCUSED).put(ctrlV, "paste");
		inputTextField.getActionMap().put("paste", pasteLine);

		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("UP"), "navUp");
		getRootPane().getActionMap().put("navUp", navigateUp);

		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("DOWN"), "navDown");
		getRootPane().getActionMap().put("navDown", navigateDown);

		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ENTER"), "newLine");
		getRootPane().getActionMap().put("newLine", newLine);

		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("DELETE"), "delLine");
		getRootPane().getActionMap().put("delLine", deleteLine);

		scenarioPane.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("UP"), "navUp");
		scenarioPane.getActionMap().put("navUp", navigateUp);

		scenarioPane.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("DOWN"), "navDown");
		scenarioPane.getActionMap().put("navDown", navigateDown);

		scenarioPane.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("ENTER"), "newLine");
		scenarioPane.getActionMap().put("newLine", newLine);

		scenarioPane.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("DELETE"), "delLine");
		scenarioPane.getActionMap().put("delLine", deleteLine);

		consoleTextPane.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("UP"), "navUp");
		consoleTextPane.getActionMap().put("navUp", navigateUp);

		consoleTextPane.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("DOWN"), "navDown");
		consoleTextPane.getActionMap().put("navDown", navigateDown);

		consoleTextPane.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("ENTER"), "newLine");
		consoleTextPane.getActionMap().put("newLine", newLine);

		consoleTextPane.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("DELETE"), "delLine");
		consoleTextPane.getActionMap().put("delLine", deleteLine);

		inputTextField.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("UP"), "navUp");
		inputTextField.getActionMap().put("navUp", navigateUp);

		inputTextField.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("DOWN"), "navDown");
		inputTextField.getActionMap().put("navDown", navigateDown);

		inputTextField.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("ENTER"), "newLine");
		inputTextField.getActionMap().put("newLine", newLine);

		inputTextField.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("DELETE"), "delLine");
		inputTextField.getActionMap().put("delLine", deleteLine);
	}

	/**
	 * Implementation of action listeners for various components.
	 */
	protected void addListeners() {

		insertText.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				addLine(getInputText());
			}

		});

		insertPause.addActionListener(new ActionListener() {

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

		insertSkipButton.addActionListener(new ActionListener() {

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

		insertSkip.addActionListener(new ActionListener() {

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

		insertUserInput.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				addLine("/~user-input");

			}

		});

		insertRepeatButton.addActionListener(new ActionListener() {

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

		insertRepeat.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addLine("/~repeat");
			}

		});

		insertEndRepeat.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addLine("/~endrepeat");
			}

		});

		insertResetButtons.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addLine("/~reset-buttons");
			}
		});

		insertSound.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				f = fileChooser.openFileChooser(new File("FactoryScenarios/AudioFiles"), "wav");
				if (f != null) {
					addLine("/~sound:" + f.getName());
				}
			}
		});

		editRemoveLine.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				deleteLine();
			}
		});

		displayAddButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String cbStr = (String) displayComboBox.getItemAt(displayComboBox.getSelectedIndex());
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

		super.addComponentListener(new ComponentListener() {

			@Override
			public void componentHidden(ComponentEvent arg0) {
				// unused
			}

			@Override
			public void componentMoved(ComponentEvent arg0) {
				// unused
			}

			@Override
			public void componentResized(ComponentEvent arg0) {
				if (isOpened) {
					state = "RESIZE";
					Rectangle rectangle = scenarioScrollPane.getVisibleRect();
					int height = rectangle.height;
					TP_HEIGHT = height / 19;
					refocus();
				}
			}

			@Override
			public void componentShown(ComponentEvent arg0) {
				// unused
			}

		});

		newMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				int n = 0;
				if (!isSaved) {
					n = optionPane("Save", "Save and/or continue?", JOptionPane.YES_NO_CANCEL_OPTION);
					if (n == 0) {
						if (hasFileName) {
							save();
						} else {
							saveAs();
						}
					}
				}
				if (isSaved || n == 1) {
					NewFileGUI temp = new NewFileGUI();
					temp.setVisible(true);
					JTextField numCell = temp.getNumCell();
					JTextField numCol = temp.getNumCol();

					temp.getCreateButton().addActionListener(new ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent evt) {

							initializeComponents();
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
								inputTextField.setText("");
								inputTextField.setForeground(Color.BLACK);
								id = controller.newDocCreated(fileStr);
								idCount = id.getLast();
								currentLine = 0;
								controller.setAttribute(id.get(currentLine));
								controller.addElement("", Integer.toString(id.getLast()), -2);
								fileStr.addLast("");
								id.add(-2);
								setTitle("Authoring App - Untitled.txt");
								hasFileName = false;
								temp.dispose();
							} else {
								optionPane("Warning", "Invalid input", JOptionPane.DEFAULT_OPTION);
							}
						}

					});

					temp.getCancelButton().addActionListener(new ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent evt) {
							isOpened = false;
							temp.dispose();
						}
					});
				}

			}

		});
		loadScenarioMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int n = 0;
				if (!isSaved) {
					n = optionPane("Save", "Save and/or continue?", JOptionPane.YES_NO_CANCEL_OPTION);
					if (n == 0) {
						if (hasFileName) {
							save();
						} else {
							saveAs();
						}
					}
				}
				if (isSaved || n == 1) {
					load();
				}
			}
		});

		saveMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (hasFileName) {
					save();
				} else {
					saveAs();
				}
			}

		});

		cutMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cutLine();
			}
		});

		saveAsMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				saveAs();
			}

		});
		loadAndRunMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				int n = 0;
				if (!isSaved) {
					n = optionPane("Save", "Save and/or continue?", JOptionPane.YES_NO_CANCEL_OPTION);
					if (n == 0) {
						if (hasFileName) {
							save();
						} else {
							saveAs();
						}
					}
				}
				if (isSaved || n == 1) {
					load();
					runScenario();
				}
			}

		});

		runMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (!isSaved) {
					int n = optionPane("Save", "Save and continue?", JOptionPane.YES_NO_OPTION);
					if (n == 0) {
						if (hasFileName) {
							save();
						} else {
							saveAs();
						}
					}
				}
				if (isSaved) {
					runScenario();
				}
			}

		});

		exitMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (exit() == 1) {
					System.exit(0);
				}
			}

		});

		undoMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				performUndo();
			}

		});

		redoMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				performRedo();
			}

		});

		userManualMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					Desktop desktop = java.awt.Desktop.getDesktop();
					URI uri = new URI("http://www.teamlumby.com");
					desktop.browse(uri);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		});

		aboutMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				optionPane("About", "v1.0. Downloadable resources at http://www.teamlumby.com",
						JOptionPane.DEFAULT_OPTION);
			}

		});

	}

	/**
	 * Checks if file has been saved when exit command as been requested.
	 * 
	 * @return 0 if file hasn't been saved, 1 otherwise.
	 */
	protected int exit() {
		int n = 0;
		if (!isSaved) {
			n = optionPane("Save", "Save and exit?", JOptionPane.YES_NO_CANCEL_OPTION);
			if (n == 0) {
				if (hasFileName) {
					save();
				} else {
					saveAs();
				}
			}
		}
		if (isSaved || n == 1) {
			return 1;
		}
		return 0;
	}

	/**
	 * Runs scenario in ToyAuthoring of enamel package.
	 */
	protected void runScenario() {
		ToyAuthoring ta = new ToyAuthoring(f.getAbsolutePath());
		ta.start();
	}

	/**
	 * Loads a new scenario into the text pane.
	 */
	protected void load() {
		try {
			state = "NEW";
			f = fileChooser.openFileChooser(new File("FactoryScenarios/"), "txt");
			if (f != null) {
				initializeComponents();
				currentFile = f;
				setTitle("Authoring App - " + currentFile.getName());
				hasFileName = true;
				FileParser fp = new FileParser(f);
				fileStr = fp.getArray();
				id = controller.newDocCreated(fileStr);
				idCount = id.getLast();
				currentLine = 0;
				controller.setAttribute(id.get(currentLine));
				controller.addElement("", Integer.toString(id.getLast()), -2);
				fileStr.addLast("");
				id.add(-2);
				isSaved = true;
				isOpened = true;
				inputTextField.setText("");
				stateChanged();
			}

		} catch (IOException e1) {
			e1.printStackTrace();

		}
	}

	/**
	 * Opens a fileChooser so a name can be entered for the file.
	 * 
	 * @return true if the file has been saved, otherwise false.
	 */
	protected boolean saveAs() {
		f = fileChooser.saveFileChooser(new File("FactoryScenarios/"), "txt");
		if (f != null) {
			currentFile = f;
			setTitle("Authoring App - " + currentFile.getName());
			hasFileName = true;
			SaveAsFile save = new SaveAsFile("txt", new File(currentFile.getAbsolutePath()));
			try {
				save.stringArrayToFile(fileStr);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			isSaved = true;
			return true;
		}
		return false;
	}

	/**
	 * Saves the current scenario as the current file name.
	 */
	protected void save() {
		setTitle("Authoring App - " + currentFile.getName());
		SaveAsFile save = new SaveAsFile("txt", new File(currentFile.getAbsolutePath()));
		try {
			save.stringArrayToFile(fileStr);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		isSaved = true;
	}

	/**
	 * Readjusts the text pane when the dimensions of the JFrame has changed
	 * such that the current line is still in focus.
	 */
	protected void refocus() {
		switch (state) {
		case "ADD_LINE":
			if (topPadding < TP_HEIGHT - 1) {
				topPadding++;
				bottomPadding--;
			} else {
				lineFocus += 19;
			}
			break;
		case "DELETE_LINE":
			if (topPadding > 0) {
				topPadding--;
				bottomPadding++;
			} else {
				if (currentLine != 0) {
					lineFocus -= 19;
				}
			}
			break;
		default:
			if (currentLine < TP_HEIGHT) {
				topPadding = currentLine;
				bottomPadding = TP_HEIGHT - 1 - topPadding;
				lineFocus = 0;
			} else if (currentLine > fileStr.size() - TP_HEIGHT) {
				bottomPadding = fileStr.size() - 2 - currentLine;
				topPadding = TP_HEIGHT - bottomPadding - 1;
				lineFocus = (fileStr.size() - TP_HEIGHT - 1) * 19;
			} else {
				topPadding = 0;
				bottomPadding = TP_HEIGHT - 1 - topPadding;
				lineFocus = currentLine * 19;
			}
		}
		scenarioScrollPane.getVerticalScrollBar().setValue(lineFocus);
	}

	/**
	 * Initializes and/or resets values for variables and components used by the
	 * scenario.
	 */
	protected void initializeComponents() {
		if (!isOpened) {
			addKeyBindings();

			scenarioPane.addComponentListener(new ComponentListener() {

				@Override
				public void componentHidden(ComponentEvent e) {
					// unused
				}

				@Override
				public void componentMoved(ComponentEvent e) {
					// unused
				}

				@Override
				public void componentResized(ComponentEvent e) {
					if (isOpened) {
						refocus();
					}
				}

				@Override
				public void componentShown(ComponentEvent e) {
					// unused
				}

			});

			scenarioPane.addMouseWheelListener(new MouseWheelListener() {

				@Override
				public void mouseWheelMoved(MouseWheelEvent e) {
					if (e.getWheelRotation() < 0) {
						navigateUp();
					} else if (e.getWheelRotation() > 0) {
						navigateDown();
					}
				}

			});
		}
		fileStr = new LinkedList<String>();
		id = new LinkedList<Integer>();
		id.add(0);
		scenarioPane.setText("");
		consoleTextPane.setText("");
		controller = new JTextPaneController(scenarioPane, scenarioScrollPane);
		consoleController = new JTextPaneController(consoleTextPane, consoleScrollPane);
		inputTextField.requestFocus();
		topPadding = 0;
		bottomPadding = TP_HEIGHT - 1;
	}

	/**
	 * Enables all buttons.
	 */
	protected void stateChanged() {
		if (isOpened) {
			saveMenuItem.setEnabled(true);
			saveAsMenuItem.setEnabled(true);
			insertText.setEnabled(true);
			insertPause.setEnabled(true);
			insertSkipButton.setEnabled(true);
			insertSkip.setEnabled(true);
			insertUserInput.setEnabled(true);
			insertRepeatButton.setEnabled(true);
			insertRepeat.setEnabled(true);
			insertEndRepeat.setEnabled(true);
			insertResetButtons.setEnabled(true);
			insertSound.setEnabled(true);
			displayComboBox.setEnabled(true);
			displayAddButton.setEnabled(true);
			editRemoveLine.setEnabled(true);
			undoMenuItem.setEnabled(true);
			redoMenuItem.setEnabled(true);
			cutMenuItem.setEnabled(true);
			copyMenuItem.setEnabled(true);
			pasteMenuItem.setEnabled(true);
			runMenuItem.setEnabled(true);

			recordButton.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if (!checkRecord) {
						checkRecord = true;
						recordButton.setIcon(stopImg);
						playButton.setIcon(playDisabledImg);
						audio = new AudioRecorder();
						audio.startRecording();
						t = new Timer();
						recordCounter = 0;
						printInConsole("Start recording");
						t.scheduleAtFixedRate(new TimerTask() {
							@Override
							public void run() {
								printRecordTime();
							}

							private void printRecordTime() {
								recordCounter++;
								printInConsole("Recording....." + recordCounter);
								;
							}
						}, 0, 1000);
					} else {
						checkRecord = false;
						audio.stopRecording();
						printInConsole("Recording finished");
						recordButton.setIcon(recordImg);
						playButton.setIcon(playImg);
						t.cancel();
						t.purge();
						int n = optionPane("Save", "Save recording?", JOptionPane.YES_NO_OPTION);
						if (n == 0) {
							File f = fileChooser.saveFileChooser(new File("FactoryScenarios/AudioFiles"), "wav");
							if (f != null) {
								audio.writeSoundFile(f);
							}
						}
					}
				}
			});

			playButton.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					File sound = fileChooser.openFileChooser(new File("FactoryScenarios/AudioFiles"), "wav");
					if (sound != null) {
						AudioPlayback ap = new AudioPlayback(sound);
						ap.play();
					}
				}
			});

			recordButton.setIcon(recordImg);
			playButton.setIcon(playImg);
		}
	}

	/**
	 * @return Text within the JTextField.
	 */
	public String getInputText() {
		return inputTextField.getText();
	}

	/**
	 * Prints a NullArgumentException in the app console.
	 */
	public void nullArgumentException() {
		printInConsole("NullArgumentException: No input detected. See \"Help\" for user manual.");
		inputTextField.requestFocus();
		inputTextField.setText("");
	}

	/**
	 * Prints an IllegalArgumentException in the app console.
	 * 
	 * @param expected
	 *            The expected input.
	 */
	public void illegalArgumentException(String expected) {
		printInConsole("IllegalArgumentException, Expected: " + expected + ". See \"Help\" for user manual.");
		inputTextField.requestFocus();
		inputTextField.setText("");
	}

	/**
	 * Prints an IndexOutOfBoundsException in the app console.
	 * 
	 * @param type
	 *            The type of variable for the scenario.
	 */
	public void indexOutOfBoundsException(String type) {
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
		inputTextField.requestFocus();
		inputTextField.setText("");
	}

	/**
	 * 
	 * @param s
	 *            The string to print in the app console.
	 */
	private void printInConsole(String s) {
		consoleController.addElement(s);
		JScrollBar vertical = consoleScrollPane.getVerticalScrollBar();
		vertical.setValue(vertical.getMaximum());
	}

	/**
	 * 
	 * @param s
	 *            The string to check
	 * @return 0 if the string is empty, 1 if a keyphrase modifier exists, 2 if
	 *         valid.
	 */
	public int validString(String s) {
		if (s.isEmpty()) {
			return 0;
		} else if (s.contains("/~")) {
			return 1;
		} else {
			return 2;
		}
	}

	/**
	 * 
	 * @param s
	 *            The number (as a string) to check.
	 * @return True if numeric, false otherwise.
	 */
	public boolean isNumeric(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	/**
	 * Checks if a given string is of length 8 consisting of only 0s or 1s.
	 * 
	 * @param s
	 *            The binary string to check.
	 * @return True if the string is binary, false otherwise.
	 */
	public boolean isBinaryChar(String s) {
		if (s.length() != 8 || !s.matches("[01]+")) {
			return false;
		}
		return true;
	}

	/**
	 * Checks if a given string is only one character which is a letter.
	 * 
	 * @param s
	 *            The string to check for char.
	 * @return True if the string is a letter character, false otherwise.
	 */
	public boolean isChar(String s) {
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

	/**
	 * Adds a string at the current indexed line of the scenario.
	 * 
	 * @param temp
	 *            The string to append to the scenario.
	 */
	public void addLine(String temp) {
		isSaved = false;
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
		inputTextField.requestFocus();
		inputTextField.setText("");
	}

	/**
	 * Redo last scenario change.
	 */
	protected void performRedo() {
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

	/**
	 * Undo last scenario change.
	 */
	protected void performUndo() {
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

	/**
	 * Removes the current line of the scenario while saving it to the
	 * clipboard.
	 */
	protected void cutLine() {
		StringSelection ss = new StringSelection(fileStr.get(currentLine));
		clipboard.setContents(ss, ss);
		deleteLine();
	}

	/**
	 * Saves the current line of the scenario to the clipboard.
	 */
	protected void copyLine() {
		StringSelection ss = new StringSelection(fileStr.get(currentLine));
		clipboard.setContents(ss, ss);
	}

	/**
	 * Paste the contents of the clipboard to the current line of the scenario.
	 */
	protected void pasteLine() {
		try {
			paste = (String) clipboard.getContents(null).getTransferData(DataFlavor.stringFlavor);
		} catch (UnsupportedFlavorException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		addLine(paste);
	}

	/**
	 * Moves the highlighter up in the text pane.
	 */
	protected void navigateUp() {
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

	/**
	 * Moves the highlighter down in the text pane.
	 */
	protected void navigateDown() {
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

	/**
	 * Sets the scroll bar position for the scenario text pane.
	 * 
	 * @param state
	 *            The change to the scroll bar. 0 = Move up, 1 = Move down, 2 =
	 *            Move to bottom, 3 = Move to top.
	 */
	private void setScrollBarValue(int state) {
		// move down
		if (state == 0) {
			if (topPadding > 0) {
				topPadding--;
				bottomPadding++;
			} else {
				lineFocus -= 19;
			}
		}
		// move up
		else if (state == 1) {
			if (bottomPadding > 0) {
				bottomPadding--;
				topPadding++;
			} else {
				lineFocus += 19;
			}
		}
		// move to bottom
		else if (state == 2) {
			topPadding = TP_HEIGHT - 1;
			bottomPadding = 0;
			lineFocus = (fileStr.size() - TP_HEIGHT - 1) * 19;
		}
		// move to top
		else if (state == 3) {
			topPadding = 0;
			bottomPadding = TP_HEIGHT - 1;
			lineFocus = 0;
		}
		scenarioScrollPane.getVerticalScrollBar().setValue(lineFocus);
	}

	/**
	 * Deletes the current line of the scenario while pushing that line onto the
	 * stack of undo nodes.
	 */
	protected void deleteLine() {
		isSaved = false;
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

	/**
	 * Adds a line to the scenario.
	 * 
	 * @param element
	 *            The string to append in valid HTML format.
	 * @param curLine
	 *            The new value for the currentLine.
	 * @param currentID
	 *            The ID for the new line being appended.
	 * @param prevID
	 *            The ID of the line directly before the new line.
	 */
	public void addLineAfter(String element, int curLine, int currentID, int prevID) {
		isSaved = false;
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

	/**
	 * Deletes a line from the scenario.
	 * 
	 * @param currentID
	 *            The ID of the line being deleted.
	 * @param curLine
	 *            The position of the line that is being deleted.
	 */
	public void deleteLine(int currentID, int curLine) {
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

	/**
	 * Appends the text field string into the scenario.
	 */
	protected void newLine() {
		addLine(getInputText());
	}

	Action cutLine = new AbstractAction() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			cutLine();
		}
	};

	Action copyLine = new AbstractAction() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			copyLine();
		}
	};

	Action pasteLine = new AbstractAction() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			pasteLine();
		}
	};

	Action navigateUp = new AbstractAction() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			navigateUp();
		}
	};

	Action navigateDown = new AbstractAction() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			navigateDown();
		}
	};

	Action newLine = new AbstractAction() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			newLine();
		}
	};

	Action deleteLine = new AbstractAction() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			deleteLine();
		}
	};

	/**
	 * Opens a JOptionPane.
	 * 
	 * @param sMsg
	 *            The message to display for the user.
	 * @param sTitle
	 *            The title of the JFrame.
	 * @param optionType
	 *            The type of JOptionPane e.g. JOptionPane.YES_NO_CANCEL_OPTION.
	 * @return
	 */
	private int optionPane(String sMsg, String sTitle, int optionType) {
		int operation = JOptionPane.showConfirmDialog(null, sTitle, sMsg, optionType);
		return operation;
	}

}

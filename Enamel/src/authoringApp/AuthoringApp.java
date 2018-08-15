package authoringApp;

import java.awt.Color;
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
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static File f, currentFile;// , error;
	private static LinkedList<String> fileStr;// , consoleStr;
	private static LinkedList<Integer> id;
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
	static Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	static String paste = null;
	private static int recordCounter = 0;
	private static boolean hasFileName = false;
	Timer t = null;

	AuthoringApp() {
		this.setVisible(true);
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				if (exit() == 1){
					System.exit(0);
				}
			}
		});
		addListeners();
	}

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

	protected void addListeners() {

		jButton1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				saveOptionPane("nice", "nice", JOptionPane.YES_NO_CANCEL_OPTION);
			}

		});

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
				if (!isSaved){
					n = saveOptionPane("Save", "Save and/or continue?", JOptionPane.YES_NO_CANCEL_OPTION);
					if (n == 0){
						if (hasFileName) {
							save();
						} else {
							saveAs();
						}
					}
				}
				if (isSaved || n == 1){
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
								// throw invalid input
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
				if (!isSaved){
					n = saveOptionPane("Save", "Save and/or continue?", JOptionPane.YES_NO_CANCEL_OPTION);
					if (n == 0){
						if (hasFileName) {
							save();
						} else {
							saveAs();
						}
					}
				}
				if (isSaved || n == 1){
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
				if (!isSaved){
					n = saveOptionPane("Save", "Save and/or continue?", JOptionPane.YES_NO_CANCEL_OPTION);
					if (n == 0){
						if (hasFileName) {
							save();
						} else {
							saveAs();
						}
					}
				}
				if (isSaved || n == 1){
					load();
					runScenario();
				}
			}

		});

		runMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (!isSaved){
					int n = saveOptionPane("Save", "Save and continue?", JOptionPane.YES_NO_OPTION);
					if (n == 0){
						if (hasFileName) {
							save();
						} else {
							saveAs();
						}
					}
				}
				if (isSaved){
					runScenario();
				}
			}

		});

		exitMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (exit() == 1){
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


	}

	protected int exit() {
		// TODO Auto-generated method stub
		int n = 0;
		if (!isSaved){
			n = saveOptionPane("Save", "Save and exit?", JOptionPane.YES_NO_CANCEL_OPTION);
			if (n == 0){
				if (hasFileName) {
					save();
				} else {
					saveAs();
				}
			}
		}
		if (isSaved || n == 1){
			return 1;
		}
		return 0;
	}

	protected void runScenario() {
		// TODO Auto-generated method stub
			ToyAuthoring ta = new ToyAuthoring(f.getAbsolutePath());
			ta.start();
	}

	protected void load() {
		// TODO Auto-generated method stub
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
					System.out.println(id.get(currentLine));
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

	protected boolean saveAs() {
		// TODO Auto-generated method stub
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

	protected void save() {
		// TODO Auto-generated method stub
		setTitle("Authoring App - " + currentFile.getName());
		SaveAsFile save = new SaveAsFile("txt", new File(currentFile.getAbsolutePath()));
		try {
			save.stringArrayToFile(fileStr);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		isSaved = true;
	}

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

	protected void initializeComponents() {
		addKeyBindings();
		fileStr = new LinkedList<String>();
		id = new LinkedList<Integer>();
		id.add(0);
		scenarioPane.setText("");
		consoleTextPane.setText("");
		controller = new JTextPaneController(scenarioPane, scenarioScrollPane);
		consoleController = new JTextPaneController(consoleTextPane, consoleScrollPane);

		scenarioPane.addMouseWheelListener(new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				// TODO Auto-generated method stub
				// System.out.println(arg0.getWheelRotation());
				if (e.getWheelRotation() < 0) {
					navigateUp();
				} else if (e.getWheelRotation() > 0) {
					navigateDown();
				}
			}

		});

		inputTextField.requestFocus();
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
		topPadding = 0;
		bottomPadding = TP_HEIGHT - 1;
	}

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
						recorder = new AudioRecorder();
						recorder.start();
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
						recorder.finish();
						printInConsole("Recording finished");
						recordButton.setIcon(recordImg);
						t.cancel();
						t.purge();
					}
				}
			});

			playButton.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if (!checkRecord) {
						System.out.println("record");
						checkRecord = true;
					} else {
						System.out.println("stop");
						checkRecord = false;
					}
				}
			});
			
			recordButton.setIcon(recordImg);
			playButton.setIcon(playImg);
		}
	}

	public String getInputText() {
		return inputTextField.getText();
	}

	public void nullArgumentException() {
		printInConsole("NullArgumentException: No input detected. See \"Help\" for user manual.");
		inputTextField.requestFocus();
		inputTextField.setText("");
	}

	public void illegalArgumentException(String expected) {
		printInConsole("IllegalArgumentException, Expected: " + expected + ". See \"Help\" for user manual.");
		inputTextField.requestFocus();
		inputTextField.setText("");
	}

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

	private void printInConsole(String s) {
		// TODO Auto-generated method stub
		consoleController.addElement(s);
		JScrollBar vertical = consoleScrollPane.getVerticalScrollBar();
		vertical.setValue(vertical.getMaximum());
	}

	public int validString(String s) {
		if (s.isEmpty()) {
			return 0;
		} else if (s.contains("/~")) {
			return 1;
		} else {
			return 2;
		}
	}

	public boolean validFileFormat(String ext) {
		if (ext.contains("ext")) {
			return true;
		}
		return false;
	}

	public boolean isNumeric(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	public boolean isBinaryChar(String s) {
		if (s.length() != 8 || !s.matches("[01]+")) {
			return false;
		}
		return true;
	}

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

	protected void cutLine() {
		/*
		 * try { paste = (String)
		 * clipboard.getContents(null).getTransferData(DataFlavor.stringFlavor);
		 * } catch (UnsupportedFlavorException e) { // TODO Auto-generated catch
		 * block e.printStackTrace(); } catch (IOException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 * System.out.println(paste);
		 */
		StringSelection ss = new StringSelection(fileStr.get(currentLine));
		clipboard.setContents(ss, ss);
		deleteLine();
	}

	protected void copyLine() {
		StringSelection ss = new StringSelection(fileStr.get(currentLine));
		clipboard.setContents(ss, ss);
	}

	protected void pasteLine() {
		System.out.println("paste line");
		try {
			paste = (String) clipboard.getContents(null).getTransferData(DataFlavor.stringFlavor);
		} catch (UnsupportedFlavorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		addLine(paste);
	}

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

	private int saveOptionPane(String sMsg, String sTitle, int optionType) {
		int operation = JOptionPane.showConfirmDialog(null, sTitle, sMsg, optionType);
		/*if (operation == 0) {
			if (hasFileName){
				save();
			}
			else{
				boolean b = saveAs();
				if (b){
					return 0;
				}
			}
			return operation;
		} else {
			return operation;*/
		return operation;
		
	}

}

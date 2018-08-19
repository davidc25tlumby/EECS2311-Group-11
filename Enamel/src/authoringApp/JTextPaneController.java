package authoringApp;

import java.awt.Color;
import java.io.IOException;
import java.util.LinkedList;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleContext;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.StyleConstants;

/**
 * Extension of JTextPane implementing features to add HTML DOM format and CSS
 * styling.
 * 
 * @author Xiahan Chen, Huy Hoang Minh Cu, Qasim Mahir
 *
 */
public class JTextPaneController extends JTextPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4947922282601680749L;
	private JTextPane tp;
	private JScrollPane sp;

	HTMLDocument doc;
	Element e;
	AttributeSet highlight, white;

	/**
	 * Initializes the JTextPaneControllers and sets the JTextPane to display
	 * defaultHTML.html.
	 * 
	 * @param tp
	 *            The JTextPane from the GUI.
	 * @param sp
	 *            The JScrollPane from the GUI.
	 */
	public JTextPaneController(JTextPane tp, JScrollPane sp) {
		this.tp = tp;
		this.sp = sp;
		try {
			tp.setPage(getClass().getResource("defaultHTML.html"));
			doc = (HTMLDocument) tp.getDocument();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		StyleContext scHighlight = StyleContext.getDefaultStyleContext();
		StyleContext scWhite = StyleContext.getDefaultStyleContext();
		highlight = scHighlight.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.WHITE);
		highlight = scHighlight.addAttributes(highlight,
				scHighlight.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Background, Color.BLUE));
		white = scWhite.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.BLACK);
		white = scWhite.addAttributes(white,
				scWhite.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Background, Color.WHITE));
	}

	/**
	 * Populates the lines of the JTextPane using the strings from a scenario
	 * file.
	 * 
	 * @param fileStr
	 *            A linked List with each element representing a line of the
	 *            scenario file.
	 * @return LinkedList n - A linked list of integer IDs within the HTML file.
	 */
	public LinkedList<Integer> newDocCreated(LinkedList<String> fileStr) {

		doc = (HTMLDocument) tp.getDocument();
		LinkedList<Integer> n = new LinkedList<Integer>();
		if (fileStr.size() > 0) {
			e = doc.getElement("main");
			try {
				doc.insertAfterStart(e, "<p id=\"0\">" + fileStr.get(0) + "</p>");
				n.add(0);
			} catch (BadLocationException | IOException e1) {
				e1.printStackTrace();
			}
		}
		for (int i = 1; i < fileStr.size(); i++) {
			e = doc.getElement(new Integer(i - 1).toString());
			try {
				doc.insertAfterEnd(e, "<p id=\"" + i + "\">" + fileStr.get(i) + "</p>");
				n.add(i);
			} catch (BadLocationException | IOException e1) {
				e1.printStackTrace();
			}

		}
		return n;
	}

	/**
	 * Adds an Element with it's string after the specified element.
	 * 
	 * @param temp
	 *            The String to be inserted.
	 * @param i
	 *            The ID to be inserted after.
	 * @param j
	 *            the ID of element being added.
	 */
	public void addElement(String temp, String i, int j) {
		e = doc.getElement(i);
		if (i == "main") {
			try {
				doc.insertAfterStart(e, "<p id=\"" + j + "\">" + temp + "</p>");
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} else {
			try {
				doc.insertAfterEnd(e, "<p id=\"" + j + "\">" + temp + "</p>");
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * Adds an element to the end of the document.
	 * 
	 * @param temp
	 *            The element to append.
	 */
	public void addElement(String temp) {
		e = doc.getElement("main");

		try {
			doc.insertBeforeEnd(e, "<p>" + temp + "</p>");
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	/**
	 * Removes an element.
	 * 
	 * @param i
	 *            The ID of the element to remove.
	 */
	public void removeElement(int i) {
		e = doc.getElement(new Integer(i).toString());
		doc.removeElement(e);
	}

	/**
	 * Removes the attributes of an element.
	 * 
	 * @param i
	 *            The ID of the element which attributes will be removed.
	 */
	public void removeAttribute(int i) {
		e = doc.getElement(new Integer(i).toString());
		doc.setParagraphAttributes(e.getStartOffset(), 0, white, false);
	}

	/**
	 * Highlights an element.
	 * 
	 * @param i
	 *            The ID of the element to highlight.
	 */
	public void setAttribute(int i) {
		e = doc.getElement(new Integer(i).toString());
		doc.setParagraphAttributes(e.getStartOffset(), 0, highlight, false);
	}

	/**
	 * For debugging. Prints the HTML document in the console.
	 */
	public void printText() {
		System.out.println(tp.getText());
	}

	/**
	 * 
	 * @param id
	 *            The ID of the element to retrieve.
	 * @return The Element identified by the ID.
	 * @throws BadLocationException
	 */
	public Element getElement(String id) throws BadLocationException {
		return doc.getElement(id);
	}
}

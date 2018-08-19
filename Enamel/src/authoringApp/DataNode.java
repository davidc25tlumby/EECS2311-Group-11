package authoringApp;

/**
 * A Node that contains data for undo/redo methods.
 * 
 * @author Xiahan Chen, Huy Hoang Minh Cu, Qasim Mahir
 */
public class DataNode {
	int prevID, currentID, currentLine;
	String element;
	boolean adding;

	/**
	 * Constructor.
	 * 
	 * @param prevID
	 *            ID directly before line being saved as a node.
	 * @param currentID
	 *            ID of line being saved as a node.
	 * @param element
	 *            String of the line.
	 * @param currentLine
	 *            Current position of line.
	 * @param adding
	 *            True if the node represents a new line being added, false if
	 *            it represents deleting a line.
	 */
	DataNode(int prevID, int currentID, String element, int currentLine, boolean adding) {
		this.prevID = prevID;
		this.element = element;
		this.currentID = currentID;
		this.currentLine = currentLine;
		this.adding = adding;
	}
}

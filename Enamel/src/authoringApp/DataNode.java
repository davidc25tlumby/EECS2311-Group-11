package authoringApp;

public class DataNode {
	int prevID, currentID, currentLine;
	String element;
	boolean adding;
	
	DataNode(int prevID, int currentID, String element, int currentLine, boolean adding){
		this.prevID = prevID;
		this.element = element;
		this.currentID = currentID;
		this. currentLine = currentLine;
		this.adding = adding;
	}
}

package authoringApp;

public class DataNode {
	int prevID, currentID, currentLine;
	String element;
	
	DataNode(int prevID, int currentID, String element, int currentLine){
		this.prevID = prevID;
		this.element = element;
		this.currentID = currentID;
		this. currentLine = currentLine;
	}
}

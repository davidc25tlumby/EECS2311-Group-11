package authoringApp;

public class SaveProtocol extends AuthoringApp{
	
	String message;
	
	SaveProtocol(String message){
		this.message = message;
	}
	
	public boolean save(boolean save){
		return save;
	}
}

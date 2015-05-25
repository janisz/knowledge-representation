package pl.edu.pw.mini.msi.knowledgerepresentation.controller.event;
/**
 * 
 * Class represents the following event: User has entered text.  
 * 
 * @author maq
 * 
 */
public class LineEnteredEvent extends Event{
	
	private String line;
	
	public LineEnteredEvent(String linel) {
		this.line = linel;
	}
	
	public String getLine() {
		return line;
	}
	
}	



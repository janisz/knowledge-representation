package pl.edu.pw.mini.msi.knowledgerepresentation.controller.event;
/**
 * 
 * @author maq
 *
 */
public class LoadFileEvent extends Event{
	
	private String filePath;
	
	public LoadFileEvent(String filePath) {
		this.filePath = filePath;
	}
	
	public String getFilePath() {
		return filePath;
	}
	
}

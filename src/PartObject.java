import java.awt.Rectangle;

public class PartObject {
	
	private String partName;
	private int partLevel;
	private Rectangle partArea;
	
	public PartObject(String partName, int partLevel, Rectangle partArea) {
		
		this.partName = partName;
		this.partLevel = partLevel;
		this.partArea = partArea;
	}
	
	public String getPartName() {
		
		return partName;
	}
	
	public int partLevel() {
		
		return partLevel;
	}
	
	public Rectangle partArea() {
		
		return partArea;
	}
}

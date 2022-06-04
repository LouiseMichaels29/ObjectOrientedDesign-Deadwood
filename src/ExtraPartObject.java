import java.awt.Rectangle;

public class ExtraPartObject {
	
	private int level;
	private String name;
	private String setName;
	private Rectangle partArea;
	
	public ExtraPartObject(String setName, String name, int level, Rectangle partArea) {
		
		this.level = level;
		this.name = name;
		this.setName = setName;
		this.partArea = partArea;
	}
	
	public String getName() {
		
		return name;
	}
	
	public String getSetName() {
		
		return setName;
	}
	
	public int getLevel() {
		
		return level;
	}
	
	public Rectangle getRectangle() {
		
		return partArea;
	}

}

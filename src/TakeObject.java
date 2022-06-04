import java.awt.Rectangle;

import javax.swing.JLabel;

public class TakeObject {
	
	private Rectangle takeArea;
	private String setName;
	private int takeN;
	private JLabel shotLabel;
	
	public TakeObject(String setName, int takeN, Rectangle takeArea) {
		
		this.takeN = takeN;
		this.setName = setName;
		this.takeArea = takeArea;
	}
	
	public String getSetName() {
		
		return setName;
	}
	
	public int getNumTakes() {
		
		return takeN;
	}
	
	public Rectangle getTakeArea() {
		
		return takeArea;
	}
	
	public void setJLabel(JLabel label) { 
		
		this.shotLabel = label;
	}
	
	public JLabel getShotImage() {
		
		return shotLabel;
	}
}

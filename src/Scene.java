import java.util.ArrayList;

import javax.swing.JLabel;
public class Scene {
	
    private String name;
    private int budget;
    private ArrayList<Role> sceneRoles;
    private boolean hasBeenUsed;
    private String cardImage;
    private int sceneNumber;
    
    private ArrayList<PartObject> onCardRoles;
    
    private JLabel backOfCard;
    private JLabel frontOfCard;

    public Scene(String name, int budget, ArrayList<Role> sceneRoles, String cardImage, int sceneNumber, ArrayList<PartObject> onCardRoles) {
        this.name = name;
        this.budget = budget;
        this.sceneRoles = sceneRoles;
        this.cardImage = cardImage;
        this.sceneNumber = sceneNumber;
        this.onCardRoles = onCardRoles;
    }

    public String getName() {
    	
        return name;
    }

    public int getBudget() {
    	
        return budget;
    }

    public void setUsed(boolean used) {
    	
    	this.hasBeenUsed = used;
    }

    public boolean isUsed() {
    	
    	return hasBeenUsed;
    }

    public ArrayList<Role> getSceneRoles() {
    	
        return sceneRoles;
    }
    
    public String getCardImage() {
    	
    	return cardImage;
    }
    
    public int getSceneNumber() {
    	
    	return sceneNumber;
    }
    
    public void setCardImage(JLabel frontOfCard) {
    	
    	this.frontOfCard = frontOfCard;
    }

    public JLabel getFrontOfCard() {
        return frontOfCard;
    }

    public void setBackOfCard(JLabel backOfCard) {
    	
    	this.backOfCard = backOfCard;
    }
    
    public JLabel getCard() {
    	
    	return frontOfCard;
    }
    
    public JLabel getBackOfCard() {
    	
    	return backOfCard;
    }
    
    public ArrayList<PartObject> getOnCardRoles(){
    	
    	return onCardRoles;
    }  
}

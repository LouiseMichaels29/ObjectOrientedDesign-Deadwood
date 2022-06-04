import java.awt.Rectangle;
import java.util.ArrayList;

public class Set extends Room{
	
    private ArrayList<Role> roles;
    private ArrayList<TakeObject> takeObjects;
    private ArrayList<TakeObject> storeTakeObjects;
    private ArrayList<ExtraPartObject> extraParts;
    private Scene sceneCard;
    private int shotCount;
    private boolean beenVisited = false;
    private int currentIndex = 0;

    public Set(String name, ArrayList<String> adjacentRoomNames, Rectangle setArea){
    	
        setName(name);
        setAdjacentRoomNames(adjacentRoomNames);
        setRectangle(setArea);
    }

    public Set(String name, int shotCount, ArrayList<Role> roles, ArrayList<String> adjacentRoomNames, 
    			Rectangle setRectangle, ArrayList<TakeObject> takeObjects, ArrayList<ExtraPartObject> extraParts) {
    	
        setName(name);
        setAdjacentRoomNames(adjacentRoomNames);
        setRectangle(setRectangle);
        this.shotCount = shotCount;
        this.takeObjects = takeObjects;
        this.extraParts = extraParts;
        this.roles = roles;
        this.storeTakeObjects = new ArrayList<>(takeObjects);
    }
    
    public void setTakeObjects(ArrayList<TakeObject> storeTakeObjects) {
    	
    	this.takeObjects = storeTakeObjects;
    }
    
    public ArrayList<TakeObject> getStoredTakeObjects(){
    	
    	return storeTakeObjects;
    }
    
    public ArrayList<TakeObject> getTakeObjects(){
    	
    	return takeObjects;
    }
    
    public ArrayList<ExtraPartObject> getExtraParts(){ 
    	
    	return extraParts;
    }

    public ArrayList<Role> getRoles(){
    	
        return roles;
    }

    public Scene getSceneCard() {
    	
        return sceneCard;
    }

    public void setSceneCard(Scene sceneCard) {
    	
        this.sceneCard = sceneCard;
    }

    public int getShotCount() {
    	
        return shotCount;
    }

    public void setShotCount(int shotCount) {
    	
        this.shotCount = shotCount;
    }

    public boolean isBeenVisited() {
        return beenVisited;
    }

    public void setBeenVisited(boolean beenVisited) {
        this.beenVisited = beenVisited;
    }
    
    public void setIndex(int index) {
    	
    	this.currentIndex = index;
    }
    
    public int getIndex() {
    	
    	return currentIndex;
    }
}

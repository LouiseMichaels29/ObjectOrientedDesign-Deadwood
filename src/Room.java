import java.awt.*;
import java.util.*;
public class Room{

	private String name;
	private ArrayList<Room> adjacentRooms;
	private ArrayList<String> adjacentRoomNames;
	private Rectangle rectangle;
	private int playerCount;

	public Room() {
		
		playerCount = 0;
	}

	public String getName() {
		
		return name;
	}

	public void setName(String name) {
		
		this.name = name;
	}

	public Rectangle getRectangle() {
		return rectangle;
	}

	public void setRectangle(Rectangle rectangle) {
		this.rectangle = rectangle;
	}

	public ArrayList<String> getAdjacentRoomNames() {
		
		return adjacentRoomNames;
	}

	public void setAdjacentRoomNames(ArrayList<String> adjacentRoomNames) {
		
		this.adjacentRoomNames = adjacentRoomNames;
	}

	public void setAdjacentRooms(ArrayList<Room> adjacentRooms) {
		
		this.adjacentRooms = adjacentRooms; 
	}
	
	public ArrayList<Room> getAdjacentRooms(){
		
		return adjacentRooms;
	}
	
	public void setPlayerCount(int playerCount) {
		
		this.playerCount = playerCount;
	}
	
	public int getPlayerCount() {
		
		return playerCount;
	}
}


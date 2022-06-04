import java.util.*;
import javax.xml.parsers.ParserConfigurationException;

public class Board extends Room{

	private static Map<String, Room> rooms = new HashMap<String, Room>();

	private static XMLParser parser = new XMLParser();
	private static ArrayList<Room> roomsList;
	private static ArrayList<Scene> scenesList;

	public Board() {
		
		setRooms();
		setScenes(); 
	}

	public void setRooms()  {
		
		try {
			
			//Read data from xml file and build all Room objects. 
			roomsList = parser.readBoardData(parser.getDocFromFile("xml/board.xml"));

			//Setup the hashMap to get from name of room to the room object.
			for(int i = 0; i < roomsList.size(); i++){
				
				rooms.put(roomsList.get(i).getName(), roomsList.get(i));
			}

			//Provide each room with a list of adjacent rooms as Room objects 
			for(int i = 0; i < roomsList.size(); i++){
				
				Room tempRoom = roomsList.get(i);
				ArrayList<Room> adjacentRooms = new ArrayList<>();
				
				for(int j = 0; j < tempRoom.getAdjacentRoomNames().size(); j++){
					
					String tempRoomName = tempRoom.getAdjacentRoomNames().get(j);
					adjacentRooms.add(rooms.get(tempRoomName));
				}
				
				tempRoom.setAdjacentRooms(adjacentRooms);
			}

		} catch (ParserConfigurationException e) {
			
			e.printStackTrace();
		}
	}

	public void setScenes() {
		
		try {
			
			scenesList = parser.readCardsData(parser.getDocFromFile("xml/cards.xml"));
		} catch (ParserConfigurationException e) {
			
			e.printStackTrace();
		}
	}

	public void resetBoard(ArrayList<Player> players){

		resetScenes();
		
		for(int i = 0; i < players.size(); i++) {
			
			players.get(i).setPosition(rooms.get("trailer"));
			BoardLayersListener.movePlayerDice(players.get(i), rooms.get("trailer").getRectangle());
		}
	
		for(int i = 0; i < roomsList.size(); i++) { 
			
			//Set shot tokens here
			if(!(roomsList.get(i).getName().equals("office")) &&
					(!(roomsList.get(i).getName().equals("trailer")))) {
				
				Set set = (Set) roomsList.get(i);
				if(set.getName().equals("Main Street")) set.setShotCount(3); 
				else if(set.getName().equals("Saloon")) set.setShotCount(2);
				else if(set.getName().equals("Jail")) set.setShotCount(1);
				else if(set.getName().equals("General Store")) set.setShotCount(2);
				else if(set.getName().equals("Train Station")) set.setShotCount(3);
				else if(set.getName().equals("Hotel")) set.setShotCount(3);
				else if(set.getName().equals("Bank")) set.setShotCount(1);
				else if(set.getName().equals("Church")) set.setShotCount(2);
				else if(set.getName().equals("Ranch")) set.setShotCount(2);
				else if(set.getName().equals("Secret Hideout")) set.setShotCount(3);
			}
		}
	}
	
	public void setBoardObjects(ArrayList<Player> players) {

		BoardLayersListener.addCards(); //Add labels/images for the new day.
		int playerCount = 0;
		
		for(int i = 0; i < players.size(); i++) {
			
			players.get(i).getRoom().setPlayerCount(0);
		}
		
		for(int i = 0; i < players.size(); i++) {
			
			rooms.get("trailer").setPlayerCount(playerCount + 1);
			BoardLayersListener.movePlayerDice(players.get(i), rooms.get("trailer").getRectangle()); 
		}
	}

	public void resetScenes(){

		Random random = new Random(); 
		
		for(int i = 0; i < roomsList.size(); i++) { 
	
			if(!(roomsList.get(i).getName().equals("office")) &&
					(!(roomsList.get(i).getName().equals("trailer")))) {
				
				int randomCard = random.nextInt(40); 
				while(scenesList.get(randomCard).isUsed()) { 

					randomCard = (randomCard + 1) % 40; 
				} 
				
				Set set = (Set) roomsList.get(i); 
				set.setSceneCard(scenesList.get(randomCard));
				scenesList.get(randomCard).setUsed(true);
			}
		}
	}

	public Map<String, Room> getRooms(){
		
		return rooms;
	}
	
	public ArrayList<Scene> getSceneList(){ 
		
		return scenesList;
	}
	
	public static ArrayList<Room> getRoomsList(){

		return roomsList;
	}
}

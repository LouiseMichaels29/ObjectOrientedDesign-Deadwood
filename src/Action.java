import java.util.*;
public class Action {

	private boolean endTurn = false; 
	private int scenesLeftInDay = 10; 
	private ArrayList<Player> players = new ArrayList<Player>(); 

	public void move(Player player, String roomName) {

		ArrayList<Room> adjacentRooms = player.getRoom().getAdjacentRooms();
		
		if(!(player.isWorking())) {

			for(int i = 0; i < adjacentRooms.size(); i++) {

				if(roomName.equals(adjacentRooms.get(i).getName())) {

					System.out.print("Moved from \"" + player.getRoom().getName() + "\" ");
					player.setPosition(adjacentRooms.get(i)); 
					player.setMoved(true);
					System.out.println("to \"" + player.getRoom().getName() + "\"");
				}
			}

			if(!(player.hasMoved())) {

				System.out.println("Unable to move to: \"" + roomName + "\"");
			}
		}
	}


	public Role work(Player player, String roleName, Object[] getAllParts) {

		Set set = (Set) player.getRoom();
		ArrayList<Role> roles = set.getRoles();
		roles.addAll(set.getSceneCard().getSceneRoles());
		
		for(int i = 0; i < roles.size(); i++) {

			if(roles.get(i).getName().equals(roleName)) {

				if(roles.get(i).isOccupied()) {
					
					System.out.println("Role is occupied. "); return null;
				}

				else if(roles.get(i).getRoleRank() <= player.getRank()) {

					roles.get(i).takeRole(true);
					roles.get(i).setPlayer(player);
					player.setRole(roles.get(i));
					player.setWorking(true);
					setEndTurn(true);

					System.out.println(player.getName() + " WORKING: \"" + roles.get(i).getName() + "\"");
					return roles.get(i);
				}

				else {

					BoardLayersListener.displayMessage("Player does not meet required rank."); return null;
				}
			}
		}
		
		return null;
	}

	public void act(Player player) {

		Random random = new Random();
		int dice = random.nextInt(6) + 1 + player.getRehearseTokens();
		Set set = (Set) player.getRoom();
		int movieBudget = set.getSceneCard().getBudget();

		if(dice >= movieBudget) {
			
			BoardLayersListener.removeTake(set);
			set.setShotCount(set.getShotCount() - 1);
			BoardLayersListener.displayMessage("Success!!\n There are now " + set.getShotCount() + " shots left.");
			if(player.getRole().isOnCard()) {

				player.setCredits(player.getCredits() + 2);
				BoardLayersListener.displayMessage(player.getName() + " recieves two credits!");
			}

			else {

				player.setCredits(player.getCredits() + 1);
				player.setDollars(player.getDollars() + 1);
				BoardLayersListener.displayMessage(player.getName() + " recieves one credit and one dollar!");
			}

			if(set.getShotCount() <= 0) {

				wrapScene(player);
			}
		}

		else {

			BoardLayersListener.displayMessage("Failure!");

			if(!(player.getRole().isOnCard())) {

				player.setDollars(player.getDollars() + 1);
				BoardLayersListener.displayMessage(player.getName() + " recieves one dollar");
			}
		}
		
		setEndTurn(true);
	}

	public void rehearse(Player player){
		
		player.setHasRehearsed(true);
		player.addOneRehearseToken();
	}

	public void upgrade(Player player, String[] optionArray){
		CastingOffice office = (CastingOffice) player.getRoom();

		if(optionArray.length == 1){
			office.printUpgrades();
		}
		else if(optionArray.length == 2 && (optionArray[1].equals("dollar") || optionArray[1].equals("credit"))){
			String sRank = optionArray[0];
			int rank;
			int price;
			String currency = optionArray[1];

			switch (sRank){
				case "1":
					rank = 1;
					break;
				case "2":
					rank = 2;
					break;
				case "3":
					rank = 3;
					break;
				case "4":
					rank = 4;
					break;
				case "5":
					rank = 5;
					break;
				case "6":
					rank = 6;
					break;
				default:
					return;
			}

			if(player.getRank() >= rank){
				System.out.println("You are already equal to or above desired rank.");
				return;
			}

			price = office.getUpgradePrice(rank, currency);

			if(currency.equals("dollar")){
				if(player.getDollars() >= price){
					player.setRank(rank);
					player.setDollars(player.getDollars() - price);
					System.out.println("You have been upgraded to level " + rank);
				}
				else{
					System.out.println("You do not have enough dollars to upgrade to rank level " + rank);
				}
			}
			else{
				if(player.getCredits() >= price){
					player.setRank(rank);
					player.setCredits(player.getCredits() - price);
					System.out.println("You have been upgraded to level " + rank);
				}
				else{
					System.out.println("You do not have enough credits to upgrade to rank level " + rank);
				}
			}
		}
		else{
			System.out.println("Please input your upgrade choice correctly.");
		}
	}

	public void endTurn(Player player){
		
		player.setMoved(false);
		player.setHasRehearsed(false);
	}

	public void wrapScene(Player player) {

		Random random = new Random();
		Set set = (Set) player.getRoom();
		Scene card = set.getSceneCard();
		
		BoardLayersListener.displayMessage("The scene is wrapped! ");

		if(checkCard(player)) {

			int dice[] = new int[card.getBudget()];

			for(int i = 0; i < card.getBudget(); i++) {

				dice[i] = random.nextInt(6) + 1;
			}

			int sceneRoles = card.getSceneRoles().size();
			for(int i = 0; i < dice.length; i++) {

				if(card.getSceneRoles().get(i % sceneRoles).getPlayer() != null) {
					card.getSceneRoles().get(i % sceneRoles).getPlayer().setDollars(
							card.getSceneRoles().get(i % sceneRoles).getPlayer().getDollars() + dice[i]);
				}
			}

			for(int i = 0; i < set.getRoles().size(); i++) {
				
				if((set.getRoles().get(i).isOccupied()) &&
						(!(set.getRoles().get(i).isOnCard()))) {

					set.getRoles().get(i).getPlayer().setDollars(
							set.getRoles().get(i).getPlayer().getDollars() +
									set.getRoles().get(i).getRoleRank());
				}
			}
		}

		freeRoles(player);
		scenesLeftInDay--;
	}

	public boolean checkCard(Player player) {

		Set set = (Set) player.getRoom();
		ArrayList<Role> roles = set.getSceneCard().getSceneRoles();

		for(int i = 0; i < roles.size(); i++) {

			if(roles.get(i).isOccupied()) {

				return true;
			}
		}

		return false;
	}

	public void freeRoles(Player player) { 

		Set set = (Set) player.getRoom(); 
		ArrayList<Role> roles = new ArrayList<Role>(set.getRoles()); 
		roles.addAll(set.getSceneCard().getSceneRoles()); 

		for(int i = 0; i < roles.size(); i++) { 

			if(roles.get(i).isOccupied()) { 
				
				roles.get(i).setOccupied(false);
				roles.get(i).getPlayer().setWorking(false);
				roles.get(i).getPlayer().setMoved(false);
				roles.get(i).getPlayer().setRehearseTokens(0);
				BoardLayersListener.movePlayerDice(roles.get(i).getPlayer(), set.getRectangle()); 
				BoardLayersListener.removeCard(set);
			} 
		} 
	} 

	public String getStringBuffer(String optionArray[]) {

		StringBuffer sb = new StringBuffer();

		int i = 1;
		for(i = 1; i < optionArray.length - 1; i++){

			sb.append(optionArray[i] + " ");
		}

		sb.append(optionArray[i]);
		String getString = sb.toString();
		return getString;
	}

	public boolean isEndTurn() {

		return endTurn;
	}

	public void setEndTurn(boolean endTurn) {

		this.endTurn = endTurn;
	}

	public int getScenesLeftInDay() {
		
		return scenesLeftInDay;
	}

	public void setScenesLeftInDay(int scenesLeftInDay) {
		
		this.scenesLeftInDay = scenesLeftInDay;
	}

	public ArrayList<Player> getPlayers() {
		
		return players;
	}

	public void setPlayers(ArrayList<Player> players) {
		
		this.players = players;
	}
}
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

public class BoardLayersListener extends JFrame {
	
	private static Board board;

	private JLabel boardlabel;
  
	private JButton buttonMove;
	private JButton buttonAct;
	private JButton buttonRehearse;
	private JButton buttonEndTurn;
	private JButton buttonTakeRole;
	private JButton buttonPlayerInfo;
	private JButton buttonUpgradeRank;

	private static JLayeredPane boardPane;
	
	private ArrayList<String> colors = new ArrayList<String>();
	private static Action actions;
	private static int numerator = 9;
	private static int denominator = 16;
	private static int doubleImageSize = 1;
	
	public BoardLayersListener(Board board, ArrayList<Player> players, int daysLeft) { 
	   
	   super("Deadwood");
	   ImageIcon icon = (ImageIcon) setBoard(board);
	   addCards();
       addColors();
       int numberOfPlayers = getPlayers();
       setPlayers(players, numberOfPlayers, daysLeft);
       loadPlayers(players);
       addButtons(icon);
       actions = Game.actions;
  }

  public void playersTurn(Player player){
	  //Wait until End is clicked.
	  try {
		  synchronized (buttonEndTurn) {
			  
			  buttonEndTurn.wait();
		  }
	  } catch(InterruptedException ex){
		  
		  System.out.println("Interrupted");
	  }
  }

  public static void removeLabels(){
	  
	  for(int i = 0; i < board.getRoomsList().size(); i++) {
		  if(!("trailer".equals(board.getRoomsList().get(i).getName())) &&
				  (!("office".equals(board.getRoomsList().get(i).getName())))) {

			  Set set = (Set) board.getRoomsList().get(i);
			  Scene card = set.getSceneCard();

			  if(!(set.isBeenVisited())){
				  JLabel backOfCardLabel = card.getBackOfCard();
				  boardPane.remove(backOfCardLabel);
			  }

			  int shotCount = set.getShotCount();
			  if(shotCount > 0) {
				  JLabel frontOfCard = card.getFrontOfCard();
				  boardPane.remove(frontOfCard);

				  //Remove every shot token label left on the set.
				  for(int j=shotCount; j>0; j--){
					  JLabel shot = set.getTakeObjects().get(shotCount-1).getShotImage();
					  boardPane.remove(shot);
				  }
			  }
		  }
	  }
  }

  
  public static void addCards() {
	  
	  for(int i = 0; i < board.getRoomsList().size(); i++) {
		  if(!("trailer".equals(board.getRoomsList().get(i).getName())) && 
				  (!("office".equals(board.getRoomsList().get(i).getName())))) {
			  Set currentRoom = (Set) board.getRoomsList().get(i);
			  Scene currentCard = currentRoom.getSceneCard();
			  placeCards(currentCard, currentRoom.getRectangle());
			  placeCardBacks(currentCard, currentRoom.getRectangle());
			  placeShotTokens(currentRoom);

			  //Set onCard Role's rectangle location b/c it changes with where the scene card is at. 
			  Rectangle cardArea = currentRoom.getRectangle();
			  ArrayList<Role> cardRoles = currentCard.getSceneRoles();
			  for(int j = 0; j < cardRoles.size(); j++){
			  	Role role = cardRoles.get(j);
			  	Rectangle roleRec = new Rectangle((int) cardArea.getX() + (int) role.getRectangle().getX(), (int) cardArea.getY() + (int) role.getRectangle().getY(), 40, 40);
			  	role.setPosition(roleRec);
			  }
		  }
	  }
  }
  
  public static void placeCardBacks(Scene card, Rectangle cardArea) {
	  
	  JLabel backOfCardLabel = new JLabel();
	  ImageIcon backOfCardImage = new ImageIcon("add/CardBack-small.jpg");
	  backOfCardImage = doubleImageSize(backOfCardImage);

	  backOfCardLabel.setIcon(backOfCardImage);
	  backOfCardLabel.setBounds((int) cardArea.getX() * numerator / denominator, (int) cardArea.getY() * numerator / denominator, backOfCardImage.getIconWidth() , backOfCardImage.getIconHeight());
	  backOfCardLabel.setOpaque(true);
	  card.setBackOfCard(backOfCardLabel);
	  boardPane.add(backOfCardLabel, new Integer(3)); 
  }
  
  public static void placeCards(Scene card, Rectangle cardArea) {
	  
	  JLabel cardLabel = new JLabel();
	  ImageIcon cardImage = new ImageIcon(card.getCardImage());

      cardImage = doubleImageSize(cardImage);
	  cardLabel.setIcon(cardImage);
	  cardLabel.setBounds((int) cardArea.getX() * numerator / denominator, (int) cardArea.getY() * numerator / denominator, cardImage.getIconWidth(), cardImage.getIconHeight());
	  cardLabel.setOpaque(true);
	  card.setCardImage(cardLabel);
	  boardPane.add(cardLabel, new Integer(2));
  }

  public static void removeTake(Set currentSet) {
	  
	  ArrayList<TakeObject> shotTokens = currentSet.getTakeObjects(); 

	  JLabel takeObjectLabel = shotTokens.get(currentSet.getShotCount() - 1).getShotImage();
	  boardPane.remove(takeObjectLabel);
	  boardPane.revalidate();
	  boardPane.repaint();

	  System.out.println("Tokens " + currentSet.getTakeObjects().size());
	  System.out.println("Tokens Copy: " + currentSet.getStoredTakeObjects().size());
  }
  
  public static void removeCard(Set currentSet) {
	  
	  JLabel cardLabel = currentSet.getSceneCard().getCard(); 
	  boardPane.remove(cardLabel);
	  boardPane.revalidate();
	  boardPane.repaint();
  }

  public static void placeShotTokens(Set currentSet){ 
	  
      ArrayList<TakeObject> shotTokens = currentSet.getTakeObjects();
      ImageIcon shotImage = new ImageIcon("add/shot.png");
      shotImage = doubleImageSize(shotImage);
      
      for(int i = 0; i < shotTokens.size(); i++){
    	  
          JLabel shotLabel = new JLabel();
          Rectangle shotArea = shotTokens.get(i).getTakeArea();

          shotLabel.setIcon(shotImage);
          shotTokens.get(i).setJLabel(shotLabel);
          shotLabel.setBounds((int) shotArea.getX() * numerator / denominator, (int) shotArea.getY() * numerator / denominator, shotImage.getIconWidth(), shotImage.getIconHeight());
          shotLabel.setOpaque(true);
          boardPane.add(shotLabel, new Integer(2));
      }
  }
  
  public int getPlayers() {
	  
	  String[] playerOptions = new String[] {"2", "3", "4", "5", "6", "7", "8"};
	  int numberOfPlayers = JOptionPane.showOptionDialog(null, "How many players? ", 
				  "Players", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, playerOptions, playerOptions[0]);
		  
	  if(numberOfPlayers == -1) {
		  
		  displayMessage("Not a valid number of players.");
		  System.exit(0);
	  }
	  
	  return (Integer.parseInt(playerOptions[numberOfPlayers]));
  }
  
  public void loadPlayers(ArrayList<Player> players) {
	  
	  int playerHeightOffset = 0;
	  int playerWidthOffset = 0;
	  
	  for(int i = 0; i < players.size(); i++) {
		  
		  String getImage = "dice/" + players.get(i).getColor() + players.get(i).getRank() + ".png";
		  JLabel playerLabel = new JLabel();
		  ImageIcon playerIcon = new ImageIcon(getImage);
		  playerIcon = doubleImageSize(playerIcon);
		  playerLabel.setIcon(playerIcon);
		  
		  //If five or more players are on the same card, we want to place then vertically and then horizontally (make a new line of players) 
		  if(i == 5) {
			  
			  playerHeightOffset = playerIcon.getIconHeight();
			  playerWidthOffset = 0;
		  }
		  
		  playerLabel.setBounds((990 + playerWidthOffset) * numerator / denominator, (244 + playerHeightOffset) * numerator / denominator,
	  				playerIcon.getIconWidth(), playerIcon.getIconHeight());
		  Player player = players.get(i);
		  player.setPlayerLabel(playerLabel);
		  player.setPlayerWidthOffset(playerWidthOffset);
		  player.setPlayerHeightOffset(playerHeightOffset);

		  boardPane.add(playerLabel, new Integer(3));
		  playerWidthOffset += playerIcon.getIconWidth();
	 }
  }
  
  public void setPlayers(ArrayList<Player> players, int numberOfPlayers, int daysLeft) {
		
		int startingRank = 1; 
		int startingCredits = 0;
		
		enter: while(true) {
			
			switch (numberOfPlayers) {

				case 2:
				case 3:
					daysLeft = 3;
					break enter;
				case 4:
					break enter;
				case 5:
					startingCredits = 2;
					break enter;
				case 6:
					startingCredits = 4;
					break enter;
				case 7:
				case 8:
					startingRank = 2;
					break enter;
				default:
					System.out.println("Not a valid number of players.");
			}
		}

		for(int i = 0; i < numberOfPlayers; i++) {
		
		players.add(new Player("Player " + (i + 1), 
				board.getRooms().get("trailer"), startingRank, startingCredits, 0, colors.get(i)));
		}
	}
  
  public void addColors() {
	  
	  colors.add("b");
	  colors.add("c");
	  colors.add("g");
	  colors.add("o");
	  colors.add("p");
	  colors.add("r");
	  colors.add("v");
	  colors.add("w");
	  colors.add("y");
  }

	private static ImageIcon doubleImageSize(ImageIcon smallImage){

		Image bigImage = smallImage.getImage();
		bigImage = bigImage.getScaledInstance(smallImage.getIconWidth() * doubleImageSize * numerator / denominator, 
				smallImage.getIconHeight() * doubleImageSize * numerator / denominator,  java.awt.Image.SCALE_SMOOTH);
		return new ImageIcon(bigImage);
	}
  
  public Icon setBoard(Board board) {
	  
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      this.board = board;
      boardPane = getLayeredPane();
      boardlabel = new JLabel();
      ImageIcon icon =  new ImageIcon("add/board.jpg");
      icon = doubleImageSize(icon);
      boardlabel.setIcon(icon); 
      boardlabel.setBounds(0, 0, icon.getIconWidth(), icon.getIconHeight());
      boardPane.add(boardlabel, new Integer(0));
      setSize(icon.getIconWidth() + 200, icon.getIconHeight() + 100);
      return icon;
  }
  
  public static void movePlayer(Player player) {

	  ArrayList<Room> rooms = player.getRoom().getAdjacentRooms();
	  Object[] getRoomNames = new Object[rooms.size()]; 

	  if(!(player.hasMoved()) && !(player.isWorking())){
		  
		  for(int i = 0; i < rooms.size(); i++) {

			  getRoomNames[i] = rooms.get(i).getName();
		  }

		  int movementOption =  JOptionPane.showOptionDialog(null, "Choose which scene to move to", "Message", 
				  JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
				  null, getRoomNames, getRoomNames.length); 
		  
		  if(movementOption != -1) {
			  
			  actions.move(player, getRoomNames[movementOption].toString());
	
			  Room room = player.getRoom();
			  Rectangle rectangle = new Rectangle((int) room.getRectangle().getX() + player.getPlayerWidthOffset(),
					  (int) room.getRectangle().getY() + player.getPlayerHeightOffset(), (int) room.getRectangle().getWidth(),
					  (int) room.getRectangle().getHeight());
	
			  if(!(player.getRoom().getName().equals("trailer")) && !(player.getRoom().getName().equals("office"))){
				
				Set set = (Set) room;
				if(!(set.isBeenVisited())){
					JLabel backOfCardLabel = set.getSceneCard().getBackOfCard();
					boardPane.remove(backOfCardLabel);
					boardPane.repaint();
				}
			  }
			  
			  movePlayerDice(player, rectangle);
		  }
	  }
  }
  
  public void takeRole(Player player) {

		if(player.isWorking()){
				
			return;
		}
		  
		if(!(player.getRoom().getName().equals("trailer")) && !(player.getRoom().getName().equals("office"))) {
		  
		  Set set = (Set) player.getRoom();
		  Scene card = set.getSceneCard();
		  ArrayList<ExtraPartObject> extraParts = set.getExtraParts();
		  ArrayList<PartObject> partsObject = card.getOnCardRoles();
		  Object getAllParts[] = new Object[extraParts.size() + partsObject.size()];
		  
		  for(int i = 0; i < extraParts.size(); i++) {

			  getAllParts[i] = extraParts.get(i).getName();
		  }
		  
		  for(int i = extraParts.size(); i < (partsObject.size() + extraParts.size()); i++) {
			  
			  getAllParts[i] = partsObject.get(i - extraParts.size()).getPartName();
		  }
		  
		  int getRole = JOptionPane.showOptionDialog(null, "Please choose role: ", "Message", 
				  JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, getAllParts, getAllParts[0]);
		  
		  if(getRole != -1) {
			  
			  String roleName = getAllParts[getRole].toString();
			  Role workingRole = actions.work(player, roleName, getAllParts);

			  //Move player dice to role if working
			  if(workingRole != null){
				  Rectangle rectangle = workingRole.getRectangle();
				  movePlayerDice(player, rectangle);
			  }
		  }
	  }
  }

  //Allow for player number so we know where to move the player dice to so they don't stack on top of each other. 
  public static void movePlayerDice(Player player, Rectangle rectangle){
	  
	  JLabel playerLabel = player.getPlayerLabel(); 
	  boardPane.remove(playerLabel);
	  boardPane.repaint();
	  Icon playerIcon = playerLabel.getIcon();

	  playerLabel.setBounds((int) rectangle.getX() * numerator / denominator + (player.getRoom().getPlayerCount() * 10), (int) rectangle.getY() * numerator / denominator,
			  playerIcon.getIconWidth() * numerator / denominator * 2, playerIcon.getIconHeight() * numerator / denominator * 2);

	  boardPane.add(playerLabel, new Integer(5)); 
	  boardPane.repaint(); 
  } 

  public void act(Player player){
	  
	if(player.isWorking()){
		
		actions.act(player);
	}
  }

  public void rehearse(Player player){
	  
	if(player.isWorking() && !(player.isHasRehearsed())){
		
		actions.rehearse(player);
		displayMessage(player.getName() + " recieves 1 rehearsal chip!");
		actions.setEndTurn(true);
	}
  }

  public void upgrade(Player player){

	  if(player.getRoom().getName().equals("office")) {
	
		  String[] ranks = {"2", "3", "4", "5", "6"};
		  int chosenRankIndex = JOptionPane.showOptionDialog(null, "Please select rank ", "Rank", 
				  JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, ranks, ranks[0]); 
		  String[] method = {"dollar", "credit"};
		  int upgradeMethodIndex = JOptionPane.showOptionDialog(null, "Select money or credits", "Upgrade Type", 
				  JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, method, method[0]);
		  String chosenRank = ranks[chosenRankIndex];
		  String upgradeMethod = method[upgradeMethodIndex]; 
		  String[] optionArray = new String[2]; 
		  optionArray[0] = chosenRank; 
		  optionArray[1] = upgradeMethod; 
		  actions.upgrade(player, optionArray); 
		  JLabel currentPlayerLabel = player.getPlayerLabel();
		  boardPane.remove(currentPlayerLabel);
		  boardPane.revalidate();
		  boardPane.repaint();
		  String getImage = "dice/" + player.getColor() + player.getRank() + ".png";
		  JLabel playerLabel = new JLabel();
		  ImageIcon playerIcon = new ImageIcon(getImage);
		  playerIcon = doubleImageSize(playerIcon);
		  playerLabel.setIcon(playerIcon);
		  playerLabel.setBounds((int) player.getRoom().getRectangle().getX() * numerator / denominator, (int) player.getRoom().getRectangle().getY() * numerator / denominator,
	  				playerIcon.getIconWidth(), playerIcon.getIconHeight());
		  player.setPlayerLabel(playerLabel);
		  boardPane.add(playerLabel, new Integer(4));
		  boardPane.revalidate();
		  boardPane.repaint();
	  }
	  
	  else {
		  
		  displayMessage("Player is not in casting office. ");
	  }
  }
  
  public void addButtons(ImageIcon icon) {
	  
	  buttonPlayerInfo = new JButton("Player Info");
	  buttonPlayerInfo.setBackground(Color.white);
	  buttonPlayerInfo.setBounds(icon.getIconWidth() + 10, 20, 120, 40);
	  buttonPlayerInfo.addMouseListener(new boardMouseListener());
	  boardPane.add(buttonPlayerInfo, new Integer(2));
	  
	  buttonMove = new JButton("Move");
	  buttonMove.setBackground(Color.white);
	  buttonMove.setBounds(icon.getIconWidth() + 10, 80, 120, 40);
	  buttonMove.addMouseListener(new boardMouseListener());
	  boardPane.add(buttonMove, new Integer(2));
	  
	  buttonAct = new JButton("Act");
	  buttonAct.setBackground(Color.white);
	  buttonAct.setBounds(icon.getIconWidth() + 10, 140, 120, 40);
	  buttonAct.addMouseListener(new boardMouseListener());
	  boardPane.add(buttonAct, new Integer(2));
	  
	  buttonRehearse = new JButton("Rehearse");
	  buttonRehearse.setBackground(Color.white);
	  buttonRehearse.setBounds(icon.getIconWidth() + 10, 200, 120, 40);
	  buttonRehearse.addMouseListener(new boardMouseListener());
	  boardPane.add(buttonRehearse, new Integer(2));
	  
	  buttonTakeRole = new JButton("Take Role");
	  buttonTakeRole.setBackground(Color.white);
	  buttonTakeRole.setBounds(icon.getIconWidth() + 10, 260, 120, 40);
	  buttonTakeRole.addMouseListener(new boardMouseListener());
	  boardPane.add(buttonTakeRole, new Integer(2));
	  
	  buttonUpgradeRank = new JButton("Upgrade Rank");
	  buttonUpgradeRank.setBackground(Color.white);
	  buttonUpgradeRank.setBounds(icon.getIconWidth() + 10, 320, 120, 40);
	  buttonUpgradeRank.addMouseListener(new boardMouseListener());
	  boardPane.add(buttonUpgradeRank, new Integer(2));
	  
	  buttonEndTurn = new JButton("End Turn");
	  buttonEndTurn.setBackground(Color.white);
	  buttonEndTurn.setBounds(icon.getIconWidth() + 10, 380, 120, 40);
	  buttonEndTurn.addMouseListener(new boardMouseListener());
	  boardPane.add(buttonEndTurn, new Integer(2));
  }
  
  public void displayMenu(Player player) {
	  
	  displayMessage("The current player is " + player.getColor() + ". ($" + player.getDollars() + 
			  " , " + player.getCredits() + ") Rank: " + player.getRank()); 
  }
  
  public static void displayMessage(String message) {

	  JOptionPane.showMessageDialog(null, message);
  }
 
  class boardMouseListener implements MouseListener{
  
      public void mouseClicked(MouseEvent e) {
		  if (e.getSource() == buttonPlayerInfo){

			  displayMenu(Game.currentPlayer);
		  }

    	 if(!(actions.isEndTurn())) {

    	 	if (e.getSource() == buttonMove){
	        	 
	        	 movePlayer(Game.currentPlayer);
	         }
	         
	         else if(e.getSource() == buttonTakeRole) {
	        	 
	        	 takeRole(Game.currentPlayer);
	         }
	         
	         else if (e.getSource() == buttonAct){
	        	 
	        	 act(Game.currentPlayer);
	         }         
	         
	         else if (e.getSource() == buttonRehearse) {
	        	 
	        	 rehearse(Game.currentPlayer);
	         }
	         
	         else if(e.getSource() == buttonUpgradeRank) {
	        	 
	        	 upgrade(Game.currentPlayer);
	         }
    	 }
         
         if(e.getSource() == buttonEndTurn) {
        	 
        	actions.setEndTurn(false);
         	actions.endTurn(Game.currentPlayer);

			 synchronized (buttonEndTurn) {
				 
				 buttonEndTurn.notify();
			 }
         }
      }
      
      public void mousePressed(MouseEvent e) {
    	  
      }
      
      public void mouseReleased(MouseEvent e) {
    	  
      }
      
      public void mouseEntered(MouseEvent e) {
    	  
      }
      
      public void mouseExited(MouseEvent e) {
    	  
      }
   }

}
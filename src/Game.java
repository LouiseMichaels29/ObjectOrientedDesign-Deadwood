import java.util.*;

public class Game {

	public static Map<String, Room> rooms = new HashMap<String, Room>();
	public Scanner scanner = new Scanner(System.in);
	
	public boolean gameRunning = true;
	public int daysLeft = 4;
	public boolean dayOver = false;
	
	public Board board;
	public static Action actions = new Action();
	public static ArrayList<Player> players = new ArrayList<Player>();
	public BoardLayersListener boardListener;
	public static Player currentPlayer;
	
	public Game() {

		startGame();
	}
	
	public void startGame() {
		
		board = new Board();
		board.resetBoard(players);
		
		boardListener = new BoardLayersListener(board, players, daysLeft); 
		boardListener.setVisible(true);
		actions.setPlayers(players); 
		
		while(daysLeft != 0) {
		
			BoardLayersListener.displayMessage("Welcome to deadwood! There are " + daysLeft + " days Left.");
			
			while(actions.getScenesLeftInDay() > 1) {
				
				for (int i = 0; i < players.size(); i++) { 
					
					currentPlayer = players.get(i); 
					BoardLayersListener.displayMessage(players.get(i).getColor() + " it is your turn."); 
					boardListener.playersTurn(players.get(i)); 

					if(actions.getScenesLeftInDay() <= 1) break;
				}
			}
			
			if(daysLeft <= 1) calculateScore();
			boardListener.removeLabels();
			board.resetBoard(players);
			board.setBoardObjects(players);
			actions.setScenesLeftInDay(10);
			
			daysLeft--;
			
			BoardLayersListener.displayMessage("Day is finished! There are " + daysLeft + " days left.");
		} 
	}
	
	public void calculateScore() {
		
		System.out.println("The game is finished! Calculating Scores... ");
		
		for(int i = 0; i < players.size(); i++) {
			
			players.get(i).setScore(players.get(i).getDollars() + 
					players.get(i).getCredits() + players.get(i).getRank() * 5);
		
			BoardLayersListener.displayMessage(players.get(i).getName() + " score is " + players.get(i).getScore());
		}
		
		System.out.println("GAME OVER");
		System.exit(0);
	}
	
	public ArrayList<Player> getPlayers(){
		
		return players;
	}
}

import javax.swing.JLabel;

public class Player {

	private String name;
	private Room room;
	private boolean turn;
	private boolean working;
	private boolean moved;
	private int rehearseTokens = 0; 
	private boolean hasRehearsed = false;

	private int rank;
	private int dollars;
	private int credits;
	private int score;
	private String color;
	
	private Role role;
	private JLabel playerLabel;
	private int playerWidthOffset;
	private int playerHeightOffset;
	
	public Player(String name, Room room, int rank, int credits, int dollars, String color) {

		this.name = name;
		this.room = room;
		this.rank = rank;
		this.dollars = dollars;
		this.credits = credits;
		this.color = color;
	}

	public int getRehearseTokens() {
		return rehearseTokens;
	}

	public void addOneRehearseToken(){
		rehearseTokens++;
	}

	public void setRehearseTokens(int rehearseTokens) {
		this.rehearseTokens = rehearseTokens;
	}

	public void setPosition(Room room) {
		
		this.room = room;
	}
	
	public Room getRoom() {
		
		return room;
	}

	public void setTurn(boolean turn) {
		
		this.turn = turn;
	}

	public boolean getTurn() {
		
		return turn;
	}
	
	public void setWorking(boolean working) {
		
		this.working = working; 
	}
	
	public boolean isWorking() {
		
		return working;
	}
	
	public void setMoved(boolean moved) {
		
		this.moved = moved;
	}
	
	public boolean hasMoved() {
		
		return moved;
	}
	
	public void setCredits(int credits) {
		
		this.credits = credits;
	}
	
	public int getCredits() {
		
		return credits;
	}

	public int getDollars() {
		
		return dollars;
	}

	public void setDollars(int dollars) {
		
		this.dollars = dollars;
	}

	public void setRank(int rank) {
		
		this.rank = rank;
	}
	
	public int getRank() {
		
		return rank;
	}
	
	public void setRole(Role role) {
		
		this.role = role;
	}
	
	public Role getRole() {
		
		return role;
	}
	
	public String getName() {
		
		return name;
	}
	
	public void freeRole() {
		
		role = null;
	}
	
	public void setScore(int score) {
		
		this.score = score;
	}
	
	public int getScore() {
		
		return score;
	}
	
	public void setColor(String color) {
    	
    	this.color = color;
    }

    public String getColor() {
    	
    	return color;
    }
    
    public void setPlayerLabel(JLabel label) {
    	
    	this.playerLabel = label;
    }
    
    public JLabel getPlayerLabel() {
    	
    	return playerLabel;
    }

	public int getPlayerWidthOffset() {
		return playerWidthOffset;
	}

	public void setPlayerWidthOffset(int playerWidthOffset) {
		this.playerWidthOffset = playerWidthOffset;
	}

	public int getPlayerHeightOffset() {
		return playerHeightOffset;
	}

	public void setPlayerHeightOffset(int playerHeightOffset) {
		this.playerHeightOffset = playerHeightOffset;
	}

	public boolean isHasRehearsed() {
		return hasRehearsed;
	}

	public void setHasRehearsed(boolean hasRehearsed) {
		this.hasRehearsed = hasRehearsed;
	}
}

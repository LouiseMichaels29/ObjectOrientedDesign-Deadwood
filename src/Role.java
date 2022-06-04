import java.awt.Rectangle;

public class Role {

	private String name;
	private int roleRank;
	private boolean onCard;
	private boolean occupied;
	private Player player;
	private Rectangle position;
	
	public Role(String name, int rank, boolean onCard, Rectangle position) {
		this.name = name;
		this.roleRank = rank;
		this.onCard = onCard;
		occupied = false;
		this.position = position;
	}
	
	public String getName() {
		return name;
	}

	public int getRoleRank() {
		
		return roleRank;
	}

	public boolean isOnCard() {
		
		return onCard;
	}

	public void setOccupied(boolean occupied) {
		this.occupied = occupied;
	}

	public boolean isOccupied() {
		
		return occupied;
	}

	public void takeRole(boolean occupied) {
		
		this.occupied = occupied;
	}
	
	public void setPlayer(Player player) {
		
		this.player = player;
	}
	
	public Player getPlayer() {
		
		return player;
	}
	
	public Rectangle getRectangle() {
		
		return position;
	}

	public void setPosition(Rectangle position) {
		this.position = position;
	}
}

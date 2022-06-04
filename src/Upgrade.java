import java.awt.Rectangle;

public class Upgrade {
    private int rank;
    private String currency;
    private int amount;
    private Rectangle area;

    public Upgrade(int rank, String currency, int amount, Rectangle area) {
        this.rank = rank;
        this.currency = currency;
        this.amount = amount;
        this.area = area;
    }

    public int getRank() {
        return rank;
    }

    public String getCurrency() {
        return currency;
    }

    public int getAmount() { 
        return amount;
    }
    
    public Rectangle getArea() {
    	
    	return area;
    }
}

import java.awt.Rectangle;
import java.util.ArrayList;

public class CastingOffice extends Room{
    private ArrayList<Upgrade> upgrades;

    public CastingOffice(ArrayList<String> adjacentRoomNames, ArrayList<Upgrade> upgrades, Rectangle area) {
        setName("office");
        setAdjacentRoomNames(adjacentRoomNames);
        this.upgrades = upgrades;
        setRectangle(area);
    }

    public void printUpgrades(){
        for(int i=0; i<upgrades.size();i++){
            Upgrade values = upgrades.get(i);
            System.out.println("Rank " + values.getRank() + " costs " + + values.getAmount() + " " + values.getCurrency() + "s");
        }
    }

    public int getUpgradePrice(int rank, String currency){
        for(int i=0; i<upgrades.size(); i++){
            Upgrade upgrade = upgrades.get(i);
            if(rank == upgrade.getRank() && currency.equals(upgrade.getCurrency())){
                return upgrade.getAmount();
            }
        }
        return 100;
    }

    public void setUpgrades(ArrayList<Upgrade> upgrades) {
        this.upgrades = upgrades;
    }

    public ArrayList<Upgrade> getUpgrades() {
        return upgrades;
    }
}

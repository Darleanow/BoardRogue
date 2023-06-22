import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private List<String> items = new ArrayList<>();

    public void add(String item) {
        items.add(item);
    }

    public boolean usePotion() {
        if (items.remove("PotionS") || items.remove("PotionM")) {
            return true;
        }
        return false;
    }
}
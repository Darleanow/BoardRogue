import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private List<String> items = new ArrayList<>();

    public void add(String item) {
        items.add(item);
    }

    public int usePotion() {
        if (items.remove("PotionM")) {
            return 10;
        } else if (items.remove("PotionS")) {
            return 5;
        }
        return -1;
    }

}
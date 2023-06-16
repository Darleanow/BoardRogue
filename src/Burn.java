import java.util.Random;

public class Burn implements Effet {
    int damage = new Random().nextInt(16) + 1;
    String name = "Burn";
    @Override
    public int applyEffect() {
        return this.damage;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
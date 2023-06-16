import java.util.Random;

public class Stun implements Effet {
    int duration = new Random().nextInt(2);
    String name = "Stun";
    @Override
    public int applyEffect() {
        return this.duration;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
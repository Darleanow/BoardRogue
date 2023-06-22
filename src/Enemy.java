public class Enemy {
    private final int hp;
    private final int attack;
    private final String name;
    public Enemy(String name) {
        switch (name) {
            case "Dragon" -> {
                this.hp = 40;
                this.attack = 8;
            }
            case "Sorcier" -> {
                this.hp = 20;
                this.attack = 12;
            }
            case "Goblin" -> {
                this.hp = 10;
                this.attack = 25;
            }
            default -> {
                this.hp = 0;
                this.attack = 0;
            }
        }
        this.name = name;
    }

    public int getHp() {
        return this.hp;
    }

    public int getAttack(){
        return this.attack;
    }

    public String getName(){
        return this.name;
    }
}
public class Enemy {
    private int hp;
    private int maxHp;
    private final int attack;
    private final String name;
    public Enemy(String name) {
        switch (name) {
            case "Dragon" -> {
                this.hp = 25;
                this.attack = 2;
            }
            case "Sorcier" -> {
                this.hp = 20;
                this.attack = 1;
            }
            case "Goblin" -> {
                this.hp = 15;
                this.attack = 1;
            }
            default -> {
                this.hp = 0;
                this.attack = 0;
            }
        }
        this.name = name;
        this.maxHp = hp;
    }

    public int getHp() {
        return this.hp;
    }
    public void setHp(int amount){
        this.hp = amount;
    }
    public int getAttack(){
        return this.attack;
    }

    public String getName(){
        return this.name;
    }

    public void takeDamage(int damage) {
        this.hp -= damage;
    }

    public double getMaxHp() {
        return this.maxHp;
    }

    public String getImagePath() {
        String base = "./assets/";
        String end = ".png";
        return base + this.name + end;
    }
}
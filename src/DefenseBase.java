public class DefenseBase {
    //Nom et hp
    protected String nom;
    protected int hp;

    //Setter et getter nom
    public String getNom() {
        return this.nom;
    }

    public void setNom(String newDefenseItem) {
        this.nom = newDefenseItem;
    }

    //Setter et  getter defense
    public int getHp() {
        return this.hp;
    }
}

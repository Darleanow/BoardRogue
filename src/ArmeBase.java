public class ArmeBase {
    //Nom et degats
    protected String nom;
    protected int degats;

    //Setter et getter nom
    public String getNom() {
        return this.nom;
    }

    public void setNom(String newWeaponName) {
        this.nom = newWeaponName;
    }

    //Setter et  getter degats
    public int getDegats() {
        return this.degats;
    }
    public void setDegats(Integer newDamage) {
        this.degats = newDamage;
    }
}

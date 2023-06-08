public class ArmeBase {
    protected String nom;
    protected int degats;

    public String getNom() {
        return this.nom;
    }

    public int getDegats() {
        return this.degats;
    }

    public void setNom(String newWeaponName) {
        this.nom = newWeaponName;
    }

    public void setDegats(Integer newDamage) {
        this.degats = newDamage;
    }
}

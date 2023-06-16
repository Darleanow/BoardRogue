public class ArmeBase {
    //Nom et degats
    protected String nom;
    protected int degats;

    protected Effet effet;

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

    public Effet getEffect() {
        return this.effet;
    }

    public void setEffect(Effet effet) {
        this.effet = effet;
    }
}

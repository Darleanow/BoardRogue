public class Arme extends ArmeBase {
    //Constructeur Arme
    public Arme(String nom, int degats)
    {
        this.nom = nom;
        this.degats = degats;
    }

    //setter nom
    public void setNom(String newWeaponName)
    {
        super.setNom(newWeaponName);
    }

    //Cast  (inutile pour le moment)
    public void cast()
    {
        System.out.println("Attacked!");
    }

    public void setEffect(Effet effet) {
        super.setEffect(effet);
    }

    public void applyEffect() {
        if (this.effet != null) {
            this.effet.applyEffect();
        }
    }

}
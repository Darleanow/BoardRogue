public class Arme extends ArmeBase {


    public Arme(String nom, int degats)
    {
        this.nom = nom;
        this.degats = degats;
    }

    public void setNom(String newWeaponName)
    {
        super.setNom(newWeaponName);
    }

    public void cast()
    {
        System.out.println("Attacked!");
    }

}
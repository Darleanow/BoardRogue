public class Sort extends  ArmeBase{

    //Constructeur sort
    public Sort(String nom, int degats)
    {
        this.nom = nom;
        this.degats = degats;
    }

    //Setter nom
    public void setNom(String newWeaponName)
    {
        super.setNom(newWeaponName);
    }

    //Cast, inutile pour le moment
    public void cast()
    {
        System.out.println("GET THAT SPELL IN THA PIF!");
    }
}

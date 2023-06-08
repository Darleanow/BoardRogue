public class Sort extends  ArmeBase{

    public Sort(String nom, int degats)
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
        System.out.println("GET THAT SPELL IN THA PIF!");
    }
}

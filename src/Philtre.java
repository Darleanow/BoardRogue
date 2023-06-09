public class Philtre extends  DefenseBase{

    //Constructeur Philtre
    public Philtre(String nom, int hp)
    {
        this.nom = nom;
        this.hp = hp;
    }

    //Setter nom
    public void setNom(String newItemName)
    {
        super.setNom(newItemName);
    }

    //Cast, inutile pour le moment
    @SuppressWarnings("unused")
    public void cast()
    {
        System.out.println("DEFENSE THAT SPELL IN DA PIF!");
    }
}

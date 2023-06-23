public class Bouclier extends DefenseBase {
    //Constructeur Bouclier
    public Bouclier(String nom, int hp)
    {
        this.nom = nom;
        this.hp = hp;
        this.maxHp = hp;
    }

    //setter nom
    public void setNom(String newItemName)
    {
        super.setNom(newItemName);
    }

    //Cast  (inutile pour le moment)
    @SuppressWarnings("unused")
    public void cast()
    {
        System.out.println("Defense!");
    }

}
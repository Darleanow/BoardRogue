public abstract class Personnage {
    protected String nom;
    protected int hp;
    protected ArmeBase arme;

    public Personnage(String nom, int hp)
    {
        this.nom = nom;
        this.hp = hp;
    }

    public void setNom(String nom)
    {
        this.nom = nom;
    }

    public String getNom()
    {
        return this.nom;
    }

    public void setHp(int hp)
    {
        this.hp = hp;
    }

    public int getHp()
    {
        return this.hp;
    }

    public ArmeBase getArme() {
        return arme;
    }

    @SuppressWarnings("unused")
    public abstract void attaquer(Personnage personnage);
}
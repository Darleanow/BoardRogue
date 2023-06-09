public abstract class Personnage {
    //Nom, pv et objet de ArmeBase qui permet l'inheritance
    protected String nom;
    protected DefenseBase hp;
    protected ArmeBase arme;

    //Constructeur Personnage
    public Personnage(String nom)
    {
        this.nom = nom;
    }

    //Setter et getter pour nom
    public void setNom(String nom)
    {
        this.nom = nom;
    }

    public String getNom()
    {
        return this.nom;
    }

    public DefenseBase getDefense()
    {
        return hp;
    }

    //récuperer l'arme
    public ArmeBase getArme() {
        return arme;
    }

    //inutile pour le moment
    @SuppressWarnings("unused")
    public abstract void attaquer(Personnage personnage);
}
import java.util.Objects;

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

    public void setEffect(Effet effet) {
        if (this.arme != null) {
            if (Objects.equals(effet.getName(), "Burn")) {
                Burn burn = new Burn();
                this.arme.setEffect(burn);
            } else {
                Stun stun = new Stun();
                this.arme.setEffect(stun);
            }
        }
    }

    private Inventory inventory = new Inventory();

    public Inventory getInventory() {
        return inventory;
    }

    public void changeWeapon(Arme newWeapon) {
        this.arme = newWeapon;
    }

    //inutile pour le moment
    @SuppressWarnings("unused")
    public abstract void attaquer(Enemy ennemy);
}
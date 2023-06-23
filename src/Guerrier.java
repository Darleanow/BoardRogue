public class Guerrier extends Personnage {
    private final Bouclier bouclier;
    //Final pour l'économie de mémoire
    private final Arme arme;

    //Constructeur guerrier, super
    public Guerrier(String nom, Bouclier bouclier, Arme arme)
    {
        //On construit le nom/hp du parent pour économiser la mémoire
        super(nom);
        this.bouclier=bouclier;
        this.arme = arme;
    }

    //Récuperer les infos de classe
    @Override
    public String toString() {
        return "Nom: " + getNom() + ", Classe: " + getClass().getSimpleName();
    }

    @Override
    public Bouclier getDefense() {
        return bouclier;
    }

    //Setter et getter nom
    @Override
    public void setNom(String nom) {
        super.setNom(nom);
    }

    @Override
    public String getNom() {
        return super.getNom();
    }

    //setter et getter arme
    @Override
    public ArmeBase getArme() {
        return arme;
    }

    //attaquer un personnage
    @Override
    public void attaquer(Enemy ennemy)
    {
        arme.cast();
        //ajouter si besoin...
    }
}
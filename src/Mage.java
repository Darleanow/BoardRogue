public class Mage extends Personnage {
    private final Philtre philtre;
    private final Sort sort;

    public Mage(String nom, Philtre philtre, Sort sort)
    {
        super(nom);
        this.philtre = philtre;
        this.sort = sort;
    }

    @Override
    public String toString() {
        return super.toString() + ", Sort: " + getArme().getNom() + ", Dégâts: " + getArme().getDegats();
    }

    @Override
    public Philtre getDefense() {
        return philtre;
    }

    @Override
    public void setNom(String nom) {
        super.setNom(nom);
    }

    @Override
    public String getNom() {
        return super.getNom();
    }

    @Override
    public ArmeBase getArme() {  // Changez le type de retour à ArmeBase
        return sort;
    }

    @Override
    public void attaquer(Personnage personnage)
    {
        sort.cast();
        //Ajouter logique si besoin
    }
}
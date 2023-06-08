public class Mage extends Personnage {
    private final Sort sort;

    public Mage(String nom, int hp, Sort sort)
    {
        super(nom, hp);
        this.sort = sort;
    }

    @Override
    public String toString() {
        return super.toString() + ", Sort: " + getArme().getNom() + ", Dégâts: " + getArme().getDegats();
    }

    @Override
    public void setHp(int hp) {
        super.setHp(hp);
    }

    @Override
    public int getHp() {
        return super.getHp();
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
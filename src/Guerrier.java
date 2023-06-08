public class Guerrier extends Personnage {
    private final Arme arme;

    public Guerrier(String nom, int hp, Arme arme)
    {
        super(nom, hp);
        this.arme = arme;
    }

    @Override
    public String toString() {
        return "Nom: " + getNom() + ", Classe: " + getClass().getSimpleName() + ", HP: " + getHp();
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
    public ArmeBase getArme() {
        return arme;
    }

    @Override
    public void attaquer(Personnage personnage)
    {
        arme.cast();
        //ajouter si besoin...
    }
}
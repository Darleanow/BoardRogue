import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.stream.IntStream;

//Classe Game, s'occupe de l'UI ainsi que de la mise a jour des persos en gros
public class Game extends JFrame {
    //Conteneur bouttons
    private JPanel buttonPanel;
    //Conteneur personnages
    private JPanel characterPanel;
    //On stocke les personnages ici
    private final ArrayList<Personnage> characters;

    //Constructeur
    public Game() {
        //Allocation de la liste
        characters = new ArrayList<>();
        //Mise en place du theme
        setUpLookAndFeel();
        //Creation des bouttons
        initializeButtons();
        //Creation de l'affichage du tableau de personnages
        initializeCharacterPanel();
        //Création de la fenetre
        setUpWindow();
    }

    //Mettre en place un theme, ici FlatDarkLaf (librairie additionnelle)
    private void setUpLookAndFeel() {
        try {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarkLaf());
        } catch( Exception ex ) {
            System.err.println( "Impossible d'initialiser le thème." );
        }
    }

    //Mettre en place la fenetre, titre/taille/ajout des composants
    private void setUpWindow() {
        setTitle("Livrable 4-5");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        addComponentsToWindow();
        setVisible(true);
    }

    //Init des bouttons avec une lambda pour chaque option, permet de simplifier le code
    private void initializeButtons() {
        buttonPanel = new JPanel();
        addButton("Démarrer la partie", e -> openNewGame());
        addButton("Créer un personnage", e -> openCharacterCreationDialog());
        addButton("Editer un personnage", e -> openCharacterSelectionDialog());
        addButton("Supprimer un personnage", e -> openCharacterDeletionDialog());
    }

    //Init du pannel des heros
    private void initializeCharacterPanel() {
        characterPanel = new JPanel();
        characterPanel.setLayout(new BoxLayout(characterPanel, BoxLayout.PAGE_AXIS));
    }

    //Ajouter un boutton
    private void addButton(String text, java.awt.event.ActionListener listener) {
        JButton button = new JButton(text);
        button.addActionListener(listener);
        buttonPanel.add(button);
    }

    //Ajouter un pannel
    private void addComponentsToWindow() {
        add(buttonPanel, BorderLayout.SOUTH);
        add(characterPanel, BorderLayout.CENTER);
    }

    //Check si un charactere a le meme nom que l'argument
    private boolean characterWithNameExists(String name) {
        for (Personnage character : characters) {
            if (character.getNom().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    //Démarrer une partie
    private void openNewGame(){

        if (characters.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Aucun personnage pour jouer...");
            return;
        }
        String[] characterNames = characters.stream().map(Personnage::getNom).toArray(String[]::new);
        String selectedCharacterName = (String) JOptionPane.showInputDialog(this, "Choisir le personnage :", null, JOptionPane.QUESTION_MESSAGE, null, characterNames, characterNames[0]);

        //Si le choix est valide, on créer un nouveau board
        if (selectedCharacterName != null && !selectedCharacterName.isBlank()) {
            for (Personnage character : characters) {
                if (character.getNom().equals(selectedCharacterName)) {
                    @SuppressWarnings("unused")
                    Board board = new Board("Partie en cours pour " + character.getNom());
                }
            }
        }
    }

    //Creation de charactere
    private void openCharacterCreationDialog() {
        //Options disponibles, donc  scalable
        String[] types = { "Guerrier", "Mage" };
        String type = (String) JOptionPane.showInputDialog(this, "Choisir le type de personnage :",
                null, JOptionPane.QUESTION_MESSAGE, null, types, types[0]);

        if (type == null) return;
        //Prompts pour les détails
        String name = JOptionPane.showInputDialog(this, "Entrer le nom du personnage :");
        while (name == null || characterWithNameExists(name)) {
            if (name == null) return;  // If user cancelled
            JOptionPane.showMessageDialog(this, "Un personnage avec ce nom existe déjà. Veuillez choisir un autre nom.");
            name = JOptionPane.showInputDialog(this, "Entrer le nom du personnage :");
        }

        String weaponName = JOptionPane.showInputDialog(this, "Entrer le nom de l'arme ou du sort :");
        if (weaponName == null) return;

        int hp, damage;

        //Affichage des valeurs sous forme de dropdown pour eviter que le joueur triche
        //On peut imaginer en faire des constantes
        String defenseName = JOptionPane.showInputDialog(this, "Entrer le nom de votre item defensif :");
        if (defenseName == null) return;
        if (type.equals("Guerrier")) {
            Integer[] possibleHpValues = IntStream.rangeClosed(10, 15).boxed().toArray(Integer[]::new);
            hp = (Integer) JOptionPane.showInputDialog(this, "Choisir les points de vie du Bouclier :", null, JOptionPane.QUESTION_MESSAGE, null, possibleHpValues, possibleHpValues[0]);

            Integer[] possibleDamageValues = IntStream.rangeClosed(10, 15).boxed().toArray(Integer[]::new);
            damage = (Integer) JOptionPane.showInputDialog(this, "Choisir les dégâts de l'arme :", null, JOptionPane.QUESTION_MESSAGE, null, possibleDamageValues, possibleDamageValues[0]);
        } else {
            Integer[] possibleHpValues = IntStream.rangeClosed(8, 11).boxed().toArray(Integer[]::new);
            hp = (Integer) JOptionPane.showInputDialog(this, "Choisir les points de vie du Philtre :", null, JOptionPane.QUESTION_MESSAGE, null, possibleHpValues, possibleHpValues[0]);

            Integer[] possibleDamageValues = IntStream.rangeClosed(13, 20).boxed().toArray(Integer[]::new);
            damage = (Integer) JOptionPane.showInputDialog(this, "Choisir les dégâts du sort :", null, JOptionPane.QUESTION_MESSAGE, null, possibleDamageValues, possibleDamageValues[0]);
        }

        //Création en mémoire du personnage et ajout a l'ArrayList
        if (hp != 0 && damage != 0) {
            Personnage character;
            if (type.equals("Guerrier")) {
                character = new Guerrier(name, new Bouclier(defenseName, hp), new Arme(weaponName, damage));
            } else {
                character = new Mage(name, new Philtre(defenseName, hp), new Sort(weaponName, damage));
            }
            characters.add(character);
            updateCharacterDisplay();
        }
    }

    //Selection du personnage pour l'édition
    private void openCharacterSelectionDialog() {
        if (characters.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Aucun personnage à éditer.");
            return;
        }
        String[] characterNames = characters.stream().map(Personnage::getNom).toArray(String[]::new);
        String selectedCharacterName = (String) JOptionPane.showInputDialog(this, "Choisir le personnage à éditer :", null, JOptionPane.QUESTION_MESSAGE, null, characterNames, characterNames[0]);
        //Si le choix est valide
        if (selectedCharacterName != null && !selectedCharacterName.isBlank()) {
            for (Personnage character : characters) {
                if (character.getNom().equals(selectedCharacterName)) {
                    // Changer type
                    String[] types = { "Guerrier", "Mage" };
                    String type = (String) JOptionPane.showInputDialog(this,
                            "Choisir le nouveau type de personnage (la modification réinitialisera HP et dégâts) :", null, JOptionPane.QUESTION_MESSAGE, null, types, character.getClass().getSimpleName());
                    if (type != null) {  // Si le type a changé
                        int hp, damage;
                        String defenseName = JOptionPane.showInputDialog(this, "Entrer le nom de votre item defensif :");
                        if (defenseName == null) return;
                        if (type.equals("Guerrier")) {
                            Integer[] possibleHpValues = IntStream.rangeClosed(10, 15).boxed().toArray(Integer[]::new);
                            hp = (Integer) JOptionPane.showInputDialog(this, "Choisir les points de vie du Bouclier :", null, JOptionPane.QUESTION_MESSAGE, null, possibleHpValues, possibleHpValues[0]);

                            Integer[] possibleDamageValues = IntStream.rangeClosed(10, 15).boxed().toArray(Integer[]::new);
                            damage = (Integer) JOptionPane.showInputDialog(this, "Choisir les dégâts de l'arme :", null, JOptionPane.QUESTION_MESSAGE, null, possibleDamageValues, possibleDamageValues[0]);
                        } else {
                            Integer[] possibleHpValues = IntStream.rangeClosed(8, 11).boxed().toArray(Integer[]::new);
                            hp = (Integer) JOptionPane.showInputDialog(this, "Choisir les points de vie du Philtre :", null, JOptionPane.QUESTION_MESSAGE, null, possibleHpValues, possibleHpValues[0]);

                            Integer[] possibleDamageValues = IntStream.rangeClosed(13, 20).boxed().toArray(Integer[]::new);
                            damage = (Integer) JOptionPane.showInputDialog(this, "Choisir les dégâts du sort :", null, JOptionPane.QUESTION_MESSAGE, null, possibleDamageValues, possibleDamageValues[0]);
                        }
                        //Allocation mémoire du nouveau type
                        if (hp != 0 && damage != 0) {
                            characters.remove(character);
                            if (type.equals("Guerrier")) {
                                character = new Guerrier(character.getNom(), new Bouclier(character.getDefense().getNom(), hp), new Arme(character.getArme().getNom(), damage));
                            } else {
                                character = new Mage(character.getNom(), new Philtre(character.getDefense().getNom(), hp), new Sort(character.getArme().getNom(), damage));
                            }
                            characters.add(character);
                        }
                    }

                    // Changer le nom
                    String newName = JOptionPane.showInputDialog(this, "Entrer le nouveau nom du personnage (laisser vide pour ne pas modifier) :");
                    if (newName != null && !newName.isBlank() && !characterWithNameExists(newName)) {
                        character.setNom(newName);
                    }

                    // Changer le nom d'arme
                    String newWeaponName = JOptionPane.showInputDialog(this, "Entrer le nouveau nom de l'arme ou du sort (laisser vide pour ne pas modifier) :");
                    if (newWeaponName != null && !newWeaponName.isBlank()) {
                        character.getArme().setNom(newWeaponName);
                    }

                    // Change les degats de l'arme
                    if (type == null) {  // Si le type de perso n'a pas changé
                        Integer[] possibleDamageValues = (character instanceof Guerrier)
                                ? IntStream.rangeClosed(10, 15).boxed().toArray(Integer[]::new)
                                : IntStream.rangeClosed(13, 20).boxed().toArray(Integer[]::new);
                        Integer newDamage = (Integer) JOptionPane.showInputDialog(this, "Choisir les nouveaux dégâts de l'arme ou du sort :", null, JOptionPane.QUESTION_MESSAGE, null, possibleDamageValues, character.getArme().getDegats());
                        if (newDamage != null) {
                            character.getArme().setDegats(newDamage);
                        }
                    }
                    //Mettre a jour l'affichage
                    updateCharacterDisplay();
                    break;
                }
            }
        }
    }


    //Supprimer un personnage
    private void openCharacterDeletionDialog() {
        String[] characterNames = characters.stream().map(Personnage::getNom).toArray(String[]::new);
        //Dropdown liste personnages
        String name = (String) JOptionPane.showInputDialog(this, "Sélectionner le personnage à supprimer :", null, JOptionPane.QUESTION_MESSAGE, null, characterNames, characterNames[0]);
        if (name == null) return;

        characters.removeIf(character -> character.getNom().equals(name));
        updateCharacterDisplay();
    }



    //Mettre a jour l'affichage
    private void updateCharacterDisplay() {
        characterPanel.removeAll();
        String[] columnNames = { "Nom", "Classe", "Arme/Sort", "Dégâts","Bouclier/Philtre", "Point de vie" };
        String[][] data = new String[characters.size()][6];
        for (int i = 0; i < characters.size(); i++) {
            Personnage character = characters.get(i);
            data[i][0] = character.getNom();
            data[i][1] = character.getClass().getSimpleName();
            data[i][2] = character.getArme().getNom();
            data[i][3] = String.valueOf(character.getArme().getDegats());
            data[i][4] = String.valueOf(character.getDefense().getNom());
            data[i][5] = String.valueOf(character.getDefense().getHp());
        }
        JTable table = new JTable(data, columnNames);
        characterPanel.add(new JScrollPane(table));

        characterPanel.revalidate();
        characterPanel.repaint();
    }
}
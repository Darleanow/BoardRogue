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

    //M
    private void setUpWindow() {
        setTitle("Livrable 5");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        addComponentsToWindow();
        setVisible(true);
    }

    private void initializeButtons() {
        buttonPanel = new JPanel();
        addButton("Créer un personnage", e -> openCharacterCreationDialog());
        addButton("Editer un personnage", e -> openCharacterSelectionDialog());
        addButton("Supprimer un personnage", e -> openCharacterDeletionDialog());
    }

    private void initializeCharacterPanel() {
        characterPanel = new JPanel();
        characterPanel.setLayout(new BoxLayout(characterPanel, BoxLayout.PAGE_AXIS));
    }

    private void addButton(String text, java.awt.event.ActionListener listener) {
        JButton button = new JButton(text);
        button.addActionListener(listener);
        buttonPanel.add(button);
    }

    private void addComponentsToWindow() {
        add(buttonPanel, BorderLayout.SOUTH);
        add(characterPanel, BorderLayout.CENTER);
    }

    private boolean characterWithNameExists(String name) {
        for (Personnage character : characters) {
            if (character.getNom().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    private void openCharacterCreationDialog() {
        String[] types = { "Guerrier", "Mage" };
        String type = (String) JOptionPane.showInputDialog(this, "Choisir le type de personnage :", null, JOptionPane.QUESTION_MESSAGE, null, types, types[0]);

        if (type == null) return;

        String name = JOptionPane.showInputDialog(this, "Entrer le nom du personnage :");
        while (name == null || characterWithNameExists(name)) {
            if (name == null) return;  // If user cancelled
            JOptionPane.showMessageDialog(this, "Un personnage avec ce nom existe déjà. Veuillez choisir un autre nom.");
            name = JOptionPane.showInputDialog(this, "Entrer le nom du personnage :");
        }

        String weaponName = JOptionPane.showInputDialog(this, "Entrer le nom de l'arme ou du sort :");
        if (weaponName == null) return;

        int hp, damage;

        if (type.equals("Guerrier")) {
            Integer[] possibleHpValues = IntStream.rangeClosed(10, 15).boxed().toArray(Integer[]::new);
            hp = (Integer) JOptionPane.showInputDialog(this, "Choisir les points de vie du Guerrier :", null, JOptionPane.QUESTION_MESSAGE, null, possibleHpValues, possibleHpValues[0]);

            Integer[] possibleDamageValues = IntStream.rangeClosed(10, 15).boxed().toArray(Integer[]::new);
            damage = (Integer) JOptionPane.showInputDialog(this, "Choisir les dégâts de l'arme :", null, JOptionPane.QUESTION_MESSAGE, null, possibleDamageValues, possibleDamageValues[0]);
        } else {
            Integer[] possibleHpValues = IntStream.rangeClosed(8, 11).boxed().toArray(Integer[]::new);
            hp = (Integer) JOptionPane.showInputDialog(this, "Choisir les points de vie du Mage :", null, JOptionPane.QUESTION_MESSAGE, null, possibleHpValues, possibleHpValues[0]);

            Integer[] possibleDamageValues = IntStream.rangeClosed(13, 20).boxed().toArray(Integer[]::new);
            damage = (Integer) JOptionPane.showInputDialog(this, "Choisir les dégâts du sort :", null, JOptionPane.QUESTION_MESSAGE, null, possibleDamageValues, possibleDamageValues[0]);
        }

        if (hp != 0 && damage != 0) {
            Personnage character;
            if (type.equals("Guerrier")) {
                character = new Guerrier(name, hp, new Arme(weaponName, damage));
            } else {
                character = new Mage(name, hp, new Sort(weaponName, damage));
            }
            characters.add(character);
            updateCharacterDisplay();
        }
    }

    private void openCharacterSelectionDialog() {
        if (characters.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Aucun personnage à éditer.");
            return;
        }
        String[] characterNames = characters.stream().map(Personnage::getNom).toArray(String[]::new);
        String selectedCharacterName = (String) JOptionPane.showInputDialog(this, "Choisir le personnage à éditer :", null, JOptionPane.QUESTION_MESSAGE, null, characterNames, characterNames[0]);

        if (selectedCharacterName != null && !selectedCharacterName.isBlank()) {
            for (Personnage character : characters) {
                if (character.getNom().equals(selectedCharacterName)) {
                    // Change the type
                    String[] types = { "Guerrier", "Mage" };
                    String type = (String) JOptionPane.showInputDialog(this, "Choisir le nouveau type de personnage (la modification réinitialisera HP et dégâts) :", null, JOptionPane.QUESTION_MESSAGE, null, types, character.getClass().getSimpleName());
                    if (type != null) {  // If the type has been changed
                        int hp, damage;

                        if (type.equals("Guerrier")) {
                            Integer[] possibleHpValues = IntStream.rangeClosed(10, 15).boxed().toArray(Integer[]::new);
                            hp = (Integer) JOptionPane.showInputDialog(this, "Choisir les points de vie du Guerrier :", null, JOptionPane.QUESTION_MESSAGE, null, possibleHpValues, possibleHpValues[0]);

                            Integer[] possibleDamageValues = IntStream.rangeClosed(10, 15).boxed().toArray(Integer[]::new);
                            damage = (Integer) JOptionPane.showInputDialog(this, "Choisir les dégâts de l'arme :", null, JOptionPane.QUESTION_MESSAGE, null, possibleDamageValues, possibleDamageValues[0]);
                        } else {
                            Integer[] possibleHpValues = IntStream.rangeClosed(8, 11).boxed().toArray(Integer[]::new);
                            hp = (Integer) JOptionPane.showInputDialog(this, "Choisir les points de vie du Mage :", null, JOptionPane.QUESTION_MESSAGE, null, possibleHpValues, possibleHpValues[0]);

                            Integer[] possibleDamageValues = IntStream.rangeClosed(13, 20).boxed().toArray(Integer[]::new);
                            damage = (Integer) JOptionPane.showInputDialog(this, "Choisir les dégâts du sort :", null, JOptionPane.QUESTION_MESSAGE, null, possibleDamageValues, possibleDamageValues[0]);
                        }

                        if (hp != 0 && damage != 0) {
                            characters.remove(character);
                            if (type.equals("Guerrier")) {
                                character = new Guerrier(character.getNom(), hp, new Arme(character.getArme().getNom(), damage));
                            } else {
                                character = new Mage(character.getNom(), hp, new Sort(character.getArme().getNom(), damage));
                            }
                            characters.add(character);
                        }
                    }

                    // Change the name
                    String newName = JOptionPane.showInputDialog(this, "Entrer le nouveau nom du personnage (laisser vide pour ne pas modifier) :");
                    if (newName != null && !newName.isBlank() && !characterWithNameExists(newName)) {
                        character.setNom(newName);
                    }

                    // Change weapon name
                    String newWeaponName = JOptionPane.showInputDialog(this, "Entrer le nouveau nom de l'arme ou du sort (laisser vide pour ne pas modifier) :");
                    if (newWeaponName != null && !newWeaponName.isBlank()) {
                        character.getArme().setNom(newWeaponName);
                    }

                    // Change damage
                    if (type == null) {  // Only if type has not been changed
                        Integer[] possibleDamageValues = (character instanceof Guerrier)
                                ? IntStream.rangeClosed(10, 15).boxed().toArray(Integer[]::new)
                                : IntStream.rangeClosed(13, 20).boxed().toArray(Integer[]::new);
                        Integer newDamage = (Integer) JOptionPane.showInputDialog(this, "Choisir les nouveaux dégâts de l'arme ou du sort :", null, JOptionPane.QUESTION_MESSAGE, null, possibleDamageValues, character.getArme().getDegats());
                        if (newDamage != null) {
                            character.getArme().setDegats(newDamage);
                        }
                    }

                    updateCharacterDisplay();
                    break;
                }
            }
        }
    }



    private void openCharacterDeletionDialog() {
        String[] characterNames = characters.stream().map(Personnage::getNom).toArray(String[]::new);
        String name = (String) JOptionPane.showInputDialog(this, "Sélectionner le personnage à supprimer :", null, JOptionPane.QUESTION_MESSAGE, null, characterNames, characterNames[0]);
        if (name == null) return;

        characters.removeIf(character -> character.getNom().equals(name));
        updateCharacterDisplay();
    }



    private void updateCharacterDisplay() {
        characterPanel.removeAll();
        String[] columnNames = { "Nom", "Classe", "HP", "Arme/Sort", "Dégâts" };
        String[][] data = new String[characters.size()][5];
        for (int i = 0; i < characters.size(); i++) {
            Personnage character = characters.get(i);
            data[i][0] = character.getNom();
            data[i][1] = character.getClass().getSimpleName();
            data[i][2] = String.valueOf(character.getHp());
            if (character instanceof Guerrier) {
                data[i][3] = character.getArme().getNom();
                data[i][4] = String.valueOf(character.getArme().getDegats());
            } else if (character instanceof Mage) {
                data[i][3] = character.getArme().getNom();
                data[i][4] = String.valueOf(character.getArme().getDegats());
            }
        }
        JTable table = new JTable(data, columnNames);
        characterPanel.add(new JScrollPane(table));

        characterPanel.revalidate();
        characterPanel.repaint();
    }
}
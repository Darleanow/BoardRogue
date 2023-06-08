import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class Game extends JFrame {
    private JPanel gameBoardPanel;
    private JPanel buttonPanel;
    private JPanel characterPanel;
    private ArrayList<Personnage> characters;

    public Game() {
        characters = new ArrayList<>();
        setUpLookAndFeel();
        initializeGameBoard();
        initializeButtons();
        initializeCharacterPanel();
        setUpWindow();
    }

    private void setUpLookAndFeel() {
        try {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarkLaf());
        } catch( Exception ex ) {
            System.err.println( "Impossible d'initialiser le thème." );
        }
    }

    private void setUpWindow() {
        setTitle("Livrable 5");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        addComponentsToWindow();
        setVisible(true);
    }

    private void initializeGameBoard() {
        gameBoardPanel = new JPanel(new GridLayout(8, 8));
    }

    private void initializeButtons() {
        buttonPanel = new JPanel();
        addButton("Créer un personnage", e -> createCharacter());
        addButton("Editer un personnage", e -> editCharacter());
        addButton("Supprimer un personnage", e -> deleteCharacter());
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
        add(gameBoardPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        add(characterPanel, BorderLayout.EAST);
    }

    private void createCharacter() {
        // Replace with your character creation logic
        Personnage character = new Personnage();
        characters.add(character);
        updateCharacterDisplay();
    }

    private void editCharacter() {
        // Replace with your character editing logic
        // Don't forget to call updateCharacterDisplay() after making changes
    }

    private void deleteCharacter() {
        // Replace with your character deletion logic
        // Don't forget to call updateCharacterDisplay() after making changes
    }

    private void updateCharacterDisplay() {
        characterPanel.removeAll();
        for (Personnage character : characters) {
            characterPanel.add(new JLabel(character.toString()));
        }
        characterPanel.revalidate();
        characterPanel.repaint();
    }
}


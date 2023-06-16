import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Board {
    // Frame de l'ecran
    JFrame frame;
    // Array de boutons, aka les cases
    JButton[][] buttons;
    // Position xy du joueur
    Integer[] xyPlayerPos = {0, 0};

    // Indexe actuel
    int current = 0;

    // Dictionnaire pour les cases spéciales
    Map<String, Integer> specialCases;

    // Créer un plateau
    public Board(String windowName) {
        // Allocation d'une jframe
        this.frame = new JFrame(windowName);
        // Permettre de fermer
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Taille de la frame
        this.frame.setSize(800, 600);

        // Pannel pour le board en gridlayout
        JPanel boardPanel = new JPanel(new GridLayout(8, 8));

        // Initialiser le dictionnaire des cases spéciales
        initializeSpecialCases();

        // Créer les boutons
        createButtons(boardPanel);

        // Quitter
        JButton quitButton = new JButton("Quitter");
        // Lancer les dés
        JButton startButton = new JButton("Lancer les dés");

        // Lambda pour quitter proprement
        quitButton.addActionListener(e -> {
            boardPanel.removeAll();
            createButtons(boardPanel);
            boardPanel.revalidate();
            boardPanel.repaint();
            frame.dispose(); // Fermer la fenetre
            frame.getContentPane().requestFocus(); // Focus  sur le thread principal
        });

        // Simuler un lancer de dés
        startButton.addActionListener(e -> {
            int diceRoll = new Random().nextInt(6) + 1; // Générer un chiffre aléatoire entre 1 compris et 6
            current += diceRoll;
            // Affichage
            JOptionPane.showMessageDialog(frame, "Tu as eu: " + diceRoll +
                    "\nAvancement: " + current + "/64.");
            advancePlayer(diceRoll); // Avancer le joueur

        });

        // Ajout des bouttons
        frame.add(startButton, BorderLayout.NORTH);
        frame.add(quitButton, BorderLayout.SOUTH);

        // Ajout du tableau
        frame.add(boardPanel);

        // Affichage de la fenetre
        frame.setVisible(true);
    }

    // Initialiser les cases spéciales
    private void initializeSpecialCases() {
        specialCases = new HashMap<>();
        specialCases.put("Dragon", 2);
        specialCases.put("Armes", 4);
    }

    // Créer des boutons
    private void createButtons(JPanel boardPanel) {
        // Alocation mémoire
        this.buttons = new JButton[8][8];

        // SImple boucle logique
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                JButton button = new JButton();

                if (i == xyPlayerPos[0] && j == xyPlayerPos[1]) {
                    button.setText("You");
                } else if (i == 7 && j == 0) {
                    button.setText("End");
                } else {
                    String specialCase = getSpecialCase();
                    if (specialCase != null) {
                        button.setText(specialCase);
                    }
                }

                buttons[i][j] = button;
                boardPanel.add(button);
            }
        }
    }

    // Avancer le joueur
    private void advancePlayer(int steps) {
        int i = xyPlayerPos[0];
        int j = xyPlayerPos[1];

        // Si le joueur est tout à gauche, le faire descendre, si le joueur est tout à droite, le faire descendre
        for (int k = 0; k < steps; k++) {
            if (i % 2 == 0) {
                if (j == 7) {
                    i++;
                } else {
                    j++;
                }
            } else {
                if (j == 0) {
                    i++;
                } else {
                    j--;
                }
            }
        }

        xyPlayerPos[0] = i;
        xyPlayerPos[1] = j;
        // Checker si il a gagné
        if (i >= 7 && j <= 0) {
            showVictory();
        }
        // Actualiser les boutons
        updateButtonStates();
    }

    // Actualiser les boutons
    private void updateButtonStates() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                JButton button = buttons[i][j];

                if (i == xyPlayerPos[0] && j == xyPlayerPos[1]) {
                    button.setText("You");
                } else if (i == 7 && j == 0) {
                    button.setText("End");
                } else {
                    String specialCase = getSpecialCase();
                    if (specialCase != null) {
                        button.setText(specialCase);
                    } else {
                        button.setText("");
                    }
                }
            }
        }
    }

    // Obtenir une case spéciale en fonction de la distribution pondérée
    private String getSpecialCase() {
        int randomIndex = new Random().nextInt(10);
        if (randomIndex < 2) {
            return "Dragon";
        } else if (randomIndex < 6) {
            return "Armes";
        }
        return null;
    }

    // Victoire
    private void showVictory() {
        // POP UP
        JOptionPane.showMessageDialog(frame, "Tu as gagné !");
        frame.dispose(); // Fermer la fenetre
        frame.getContentPane().requestFocus(); // Mettre le focus sur le thread fenetre principal
    }
}

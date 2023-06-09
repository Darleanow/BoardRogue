import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Board {
    //Frame de l'ecran
    JFrame frame;
    //Array de bouttons, aka les cases
    JButton[][] buttons;
    //Position xy du joueur
    Integer[] xyPlayerPos = {0,0};

    //Indexe actuel
    int current = 0;
    //Créer un plateau
    public Board(String windowName){
        //Allocation d'une jframe
        this.frame = new JFrame(windowName);
        //Permettre de fermer
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Taille de la frame
        this.frame.setSize(800,600);

        //Pannel pour le board en gridlayout
        JPanel boardPanel = new JPanel(new GridLayout(8, 8));

        // Créer les bouttons
        createButtons(boardPanel);

        // Quitter
        JButton quitButton = new JButton("Quitter");
        // Lancer les dés
        JButton startButton = new JButton("Lancer les dés");

        //Lambda pour quitter proprement
        quitButton.addActionListener(e -> {
            boardPanel.removeAll();
            createButtons(boardPanel);
            boardPanel.revalidate();
            boardPanel.repaint();
            frame.dispose(); // Fermer la fenetre
            frame.getContentPane().requestFocus(); //Focus  sur le thread principal
        });

        // Simuler un lancer de dés
        startButton.addActionListener(e -> {
            int diceRoll = new Random().nextInt(7); // Générer un chiffre aléatoire entre 0 compris et 6
            current += diceRoll;
            //Affichage
            JOptionPane.showMessageDialog(frame, "Tu as eu: " + diceRoll +
                    "\n Avancement: " + current + "/64.");
            advancePlayer(diceRoll); // Avancer le joueur

        });

        //Ajout des bouttons
        frame.add(startButton, BorderLayout.NORTH);
        frame.add(quitButton, BorderLayout.SOUTH);

        //Ajout du tableau
        frame.add(boardPanel);

        //Affichage de la fenetre
        frame.setVisible(true);
    }

    //Créer des bouttons
    private void createButtons(JPanel boardPanel) {
        //Alocation mémoire
        this.buttons = new JButton[8][8];

        //SImple boucle logique
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                JButton button = new JButton();

                if (i == xyPlayerPos[0] && j == xyPlayerPos[1])
                {
                    button.setText("You");
                } else if (i == 7 && j == 0) {
                    button.setText("End");
                }

                buttons[i][j] = button;
                boardPanel.add(button);
            }
        }
    }

    //Avancer le joueur
    private void advancePlayer(int steps) {
        int i = xyPlayerPos[0];
        int j = xyPlayerPos[1];

        //Si le joueur est tout a gauche, le faire descendre, si le joueur est tout a droite, le faire descendre
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
        //Checker si il a gagné
        if (i >= 7 && j <= 0)
        {
            showVictory();
        }
        //ACtualiser les bouttons
        updateButtonStates();
    }

    //Actualiserles bouttons
    private void updateButtonStates() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                JButton button = buttons[i][j];

                if (i == xyPlayerPos[0] && j == xyPlayerPos[1]) {
                    button.setText("You");
                } else if (i == 7 && j == 0) {
                    button.setText("End");
                } else {
                    button.setText("");
                }
            }
        }
    }

    //Victoire
    private void showVictory(){
        //POP UP
        JOptionPane.showMessageDialog(frame, "Tu as gagné !");
        frame.dispose(); // Fermer la fenetre
        frame.getContentPane().requestFocus(); //Mettre le focus sur le thread fenetre principal
    }
}

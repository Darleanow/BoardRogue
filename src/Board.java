import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
//import java.util.Stack;
import java.awt.CardLayout;
public class Board implements ActionListener {
    JFrame frame;
    JButton[][] buttons;
    Integer[] xyPlayerPos = {0, 0};
    int current = 0;
    Map<String, Integer> specialCases;
    List<String> luckyCases;
    private Map<String, Enemy> monsters;

    private CardLayout cardLayout = new CardLayout();
    private JPanel cards = new JPanel(cardLayout);

    private CombatPannel combatPanel;

    private Personnage player;
    public Board(String windowName, Personnage player) {
        monsters = new HashMap<>();
        SwingUtilities.invokeLater(() -> {
            frame = new JFrame(windowName);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);

            this.player = player;

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BorderLayout());

            JPanel boardPanel = new JPanel(new GridLayout(8, 8));
            initializeSpecialCases();
            createButtons(boardPanel);

            JButton quitButton = new JButton("Exit");
            JButton startButton = new JButton("Roll the dice");

            quitButton.addActionListener(e -> frame.dispose());

            startButton.addActionListener(e -> {
                int diceRoll = new Random().nextInt(6) + 1;
                current += diceRoll;
                JOptionPane.showMessageDialog(frame, "You got: " + diceRoll +
                        "\nProgress: " + current + "/64.");
                advancePlayer(diceRoll);
            });

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
            buttonPanel.add(startButton);
            buttonPanel.add(quitButton);

            mainPanel.add(boardPanel, BorderLayout.CENTER);
            mainPanel.add(buttonPanel, BorderLayout.SOUTH);
            cards.add(mainPanel, "Board");

            frame.getContentPane().add(cards);
            frame.setVisible(true);
            CombatPannel combatPanel = new CombatPannel();
            combatPanel.setCards(this.cardLayout,this.cards);
            mainPanel.add(combatPanel, BorderLayout.EAST);
            cards.add(combatPanel, "Combat");
        });
    }

    private void initializeSpecialCases() {
        specialCases = new HashMap<>();
        specialCases.put("Dragon", 4);
        specialCases.put("Sorcier", 10);
        specialCases.put("Goblin", 10);
        specialCases.put("Epee", 4);
        specialCases.put("Massue", 5);
        specialCases.put("Lighting", 5);
        specialCases.put("FireBall", 2);
        specialCases.put("PotionS", 6);
        specialCases.put("PotionM", 2);

        luckyCases = new ArrayList<String>();
        luckyCases.addAll(Arrays.asList("Epee", "Massue", "Lighting", "FireBall", "PotionS", "PotionM"));
    }

    // Créer des boutons
    private void createButtons(JPanel boardPanel) {
        // Allocation mémoire
        this.buttons = new JButton[8][8];

        // Simple boucle logique
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                JButton button = new JButton();
                button.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        // Handle the button click event
                        JButton clickedButton = (JButton) e.getSource();
                        String buttonText = clickedButton.getText();

                        // Display information about the clicked case in a panel or dialog
                        displayCaseInformation(buttonText);
                    }
                });
                button.setForeground(Color.BLACK);
                if (i == xyPlayerPos[0] && j == xyPlayerPos[1]) {
                    button.setText("You");
                    button.setBackground(new Color(83, 156, 190)); // Pale Blue for current player position
                } else if (i == 7 && j == 0) {
                    button.setText("End");
                    button.setBackground(new Color(122, 74, 94)); // Pale Red for end
                } else {
                    String specialCase = getSpecialCase();
                    button.setText(specialCase);
                    button.setName(specialCase);
                    if (specialCase != null) {
                        button.setText(specialCase);
                        // Set background color based on special case
                        switch (specialCase) {
                            case "Lucky Case" ->
                                    button.setBackground(new Color(107, 124, 101)); // Pale Green for lucky cases
                            case "Goblin", "Sorcier" -> {
                                monsters.put(specialCase, new Enemy(specialCase));
                                button.setBackground(new Color(116, 130, 163)); // Pale Gray for enemies
                            }

                            case "Dragon" -> {
                                monsters.put(specialCase, new Enemy(specialCase));
                                button.setBackground(new Color(171, 93, 93));
                            }
                        }
                    }
                }



                // Modify button size
                Dimension buttonSize = new Dimension(100, 100);
                button.setPreferredSize(buttonSize);
                button.setMinimumSize(buttonSize);
                button.setMaximumSize(buttonSize);
                button.setSize(buttonSize);

                buttons[i][j] = button;
                boardPanel.add(button);
            }
        }
    }

    private void displayCaseInformation(String caseText) {
        // Create a dialog box to display case information
        JOptionPane.showMessageDialog(null, "Case information: " + caseText, "Case Information", JOptionPane.INFORMATION_MESSAGE);
    }


    // Handle button click event
    private void handleButtonClick(int x, int y) {
        JButton button = buttons[x][y];
        String buttonText = button.getText();

        // Display information about the clicked case
        switch (buttonText) {
            case "Lucky Case":
                // Handle lucky case information display
                // ...
                break;
            case "Goblin":
                // Handle goblin information display
                // ...
                break;
            case "Sorcier":
                // Handle sorcier information display
                // ...
                break;
            case "Dragon":
                // Handle dragon information display
                // ...
                break;
            default:
                // Handle other cases
                // ...
                break;
        }
    }

    // Obtenir une case spéciale en fonction de la distribution pondérée
    private String getSpecialCase() {
        int totalWeight = specialCases.values().stream().mapToInt(Integer::intValue).sum();
        int randomIndex = new Random().nextInt(totalWeight);

        for (Map.Entry<String, Integer> entry : specialCases.entrySet()) {
            randomIndex -= entry.getValue();
            if (randomIndex < 0) {
                String specialCase = entry.getKey();
                if (luckyCases.contains(specialCase)) {
                    int caseCount = countSpecialCase(specialCase);
                    if (caseCount >= specialCases.get(specialCase)) {
                        luckyCases.remove(specialCase);
                        return null; // Skip adding more than the desired count
                    }
                    return "Lucky Case";
                } else if (specialCase.equals("Dragon")) {
                    int dragonCount = countSpecialCase("Dragon");
                    if (dragonCount >= specialCases.get("Dragon")) {
                        return null; // Skip adding more dragons than the desired count
                    }
                    return "Dragon";
                } else if (specialCase.equals("Goblin")) {
                    int goblinCount = countSpecialCase("Goblin");
                    if (goblinCount >= specialCases.get("Goblin")) {
                        return null; // Skip adding more goblins than the desired count
                    }
                    return "Goblin";
                } else if (specialCase.equals("Sorcier")) {
                    int sorcierCount = countSpecialCase("Sorcier");
                    if (sorcierCount >= specialCases.get("Sorcier")) {
                        return null; // Skip adding more sorciers than the desired count
                    }
                    return "Sorcier";
                } else if (countSpecialCase(specialCase) >= 1) {
                    return null; // Skip adding more than one of non-lucky special cases
                }
                return specialCase;
            }
        }

        return null;
    }

    // Count the occurrence of a special case on the board
    private int countSpecialCase(String specialCase) {
        int count = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                JButton button = buttons[i][j];
                if (button != null && button.getText() != null) {
                    if (button.getText().equals(specialCase)){
                        count++;
                    }
                }
            }
        }
        return count;
    }

    // Avancer le joueur
    private void advancePlayer(int steps) {
        int i = xyPlayerPos[0];
        int j = xyPlayerPos[1];

        // Calculate the new position of the player
        int newI;
        int newJ;
        if (i % 2 == 0) {
            newI = i;
            newJ = j + steps;
            if (newJ > 7) {
                newI += 1;
                newJ = 7 - (newJ % 8);
            }
        } else {
            newI = i;
            newJ = j - steps;
            if (newJ < 0) {
                newI += 1;
                newJ = -newJ - 1;
            }
        }

        // Check if the new position exceeds board boundaries
        if (newI > 7) {
            newI = 7;
            newJ = 0; // Make sure to reach the last cell when newI becomes 7.
        }
        if (newJ > 7) {
            newJ = 7;
        }
        if (newI < 0) {
            newI = 0;
        }
        if (newJ < 0) {
            newJ = 0;
        }

        if (buttons[xyPlayerPos[0]][xyPlayerPos[1]] != null &&
                buttons[xyPlayerPos[0]][xyPlayerPos[1]].getName() != null) {
            if (buttons[xyPlayerPos[0]][xyPlayerPos[1]].getName().equals("Dragon") ||
                    buttons[xyPlayerPos[0]][xyPlayerPos[1]].getName().equals("Goblin") ||
                    buttons[xyPlayerPos[0]][xyPlayerPos[1]].getName().equals("Sorcier")) {

                Enemy monster = monsters.get(buttons[xyPlayerPos[0]][xyPlayerPos[1]].getName());
                combatPanel.startBattle(player, monster);
                cardLayout.show(cards, "Combat");
            }
        }
        buttons[xyPlayerPos[0]][xyPlayerPos[1]].setText("");
        buttons[0][0].setText("Start");
        // Update the player's position
        xyPlayerPos[0] = newI;
        xyPlayerPos[1] = newJ;

        // Check if the player has won
        if (newI >= 7 && newJ <= 0) {
            showVictory();
        }

        // Update the button states
        updateButtonStates();
    }


    private void updateButtonStates() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                JButton button = buttons[i][j];

                if (i == xyPlayerPos[0] && j == xyPlayerPos[1]) {
                    button.setText("You");
                    button.setName("You");
                } else if (i == 7 && j == 0) {
                    button.setText("End");
                    button.setName("End");
                }
            }
        }
    }


    public void actionPerformed(ActionEvent e) {
        // Get the source of the action
        JButton button = (JButton) e.getSource();

        // Get the button's text
        String buttonText = button.getText();

        try {
            // Parse the button text as an integer
            int steps = Integer.parseInt(buttonText);

            // Advance the player by the specified number of steps
            advancePlayer(steps);
        } catch (NumberFormatException ex) {
            // Handle non-numeric inputs (e.g., "Lucky Case")
            if (buttonText.equals("Lucky Case")) {
                // Open a loot box or add potion/equipment to inventory
                //openLootBox();
                System.out.println("GJ LOOT");
            } else if (buttonText.equals("Dragon")) {
                // Fight the monster
                //fightMonster();
                System.out.println("GJ BOSS");
            } else if (buttonText.equals("Goblin") || buttonText.equals("Sorcier")) {
                // Fight the monster
                //fightMonster();
                System.out.println("GJ ENNEMI");
            } else {
                System.out.println("BLANK");
            }
        }
    }


    // Victoire
    private void showVictory() {
        // POP UP
        JOptionPane.showMessageDialog(frame, "Tu as gagné !");
        frame.dispose(); // Fermer la fenetre
        frame.getContentPane().requestFocus(); // Mettre le focus sur le thread fenetre principal
    }
}

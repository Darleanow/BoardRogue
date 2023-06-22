import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Board {
    private final Map<JButton, String> buttonOrigins = new HashMap<>();
    private static final Map<String, Integer> SPECIAL_CASES = Map.of(
            "Dragon", 4,
            "Sorcier", 10,
            "Goblin", 10,
            "Epee", 4,
            "Massue", 5,
            "Lighting", 5,
            "FireBall", 2,
            "PotionS", 6,
            "PotionM", 2
    );

    private Map<String, Integer> specialCases = new HashMap<>(SPECIAL_CASES);
    private final Map<String, String> luckyCasesContents = new HashMap<>();

    private static final List<String> LUCKY_CASES = List.of(
            "Epee", "Massue", "Lighting", "FireBall", "PotionS", "PotionM"
    );

    private final JFrame frame;
    private final JButton[][] buttons = new JButton[8][8];
    private final Map<String, Enemy> monsters = new HashMap<>();
    private final Personnage player;
    private CardLayout cardLayout = new CardLayout();
    private final JPanel cards = new JPanel(cardLayout);
    private final CombatPanel combatPanel;
    private int current = 0;
    private final Point playerPos = new Point(0, 0);

    public Board(String windowName, Personnage player) {

        frame = new JFrame(windowName);
        this.player = player;

        // Change to cards.setLayout(new CardLayout());
        cards.setLayout(new CardLayout());

        // Create mainPanel before assigning it to cards
        JPanel mainPanel = setupMainPanel();

        // Assign mainPanel to cards
        cards.add(mainPanel, "Board");

        combatPanel = new CombatPanel();
        combatPanel.setCards(cards);

        // Add combatPanel to cards
        cards.add(combatPanel, "Combat");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.getContentPane().add(cards);
        frame.setVisible(true);


        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);  // Disable default close operation
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Reset player's HP to maximum value before exiting
                player.getDefense().setHp(player.getDefense().getMaxHp());
                frame.dispose();  // Close the frame
            }
        });
    }

    private JPanel setupMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel boardPanel = new JPanel(new GridLayout(8, 8));

        setupButtons(boardPanel);

        JPanel buttonPanel = setupControlButtons();
        mainPanel.add(boardPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        return mainPanel;
    }

    private void setupButtons(JPanel boardPanel) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                JButton button = new JButton();
                setupButtonState(button, i, j);
                button.addActionListener(new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (button.getText().equals("You")) {
                            displayCaseInformation(buttonOrigins.get(button));
                        }
                    }
                });
                boardPanel.add(button);
                buttons[i][j] = button;
            }
        }
    }

    private JPanel setupControlButtons() {
        JButton quitButton = new JButton("Exit");
        quitButton.addActionListener(e -> frame.dispose());

        JButton startButton = new JButton("Roll the dice");
        startButton.addActionListener(e -> rollDice());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.add(startButton);
        buttonPanel.add(quitButton);

        return buttonPanel;
    }

    private void setupButtonState(JButton button, int i, int j) {
        button.addActionListener(e -> {
            if (i == playerPos.x && j == playerPos.y) {
                displayCaseInformation(button.getText());
            }
        });
        if (i == playerPos.x && j == playerPos.y) {
            button.setText("You");
            button.setBackground(new Color(83, 156, 190));
            button.setForeground(Color.BLACK);
        } else if (i == 7 && j == 7) {  // "End" is moved to the bottom-right position
            button.setText("End");
            button.setBackground(new Color(122 , 200, 109));
            button.setForeground(Color.BLACK);
        } else {
            setRandomEnemy(button, i, j);
        }
    }

    private void setRandomEnemy(JButton button, int i, int j) {
        // Generate list of all possible cases according to their quantities
        List<String> options = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : SPECIAL_CASES.entrySet()) {
            options.addAll(Collections.nCopies(entry.getValue(), entry.getKey()));
        }

        if (options.isEmpty()) {
            button.setText("");
            button.setBackground(Color.GRAY);
            button.setForeground(Color.BLACK);
            return;
        }

        Random rand = new Random();
        String enemyName = options.get(rand.nextInt(options.size()));
        specialCases.put(enemyName, specialCases.getOrDefault(enemyName, 0) + 1);

        if (LUCKY_CASES.contains(enemyName)) {
            button.setText("Caisse Chance");
            button.setBackground(new Color(222, 207, 63));
            button.setForeground(Color.WHITE);
            luckyCasesContents.put(button.getText(), enemyName);  // Associate "Caisse Chance" with actual item
        } else {
            button.setText(enemyName);
            button.setBackground(new Color(182, 8, 8));
            button.setForeground(Color.WHITE);
            monsters.put(button.getText(), new Enemy(enemyName));
        }

        // populate the original text into buttonOrigins map
        buttonOrigins.put(button, button.getText());
    }


    private void rollDice() {
        Random random = new Random();
        current = random.nextInt(6) + 1;

        int newX = playerPos.x;
        int newY = playerPos.y;

        if ((playerPos.x + current) / 8 > playerPos.x / 8) {
            newX = (playerPos.x + current) % 8;
            newY++;
        } else {
            newX = playerPos.x + current;
        }

        // If newX or newY is greater than 7, set it to 7
        newX = Math.min(newX, 7);
        newY = Math.min(newY, 7);

        String originalText = buttonOrigins.get(buttons[playerPos.x][playerPos.y]);

        // Reset the button text to the original string when the player leaves
        buttons[playerPos.x][playerPos.y].setText(originalText);
        buttons[playerPos.x][playerPos.y].setBackground(new Color(255, 255, 255));

        playerPos.setLocation(newX, newY);

        // If the player reaches the end, display the winning message and close the game
        if (newX >= 7 && newY >= 7) {
            JOptionPane.showMessageDialog(frame, "Congratulations! You reached the end. Here are your stats: " + player);
            frame.dispose();
            return;
        }

        setupButtonState(buttons[newX][newY], newX, newY);
        // We get the original text before the move to call the method.
        displayCaseInformation(originalText);
        setupButtonState(buttons[newX][newY], newX, newY);
    }

    private void displayCaseInformation(String text) {
        String originalText = buttonOrigins.get(buttons[playerPos.x][playerPos.y]);

        // Check if the originalText is null
        if (originalText == null) {
            JOptionPane.showMessageDialog(frame, "This is an empty case.");
            return;
        }

        // If it's a "Caisse Chance", we need to handle it differently
        if (originalText.equals("Caisse Chance")) {
            // Retrieve actual item associated with "Caisse Chance"
            String itemName = luckyCasesContents.get(buttons[playerPos.x][playerPos.y]);

            // Add conditions here for each item type
            switch (itemName) {
                case "PotionS", "PotionM" -> {
                    player.getInventory().add(itemName); // add the potion to the player inventory
                    JOptionPane.showMessageDialog(frame, "En ouvrant la caisse, tu trouves une " + itemName);
                }
                case "Epee", "Massue" -> {
                    if (player.getClass() == Mage.class) {
                        JOptionPane.showMessageDialog(frame, "Tu ne peux pas prendre cela car tu es un Mage.");
                    } else {
                        player.getArme().setDegats(itemName.equals("Epee") ? 3 : 5);
                        player.getArme().setNom(itemName);
                        if (itemName.equals("Massue")) player.getArme().setEffect(new Stun());
                        JOptionPane.showMessageDialog(frame, "En ouvrant la caisse, tu trouves une " + itemName);
                    }
                }
                case "Eclair", "Boule de Feu" -> {
                    if (player.getClass() == Guerrier.class) {
                        JOptionPane.showMessageDialog(frame, "Tu ne peux pas prendre cela car tu es un Guerrier.");
                    } else {
                        player.getArme().setNom(itemName);
                        player.getArme().setDegats(itemName.equals("Eclair") ? 10 : 15);
                        if (itemName.equals("Boule de Feu")) {
                            player.getArme().setEffect(new Burn());
                        } else {
                            player.getArme().setEffect(new Stun());
                        }
                        JOptionPane.showMessageDialog(frame, "Une caisse chance, en l'ouvrant tu trouves un sort de  " + itemName);
                    }
                }
                default -> JOptionPane.showMessageDialog(frame, "Une caisse chance, mais tu ne trouves rien.");
            }
            return;
        }

        // Check if the player reached the end
        if (playerPos.x >= 7 && playerPos.y >= 7) {
            JOptionPane.showMessageDialog(frame, "Congratulations! You reached the end. Here are your stats: " + player);
            frame.dispose();
            return;
        }

        Enemy enemy = monsters.get(originalText);
        if (enemy == null) {
            JOptionPane.showMessageDialog(frame, "No enemy found for the case: " + originalText);
            return;
        }

        cardLayout = (CardLayout) cards.getLayout(); // Get the CardLayout from the parent container
        cardLayout.show(cards, "Combat");
        combatPanel.startBattle(player, enemy);
    }
}
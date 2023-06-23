import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.text.*;
import java.util.Objects;

public class CombatPanel extends JPanel {
    private Personnage player;
    private Enemy enemy;

    private JButton potionButton;
    private JButton attackButton;
    private JButton runButton;

    private JTextPane combatLog;
    private JLabel playerLabel;
    private JLabel enemyLabel;
    private JLabel playerStatsLabel;
    private JLabel enemyStatsLabel;
    private JProgressBar playerHpBar;
    private JProgressBar enemyHpBar;

    private CardLayout cardLayout;
    private JPanel cards;

    private BufferedImage playerIcon;
    private BufferedImage enemyIcon;
    private ImageIcon heartIcon;
    private ImageIcon swordIcon;

    private JLabel enemyNameLabel;

    public CombatPanel(JPanel cards) {
        cardLayout = (CardLayout) cards.getLayout();
        setLayout(new BorderLayout());

        this.cards = cards;

        // Load icons
        playerIcon = resizeImage(loadImage("./assets/swordsman.png"), 100, 100);
        heartIcon = new ImageIcon("./assets/heart.png");
        swordIcon = new ImageIcon("./assets/sword.png");

        potionButton = new JButton("Use Potion");
        attackButton = new JButton("Attack");
        runButton = new JButton("Run");

        attackButton.addActionListener(e -> {
            attack();
            endTurn();
            endBattle();
        });

        potionButton.addActionListener(e -> {
            if (player != null) {
                int amount =  player.getInventory().usePotion();
                switch (amount) {
                    case -1 -> appendColoredText(combatLog, "Pas de potions.\n", Color.BLACK);
                    case 5 -> {
                        player.getDefense().setHp(Math.min(player.getDefense().getHp() + amount, player.getDefense().getMaxHp()));
                        appendColoredText(combatLog, "Tu as utilisé une PotionS.\n", Color.BLACK);
                    }
                    case 10 -> {
                        player.getDefense().setHp(Math.min(player.getDefense().getHp() + amount, player.getDefense().getMaxHp()));
                        appendColoredText(combatLog, "Tu as utilisé une PotionM.\n", Color.BLACK);
                    }
                }
                updateCharacterStats();
                revalidate();
                repaint();
            }
        });


        runButton.addActionListener(e -> {
            run();
            endBattle();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(attackButton);
        buttonPanel.add(potionButton);
        buttonPanel.add(runButton);

        combatLog = new JTextPane();
        combatLog.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(combatLog);


        playerLabel = new JLabel(new ImageIcon(playerIcon));
        enemyLabel = new JLabel();
        playerStatsLabel = new JLabel();
        enemyStatsLabel = new JLabel();
        playerHpBar = new JProgressBar();
        playerHpBar.setStringPainted(true);
        playerHpBar.setForeground(Color.GREEN);
        JPanel playerPanel = new JPanel(new BorderLayout());
        playerPanel.add(playerStatsLabel, BorderLayout.CENTER);
        playerPanel.setOpaque(false);
        enemyHpBar = new JProgressBar();
        enemyHpBar.setStringPainted(true);
        enemyHpBar.setForeground(Color.GREEN);

        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.X_AXIS));
        statsPanel.add(playerLabel);
        statsPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        statsPanel.add(playerStatsLabel);
        statsPanel.add(Box.createHorizontalGlue());
        statsPanel.add(enemyStatsLabel);
        statsPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        statsPanel.add(enemyLabel);
        statsPanel.setOpaque(false);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(statsPanel, BorderLayout.NORTH);
        bottomPanel.add(buttonPanel, BorderLayout.CENTER);

        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Initialize enemy components
        enemyNameLabel = new JLabel();
        enemyStatsLabel = new JLabel();
        enemyStatsLabel.setVerticalAlignment(JLabel.TOP);
        enemyStatsLabel.setHorizontalAlignment(JLabel.LEFT);

        JPanel enemyPanel = new JPanel(new BorderLayout());
        enemyPanel.add(enemyNameLabel, BorderLayout.WEST);
        enemyPanel.add(enemyStatsLabel, BorderLayout.CENTER);
        enemyPanel.setOpaque(false);
        statsPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        statsPanel.add(enemyPanel);
    }

    public void setPlayer(Personnage player) {
        this.player = player;
    }

    public void setEnemy(Enemy enemy, String imagePath) {
        this.enemy = enemy;
        updateEnemyStats();
        enemyLabel.setIcon(new ImageIcon(resizeImage(loadImage(imagePath), 100, 100)));
        enemyNameLabel.setText(enemy.getName());
    }



    private void attack() {
        if (player != null && enemy != null) {
            int damage = player.getArme().getDegats();
            enemy.takeDamage(damage);
            updateEnemyStats();

            if (enemy.getHp() <= 0) {
                enemy.setHp(0);
            }

            appendColoredText(combatLog, "Tu attaque " + enemy.getName() + " pour " + damage + " dégats.\n", Color.BLACK);
        }
    }

    private void run() {
        if (player != null && enemy != null) {
            appendColoredText(combatLog, "Tu t'enfuis du " + enemy.getName() + ".\n", Color.BLACK);
            cardLayout.show(cards, "Board");
        }
    }

    private void endTurn() {
        if (enemy != null) {
            int damage = enemy.getAttack();
            player.getDefense().setHp(player.getDefense().getHp() - damage);
            appendColoredText(combatLog, "Le " + enemy.getName() + " t'as fait " + damage + " dégats.\n", Color.BLACK);
            updateCharacterStats();
        }
    }

    private void endBattle() {
        if (player != null && enemy != null) {
            if (player.getDefense().getHp() <= 0) {
                appendColoredText(combatLog, "Défaite.\n", Color.RED);
                showPlayerDefeatedDialog(); // Display player defeated dialog
                cardLayout.show(cards, "Board"); // Return to the board only after the defeat
            } else if (enemy.getHp() <= 0) {
                appendColoredText(combatLog, "Tu as tué le " + enemy.getName() + ".\n", Color.GREEN);
                showEnemyKilledDialog(enemy.getName()); // Display enemy killed dialog
                combatLog.setText(""); // Clear the combat log after victory
                cardLayout.show(cards, "Board"); // Return to the board only after the victory
            }
        }
    }



    private void showPlayerDefeatedDialog() {
        String message = "Défaite";
        JOptionPane.showMessageDialog(this, message, "Défaite", JOptionPane.INFORMATION_MESSAGE);
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING)); // Close only the battle frame when the player is defeated
    }

    private void showEnemyKilledDialog(String enemyName) {
        String message = "Tu as tué le " + enemyName + "!";
        JOptionPane.showMessageDialog(this, message, "Victoire", JOptionPane.INFORMATION_MESSAGE);
    }

    public void updateCharacterStats() {
        if (player != null) {
            int hp = player.getDefense().getHp() < 0 ? 0 : player.getDefense().getHp(); // Prevent hp from going below 0
            String characterStats = "<html><font color='black'>Character Stats:<br>";
            characterStats += "Nom: " + player.getNom() + "<br>";
            characterStats += "HP: " + hp + " / " + player.getDefense().getMaxHp() + "</font></html>";
            playerStatsLabel.setText(characterStats);
        }
    }


    private void updateEnemyStats() {
        if (enemy != null) {
            int hp = enemy.getHp() < 0 ? 0 : enemy.getHp(); // Prevent hp from going below 0
            String enemyStats = "<html><font color='black'>Enemy Stats:<br>";
            enemyStats += "Nom: " + enemy.getName() + "<br>";
            int hpPercentage = (int) (((double) hp / enemy.getMaxHp()) * 100);
            enemyStats += "HP: " + hpPercentage + "%</font></html>";
            enemyStatsLabel.setText(enemyStats);
            enemyHpBar.setValue(hpPercentage);
        }
    }

    private BufferedImage loadImage(String imagePath) {
        try {
            return ImageIO.read(getClass().getResource(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private BufferedImage resizeImage(BufferedImage image, int width, int height) {
        Image resizedImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage bufferedResizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedResizedImage.createGraphics();
        g2d.drawImage(resizedImage, 0, 0, null);
        g2d.dispose();
        return bufferedResizedImage;
    }

    private void appendColoredText(JTextPane textPane, String text, Color color) {
        StyledDocument doc = textPane.getStyledDocument();
        Style style = textPane.addStyle("coloredText", null);
        StyleConstants.setForeground(style, color);
        try {
            doc.insertString(doc.getLength(), text, style);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}

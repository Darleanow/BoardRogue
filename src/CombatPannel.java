import javax.swing.*;
import java.awt.*;

public class CombatPannel extends JPanel {

    private Personnage player;
    private Enemy monster;

    private JButton attackButton;
    private JButton defendButton;

    private JTextArea combatLog;

    private CardLayout cardLayout;
    private JPanel cards;

    public CombatPannel() {
    }

    private void init(Personnage player, Enemy monster) {
        setLayout(new BorderLayout());

        this.player = player;
        this.monster = monster;

        attackButton = new JButton("Attack");
        defendButton = new JButton("Defend");

        attackButton.addActionListener(e -> {
            attack();
            endTurn();
        });

        defendButton.addActionListener(e -> {
            defend();
            endTurn();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(attackButton);
        buttonPanel.add(defendButton);

        combatLog = new JTextArea();
        combatLog.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(combatLog);

        add(buttonPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void attack() {
        // Player attacks the monster
        // ...
        combatLog.append("You attacked the " + monster.getName() + ".\n");
        // Update the combat log with the attack results
    }

    private void defend() {
        // Player defends against the monster's next attack
        // ...
        combatLog.append("You are preparing to defend against the " + monster.getName() + "'s next attack.\n");
        // Update the combat log with the defend results
    }

    private void endTurn() {
        // The monster takes its turn
        // ...
        combatLog.append("The " + monster.getName() + " takes its turn.\n");
        // Update the combat log with the results of the monster's turn
    }

    public void setMonster(Enemy monster) {
        this.monster = monster;
    }

    public void startBattle(Personnage player, Enemy monster) {
        this.player = player;
        this.monster = monster;
        combatLog.setText("");
        combatLog.append("You encountered a " + monster.getName() + ".\n");
    }

    private void endBattle() {
        if (player.hp.getHp() <= 0) {
            combatLog.append("You have been defeated.\n");
            // Handle player defeat...
        } else {
            combatLog.append("You defeated the " + monster.getName() + ".\n");
            // Handle monster defeat...
        }
        cardLayout.show(cards, "Board");
    }

    public void setCards(CardLayout layout,JPanel card){
        this.cardLayout = layout;
        this.cards = card;
    }
}

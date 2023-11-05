package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GamePanel extends JPanel implements ActionListener {
    private JButton[] letterButtons;
    private JLabel wordLabel;
    private JLabel livesLabel;
    private JLabel hintLabel;
    private HangmanGame game;

    public GamePanel() {
        game = new HangmanGame();
        initializeComponents();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(255, 255, 200));

        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        topPanel.setBackground(new Color(255, 255, 200));

        livesLabel = new JLabel("Lives left: " + game.getAttemptsLeft());
        livesLabel.setFont(new Font("Arial", Font.BOLD, 18));
        livesLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        livesLabel.setForeground(Color.RED);
        topPanel.add(livesLabel);

        hintLabel = new JLabel(" " + game.getHint(game.getSecretWord()));
        hintLabel.setFont(new Font("Arial", Font.BOLD, 25));
        hintLabel.setHorizontalAlignment(SwingConstants.CENTER);
        hintLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        topPanel.add(hintLabel);

        add(topPanel, BorderLayout.NORTH);

        wordLabel = new JLabel(game.getGuessedWord());
        wordLabel.setFont(new Font("Arial", Font.BOLD, 36));
        wordLabel.setHorizontalAlignment(SwingConstants.CENTER);
        wordLabel.setForeground(new Color(50, 100, 200));

        add(wordLabel, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel(new GridLayout(2, 13, 5, 5));
        buttonsPanel.setBackground(new Color(255, 255, 200));

        letterButtons = new JButton[26];
        for (int i = 0; i < 26; i++) {
            char letter = (char) ('a' + i);
            letterButtons[i] = new JButton(String.valueOf(letter));
            letterButtons[i].setFont(new Font("Arial", Font.BOLD, 20));
            letterButtons[i].setBackground(new Color(100, 200, 100));
            letterButtons[i].setFocusPainted(false);
            letterButtons[i].setMargin(new Insets(10, 10, 10, 10));
            letterButtons[i].addActionListener(this);
            buttonsPanel.add(letterButtons[i]);
        }

        add(buttonsPanel, BorderLayout.SOUTH);
    }

    private void resetGame() {
        game = new HangmanGame();
        removeAll();
        initializeComponents();
        revalidate();
        repaint();
    }

    private void updateGameState() {
        wordLabel.setText(game.getGuessedWord());
        livesLabel.setText("Lives left: " + game.getAttemptsLeft());
        if (game.isGameOver()) {
            for (JButton button : letterButtons) {
                button.setEnabled(false);
            }

            String message;
            if (game.isGameWon()) {
                game.incrementScore();
                message = "You won!\nDo you want to play again?";

            } else {
                message = "You lost. The word was: " + game.getSecretWord() + "\nDo you want to play again?";
            }

            int response = JOptionPane.showConfirmDialog(this, message, "Game Over",
                    JOptionPane.YES_NO_OPTION);

            if (response == JOptionPane.YES_OPTION) {
                resetGame();
            } else {
                System.exit(0);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        char letter = button.getText().charAt(0);

        if (game.guessLetter(letter)) {
            button.setEnabled(false);
        }

        updateGameState();
    }
}

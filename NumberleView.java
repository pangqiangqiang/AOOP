import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class NumberleView extends JFrame implements Observer {
    private INumberleModel model;
    private NumberleController controller;
    private JPanel keyboardPanel, historyPanel;
    private JButton newGameButton;
    private JLabel attemptsLabel;
    private JTextField guessField;
    private JTextField inputField;

    public NumberleView(INumberleModel model, NumberleController controller) {
        this.model = model;
        this.controller = controller;
        // 注册观察者
        ((NumberleModel) this.model).addObserver(this);
        initializeUI();

        controller.setView(this);

    }

    private void initializeUI() {
        setTitle("Numberle Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLayout(new BorderLayout());

        inputField = new JTextField();
        inputField.setEditable(false);
        add(inputField, BorderLayout.NORTH);

        guessField = new JTextField();
        guessField.setEditable(false);
        add(guessField, BorderLayout.SOUTH);

        // 设置键盘区域大小
        keyboardPanel = new JPanel(new GridLayout(4, 5, 2, 2));
        keyboardPanel.setPreferredSize(new Dimension(300, 100));
        addKeys();
        add(keyboardPanel, BorderLayout.CENTER);

        // 设置历史记录区域大小
        historyPanel = new JPanel(new GridLayout(6, 1));
        historyPanel.setPreferredSize(new Dimension(300, 300));
        add(historyPanel, BorderLayout.WEST);

        newGameButton = new JButton("New Game");
        newGameButton.addActionListener(e -> controller.startNewGame());
        add(newGameButton, BorderLayout.EAST);

        attemptsLabel = new JLabel("Attempts left: 6");
        add(attemptsLabel, BorderLayout.PAGE_END);

        setVisible(true);
    }


    private void addKeys() {
        String[] keys = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "+", "-", "*", "/", "=", "Enter", "C"};
        for (String key : keys) {
            JButton button = new JButton(key);
            button.addActionListener(this::handleKeyPress);
            keyboardPanel.add(button);
        }
    }

    private void handleKeyPress(ActionEvent e) {
        String key = ((JButton) e.getSource()).getText();
        if ("Enter".equals(key)) {
            if (!inputField.getText().isEmpty()) {
                controller.processGuess(inputField.getText());
                inputField.setText("");
            }
        } else if ("C".equals(key)) {
            inputField.setText("");
        } else {
            inputField.setText(inputField.getText() + key);
        }
    }

    public void updateHistory(String guess, Map<Character, String> results) {
        boolean shouldAddRow = false;  // 标记是否应该添加这行到历史记录
        JPanel row = new JPanel(new GridLayout(1, guess.length()));
        row.setBackground(Color.WHITE);

        for (int i = 0; i < guess.length(); i++) {
            char ch = guess.charAt(i);
            JLabel label = new JLabel(String.valueOf(ch), SwingConstants.CENTER);
            label.setOpaque(true);
            label.setBackground(Color.LIGHT_GRAY);

            if (results.containsKey(ch)) {
                switch (results.get(ch)) {
                    case "correct":
                        label.setBackground(Color.GREEN);
                        shouldAddRow = true;
                        break;
                    case "misplaced":
                        label.setBackground(Color.ORANGE);
                        shouldAddRow = true;
                        break;
                    default:
                        label.setBackground(Color.GRAY);
                        break;
                }
            }
            row.add(label);
        }

        if (shouldAddRow) {
            historyPanel.add(row);
            historyPanel.revalidate();
            historyPanel.repaint();
        }
    }

    public void updateKeyColors(Map<Character, String> results) {
        for (Component comp : keyboardPanel.getComponents()) {
            JButton button = (JButton) comp;
            if (results != null && results.containsKey(button.getText().charAt(0))) {
                switch (results.get(button.getText().charAt(0))) {
                    case "correct":
                        button.setBackground(Color.GREEN);
                        break;
                    case "misplaced":
                        button.setBackground(Color.ORANGE);
                        break;
                    default:
                        button.setBackground(Color.GRAY);
                        break;
                }
            } else {
                button.setBackground(UIManager.getColor("Button.background"));
            }
        }
    }

    public void updateAttempts(int remainingAttempts) {
        attemptsLabel.setText("Attempts left: " + remainingAttempts);
    }

    public void showGameOver(boolean won, String targetEquation) {
        String message = won ? "Congratulations! You've guessed correctly!" : "Game over! The correct equation was: " + targetEquation;
        JOptionPane.showMessageDialog(this, message, "Game Over", JOptionPane.INFORMATION_MESSAGE);
    }
    public void resetGame() {
        historyPanel.removeAll();
        historyPanel.revalidate();
        historyPanel.repaint();
        updateKeyColors(null);
        attemptsLabel.setText("Attempts left: 6");
        newGameButton.setEnabled(true);
    }


    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void update(Observable o, Object arg) {
        try {
            String guess = inputField.getText();
            Map<Character, String> results = model.checkGuess(guess);
            updateAttempts(model.getRemainingAttempts());
            updateHistory(guess, results);
            updateKeyColors(results);
            if (model.isGameOver()) {
                showGameOver(model.isGameWon(), model.getTargetEquation());
            }
        } catch (IllegalArgumentException e) {
            showErrorMessage(e.getMessage());
        }
        updateHistory(inputField.getText(), controller.getLastGuessResults());
    }
}

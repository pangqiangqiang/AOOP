import java.util.*;
import java.io.*;

public class NumberleModel extends Observable implements INumberleModel {
    private List<String> equations;
    private String targetEquation;
    private int attemptsUsed = 0;
    private static final int MAX_ATTEMPTS = 6;
    private boolean errorFlag, displayTargetFlag, randomSelectionFlag;
    private Map<Character, String> lastGuessResults = new HashMap<>();
    // Class invariant that ensures some internal states remain consistent.
    private void checkInvariants() {
        assert attemptsUsed >= 0 && attemptsUsed <= MAX_ATTEMPTS : "Attempts used are out of valid range";
        assert equations != null : "Equations list cannot be null";
    }

    public NumberleModel() {
        loadEquations();
        setFlags(true, true, true);
    }

    @Override
    public void loadEquations() {
        equations = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("equations.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.matches("^[0-9+\\-*/=]{7}$")) {
                    equations.add(line.trim());
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to load equations: " + e.getMessage());
            System.exit(1);
        }
        assert !equations.isEmpty() : "Equations list should not be empty after loading";
        notifyObservers();
        startNewGame();
    }

    public void startNewGame() {
        assert !equations.isEmpty() : "Cannot start a new game without equations";
        // Debugging code
        System.out.println("Starting new game. Total equations available: " + equations.size());
        if (!equations.isEmpty()) {
            Random random = new Random();
            targetEquation = equations.get(random.nextInt(equations.size()));
            attemptsUsed = 0;
            lastGuessResults.clear();
            checkInvariants();
            System.out.println("New target equation set: " + targetEquation);
        } else {
            System.out.println("No equations available to play.");
        }
    }



    @Override
    public Map<Character, String> checkGuess(String guess) {
        assert guess != null : "Guess string cannot be null";
        assert guess.matches("^[0-9+\\-*/=]{7}$") || errorFlag : "Guess must match the expected format or errorFlag must handle it";

        lastGuessResults.clear();
        if (!guess.matches("^[0-9+\\-*/=]{7}$")) {
            if (errorFlag) {
                throw new IllegalArgumentException("Invalid guess format.");
            }
        }

        attemptsUsed++;
        for (int i = 0; i < guess.length(); i++) {
            char ch = guess.charAt(i);
            if (ch == targetEquation.charAt(i)) {
                lastGuessResults.put(ch, "correct");
            } else if (targetEquation.indexOf(ch) >= 0) {
                lastGuessResults.put(ch, "misplaced");
            } else {
                lastGuessResults.put(ch, "incorrect");
            }
        }
        assert lastGuessResults != null : "Results map should be initialized";
        checkInvariants();  // Check class invariants after state change
        Map<Character, String> results = new HashMap<>();
        return results;
    }

    @Override
    public boolean isGameOver() {

        checkInvariants();
        return attemptsUsed >= MAX_ATTEMPTS || isGameWon();
    }

    @Override
    public boolean isGameWon() {
        // Check if there's at least one guess made and all are correct.
        return lastGuessResults.size() > 0 && !lastGuessResults.containsValue("incorrect") && !lastGuessResults.containsValue("misplaced");
    }



    @Override
    public String getTargetEquation() {
        return targetEquation;
    }

    @Override
    public int getRemainingAttempts() {
        return MAX_ATTEMPTS - attemptsUsed;
    }

    @Override
    public Map<Character, String> getLastGuessResults() {
        return lastGuessResults;
    }

    @Override
    public void setFlags(boolean errorFlag, boolean displayTargetFlag, boolean randomSelectionFlag) {
        this.errorFlag = errorFlag;
        this.displayTargetFlag = displayTargetFlag;
        this.randomSelectionFlag = randomSelectionFlag;
        checkInvariants();
    }

    public void setTargetEquation(String equation) {
        assert equation != null && equation.matches("^[0-9+\\-*/=]{7}$") : "Target equation must be non-null and match the expected format";
        this.targetEquation = equation;
        // Optionally reset game state related to the target equation here
    }

    @Override
    public void processGuess() {
        setChanged();
        notifyObservers(null);
    }
}

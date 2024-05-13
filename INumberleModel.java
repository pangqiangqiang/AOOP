import java.util.Map;

public interface INumberleModel {
    Map<Character, String> checkGuess(String guess);
    boolean isGameOver();
    boolean isGameWon();
    String getTargetEquation();
    int getRemainingAttempts();
    void startNewGame();
    void loadEquations();
    void setFlags(boolean errorFlag, boolean displayTargetFlag, boolean randomSelectionFlag);
    Map<Character, String> getLastGuessResults();
    void processGuess();
}

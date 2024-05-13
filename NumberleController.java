import java.util.HashMap;
import java.util.Map;
public class NumberleController {
    private INumberleModel model;
    private NumberleView view;

    public NumberleController(INumberleModel model) {
        this.model = model;
    }

    public void setView(NumberleView view) {
        this.view = view;
    }

    // Initiates a new game
    public void startNewGame() {
        model.startNewGame();
        view.resetGame();
        view.updateAttempts(model.getRemainingAttempts());
        updateView();  // Updates view with initial game state
    }

    // Processes the user's guess
    public void processGuess(String guess) {
        System.out.println("Processing guess: " + guess);
        model.processGuess();
        //try {
        //    Map<Character, String> results = model.checkGuess(guess);
        //    view.updateAttempts(model.getRemainingAttempts());
        //    view.updateHistory(guess, results);
        //    view.updateKeyColors(results);
        //    if (model.isGameOver()) {
        //        view.showGameOver(model.isGameWon(), model.getTargetEquation());
        //    }
        //} catch (IllegalArgumentException e) {
        //    view.showErrorMessage(e.getMessage());
        //}
    }
    public Map<Character, String> getLastGuessResults() {
        return model.getLastGuessResults();  // 确保model类有一个这样的方法
    }



    // Updates the view with the latest data from the model
    private void updateView() {
        view.updateKeyColors(model.getLastGuessResults());
    }
}

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class NumberleModelTest {
    private NumberleModel model;

    @Before
    public void setUp() {
        model = new NumberleModel();  // Assumes NumberleModel initializes properly
        model.loadEquations();        // Loads a list of equations from a file or preset list
        model.setFlags(false, false, false);  // Setting flags for testing, with no special behaviors triggered
    }



    /**
     * Test that the game correctly recognizes an incorrect guess and decrements the attempt counter.
     */
    @Test
    public void testValidIncorrectGuess() {
        model.setTargetEquation("5/1+2=7");
        model.checkGuess("6*1-2=4");  // A valid format, but incorrect guess
        assertFalse("The guess should be incorrect", model.isGameWon());
        assertEquals("Attempts should decrement by 1 for an incorrect guess", 5, model.getRemainingAttempts());
    }

    /**
     * Test that the game correctly handles game over after the maximum number of attempts.
     */
    @Test
    public void testGameOverAfterMaxAttempts() {
        model.setTargetEquation("5/1+2=7");
        for (int i = 0; i < 6; i++) {
            model.checkGuess("6*1-2=4");  // Incorrect guesses, each decrementing the attempt counter
        }
        assertTrue("Game should be over after the maximum number of attempts", model.isGameOver());
        assertEquals("No attempts left", 0, model.getRemainingAttempts());
    }

    /**
     * Test resetting the game restores all initial conditions like maximum attempts.
     */
    @Test
    public void testGameReset() {
        model.setTargetEquation("5/1+2=7");
        model.checkGuess("5/1+2=7");  // Correct guess to win the game
        assertTrue("Game should be won", model.isGameWon());
        model.startNewGame();  // Reset the game
        assertFalse("Game should not be over immediately after reset", model.isGameOver());
        assertEquals("Should restore full attempts after game reset", 6, model.getRemainingAttempts());
    }
}

import java.util.Scanner;
import java.util.Map;

public class CLIApp {
    public static void main(String[] args) {
        INumberleModel model = new NumberleModel();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to CLI Numberle!");
        while (!model.isGameOver() && model.getRemainingAttempts() > 0) {
            System.out.println("You have " + model.getRemainingAttempts() + " attempts left.");
            System.out.print("Enter your guess (7 characters, numbers and signs [+ - * / =]): ");
            String guess = scanner.nextLine().trim();

            try {
                model.checkGuess(guess);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                continue;
            }

            System.out.println("Feedback for your guess:");
            for (Map.Entry<Character, String> entry : model.getLastGuessResults().entrySet()) {
                System.out.println("Character " + entry.getKey() + ": " + getFeedback(entry.getValue()));
            }

            if (model.isGameWon()) {
                System.out.println("Congratulations! You've guessed correctly! The equation was: " + model.getTargetEquation());
                break;
            } else if (model.isGameOver()) {
                System.out.println("Game over! The correct equation was: " + model.getTargetEquation());
            }
        }

        scanner.close();
        System.out.println("Thank you for playing Numberle.");
    }

    private static String getFeedback(String status) {
        switch (status) {
            case "correct":
                return "is in the correct position (Green)";
            case "misplaced":
                return "is misplaced (Orange)";
            case "incorrect":
                return "does not exist in the equation (Gray)";
            default:
                return "unknown status";
        }
    }
}

public class GUIApp {
    public static void main(String[] args) {
        INumberleModel model = new NumberleModel();
        NumberleController controller = new NumberleController(model);
        new NumberleView(model, controller);
    }
}

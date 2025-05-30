
import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SelectionScreen screen = new SelectionScreen();
            screen.setSelectionListener((gridSize, xInARow, bestOf) -> {
                new Game(gridSize, xInARow, bestOf);
            });
        });
    }
}

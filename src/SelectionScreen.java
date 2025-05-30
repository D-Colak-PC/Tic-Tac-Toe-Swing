
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SelectionScreen {
    private static final int MIN_GRID_SIZE = 3;
    private static final int MIN_X_IN_A_ROW = 3;
    private static final int MAX_GRID_SIZE = 15;
    private static final int MAX_X_IN_A_ROW = 15;
    private static final int MIN_BEST_OF = 1;
    private static final int MAX_BEST_OF = 10;
    private static final Color BUTTON_COLOR = new Color(100, 200, 100);

    // Callback interface for selection results
    public interface SelectionListener {
        void onSelectionMade(int gridSize, int xInARow, int bestOf);
    }

    private SelectionListener listener;
    private JFrame frame;

    public void setSelectionListener(SelectionListener listener) {
        this.listener = listener;
    }

    public SelectionScreen() {
        frame = new JFrame("Selection Screen");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 150);
        frame.setLayout(new BorderLayout());

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(3, 2, 10, 10));

        // First row with spinner
        JLabel label1 = new JLabel("Grid Size:");
        SpinnerModel model1 = new SpinnerNumberModel(MIN_GRID_SIZE, MIN_GRID_SIZE, MAX_GRID_SIZE, 1);
        JSpinner spinner1 = new JSpinner(model1);
        leftPanel.add(label1);
        leftPanel.add(spinner1);

        // Second row with spinner
        JLabel label2 = new JLabel("X-in-a-Row:");
        SpinnerModel model2 = new SpinnerNumberModel(MIN_X_IN_A_ROW, MIN_X_IN_A_ROW, MAX_X_IN_A_ROW, 1);
        JSpinner spinner2 = new JSpinner(model2);
        leftPanel.add(label2);
        leftPanel.add(spinner2);

        // 3rd row with spinner
        JLabel label3 = new JLabel("Best of:");
        SpinnerModel model3 = new SpinnerNumberModel(MIN_BEST_OF, MIN_BEST_OF, MAX_BEST_OF, 2);
        JSpinner spinner3 = new JSpinner(model3);
        leftPanel.add(label3);
        leftPanel.add(spinner3);

        JButton submitButton = new JButton("Submit");
        submitButton.setBackground(BUTTON_COLOR);
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int gridSize = (Integer) spinner1.getValue();
                int xInARow = (Integer) spinner2.getValue();
                int bestOf = (Integer) spinner3.getValue();

                if (listener != null) {
                    listener.onSelectionMade(gridSize, xInARow, bestOf);
                }

                frame.dispose();
            }
        });

        // Create a panel for the button to center it vertically
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10)); // Add horizontal padding
        buttonPanel.add(submitButton);

        frame.add(leftPanel, BorderLayout.WEST);
        frame.add(buttonPanel, BorderLayout.EAST);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // Example usage:
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SelectionScreen screen = new SelectionScreen();
            screen.setSelectionListener((gridSize, xInARow, bestOf) -> {
                System.out.println("Selected: " + gridSize + " and " + xInARow + " and " + bestOf);
            });
        });
    }
}

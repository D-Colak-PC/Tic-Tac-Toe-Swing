// overengineered tic tac toe game

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.BorderFactory;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Arrays;

public class Game {
    private static final int WINDOW_SIZE = 1000;
    private static final String X_SYMBOL = "✖";
    private static final String O_SYMBOL = "⭕";
    private static final Color X_COLOR = Color.RED;
    private static final Color O_COLOR = Color.BLUE;
    private static final String EMPTY_CELL = "";
    private static final String FONT_NAME = "Arial Unicode MS";
    private static final float FONT_SIZE_RATIO = 0.6f;
    private static final int BORDER_THICKNESS = 5;

    private final int gridSize;
    private final int xInARow;
    private final int bestOf;

    private boolean xTurn;
    private String[][] internalBoard;
    private int xScore;
    private int oScore;

    private JButton[][] externalBoard;
    private JFrame frame;

    public Game(int gridSize, int xInARow, int bestOf) {
        this.gridSize = gridSize;
        this.xInARow = xInARow;
        this.bestOf = bestOf;
        this.xTurn = true; // X starts first
        this.xScore = 0;
        this.oScore = 0;

        initializeGame();
        frame.setVisible(true);
    }

    private void initializeGame() {
        setupFrame();
        createInternalBoard();
        createExternalBoard();
    }

    private void setupFrame() {
        frame = new JFrame("Tic Tac Toe");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WINDOW_SIZE, WINDOW_SIZE);
        frame.setResizable(false);
        frame.setLayout(new GridLayout(gridSize, gridSize));
        System.setProperty("file.encoding", "UTF-8");

        updateBorder();
        updateScoreDisplay();
    }

    private void createInternalBoard() {
        internalBoard = new String[gridSize][gridSize];
        externalBoard = new JButton[gridSize][gridSize];

        for (String[] row : internalBoard) {
            Arrays.fill(row, EMPTY_CELL); // cool trick
        }
    }

    private void createExternalBoard() {
        int buttonSize = WINDOW_SIZE / gridSize;
        int fontSize = (int) (buttonSize * FONT_SIZE_RATIO);
        Font symbolFont = new Font(FONT_NAME, Font.BOLD, fontSize); // font is proportional to button size

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                final int row = i;
                final int col = j;

                externalBoard[i][j] = new JButton(EMPTY_CELL);
                externalBoard[i][j].setFont(symbolFont);
                externalBoard[i][j].addActionListener(e -> handleButtonClick(row, col));
                frame.add(externalBoard[i][j]);
            }
        }
    }

    private void handleButtonClick(int row, int col) {
        if (EMPTY_CELL.equals(internalBoard[row][col])) {
            String currentSymbol = xTurn ? X_SYMBOL : O_SYMBOL;
            makeMove(row, col, currentSymbol);

            if (checkWin(row, col, currentSymbol)) {
                handleWin();
            } else if (isBoardFull()) {
                handleTie();
            } else {
                switchTurn();
            }
        }
    }

    private void makeMove(int row, int col, String symbol) {
        internalBoard[row][col] = symbol;
        externalBoard[row][col].setText(symbol);
        externalBoard[row][col].setEnabled(false);
        System.out.println(Arrays.deepToString(internalBoard));
    }

    private void handleWin() {
        // Update score
        if (xTurn) {
            xScore++;
        } else {
            oScore++;
        }
        updateScoreDisplay();

        // Check if match is over, no need to check even bestOf
        int winsNeeded = (bestOf / 2) + 1;
        if (xScore >= winsNeeded) {
            JOptionPane.showMessageDialog(frame,
                    X_SYMBOL + " wins the match! (" + xScore + "-" + oScore + ")");
            resetMatch();
        } else if (oScore >= winsNeeded) {
            JOptionPane.showMessageDialog(frame,
                    O_SYMBOL + " wins the match! (" + xScore + "-" + oScore + ")");
            resetMatch();
        } else {
            JOptionPane.showMessageDialog(frame,
                    (xTurn ? X_SYMBOL : O_SYMBOL) + " wins this round!");
            resetGame();
        }
    }

    private void handleTie() {
        JOptionPane.showMessageDialog(frame, "It's a tie!");
        resetGame();
    }

    private void switchTurn() {
        xTurn = !xTurn;
        updateBorder();
    }

    private void updateScoreDisplay() {
        frame.setTitle("Tic Tac Toe - Score: " + X_SYMBOL + " " + xScore + " - " + oScore + " " + O_SYMBOL +
                " (Best of " + bestOf + ")");
    }

    private void resetMatch() {
        xScore = 0;
        oScore = 0;
        updateScoreDisplay();
        resetGame();
    }

    private boolean isBoardFull() {
        for (String[] row : internalBoard) {
            for (String cell : row) {
                if (EMPTY_CELL.equals(cell)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkWin(int row, int col, String symbol) {
        return isRowWinning(row, symbol) ||
                isColumnWinning(col, symbol) ||
                areDiagonalsWinning(row, col, symbol);
    }

    private boolean isRowWinning(int row, String symbol) {
        int count = 0;
        for (String cell : internalBoard[row]) {
            if (symbol.equals(cell)) {
                count++;
            } else {
                count = 0;
            }

            if (count >= xInARow) {
                return true;
            }
        }
        return false;
    }

    private boolean isColumnWinning(int col, String symbol) {
        int count = 0;
        for (int row = 0; row < gridSize; row++) {
            if (symbol.equals(internalBoard[row][col])) {
                count++;
            } else {
                count = 0;
            }

            if (count >= xInARow) {
                return true;
            }
        }
        return false;
    }

    private boolean areDiagonalsWinning(int row, int col, String symbol) {
        return nwse(row, col, symbol) ||
                nesw(row, col, symbol);
    }

    // check northwest-southeast diagonal
    private boolean nwse(int row, int col, String symbol) {
        int count = 0;

        // Start from the northwest corner
        int currRow = row - Math.min(row, col);
        int currCol = col - Math.min(row, col);

        while (currRow < gridSize && currCol < gridSize) {
            if (currRow >= 0 && currCol >= 0 && symbol.equals(internalBoard[currRow][currCol])) {
                count++;
                if (count >= xInARow) {
                    return true;
                }
            } else {
                count = 0;
            }
            currRow++;
            currCol++;
        }

        return false;
    }

    // check northeast-southwest diagonal
    private boolean nesw(int row, int col, String symbol) {
        int count = 0;

        // Start from the northeast corner
        int currRow = row - Math.min(row, gridSize - 1 - col);
        int currCol = col + Math.min(row, gridSize - 1 - col);

        while (currRow < gridSize && currCol >= 0) {
            if (currRow >= 0 && currCol < gridSize && symbol.equals(internalBoard[currRow][currCol])) {
                count++;
                if (count >= xInARow) {
                    return true;
                }
            } else {
                count = 0;
            }
            currRow++;
            currCol--;
        }

        return false;
    }

    private void resetGame() {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                internalBoard[i][j] = EMPTY_CELL;
                externalBoard[i][j].setText(EMPTY_CELL);
                externalBoard[i][j].setEnabled(true);
            }
        }
        xTurn = true; // Reset to X's turn
        updateBorder();
    }

    private void updateBorder() {
        Color borderColor = xTurn ? X_COLOR : O_COLOR;
        frame.getRootPane().setBorder(BorderFactory.createLineBorder(borderColor, BORDER_THICKNESS));
    }

    public static void main(String[] args) {
        int gridSize = 7;
        int xInARow = 3;
        int bestOf = 3;

        new Game(gridSize, xInARow, bestOf);
    }
}

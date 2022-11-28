package mowmowrats;

import java.awt.event.*;
import javax.swing.*;
import static mowmowrats.Board.COLUMNS;
import static mowmowrats.Board.ROWS;
import static mowmowrats.Board.TILE_SIZE;

/**
 * Main application
 *
 * @author Anthony Wittenborn, Matti Lambert,Alexandria Mwaura, Ashley Poteau
 */
public class MowMowRats {
    private static JFrame window;
    private static ActionListener listener;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        window = new JFrame("MowMow and the Rats");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(COLUMNS * TILE_SIZE, TILE_SIZE * (ROWS + 2));
        window.setResizable(false);
        window.setLocationRelativeTo(null);
        
        listener = (e) -> {
            SwingUtilities.invokeLater(() -> runGame());
        };
        
        Title title = new Title(listener);
        window.add(title);
        window.setVisible(true);
    }
    
    /**
     * clears the title screen from the screen then adds the game board
     */
    public static void runGame() {
        window.getContentPane().removeAll();
        Board board = new Board(listener);
        window.add(board);
        window.addKeyListener(board);
        window.pack();
        window.repaint();
    }
}

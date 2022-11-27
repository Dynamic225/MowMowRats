package mowmowrats;

import javax.swing.*;
import java.awt.event.*;
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
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        window = new JFrame("MowMow and the Rats");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(COLUMNS * TILE_SIZE, TILE_SIZE * (ROWS + 2));
        window.setResizable(false);
        window.setLocationRelativeTo(null);
        
        ActionListener listener = (e) -> {
            runGame();
        };
        
        Title title = new Title(listener);
        window.add(title);
        window.setVisible(true);
    }
    
    public static void runGame() {
        window.getContentPane().removeAll();
        Board board = new Board();
        window.add(board);
        window.addKeyListener(board);
        window.pack();
        window.repaint();
    }
}

package mowmowrats;

import javax.swing.*;
import static mowmowrats.Board.COLUMNS;
import static mowmowrats.Board.ROWS;
import static mowmowrats.Board.TILE_SIZE;

/**
 * Main application
 *
 * @author Anthony Wittenborn, Matti Lambert,Alexandria Mwaura, Ashley Poteau
 */
public class MowmowRats {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JFrame window = new JFrame("MowMow and the Rats");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(COLUMNS * TILE_SIZE, TILE_SIZE * (ROWS + 2));
        window.setResizable(false);
        window.setLocationRelativeTo(null);
        
        Title title = new Title();
        window.add(title);
        window.setVisible(true);
    }
}

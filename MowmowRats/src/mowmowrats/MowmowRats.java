package mowmowrats;

import javax.swing.*;

/**
 * Main application
 * @author Anthony Wittenborn, Alexandria Mwaura, Matti Lambert, Ashley Poteau
 *
 * @author Anthony Wittenborn, Matti Lambert,Alexandria Mwaura, Ashley Poteau
 */
public class MowmowRats {
    /**
     * Window initialization code
     */
    private static void initWindow() {
        //create window with title "Mowmow and the Rats"
        JFrame window = new JFrame("MowMow and the Rats");
        //make the program stop when the window closes
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //create the jpanel and initialize game loop
        Board board = new Board();
        //add the board to the window
        window.add(board);
        //send key inputs to the board
        window.addKeyListener(board);
        
        //prevent the window from being resizable
        window.setResizable(false);
        //fit the window size to the board size
        window.pack();
        //open window in center of screen
        window.setLocationRelativeTo(null);
        //display the window
        window.setVisible(true);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //invokeLater() used to prevent our graphics from blocking the GUI
        //using a lambda expression to replace "new Runnable() {run() {//code}}"
        SwingUtilities.invokeLater(() -> {
            initWindow();
        });
    }
    
}

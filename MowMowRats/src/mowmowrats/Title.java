package mowmowrats;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import static mowmowrats.Board.COLUMNS;
import static mowmowrats.Board.ROWS;
import static mowmowrats.Board.TILE_SIZE;

/**
 * Title screen graphics
 * @author Anthony, Ashley
 */
public class Title extends JPanel {
    public static final int T_WIDTH = TILE_SIZE * COLUMNS;
    public static final int T_HEIGHT = TILE_SIZE * (ROWS + 2);
    public static final int C_WIDTH = TILE_SIZE * 12;
    public static final int C_HEIGHT = TILE_SIZE * 8;
    public static final int B_WIDTH = 140;
    public static final int B_HEIGHT = 65;
    public static final Color B_COLOR = new Color(0xddb439);
    public static final Color T_COLOR = new Color(0x2e0c47);
    private BufferedImage bgImage;
    
    /**
     * constructor for Title
     * @param runGame ActionListener for starting the game
     */
    public Title(ActionListener runGame) {
        setPreferredSize(new Dimension(T_WIDTH, T_HEIGHT));
        setLayout(null);
        
        JPanel container = new JPanel();
        container.setLayout(null);
        container.setBounds((T_WIDTH - C_WIDTH)/2, (T_HEIGHT - C_HEIGHT)/2, C_WIDTH, C_HEIGHT);
        container.setBackground(new Color(0, 0, 0, 0));
        add(container);
        
        JLabel titleText = new JLabel("MowMow and the Rats");
        titleText.setHorizontalAlignment(JLabel.CENTER);
        titleText.setFont(new Font("Lato", Font.BOLD, 40));
        titleText.setForeground(new Color(0xc45709));
        titleText.setBounds(0, 0, C_WIDTH, 40);
        container.add(titleText);
        
        JButton startButton = new JButton("Start");
        startButton.setBounds((C_WIDTH - B_WIDTH)/2, TILE_SIZE*2, B_WIDTH, B_HEIGHT);
        startButton.addActionListener(runGame);
        setCommonButtonSettings(startButton);
        container.add(startButton);
        
        JButton quitButton = new JButton("Quit");
        quitButton.setBounds((C_WIDTH - B_WIDTH)/2, TILE_SIZE*4 + TILE_SIZE/2, B_WIDTH, B_HEIGHT);
        quitButton.addActionListener((e) -> System.exit(0));
        setCommonButtonSettings(quitButton);
        container.add(quitButton);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        //draw the background image
        loadImage();
        g.drawImage(bgImage, 0, 0, TILE_SIZE * COLUMNS, TILE_SIZE * (ROWS + 2), null);
        
        //put a layer of low opacity white over the image to lighten it up a bit
        g.setColor(new Color(255, 255, 255, 10));
        g.fillRect(0, 0, TILE_SIZE * COLUMNS, TILE_SIZE * (ROWS + 2));
    }
    
    /**
     * Load the background image for the class from the file
     */
    private void loadImage() {
        try {
           final String imageName = "daybgfr.png";
            bgImage = ImageIO.read(new File("res/" + imageName));
        } catch (IOException exc) {
            System.out.println("Error opening image file: " + exc.getMessage());
        }
    }
    
    /**
     * Set the button parameters that are the same
     * @param button the button to have set
     */
    public static void setCommonButtonSettings(JButton button) {
        button.setFocusable(false);
        button.setFont(new Font("Lato", Font.BOLD, 25));
        button.setBackground(B_COLOR);
        button.setForeground(T_COLOR);
        button.setBorder(BorderFactory.createLineBorder(new Color(0xaf8d3b), 2, true));
    }
}

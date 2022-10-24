package mowmowrats;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList; 
import java.util.Random;
import javax.swing.*;

/**
 * JPanel which stores board information of the game
 * @author Anthony Wittenborn
 */
public class Board extends JPanel implements ActionListener, KeyListener {
    //set the constants
    private final int DELAY = 25;
    public static final int TILE_SIZE = 50;
    public static final int ROWS = 12;
    public static final int COLUMNS = 18;
    
    
    
    private Timer timer;
    private Player player;
    private ArrayList<Rat> rats;
    //private ArrayList walls; //TODO add this later
    
    public Board() {
        //set board size
        setPreferredSize(new Dimension(TILE_SIZE * COLUMNS, TILE_SIZE * ROWS));
        //set background color
        setBackground(new Color(232, 218, 160));
        
        //initialize the game state
        player = new Player();
        rats = populateRats();
        //walls = generateWalls(); //TODO add walls later
        
        //timer will call the actionPerformed() method every DELAY ms
        timer = new Timer(DELAY, this);
        timer.start();
    }
    
    
    @Override
    /**
     * code that is called every game tick
     */
    public void actionPerformed(ActionEvent e) {
        player.tick();
        
        collectRats();
        
        repaint();
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        drawBackground(g);
        
        for (Rat rat : rats) {
            rat.draw(g, this);
        }
        player.draw(g, this);
        
        drawScore(g);
        
        Toolkit.getDefaultToolkit().sync();
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        //not used
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        //send pressed key events to player
        player.keyPressed(e);
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        //not used
    }
    
    /**
     * draw checkered background
     * @param g the graphics component
     */
    private void drawBackground(Graphics g) {
        g.setColor(new Color(203, 182, 94));
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                //color every other tile
                if ((row + col) % 2 == 1) {
                    //draw a square tile at current row/column pos
                    g.fillRect(
                        col * TILE_SIZE,
                        row * TILE_SIZE,
                        TILE_SIZE,
                        TILE_SIZE
                    );
                }
            }
        }
    }
    
    /**
     * Draw the rats collected score
     * @param g the graphics component
     */
    private void drawScore(Graphics g) {
        String text = "Rats collected: " + player.getScore();
        //cast the graphics to 2d to make it look nice
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        
        //set text color and font
        g2d.setColor(new Color(82, 31, 94));
        g2d.setFont(new Font("Lato", Font.BOLD, 25));
        
        FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
        Rectangle rect = new Rectangle(0, TILE_SIZE * (ROWS - 1), TILE_SIZE * COLUMNS, TILE_SIZE);
        
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        
        //draw the string
        g2d.drawString(text, x, y);
    }
    
    /**
     * Put the rats on the board in random locations
     * @return ratList the list of rats
     */
    private ArrayList populateRats() {
        ArrayList ratList = new ArrayList<Rat>();
        Random rand = new Random();
        Random ratAmt = new Random();
        int upperBound = 20;
        int randomRat = ratAmt.nextInt(upperBound);
        
        for (int i=0; i < randomRat; i++) {
            int ratX = rand.nextInt(COLUMNS);
            int ratY = rand.nextInt(ROWS);
            ratList.add(new Rat(ratX, ratY));
        }
        
        return ratList;
    }
    
    /**
     * Removes the rats from the board and adds to the score
     */
    private void collectRats() {
        ArrayList collectedRats = new ArrayList<Rat>();
        for (Rat rat : rats) {
            //if player is on same tile as rat, collect the rat
            if (player.getPos().equals(rat.getPos())) {
                player.addScore(1);
                collectedRats.add(rat);
            }
        }
        
        rats.removeAll(collectedRats);
    }
    
    //TODO generate the walls
    /*
    private ArrayList generateWalls() {
        ArrayList walls = new ArrayList<>();
    }
    */
}

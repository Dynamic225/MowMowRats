package mowmowrats;

import java.awt.event.KeyEvent;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.ArrayList;

/**
 * Stores information about the player
 * 
 * @author Matti Lambert
 */
public class Player {
    
    //image to represent position of the player
    private BufferedImage image;
    
    //current position of the player on the board
    private Point pos;
    
    //last position of the player on the board before most recent translate
    private Point lastPos;
    
    //has a tick passed yet
    private boolean hasTicked;
    
    //keep track of the score
    private int score;
    
    /**
     * Constructor for the player
     */
    public Player() {
        loadImage();
        
        //initialize the position and score
        pos = new Point(0, 0);
        lastPos = pos;
        hasTicked = true;
        score = 0;
    }
    
    /**
     * loads the Mowmow image file
     */
    private void loadImage() {
        try {
            final String imageName = "TempMowMow.png";
            image = ImageIO.read(new File("res/" + imageName));
        } catch(IOException exc) {
            System.out.println("Error opening image file: " + exc.getMessage());
        }
    }
    
    /**
     * creates the graphic of the player
     * 
     * @param g the graphics element
     * @param observer the image observer
     */
    public void draw(Graphics g, ImageObserver observer) {
        g.drawImage(
            image, 
            pos.x * Board.TILE_SIZE, 
            (pos.y + 1) * Board.TILE_SIZE, 
            Board.TILE_SIZE, 
            Board.TILE_SIZE, 
            observer
        );
    }
    
    /**
     * defines the movement of the character to the keys pressed
     * @param e key event
     */
    public void keyPressed(KeyEvent e) {
        if (hasTicked) {
            lastPos = (Point)pos.clone();
            hasTicked = false;
        }
        int key = e.getKeyCode();
        
        //move the player depending on key pressed
        
            if (key == KeyEvent.VK_UP) {
                pos.translate(0, -1); //move the character up one space
            } 
            if (key == KeyEvent.VK_RIGHT) {
                pos.translate(1, 0); //move the character to the right one space
            } 
            if (key == KeyEvent.VK_DOWN) {
                pos.translate(0, 1); //move the character down one space
            } 
            if (key == KeyEvent.VK_LEFT) {
                pos.translate(-1, 0); //move the character to the left one space
            } 
        
    }
    
    /**
     * updates the state of the player
     * @param walls array list of Point positions of walls on the grid
     */
    public void tick(ArrayList<Point> walls) {
        //prevents the player from moving off the horizontal side of the board
        if(pos.x < 0 || pos.x >= Board.COLUMNS) {
            pos.x = (pos.x < 0) ? 0 : Board.COLUMNS - 1;
        } 
        
        //prevents the player from moving off the vertical side of the board
        if(pos.y < 0 || pos.y >= Board.ROWS) {
            pos.y = (pos.y < 0) ? 0 : Board.ROWS - 1;
        } 
        
        if (walls.contains(pos)) {
            pos = lastPos;
        }
        
        hasTicked = true;
    }
    
    /**
     * print the score to the screen
     * 
     * @return gives a string value of the player's score
     */
    public String getScore() {
        return String.valueOf(score);
    }
    
    /**
     * adds the score
     * 
     * @param amount gives the total score of the player as they traverse 
     *               the maze
     */
    public void addScore(int amount) {
        score += amount;
    }
    
    /**
     * shows the position of the player
     * 
     * @return gives the position of the player's icon on the screen
     */
    public Point getPos() {
        return pos;
    }
}

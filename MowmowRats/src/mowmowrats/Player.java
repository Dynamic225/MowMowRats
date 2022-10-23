package mowmowrats;

import java.awt.event.KeyEvent;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Stores information about the player
 * 
 * @author Anthony Wittenborn, Matti Lambert
 */
public class Player {
    
    //image of position of the player
    private BufferedImage image;
    
    //current position of the player on the board
    private Point pos;
    
    //keep track of the score
    private int score;
    
    public Player() {
        loadImage();
        
        //initialize the position and score
        pos = new Point(0, 0);
        score = 0;
    }
    
    //loads the image file so the player icon is not a blank icon
    private void loadImage() {
        try {
            image = ImageIO.read(new File("TempMowMow.png"));
        } catch(IOException exc) {
            System.out.println("Error opening image file: " + exc.getMessage());
        }
    }
    
    /**
     * creates the graphic of the player
     * 
     * @param g represents the player icon
     * @param observe allows the image to be seen on the screen
     */
    public void draw(Graphics g, ImageObserver observe) {
        g.drawImage(image, pos.x * Board.TILE_SIZE, pos.y * Board.TILE_SIZE, observe);
    }
    
    /**
     * defines the movement of the character to the keys pressed
     * 
     * @param event represents the response to the key being pressed
     */
    public void keyPressed(KeyEvent event) {
        int key = event.getKeyCode();
        
        //move the player depending on key pressed
        if(key == KeyEvent.VK_UP) {
            pos.translate(0, -1); //move the character up one space
        }
        if(key == KeyEvent.VK_RIGHT) {
            pos.translate(1, 0); //move the character to the right one space
        }
        if(key == KeyEvent.VK_DOWN) {
            pos.translate(0, 1); //move the character down one space
        }
        if(key == KeyEvent.VK_LEFT) {
            pos.translate(-1, 0); //move the character to the left one space
        }
    }
    
    //updates the state of the player
    public void tick() {
        //prevents the player from moving off the horizontal side of the board
        if(pos.x < 0) {
            pos.x = 0;
        } else if(pos.x >= Board.COLUMNS) {
            pos.x = Board.COLUMNS - 1;
        }
        
        //prevents the player from moving off the vertical side of the board
        if(pos.y < 0) {
            pos.y = 0;
        } else if(pos.y >= Board.ROWS) {
            pos.y = Board.ROWS - 1;
        }
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

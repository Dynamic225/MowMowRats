package mowmowrats;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


/**
 *
 * @author Alexandria Mwaura, Anthony Wittenborn
 */
public class Rat {
    
    // image that represents the rat's position on the board
    private BufferedImage image;
    // current position of the rat on the board grid
    private Point pos;

    /**
     * constructor for the rat
     * 
     * @param ratPos point at which to generate rat
     */
    public Rat(Point ratPos) {
        // load the assets
        loadImage();

        // initialize the state
        pos = ratPos;
    }
    /**
     * load the image to display 
     */
    private void loadImage() {
        try {
           final String imageName = "rat.png";
            image = ImageIO.read(new File("res/" + imageName));
        } catch (IOException exc) {
            System.out.println("Error opening image file: " + exc.getMessage());
        }
    }

    /**
     * creates the graphic of the rat
     * 
     * @param g
     * @param observer
     */
    public void draw(Graphics g, ImageObserver observer) {
        /**
         * draw the image with the same size as a board tile
         */
        
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
     * gets the rat position
     * @return variable pos which corresponds to position of rat 
     */
    public Point getPos() {
        return pos;
    }
       
}

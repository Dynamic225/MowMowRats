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
 * @author Alexandria Mwaura
 */
public class Rat {
    //pretty much copy this almost exactly from the tutorial in the discord,
    //this class replaces the Coins class in the tutorial.
    //be sure to use javadoc for the code implementation, see the main class and the board class 
    //for how to do this. Pretty much, if a function has a return type other than void,
    //specify the return value with @return
    //if a function accepts a parameter, use @param to document that before each function you define
    
        // image that represents the coin's position on the board
    private BufferedImage image;
    // current position of the coin on the board grid
    private Point pos;

    /**
     *
     * @param x
     * @param y
     */
    // rat constructor
    public Rat(int x, int y) {
        // load the assets
        loadImage();

        // initialize the state
        pos = new Point(x, y);
    }
//laod the image to display 
    private void loadImage() {
        try {
           //rat image
            image = ImageIO.read(new File("/images/rat.jpg"));
        } catch (IOException exc) {
            System.out.println("Error opening image file: " + exc.getMessage());
        }
    }

    /**
     *
     * @param g
     * @param observer
     */
    public void draw(Graphics g, ImageObserver observer) {
        // this is also where we translate board grid position into a canvas pixel
        // position by multiplying by the tile size.
        g.drawImage(
            image, 
            pos.x * Board.TILE_SIZE, 
            pos.y * Board.TILE_SIZE, 
            observer
        );
    }

    /**
     *
     * @return variable pos which corresponds to position of rat 
     */
    public Point getPos() {
        return pos;
    }
       
}

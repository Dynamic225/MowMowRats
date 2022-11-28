package mowmowrats;

import java.awt.*;
import java.awt.event.*;
import java.awt.Point;
import java.util.ArrayList; 
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;
import static mowmowrats.Title.T_WIDTH;
import static mowmowrats.Title.T_COLOR;
import static mowmowrats.Title.B_WIDTH;
import static mowmowrats.Title.B_HEIGHT;
import static mowmowrats.Title.setCommonButtonSettings;

/**
 * JPanel which stores board information of the game
 * @author Anthony Wittenborn
 */
public class Board extends JPanel implements ActionListener, KeyListener {
    //set the constants
    private final int DELAY = 25; //number of miliseconds between each update
    private final int GAME_TIME = 10; //game time in seconds
    private final int NUM_RATS = 10;
    public static final int TILE_SIZE = 50; //size of the game board tiles in pixels
    public static final int ROWS = 12;
    public static final int COLUMNS = 18;
    
    private Timer timer;
    private TimerTask timeTick;
    private TimerTask ratWave;
    private TimerTask stopGame;
    private int gameTime;
    private javax.swing.Timer gameTick; //need to specify swing timer since both swing and util add timer
    private Player player;
    private ArrayList<Rat> rats;
    private ArrayList<Point> walls; 
    private boolean skipNextRatWave;
    private ActionListener listener;
    
    public Board(ActionListener runGame) {
        //initialize all class variables
        listener = runGame; //set the listener for restarting the game equal to the passed in runGame listener
        //timer will call the actionPerformed() method every DELAY ms
        gameTick = new javax.swing.Timer(DELAY, this);
        gameTime = GAME_TIME; //initialize the time tracker to the starting game time
        
        skipNextRatWave = false; 
        
        timer = new Timer();
        
        timeTick = new TimerTask() {
            @Override
            public void run() {
                gameTime--;
            }
        };
        
        ratWave = new TimerTask() {
            @Override
            public void run() {
                if (skipNextRatWave) {
                    skipNextRatWave = false;
                    return;
                }
                if (gameTime > 0) rats = populateRats();
            }
        };
        
        stopGame = new TimerTask () {
            @Override
            public void run() {
                timer.cancel();
            }
        };
        
        //initialize the game state
        walls = generateWalls(); 
        player = new Player();
        rats = populateRats();
        
        //set board size
        setPreferredSize(new Dimension(TILE_SIZE * COLUMNS, TILE_SIZE * (ROWS + 2)));
        //set background color
        setBackground(new Color(232, 218, 160));
        
        gameTick.start(); 
        
        timer.scheduleAtFixedRate(timeTick, 1000, 1000);
        timer.scheduleAtFixedRate(ratWave, 10000, 10000);
        timer.schedule(stopGame, GAME_TIME * 1000);
    }
    
    //code called every game tick
    @Override
    public void actionPerformed(ActionEvent e) {
        player.tick(walls);
        
        collectRats();
        
        if (rats.isEmpty() && gameTime > 0) {
            rats = populateRats();
            skipNextRatWave = true;
        }
        
        repaint();
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        drawBackground(g);
        drawPadding(g);
        
        drawWalls(g);
        
        for (Rat rat : rats) {
            rat.draw(g, this);
        }
        player.draw(g, this);
        
        drawTimer(g);
        drawScore(g);
        
        if (gameTime == 0) { //display over board when time runs out
            g.setColor(new Color(255, 255, 255, 50));
            g.fillRect(0, 0, TILE_SIZE * COLUMNS, TILE_SIZE * (ROWS + 2));
            drawGameOver(g);
        }
        
        Toolkit.getDefaultToolkit().sync();
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        //not used
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        //send pressed key events to player if game is still running
        if (gameTime > 0) player.keyPressed(e);
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
                        (row + 1) * TILE_SIZE,
                        TILE_SIZE,
                        TILE_SIZE
                    );
                }
            }
        }
    }
    
    /**
     * Draw the padding where the text information is displayed
     * @param g the graphics component
     */
    private void drawPadding(Graphics g) {
        g.setColor(new Color(244, 232, 193));
        g.fillRect(0, 0, TILE_SIZE * COLUMNS, TILE_SIZE);
        g.fillRect(0, (ROWS + 1) * TILE_SIZE, TILE_SIZE * COLUMNS, TILE_SIZE);
    }
    
    /**
     * Draw the text that displays the time remaining
     * @param g the graphics component
     */
    private void drawTimer(Graphics g) {
        String text = "Time Remaining: " + gameTime;
        //cast the graphics to 2d to make it look nice
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        
        //set text color and font
        g2d.setColor(T_COLOR);
        g2d.setFont(new Font("Lato", Font.BOLD, 25));
        
        FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
        Rectangle rect = new Rectangle(0, 0, TILE_SIZE * COLUMNS, TILE_SIZE);
        
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        
        //draw the string
        g2d.drawString(text, x, y);
    }
    
    /**
     * Draw the rats collected score
     * @param g the graphics component
     */
    private void drawScore(Graphics g) {
        String text = "Rats Collected: " + player.getScore();
        //cast the graphics to 2d to make it look nice
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        
        //set text color and font
        g2d.setColor(T_COLOR);
        g2d.setFont(new Font("Lato", Font.BOLD, 25));
        
        FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
        Rectangle rect = new Rectangle(0, TILE_SIZE * (ROWS + 1), TILE_SIZE * COLUMNS, TILE_SIZE);
        
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        
        //draw the string
        g2d.drawString(text, x, y);
    }
    
    /**
     * draw the game over text and insert buttons to retry or quit
     * @param g 
     */
    public void drawGameOver(Graphics g) {
        String text = "Game Over";
        //cast the graphics to 2d to make it look nice
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        
        //set text color and font
        g2d.setColor(T_COLOR);
        g2d.setFont(new Font("Lato", Font.BOLD, 50));
        
        FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
        Rectangle rect = new Rectangle(0, 160, TILE_SIZE * COLUMNS, TILE_SIZE);
        
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        
        //draw the string
        g2d.drawString(text, x, y);
        
        JButton retry = new JButton("Retry");
        retry.setBounds((T_WIDTH - B_WIDTH)/2, TILE_SIZE*6, B_WIDTH, B_HEIGHT);
        retry.addActionListener(listener);
        setCommonButtonSettings(retry);
        add(retry);
        JButton quit = new JButton("Quit");
        quit.setBounds((T_WIDTH - B_WIDTH)/2, TILE_SIZE*9, B_WIDTH, B_HEIGHT);
        quit.addActionListener((e) -> System.exit(0));
        setCommonButtonSettings(quit);
        add(quit);
    }
    
    /**
     * Put the rats on the board in random locations
     * @return an ArrayList of Rat objects
     */
    private ArrayList populateRats() {
        ArrayList ratList = new ArrayList<Rat>();
        ArrayList ratPosList = new ArrayList<Point>(); //temporary list to make sure rats do not overlap
        Random rand = new Random();
        
        for (int i=0; i < NUM_RATS; i++) {
            int ratX = -1;
            int ratY = -1;
            Point ratPos = new Point(ratX, ratY);
            while (ratPosList.contains(ratPos) || walls.contains(ratPos) || ratPos.x == -1) {
                ratX = rand.nextInt(COLUMNS);
                ratY = rand.nextInt(ROWS);
                ratPos = new Point(ratX, ratY);
            }
            ratPosList.add(ratPos);
            ratList.add(new Rat(ratPos));
        }
        
        return ratList;
    }
    
    /**
     * Removes the collected rats from the board and adds to the score
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
    
    /**
     * function to pick a random preset of wall schematics to be drawn on the board
     * @return an array list of Point positions of each wall on the board
     */
    private ArrayList<Point> generateWalls() {
        ArrayList<Point> wallList = new ArrayList<>();
        Random choice = new Random();
        Point pos = new Point();
        
        switch (choice.nextInt(4)) {
            case 0 -> {
                pos.x = 2;
                for (int i=1; i<=5; i++) {
                    pos = (Point)pos.clone();
                    pos.y = i;
                    wallList.add(pos);
                }
                for (int i=3; i<=7; i++) {
                    pos = (Point)pos.clone();
                    pos.x = i;
                    wallList.add(pos);
                }
                pos = new Point(14, 3);
                wallList.add(pos);
                for (int i=4; i<=9; i++) {
                    pos = (Point)pos.clone();
                    pos.y = i;
                    wallList.add(pos);
                }
                for (int i=13; i>=8; i--) {
                    pos = (Point)pos.clone();
                    pos.x = i;
                    wallList.add(pos);
                }
                wallList.add(new Point(3, 9));
                wallList.add(new Point(2, 9));
                wallList.add(new Point(4, 9));
                wallList.add(new Point(3, 10));
                wallList.add(new Point(3, 8));
                pos = new Point(6, 2);
                wallList.add(pos);
                for (int i=7; i<=11; i++) {
                    pos = (Point)pos.clone();
                    pos.x = i;
                    wallList.add(pos);
                }
            }
            case 1 -> { 
                pos.x = 2;
                for (int i=2; i<=9; i++) {
                    pos = (Point)pos.clone();
                    pos.y = i;
                    wallList.add(pos);
                }
                for (int i=3; i<=5; i++) {
                    pos = (Point)pos.clone();
                    pos.x = i;
                    wallList.add(pos);
                }
                pos = new Point();
                pos.x = 11;
                for (int i=0; i<=5; i++) {
                    pos = (Point)pos.clone();
                    pos.y = i;
                    wallList.add(pos);
                }
                pos = new Point();
                pos.y = 7;
                for (int i=8; i<=13; i++) {
                    pos = (Point)pos.clone();
                    pos.x = i;
                    wallList.add(pos);
                }
            }
            case 2 -> {
                pos.y = 1;
                for (int i=0; i<=5; i++) {
                    pos = (Point)pos.clone();
                    pos.x = i;
                    wallList.add(pos);
                }
                pos = new Point();
                pos.y = 7;
                for (int i=7; i>=6; i--) {
                    pos = (Point)pos.clone();
                    pos.x = i;
                    wallList.add(pos);
                }
                for (int i=pos.y; i>=3; i--) {
                    pos = (Point)pos.clone();
                    pos.y = i;
                    wallList.add(pos);
                }
                for (int i=pos.x; i<=10; i++) {
                    pos = (Point)pos.clone();
                    pos.x = i;
                    wallList.add(pos);
                }
                for (int i=pos.y; i<=7; i++) {
                    pos = (Point)pos.clone();
                    pos.y = i;
                    wallList.add(pos);
                }
                pos = (Point)pos.clone();
                pos.x--;
                wallList.add(pos);
                pos = new Point();
                pos.x = 14;
                for (int i=0; i<=2; i++) {
                    pos = (Point)pos.clone();
                    pos.y = i;
                    wallList.add(pos);
                }
                for (int i=4; i<=6; i++) {
                    pos = (Point)pos.clone();
                    pos.y = i;
                    wallList.add(pos);
                }
                for (int i=pos.x; i<=17; i++) {
                    pos = (Point)pos.clone();
                    pos.x = i;
                    wallList.add(pos);
                }
            }
            case 3 -> {
                pos.y = 3;
                for (int i=0; i<=7; i++) {
                    pos = (Point)pos.clone();
                    pos.x = i;
                    wallList.add(pos);
                }
                for (int i=pos.y; i<=9; i++) {
                    pos = (Point)pos.clone();
                    pos.y = i;
                    wallList.add(pos);
                }
                pos = new Point();
                pos.x = 13;
                for (int i=3; i<=7; i++) {
                    pos = (Point)pos.clone();
                    pos.y = i;
                    wallList.add(pos);
                }
                for (int i=pos.x; i<=16; i++) {
                    pos = (Point)pos.clone();
                    pos.x = i;
                    wallList.add(pos);
                }
                pos = new Point();
                pos.x = 10;
                for (int i=0; i<ROWS; i++) {
                    if (i == 5 || i == 6) continue;
                    pos = (Point)pos.clone();
                    pos.y = i;
                    wallList.add(pos);
                }
            }
        }
        
        
        
        return wallList;
    }
    
    /**
     * code for drawing the walls on the board
     * @param g the graphics component that is being drawn on
     */
    private void drawWalls(Graphics g) {
        g.setColor(new Color(33, 30, 20));
        for (int i=0; i<walls.size(); i++) {
            Point pos = walls.get(i);
            g.fillRect(
                pos.x * TILE_SIZE,
                (pos.y + 1) * TILE_SIZE,
                TILE_SIZE,
                TILE_SIZE
            );
        }
    }
}

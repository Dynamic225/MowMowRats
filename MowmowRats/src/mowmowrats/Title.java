package mowmowrats;

import javax.swing.*;
/**
 * Title screen graphics
 * @author ashley
 */
public abstract class Title implements RunFromStart {
    private JFrame title;
    
    /**
     * launches the title screen window
     */
    public void launchTitle(){
        title = new JFrame();
        title.setSize(650, 450);
        title.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        title.setTitle("MowMow and the Rats");
        title.setVisible(true);

        // start button, starts game
        JButton start = new JButton("Start");  
        start.setSize(100,30);
        start.setLocation(260,200);  
        start.addActionListener((event) -> startCode(title));
        title.add(start);  
      
        // quit button, quits application
        JButton quit = new JButton("Quit");  
        quit.setSize(100,30);
        quit.setLocation(260,275); 
        quit.addActionListener((event) -> System.exit(0));
        title.add(quit);
    }
}

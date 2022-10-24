/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mowmowrats;
import javax.swing.*;
/**
 *
 * @author ashley
 */
public class Title {
    
    public static void launchTitle(){
      JFrame win = new JFrame();
      win.setSize(650, 450);
      win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      win.setTitle("MowMow and the Rats");
      win.setVisible(true);
      
      // start button, starts game
      JButton start = new JButton("Start");  
      start.setSize(100,30);
      start.setLocation(260,225);  
      win.add(start);  
      
      // quit button, quits application
      JButton quit = new JButton("Quit");  
      quit.setSize(100,30);
      quit.setLocation(260,275); 
      quit.addActionListener((event) -> System.exit(0));
      win.add(quit);
   }
}

/**
 * [Main.java]
 * File that runs the game
 * @author Avneesh Verma and Jeffrey Xu
 * @version 1.0, January 22, 2021
 */

/* Imports */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main {
  // Window Properties
  static JFrame window;
  static final int FRAME_WIDTH = 1280;
  static final int FRAME_HEIGHT = 720;
  
  // Listener and graphics
  static GraphicsPanel canvas;
  static MyKeyListener keyListener = new MyKeyListener();  
  
  // Level end conditions and menu
  static Level[] levels = new Level[4];
  static boolean died;
  static boolean inMenu = true;
  static boolean infoPage = false;
  static boolean[] buttonSelected = {false, false};
  
  // MAIN METHOD
  public static void main(String[] args) {
    // Window and canvas
    window = new JFrame("Astro Mario");
    canvas = new GraphicsPanel();
    window.setSize(FRAME_WIDTH, FRAME_HEIGHT);
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.add(canvas);
    canvas.addKeyListener(keyListener);
    window.setVisible(true);
    
    do {
      window.repaint();
      try {
        Thread.sleep(100);
      }
      catch (Exception e) {}
    } while (inMenu);
    // Exit menu and load game
    window.setVisible(false);
    canvas.removeKeyListener(keyListener);
    window.removeKeyListener(keyListener);
    window.remove(canvas);
    
    do {
      levels[0] = new Level1(window);
      died = levels[0].myMain();
    } while (died);

    do {
      levels[1] = new Level2(window);
      died = levels[1].myMain();    
    } while (died);
  }  // main method end
  
  /* GRAPHICS PANEL */
  static class GraphicsPanel extends JPanel {
    
    public GraphicsPanel() {
      setFocusable(true);
      requestFocusInWindow();
    }
    
    public void paintComponent(Graphics g) {
      super.paintComponent(g);
      
      // Custom colours
      Color darkPurple = new Color(48, 25, 52);      
      Color darkYellow = new Color(255, 211, 0);
      Color lightRed = new Color(222, 23, 56);
      Color lightBlue = new Color(135, 206, 235);
      // Fill background with dark purple
      g.setColor(darkPurple);
      g.fillRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT); 
      
      if (infoPage) {
        // Write extra info
        g.setColor(lightBlue);
        g.setFont(new Font("TimesRoman", Font.ITALIC, 75));
        g.drawString("Creators:", FRAME_WIDTH/2 - 155, FRAME_HEIGHT/2 + 160);
        g.setFont(new Font("Helvetica", Font.BOLD, 30));
        g.drawString("Press Enter to go back to the menu", FRAME_WIDTH/2 - 265, 60);
        // Game controls
        g.setColor(darkYellow);
        g.setFont(new Font("Courier", Font.BOLD, 40));
        g.drawString("Avneesh Verma", FRAME_WIDTH/2 - 465, FRAME_HEIGHT/2 + 250);
        g.drawString("Jeffrey Xu", FRAME_WIDTH/2 + 210, FRAME_HEIGHT/2 + 250);
        g.drawString("Press W to jump", FRAME_WIDTH/2 - 450, FRAME_HEIGHT/2 - 175);
        g.drawString("Press A and D to move left and right", FRAME_WIDTH/2 - 450, FRAME_HEIGHT/2 - 125);
        g.drawString("Press SPACE to shoot", FRAME_WIDTH/2 - 450, FRAME_HEIGHT/2 - 75);
        g.drawString("Press K to reload", FRAME_WIDTH/2 - 450, FRAME_HEIGHT/2 - 25);
        g.drawString("Press 1, 2, and 3 to switch guns", FRAME_WIDTH/2 - 450, FRAME_HEIGHT/2 + 25);
      }
      else {
        // Draw play button
        if (!buttonSelected[0]) {
          g.setColor(darkYellow);
        }
        else {
          g.setColor(lightRed);
        }
        g.fillRect(FRAME_WIDTH/2 - 350, FRAME_HEIGHT/2 + 60, 220, 130);
        
        // Draw info button
        if (!buttonSelected[1]) {
          g.setColor(darkYellow);
        }
        else {
          g.setColor(lightRed);
        }
        g.fillRect(FRAME_WIDTH/2 + 130, FRAME_HEIGHT/2 + 60, 220, 130);
        
        // Draw text
        g.setColor(lightBlue);
        g.setFont(new Font("TimesRoman", Font.ITALIC, 125));
        g.drawString("Astro Mario", FRAME_WIDTH/2 - 305, FRAME_HEIGHT/2 - 100);
        g.setFont(new Font("Courier", Font.BOLD, 50));
        g.drawString("Play!", FRAME_WIDTH/2 - 312, FRAME_HEIGHT/2 + 137);
        g.drawString("Info", FRAME_WIDTH/2 + 180, FRAME_HEIGHT/2 + 137);
      }
    }  // paintComponent method end
  }  // GraphicsPanel class end
    
  /* Key Listener */
  static class MyKeyListener implements KeyListener {
    public void keyPressed(KeyEvent e) {
      int key = e.getKeyCode();
      
      if (key == KeyEvent.VK_ENTER) {
        if (infoPage) {
          infoPage = false;
        }
        else {
          if (buttonSelected[0]) {
            inMenu = false;
          }
          else if (buttonSelected[1]) {
            infoPage = true;
          }
        }
      }
      if (key == KeyEvent.VK_A && !buttonSelected[0]) {
        if (!infoPage) {
          buttonSelected[0] = true;
          buttonSelected[1] = false;
        }
      }
      else if (key == KeyEvent.VK_D && !buttonSelected[1]) {
        if (!infoPage) {
          buttonSelected[0] = false;
          buttonSelected[1] = true;
        }
      }
    }
    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
  }  // MyKeyListener class end
}  // Main class end
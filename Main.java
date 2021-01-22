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
  
  // Level end conditions
  static Level[] levels = new Level[4];
  static boolean died;
  static boolean inMenu = true;

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
      
      // Fill background with dark purple
      Color darkPurple = new Color(48, 25, 52);
      g.setColor(darkPurple);
      g.fillRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT); 
      
      // Draw text
      g.setColor(Color.WHITE);
      g.setFont(new Font("Helvetica", Font.BOLD, 50));
      g.drawString("Insert Game Name", FRAME_WIDTH/2 - 100, FRAME_HEIGHT/2 - 100);
    }  // paintComponent method end
  }  // GraphicsPanel class end
    
  /* Key Listener */
  static class MyKeyListener implements KeyListener {
    public void keyPressed(KeyEvent e) {
      int key = e.getKeyCode();
      
      if (key == KeyEvent.VK_SPACE) {
        inMenu = false;
      }
    }
    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
  }  // MyKeyListener class end
}  // Main class end
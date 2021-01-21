
/* Imports */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class NewMain {
  // Window Properties
  static JFrame window;
  static final int FRAME_WIDTH = 1280;
  static final int FRAME_HEIGHT = 720;
  
  // Level end conditions
  static boolean[] died = new boolean[1];


  // MAIN METHOD
  public static void main(String[] args) {
    // Window
    window = new JFrame("Space Window");
    window.setSize(FRAME_WIDTH, FRAME_HEIGHT);
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.setVisible(true);
    
    Level level1;

    do {
      level1 = new Level1(window);
      died[0] = level1.myMain();
      
    } while (died[0]);
    
  }

}

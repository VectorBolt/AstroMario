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
  static Level[] levels = new Level[4];
  static boolean died;

  // MAIN METHOD
  public static void main(String[] args) {
    // Window
    window = new JFrame("Space Window");
    window.setSize(FRAME_WIDTH, FRAME_HEIGHT);
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    do {
      levels[0] = new Level1(window);
      died = levels[0].myMain();
      
    } while (died);

    do {
      levels[1] = new Level2(window);
      died = levels[1].myMain();
      
    } while (died);
  }
}

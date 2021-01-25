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
// Image imports
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
// Sound Imports
import java.io.File;
import javax.sound.sampled.*;
// Scanner and Printwriter
import java.util.Scanner;
import java.io.PrintWriter;

public class Main {
  // Window Properties
  static JFrame window;
  static final int FRAME_WIDTH = 1280;
  static final int FRAME_HEIGHT = 720;
  
  // Listener and graphics
  static GraphicsPanel canvas;
  static MyKeyListener keyListener = new MyKeyListener();  
  
  // Level end conditions and menu
  static boolean gameOpen = true;
  static Level[] levels = new Level[3];
  static boolean died;
  static boolean inMenu = true;
  static boolean victoryMenu = false;
  static boolean infoPage = false;
  static boolean[] buttonSelected = {false, false};

  // Music and Background Image
  static AudioInputStream audioStream;
  static Clip music;
  static BufferedImage menuBackground;

  // Counting Coins
  public static int coinCount;
  public static int highScore;

  // MAIN METHOD
  public static void main(String[] args) throws Exception {
    // Window and canvas
    window = new JFrame("Astro Mario");
    canvas = new GraphicsPanel();
    window.setSize(FRAME_WIDTH, FRAME_HEIGHT);
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.add(canvas);
    canvas.addKeyListener(keyListener);
    window.setResizable(false);
    window.setVisible(true);

    // Keeping track of high score
    File saveFile = new File("SaveFile.txt");
    saveFile.createNewFile(); // Creates a new file if it doesn't already exist
    PrintWriter output; // Create File if not already existing
    Scanner read;

    // load music and background image 
    try {
      menuBackground = ImageIO.read(new File("images/menubackground.jpg"));
      File audioFile = new File("sounds/music.wav");
      audioStream = AudioSystem.getAudioInputStream(audioFile);
      music = AudioSystem.getClip();
      music.open(audioStream);
    } catch (Exception ex){} 

    // Play Game
    music.start();
    music.loop(Clip.LOOP_CONTINUOUSLY); 
    while (gameOpen) {
      coinCount = 0; // Reset Coin Counter
      do {
        window.repaint();
        try {
          Thread.sleep(100);
        }
        catch (Exception e) {}
      } while (inMenu);
      // Exit menu and load game
      canvas.removeKeyListener(keyListener);
      window.removeKeyListener(keyListener);
      window.remove(canvas);
      
      // Level 1
      do {
        levels[0] = new Level1(window);
        died = levels[0].playLevel(coinCount);
      } while (died);
      
      // Level 2
      do {
        levels[1] = new Level2(window);
        died = levels[1].playLevel(coinCount);    
      } while (died);

      // Level 3
      do {
        levels[2] = new Level3(window);
        died = levels[2].playLevel(coinCount);
      } while (died);

      // Coin Counting
      for (int curLevel = 0; curLevel < levels.length; curLevel++) {
        for (int coin = 0; coin < levels[0].coinsCollected.length; coin++) {
          if (levels[curLevel].coinsCollected[coin]) {
            coinCount++;
          }
        }
      }
      victoryMenu = true; 

      // Write high score
      read = new Scanner(saveFile);
      try {
        highScore = read.nextInt();
      } catch (Exception e) {
        highScore = 0;
      }
      read.close();
      output = new PrintWriter(saveFile);
      if (coinCount > highScore) {
        output.println(coinCount);
        highScore = coinCount;
      }
      output.close();
      
      // Victory menu
      window.add(canvas);
      canvas.addKeyListener(keyListener);
      canvas.requestFocusInWindow();
      do {
        window.repaint();
        try {
          Thread.sleep(100);
        }
        catch (Exception e) {}
      } while (victoryMenu);
    }
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
      g.drawImage(menuBackground, 0, 0, this);
      
      if (infoPage) {
        // Write extra info
        g.setColor(lightBlue);
        g.setFont(new Font("Helvetica", Font.ITALIC, 75));
        g.drawString("Creators:", FRAME_WIDTH/2 - 155, FRAME_HEIGHT/2 + 165);
        g.setFont(new Font("Helvetica", Font.BOLD, 30));
        g.drawString("Press ENTER to go back to the menu", FRAME_WIDTH/2 - 275, 60);
        // Game controls
        g.setColor(darkYellow);
        g.setFont(new Font("Courier", Font.BOLD, 37));
        g.drawString("Avneesh Verma", FRAME_WIDTH/2 - 465, FRAME_HEIGHT/2 + 250);
        g.drawString("Jeffrey Xu", FRAME_WIDTH/2 + 210, FRAME_HEIGHT/2 + 250);
        g.drawString("Press W/up to jump", FRAME_WIDTH/2 - 530, FRAME_HEIGHT/2 - 175);
        g.drawString("Press A/left and D/right to move left and right", FRAME_WIDTH/2 - 530, FRAME_HEIGHT/2 - 125);
        g.drawString("Press SPACE to shoot", FRAME_WIDTH/2 - 530, FRAME_HEIGHT/2 - 75);
        g.drawString("Press K to reload", FRAME_WIDTH/2 - 530, FRAME_HEIGHT/2 - 25);
        g.drawString("Press 1, 2, and 3 to switch guns", FRAME_WIDTH/2 - 530, FRAME_HEIGHT/2 + 25);
      }
      else if(victoryMenu) {
        g.setColor(lightBlue);
        g.fillRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT); 
        g.setColor(Color.BLACK);
        g.setFont(new Font("TimesRoman", Font.ITALIC, 100));
        g.drawString("You beat the game!", FRAME_WIDTH/2 - 395, 205);
        g.setFont(new Font("Courier", Font.BOLD, 40));
        g.drawString("You collected " + coinCount + " out of 9 coins!", FRAME_WIDTH/5 + 15, 365);
        g.drawString("Your high score is " + highScore + " out of 9 coins.", FRAME_WIDTH/6, 425);
        g.setFont(new Font("Helvetica", Font.BOLD, 30));
        g.drawString("Press SPACE to return to the menu", FRAME_WIDTH/2 - 260, 600);
      }
      else {
        // Draw play button
        if (!buttonSelected[0]) {
          g.setColor(lightBlue);
        }
        else {
          g.setColor(lightRed);
        }
        g.fillRect(FRAME_WIDTH/2 - 350, FRAME_HEIGHT/2 + 60, 220, 130);
        
        // Draw info button
        if (!buttonSelected[1]) {
          g.setColor(lightBlue);
        }
        else {
          g.setColor(lightRed);
        }
        g.fillRect(FRAME_WIDTH/2 + 130, FRAME_HEIGHT/2 + 60, 220, 130);
        
        // Draw text
        g.setColor(Color.WHITE);
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
      
      if (inMenu) {
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
        if ((key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT ) && !buttonSelected[0]) {
          if (!infoPage) {
            buttonSelected[0] = true;
            buttonSelected[1] = false;
          }
        }
        else if ((key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT ) && !buttonSelected[1]) {
          if (!infoPage) {
            buttonSelected[0] = false;
            buttonSelected[1] = true;
          }
        }
      }
      else if (victoryMenu) {
        if (key == KeyEvent.VK_SPACE) {
          victoryMenu = false;
          inMenu = true;
          buttonSelected[0] = false;
          buttonSelected[1] = false;
        }
      }
    }

    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
  }  // MyKeyListener class end
}  // Main class end

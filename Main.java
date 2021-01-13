/*
 * [SpacePlatformer.java]
 * This program has a scrolling space background.
 * @author Avneesh Verma
 * @version 1.0 December 23, 2020
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
// Image imports
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Main {
  // Window Properties
  static JFrame window;
  static GraphicsPanel canvas;
  static final int FRAME_WIDTH = 1534;
  static final int FRAME_HEIGHT = 816;

  // Listeners
  static MyKeyListener keyListener = new MyKeyListener();

  // Background Properties
  static BufferedImage background1;
  static int background1X = 0;
  static int background1Y = 0;
  static int background1W = 1534;

  static BufferedImage background2;
  static int background2X = background1W;
  static int background2Y = 0;
  static int background2W = 1534;

  // Gravity Properties
  static final int GROUND_HEIGHT = 100;
  static final int GRAVITY = 2;
  static int jumpCount = 0;

  // Enemy Properties
  static BufferedImage goomba;
  static boolean isAlive = true;
  static int goombaW = 32, goombaH = 32;
  static int goombaX = 3*FRAME_WIDTH/4;
  static int goombaY = FRAME_HEIGHT - GROUND_HEIGHT - goombaH;
  static Rectangle goombaHitbox = new Rectangle(goombaX, goombaY, goombaW, goombaH);

  // Platform properties
  static int[][] platforms = {
    {FRAME_WIDTH/2, FRAME_HEIGHT - GROUND_HEIGHT - 200, 300, 30},
    {3*FRAME_WIDTH/4, FRAME_HEIGHT - GROUND_HEIGHT - 400, 300, 30}
  };

  // Player
  static Player player1 = new Player(FRAME_WIDTH/4, FRAME_HEIGHT-GROUND_HEIGHT-63, 10, -30);
  static BasicGun basicGun = new BasicGun();

  // MAIN METHOD
  public static void main(String[] args) throws Exception {
    window = new JFrame("Space Window");
    window.setSize(FRAME_WIDTH, FRAME_HEIGHT);
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    canvas = new GraphicsPanel();
    canvas.addKeyListener(keyListener);
    window.add(canvas);

    // Images
    background1 = ImageIO.read(new File("space copy.png"));
    background2 = ImageIO.read(new File("space copy flipped.png"));
    goomba = ImageIO.read(new File("images/goomba1.png"));

    // Bullets
    for (int i = 0; i < basicGun.numBullets; i++) {
      basicGun.bulletLocs[i][0] = player1.x + player1.w;
      basicGun.bulletLocs[i][1] = player1.y/2;
      basicGun.bulletVisible[i] = false;
      basicGun.bulletVelocities[i] = 20;
    }


    window.setVisible(true);
    runGameLoop();

  }


  // Game Loop
  public static void runGameLoop() {
    while (true) {
      window.repaint();
      try {
	Thread.sleep(20);
      } catch (Exception e) {}

      // Move Background Image when moving left-to-right
      background1X -= player1.vX;
      if (background1X + background1W <= 0) {
	background1X = background2X - player1.vX + background2W;
      }
      else if (background1X >= FRAME_WIDTH) {
	background1X = background2X - background2W - player1.vX;
      }

      background2X -= player1.vX;
      if (background2X + background2W <= 0) {
	background2X = background1X + background1W;
      }
      else if (background2X >= FRAME_WIDTH) {
	background2X = background1X - background1W;
      }

      // Move Enemy and Platform
      for (int i = 0; i < platforms.length; i++) {
	platforms[i][0] -= player1.vX;
      }
      goombaX -= player1.vX;
      goombaHitbox.setLocation(goombaX, goombaY);

      // Move Bullets
      for (int i = 0; i < basicGun.numBullets; i++) {
	if (basicGun.bulletVisible[i]) {
	  basicGun.bulletLocs[i][0] += basicGun.bulletVelocities[i];
	  basicGun.bulletBoxes[i].setLocation(basicGun.bulletLocs[i][0], basicGun.bulletLocs[i][1]);
	}

	// Remove Bullet when off-screen
	if (basicGun.bulletLocs[i][0] > FRAME_WIDTH || basicGun.bulletLocs[i][0] < 0) {
	  basicGun.bulletVisible[i] = false;
	}

	// If an enemy is hit
	if (basicGun.bulletBoxes[i] != null && isAlive && basicGun.bulletBoxes[i].intersects(goombaHitbox)) {
	  isAlive = false;
	  basicGun.bulletVisible[i] = false;
	}

      }

      // Move Player when Jumping
      player1.vY += GRAVITY;
      player1.y += player1.vY;

      // Land on Ground
      if (player1.y + player1.h >= FRAME_HEIGHT - GROUND_HEIGHT) {
	player1.y = FRAME_HEIGHT - GROUND_HEIGHT - player1.h;
	player1.vY = 0;
	jumpCount = 0;
	player1.isJumping = false;
      }

      // Platform landing
      for (int i = 0; i < platforms.length; i++) {
	if (player1.y + player1.h >= platforms[i][1] && player1.y + player1.h <= platforms[i][1] + platforms[i][3] 
	    && player1.x + player1.w >= platforms[i][0] && player1.x <= platforms[i][0] + platforms[i][2]) {

	  player1.y = platforms[i][1] - player1.h;
	  player1.vY = 0;
	  jumpCount = 0;
	  player1.isJumping = false;
	}
      }

      // Iterate over each frame of walking
      if (player1.isWalking && !player1.isJumping) {
	player1.walkFrame += 0.3;
	if (player1.walkFrame > 3) {
	  player1.walkFrame = 0;
	}
      }

    }
  }



  // Graphics Panel
  static class GraphicsPanel extends JPanel {
    public GraphicsPanel() {
      setFocusable(true);
      requestFocusInWindow();
    }

    public void paintComponent(Graphics g) {
      super.paintComponent(g);

      // Get current player state
      player1.getState();
      // Draw Background and Player
      g.drawImage(background1, background1X, background1Y, this);
      g.drawImage(background2, background2X, background2Y, this);
      g.drawImage(player1.currentImage, player1.x, player1.y, this);
      if (isAlive) {
	g.drawImage(goomba, goombaX, goombaY, this);
      }

      // Draw Ground
      g.setColor(Color.GREEN);
      g.fillRect(0, FRAME_HEIGHT-100, FRAME_WIDTH, 100);

      for (int i = 0; i < platforms.length; i++) {
	g.fillRect(platforms[i][0], platforms[i][1], platforms[i][2], platforms[i][3]);
      }

      // Draw Bullets
      g.setColor(Color.RED);
      for (int i = 0; i < basicGun.numBullets; i++) {
	if (basicGun.bulletVisible[i]) {
	  g.fillOval(basicGun.bulletLocs[i][0], basicGun.bulletLocs[i][1], basicGun.bulletW, basicGun.bulletH);
	}
      }

      g.setColor(Color.WHITE);
      g.setFont(new Font("Helvetica", Font.BOLD, 30));
      g.drawString("Bullets: " + Integer.toString(basicGun.numBullets - basicGun.curBullet),
	  3*FRAME_WIDTH/4, 100);

    }
  }


  // Key Listener
  static class MyKeyListener implements KeyListener {

    public void keyPressed(KeyEvent e) {
      int key = e.getKeyCode();

      // Horizontal Movement
      if (key == KeyEvent.VK_LEFT) {
	player1.vX = -player1.speed;
	player1.facingRight = false;
	player1.isWalking = true;
      }

      else if (key == KeyEvent.VK_RIGHT) {
	player1.vX = player1.speed;
	player1.facingRight = true;
	player1.isWalking = true;
      }

      // Jump
      if (key == KeyEvent.VK_UP && jumpCount < 2) {
	player1.vY = player1.jumpSpeed;
	jumpCount++;
	player1.isJumping = true;
      }

      // Fire
      if (key == KeyEvent.VK_SPACE) {
	basicGun.shoot(player1);
      }

    }

    public void keyReleased(KeyEvent e) {
      int key = e.getKeyCode();

      if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {
	player1.vX = 0;
	player1.isWalking = false;
	player1.walkFrame = 0;
      }

    }

    public void keyTyped(KeyEvent e) {}

  }


}

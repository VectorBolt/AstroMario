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
  static double gravity = 2.0;
  static int jumpCount = 0;

  // Enemy Properties
  static Goomba[] goombas = {
    new Goomba(3*FRAME_WIDTH/4, FRAME_HEIGHT-GROUND_HEIGHT-32, 100),
    new Goomba(FRAME_WIDTH/2 + 150, FRAME_HEIGHT - GROUND_HEIGHT - 232, 100)
  };

  // Platform properties
  static int[][] platforms = {
    {FRAME_WIDTH/2, FRAME_HEIGHT - GROUND_HEIGHT - 200, 300, 30},
    {3*FRAME_WIDTH/4, FRAME_HEIGHT - GROUND_HEIGHT - 400, 300, 30}
  };

  static Rectangle[] water = {
    new Rectangle(200, 100, 700, 600),
    new Rectangle(1700, 100, 800, 800)
  };

  // Player
  static Player player1 = new Player(FRAME_WIDTH/4, FRAME_HEIGHT-GROUND_HEIGHT-63, 10, -30);
  static BasicGun basicGun = new BasicGun();


  // MAIN METHOD
  public static void main(String[] args) throws Exception {
    // Window
    window = new JFrame("Space Window");
    window.setSize(FRAME_WIDTH, FRAME_HEIGHT);
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    canvas = new GraphicsPanel();
    canvas.addKeyListener(keyListener);
    window.add(canvas);

    // Images
    background1 = ImageIO.read(new File("space copy.png"));
    background2 = ImageIO.read(new File("space copy flipped.png"));

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


  // GAME LOOP
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

      // Move Platforms
      for (int i = 0; i < platforms.length; i++) {
	platforms[i][0] -= player1.vX;
      }
      for (int i = 0; i < water.length; i++) {
	water[i].setLocation((int)water[i].getX() - player1.vX, (int)water[i].getY());
      }

      // Move Enemies
      for (int i = 0; i < goombas.length; i++) {
	goombas[i].init_x -= player1.vX;
	goombas[i].x -= player1.vX;

	goombas[i].x += goombas[i].speed;
	if (Math.abs(goombas[i].x - goombas[i].init_x) >= goombas[i].walkRange) {
	  goombas[i].speed *= -1;
	}
	goombas[i].hitbox.setLocation(goombas[i].x, goombas[i].y);
	goombas[i].collision(player1);

	// Walk Frames for images
	goombas[i].walkFrame += 0.1;
	if (goombas[i].walkFrame >= 2) {
	  goombas[i].walkFrame = 0;
	}
      }

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
	for (int j = 0; j < goombas.length; j++) {
	  if (basicGun.bulletBoxes[i] != null && goombas[j].health > 0 && basicGun.bulletVisible[i]
	      && basicGun.bulletBoxes[i].intersects(goombas[j].hitbox)) {
	    goombas[j].health -= 1;
	    basicGun.bulletVisible[i] = false;
	  }
	}

      }

      // When in water
      player1.isSwimming = false; // reset to false before checking
      player1.jumpSpeed = -30;
      player1.speed = 10;
      gravity = 2.0;
      for (int i = 0; i < water.length; i++) {
	if (player1.hitbox.intersects(water[i])) {

	  if (player1.vY > 9) {
	    player1.vY = 9;
	  }
	  else if (player1.vY < -9) {
	    player1.vY = -9;
	  }

	  player1.isSwimming = true;
	  player1.speed = 7;
	  player1.jumpSpeed = -7;
	  gravity = 0.3;
	  jumpCount = 0;
	}
      }

      // Move Player when Jumping
      player1.vY += gravity;
      player1.y += (int)player1.vY;
      player1.hitbox.setLocation(player1.x, player1.y);

      // Land on Ground
      if (player1.y + player1.h >= FRAME_HEIGHT - GROUND_HEIGHT) {
	player1.y = FRAME_HEIGHT - GROUND_HEIGHT - player1.h;
	player1.vY = 0;
	jumpCount = 0;
	player1.isJumping = false;
      }

      platformCollision();


      // Iterate over each frame of walking
      if (player1.isWalking && !player1.isJumping) {
	player1.walkFrame += 0.3;
	if (player1.walkFrame > 3) {
	  player1.walkFrame = 0;
	}
      }

      // Player invulnerability when hit
      if (player1.isInvulnerable && player1.invulnerabilityCounter < 100) {
	player1.invulnerabilityCounter++;
      }
      else {
	player1.isInvulnerable = false;
	player1.invulnerabilityCounter = 0;
      }

    }
  }


  /*
   * platformCollision
   * This method iterates over each platform and checks if the player is touching it.
   * Based on which edge the player is touching, the player's velocity is adjusted.
   */
  public static void platformCollision() {

    // Iterate over each platform
    for (int i = 0; i < platforms.length; i++) {

      // If the player is on top of the platform
      if (player1.y + player1.h >= platforms[i][1] && player1.y + player1.h < platforms[i][1] + platforms[i][3] 
	  && player1.x + player1.w >= platforms[i][0] && player1.x <= platforms[i][0] + platforms[i][2]) {

	player1.y = platforms[i][1] - player1.h;
	player1.vY = 0;
	jumpCount = 0;
	player1.isJumping = false;
      }

      // If the player bumps their head on the platform from below
      else if (player1.y <= platforms[i][1] + platforms[i][3] && player1.y > platforms[i][1]
	  && player1.x + player1.w >= platforms[i][0] && player1.x <= platforms[i][0] + platforms[i][2]) {

	player1.y = platforms[i][1] + platforms[i][3];
	player1.vY = 0;
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

      // Draw Background 
      g.drawImage(background1, background1X, background1Y, this);
      g.drawImage(background2, background2X, background2Y, this);

      // Draw Water
      g.setColor(Color.BLUE);
      for (int i = 0; i < water.length; i++) {
	g.fillRect((int)water[i].getX(), (int)water[i].getY(), 
	    (int)water[i].getWidth(), (int)water[i].getHeight());
      }

      // Draw Player
      g.drawImage(player1.currentImage, player1.x, player1.y, this);

      for (int i = 0; i < goombas.length; i++) {
	if (goombas[i].health > 0) {
	  g.drawImage(goombas[i].images[(int)goombas[i].walkFrame], goombas[i].x, goombas[i].y, this);
	}
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

      // Display Information
      g.setColor(Color.WHITE);
      g.setFont(new Font("Helvetica", Font.BOLD, 30));
      g.drawString("Bullets: " + Integer.toString(basicGun.numBullets - basicGun.curBullet),
	  3*FRAME_WIDTH/4, 100);
      g.drawString("Health: " + Integer.toString(player1.health), 3*FRAME_WIDTH/4, 150); 

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
	player1.vY = (double)player1.jumpSpeed;
	player1.isJumping = true;

	// Only increment jump counter if the player is not in water
	if (!player1.isSwimming) {
	  jumpCount++;
	}
      }

      // Fire
      if (key == KeyEvent.VK_SPACE) {
	basicGun.shoot(player1);
      }

    }

    public void keyReleased(KeyEvent e) {
      int key = e.getKeyCode();

      if ((key == KeyEvent.VK_LEFT && !player1.facingRight) 
	  || (key == KeyEvent.VK_RIGHT && player1.facingRight)) {
	player1.vX = 0;
	player1.isWalking = false;
	player1.walkFrame = 0;
      }

    }

    public void keyTyped(KeyEvent e) {}

  }


}

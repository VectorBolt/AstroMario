/**
 * [Main.java]
 * This program is the main file where the game runs
 * @author Avneesh Verma and Jeffrey Xu
 * @version 1.0 January 18, 2020
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

//---------------------------------------------------------------------------------

public class Main {
  // Window Properties
  static JFrame window;
  static GraphicsPanel canvas;
  static final int FRAME_WIDTH = 1280;
  static final int FRAME_HEIGHT = 720;
  
  // Listeners
  static MyKeyListener keyListener = new MyKeyListener();
  
  // Background Properties
  static BufferedImage background1;
  static int background1X = 0;
  static int background1Y = 0;
  static int background1W = 1280;
  
  static BufferedImage background2;
  static int background2X = background1W;
  static int background2Y = 0;
  static int background2W = 1280;
  
  // Gravity Properties
  static final int GROUND_HEIGHT = 100;
  static double gravity = 2.0;
  static int jumpCount = 0;
  
  // Enemy Properties
  static Goomba[] goombas = {
    new Goomba(3*FRAME_WIDTH/4, FRAME_HEIGHT-GROUND_HEIGHT-32, 100),
    new Goomba(FRAME_WIDTH/2 + 150, FRAME_HEIGHT - GROUND_HEIGHT - 232, 100)
  };
  
  static Crab[] crabs = {
    new Crab(500, FRAME_HEIGHT-GROUND_HEIGHT-32, 100),
    new Crab(800, FRAME_HEIGHT-GROUND_HEIGHT-32, 100)
  };
  
  static Bee[] bees = {
    new Bee(900, 400, 100),
    new Bee(1200, 500, 250)
  };
  
  static Flyer[] flyers = {
    new Flyer(1000, 200, 100),
    new Flyer(1600, 300, 250)
  };
  
  static Enemy[][] enemies = {goombas, crabs, bees, flyers};
  
  // Platform properties
  static int[][] platforms = {
    // Start wall
    {-500, 0, 550, FRAME_HEIGHT},
    // End wall
    {7*FRAME_WIDTH, 0, 1000, FRAME_HEIGHT},
    {FRAME_WIDTH/3, FRAME_HEIGHT - GROUND_HEIGHT - 100, 245, 65},
    {3*FRAME_WIDTH/4, FRAME_HEIGHT - GROUND_HEIGHT - 250, 245, 65},
    {FRAME_WIDTH, FRAME_HEIGHT - GROUND_HEIGHT - 160, 250, 65}
  };
  static BufferedImage platformImage;
  
  // Spike properties
  
  static Rectangle[] water = {
    new Rectangle(200, 100, 320, 192),
    new Rectangle(1700, 100, 320, 192)
  };
  static BufferedImage waterImage;
  
  // Player
  static Player player1 = new Player(FRAME_WIDTH/4, FRAME_HEIGHT-GROUND_HEIGHT-63, 10, -30);
  static int collisionShift = 0;
  
  // Guns
  static Gun basicGun = new BasicGun(player1);
  static Gun energyGun = new EnergyGun(player1);
  static Gun machineGun = new MachineGun(player1);
  static Gun curGun = basicGun;
  static Timer reloadDelay;
  static boolean reloading = false;
  static int[] shotGap = {basicGun.shotDelay,energyGun.shotDelay};
  static int[] beamFade = {0,0,0};
  static int[] chargeLength = {0,0,0};
  static BufferedImage[] energyBeam = new BufferedImage[2];
  
  // Game
  static boolean inPlay = true;
  static boolean gameOpen = true;
  static boolean failedLevel = false;
  
//---------------------------------------------------------------------------------
  
  // MAIN METHOD
  public static void main(String[] args) throws Exception {
    // Window
    window = new JFrame("Space Window");
    window.setSize(FRAME_WIDTH, FRAME_HEIGHT);
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    canvas = new GraphicsPanel();
    window.add(canvas);
    
    // Images
    background1 = ImageIO.read(new File("images/space copy.png"));
    background2 = ImageIO.read(new File("images/space copy flipped.png"));
    platformImage = ImageIO.read(new File("images/platform.png"));
    waterImage = ImageIO.read(new File("images/water.png"));
    for (int i = 0; i < 2; i++) {
      energyBeam[i] = ImageIO.read(new File("images/EnergyBeam" + i + ".png"));
    }
    
    // Keep game running until user closes game
    while (gameOpen) {
      // Game window
      canvas.addKeyListener(keyListener);
      window.setVisible(true);
      
      runGameLoop();
      failedLevel();
    }
  }
  
//---------------------------------------------------------------------------------
  
  // GAME LOOP
  public static void runGameLoop() {
    player1.health = 3;
    while (inPlay) {
      window.repaint();
      try {
        Thread.sleep(20);
      } catch (Exception e) {}
      
      waterPhysics(); // Check if the player is in water, and change physics accordingly
      moveBackground(); // Call method to move background as player moves
      
      // Scroll platforms
      for (int i = 0; i < platforms.length; i++) {
        platforms[i][0] -= player1.vX - collisionShift;
      }
      for (int i = 0; i < water.length; i++) {
        water[i].setLocation((int)water[i].getX() - player1.vX + collisionShift, (int)water[i].getY());
      }
      
      // Move Enemies
      for (int enemyType = 0; enemyType < enemies.length; enemyType++) {
        for (int curEnemy = 0; curEnemy < enemies[enemyType].length; curEnemy++) {
          enemies[enemyType][curEnemy].move(player1, collisionShift);
        }
      }
      
      /* Gun bullets */
      // Basic gun bullets
      moveBullets(basicGun);
      
      // Energy gun bullet
      energygunBullets();
      
      // Machine gun bullets
      moveBullets(machineGun);
      
      // Shooting
      // Basic gun shoot
      if (curGun == basicGun && shotGap[0] >= basicGun.shotDelay && basicGun.isShooting) {
        basicGun.shoot(player1);
        shotGap[0] = 0;
      }
      // Energy gun shoot
      else if (curGun == energyGun && shotGap[1] >= energyGun.shotDelay && energyGun.isShooting) {
        energyGun.shoot(player1);
        shotGap[1] = 0;
      }
      // Machine gun shoot
      else if (curGun == machineGun && machineGun.isShooting) {
        machineGun.shoot(player1); 
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
        player1.isBlockedRight = false;
        player1.isBlockedLeft = false;
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
      
      // Make sure player is walking right direction
      if (player1.facingRight && collisionShift > 0) {
        collisionShift = 0;
        player1.vX = 0;
      }
      else if (!player1.facingRight && collisionShift < 0) {
        collisionShift = 0;
        player1.vX = 0;
      }
      
      // Check if player has died
      if (player1.health <= 0) {
        inPlay = false;
        failedLevel = true;
        player1.deaths += 1;
      }
    }
  }  //runGameLoop method end

//---------------------------------------------------------------------------------
  
  public static void failedLevel() {
    // Display death menu
    window.repaint();
    try {
      Thread.sleep(3000);
    } catch (Exception e) {}
  }
  
//---------------------------------------------------------------------------------
  
  /**
   * moveBackground
   * This method moves the background in the opposite direction of the player
   */
  public static void moveBackground() {
    if (!player1.isBlockedRight && !player1.isBlockedLeft) {
      background1X -= player1.vX - collisionShift;
      if (background1X + background1W <= 0) {
        background1X = background2X - player1.vX + background2W;
      }
      else if (background1X >= FRAME_WIDTH) {
        background1X = background2X - background2W - player1.vX;
      }
      
      background2X -= player1.vX - collisionShift;
      if (background2X + background2W <= 0) {
        background2X = background1X + background1W;
      }
      else if (background2X >= FRAME_WIDTH) {
        background2X = background1X - background1W;
      }
    }
  } // moveBackground method end
  
  /**
   * moveBullets
   * Moves bullets, detects enemy hits, and detects exit screen
   * @param type of gun
   */
  public static void moveBullets(Gun guntype) {
    for (int i = 0; i < guntype.numBullets; i++) {  
      if (guntype.bulletVisible[i]) {
        guntype.bulletLocs[i][0] += guntype.bulletVelocities[i];
        guntype.bulletBoxes[i].setLocation(guntype.bulletLocs[i][0], guntype.bulletLocs[i][1]);
      }
      
      // Remove Bullet when off-screen
      if (guntype.bulletLocs[i][0] > FRAME_WIDTH || guntype.bulletLocs[i][0] < 0) {
        guntype.bulletVisible[i] = false;
      }
      
      // If an enemy is hit
      for (int enemyType = 0; enemyType < enemies.length; enemyType++) {
        for (int curEnemy = 0; curEnemy < enemies[enemyType].length; curEnemy++) {
          if (guntype.bulletBoxes[i] != null && enemies[enemyType][curEnemy].health > 0 
                && guntype.bulletBoxes[i].intersects(enemies[enemyType][curEnemy].hitbox)) {
            if (guntype.bulletVisible[i]) {
              enemies[enemyType][curEnemy].health -= guntype.bulletDamage;
            }
            guntype.bulletVisible[i] = false;
          }
        }
      }
      
    }
  }  // moveBullets method end
  
  /**
   * energygunBullets
   * Shoots energy gun beam
   */
  public static void energygunBullets() {
    for (int i = 0; i < energyGun.numBullets; i++) {
      if (energyGun.charging[i]) {
        energyGun.isShooting = true;
        chargeLength[i] += 20;
      }
      if (chargeLength[i] >= energyGun.chargingTime) {
        energyGun.bulletVisible[i] = true;
        energyGun.charging[i] = false;
        chargeLength[i] = 0;  
      }
      else if (energyGun.bulletVisible[i]) {
        energyGun.bulletBoxes[i] = new Rectangle(energyGun.bulletLocs[i][0], energyGun.bulletLocs[i][1], 
                                                 energyGun.bulletW, energyGun.bulletH);  // Update hitbox
        beamFade[i] += 20;
        // If an enemy is hit
        for (int enemyType = 0; enemyType < enemies.length; enemyType++) {
          for (int curEnemy = 0; curEnemy < enemies[enemyType].length; curEnemy++) { 
            if (energyGun.bulletBoxes[i] != null && enemies[enemyType][curEnemy].health > 0 
                  && energyGun.bulletBoxes[i].intersects(enemies[enemyType][curEnemy].hitbox)) {
              enemies[enemyType][curEnemy].health -= energyGun.bulletDamage;
            }
          }
        }
        // Make beam fade after set time
        if (beamFade[i] >= energyGun.beamDuration) {
          energyGun.bulletVisible[i] = false;
          beamFade[i] = 0;
          shotGap[1] = energyGun.shotDelay;
          energyGun.isShooting = false;
        }
      }
    }
  }
  
  /**
   * platformCollision
   * This method iterates over each platform and checks if the player is touching it.
   * Based on which edge the player is touching, the player's velocity is adjusted.
   */
  public static void platformCollision() {
    
    // Iterate over each platform
    
    for (int i = 0; i < platforms.length; i++) {
      // If the player is on top of the platform
      if (player1.y >= platforms[i][1] - player1.h && player1.y <= platforms[i][1]
            && player1.x + player1.w > platforms[i][0] && player1.x < platforms[i][0] + platforms[i][2]) {
        player1.y = platforms[i][1] - player1.h - 1;
        player1.vY = 0;
        jumpCount = 0;
        player1.isJumping = false;
        player1.isBlockedRight = false;
        player1.isBlockedLeft = false;
      }
      
      // If player hits playform from the side or from below
      else if (player1.y + player1.h > platforms[i][1] && player1.y < platforms[i][1] + platforms[i][3] + 1
                 && player1.x + player1.w >= platforms[i][0] && player1.x <= platforms[i][0] + platforms[i][2]) {
        // If player collides with platform on right
        if (player1.x + 10 <= platforms[i][0]) {
          player1.vX = 0;
          player1.isBlockedRight = true;
        }
        // If player collides with platform on left
        else if (player1.x + player1.w - 10 >= platforms[i][0] + platforms[i][2]) {
          player1.vX = 0;
          player1.isBlockedLeft = true;
        }
        // If the player bumps their head on the platform from below
        else {
          player1.y = platforms[i][1] + platforms[i][3] + 1;
          player1.isBlockedRight = false;
          player1.isBlockedLeft = false;
        }
      }
    }
  }  // platformCollision method end
  
  /**
   * waterPhysics
   * This method moves the player differently if they are in water.
   * The method automatically implements water physics only if the player is in water.
   */
  public static void waterPhysics() {
    
    // Reset player properties and gravity to normal before checking if the player is in water.
    player1.isSwimming = false; 
    player1.jumpSpeed = -30;
    player1.speed = 10;
    gravity = 2.0;
    
    // Iterate over each block of water
    for (int i = 0; i < water.length; i++) {
      if (player1.hitbox.intersects(water[i])) {
        
        // Set a velocity cap so the player doesn't fall too fast through water
        if (player1.vY > 9) {
          player1.vY = 9;
        }
        else if (player1.vY < -9) {
          player1.vY = -9;
        }
        
        // Modify player properties
        player1.isSwimming = true;
        player1.speed = 7;
        player1.jumpSpeed = -7;
        
        gravity = 0.3; // Slower gravity when the player is in water
        jumpCount = 0; // Give player infinite jumps when in water
      }
    }
    
  } // waterPhysics method end
  
//---------------------------------------------------------------------------------
  
  /* GRAPHICS PANEL */
  static class GraphicsPanel extends JPanel {
    public GraphicsPanel() {
      setFocusable(true);
      requestFocusInWindow();
    }
    
    public void paintComponent(Graphics g) {
      super.paintComponent(g);
      // Fill background black
      g.setColor(Color.BLACK);
      g.fillRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
      
      // Get current player state
      player1.getState();
      
      // Draw Background 
      g.drawImage(background1, background1X, background1Y, this);
      g.drawImage(background2, background2X, background2Y, this);
      
      // Draw Water
      for (int i = 0; i < water.length; i++) {
        g.drawImage(waterImage, (int)water[i].getX(), (int)water[i].getY(), this);
      }
      
      // Draw Player
      g.drawImage(player1.currentImage, player1.x, player1.y, this);
      
      // Move Enemies
      for (int enemyType = 0; enemyType < enemies.length; enemyType++) {
        for (int curEnemy = 0; curEnemy < enemies[enemyType].length; curEnemy++) {
          if (enemies[enemyType][curEnemy].health > 0) {
            g.drawImage(enemies[enemyType][curEnemy].images[(int)enemies[enemyType][curEnemy].walkFrame],
                        enemies[enemyType][curEnemy].x, enemies[enemyType][curEnemy].y, this);
          }
        }
      }
      
      // Draw Ground
      g.setColor(Color.GREEN);
      g.fillRect(0, FRAME_HEIGHT-100, FRAME_WIDTH, 100);
      
      g.setColor(Color.GRAY);
      for (int i = 0; i < platforms.length; i++) {
        if (i <= 1) {
          g.fillRect(platforms[i][0], platforms[i][1], platforms[i][2], platforms[i][3]);
        }
        else {
          g.drawImage(platformImage, platforms[i][0], platforms[i][1], this);
        }
      }
      
      /* Draw Bullets */
      // Basic gun
      g.setColor(Color.RED);
      for (int i = 0; i < basicGun.numBullets; i++) {
        if (basicGun.bulletVisible[i]) {
          g.fillOval(basicGun.bulletLocs[i][0], basicGun.bulletLocs[i][1], basicGun.bulletW, basicGun.bulletH);
        }
      }
      // Energy gun
      for (int i = 0; i < energyGun.numBullets; i++) {
        if (energyGun.bulletVisible[i]) {
          energyGun.bulletLocs[i][1] = player1.y + player1.h/5;
          if (player1.facingRight) {
            energyGun.bulletLocs[i][0] = player1.x + player1.w;  
            g.drawImage(energyBeam[0],energyGun.bulletLocs[i][0], energyGun.bulletLocs[i][1] - 15, this);
          }
          else if (!player1.facingRight) {
            energyGun.bulletLocs[i][0] = player1.x - energyGun.bulletW;
            g.drawImage(energyBeam[1],energyGun.bulletLocs[i][0], energyGun.bulletLocs[i][1] - 15, this);
          }
        }
      }
      // Machine gun
      g.setColor(Color.YELLOW);
      for (int i = 0; i < machineGun.numBullets; i++) {
        if (machineGun.bulletVisible[i]) {
          g.fillRect(machineGun.bulletLocs[i][0], machineGun.bulletLocs[i][1], machineGun.bulletW, machineGun.bulletH);
        }
      }      
      
      // Display Information
      g.setColor(Color.WHITE);
      g.setFont(new Font("Helvetica", Font.BOLD, 30));
      g.drawString("Bullets: " + Integer.toString(curGun.numBullets - curGun.curBullet), 3*FRAME_WIDTH/4, 100);
      g.drawString("Health: " + Integer.toString(player1.health), FRAME_WIDTH/8, 100);
      if (reloading) {
        g.drawString("Reloading...", 3*FRAME_WIDTH/4, 150);
      }
      
      // If player is dead
      if (!inPlay && failedLevel) {
        // Make background black
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
        
        // Display game over information
        g.setColor(Color.WHITE);
        g.setFont(new Font("TimesRoman", Font.BOLD, 150));
        g.drawString("You Died...", FRAME_WIDTH/2 - 360, FRAME_HEIGHT/2 - 140);
        g.setFont(new Font("TimesRoman", Font.BOLD, 45));
        g.drawString("Deaths: " + Integer.toString(player1.deaths), FRAME_WIDTH/2 - 105, FRAME_HEIGHT/2 + 40);
        g.drawString("Press space to try again.", FRAME_WIDTH/2 - 235, FRAME_HEIGHT/2 + 220);
      }
    }  //PaintComponent method end
  }  //GraphicsPanel class end
  
//---------------------------------------------------------------------------------  

  /* KEY LISTENER */ 
  static class MyKeyListener implements KeyListener {
    public void keyPressed(KeyEvent e) {
      int key = e.getKeyCode();
      
      // Controls for if in game
      if (inPlay) {
        // Horizontal Movement
        if (key == KeyEvent.VK_A) {
          player1.vX = -player1.speed;
          player1.facingRight = false;
          player1.isWalking = true;
          player1.isBlockedRight = false;
          // Check if blocked
          if (player1.isBlockedLeft) {
            collisionShift = player1.vX;
          }
          else {
            collisionShift = 0;
          }
        }
        
        else if (key == KeyEvent.VK_D) {
          player1.vX = player1.speed;
          player1.facingRight = true;
          player1.isWalking = true;
          // Check if blocked
          player1.isBlockedLeft = false;
          if (player1.isBlockedRight) {   
            collisionShift = player1.vX;
          }
          else {
            collisionShift = 0;
          } 
        }
        
        // Jump
        if (key == KeyEvent.VK_W && jumpCount < 2) {
          player1.vY = (double)player1.jumpSpeed;
          player1.isJumping = true;
          
          // Only increment jump counter if the player is not in water
          if (!player1.isSwimming) {
            jumpCount++;
          }
          player1.isBlockedLeft = false;
          player1.isBlockedRight = false;
        }
        
        // Fire
        if (key == KeyEvent.VK_SPACE && !reloading && curGun.curBullet < curGun.numBullets) {   
          shotGap[0] += 20;
          curGun.isShooting = true;
        }
        
        // Reload
        if (key == KeyEvent.VK_K && curGun.numBullets - curGun.curBullet != curGun.numBullets && !reloading && !curGun.isShooting) {
          reloadDelay = new Timer(curGun.reloadDelay, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              reloading = curGun.reload(player1);
              reloadDelay.stop();
            }
          });
          reloadDelay.start();
          reloading = true;
        }
        
        //Switch to basic gun
        if (key == KeyEvent.VK_1 && curGun != basicGun) {
          curGun = basicGun;
        }
        //Switch to energy gun
        else if (key == KeyEvent.VK_2 && curGun != energyGun) {
          curGun = energyGun;
        }
        //Switch to machine gun
        else if (key == KeyEvent.VK_3 && curGun != machineGun) {
          curGun = machineGun;
        }
      }
      
      // If player is in menu
      else if (failedLevel) {
        if (key == KeyEvent.VK_SPACE) {
          failedLevel = false;
          inPlay = true;
        }
      }
    }
    
    public void keyReleased(KeyEvent e) {
      int key = e.getKeyCode();
      // Controls for if in game
      // Stop moving when key is released
      if ((key == KeyEvent.VK_A && !player1.facingRight) || (key == KeyEvent.VK_D && player1.facingRight)) {
        player1.vX = 0;
        player1.isWalking = false;
        player1.walkFrame = 0;
        collisionShift = 0;
      }
      
      // If player is dead
      else if (key == KeyEvent.VK_SPACE) {
        curGun.isShooting = false;
        // Reset shot delay for basic gun
        if (curGun == basicGun) {
          shotGap[0] = basicGun.shotDelay;
        }
      }
    }
    
    public void keyTyped(KeyEvent e) {}
  }  //KeyListener class end
}  //Main class end
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
    {FRAME_WIDTH/3, FRAME_HEIGHT - GROUND_HEIGHT - 100, 300, 100},
    {3*FRAME_WIDTH/4, FRAME_HEIGHT - GROUND_HEIGHT - 250, 400, 100},
    {FRAME_WIDTH, FRAME_HEIGHT - GROUND_HEIGHT - 150, 200, 10}
  };
  
  static Rectangle[] water = {
    new Rectangle(200, 100, 700, 600),
    new Rectangle(1700, 100, 800, 800)
  };
  
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
      
      // Scroll Background Image when moving left-to-right
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
        
        // Scroll platforms
        for (int i = 0; i < platforms.length; i++) {
          platforms[i][0] -= player1.vX - collisionShift;
        }
        for (int i = 0; i < water.length; i++) {
          water[i].setLocation((int)water[i].getX() - player1.vX + collisionShift, (int)water[i].getY());
        }
        
        // Scroll enemies with background
        for (int i = 0; i < goombas.length; i++) {
          goombas[i].init_x -= player1.vX - collisionShift;
          goombas[i].x -= player1.vX - collisionShift;
        }
      }
      
      // Move enemies
      for (int i = 0; i < goombas.length; i++) {  
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
      
      /* Gun bullets */
      // Basic gun bullets
      moveBullets(basicGun);
      
      // Energy gun bullet
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
          energyGun.bulletBoxes[i] = new Rectangle(energyGun.bulletLocs[i][0], energyGun.bulletLocs[i][1], energyGun.bulletW, energyGun.bulletH);  // Update hitbox
          beamFade[i] += 20;
          // If an enemy is hit
          for (int j = 0; j < goombas.length; j++) {
            if (energyGun.bulletBoxes[i] != null && goombas[j].health > 0 && energyGun.bulletBoxes[i].intersects(goombas[j].hitbox)) {
              if (energyGun.bulletVisible[i]) {
                goombas[j].health -= energyGun.bulletDamage;
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
      
    }
  }  //runGameLoop method end
  
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
      for (int j = 0; j < goombas.length; j++) {
        if (guntype.bulletBoxes[i] != null && goombas[j].health > 0 && guntype.bulletBoxes[i].intersects(goombas[j].hitbox)) {
          if (guntype.bulletVisible[i]) {
            goombas[j].health -= guntype.bulletDamage;
          }
          guntype.bulletVisible[i] = false;
        }
      }
    }
  }  // moveBullets method end
  
  /*
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
      else if (player1.y + player1.h > platforms[i][1]  - 1 && player1.y < platforms[i][1] + platforms[i][3] + 1
                 && player1.x + player1.w >= platforms[i][0] && player1.x <= platforms[i][0] + platforms[i][2]) {
        // If player collides with platform on right
        if (player1.x <= platforms[i][0]) {
          player1.vX = 0;
          player1.isBlockedRight = true;
        }
        // If player collides with platform on left
        else if (player1.x + player1.w >= platforms[i][0] + platforms[i][2]) {
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
      
      // Draw goombas
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
      
      /* Draw Bullets */
      // Basic gun
      g.setColor(Color.RED);
      for (int i = 0; i < basicGun.numBullets; i++) {
        if (basicGun.bulletVisible[i]) {
          g.fillOval(basicGun.bulletLocs[i][0], basicGun.bulletLocs[i][1], basicGun.bulletW, basicGun.bulletH);
        }
      }
      // Energy gun
      g.setColor(Color.BLUE);
      for (int i = 0; i < energyGun.numBullets; i++) {
        if (energyGun.bulletVisible[i]) {
          energyGun.bulletLocs[i][1] = player1.y + player1.h/5;
          if (player1.facingRight) {
            energyGun.bulletLocs[i][0] = player1.x + player1.w;
          }
          else if (!player1.facingRight) {
            energyGun.bulletLocs[i][0] = player1.x - energyGun.bulletW;
            
          }
          g.fillRect(energyGun.bulletLocs[i][0], energyGun.bulletLocs[i][1], energyGun.bulletW, energyGun.bulletH);
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
      g.drawString("Health: " + Integer.toString(player1.health), FRAME_WIDTH/4, 100);
      if (reloading) {
        g.drawString("Reloading...", 3*FRAME_WIDTH/4, 150);
      }
    }  //PaintComponent method end
  }  //GraphicsPanel class end
  
  // Key Listener
  static class MyKeyListener implements KeyListener {
    
    public void keyPressed(KeyEvent e) {
      int key = e.getKeyCode();
      
      // Horizontal Movement
      if (key == KeyEvent.VK_A) {
        player1.facingRight = false;
        player1.isWalking = true;
        player1.vX = -player1.speed;
        player1.isBlockedRight = false;
        if (player1.isBlockedLeft) {
          collisionShift = player1.vX;
        }
        else {
          collisionShift = 0;
        }
      }
      else if (key == KeyEvent.VK_D) {
        player1.facingRight = true;
        player1.isWalking = true;
        player1.vX = player1.speed;
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
        player1.isBlockedLeft = false;
        player1.isBlockedRight = false;
        
        // Only increment jump counter if the player is not in water
        if (!player1.isSwimming) {
          jumpCount++;
        }
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
    
    public void keyReleased(KeyEvent e) {
      int key = e.getKeyCode();
      // Stop moving when key released
      if ((key == KeyEvent.VK_A && !player1.facingRight) || (key == KeyEvent.VK_D && player1.facingRight)) {
        player1.vX = 0;
        player1.isWalking = false;
        player1.walkFrame = 0;
        collisionShift = 0;
      }
      // Reset shot delay for basic gun
      else if (key == KeyEvent.VK_SPACE) {
        curGun.isShooting = false;
        if (curGun == basicGun) {
          shotGap[0] = basicGun.shotDelay;
        }
      }
    }
    
    public void keyTyped(KeyEvent e) {}
  }  //KeyListener class end
}  //Main class end
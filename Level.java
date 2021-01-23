/**
 * [Level.java]
 * This program encodes all of the physics for a level.
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
// Sound Imports
import javax.sound.sampled.*; 

//---------------------------------------------------------------------------------

public abstract class Level {
  // Window Properties
  final int FRAME_WIDTH = 1280;
  final int FRAME_HEIGHT = 720;
  
  // Listeners
  JFrame window;
  GraphicsPanel canvas;
  MyKeyListener keyListener;
  
  // Background Properties
  BufferedImage background1;
  int background1X;
  int background1Y;
  int background1W = 1280;
  
  BufferedImage background2;
  int background2X;
  int background2Y;
  int background2W = 1280;
  
  // Gravity Properties
  final int GROUND_HEIGHT = 100;
  double gravity;
  int jumpCount;
  
  // Enemy Properties
  Goomba[] goombas;
  Crab[] crabs;
  Bee[] bees;
  Flyer[] flyers;
  Enemy[][] enemies;
  
  // Platform properties
  int[][] platforms;
  int[][] icePlatforms;
  int[][] walls;
  BufferedImage platformImage;
  BufferedImage icePlatformImage;
  
  // Spike properties
  int[][] spikes;
  BufferedImage spikeImage;
  
  Rectangle[] water;
  BufferedImage waterImage;
  
  // Coins
  Rectangle coins[];
  boolean coinsCollected[];
  BufferedImage coinImage;
  BufferedImage smallCoinImage;
  
  // Player
  Player player1;
  int collisionShift;
  BufferedImage playerHearts;
  
  // Guns
  Gun basicGun;
  Gun energyGun;
  Gun machineGun;
  Gun curGun;
  Timer reloadDelay;
  boolean reloading;
  int[] shotGap;
  int[] beamFade;
  int[] chargeLength;
  BufferedImage[] energyBeam = new BufferedImage[2];
  
  // Sound Effects
  AudioInputStream audioStream;
  Clip coinSound;

  
  // Game
  boolean inPlay;
  boolean failedLevel;
  boolean wonLevel;
  boolean endedLevel;
  BufferedImage deathImage;
  
//---------------------------------------------------------------------------------
  
  // MAIN METHOD
  public boolean myMain() {
    
    // Background Properties
    this.background1X = 0;
    this.background1Y = 0;
    this.background2X = background1W;
    this.background2Y = 0;
    
    this.gravity = 2.0;
    this.jumpCount = 0;
    
    this.collisionShift = 0;
    
    // Guns
    this.basicGun = new BasicGun(this.player1);
    this.energyGun = new EnergyGun(this.player1);
    this.machineGun = new MachineGun(this.player1);
    this.curGun = this.basicGun;
    this.reloading = false;
    this.shotGap = new int[] {this.basicGun.shotDelay, this.energyGun.shotDelay};
    this.beamFade = new int[] {0,0,0};
    this.chargeLength = new int[] {0,0,0};
    
    // Coins
    this.coinsCollected = new boolean[3];
    for (int i = 0; i < coins.length; i++) {
      this.coinsCollected[i] = false;
    }

    
    // Images and Sounds
    try {
      //Images
      this.background1 = ImageIO.read(new File("images/space copy.png"));
      this.background2 = ImageIO.read(new File("images/space copy flipped.png"));
      this.platformImage = ImageIO.read(new File("images/platform.png"));
      this.icePlatformImage = ImageIO.read(new File("images/icePlatform.png"));
      this.waterImage = ImageIO.read(new File("images/water.png"));
      this.spikeImage = ImageIO.read(new File("images/spikes.png"));
      for (int i = 0; i < 2; i++) {
        this.energyBeam[i] = ImageIO.read(new File("images/EnergyBeam" + i + ".png"));
      }
      this.deathImage = ImageIO.read(new File("images/gravestone.png"));
      this.playerHearts = ImageIO.read(new File("images/heart.png"));
      this.coinImage = ImageIO.read(new File("images/coin.png"));
      this.smallCoinImage = ImageIO.read(new File("images/SmallCoin.png"));
      this.playerHearts= ImageIO.read(new File("images/heart.png"));
      
      //Sounds
      File audioFile = new File("sounds/CoinSound.wav");
      this.audioStream = AudioSystem.getAudioInputStream(audioFile);
      this.coinSound = AudioSystem.getClip();
      this.coinSound.open(audioStream);
      this.coinSound.addLineListener(new CoinListener(this));
    } catch (Exception e) {} 
    
    this.inPlay = true;
    this.failedLevel = false;
    this.wonLevel = false;
    this.endedLevel = false;
    
    this.window.setVisible(true);
    this.runGameLoop();
    this.endLevel();
    
    this.canvas.removeKeyListener(this.keyListener);
    this.window.removeKeyListener(this.keyListener);
    this.window.remove(this.canvas);
    
    return this.failedLevel;
  }
  
//---------------------------------------------------------------------------------
  
  // GAME LOOP
  public void runGameLoop() {
    while (this.inPlay) {
      this.window.repaint();
      try {
        Thread.sleep(20);
      } catch (Exception e) {}
      
      this.waterPhysics(); // Check if the player is in water, and change physics accordingly
      this.moveBackground(); // Call method to move background as player moves
      
      // Scroll platforms
      for (int i = 0; i < this.platforms.length; i++) {
        this.platforms[i][0] -= this.player1.vX - this.collisionShift;
      }
      // Scroll platforms
      for (int i = 0; i < this.icePlatforms.length; i++) {
        this.icePlatforms[i][0] -= this.player1.vX - this.collisionShift;
      }
      for (int i = 0; i < this.water.length; i++) {
        this.water[i].setLocation((int)this.water[i].getX() - this.player1.vX + this.collisionShift, (int)this.water[i].getY());
      }
      for (int i = 0; i < this.walls.length; i++) {
        this.walls[i][0] -= this.player1.vX - this.collisionShift;
      }
      
      for (int i = 0; i < spikes.length; i++) {
        this.spikes[i][0] -= this.player1.vX - this.collisionShift;
        if (this.player1.hitbox.intersects(this.spikes[i][0], this.spikes[i][1], this.spikes[i][2], this.spikes[i][3])) {
          if (!this.player1.isInvulnerable) {
            this.player1.health -= 1;
          }
          this.player1.isInvulnerable = true;
        }
      }
      
      // Scroll coins and check if the player has collected them
      for (int i = 0; i < this.coins.length; i++) {
        this.coins[i].setLocation((int)this.coins[i].getX() - this.player1.vX + this.collisionShift, (int)this.coins[i].getY());
        if (!this.coinsCollected[i] && this.player1.hitbox.intersects(this.coins[i])) {
          this.coinsCollected[i] = true;
          this.coinSound.start();
        }
      }
      
      // Scroll Enemies
      for (int enemyType = 0; enemyType < enemies.length; enemyType++) {
        for (int curEnemy = 0; curEnemy < enemies[enemyType].length; curEnemy++) {
          this.enemies[enemyType][curEnemy].move(this.player1, this.collisionShift);
        }
      }
      
      /* Gun bullets */
      // Basic gun bullets
      this.moveBullets(basicGun);
      
      // Energy gun bullet
      this.energygunBullets();
      
      // Machine gun bullets
      this.moveBullets(machineGun);
      
      // Shooting
      // Basic gun shoot
      if (this.curGun == this.basicGun && this.shotGap[0] >= this.basicGun.shotDelay && this.basicGun.isShooting) {
        this.basicGun.shoot(player1);
        this.shotGap[0] = 0;
      }
      // Energy gun shoot
      else if (this.curGun == this.energyGun && this.shotGap[1] >= this.energyGun.shotDelay && this.energyGun.isShooting) {
        this.energyGun.shoot(player1);
        this.shotGap[1] = 0;
      }
      // Machine gun shoot
      else if (this.curGun == this.machineGun && this.machineGun.isShooting) {
        this.machineGun.shoot(player1); 
      }
      
      // Move Player when Jumping
      this.player1.vY += this.gravity;
      this.player1.y += (int)this.player1.vY;
      this.player1.hitbox.setLocation(this.player1.x, this.player1.y);
      
      // Land on Ground
      if (this.player1.y + this.player1.h >= this.FRAME_HEIGHT - this.GROUND_HEIGHT) {
        this.player1.y = this.FRAME_HEIGHT - this.GROUND_HEIGHT - this.player1.h;
        this.player1.vY = 0;
        this.jumpCount = 0;
        this.player1.isJumping = false;
        this.player1.isBlockedRight = false;
        this.player1.isBlockedLeft = false;

        // Reset Ice Physics
        this.player1.isOnIce = false;
        if (!this.player1.isWalking) {
          this.player1.vX = 0;
        }

      }
      
      this.platformCollision(this.platforms, false);
      this.platformCollision(this.icePlatforms, true);
      this.wallCollision();
      
      // Iterate over each frame of walking
      if (this.player1.isWalking && !this.player1.isJumping) {
        this.player1.walkFrame += 0.3;
        if (this.player1.walkFrame > 3) {
          this.player1.walkFrame = 0;
        }
      }
      
      // Player invulnerability when hit
      if (this.player1.isInvulnerable && this.player1.invulnerabilityCounter < 75) {
        this.player1.invulnerabilityCounter++;
      }
      else {
        this.player1.isInvulnerable = false;
        this.player1.invulnerabilityCounter = 0;
      }
      
      // Make sure player is walking right direction
      if (this.player1.facingRight && this.collisionShift > 0) {
        this.collisionShift = 0;
        this.player1.vX = 0;
      }
      else if (!this.player1.facingRight && this.collisionShift < 0) {
        this.collisionShift = 0;
        this.player1.vX = 0;
      }
      
      // Check if player has died
      if (player1.health <= 0) {
        this.inPlay = false;
        this.failedLevel = true;
      }
      
      // Check if player has finished the level
      if (player1.hitbox.intersects(platforms[0][0] - 5, platforms[0][1] - 5, platforms[0][2] + 10, platforms[0][3] + 5)) {
        this.inPlay = false;
        this.wonLevel = true;
      }
      
    }
  }  //runGameLoop method end
  
//---------------------------------------------------------------------------------
  
  public void endLevel() {
    while (!this.endedLevel) {
      // Display death menu
      this.window.repaint();
      try {
        Thread.sleep(20);
      } catch (Exception e) {}
      
    }
  }
  
//---------------------------------------------------------------------------------
  
  /**
   * moveBackground
   * This method moves the background in the opposite direction of the player
   */
  public void moveBackground() {
    
    if (!this.player1.isBlockedRight && !this.player1.isBlockedLeft) {
      this.background1X -= this.player1.vX - this.collisionShift;
      if (this.background1X + this.background1W <= 0) {
        this.background1X = this.background2X - this.player1.vX + this.background2W;
      }
      else if (this.background1X >= this.FRAME_WIDTH) {
        this.background1X = this.background2X - this.background2W - this.player1.vX;
      }
      
      this.background2X -= this.player1.vX - this.collisionShift;
      if (this.background2X + this.background2W <= 0) {
        this.background2X = this.background1X + this.background1W;
      }
      else if (this.background2X >= this.FRAME_WIDTH) {
        this.background2X = this.background1X - this.background1W;
      }
    }
    
  } // moveBackground method end
  
  
  /**
   * moveBullets
   * Moves bullets, detects enemy hits, and detects exit screen
   * @param type of gun
   */
  public  void moveBullets(Gun guntype) {
    for (int i = 0; i < guntype.numBullets; i++) {  
      if (guntype.bulletVisible[i]) {
        guntype.bulletLocs[i][0] += guntype.bulletVelocities[i];
        guntype.bulletBoxes[i].setLocation(guntype.bulletLocs[i][0], guntype.bulletLocs[i][1]);
      }
      
      // Remove Bullet when off-screen
      if (guntype.bulletLocs[i][0] > this.FRAME_WIDTH || guntype.bulletLocs[i][0] < 0) {
        guntype.bulletVisible[i] = false;
      }
      
      // If an enemy is hit
      for (int enemyType = 0; enemyType < this.enemies.length; enemyType++) {
        for (int curEnemy = 0; curEnemy < this.enemies[enemyType].length; curEnemy++) {
          if (guntype.bulletBoxes[i] != null && this.enemies[enemyType][curEnemy].health > 0 
                && guntype.bulletBoxes[i].intersects(this.enemies[enemyType][curEnemy].hitbox)) {
            if (guntype.bulletVisible[i]) {
              this.enemies[enemyType][curEnemy].health -= guntype.bulletDamage;
            }
            guntype.bulletVisible[i] = false;
          }
        }
      }
      
      // If bullet touches a platform
      if (guntype.bulletVisible[i]) {
        for (int k = 0; k < this.platforms.length; k++) {
          if (guntype.bulletBoxes[i].intersects(this.platforms[k][0], this.platforms[k][1], 
                                                this.platforms[k][2], this.platforms[k][3])) {
            guntype.bulletVisible[i] = false;
          }
        }
        
        // If bullet touches a wall
        for (int k = 0; k < this.walls.length; k++) {
          if (guntype.bulletBoxes[i].intersects(this.walls[k][0], this.walls[k][1], 
                                                this.walls[k][2], this.walls[k][3])) {
            guntype.bulletVisible[i] = false;
          }
        }
        
        // If bullet touches an ice platform
        for (int k = 0; k < this.icePlatforms.length; k++) {
          if (guntype.bulletBoxes[i].intersects(this.icePlatforms[k][0], this.icePlatforms[k][1], 
                                                this.icePlatforms[k][2], this.icePlatforms[k][3])) {
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
  public void energygunBullets() {
    for (int i = 0; i < this.energyGun.numBullets; i++) {
      if (this.energyGun.charging[i]) {
        this.energyGun.isShooting = true;
        this.chargeLength[i] += 20;
      }
      if (this.chargeLength[i] >= this.energyGun.chargingTime) {
        this.energyGun.bulletVisible[i] = true;
        this.energyGun.charging[i] = false;
        this.chargeLength[i] = 0;  
      }
      else if (this.energyGun.bulletVisible[i]) {
        this.energyGun.bulletBoxes[i] = new Rectangle(this.energyGun.bulletLocs[i][0], this.energyGun.bulletLocs[i][1], 
                                                      this.energyGun.bulletW, this.energyGun.bulletH);  // Update hitbox
        this.beamFade[i] += 20;
        // If an enemy is hit
        for (int enemyType = 0; enemyType < this.enemies.length; enemyType++) {
          for (int curEnemy = 0; curEnemy < this.enemies[enemyType].length; curEnemy++) { 
            if (this.energyGun.bulletBoxes[i] != null && this.enemies[enemyType][curEnemy].health > 0 
                  && this.energyGun.bulletBoxes[i].intersects(this.enemies[enemyType][curEnemy].hitbox)) {
              this.enemies[enemyType][curEnemy].health -= this.energyGun.bulletDamage;
            }
          }
        }
        // Make beam fade after set time
        if (this.beamFade[i] >= this.energyGun.beamDuration) {
          this.energyGun.bulletVisible[i] = false;
          this.beamFade[i] = 0;
          this.shotGap[1] = this.energyGun.shotDelay;
          this.energyGun.isShooting = false;
        }
      }
    }
    
  }
  
  /**
   * platformCollision
   * This method iterates over each platform and checks if the player is touching it.
   * Based on which edge the player is touching, the player's velocity is adjusted.
   */
  public void platformCollision(int[][] platArray, boolean isIcy) {
    
    // Iterate over each platform
    
    for (int i = 0; i < platArray.length; i++) {
      if (isIcy || i > 0) {
        // If the player is on top of the platform
        if (this.player1.y >= platArray[i][1] - this.player1.h && this.player1.y <= platArray[i][1]
              && this.player1.x + this.player1.w > platArray[i][0] && this.player1.x < platArray[i][0] + platArray[i][2]) {
          this.player1.y = platArray[i][1] - this.player1.h - 1;
          this.player1.vY = 0;
          this.jumpCount = 0;
          this.player1.isJumping = false;
          this.player1.isOnIce = false; // reset to false before checking if the player is on ice

          // Reset Ice Physics before checking if the player is on ice
          this.player1.isOnIce = false;
          if (isIcy) {
            this.player1.isOnIce = true;
          }
          if (!this.player1.isWalking && !this.player1.isOnIce) {
            this.player1.vX = 0;
          }
        }
        
        // If the player bumps their head on the platform from below
        else if (this.player1.y + this.player1.h > platArray[i][1] && this.player1.y < platArray[i][1] + platArray[i][3]
                   && this.player1.x + this.player1.w > platArray[i][0] && this.player1.x < platArray[i][0] + platArray[i][2]) {
          
          this.player1.y = platArray[i][1] + platArray[i][3] + 1;
          this.player1.vY = 0;
        }
      }
    }
  }  // platformCollision method end 
  
  /**
   * wallCollision
   * This method iterates over each wall and checks if the player is touching it.
   * Based on which edge the player is touching, the player's velocity is adjusted.
   */
  public void wallCollision() {
    for (int i = 0; i < this.walls.length; i++) {
      // If player hits wall from the side
      if (this.player1.y + this.player1.h > this.walls[i][1] && this.player1.y < this.walls[i][1] + this.walls[i][3] + 1
            && this.player1.x + this.player1.w >= this.walls[i][0] && this.player1.x <= this.walls[i][0] + this.walls[i][2]) {
        // If player collides with wall on right
        if (this.player1.x <= this.walls[i][0]) {
          this.player1.vX = 0;
          this.player1.isBlockedRight = true;
        }
        // If player collides with wall on left
        else if (this.player1.x + this.player1.w >= this.walls[i][0] + this.walls[i][2]) {
          this.player1.vX = 0;
          this.player1.isBlockedLeft = true;
        }
      }
      
    }
  } // wallCollision method end
  
  /**
   * waterPhysics
   * This method moves the player differently if they are in water.
   * The method automatically implements water physics only if the player is in water.
   */
  public void waterPhysics() {
    
    // Reset player properties and gravity to normal before checking if the player is in water.
    this.player1.isSwimming = false; 
    this.player1.jumpSpeed = -28;
    this.player1.speed = 10;
    this.gravity = 2.0;
    
    // Iterate over each block of water
    for (int i = 0; i < this.water.length; i++) {
      if (this.player1.hitbox.intersects(water[i])) {
        
        // Set a velocity cap so the player doesn't fall too fast through water
        if (this.player1.vY > 9) {
          this.player1.vY = 9;
        }
        else if (this.player1.vY < -9) {
          this.player1.vY = -9;
        }
        
        // Modify player properties
        this.player1.isSwimming = true;
        this.player1.speed = 7;
        this.player1.jumpSpeed = -7;
        
        this.gravity = 0.3; // Slower gravity when the player is in water
        this.jumpCount = 0; // Give player infinite jumps when in water
      }
    }
  } // waterPhysics method end
  
//---------------------------------------------------------------------------------
  
  /* GRAPHICS PANEL */
  class GraphicsPanel extends JPanel {
    Level level;
    
    public GraphicsPanel(Level level) {
      this.level = level;
      setFocusable(true);
      requestFocusInWindow();
    }
    
    public void paintComponent(Graphics g) {
      super.paintComponent(g);
      
      // Fill background black
      g.setColor(Color.BLACK);
      g.fillRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
      
      // Get current player state
      level.player1.getState();
      
      // Draw Background 
      g.drawImage(level.background1, level.background1X, level.background1Y, this);
      g.drawImage(level.background2, level.background2X, level.background2Y, this);
      
      // Draw Water
      for (int i = 0; i < level.water.length; i++) {
        g.drawImage(level.waterImage, (int)level.water[i].getX(), (int)level.water[i].getY(), this);
      }
      
      // Draw Player
      if (level.player1.isInvulnerable) {
        if ((level.player1.invulnerabilityCounter/5)%2 == 0) { 
          g.drawImage(level.player1.currentImage, level.player1.x, level.player1.y, this);
        }
      }
      else {
        g.drawImage(level.player1.currentImage, level.player1.x, level.player1.y, this);
      }
      
      // Draw Spikes
      for (int i = 0; i < level.spikes.length; i++) {
        g.drawImage(level.spikeImage, level.spikes[i][0], level.spikes[i][1], this);
      }
      
      // Draw Coins
      for (int i = 0; i < level.coins.length; i++) {
        if(!level.coinsCollected[i]) {
          g.drawImage(level.coinImage, (int)level.coins[i].getX(), (int)level.coins[i].getY(), this);
        }
      }
      
      // Draw Enemies
      for (int enemyType = 0; enemyType < level.enemies.length; enemyType++) {
        for (int curEnemy = 0; curEnemy < level.enemies[enemyType].length; curEnemy++) {
          if (level.enemies[enemyType][curEnemy].health > 0) {
            g.drawImage(
                        level.enemies[enemyType][curEnemy].images[(int)level.enemies[enemyType][curEnemy].walkFrame],
                        level.enemies[enemyType][curEnemy].x, level.enemies[enemyType][curEnemy].y, this);
          }
        }
      }
      
      // Draw platforms, walls, and end checkpoint
      for (int i = 0; i < platforms.length; i++) {
        if (i != 0) {
          g.drawImage(level.platformImage, level.platforms[i][0], level.platforms[i][1], this);
        }
        else {
          // Draw end checkpoint/flag
          g.setColor(Color.WHITE);
          g.fillRect(level.platforms[i][0], level.platforms[i][1], 10, 150);
          g.setColor(Color.RED);
          g.fillRect(level.platforms[i][0], level.platforms[i][1], 75, 50);
        }
      }
      for (int i = 0; i < icePlatforms.length; i++) {
        g.drawImage(level.icePlatformImage, level.icePlatforms[i][0], level.icePlatforms[i][1], this);
      }
      
      Color brown = new Color(73, 43, 0);
      g.setColor(brown);
      for (int i = 0; i < walls.length; i++) {
        g.fillRect(level.walls[i][0], level.walls[i][1], level.walls[i][2], level.walls[i][3]);
      }
      
      /* Draw Bullets */
      // Basic gun
      g.setColor(Color.RED);
      for (int i = 0; i < level.basicGun.numBullets; i++) {
        if (level.basicGun.bulletVisible[i]) {
          g.fillOval(level.basicGun.bulletLocs[i][0], level.basicGun.bulletLocs[i][1], level.basicGun.bulletW, level.basicGun.bulletH);
        }
      }
      
      // Energy gun
      Color myBlue = new Color(0, 190, 255);
      g.setColor(myBlue);
      for (int i = 0; i < level.energyGun.numBullets; i++) {
        level.energyGun.bulletLocs[i][1] = level.player1.y + level.player1.h/5;
        if (level.energyGun.charging[i]) {
          if (level.player1.facingRight) {
            level.energyGun.bulletLocs[i][0] = level.player1.x + level.player1.w;  
            g.fillRect(level.energyGun.bulletLocs[i][0], level.energyGun.bulletLocs[i][1], 20, 27);
          }
          else if (!level.player1.facingRight) {
            level.energyGun.bulletLocs[i][0] = level.player1.x - 20;
            g.fillRect(level.energyGun.bulletLocs[i][0], level.energyGun.bulletLocs[i][1], 20, 27);
          }
        }
        else if (level.energyGun.bulletVisible[i]) {
          if (level.player1.facingRight) {
            level.energyGun.bulletLocs[i][0] = level.player1.x + level.player1.w;  
            g.drawImage(level.energyBeam[0], level.energyGun.bulletLocs[i][0], level.energyGun.bulletLocs[i][1] - 15, this);
          }
          else if (!level.player1.facingRight) {
            level.energyGun.bulletLocs[i][0] = level.player1.x - level.energyGun.bulletW;
            g.drawImage(level.energyBeam[1], level.energyGun.bulletLocs[i][0], level.energyGun.bulletLocs[i][1] - 15, this);
          }
        }
      }
      
      // Machine gun
      g.setColor(Color.YELLOW);
      for (int i = 0; i < level.machineGun.numBullets; i++) {
        if (level.machineGun.bulletVisible[i]) {
          g.fillRect(level.machineGun.bulletLocs[i][0], level.machineGun.bulletLocs[i][1], level.machineGun.bulletW, level.machineGun.bulletH);
        }
      }      
      
      // Display Information
      g.setColor(Color.WHITE);
      g.setFont(new Font("Helvetica", Font.BOLD, 30));
      g.drawString("Bullets: " + Integer.toString(level.curGun.numBullets - level.curGun.curBullet), 3*level.FRAME_WIDTH/4 + 100, 60);
      g.drawString("Health: " + Integer.toString(level.player1.health), 60, 60);
      for (int h = 0; h < level.player1.health; h++) {
        g.drawImage(level.playerHearts, level.FRAME_WIDTH/8 - 90 + 40*h, 70, this);
      }
      for (int i = 0; i < coinsCollected.length; i++) {
         if (coinsCollected[i]) {
           g.drawImage(level.smallCoinImage, 3*level.FRAME_WIDTH/4 + 110 + i*40, 70, this);
         }
      }
      if (level.reloading) {
        g.drawString("Reloading...", level.FRAME_WIDTH/2 - 93, 60);
      }
      
      // If player is dead
      if (level.failedLevel) {
        // Fill game window with black
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
        
        // Draw little gravestone image
        g.drawImage(deathImage, FRAME_WIDTH/2 - 115, FRAME_HEIGHT/2 - 84, this);
        
        // Display game over information
        g.setColor(Color.WHITE);
        g.setFont(new Font("TimesRoman", Font.BOLD, 150));
        g.drawString("You Died...", FRAME_WIDTH/2 - 360, FRAME_HEIGHT/2 - 140);
        g.setFont(new Font("TimesRoman", Font.BOLD, 45));
        g.drawString("Press space to try again.", FRAME_WIDTH/2 - 235, FRAME_HEIGHT/2 + 220);
      }
      else if (level.wonLevel) {
        // FIll game window with yellow/gold
        Color gold = new Color(249, 166, 2);
        g.setColor(gold);
        g.fillRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
        
        // Display victory information
        g.setColor(Color.BLACK);
        g.setFont(new Font("TimesRoman", Font.BOLD, 100));
        g.drawString("You beat the level!", FRAME_WIDTH/2 - 405, FRAME_HEIGHT/2 - 130);
        g.setFont(new Font("TimesRoman", Font.BOLD, 45));
        g.drawString("Press space to go to the next level.", FRAME_WIDTH/2 - 330, FRAME_HEIGHT/2 + 210);
      }
      
    }  //PaintComponent method end
  }  //GraphicsPanel class end
  
//---------------------------------------------------------------------------------
  
  /* KEY LISTENER */ 
  class MyKeyListener implements KeyListener {
    Level level;
    
    public MyKeyListener (Level level) {
      this.level = level;
    }
    
    public void keyPressed(KeyEvent e) {
      int key = e.getKeyCode();
      
      // Horizontal Movement
      if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) {
        level.player1.vX = -level.player1.speed;
        level.player1.facingRight = false;
        level.player1.isWalking = true;
        level.player1.isBlockedRight = false;
        
        if (level.player1.isBlockedLeft) {
          level.collisionShift = level.player1.vX;
        }
        else {
          level.collisionShift = 0;
        }
        
      }
      
      else if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) {
        level.player1.vX = level.player1.speed;
        level.player1.facingRight = true;
        level.player1.isWalking = true;
        
        level.player1.isBlockedLeft = false;
        if (level.player1.isBlockedRight) {   
          level.collisionShift = player1.vX;
        }
        else {
          level.collisionShift = 0;
        }
      }
      
      // Jump
      if ((key == KeyEvent.VK_W || key == KeyEvent.VK_UP ) && jumpCount < 2) {
        level.player1.vY = (double)level.player1.jumpSpeed;
        level.player1.isJumping = true;
        
        // Only increment jump counter if the player is not in water
        if (!level.player1.isSwimming) {
          level.jumpCount++;
        }
        
        level.player1.isBlockedLeft = false;
        level.player1.isBlockedRight = false;
        level.player1.jumpSound.start();
      }
      
      // Fire
      if (key == KeyEvent.VK_SPACE && !level.reloading && level.curGun.curBullet < level.curGun.numBullets) {   
        level.shotGap[0] += 20;
        level.curGun.isShooting = true;
      }
      
      // Reload
      if (key == KeyEvent.VK_K && level.curGun.numBullets - level.curGun.curBullet != level.curGun.numBullets && !level.reloading && !level.curGun.isShooting) {
        level.reloadDelay = new Timer(curGun.reloadDelay, new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            level.reloading = curGun.reload(player1);
            level.reloadDelay.stop();
          }
        });
        level.reloadDelay.start();
        level.reloading = true;
      }
      
      //Switch to basic gun
      if (key == KeyEvent.VK_1 && level.curGun != level.basicGun && !level.reloading) {
        level.curGun = level.basicGun;
      }
      //Switch to energy gun
      else if (key == KeyEvent.VK_2 && level.curGun != level.energyGun && !level.reloading) {
        level.curGun = level.energyGun;
      }
      //Switch to machine gun
      else if (key == KeyEvent.VK_3 && level.curGun != level.machineGun && !level.reloading) {
        level.curGun = level.machineGun;
      }
      
      // If player is in menu
      else if (level.failedLevel) {
        if (key == KeyEvent.VK_SPACE) {
          level.endedLevel = true;
        }
      }
      else if (level.wonLevel) {
        if (key == KeyEvent.VK_SPACE) {
          level.endedLevel = true;
        }
      }
    } // end keyPressed
    
    public void keyReleased(KeyEvent e) {
      int key = e.getKeyCode();
      
      if (inPlay) {
        // Stop moving when key is released
        if ( ((key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) && !level.player1.facingRight) 
              || ((key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) && level.player1.facingRight) ) {
          // Do not stop moving if the player is on ice
          if (!level.player1.isOnIce) {
            level.player1.vX = 0;
            level.collisionShift = 0;
          }
          
          level.player1.isWalking = false;
          level.player1.walkFrame = 0;
          
        }
        // Reset shot delay for basic gun
        else if (key == KeyEvent.VK_SPACE) {
          level.curGun.isShooting = false;
          if (level.curGun == level.basicGun) {
            level.shotGap[0] = level.basicGun.shotDelay;
          }
        }
      }
    }
    
    public void keyTyped(KeyEvent e) {}
  }  //KeyListener class end

//---------------------------------------------------------------------------------
  
  // SOUND EFFECT LISTENER
   class CoinListener implements LineListener {
     Level level;

     public CoinListener(Level level) {
       super();
       this.level = level;
     }

     public void update(LineEvent event) {
       if (event.getType() == LineEvent.Type.STOP) {
         level.coinSound.flush();              // clear the buffer with audio data
         level.coinSound.setFramePosition(0);  // prepare to start from the beginning
       }
     }
   } 
}  //Level class end
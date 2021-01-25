/**
 * [Player.java]
 * This class represents the player object
 * It contains all the properties and methods of a player
 * @author Avneesh Verma and Jeffrey Xu
 * @version 1.0 January 5, 2020
 */

/* Imports */
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
// Image imports
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
// Sound Imports
import javax.sound.sampled.*; 

public class Player {
  
  /* VARIABLES */
  // Movement
  int x, y, w, h;
  int speed, jumpSpeed;
  int vX;
  double vY;
  
  // Images
  BufferedImage[] images = new BufferedImage[10];
  BufferedImage currentImage;
  // Sound Effects
  AudioInputStream audioStream;
  Clip jumpSound;
  
  // Player States
  boolean facingRight, isWalking, isJumping, isSwimming, isInvulnerable, isBlockedRight, isBlockedLeft, isOnIce;
  double walkFrame;
  int invulnerabilityCounter;
  
  // Health
  int health;
  Rectangle hitbox;
  
  /*
   * CONSTRUCTOR
   * This method is the constructor for the Player object.
   * It initializes all the properties of the Player object.
   */
  public Player(int x, int y, int speed, int jumpSpeed) {
    // Player Coordinates
    this.x = x;
    this.y = y;
    this.w = 33;
    this.h = 63;
    
    // Speed variables
    this.speed = speed;
    this.jumpSpeed = jumpSpeed; 
    this.vX = 0;
    this.vY = 0.0;
    
    // Initialize images
    try {
      for (int i = 0; i < 10; i++) {
        this.images[i] = ImageIO.read(new File("images/mario" + i + ".png"));
      }
      this.currentImage = this.images[4];
    } catch (Exception e) {}
    
    // Player States
    this.facingRight = true;
    this.isWalking = false;
    this.isJumping = false;
    this.isBlockedRight = false;
    this.isBlockedLeft = false;
    this.isSwimming = false;
    this.isInvulnerable = false;
    this.walkFrame = 0.0; // To iterate over each frame when walking
    this.invulnerabilityCounter = 0;
    
    // health
    this.hitbox = new Rectangle(this.x, this.y, this.w, this.h);
    this.health = 3;

    // Sound Effects
    try {
      File audioFile = new File("sounds/Jump.wav");
      this.audioStream = AudioSystem.getAudioInputStream(audioFile);
      this.jumpSound = AudioSystem.getClip();
      this.jumpSound.open(audioStream);
      this.jumpSound.addLineListener(new JumpListener(this));
    } catch (Exception e){} 

  }
  
  /*
   * getState
   * This method sets the correct currentImage,
   * based on the current state of the player
   */
  public void getState() {
    if (this.facingRight) {
      if (this.isJumping) {
        this.currentImage = this.images[9];
      }
      else if (this.isWalking) {
        this.currentImage = this.images[5 + (int)this.walkFrame]; // Round down to stall frames
      }
      else {
        this.currentImage = this.images[4];
      }
    }
    
    else {
      if (this.isJumping) {
        this.currentImage = this.images[8];
      }
      else if (this.isWalking) {
        this.currentImage = this.images[1 + (int)this.walkFrame]; // Round down to stall frames
      }
      else {
        this.currentImage = this.images[0];
      }
    }
  }

  // SOUND EFFECT LISTENER
  class JumpListener implements LineListener {
    Player player;
    
    public JumpListener(Player player) {
      super();
      this.player = player;
    }

    public void update(LineEvent event) {
      if (event.getType() == LineEvent.Type.STOP) {
        player.jumpSound.flush();              // clear the buffer with audio data
        player.jumpSound.setFramePosition(0);  // prepare to start from the beginning
      }
    }
  } 
}
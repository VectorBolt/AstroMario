/**
 * [Goomba.java]
 * This class represents a goomba enemy.
 * This is the most basic enemy.
 * It can only move left and right.
 * @author Avneesh Verma and Jeffrey Xu
 * @version 1.0 January 5, 2020
 */

/* Imports */
import java.awt.*;
// Image imports
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Goomba extends Enemy {
  public Goomba(int x, int y, int walkRange) {
    this.x = x;
    this.y = y;
    this.w = 32;
    this.h = 32;
    
    // These will be used to check if the goomba has exceeded its walkRange
    this.init_x = x;
    this.init_y = y;
    
    this.walkRange = walkRange; 
    this.speed = 1;
    
    this.movesHorizontally = true;
    this.targetsX = false;
    this.targetsY = false;
    
    this.health = 20;
    this.damage = 1;
    this.hitbox = new Rectangle(this.x, this.y, this.w, this.h);
    try {
      for (int i = 0; i < 2; i++) {
        this.images[i] = ImageIO.read(new File("images/goomba" + i + ".png"));
      };
    } catch (Exception e) {}
    this.walkFrame = 0.0;
  }
}
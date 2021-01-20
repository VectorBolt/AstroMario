/**
 * [Enemy.java]
 * This class represents an abstract enemy
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

public abstract class Enemy {
  
  // VARIABLES
  int x, y, w, h;
  int init_x, init_y;
  int walkRange, speed;
  boolean movesHorizontally, targetsX, targetsY;
  int health;
  int damage;
  Rectangle hitbox;
  BufferedImage[] images = new BufferedImage[2];
  double walkFrame;
  
  /**
   * collision
   * This method checks if the player collided with the enemy.
   * If the player touched the enemey, the players health will be reduced
   * and the player will be invincible for a temporary amount of time.
   * @param player the instance of the player
   */
  public void collision(Player player) {
    if (this.hitbox.intersects(player.hitbox) && this.health > 0 && !player.isInvulnerable) {
      player.health -= this.damage;
      player.isInvulnerable = true;
    }
  }
  

  /**
   * move
   * This method updates the enemies coordinates as the player moves
   * @param player the instance of the player
   * @param screenShift
   */
  public void move(Player player, int screenShift) {
    // Move Enemies
    this.init_x -= player.vX - screenShift;
    this.x -= player.vX - screenShift;

    if (this.movesHorizontally) {
      this.x += this.speed;
      if (Math.abs(this.x - this.init_x) >= this.walkRange) {
        this.speed *= -1;
      }
    }
    else {
      this.y += this.speed;
      if (Math.abs(this.y - this.init_y) >= this.walkRange) {
        this.speed *= -1;
      }
    }

    this.hitbox.setLocation(this.x, this.y);
    this.collision(player);

    // Walk Frames for images
    this.walkFrame += 0.1;
    if (this.walkFrame >= 2) {
      this.walkFrame = 0;
    }
  }
}
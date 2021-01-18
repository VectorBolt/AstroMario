/*
 * Enemy.java
 * This class represents an abstract enemy
 * @author Avneesh Verma
 * @version 1.0 January 5, 2020
 */

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
  
  /*
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
  
}


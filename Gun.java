/**
 * [Gun.java]
 * This abstract class represents the base gun
 * @author Jeffrey Xu and Avneesh Verma
 * @version 1.0 January 18, 2020
 */

/* Imports */
import javax.swing.*;
import java.awt.*;

public abstract class Gun {
  
  /* VARIABLES */
  // Location
  int[][] bulletLocs; // {{x1, y1}, {x2, y2}, etc}
  int bulletW;
  int bulletH;
  // Shooting and reloading
  int chargingTime;
  int beamDuration;
  boolean[] charging;
  int shotDelay;
  int reloadDelay;
  boolean[] bulletVisible;
  // Other properties
  int bulletDamage;
  int numBullets;
  boolean isShooting;
  int[] bulletVelocities;
  Rectangle[] bulletBoxes;
  int curBullet;
  
  /**
   * shoot
   * Positions and readys bullets to be shot
   * @param the instance of player
   */
  public void shoot(Player player) {
    if (this.curBullet < this.numBullets) {
      this.bulletVisible[this.curBullet] = true;
      this.bulletLocs[this.curBullet][1] = player.y + player.h/2;
      this.bulletVelocities[this.curBullet] = 20;
      if (player.facingRight) {
        this.bulletLocs[this.curBullet][0] = player.x + player.w;
        this.bulletVelocities[this.curBullet] *= 1; // Move to the right
      }
      else if (!player.facingRight) {
        this.bulletLocs[this.curBullet][0] = player.x;
        this.bulletVelocities[this.curBullet] *= -1; // Move to the left 
      }
      
      this.bulletBoxes[this.curBullet] = new Rectangle(this.bulletLocs[this.curBullet][0],
                                                       this.bulletLocs[this.curBullet][1], this.bulletW, this.bulletH);
      this.curBullet++;
    }
  }
  
  public Boolean reload(Player player) {
    this.curBullet = 0;
    return false;
  }
}

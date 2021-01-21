/**
 * [EnergyGun.java]
 * This class represents the energy gun
 * The energy gun shoots a large beam that kills everything in its way
 * @author Jeffrey Xu and Avneesh Verma
 * @version 1.0 January 18, 2020
 */

/* Imports */
import javax.swing.*;
import java.awt.*;
// Image imports
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class EnergyGun extends Gun {
  public EnergyGun(Player player) {
    
    /* VARIABLES */
    // Bullets
    this.numBullets = 3;
    this.bulletDamage = 9999;  // Oneshots the enemy
    this.bulletVisible = new boolean[numBullets];
    this.bulletBoxes = new Rectangle[numBullets];
    this.curBullet = 0;
    // Location
    this.bulletLocs = new int[numBullets][numBullets]; // {{x1, y1}, {x2, y2}, etc}
    this.bulletW = 930;
    this.bulletH = 29;
    // Shooting and reloading
    this.shotDelay = 2000;
    this.reloadDelay = 3000;
    this.beamDuration = 1000;
    this.chargingTime = 2000;
    this.isShooting = false;
    this.charging = new boolean[numBullets];
    
    // Initialize individual bullets
    for (int i = 0; i < this.numBullets; i++) {
      this.bulletLocs[i][0] = player.x + player.w;
      this.bulletLocs[i][1] = player.y/2;
      this.bulletVisible[i] = false;
    }
  }
  
  /**
   * shoot
   * Specialized shoot method for energy gun
   * Begins the shooting process of the energy gun by charging the shot
   * @param the instance of player
   * return true;
   */
  @Override
  public void shoot(Player player) {
    if (this.curBullet < this.numBullets) {
      this.charging[this.curBullet] = true;
      this.curBullet++;
    }
  }
}
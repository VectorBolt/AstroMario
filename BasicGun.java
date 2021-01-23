/**
 * [BasicGun.java]
 * This class represents the basic gun
 * The basic gun is just a normal gun; similar to a pistol
 * @author Jeffrey Xu and Avneesh Verma
 * @version 1.0 January 18, 2020
 */

/* Imports */
import javax.swing.*;
import java.awt.*;

public class BasicGun extends Gun {
  public BasicGun(Player player) {
    
    /* VARIABLES */
    // Bullets
    this.numBullets = 8;
    this.curBullet = 0;
    this.bulletVelocities = new int[numBullets];
    this.bulletBoxes = new Rectangle[numBullets];
    this.bulletDamage = 25;
    // Location
    this.bulletLocs = new int[numBullets][numBullets]; // {{x1, y1}, {x2, y2}, etc}
    this.bulletW = 15;
    this.bulletH = 9;
    // Shooting and reloading
    this.shotDelay = 100;
    this.reloadDelay = 750;
    this.bulletVisible = new boolean[numBullets];
    this.isShooting = false;

    // Initialize individual bullets
    for (int i = 0; i < this.numBullets; i++) {
      this.bulletLocs[i][0] = player.x + player.w;
      this.bulletLocs[i][1] = player.y/2;
      this.bulletVisible[i] = false;
    }
  }
}

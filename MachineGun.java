/**
 * [MachineGun.java]
 * This class represents the machine gun
 * The machine gun shoots rapidly and has many bullets; doesn't do much damage though
 * @author Jeffrey Xu and Avneesh Verma
 * @version 1.0 January 18, 2020
 */

/* Imports */
import javax.swing.*;
import java.awt.*;

public class MachineGun extends Gun {
  public MachineGun(Player player) {
    
    /* VARIABLES */
    // Bullets
    this.bulletDamage = 3;
    this.numBullets = 75;
    this.bulletVelocities = new int[numBullets];
    this.bulletBoxes = new Rectangle[numBullets];
    // Location
    this.bulletLocs = new int[numBullets][numBullets]; // {{x1, y1}, {x2, y2}, etc} 
    this.bulletW = 10;
    this.bulletH = 6;
    // Shooting/reloading
    this.reloadDelay = 1000;
    this.bulletVisible = new boolean[numBullets];
    this.isShooting = false;
    
    this.curBullet = 0;
    
    // Initialize individual bullets
    for (int i = 0; i < this.numBullets; i++) {
      this.bulletLocs[i][0] = player.x + player.w;
      this.bulletLocs[i][1] = player.y/2;
      this.bulletVisible[i] = false;
    }
  }
}

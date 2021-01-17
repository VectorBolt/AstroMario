import javax.swing.*;
import java.awt.*;

public class EnergyGun extends Gun {
  
  public EnergyGun(Player player) {
    
    this.bulletDamage = 9999;  // I guess we make it oneshot
    this.shotDelay = 3000;
    this.reloadDelay = 3000;
    this.beamDuration = 1000;
    this.chargingTime = 2000;
    this.numBullets = 3;
    this.bulletLocs = new int[numBullets][numBullets]; // {{x1, y1}, {x2, y2}, etc}
    this.bulletVisible = new boolean[numBullets];
    this.isShooting = false;
    this.charging = new boolean[numBullets];
    this.bulletBoxes = new Rectangle[numBullets];
    this.bulletW = 2000;
    this.bulletH = 40;
    this.curBullet = 0;
    
    // Initialize individual bullets
    for (int i = 0; i < this.numBullets; i++) {
      this.bulletLocs[i][0] = player.x + player.w;
      this.bulletLocs[i][1] = player.y/2;
      this.bulletVisible[i] = false;
    }
  }
  // Specialized shoot method for energy gun
  @Override
  public void shoot(Player player) {
    if (this.curBullet < this.numBullets) {
      this.charging[this.curBullet] = true;
      this.curBullet++;
    }
  }
}

import javax.swing.*;
import java.awt.*;

public class BasicGun extends Gun {
  
  public BasicGun(Player player) {
    
    this.bulletDamage = 10;
    this.shotDelay = 100;
    this.reloadDelay = 750;
    this.numBullets = 7;
    this.bulletLocs = new int[numBullets][numBullets]; // {{x1, y1}, {x2, y2}, etc}
    this.bulletVisible = new boolean[numBullets];
    this.isShooting = false;
    this.bulletVelocities = new int[numBullets];
    this.bulletBoxes = new Rectangle[numBullets];
    this.bulletW = 15;
    this.bulletH = 9;
    this.curBullet = 0;
    
    // Initialize individual bullets
    for (int i = 0; i < this.numBullets; i++) {
      this.bulletLocs[i][0] = player.x + player.w;
      this.bulletLocs[i][1] = player.y/2;
      this.bulletVisible[i] = false;
    }
  }
}

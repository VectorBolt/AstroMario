import javax.swing.*;
import java.awt.*;

public class MachineGun extends Gun {
  
  public MachineGun(Player player) {
    
    this.bulletDamage = 5;
    this.reloadDelay = 1000;
    this.numBullets = 9999;
    this.bulletLocs = new int[numBullets][numBullets]; // {{x1, y1}, {x2, y2}, etc}
    this.bulletVisible = new boolean[numBullets];
    this.isShooting = false;
    this.bulletVelocities = new int[numBullets];
    this.bulletBoxes = new Rectangle[numBullets];
    this.bulletW = 10;
    this.bulletH = 6;
    this.curBullet = 0;
    
    // Initialize individual bullets
    for (int i = 0; i < this.numBullets; i++) {
      this.bulletLocs[i][0] = player.x + player.w;
      this.bulletLocs[i][1] = player.y/2;
      this.bulletVisible[i] = false;
      this.bulletVelocities[i] = 20;
    }
  }
}

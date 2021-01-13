
import javax.swing.*;
import java.awt.*;

public abstract class Gun {

  String gunType;
  int shotDelay;
  int numBullets;
  int[][] bulletLocs; // {{x1, y1}, {x2, y2}, …}
  boolean[] bulletVisible;
  int[] bulletVelocities;
  Rectangle[] bulletBoxes;
  int curBullet;

  int bulletW;
  int bulletH;


  public void shoot(Player player) {
    if (this.curBullet < this.numBullets) {
      this.bulletVisible[this.curBullet] = true;
      this.bulletLocs[this.curBullet][1] = player.y + player.h/2;
      if (player.facingRight) {
        this.bulletLocs[this.curBullet][0] = player.x + player.w;
        this.bulletVelocities[this.curBullet] *= 1; // Move to the right
      }
      else {
        this.bulletLocs[this.curBullet][0] = player.x;
        this.bulletVelocities[this.curBullet] *= -1; // Move to the left 
      }
      
      this.bulletBoxes[this.curBullet] = new Rectangle(this.bulletLocs[this.curBullet][0],
                                                       this.bulletLocs[this.curBullet][1], this.bulletW, this.bulletH);
      this.curBullet++;
      
    }
    
  }

}

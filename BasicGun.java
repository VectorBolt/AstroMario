
import javax.swing.*;
import java.awt.*;

public class BasicGun extends Gun {

  public BasicGun() {
    this.gunType = "Basic Gun";
    this.shotDelay = 1;
    this.numBullets = 7;
    this.bulletLocs = new int[numBullets][numBullets]; // {{x1, y1}, {x2, y2}, …}
    this.bulletVisible = new boolean[numBullets];
    this.bulletVelocities = new int[numBullets];
    this.bulletBoxes = new Rectangle[numBullets];
    this.bulletW = 15;
    this.bulletH = 9;
    this.curBullet = 0;
  } 


}

/**
 * [Crab.java]
 * This class represents a crab enemy
 * It can only move left and right.
 * @author Avneesh Verma and Jeffrey Xu
 * @version 1.0 January 14, 2020
 */

/* Imports */
import java.awt.*;
// Image imports
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Crab extends Goomba {
  public Crab(int x, int y, int walkRange) {
    super(x, y, walkRange);
    this.health = 35;
    this.damage = 1;

    try {
      for (int i = 0; i < 2; i++) {
        this.images[i] = ImageIO.read(new File("images/crab" + i + ".png"));
      }
    } catch (Exception e) {}
  }
}
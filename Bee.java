/**
 * [Bee.java]
 * This class represents a bee enemy
 * It can only move up and down
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

public class Bee extends Goomba {
  public Bee(int x, int y, int walkRange) {
    super(x, y, walkRange);
    this.health = 15;
    this.damage = 1;
    this.movesHorizontally = false;

    try {
      for (int i = 0; i < 2; i++) {
        this.images[i] = ImageIO.read(new File("images/bee" + i + ".png"));
      }
    } catch (Exception e) {}
  }
}
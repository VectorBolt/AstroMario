/* Imports */
import java.awt.*;
// Image imports
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Flyer extends Goomba {

  public Flyer(int x, int y, int walkRange) {
    super(x, y, walkRange);
    this.health = 15;
    this.damage = 1;
    this.speed = 3;

    try {
      for (int i = 0; i < 2; i++) {
        this.images[i] = ImageIO.read(new File("images/flyer" + i + ".png"));
      };
    } catch (Exception e) {}
  }

  /**
   * move
   * This method updates the enemies coordinates as the player moves
   * @param player the instance of the player
   */
  @Override
  public void move(Player player, int screenShift) {
    // Move Enemies
    this.init_x -= player.vX - screenShift;
    this.x -= player.vX - screenShift;

    if (this.movesHorizontally) {
      this.x += this.speed;
      if (Math.abs(this.x - this.init_x) >= this.walkRange) {
        this.speed *= -1;
        if (this.walkFrame == 0) {this.walkFrame = 1;}
        else {this.walkFrame = 0;}
      }
    }
    else {
      this.y += this.speed;
      if (Math.abs(this.y - this.init_y) >= this.walkRange) {
        this.speed *= -1;
        if (this.walkFrame == 0) {this.walkFrame = 1;}
        if (this.walkFrame == 1) {this.walkFrame = 0;}
      }
    }

    this.hitbox.setLocation(this.x, this.y);
    this.collision(player);
  }
}
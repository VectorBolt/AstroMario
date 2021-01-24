/**
 * Level2.java
 * This file encodes the locations of all objects in Level 2.
 * @author Avneesh Verma
 * @version 1.0 January 24, 2020
 */


/* Imports */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Level2 extends Level {

  /* CONSTRUCTOR */
  public Level2(JFrame gameWindow) {

    this.window = gameWindow;
    this.keyListener = new MyKeyListener(this);
    this.canvas = new GraphicsPanel(this);

    // Window
    this.canvas.addKeyListener(this.keyListener);
    this.window.add(this.canvas);
    this.canvas.requestFocusInWindow();

    this.player1 = new Player(FRAME_WIDTH/4, FRAME_HEIGHT-GROUND_HEIGHT-63, 10, -28);

    // Enemy Properties
    this.goombas = new Goomba[] {
      new Goomba(1990, 278, 50),
      new Goomba(4000, 548, 200),
      new Goomba(4300, 548, 100),
      new Goomba(4320, 393, 100),
      new Goomba(4700, 548, 200),
      new Goomba(7100, 588, 125),
    };

    this.crabs = new Crab[] {
      new Crab(1742, 43, 100),
      new Crab(1802, 448, 80),
      new Crab(6350, 588, 100),
      new Crab(7300, 68, 100),
      new Crab(7825, 243, 100),
      new Crab(8075, 393, 100),
    };

    this.bees = new Bee[] {
      new Bee(850, 450, 100),
      new Bee(1396, 200, 100),
      new Bee(1550, 380, 175),
      new Bee(2700, 415, 80),
      new Bee(2900, 300, 125),
      new Bee(3500, 415, 80),
      new Bee(3700, 415, 80),
      new Bee(5550, 450, 100),
      new Bee(6550, 300, 100),
      new Bee(7025, 350, 100),
      new Bee(7075, 350, 100),
      new Bee(7125, 350, 100)
    };

    this.flyers = new Flyer[] {
      new Flyer(1100, 500, 100),
      new Flyer(1450, 300, 100),
      new Flyer(1800, 310, 70),
      new Flyer(3822, 200, 120),
      new Flyer(4400, 500, 200),
      new Flyer(5250, 500, 120),
      new Flyer(6089, 500, 100),
      new Flyer(6800, 450, 100),
      new Flyer(6800, 500, 100),
      new Flyer(6800, 550, 100)
    };

    this.enemies = new Enemy[][] {goombas, crabs, bees, flyers};

    // Platform properties
    this.platforms = new int[][] {
      // End checkpoint/flag
      {7*FRAME_WIDTH - 375, FRAME_HEIGHT - 250, 75, 150},
        // Other platforms
        {1640, 75, 245, 65},
        {1915, 310, 245, 65},
        {1700, 480, 245, 65},
        {3200, 125, 245, 65},
        {3700, 275, 245, 65},
        {4200, 425, 245, 65},
        {6200, 200, 245, 65},
        {6200, 400, 245, 65},
        {7200, 100, 245, 65},
        {7300, 450, 245, 65},
        {7725, 275, 245, 65},
        {7975, 425, 245, 65},
    };

    // Ice Platforms
    this.icePlatforms = new int[11][4];
    for (int i = 0; i < this.icePlatforms.length-2; i++) {
      this.icePlatforms[i] = new int[] {2500 + 278*i, 580, 278, 65};
    }
    this.icePlatforms[this.icePlatforms.length-2] = new int[] {5700, 400, 278, 65};
    this.icePlatforms[this.icePlatforms.length-1] = new int[] {6700, 300, 278, 65};


    // Wall Properties
    this.walls = new int[][] {
      // Start wall
      {-500, 0, 550, FRAME_HEIGHT-GROUND_HEIGHT+5},
        // End wall
        {7*FRAME_WIDTH, 0, 1000, FRAME_HEIGHT},
        // Other Walls
        {1620, 75, 100, 630},
        {2080, -500, 100, 860}
    };

    this.spikes = new int[][] {
      {500, FRAME_HEIGHT - 130, 125, 30},
        {725, FRAME_HEIGHT - 130, 125, 30},
        {950, FRAME_HEIGHT - 130, 125, 30},
        {3000, 550, 125, 30},
        {3300, 550, 125, 30},
        {3600, 550, 125, 30},
        {5010, FRAME_HEIGHT-GROUND_HEIGHT-30, 125, 30},
        {6200, 170, 125, 30},
        {5700, FRAME_HEIGHT-GROUND_HEIGHT-30, 125, 30},
        {5825, FRAME_HEIGHT-GROUND_HEIGHT-30, 125, 30},
        {5950, FRAME_HEIGHT-GROUND_HEIGHT-30, 125, 30},
        {6075, FRAME_HEIGHT-GROUND_HEIGHT-30, 125, 30},
        {6325, 170, 125, 30},
        {6450, FRAME_HEIGHT-GROUND_HEIGHT-30, 125, 30},
        {6575, FRAME_HEIGHT-GROUND_HEIGHT-30, 125, 30},
        {7300, 420, 125, 30},
        {7600, FRAME_HEIGHT-GROUND_HEIGHT-30, 125, 30},
        {7725, FRAME_HEIGHT-GROUND_HEIGHT-30, 125, 30},
        {7850, FRAME_HEIGHT-GROUND_HEIGHT-30, 125, 30},
        {7975, FRAME_HEIGHT-GROUND_HEIGHT-30, 125, 30},
        {8100, FRAME_HEIGHT-GROUND_HEIGHT-30, 125, 30},
    };

    this.water = new Rectangle[] {
      new Rectangle(1300, 75, 320, 192),
          new Rectangle(1300, 267, 320, 192),
          new Rectangle(1300, 367, 320, 192),
          new Rectangle(1300, 428, 320, 192),
          new Rectangle(7600, 0, 320, 192),
          new Rectangle(7600, 192, 320, 192),
          new Rectangle(7600, 384, 320, 192),
          new Rectangle(7600, 428, 320, 192),
          new Rectangle(7920, 0, 320, 192),
          new Rectangle(7920, 192, 320, 192),
          new Rectangle(7920, 384, 320, 192),
          new Rectangle(7920, 428, 320, 192),
          /*
             new Rectangle(1620, 428, 320, 192),
             new Rectangle(1940, 428, 320, 192),
             new Rectangle(1620, 236, 320, 192),
             new Rectangle(1940, 236, 320, 192),
             new Rectangle(1620, 44, 320, 192),
             new Rectangle(1940, 44, 320, 192),
           */
    };

    this.coins = new Rectangle[] {
      new Rectangle(1832, 560, 60, 58),
          new Rectangle(3323, 60, 60, 58),
          new Rectangle(6089, 530, 60, 58)
    };

  } // End of Constructor
} // End of Level2 class


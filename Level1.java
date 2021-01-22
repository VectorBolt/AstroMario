/**
 * [Level1.java]
 * The information for the first level of the game
 * @author Jeffrey Xu and Avneesh Verma
 * @version 1.0 January 18, 2020
 */

/* Imports */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Level1 extends Level {
  
  /* CONSTRUCTOR */
  public Level1(JFrame gameWindow) {
    
    this.window = gameWindow;
    this.keyListener = new MyKeyListener(this);
    this.canvas = new GraphicsPanel(this);
    
    // Window
    this.canvas.addKeyListener(this.keyListener);
    this.window.add(this.canvas);
    
    this.player1 = new Player(FRAME_WIDTH/4, FRAME_HEIGHT-GROUND_HEIGHT-63, 10, -30);
    
    // Enemy Properties
    this.goombas = new Goomba[28];
    // Initialize Goombas
    this.goombas[0] = new Goomba(530, FRAME_HEIGHT-GROUND_HEIGHT-172, 100);
    this.goombas[1] = new Goomba(3*FRAME_WIDTH/4 + 75, FRAME_HEIGHT - GROUND_HEIGHT - 282, 100);
    for (int g = 2; g < 15; g++) {
        this.goombas[g] = new Goomba(5*FRAME_WIDTH - 800 + 100*g, FRAME_HEIGHT - GROUND_HEIGHT - 32, 200);
    }
    for (int k = 0; k < 13; k++) {
      this.goombas[k + 15] = new Goomba(5*FRAME_WIDTH - 550 + 100*k, FRAME_HEIGHT - GROUND_HEIGHT - 32, 300);
    }
    
    this.crabs = new Crab[] {
      new Crab(530, FRAME_HEIGHT-GROUND_HEIGHT-32, 100),
      new Crab(830, FRAME_HEIGHT-GROUND_HEIGHT-32, 100),
      new Crab(2*FRAME_WIDTH - 325, FRAME_HEIGHT - GROUND_HEIGHT - 172, 100),
      new Crab(4*FRAME_WIDTH + 510, FRAME_HEIGHT - GROUND_HEIGHT - 185, 100),
      new Crab(5*FRAME_WIDTH - 240, FRAME_HEIGHT - GROUND_HEIGHT - 282, 100),
      new Crab(5*FRAME_WIDTH + 300, FRAME_HEIGHT - GROUND_HEIGHT - 387, 100),
      new Crab(6*FRAME_WIDTH + 173, FRAME_HEIGHT - GROUND_HEIGHT - 257, 110)
    };

    this.bees = new Bee[] {
      new Bee(800, 400, 100),
      new Bee(1200, 250, 250),
      new Bee(3435, 435, 100),
      new Bee(3600, 321, 200),
      new Bee(3795, 279, 180),
      new Bee(4*FRAME_WIDTH + 760, FRAME_HEIGHT - GROUND_HEIGHT - 200, 100),
      new Bee(5*FRAME_WIDTH + 20, FRAME_HEIGHT - GROUND_HEIGHT - 300, 100),
      new Bee(5*FRAME_WIDTH + 560, FRAME_HEIGHT - GROUND_HEIGHT - 400, 100)
    };

    this.flyers = new Flyer[19];
    // Initalize Flyers
    this.flyers[0] = new Flyer(1000, 250, 100);
    this.flyers[1] = new Flyer(1600, 350, 250);
    this.flyers[2] = new Flyer(3500, 505, 305);
    this.flyers[3] = new Flyer(3650, 410, 555);
    this.flyers[4] = new Flyer(3700, 230, 430);
    this.flyers[5] = new Flyer(3575, 305, 510);
    for (int f = 0; f < this.flyers.length - 6; f++) {
      this.flyers[f + 6] = new Flyer(5*FRAME_WIDTH - 600 + 100*f, FRAME_HEIGHT - GROUND_HEIGHT - 70 - 10*f, 200);
    }

    this.enemies = new Enemy[][] {goombas, crabs, bees, flyers};
  
    // Platform properties
    this.platforms = new int[][] {
      // End checkpoint/flag
      {7*FRAME_WIDTH - 375, FRAME_HEIGHT - 250, 75, 150},
      // Other platforms
      {2*FRAME_WIDTH - 430, FRAME_HEIGHT - GROUND_HEIGHT - 140, 245, 65},
      {FRAME_WIDTH/3, FRAME_HEIGHT - GROUND_HEIGHT - 140, 245, 65},
      {3*FRAME_WIDTH/4 - 50, FRAME_HEIGHT - GROUND_HEIGHT - 250, 245, 65},
      {FRAME_WIDTH, FRAME_HEIGHT - GROUND_HEIGHT - 160, 245, 65},
      {4*FRAME_WIDTH + 400, FRAME_HEIGHT - GROUND_HEIGHT - 155, 245, 60},
      {5*FRAME_WIDTH - 350, FRAME_HEIGHT - GROUND_HEIGHT - 250, 245, 60},
      {5*FRAME_WIDTH + 190, FRAME_HEIGHT - GROUND_HEIGHT - 355, 245, 60}
    };

    this.icePlatforms = new int[][] {
      {6*FRAME_WIDTH - 300, FRAME_HEIGHT - GROUND_HEIGHT - 150, 278, 65},
      {6*FRAME_WIDTH + 55, FRAME_HEIGHT - GROUND_HEIGHT - 225, 278, 65},
      {6*FRAME_WIDTH + 374, FRAME_HEIGHT - GROUND_HEIGHT - 323, 278, 65},
      {6*FRAME_WIDTH + 717, FRAME_HEIGHT - GROUND_HEIGHT - 423, 278, 65}
    };
    
    // Wall Properties
    this.walls = new int[][] {
      // Start wall
      {-500, 0, 550, FRAME_HEIGHT},
        // End wall
      {7*FRAME_WIDTH, 0, 1000, FRAME_HEIGHT}
    };
    
    this.spikes = new int[32][4];
    // Initalize spikes
    this.spikes[0][0] = 2*FRAME_WIDTH - 425;
    this.spikes[1][0] = 2*FRAME_WIDTH - 300;
    this.spikes[2][0] = 2*FRAME_WIDTH;
    for (int s = 0; s < this.spikes.length; s++) {
      if (s >= 3 && s <= 22) {
        this.spikes[s][0] = 2*FRAME_WIDTH + 300 + 125*(s-3);
      }
      else if (s >= 23) {
        this.spikes[s][0] = 6*FRAME_WIDTH - 300 + 125*(s-23);
      }
      this.spikes[s][1] = FRAME_HEIGHT - GROUND_HEIGHT - 30;
      this.spikes[s][2] = 125;
      this.spikes[s][3] = 30;
    }

    this.water = new Rectangle[] {
      new Rectangle(3000, FRAME_HEIGHT - GROUND_HEIGHT - 192, 320, 192),
      new Rectangle(3220, FRAME_HEIGHT - GROUND_HEIGHT - 192, 320, 192),
      new Rectangle(3220, FRAME_HEIGHT - GROUND_HEIGHT - 310, 320, 192),
      new Rectangle(3440, FRAME_HEIGHT - GROUND_HEIGHT - 192, 320, 192),
      new Rectangle(3440, FRAME_HEIGHT - GROUND_HEIGHT - 310, 320, 192),
      new Rectangle(3440, FRAME_HEIGHT - GROUND_HEIGHT - 450, 320, 192),
      new Rectangle(3660, FRAME_HEIGHT - GROUND_HEIGHT - 192, 320, 192),
      new Rectangle(3660, FRAME_HEIGHT - GROUND_HEIGHT - 310, 320, 192),
      new Rectangle(3660, FRAME_HEIGHT - GROUND_HEIGHT - 450, 320, 192),
      new Rectangle(3660, FRAME_HEIGHT - GROUND_HEIGHT - 600, 320, 192),
      new Rectangle(4400, FRAME_HEIGHT - GROUND_HEIGHT - 350, 320, 192),
      new Rectangle(4675, FRAME_HEIGHT - GROUND_HEIGHT - 350, 320, 192),
    };
  }
}
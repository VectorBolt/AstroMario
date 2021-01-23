/**
 * [Level3.java]
 * The information for the third level of the game
 * @author Jeffrey Xu and Avneesh Verma
 * @version 1.0 January 22, 2021
 */

/* Imports */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Level3 extends Level {
  
  /* CONSTRUCTOR */
  public Level3(JFrame gameWindow) {
    
    this.window = gameWindow;
    this.keyListener = new MyKeyListener(this);
    this.canvas = new GraphicsPanel(this);
    
    // Window
    this.canvas.addKeyListener(this.keyListener);
    this.window.add(this.canvas);
    this.canvas.requestFocusInWindow();
    
    this.player1 = new Player(FRAME_WIDTH/4, FRAME_HEIGHT-GROUND_HEIGHT-63, 10, -30);
    
    // Enemy Properties
    this.goombas = new Goomba[] {
      new Goomba(FRAME_WIDTH - 530, FRAME_HEIGHT - GROUND_HEIGHT - 32, 110),
    };
    
    this.crabs = new Crab[] {
      new Crab(FRAME_WIDTH - 530, FRAME_HEIGHT - GROUND_HEIGHT - 182, 110),
    };

    this.bees = new Bee[] {
      new Bee(500, 475, 60),
      new Bee(580, 460, 90),
    };

    this.flyers = new Flyer[] {
      new Flyer(740, 350, 100),
    };
    
    this.enemies = new Enemy[][] {goombas, crabs, bees, flyers};
  
    // Platform properties
    this.platforms = new int[][] {
      // End checkpoint/flag
      {7*FRAME_WIDTH - 375, FRAME_HEIGHT - 250, 75, 150}
    };

    this.icePlatforms = new int[][] {
      {FRAME_WIDTH - 650, FRAME_HEIGHT - GROUND_HEIGHT - 150, 278, 65},
    };
    
    // Wall Properties
    this.walls = new int[][] {
      // Start wall
      {-500, 0, 550, FRAME_HEIGHT - GROUND_HEIGHT + 5},
      // End wall
      {7*FRAME_WIDTH, 0, 1000, FRAME_HEIGHT - GROUND_HEIGHT + 5}
    };
    
    this.spikes = new int[1][4];
    // Initalize spikes
    this.spikes[0][0] = 2*FRAME_WIDTH - 425;
    for (int s = 0; s < this.spikes.length; s++) {
      this.spikes[s][1] = FRAME_HEIGHT - GROUND_HEIGHT - 30;
      this.spikes[s][2] = 125;
      this.spikes[s][3] = 30;
    }

    this.water = new Rectangle[] {
      new Rectangle(3000, FRAME_HEIGHT - GROUND_HEIGHT - 192, 320, 192)
    };
  }
}
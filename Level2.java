

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
    
    this.player1 = new Player(FRAME_WIDTH/4, FRAME_HEIGHT-GROUND_HEIGHT-63, 10, -30);
    
    // Enemy Properties
    this.goombas = new Goomba[] {
      new Goomba(3*FRAME_WIDTH/4, FRAME_HEIGHT-GROUND_HEIGHT-32, 100),
      new Goomba(FRAME_WIDTH/2 + 150, FRAME_HEIGHT - GROUND_HEIGHT - 232, 100)
    };

    this.crabs = new Crab[] {
      new Crab(500, FRAME_HEIGHT-GROUND_HEIGHT-32, 100),
      new Crab(800, FRAME_HEIGHT-GROUND_HEIGHT-32, 100)
    };

    this.bees = new Bee[] {
      new Bee(900, 400, 100),
      new Bee(1200, 500, 250)
    };

    this.flyers = new Flyer[] {
      new Flyer(1000, 200, 100),
      new Flyer(1600, 300, 250)
    };

    this.enemies = new Enemy[][] {goombas, crabs, bees, flyers};

    // Platform properties
    this.platforms = new int[][] {
        // End checkpoint/flag
      {7*FRAME_WIDTH - 375, FRAME_HEIGHT - 250, 75, 150},
        // Other platforms
      {FRAME_WIDTH/3, FRAME_HEIGHT - GROUND_HEIGHT - 100, 245, 65},
      {3*FRAME_WIDTH/4, FRAME_HEIGHT - GROUND_HEIGHT - 250, 245, 65},
      {FRAME_WIDTH, FRAME_HEIGHT - GROUND_HEIGHT - 160, 245, 65}
    };
    
    // Wall Properties
    this.walls = new int[][] {
      // Start wall
      {-500, 0, 550, FRAME_HEIGHT},
        // End wall
      {7*FRAME_WIDTH, 0, 1000, FRAME_HEIGHT}
    };

    this.spikes = new int[][] {
      {500, FRAME_HEIGHT - 130, 125, 30},
      {675, FRAME_HEIGHT - 130, 125, 30},
      {850, FRAME_HEIGHT - 130, 125, 30},
      {1025, FRAME_HEIGHT - 130, 125, 30},
    };

    this.water = new Rectangle[] {
      new Rectangle(1100, 100, 320, 192),
      new Rectangle(1420, 100, 320, 192)
    };


  }


}


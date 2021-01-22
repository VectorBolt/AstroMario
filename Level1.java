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
    this.goombas = new Goomba[] {
      new Goomba(530, FRAME_HEIGHT-GROUND_HEIGHT-172, 100),
      new Goomba(3*FRAME_WIDTH/4 + 75, FRAME_HEIGHT - GROUND_HEIGHT - 282, 100),
      new Goomba(5*FRAME_WIDTH - 600, FRAME_HEIGHT - GROUND_HEIGHT - 32, 200),
      new Goomba(5*FRAME_WIDTH - 500, FRAME_HEIGHT - GROUND_HEIGHT - 32, 200),
      new Goomba(5*FRAME_WIDTH - 400, FRAME_HEIGHT - GROUND_HEIGHT - 32, 200),
      new Goomba(5*FRAME_WIDTH - 300, FRAME_HEIGHT - GROUND_HEIGHT - 32, 200),
      new Goomba(5*FRAME_WIDTH - 200, FRAME_HEIGHT - GROUND_HEIGHT - 32, 200),
      new Goomba(5*FRAME_WIDTH - 100, FRAME_HEIGHT - GROUND_HEIGHT - 32, 200),
      new Goomba(5*FRAME_WIDTH, FRAME_HEIGHT - GROUND_HEIGHT - 32, 200),
      new Goomba(5*FRAME_WIDTH + 100, FRAME_HEIGHT - GROUND_HEIGHT - 32, 200),  
      new Goomba(5*FRAME_WIDTH + 200, FRAME_HEIGHT - GROUND_HEIGHT - 32, 200),
      new Goomba(5*FRAME_WIDTH + 300, FRAME_HEIGHT - GROUND_HEIGHT - 32, 200),
      new Goomba(5*FRAME_WIDTH + 400, FRAME_HEIGHT - GROUND_HEIGHT - 32, 200),
      new Goomba(5*FRAME_WIDTH + 500, FRAME_HEIGHT - GROUND_HEIGHT - 32, 200),
      new Goomba(5*FRAME_WIDTH + 600, FRAME_HEIGHT - GROUND_HEIGHT - 32, 200),
      new Goomba(5*FRAME_WIDTH + 700, FRAME_HEIGHT - GROUND_HEIGHT - 32, 200),
      new Goomba(5*FRAME_WIDTH + 800, FRAME_HEIGHT - GROUND_HEIGHT - 32, 200),
      new Goomba(5*FRAME_WIDTH - 550, FRAME_HEIGHT - GROUND_HEIGHT - 32, 300),
      new Goomba(5*FRAME_WIDTH - 450, FRAME_HEIGHT - GROUND_HEIGHT - 32, 300),
      new Goomba(5*FRAME_WIDTH - 350, FRAME_HEIGHT - GROUND_HEIGHT - 32, 300),
      new Goomba(5*FRAME_WIDTH - 250, FRAME_HEIGHT - GROUND_HEIGHT - 32, 300),
      new Goomba(5*FRAME_WIDTH - 150, FRAME_HEIGHT - GROUND_HEIGHT - 32, 300),
      new Goomba(5*FRAME_WIDTH - 50, FRAME_HEIGHT - GROUND_HEIGHT - 32, 300),
      new Goomba(5*FRAME_WIDTH + 50, FRAME_HEIGHT - GROUND_HEIGHT - 32, 300),
      new Goomba(5*FRAME_WIDTH + 150, FRAME_HEIGHT - GROUND_HEIGHT - 32, 300),
      new Goomba(5*FRAME_WIDTH + 250, FRAME_HEIGHT - GROUND_HEIGHT - 32, 300),
      new Goomba(5*FRAME_WIDTH + 350, FRAME_HEIGHT - GROUND_HEIGHT - 32, 300),
      new Goomba(5*FRAME_WIDTH + 450, FRAME_HEIGHT - GROUND_HEIGHT - 32, 300),
      new Goomba(5*FRAME_WIDTH + 550, FRAME_HEIGHT - GROUND_HEIGHT - 32, 300),
      new Goomba(5*FRAME_WIDTH + 650, FRAME_HEIGHT - GROUND_HEIGHT - 32, 300),
      new Goomba(5*FRAME_WIDTH + 750, FRAME_HEIGHT - GROUND_HEIGHT - 32, 300),
      new Goomba(5*FRAME_WIDTH + 850, FRAME_HEIGHT - GROUND_HEIGHT - 32, 300)
    };

    this.crabs = new Crab[] {
      new Crab(530, FRAME_HEIGHT-GROUND_HEIGHT-32, 100),
      new Crab(830, FRAME_HEIGHT-GROUND_HEIGHT-32, 100),
      new Crab(2*FRAME_WIDTH - 325, FRAME_HEIGHT - GROUND_HEIGHT - 172, 100),
      new Crab(4*FRAME_WIDTH + 510, FRAME_HEIGHT - GROUND_HEIGHT - 185, 100),
      new Crab(5*FRAME_WIDTH - 240, FRAME_HEIGHT - GROUND_HEIGHT - 282, 100),
      new Crab(5*FRAME_WIDTH + 300, FRAME_HEIGHT - GROUND_HEIGHT - 387, 100)
    };

    this.bees = new Bee[] {
      new Bee(800, 400, 100),
      new Bee(1200, 250, 250),
      new Bee(3600, 250, 200),
      new Bee(4*FRAME_WIDTH + 760, FRAME_HEIGHT - GROUND_HEIGHT - 200, 100),
      new Bee(5*FRAME_WIDTH + 20, FRAME_HEIGHT - GROUND_HEIGHT - 300, 100),
      new Bee(5*FRAME_WIDTH + 560, FRAME_HEIGHT - GROUND_HEIGHT - 400, 100)
    };

    this.flyers = new Flyer[] {
      new Flyer(1000, 250, 100),
      new Flyer(1600, 350, 250),
      new Flyer(3500, 425, 700),
      new Flyer(3700, 200, 400),
      new Flyer(5*FRAME_WIDTH - 600, FRAME_HEIGHT - GROUND_HEIGHT - 90, 200),
      new Flyer(5*FRAME_WIDTH - 500, FRAME_HEIGHT - GROUND_HEIGHT - 95, 200),
      new Flyer(5*FRAME_WIDTH - 400, FRAME_HEIGHT - GROUND_HEIGHT - 100, 200),
      new Flyer(5*FRAME_WIDTH - 300, FRAME_HEIGHT - GROUND_HEIGHT - 105, 200),
      new Flyer(5*FRAME_WIDTH - 200, FRAME_HEIGHT - GROUND_HEIGHT - 110, 200),
      new Flyer(5*FRAME_WIDTH - 100, FRAME_HEIGHT - GROUND_HEIGHT - 115, 200),
      new Flyer(5*FRAME_WIDTH, FRAME_HEIGHT - GROUND_HEIGHT - 120, 200),
      new Flyer(5*FRAME_WIDTH + 100, FRAME_HEIGHT - GROUND_HEIGHT - 125, 200),  
      new Flyer(5*FRAME_WIDTH + 200, FRAME_HEIGHT - GROUND_HEIGHT - 130, 200),
      new Flyer(5*FRAME_WIDTH + 300, FRAME_HEIGHT - GROUND_HEIGHT - 135, 200),
      new Flyer(5*FRAME_WIDTH + 400, FRAME_HEIGHT - GROUND_HEIGHT - 140, 200),
      new Flyer(5*FRAME_WIDTH + 500, FRAME_HEIGHT - GROUND_HEIGHT - 145, 200),
      new Flyer(5*FRAME_WIDTH + 600, FRAME_HEIGHT - GROUND_HEIGHT - 150, 200),
      new Flyer(5*FRAME_WIDTH + 700, FRAME_HEIGHT - GROUND_HEIGHT - 155, 200),
      new Flyer(5*FRAME_WIDTH + 800, FRAME_HEIGHT - GROUND_HEIGHT - 160, 200)
    };

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

    this.icePlatforms = new int[][] {};

    // Wall Properties
    this.walls = new int[][] {
      // Start wall
      {-500, 0, 550, FRAME_HEIGHT},
        // End wall
      {7*FRAME_WIDTH, 0, 1000, FRAME_HEIGHT}
    };
    
    this.spikes = new int[][] {
      {2*FRAME_WIDTH - 425, FRAME_HEIGHT - GROUND_HEIGHT - 30, 125, 30},
      {2*FRAME_WIDTH - 300, FRAME_HEIGHT - GROUND_HEIGHT - 30, 125, 30},
      {2*FRAME_WIDTH, FRAME_HEIGHT - GROUND_HEIGHT - 30, 125, 30},
      {2*FRAME_WIDTH + 300, FRAME_HEIGHT - GROUND_HEIGHT - 30, 125, 30},
      {2*FRAME_WIDTH + 425, FRAME_HEIGHT - GROUND_HEIGHT - 30, 125, 30},
      {2*FRAME_WIDTH + 550, FRAME_HEIGHT - GROUND_HEIGHT - 30, 125, 30}, 
      {2*FRAME_WIDTH + 675, FRAME_HEIGHT - GROUND_HEIGHT - 30, 125, 30},
      {2*FRAME_WIDTH + 800, FRAME_HEIGHT - GROUND_HEIGHT - 30, 125, 30}, 
      {2*FRAME_WIDTH + 925, FRAME_HEIGHT - GROUND_HEIGHT - 30, 125, 30},
      {2*FRAME_WIDTH + 1050, FRAME_HEIGHT - GROUND_HEIGHT - 30, 125, 30}, 
      {2*FRAME_WIDTH + 1175, FRAME_HEIGHT - GROUND_HEIGHT - 30, 125, 30}, 
      {2*FRAME_WIDTH + 1300, FRAME_HEIGHT - GROUND_HEIGHT - 30, 125, 30}, 
      {2*FRAME_WIDTH + 1425, FRAME_HEIGHT - GROUND_HEIGHT - 30, 125, 30},
      {2*FRAME_WIDTH + 1550, FRAME_HEIGHT - GROUND_HEIGHT - 30, 125, 30}, 
      {2*FRAME_WIDTH + 1675, FRAME_HEIGHT - GROUND_HEIGHT - 30, 125, 30},
      {2*FRAME_WIDTH + 1800, FRAME_HEIGHT - GROUND_HEIGHT - 30, 125, 30},
      {2*FRAME_WIDTH + 1925, FRAME_HEIGHT - GROUND_HEIGHT - 30, 125, 30}, 
      {2*FRAME_WIDTH + 2050, FRAME_HEIGHT - GROUND_HEIGHT - 30, 125, 30},
      {2*FRAME_WIDTH + 2175, FRAME_HEIGHT - GROUND_HEIGHT - 30, 125, 30},
      {2*FRAME_WIDTH + 2300, FRAME_HEIGHT - GROUND_HEIGHT - 30, 125, 30},
      {2*FRAME_WIDTH + 2425, FRAME_HEIGHT - GROUND_HEIGHT - 30, 125, 30},
      {2*FRAME_WIDTH + 2550, FRAME_HEIGHT - GROUND_HEIGHT - 30, 125, 30}
    };

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

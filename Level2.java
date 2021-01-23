
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

     this.player1 = new Player(FRAME_WIDTH/4, FRAME_HEIGHT-GROUND_HEIGHT-63, 10, -28);

     // Enemy Properties
     this.goombas = new Goomba[] {
       new Goomba(2022, 278, 100)
     };

     this.crabs = new Crab[] {
       new Crab(1732, 43, 100),
       new Crab(1832, 448, 100)
     };

     this.bees = new Bee[] {
       new Bee(850, 450, 100)
     };

     this.flyers = new Flyer[] {
       new Flyer(1100, 500, 100),
       new Flyer(1800, 310, 75)
     };

     this.enemies = new Enemy[][] {goombas, crabs, bees, flyers};

     // Platform properties
     this.platforms = new int[][] {
         // End checkpoint/flag
       {7*FRAME_WIDTH - 375, FRAME_HEIGHT - 250, 75, 150},
         // Other platforms
       {1630, 75, 245, 65},
       {1915, 310, 245, 65},
       {1730, 480, 245, 65},
     };

     this.icePlatforms = new int[][] {
       {2500, 350, 278, 65},
       {3000, 350, 278, 65},
     };


     // Wall Properties
     this.walls = new int[][] {
       // Start wall
       {-500, 0, 550, FRAME_HEIGHT},
         // End wall
       {7*FRAME_WIDTH, 0, 1000, FRAME_HEIGHT},
       // Other Walls
       {1620, 140, 100, 480},
       {2160, 0, 100, 480}
     };

     this.spikes = new int[][] {
       {500, FRAME_HEIGHT - 130, 125, 30},
       {700, FRAME_HEIGHT - 130, 125, 30},
       {900, FRAME_HEIGHT - 130, 125, 30},
       {1100, FRAME_HEIGHT - 130, 125, 30},
     };

     this.water = new Rectangle[] {
       new Rectangle(1400, 428, 320, 192),
       new Rectangle(1620, 428, 320, 192),
       new Rectangle(1940, 428, 320, 192),
       new Rectangle(1400, 236, 320, 192),
       new Rectangle(1620, 236, 320, 192),
       new Rectangle(1940, 236, 320, 192),
       new Rectangle(1400, 44, 320, 192),
       new Rectangle(1620, 44, 320, 192),
       new Rectangle(1940, 44, 320, 192),
     };

     this.coins = new Rectangle[] {
       new Rectangle(1832, 560, 60, 58),
       new Rectangle(300, 300, 60, 58),
       new Rectangle(200, 200, 60, 58)
     };


   }


 }


import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Random;
import javax.swing.*;

public class MazeMania extends JPanel implements ActionListener,KeyListener{
       class Block {
              int x;
              int y;
              int width;
              int height;
              Image image;

              int startX;
              int startY;
              char d='U';
              int velX=0;
              int velY=0;
              
              Block(Image image,int x,int y,int width,int height) {
                     this.image=image;
                     this.x=x;
                     this.y=y;
                     this.width=width;
                     this.height=height;
                     this.startX=x;
                     this.startY=y;
              }
              void updateDirection(char d) {
                     char prevD=this.d;
                     this.d=d;
                     updateVel();
                     this.x+=this.velX;
                     this.y+=this.velY;
                     for(Block wall:walls) {
                            if(collision(this,wall)) {
                                   this.x-=this.velX;
                                   this.y-=this.velY;
                                   this.d=prevD;
                                   updateVel();
                            }
                     }
              }
              void updateVel() {
                     if(this.d=='U') {
                            this.velX=0;
                            this.velY=-tileSize/4;
                     }
                     else if(this.d=='D') {
                            this.velX=0;
                            this.velY=tileSize/4;
                     }
                     else if(this.d=='L') {
                            this.velX=-tileSize/4;
                            this.velY=0;
                     }
                     else if(this.d=='R') {
                            this.velX=tileSize/4;
                            this.velY=0;
                     }

              }
       }

       private int rowCnt=21;
       private int columnCnt=19;
       private int tileSize=32;
       private int boardHeight=rowCnt*tileSize;
       private int boardWidth=columnCnt*tileSize;

       private Image wallImage;
       private Image cyanGhost;
       private Image orangeGhost;
       private Image pinkGhost;
       //private Image purpleGhost;
       private Image redGhost;

       private Image pacmanUp;
       private Image pacmanDown;
       private Image pacmanLeft;
       private Image pacmanRight;

       private Image foodImage;

     //X=wall,O=skip,P=pacman,' '=food
    //Ghosts:c=cyan,o=orange,p=pink,r=red
    private String[] tileMap = {
       "XXXXXXXXXXXXXXXXXXX",
       "X        X        X",
       "X XX XXX X XXX XX X",
       "X                 X",
       "X XX X XXXXX X XX X",
       "X    X       X    X",
       "XXXX XXXX XXXX XXXX",
       "OOOX X       X XOOO",
       "XXXX X XXrXX X XXXX",
       "O       cpo       O",
       "XXXX X XXXXX X XXXX",
       "OOOX X       X XOOO",
       "XXXX X XXXXX X XXXX",
       "X        X        X",
       "X XX XXX X XXX XX X",
       "X  X     P     X  X",
       "XX X X XXXXX X X XX",
       "X    X   X   X    X",
       "X XXXXXX X XXXXXX X",
       "X                 X",
       "XXXXXXXXXXXXXXXXXXX" 
   };

       HashSet<Block> walls;
       HashSet<Block> foods;
       HashSet<Block> ghosts;
       Block pacman;

       Timer gameLoop;

       MazeMania() {
        setPreferredSize(new Dimension(boardWidth,boardHeight));
        setBackground(Color.darkGray);
        addKeyListener(this);
        setFocusable(true);

        wallImage=new ImageIcon(getClass().getResource("./wall.png")).getImage();
        cyanGhost=new ImageIcon(getClass().getResource("./cyan ghost.png")).getImage();
        orangeGhost=new ImageIcon(getClass().getResource("./orange ghost.png")).getImage();
        pinkGhost=new ImageIcon(getClass().getResource("./pink ghost.png")).getImage();
        //purpleGhost=new ImageIcon(getClass().getResource("./purple ghost.png")).getImage();
        redGhost=new ImageIcon(getClass().getResource("./red ghost.png")).getImage();

        pacmanUp=new ImageIcon(getClass().getResource("./pacman up.png")).getImage();
        pacmanDown=new ImageIcon(getClass().getResource("./pacman down.png")).getImage();
        pacmanLeft=new ImageIcon(getClass().getResource("./pacman left.png")).getImage();
        pacmanRight=new ImageIcon(getClass().getResource("./pacman right.png")).getImage();

        foodImage=new ImageIcon(getClass().getResource("./orange.png")).getImage();

        loadMap();

        gameLoop=new Timer(50,this);
        gameLoop.start();
       }

       public void loadMap() {
              walls=new HashSet<Block>();
              foods=new HashSet<Block>();
              ghosts=new HashSet<Block>();

              for(int i=0;i<rowCnt;i++) {
                     for(int j=0;j<columnCnt;j++) {
                            String row=tileMap[i];
                            char tileMapChar=row.charAt(j);

                            int x=j*tileSize;
                            int y=i*tileSize;

                            if(tileMapChar=='X') {
                                   Block wall=new Block(wallImage,x,y,tileSize,tileSize);
                                   walls.add(wall);
                            }
                            else if(tileMapChar=='c') {
                                   Block ghost=new Block(cyanGhost,x,y,tileSize,tileSize);
                                   ghosts.add(ghost);
                            }
                            else if(tileMapChar=='p') {
                                   Block ghost=new Block(pinkGhost,x,y,tileSize,tileSize);
                                   ghosts.add(ghost);
                            }
                            else if(tileMapChar=='o') {
                                   Block ghost=new Block(orangeGhost,x,y,tileSize,tileSize);
                                   ghosts.add(ghost);
                            }
                            else if(tileMapChar=='r') {
                                   Block ghost=new Block(redGhost,x,y,tileSize,tileSize);
                                   ghosts.add(ghost);
                            }
                            else if(tileMapChar=='P') {
                                   pacman=new Block(pacmanRight,x,y,tileSize,tileSize);
                            }
                            else if(tileMapChar==' ') {
                                   Block food=new Block(foodImage,x+8,y+8,16,16);
                                   foods.add(food);
                            }
                     }
              }
       }
       public void paintComponent(Graphics g) {
              super.paintComponent(g);
              draw(g);
       }
       public void draw(Graphics g) {
             g.drawImage(pacman.image,pacman.x,pacman.y,pacman.width,pacman.height,null);

              for(Block ghost:ghosts) {
              g.drawImage(ghost.image,ghost.x,ghost.y,ghost.width,ghost.height,null);
              }
              for(Block wall:walls) {
                     g.drawImage(wall.image,wall.x,wall.y,wall.width,wall.height,null);
              }
              for(Block food:foods) {
                     g.drawImage(food.image,food.x,food.y,food.width,food.height,null);
              }
       }

       public void move() {
              pacman.x+=pacman.velX;
              pacman.y+=pacman.velY;

              for(Block wall:walls) {
                     if(collision(pacman,wall)) {
                            pacman.x-=pacman.velX;
                            pacman.y-=pacman.velY;
                            break;
                     }
              }
       }      
       
       public boolean collision(Block a,Block b) {
              return a.x<b.x+b.width && a.x+a.width>b.x 
              && a.y<b.y+b.height && a.y+a.height>b.y;
       }

       @Override
       public void actionPerformed(ActionEvent e) {
              move();
              repaint();
       }

       @Override
       public void keyTyped(KeyEvent e) {
       }

       @Override
       public void keyPressed(KeyEvent e) {
      }

       @Override
       public void keyReleased(KeyEvent e) {
       if(e.getKeyCode()==KeyEvent.VK_UP) {
          pacman.updateDirection('U');
       }
       else if(e.getKeyCode()==KeyEvent.VK_DOWN) {
          pacman.updateDirection('D');
       }
       else if(e.getKeyCode()==KeyEvent.VK_LEFT) {
          pacman.updateDirection('L');
       }
       else if(e.getKeyCode()==KeyEvent.VK_RIGHT) {
          pacman.updateDirection('R');
       }
       if(pacman.d=='U') {
          pacman.image=pacmanUp;
       } 
       else if(pacman.d=='D') {
         pacman.image=pacmanDown;
       } 
       else if(pacman.d=='L') {
          pacman.image=pacmanLeft;
       } 
       else if(pacman.d=='R') {
          pacman.image=pacmanRight;
       }
 }
}
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
              void reset() {
                     this.x=this.startX;
                     this.y=this.startY;
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
       char[] direction={'U','D','L','R'};
       Random rando=new Random();
       int score=0,lives=3;
       boolean gameOver=false;

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
        for(Block ghost:ghosts) {
              char newD=direction[rando.nextInt(4)];
              ghost.updateDirection(newD);
        }
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
              g.setFont(new Font("Arial",Font.PLAIN,18));
              if(gameOver) {
                     g.drawString("Game Over! Score : "+String.valueOf(score),tileSize/2,tileSize/2);
              } else{
                     g.drawString("x"+String.valueOf(lives)+" Score : "+String.valueOf(score),tileSize/2,tileSize/2);
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

              for(Block ghost:ghosts) {
                     if(collision(ghost,pacman)) {
                            lives--;
                            if(lives==0) {
                               gameOver=true;
                               return;
                            }
                            resetPosition();
                     }
                     if(ghost.y==tileSize*9 && ghost.d!='U' && ghost.d!='D') {
                            ghost.updateDirection('U');
                     }
                     ghost.x+=ghost.velX;
                     ghost.y+=ghost.velY;

                     for(Block wall:walls) {
                            if(collision(ghost,wall) || ghost.x<=0 || ghost.x+ghost.width>=boardWidth) {
                                   ghost.x-=ghost.velX;
                                   ghost.y-=ghost.velY;
                                   char newD=direction[rando.nextInt(4)];
                                   ghost.updateDirection(newD);
                            }
                     }
              }
              Block foodEaten=null;
              for(Block food:foods) {
                     if(collision(pacman,food)) {
                     foodEaten=food;
                     score+=5;
                  }
              }
              foods.remove(foodEaten);

              if(foods.isEmpty()) {
                     resetPosition();
              }
       }      
       
       public boolean collision(Block a,Block b) {
              return a.x<b.x+b.width && a.x+a.width>b.x 
              && a.y<b.y+b.height && a.y+a.height>b.y;
       }
       
       public void resetPosition() {
              pacman.reset();
              pacman.velX=0;
              pacman.velY=0;
              for(Block ghost:ghosts) {
                     ghost.reset();
                     char newD=direction[rando.nextInt(4)];
                     ghost.updateDirection(newD);
              }
       }

       @Override
       public void actionPerformed(ActionEvent e) {
              move();
              repaint();
              if(gameOver) {
                     gameLoop.stop();
              }
       }

       @Override
       public void keyTyped(KeyEvent e) {
       }

       @Override
       public void keyPressed(KeyEvent e) {
      }

       @Override
       public void keyReleased(KeyEvent e) {

       if(gameOver) {
              loadMap();
              resetPosition();
              lives=3;
              score=0;
              gameOver=false;
              gameLoop.start();
       }

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
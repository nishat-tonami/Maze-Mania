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
                            this.velY=-speed;
                     }
                     else if(this.d=='D') {
                            this.velX=0;
                            this.velY=speed;
                     }
                     else if(this.d=='L') {
                            this.velX=-speed;
                            this.velY=0;
                     }
                     else if(this.d=='R') {
                            this.velX=speed;
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
       private int tileSize=36;
       private int speed=tileSize/6;
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
       private Image heartImage;

       private boolean showHomeScreen=true;
       private Image homeScreenImage;

       private boolean showEndScreen=false;
       private Image endScreenImage;

       // here 0=empty space/food(cause in the empty space there would be food)
       //1=wall,2=pacman,3=cyan ghost,4=red ghost,5=orange ghost,6=pink ghost
       //7=empty space(no food,inside wall)

       private int[][] tileMap=new int[][] {
       {1,1,1,1,0,0,1,1,1,1,1,1,1,0,0,1,1,1,1},
       {1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1},
       {1,1,0,1,1,0,1,1,1,0,1,0,1,1,1,0,1,1,1},
       {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
       {1,1,0,1,1,1,1,1,1,1,0,1,1,1,1,0,1,1,1},
       {0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0},
       {1,1,1,1,0,1,1,1,1,1,1,1,1,1,0,1,1,1,1},
       {7,7,7,1,0,0,0,0,0,0,0,0,0,0,0,1,7,7,7},
       {1,1,1,1,0,1,0,1,1,4,1,1,0,1,0,1,1,1,1},
       {0,0,0,0,0,0,0,0,6,5,3,0,0,0,0,0,0,0,0},
       {1,1,1,1,0,1,0,1,1,1,1,1,0,1,0,1,1,1,1},
       {7,7,7,1,0,0,0,0,0,1,0,0,0,0,0,1,7,7,7},
       {1,1,1,1,0,1,0,1,1,1,1,1,0,1,0,1,1,1,1},
       {1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1},
       {1,1,0,1,1,0,1,1,1,0,1,0,1,1,1,0,1,1,1},
       {1,0,0,1,0,0,0,0,0,2,0,0,0,0,1,0,0,0,1},
       {1,1,0,1,0,1,1,1,1,0,1,0,1,1,1,0,1,1,1},
       {0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0},
       {1,0,1,1,1,1,1,1,0,1,0,1,1,1,1,1,1,0,1},
       {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
       {1,1,1,1,0,0,1,1,1,1,1,1,1,0,0,1,1,1,1}
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
       char nextDirection=' ';


       MazeMania() {
        setPreferredSize(new Dimension(boardWidth,boardHeight));
        setBackground(Color.darkGray);
        addKeyListener(this);
        setFocusable(true);
        
        homeScreenImage=new ImageIcon(getClass().getResource("./background.png")).getImage();
        endScreenImage=new ImageIcon(getClass().getResource("./gameover.jpeg")).getImage();

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
        heartImage=new ImageIcon(getClass().getResource("./heart.png")).getImage();

        loadMap();
        for(Block ghost:ghosts) {
              char newD=direction[rando.nextInt(4)];
              ghost.updateDirection(newD);
        }
        gameLoop=new Timer(50,this);
        //gameLoop.start();
       }

       public void loadMap() {
              walls=new HashSet<>();
              foods=new HashSet<>();
              ghosts=new HashSet<>();
              
              for(int i=0;i<rowCnt;i++) {
                     for(int j=0;j<columnCnt;j++) {
                            int tile=tileMap[i][j];
                            int x=j*tileSize;
                            int y=i*tileSize;

                            if(tile==1) walls.add(new Block(wallImage,x,y,tileSize,tileSize));
                            else if(tile==0) foods.add(new Block(foodImage,x+8,y+8,16,16));
                            else if(tile==2) pacman=new Block(pacmanRight,x,y,tileSize,tileSize);
                            else if(tile==3) ghosts.add(new Block(cyanGhost,x,y,tileSize,tileSize));
                            else if(tile==4) ghosts.add(new Block(redGhost,x,y,tileSize,tileSize));
                            else if(tile==5) ghosts.add(new Block(orangeGhost,x,y,tileSize,tileSize));
                            else if(tile==6) ghosts.add(new Block(pinkGhost,x,y,tileSize,tileSize));
                     }

              }    
    }

       public void paintComponent(Graphics g) {
              super.paintComponent(g);
              if(showHomeScreen) drawHomeScreen(g);
              else if(showEndScreen) drawEndScreen(g);
              else draw(g);
       }

       private void drawHomeScreen(Graphics g) {
            int imgW=homeScreenImage.getWidth(null);
            int imgH=homeScreenImage.getHeight(null);
            g.drawImage(homeScreenImage,(boardWidth-imgW)/2,(boardHeight-imgH)/3,null);

            g.setColor(Color.YELLOW);
            g.setFont(new Font("Arial",Font.BOLD,34));
            String msg="Press 'S' to Start!!"; 
            int msgWidth=g.getFontMetrics().stringWidth(msg);
            g.drawString(msg,(boardWidth-msgWidth)/2,boardHeight-100);
       }

       private void drawEndScreen(Graphics g) {
              int imgW=endScreenImage.getWidth(null);
              int imgH=endScreenImage.getHeight(null);
              g.drawImage(endScreenImage,(boardWidth-imgW)/2,(boardHeight-imgH)/3,null);

              g.setColor(Color.RED);
              g.setFont(new Font("Arial",Font.BOLD,44));
              String msg="Game Over ! Score: "+score;
              int msgWidth=g.getFontMetrics().stringWidth(msg);
              g.drawString(msg,(boardWidth-msgWidth)/2,boardHeight/2-20);

              g.setColor(Color.YELLOW);
              g.setFont(new Font("Arial",Font.BOLD,36));
              String str="Press 'SPACEBAR' to play again!!";
              int strtWidth= g.getFontMetrics().stringWidth(str);
              g.drawString(str,(boardWidth-strtWidth)/2,boardHeight/2+40);
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
                     g.setFont(new Font("Arial",Font.BOLD,26));
                     g.setColor(Color.RED);
                     g.drawString("Game Over! Score : " + score,tileSize,tileSize);
              } else {
                     int heartX=tileSize/2;
                     int heartY=tileSize/4;
                     for(int i=0;i<lives;i++) {
                     g.drawImage(heartImage,heartX+i*(tileSize+4),heartY,tileSize,tileSize,null);
              }
              g.setFont(new Font("Arial",Font.BOLD,26));
              g.setColor(Color.BLACK);
              g.drawString("Score: " + score,boardWidth-200+2,tileSize);
              g.setColor(Color.YELLOW);
              g.drawString("Score: "+score,boardWidth-200,tileSize);
          }
       }

       public void move() {
              if(canMove(pacman,nextDirection)) {
                 pacman.updateDirection(nextDirection);
              }
              pacman.x+=pacman.velX;
              pacman.y+=pacman.velY;

              if(pacman.x<0) {
                     pacman.x=boardWidth-pacman.width;
              } else if(pacman.x+pacman.width>boardWidth) {
                     pacman.x=0;
              } 

              if(pacman.y<0) {
                     pacman.y=boardHeight-pacman.height;
              } else if(pacman.y+pacman.height>boardHeight) {
                     pacman.y=0;
              } 

              if(pacman.d=='U') pacman.image=pacmanUp;
              else if(pacman.d=='D') pacman.image=pacmanDown;
              else if(pacman.d=='L') pacman.image=pacmanLeft;
              else if(pacman.d=='R') pacman.image=pacmanRight;
 
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
                            nextDirection=' ';
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

       public boolean canMove(Block a,char d) {
              int tempX=a.x;
              int tempY=a.y;
              int speed=this.speed;
              
              if(d=='U') tempY-=speed;
              else if(d=='D') tempY+=speed;
              else if(d=='L') tempX-=speed;
              else if(d=='R') tempX+=speed;

              Block temp=new Block(null,tempX,tempY,a.width,a.height);

              for(Block wall:walls) {
                     if(collision(temp,wall)) {
                            return false;
                     }
              }
              return true;
       }
       
       public void resetPosition() {
              pacman.reset();
              pacman.velX=0;
              pacman.velY=0;
              nextDirection=' ';
              
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
                     showEndScreen=true;
              }
       }

       @Override
       public void keyTyped(KeyEvent e) {
       }

       @Override
       public void keyPressed(KeyEvent e) {
              if(showHomeScreen && e.getKeyCode()==KeyEvent.VK_S) {
              showHomeScreen=false;
              gameLoop.start();
              return;
          }

              if(showEndScreen && e.getKeyCode()==KeyEvent.VK_SPACE) {
                     showEndScreen=false;
                     loadMap();
                     resetPosition();
                     lives=3;
                     score=0;
                     gameOver=false;
                     nextDirection=' ';
                     gameLoop.start();
                     return;
          }

              if(gameOver || showEndScreen) return;
             
              if(e.getKeyCode()==KeyEvent.VK_UP) nextDirection='U';
              else if(e.getKeyCode()==KeyEvent.VK_DOWN) nextDirection='D';
              else if(e.getKeyCode()==KeyEvent.VK_LEFT) nextDirection='L';
              else if (e.getKeyCode()==KeyEvent.VK_RIGHT) nextDirection='R';

         }

       @Override
       public void keyReleased(KeyEvent e) {

       if(gameOver) {
              loadMap();
              resetPosition();
              lives=3;
              score=0;
              gameOver=false;
              nextDirection=' ';
              gameLoop.start();
       }
 }
}
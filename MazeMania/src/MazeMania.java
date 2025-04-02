import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Random;
import javax.swing.*;

public class MazeMania extends JPanel {
       
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
       

       MazeMania() {
        setPreferredSize(new Dimension(boardWidth,boardHeight));
        setBackground(Color.darkGray);
        
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

        
       }
}

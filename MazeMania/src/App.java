import javax.swing.JFrame;

public class App {
    public static void main(String[] args) throws Exception {
        int rowCnt=21;
        int columnCnt=19;
        int tileSize=36;
        int boardHeight=rowCnt*tileSize;
        int boardWidth=columnCnt*tileSize;

        JFrame frame=new JFrame("Maze Mania!");

        //frame.setVisible(true);
        frame.setSize(boardWidth,boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        MazeMania maze=new MazeMania();
        frame.add(maze);
        frame.pack();
        maze.requestFocus();
        frame.setVisible(true);
    }
}

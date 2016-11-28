package csci310javafinal;

import static kiss.API.*;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class CSCI310JavaFinal extends JFrame {
    JLabel statusbar;
    
    public CSCI310JavaFinal() {
        statusbar = new JLabel(" 0");
        add(statusbar, BorderLayout.SOUTH);
        Board board = new Board(this);
        add(board);
        board.start();
        setSize(200, 400);
        setTitle("CSCI 310 Java Final: Tetris Clone");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
    public JLabel getStatusBar() {
        return statusbar;
    }
    
    public static void main(String[] args) {
        CSCI310JavaFinal game = new CSCI310JavaFinal();
        game.setLocationRelativeTo(null);
        game.setVisible(true);
    }
}

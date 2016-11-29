package csci310javafinal;

import static kiss.API.*;
import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class CSCI310JavaFinal extends JFrame {

    JLabel statusbar;
    int width = 20;
    int height = 10;

    public CSCI310JavaFinal() {
        statusbar = new JLabel(" 0");
        statusbar.setFont(new Font(statusbar.getFont().getName(), 
                          statusbar.getFont().getStyle(), 30));        
        add(statusbar, BorderLayout.SOUTH);
        Board board = new Board(this);
        add(board);
        board.start();
        setSize(300, 600);
        setTitle("CSCI 310 Java Final");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public JLabel getStatusBar() {
        return statusbar;
    }
    
    void testgetStatusBar() {
        
    }

    public static void main(String[] args) {
        CSCI310JavaFinal game = new CSCI310JavaFinal();
        game.setLocationRelativeTo(null);
        game.setVisible(true);
    }
}

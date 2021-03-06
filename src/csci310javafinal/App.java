package csci310javafinal;

import static kiss.API.*;
import java.awt.*;
import javax.swing.*;

public class App extends JFrame {

    JLabel statusbar;
    int width = 20;
    int height = 10;

    public App() {
        statusbar = new JLabel(" 0");
        statusbar.setFont(new Font(statusbar.getFont().getName(), 
                          statusbar.getFont().getStyle(), 20));        
        add(statusbar, BorderLayout.SOUTH);
        Game board = new Game(this);
        add(board);
        board.start();
        setSize(300, 600);
        setTitle("Java Final Project");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public JLabel getStatusBar() {
        return statusbar;
    }
    
    void testgetStatusBar() {
        JLabel status;
        status = new JLabel();
        assert (status == statusbar);
        println("statusbar = " + statusbar);
    }

    public static void main(String[] args) {
        App game = new App();
        game.setLocationRelativeTo(null);
        game.setVisible(true);
    }
}

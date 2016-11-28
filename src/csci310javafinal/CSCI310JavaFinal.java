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
        //add code for Board here when its done
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

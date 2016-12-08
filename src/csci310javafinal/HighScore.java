package csci310javafinal;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class HighScore {
    HighScore(){
        JFrame history = new JFrame();
        JPanel panel = new JPanel(new GridLayout(3, 1));
        JLabel numGames = new JLabel("HIGH SCORE: ");
        JTextArea highscore = new JTextArea();
        JButton clearHistory = new JButton();

        try {
            String textLine;
            FileReader fr = new FileReader("highscore.txt");
            BufferedReader reader = new BufferedReader(fr);
            while((textLine=reader.readLine()) != null){
                textLine = reader.readLine();
                highscore.read(reader,"Something");
            }
            reader.close();

        } catch(IOException ex) {
                System.out.println("ABORT! YOU KILLED IT!!");
            }

        history.pack();
        history.setVisible(true);
        
        panel.add(highscore);

        JOptionPane.showMessageDialog(null, panel, "High Score", JOptionPane.PLAIN_MESSAGE);
    }
}

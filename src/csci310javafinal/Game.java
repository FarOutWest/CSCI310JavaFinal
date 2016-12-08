package csci310javafinal;

import static kiss.API.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import csci310javafinal.Shapes.Blocks;
import java.io.*;
import static java.lang.String.valueOf;

public class Game extends JPanel implements ActionListener {
    final int BoardWidth = 10;
    final int BoardHeight = 22;

    Timer timer;
    boolean FallingFinished = false;
    boolean Started = false;
    boolean Paused = false;
    int numLinesRemoved = 0;
    int currentX = 0;
    int currentY = 0;
    static JLabel statusbar;
    Shapes currentPiece;
    Blocks[] gameBoard;

    public Game(App parent) {
        setFocusable(true);
        currentPiece = new Shapes();
        statusbar = parent.getStatusBar();
        gameBoard = new Blocks[BoardWidth * BoardHeight];
        addKeyListener(new KAdapter());
        clearBoard();
    }

    final void testTimer() {
        println("TIMER DELAY = " + timer.getDelay());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (FallingFinished) {
            FallingFinished = false;
            newPiece();
        } else {
            oneLineDown();
        }
    }

    int squareWidth() {
        return (int) getSize().getWidth() / BoardWidth;
    }

    int squareHeight() {
        return (int) getSize().getHeight() / BoardHeight;
    }

    Blocks shapeLoc(int x, int y) {
        return gameBoard[(y * BoardWidth) + x];
    }

    public void start() {
        if (Paused) { return; }
        Started = true;
        FallingFinished = false;
        if (numLinesRemoved == 0) {
            timer = new Timer(450, this);
            timer.start();
            testTimer();
        }
        clearBoard();
        newPiece();
        timer.start();
    }

    private void pause() {
        if (!Started) { return; }

        Paused = !Paused;
        if (Paused) {
            timer.stop();
            statusbar.setText("paused");
        } else {
            timer.start();
            statusbar.setText(String.valueOf(numLinesRemoved));
        }
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Dimension size = getSize();
        int boardTop = (int) size.getHeight() - BoardHeight * squareHeight();

        for (int i = 0; i < BoardHeight; ++i) {
            for (int j = 0; j < BoardWidth; ++j) {
                Blocks shape = shapeLoc(j, BoardHeight - i - 1);
                if (shape != Blocks.NoShape) {
                    drawSquare(g, 0 + j * squareWidth(),
                            boardTop + i * squareHeight(), shape);
                }
            }
        }

        if (currentPiece.getShape() != Blocks.NoShape) {
            for (int i = 0; i < 4; ++i) {
                int x = currentX + currentPiece.x(i);
                int y = currentY - currentPiece.y(i);
                drawSquare(g, 0 + x * squareWidth(),
                        boardTop + (BoardHeight - y - 1) * squareHeight(),
                        currentPiece.getShape());
            }
        }
    }

    private void dropDown() {
        int newY = currentY;
        while (newY > 0) {
            if (!tryMove(currentPiece, currentX, newY - 1)) {
                break;
            }
            --newY;
        }
        pieceDropped();
    }

    private void oneLineDown() {
        if (!tryMove(currentPiece, currentX, currentY - 1)) {
            pieceDropped();
        }
    }

    private void clearBoard() {
        for (int i = 0; i < BoardHeight * BoardWidth; ++i) {
            gameBoard[i] = Blocks.NoShape;
        }
    }

    private void pieceDropped() {
        for (int i = 0; i < 4; ++i) {
            int x = currentX + currentPiece.x(i);
            int y = currentY - currentPiece.y(i);
            gameBoard[(y * BoardWidth) + x] = currentPiece.getShape();
        }
        removeFullLines();

        if (!FallingFinished) {
            newPiece();
        }
    }

    private int randomX() {
        int max = BoardWidth - 2;
        int min = 2;
        int random = (int) (Math.random() * max - 1 + min);
        return random;
    }

    void testrandomX() {
        int test = randomX();
        assert (test == 2 || test == 3 || test == 4 || test == 5
                || test == 6 || test == 7 || test == 8);
        println("testing RandomX = " + test);
    }
    
    void writeHighScore() {
        String highScore = statusbar.getText();
        if (" 0".equals(highScore)) {
            highScore = highScore.substring(1);
        }
        String oldHigh = "0";
        //println("highScore: " + highScore);
        int oldHighInt = Integer.parseInt(oldHigh);
        int highScoreInt = Integer.parseInt(highScore);
        //println("old high INT : " + oldHighInt);
        //println("high score INT: " + highScoreInt);
        //WONT READ FILE... SO THATS GREAT.... 
        //println(new File("highscore.txt").getAbsolutePath());
        String fileName = "highscore.txt";
        String line = null;

        try {
            FileReader fileReader = new FileReader(fileName);
            try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                while((line = bufferedReader.readLine()) != null) {
                    oldHigh = line;
                    oldHighInt = Integer.parseInt(line);
                    println("Old High Score: " + line);
                }
            }   
            
            FileWriter fileWriter = new FileWriter(fileName);
            try (BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
                if ((int) oldHighInt >= (int) highScoreInt) {
                    bufferedWriter.write(oldHigh);
                    println("You didn't beat the old High Score. Try Again!");
                } else {
                    bufferedWriter.write(highScore);
                    println("New High Score: " + highScore);
                }
            }
        }
        
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + fileName + "'");                
        }
        
        catch(IOException ex) {
            System.out.println("Error reading from/writing to file '" + fileName + "'");                  
        }
    }

    private void newPiece() {
        currentPiece.setRandomShape();
        currentX = randomX();
        currentY = (int) (BoardHeight - 1 + currentPiece.minY());
        
        testrandomX();
        System.out.print(valueOf(currentPiece) + EOL);
        System.out.print("currentX = " + currentX + EOL);
        System.out.print("currentY = " + currentY + EOL);

        if (!tryMove(currentPiece, currentX, currentY)) {
            currentPiece.setShape(Blocks.NoShape);
            timer.stop();
            Started = false;
            writeHighScore();
            statusbar.setText("GAME OVER - SCORE: " + valueOf(numLinesRemoved));
        }
    }

    private boolean tryMove(Shapes newPiece, int newX, int newY) {
        for (int i = 0; i < 4; ++i) {
            int x = newX + newPiece.x(i);
            int y = newY - newPiece.y(i);
            if (x < 0 || x >= BoardWidth || y < 0 || y >= BoardHeight) {
                return false;
            }
            if (shapeLoc(x, y) != Blocks.NoShape) {
                return false;
            }
        }

        currentPiece = newPiece;
        currentX = newX;
        currentY = newY;
        repaint();
        return true;
    }
    
    void testLevelError() {
        println("ERROR:");
        println("could not find level information");
        println("Timer delay set to default 450");
    }

    //Set up Levels and actual scoring for the game
    private void removeFullLines() {
        int numFullLines = 0;

        for (int i = BoardHeight - 1; i >= 0; --i) {
            boolean lineIsFull = true;

            for (int j = 0; j < BoardWidth; ++j) {
                if (shapeLoc(j, i) == Blocks.NoShape) {
                    lineIsFull = false;
                    break;
                }
            }

            if (lineIsFull) {
                ++numFullLines;
                for (int k = i; k < BoardHeight - 1; ++k) {
                    for (int j = 0; j < BoardWidth; ++j) {
                        gameBoard[(k * BoardWidth) + j] = shapeLoc(j, k + 1);
                    }
                }
            }
        }

        if (numFullLines > 0) {
            if (Paused == true) { pause(); }
            numLinesRemoved += numFullLines;
            statusbar.setText(String.valueOf(numLinesRemoved));
            FallingFinished = true;
            currentPiece.setShape(Blocks.NoShape);

            if (numLinesRemoved >= 10 && numLinesRemoved <= 19) {
                if (Paused == true) { pause(); }
                timer.setDelay(400);
                if (Paused == false) { 
                    timer.start();
                    testTimer();
                }
            }

            else if (numLinesRemoved >= 20 && numLinesRemoved <= 34) {
                if (Paused == true) { pause(); }
                timer.setDelay(375);
                if (Paused == false) {
                    timer.start();
                    testTimer();
                }
            }

            else if (numLinesRemoved >= 35 && numLinesRemoved <= 49) {
                if (Paused == true) { pause(); }
                timer.setDelay(350);
                if (Paused == false) {
                    timer.start();
                    testTimer();
                }
            }

            else if (numLinesRemoved >= 50 && numLinesRemoved <= 74) {
                if (Paused == true) { pause(); }
                timer.setDelay(300);
                if (Paused == false) {
                    timer.start();
                    testTimer();
                }
            }

            else if (numLinesRemoved >=  75) {
                if (Paused == true) { pause(); }
                timer.setDelay(275);
                if (Paused == false) {
                    timer.start();
                    testTimer();
                }
            }
            
            else {
                if (Paused == true) { pause(); }
                timer.setDelay(450);
                if (Paused == false) {
                    timer.start();
                    testLevelError();
                    testTimer();
                }
            }
            repaint();
        }
    }

    private void drawSquare(Graphics g, int x, int y, Blocks shape) {
        Color colors[] = {Color.BLACK, Color.CYAN, Color.ORANGE, Color.RED,
            Color.YELLOW, Color.MAGENTA, Color.GREEN, Color.GRAY};

        Color color = colors[shape.ordinal()];

        g.setColor(color);
        g.fillRect(x + 1, y + 1, squareWidth() - 2, squareHeight() - 2);
        g.setColor(color.brighter());
        g.drawLine(x, y + squareHeight() - 1, x, y);
        g.drawLine(x, y, x + squareWidth() - 1, y);
        g.setColor(color.darker());
        g.drawLine(x + 1, y + squareHeight() - 1,
                x + squareWidth() - 1, y + squareHeight() - 1);
        g.drawLine(x + squareWidth() - 1, y + squareHeight() - 1,
                x + squareWidth() - 1, y + 1);
    }

    class KAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {

            if (!Started || currentPiece.getShape() == Blocks.NoShape) {
                return;
            }

            int keycode = e.getKeyCode();

            if (keycode == 'p' || keycode == 'P') {
                pause();
                return;
            }

            if (keycode == 'd' || keycode == 'D') {
                dropDown();
                return;
            }

            switch (keycode) {
                case KeyEvent.VK_LEFT:
                    tryMove(currentPiece, currentX - 1, currentY);
                    break;
                case KeyEvent.VK_RIGHT:
                    tryMove(currentPiece, currentX + 1, currentY);
                    break;
                case KeyEvent.VK_DOWN:
                    tryMove(currentPiece, currentX, currentY - 1);
                    break;
                case KeyEvent.VK_SPACE:
                    tryMove(currentPiece.rotate(), currentX, currentY);
                    break;
            }
        }
    }
}

package csci310javafinal;

import static kiss.API.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import csci310javafinal.Shapes.Blocks;

public class Board extends JPanel implements ActionListener {
    final int BoardWidth = 10;
    final int BoardHeight = 22;
    
    Timer timer;
    boolean FallingFinished = false;
    boolean Started = false;
    boolean Paused = false;
    int LinesRemoved = 0;
    int currentX = 0;
    int currentY = 0;
    JLabel statusbar;
    Shapes currentPiece;
    Blocks[] board;
    
    public Board(CSCI310JavaFinal parent) {
        setFocusable(true);
        currentPiece = new Shapes();
        timer = new Timer(500, this);
        timer.start();
        statusbar = parent.getStatusBar();
        board = new Blocks[BoardWidth * BoardHeight];
        addKeyListener(new TAdapter());
        clearBoard();
    }
    
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
    
    Blocks shapeLocation(int x, int y) {
        return board[(y * BoardWidth) + x];
    }
    
    public void start() {
        if (Paused) return;
        Started = true;
        FallingFinished = false;
        LinesRemoved = 0;
        clearBoard();
        newPiece();
        timer.start();
    }
    
    private void pause() {
        if (!Started) return;
        Paused = !Paused;
        if (Paused) {
            timer.stop();
            statusbar.setText("game is paused");
        } else {
            timer.start();
            statusbar.setText(String.valueOf(LinesRemoved));
        }
        repaint();
    }
    
    public void paint(Graphics g) {
        super.paint(g);
        Dimension size = getSize();
        int boardTop = (int) size.getHeight() - BoardHeight * squareHeight();
        
        for (int i = 0; i < BoardHeight; ++i) {
            for (int j = 0; j < BoardWidth; ++j) {
                Blocks shape = shapeLocation(j, BoardHeight- i - 1);
                if (shape != Blocks.NoShape)
                    drawSquare(g, 0 + j * squareWidth(),
                            boardTop + i * squareHeight(), shape);
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
            if (!tryMove(currentPiece, currentX, newY - 1)) break;
            --newY;
        }
        pieceDropped();
    }
    
    private void oneLineDown() {
        if (!tryMove(currentPiece, currentX, currentY - 1)) pieceDropped();
    }
    
    private void clearBoard() {
        for (int i = 0; i < BoardHeight * BoardWidth; ++i)
            board[i] = Blocks.NoShape;
    }
    
    private void pieceDropped() {
        for (int i = 0; i < 4; ++i) {
            int x = currentX + currentPiece.x(i);
            int y = currentY + currentPiece.y(i);
            board[(y * BoardWidth) + x] = currentPiece.getShape();
        }
        removeFullLines();
        if (!FallingFinished) newPiece();
    }
    
    private void newPiece() {
        currentPiece.setRandomShape();
        currentX = BoardWidth / 2 + 1;
        currentY = BoardHeight - 1 + currentPiece.minY();
        
        if (!tryMove(currentPiece, currentX, currentY)) {
            currentPiece.setShape(Blocks.NoShape);
            timer.stop();
            Started = false;
            statusbar.setText("GAME OVER!");
        }
    }
    
    private boolean tryMove(Shapes newPiece, int newX, int newY) {
        for (int i = 0; i < 4; ++i) {
            int x = newX + newPiece.x(i);
            int y = newY + newPiece.y(i);
            if (x < 0 || x >= BoardWidth || y < 0 || y >= BoardHeight)
                return false;
            if (shapeLocation(x, y) != Blocks.NoShape)
                return false;
        }
        
        currentPiece = newPiece;
        currentX = newX;
        currentY = newY;
        repaint();
        return true;
    }
    
    private void removeFullLines() {
        int numFullLines = 0;
        for (int i = BoardHeight - 1; i >= 0; --i) {
            boolean lineIsFull = true;
            for (int j = 0; j < BoardWidth; ++j) {
                if (shapeLocation(j, i) == Blocks.NoShape) {
                    lineIsFull = false;
                    break;
                }
            }
            if (lineIsFull) {
                ++numFullLines;
                for (int k = i; k < BoardHeight - 1; ++k) {
                    for (int j = 0; j < BoardWidth; ++j)
                        board[(k * BoardWidth) + j] = shapeLocation(j, k + 1);
                }
            }
        }
        
        if (numFullLines > 0) {
            LinesRemoved += numFullLines;
            statusbar.setText(String.valueOf(LinesRemoved));
            FallingFinished = true;
            currentPiece.setShape(Blocks.NoShape);
            repaint();
        }
    }
    
    private void drawSquare(Graphics g, int x, int y, Blocks shape) {
        Color colors[] = { 
            new Color(0, 0, 0), new Color(204, 102, 102), 
            new Color(102, 204, 102), new Color(102, 102, 204), 
            new Color(204, 204, 102), new Color(204, 102, 204), 
            new Color(102, 204, 204), new Color(218, 170, 0)
        };
        
        Color color = colors[shape.ordinal()];
        
        g.setColor(color);
        g.fillRect(x + 1, y + 1, squareWidth() - 2, squareHeight() - 2);
        g.setColor(color.brighter());
        g.drawLine(x, y + squareHeight() - 1, x, y);
        g.drawLine(x, y, x + squareWidth() - 1, y);
        g.setColor(color.darker());
        g.drawLine(x + 1, y + squareHeight() - 1,
                   x + squareWidth() - 1, y + squareHeight() - 1);
        g.drawLine(x + squareWidth() - 1, y + squareHeight() -1,
                   x + squareWidth() - 1, y + 1);
    }
    
    class TAdapter extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            if (!Started || currentPiece.getShape() == Blocks.NoShape) return;

            int keycode = e.getKeyCode();

            if (keycode == 'p' || keycode == 'P') {
                pause();
                return;
            }

            if (Paused) return;

            switch (keycode) {
                case KeyEvent.VK_LEFT:
                    tryMove(currentPiece, currentX - 1, currentY);
                    break;
                case KeyEvent.VK_RIGHT:
                    tryMove(currentPiece, currentX + 1, currentY);
                    break;
                case KeyEvent.VK_DOWN:
                    tryMove(currentPiece.rotateLeft(), currentX, currentY);
                    break;
                case KeyEvent.VK_UP: 
                    tryMove(currentPiece.rotateRight(), currentX, currentY);
                    break;
                case KeyEvent.VK_SPACE:
                    tryMove(currentPiece, currentX, currentY - 1);
                    break;
                case KeyEvent.VK_D:
                    dropDown();
                    break;
            }
        }
    }
}

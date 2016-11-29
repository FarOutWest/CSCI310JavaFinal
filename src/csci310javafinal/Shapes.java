package csci310javafinal;

import static kiss.API.*;
import java.util.*;
import java.lang.Math;

public class Shapes {

    enum Blocks {
        NoShape,
        ZShape,
        SShape,
        LineShape,
        TShape,
        SquareShape,
        LShape,
        MirroredLShape
    };

    private Blocks pieceShape;
    private int coords[][];
    private int[][][] coordsTable;

    public Shapes() {
        coords = new int[4][2];
        setShape(Blocks.NoShape);
    }

    public void setShape(Blocks shape) {
        coordsTable = new int[][][]{
            {{0, 0}, {0, 0}, {0, 0}, {0, 0}}, //NoShape
            {{0, -1}, {0, 0}, {-1, 0}, {-1, 1}}, //ZShape
            {{0, -1}, {0, 0}, {1, 0}, {1, 1}}, //SShape
            {{0, -1}, {0, 0}, {0, 1}, {0, 2}}, //LineShape
            {{-1, 0}, {0, 0}, {1, 0}, {0, 1}}, //TShape
            {{0, 0}, {1, 0}, {0, 1}, {1, 1}}, //SquareShape
            {{-1, -1}, {0, -1}, {0, 0}, {0, 1}}, //LShape
            {{1, -1}, {0, -1}, {0, 0}, {0, 1}} //MirroredLShape
        };

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 2; ++j) {
                coords[i][j] = coordsTable[shape.ordinal()][i][j];
            }
        }
        pieceShape = shape;
    }

    private void setX(int index, int x) { coords[index][0] = x; }

    private void setY(int index, int y) { coords[index][1] = y; }

    public int x(int index) { return coords[index][0]; }

    public int y(int index) { return coords[index][1]; }

    public Blocks getShape() { return pieceShape; }

    public void setRandomShape() {
        Random r = new Random();
        int x = Math.abs(r.nextInt()) % 7 + 1; //pick a random shape
        Blocks[] values = Blocks.values();
        setShape(values[x]);
    }

    void testsetRandomShape() {
        
    }

    public int minX() {
        int m = coords[0][0];
        for (int i = 0; i < 4; i++) {
            m = Math.min(m, coords[i][0]);
        }
        return m;
    }
    
    void testMinX() {
        
    }

    public int minY() {
        int m = coords[0][1];
        for (int i = 0; i < 4; i++) {
            m = Math.min(m, coords[i][1]);
        }
        return m;
    }
    
    void testMinY() {
        
    }

    public Shapes rotate()//rotateLeft() 
    {
        if (pieceShape == Blocks.SquareShape) {
            return this;
        }

        Shapes result = new Shapes();
        result.pieceShape = pieceShape;

        for (int i = 0; i < 4; ++i) {
            result.setX(i, y(i));
            result.setY(i, -x(i));
        }
        return result;
    }
    
    void testRotate() {
        
    }

    /*public Shapes rotateRight()
    {
        if (pieceShape == Blocks.SquareShape)
            return this;

        Shapes result = new Shapes();
        result.pieceShape = pieceShape;

        for (int i = 0; i < 4; ++i) {
            result.setX(i, -y(i));
            result.setY(i, x(i));
        }
        return result;
    }*/
}

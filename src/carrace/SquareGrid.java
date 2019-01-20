package carrace;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class SquareGrid extends Pane
{
    public static int SQUARE_SIZE = 20;   //velikost ctverecku
    public static int WINDOW_HEIGHT = 650;             //defaultni velikost okna
    public static int WINDOW_WIDTH = 1200;
    private static Color GRID_COLOR = Color.LIGHTBLUE;

    public SquareGrid(int width, int height, int square, Color color)    //vytvoreni ctvercove site
    {
        WINDOW_WIDTH = width;
        WINDOW_HEIGHT = height;
        SQUARE_SIZE = square;
        GRID_COLOR = color;
        this.setStyle("-fx-background: #FFFFFF;");
        Line line;
        for(int i = 0; i < width; i = i + square)           //vertikalni cary       
        {
            line = new Line(i, 0, i, height);           
            line.setStroke(color);
            this.getChildren().add(line);
        }
        for(int i = 0; i < height; i = i + square)          //horizontalni cary  
        {
            line = new Line(0, i, width, i);
            line.setStroke(color);
            this.getChildren().add(line);
        }
    }
    public SquareGrid(int width, int height, int square)
    {
        this(width, height, square, GRID_COLOR);
    }
    
    public SquareGrid()
    {
        this(WINDOW_WIDTH, WINDOW_HEIGHT, SQUARE_SIZE, GRID_COLOR);
    }
    
    public SquareGrid(Track track)
    {
        this((int)track.getWidth(), (int)track.getHeight(), SQUARE_SIZE);
    }
}

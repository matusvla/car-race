package carrace;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class FinishLine extends Line
{
    public FinishLine(int startX, int startY, int endX, int endY)
    {
        super(startX * SquareGrid.SQUARE_SIZE, startY * SquareGrid.SQUARE_SIZE, endX * SquareGrid.SQUARE_SIZE, endY * SquareGrid.SQUARE_SIZE);
        setStroke(Color.BLUE);
    }    
    public FinishLine()
    {        
    }
}

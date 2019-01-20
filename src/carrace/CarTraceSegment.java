package carrace;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class CarTraceSegment extends Line
{
    public CarTraceSegment(Coordinates beginning, Coordinates end, Color color)
    {
        super(beginning.getX(), beginning.getY(), end.getX(), end.getY());
        setStroke(color);
    }    
}

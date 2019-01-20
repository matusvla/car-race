package carrace;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public abstract class AbstractPossibleMove extends Circle
{
    private Car car;
    private Coordinates xy;
    private boolean absoluteMove;
    public AbstractPossibleMove(Coordinates xy, Car car, boolean absoluteMove)
    {
        super(xy.getX(),xy.getY(),SquareGrid.SQUARE_SIZE/2-1);
        this.car = car;
        this.xy = xy;
        this.absoluteMove = absoluteMove;
        
        setDesign();
    }  
    
    public abstract void clickevent();
    public abstract void setDesign();

    public Car getCar() {
        return car;
    }

    public boolean isAbsoluteMove() {
        return absoluteMove;
    }
}

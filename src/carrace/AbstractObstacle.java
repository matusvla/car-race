package carrace;

import javafx.scene.shape.Rectangle;

public abstract class AbstractObstacle extends Rectangle
{           

    public AbstractObstacle()
    {
    }
    
    public AbstractObstacle(double d, double d1, double d2, double d3)
    {
        super(d, d1, d2, d3);
        design();
    }
    
    public abstract void collision(PossibleMoves movesToModify);
    public abstract void design();
}

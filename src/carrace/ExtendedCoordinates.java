package carrace;


public class ExtendedCoordinates<T> extends Coordinates
{
    private T condition;
    public ExtendedCoordinates(int x, int y, T condition)
    {
        super(x,y);
        this.condition = condition;
    }        

    public T getCondition() {
        return condition;
    }
    
    public void setCondition(T condition) {
        this.condition = condition;
    }
}

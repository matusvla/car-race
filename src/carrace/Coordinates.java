package carrace;

import javafx.scene.shape.Line;

public class Coordinates
{
    private int x;    
    private int y;
    public Coordinates(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
    
    public Coordinates(Coordinates copy)
    {
        this.x = copy.getX();
        this.y = copy.getY();
    }
    
    public void changeBy(int x,int y)
    {
        this.x += x;
        this.y += y;
    }
    
    public void changeTo(int x,int y)
    {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    public Coordinates minus(Coordinates minusCoordinates)
    {
        Coordinates result = new Coordinates(this.x - minusCoordinates.x, this.y - minusCoordinates.y);
        return result;
    }
    
    public Coordinates plus(Coordinates plusCoordinates)
    {
        Coordinates result = new Coordinates(this.x + plusCoordinates.x, this.y + plusCoordinates.y);
        return result;
    }
    
    public boolean isEqual(Coordinates equalCoordinates)
    {
        boolean equal = false;
        if(equalCoordinates.getX() == this.getX() && equalCoordinates.getY() == this.getY())
            equal = true;
        return equal;
    }
    
    public boolean isInLineWith(Coordinates a, Coordinates b)
    { 
        return a.distance(b) == (a.distance(this) + this.distance(b));              
    }
    
    public double distance(Coordinates a)
    {
        return Math.sqrt(Math.pow(this.getX()-a.getX(),2.0)+Math.pow(this.getY()-a.getY(),2.0));
    }
    
    @Override
    public String toString()
    {
        return "(" + getX() + ", " + getY() + ")";
    }
}

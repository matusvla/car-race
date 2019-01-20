package carrace;


import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class StartLine extends Line
{
    private int orientation;     
    public StartLine(Coordinates startXY, int sqareLength,  int orientation) // 0 left, 1 up, 2 right, else down
    {
        super(SquareGrid.SQUARE_SIZE * startXY.getX(), SquareGrid.SQUARE_SIZE * startXY.getY(), SquareGrid.SQUARE_SIZE * startXY.getX(), SquareGrid.SQUARE_SIZE * startXY.getY());
        this.orientation = orientation;
        switch(orientation){
            case 0: case 2:
                this.setEndY(this.getEndY()+ sqareLength * SquareGrid.SQUARE_SIZE);
                break;
            default:
                this.setEndX(this.getEndX()+ sqareLength * SquareGrid.SQUARE_SIZE);                
        }    
    }  

    public StartLine() {
    }  
    
    public void addPossibleMoves()
    {
        PossibleMoves possibleMoves = new PossibleMoves(this);
        AppWindow.getGame().setPossibleMoves(possibleMoves);
        AppWindow.getGame().getPossibleMoves().draw(false);
    }

    public int getOrientation() {
        return orientation;
    }
    
     public Coordinates getOrientationCoordinates() {
         switch(getOrientation())
        {
            case 0:
                return new Coordinates(-1, 0);
            case 1:
                return new Coordinates(0, -1);
            case 2:
                return new Coordinates(1, 0);
            default:
                return new Coordinates(0, 1);
        }
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }
    
    public int getCenterX()
    {
        return (int)(this.getStartX() + this.getEndX())/2;
    }
    
    public int getCenterY()
    {
        return (int)(this.getStartY() + this.getEndY())/2;
    }
    
    public int getLength()
    {
        if(this.getOrientation() == 0 || this.getOrientation() ==2)
            return (int)(Math.abs(this.getStartY()-this.getEndY()));
        else
            return (int)(Math.abs(this.getStartX()-this.getEndX()));
    }
}

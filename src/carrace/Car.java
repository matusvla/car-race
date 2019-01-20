package carrace;

import java.util.ArrayList;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

public class Car extends Circle
{
    private Coordinates xy;
    private Coordinates speed;
    private String playerName;
    private int moves;
    private int delay = 0;
    public static Pane allTraces = new Pane();
    public static int CIRCLE_RADIUS = (int)Math.ceil((double)SquareGrid.SQUARE_SIZE/4.0);

    public static Pane getAllTraces()
    {
        return allTraces;
    }
    
    public Coordinates getXY()
    {
        return xy;
    }
    
        
    public void setXY(Coordinates xy)
    {
        this.xy = xy;
    }
    
    public Coordinates getSpeed()
    {
        return speed;
    }
    
    public Car(Coordinates xy, String playerName)
    {
        super(xy.getX(),xy.getY(),CIRCLE_RADIUS);
        this.xy = xy;
        this.speed = new Coordinates(0,0);
        moves = -1;              
    }

    public Car(String playerName)
    {
        this(new Coordinates(-10,-10), playerName);
    }
    
    public Car()
    {
        this(new Coordinates(-10,-10), "Player" + AllCars.NUMBER_OF_CARS);
    }
    
    public void moveTo(int x, int y, boolean absoluteAndNoDraw)
    {
        this.setMoves(this.getMoves() + 1);
        //drawing of new trace segment
        Coordinates startCoordinates= new Coordinates(xy);
        xy.changeTo(x, y);
        if(!absoluteAndNoDraw)
        {
            setCenterX(xy.getX());
            setCenterY(xy.getY());            
            return;
        }
        CarTraceSegment trace = new CarTraceSegment(startCoordinates, xy, (Color)getFill());
        allTraces.getChildren().add(trace);        
        drawOldPosition();
        
        //changing speed
        speed = xy.minus(startCoordinates); 
        
        //checking crash
        Shape crashPlace = Shape.intersect(AppWindow.getGame().getTrack().getAllBorders(),trace);
        Shape finishPlace = Shape.intersect(AppWindow.getGame().getTrack().getFinishLine(),trace);
        ArrayList<Shape> obstaclePlace;       
        if(finishPlace.getBoundsInLocal().getWidth() != -1)
                AppWindow.getGame().getAllCars().removeCar(this);
        else
        {
            if(crashPlace.getBoundsInLocal().getWidth() != -1)
            {    
                delay = delaySetting();
                allTraces.getChildren().add(crashPlace);        
                while(Shape.intersect(AppWindow.getGame().getTrack().getAllBorders(),trace).getBoundsInLocal().getWidth() != -1)
                {
                    try{
                    allTraces.getChildren().remove(trace);
                    }catch(Exception ex){};
                    speed = new Coordinates(0,0);
                    if(Math.abs(startCoordinates.getX()-xy.getX())>= Math.abs(startCoordinates.getY()-xy.getY()))
                    {
                        int sign = (startCoordinates.getX()-xy.getX())/Math.abs(startCoordinates.getX()-xy.getX());
                        xy.changeBy(sign*SquareGrid.SQUARE_SIZE, 0);
                    }
                    else
                    {
                        int sign = (startCoordinates.getY()-xy.getY())/Math.abs(startCoordinates.getY()-xy.getY());
                        xy.changeBy(0,sign*SquareGrid.SQUARE_SIZE);
                    }   
                    trace = new CarTraceSegment(startCoordinates, xy, (Color)getFill());
                    allTraces.getChildren().add(trace);
                } 
                allTraces.getChildren().remove(trace);
                drawCrash(crashPlace,startCoordinates);
            }
        }
        setCenterX(xy.getX());
        setCenterY(xy.getY());
    }
    
    public void moveTo()
    {    
        this.moveTo(this.getXY().getX() + this.getSpeed().getX(), this.getXY().getY() + this.getSpeed().getY(), true);
    }
    
    public void firstMove(StartLine startLine)
    {
        switch(startLine.getOrientation())
        {
            case 0:
                moveBySqares(-1, 0);
                break;
            case 1:
                moveBySqares(0, -1);
                break;
            case 2:
                moveBySqares(1, 0);
                break;
            default:
                moveBySqares(0, 1);
        }
    }
        
    private void moveBySqares(int x, int y)
    {
        moveTo(getXY().getX() + x * SquareGrid.SQUARE_SIZE, getXY().getY() + y * SquareGrid.SQUARE_SIZE, true);
    }
    
    private void drawOldPosition()
    {
        Circle oldPosition = new Circle();
        oldPosition.setCenterX(this.getCenterX());
        oldPosition.setCenterY(this.getCenterY());
        oldPosition.setRadius(CIRCLE_RADIUS-1);
        oldPosition.setFill((Color)getFill());
        allTraces.getChildren().add(oldPosition);
    }
    
    private void drawCrash(Shape crashPlace, Coordinates startCoordinates)
    {        
        int crashX = (int)(crashPlace.getBoundsInParent().getMaxX() - crashPlace.getParent().getParent().getLayoutX());
        int crashY = (int)(crashPlace.getBoundsInParent().getMaxY() - crashPlace.getParent().getParent().getLayoutY());
        Coordinates crashCoordinates = new Coordinates(crashX, crashY);
        Line crossline1 = new Line(crashX-5, crashY, crashX+5, crashY);
        Line crossline2 = new Line(crashX, crashY-5, crashX, crashY+9);
        crossline1.setStrokeWidth(3);
        crossline2.setStrokeWidth(3);
        CarTraceSegment crashLine1 = new CarTraceSegment(startCoordinates, crashCoordinates, (Color)getFill());
        CarTraceSegment crashLine2 = new CarTraceSegment(xy, crashCoordinates, (Color)getFill());
        allTraces.getChildren().addAll(crashLine1, crashLine2, crossline1, crossline2);
    }
    
    public int delaySetting()
    {
        if(AppWindow.getDelaySetting() < 3)
            return AppWindow.getDelaySetting();
        else
            return Math.max(Math.abs(speed.getX())/SquareGrid.SQUARE_SIZE, Math.abs(speed.getY())/SquareGrid.SQUARE_SIZE);
    }

    public String getPlayerName() {
        return playerName;
    }
    
    public int getMoves() {
        return moves;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
    
    public Coordinates getNextCenterPoint()
    {
        return new Coordinates(this.getXY().getX() + this.getSpeed().getX(), this.getXY().getY() + this.getSpeed().getY());
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    /**
     * @param moves the moves to set
     */
    public void setMoves(int moves) {
        this.moves = moves;
    }
}

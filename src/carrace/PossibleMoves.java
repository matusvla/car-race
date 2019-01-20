package carrace;

import java.util.ArrayList;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

public class PossibleMoves extends ArrayList<ExtendedCoordinates<Double>>
{
    private Car car; 
    private int up, down, left, right;    
    private double defaultProbabilityCoefficient = 1;
    private double probabilityCoefficient = getDefaultProbabilityCoefficient();
    
    public PossibleMoves(Car car)
    {
        this.car = car;  
        if(car.getDelay()>0)    //pokud auto pauziruje, at se zmensi cas pauzirovani
        {
            car.setDelay(car.getDelay()-1);
            return;
        }

        if(AppWindow.getDriftSetting()>0) //pokud jsou zapnute drifty
        {
            down = -2;
            up = 2;
        }
        else    //pokud jsou drifty vypnute
        {
            down = -1;
            up = 1;
        }      
                
        for(AbstractObstacle obstacle : AppWindow.getGame().getTrack().getAllObstacles())
        {
            Circle circ = new Circle(car.getXY().getX(),car.getXY().getY(),1);
            AppWindow.getGame().getActiveLayer().getChildren().add(circ);
            if(Shape.intersect(obstacle,circ).getBoundsInLocal().getWidth() != -1)
                obstacle.collision(this);
            AppWindow.getGame().getActiveLayer().getChildren().remove(circ);
        }
        
        createPossibleMoves();
        
    }  
    
    private void createPossibleMoves()
    {
        for(int i = down; i <= up; i++)
            for(int j = down; j <= up; j++)
            {
                int X0 = getCar().getXY().getX() + getCar().getSpeed().getX() + i * SquareGrid.SQUARE_SIZE;   //nove misto na mozny tah Xova souradnice
                int Y0 = getCar().getXY().getY() + getCar().getSpeed().getY() + j * SquareGrid.SQUARE_SIZE;   //nove misto na mozny tah Yova souradnice                
                if((new Coordinates(X0, Y0)).distance(getCar().getNextCenterPoint())==0)
                    probabilityCoefficient = 1;
                else if((new Coordinates(X0, Y0)).distance(getCar().getNextCenterPoint())>SquareGrid.SQUARE_SIZE*1.9)                    
                    probabilityCoefficient = getDefaultProbabilityCoefficient() * SquareGrid.SQUARE_SIZE/getCar().getNextCenterPoint().distance(new Coordinates(X0, Y0));
                else
                    probabilityCoefficient = getDefaultProbabilityCoefficient();
                Coordinates coordinatesX0Y0 = new Coordinates(X0, Y0);  //nove souradnice pro mozny tah
                boolean unoccupied = true;  //
                for(Car c : AppWindow.getGame().getAllCars())
                {
                    if(AppWindow.getBlockSetting() != 2)    //pokud se blokuje alespon misto
                        if(c.getXY().isEqual(coordinatesX0Y0))  //pokud je zablokovano
                        {
                            unoccupied = false;     //zakaz tah
                            break;
                        }
                    if(AppWindow.getBlockSetting() == 0)    //pokud se blokuje i prujezd
                        if(getCar() != c && c.getXY().isInLineWith(getCar().getXY(), coordinatesX0Y0) && !car.getXY().isEqual(c.getXY())) //pokud neco stoji v ceste
                        {
                            unoccupied = false;     //zakaz tah
                            break;
                        }
                }
                if(unoccupied)  //nenastal zadny problem s timto moznym tahem
                    this.add(new ExtendedCoordinates<Double>(X0, Y0, probabilityCoefficient));
            }
    }
    
    public PossibleMoves(StartLine startLine)
    {      
        this.car = AppWindow.getGame().getAllCars().peek();     //podivame se na prvni auto ve fronte
        if(startLine.getOrientation() == 0 || startLine.getOrientation() == 2)  //svisla startline
            for(int i = (int)startLine.getStartY()+ SquareGrid.SQUARE_SIZE; i < startLine.getEndY(); i = i + SquareGrid.SQUARE_SIZE) //na vsechny mista na startline krome kraju
            {
                ExtendedCoordinates<Double> possiblePlace = new ExtendedCoordinates<>((int)startLine.getStartX(), i, probabilityCoefficient); //zkontrolujeme misto
                if(unoccupiedStart(possiblePlace))  //pokud neni obsazene, pridame ho
                    this.add(possiblePlace);
            }
        else    //vodorovna startline
            for(int i = (int)startLine.getStartX()+ SquareGrid.SQUARE_SIZE; i < startLine.getEndX(); i = i + SquareGrid.SQUARE_SIZE) //na vsechny mista na startline krome kraju
            {
                ExtendedCoordinates<Double> possiblePlace = new ExtendedCoordinates<>(i,(int)startLine.getStartY(), probabilityCoefficient); //zkontrolujeme misto
                if(unoccupiedStart(possiblePlace)) //pokud neni obsazene, pridame ho
                    this.add(possiblePlace);
            }
    }
    
    private boolean unoccupiedStart(Coordinates possiblePlace)
    {
        boolean unoccupied = true;
        for (Car c : AppWindow.getGame().getAllCars())  //kontrolujeme, jestli nejake auto uz nestoji na possiblePlace
        {
            if (c.getXY().minus(c.getSpeed()).isEqual(possiblePlace))
            {
                unoccupied = false;
                break;                
            }
        }
        return unoccupied;
    }
    
    public void draw(boolean absoluteMove)
    {
        AppWindow.getGame().setPlayerTurnText(getCar().getPlayerName() + "'s turn");     //nastav text pro hrace
        for(ExtendedCoordinates<Double> xy : this)  //pro vsechny mozna tahy
        {
            AbstractPossibleMove possibleMove;
            if(xy.getCondition() == 1)    //pokud je blizko, normalni tah
                possibleMove = new PossibleMove(xy, getCar(), absoluteMove);
            else    //jinak drift
                possibleMove = new DriftPossibleMove(xy, getCar(), absoluteMove, xy.getCondition());
            AppWindow.getGame().getActiveLayer().getChildren().add(possibleMove);   //namaluj tah                
        }
    }

    @Override
    public String toString() {
        String s = "PossibleMoves Object: \n";
        for(Coordinates coor : this)
        {
            s += "  Coordinates:" + coor.getX() + ", " +coor.getY() + "\n";
        }
        return s;
    }

    /**
     * @return the car
     */
    public Car getCar() {
        return car;
    }

    /**
     * @return the up
     */
    public int getUp() {
        return up;
    }

    /**
     * @param up the up to set
     */
    public void setUp(int up) {
        this.up = up;
    }

    /**
     * @return the down
     */
    public int getDown() {
        return down;
    }

    /**
     * @param down the down to set
     */
    public void setDown(int down) {
        this.down = down;
    }

    /**
     * @return the left
     */
    public int getLeft() {
        return left;
    }

    /**
     * @param left the left to set
     */
    public void setLeft(int left) {
        this.left = left;
    }

    /**
     * @return the right
     */
    public int getRight() {
        return right;
    }

    /**
     * @param right the right to set
     */
    public void setRight(int right) {
        this.right = right;
    }

    /**
     * @return the defaultProbabilityCoefficient
     */
    public double getDefaultProbabilityCoefficient() {
        return defaultProbabilityCoefficient;
    }

    /**
     * @param defaultProbabilityCoefficient the defaultProbabilityCoefficient to set
     */
    public void setDefaultProbabilityCoefficient(double defaultProbabilityCoefficient) {
        this.defaultProbabilityCoefficient = defaultProbabilityCoefficient;
    }
}

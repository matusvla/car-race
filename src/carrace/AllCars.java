package carrace;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import javafx.scene.paint.Color;

public class AllCars extends ConcurrentLinkedQueue<Car>
{  
    public static int NUMBER_OF_CARS = 2;
    private ArrayList<Result> results = new ArrayList<>();
    
    public AllCars(int numberOfCars)
    {
        NUMBER_OF_CARS = numberOfCars;
        for(int i = 0; i < numberOfCars; i++)
        {
            Car car = new Car();   
            Color carColor = AppWindow.newGameWindow.getPlayerColor(i);
            car.setFill(carColor);
            car.setPlayerName(AppWindow.newGameWindow.getPlayerName(i));
            this.add(car);
        }
    }
    
    public AllCars()
    {
        this(NUMBER_OF_CARS);        
    }  
    
    public String ToString()
    {
        String output = new String();
        for(Car car : this)
        {
            System.out.println(car);
        }    
        return output;
    }
    
    public boolean removeCar(Car car)
    {
        getResults().add(new Result(car.getPlayerName(), car.getMoves()));
        car.getMoves();
        boolean returnStatement = super.remove(car);        
        return returnStatement;
    }

    /**
     * @return the results
     */
    public ArrayList<Result> getResults() {
        return results;
    }
}

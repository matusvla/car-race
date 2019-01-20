package carrace;

import javafx.scene.layout.Pane;

public class AllCarsPane extends Pane
{
    public AllCarsPane(Game game)
    {
        for(Car car : game.getAllCars())
        {
            this.getChildren().add(car);
        }
    }
}

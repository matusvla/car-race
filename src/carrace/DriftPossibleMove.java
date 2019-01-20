package carrace;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class DriftPossibleMove extends AbstractPossibleMove
{
    private double probability;
    
    public DriftPossibleMove(Coordinates xy, Car car, boolean absoluteMove, double probability) {
        super(xy, car, absoluteMove);
        this.probability = probability;
    }    

    @Override
    public void clickevent() {
        double d = Math.random();
        if(d < probability)
            getCar().moveTo((int)this.getCenterX(), (int)this.getCenterY(), isAbsoluteMove());  
        else
            getCar().moveTo();  
    }  

    @Override
    public void setDesign() {
        this.setFill(Color.BLACK);
        this.setOpacity(0.2);        
        this.setStroke(Color.BLACK);        
        this.setOnMousePressed((MouseEvent me) -> {
            clickevent();
            AppWindow.getGame().drawNewPossibleMoves();
        });                
        this.setOnMouseEntered((MouseEvent me) -> {
            this.setOpacity(1);
        });
        
        this.setOnMouseExited((MouseEvent me) -> {
            this.setOpacity(0.2);
        });
    }

    /**
     * @param probability the probability to set
     */
    public void setProbability(double probability) {
        this.probability = probability;
    }
}

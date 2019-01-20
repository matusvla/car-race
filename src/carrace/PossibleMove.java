package carrace;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class PossibleMove extends AbstractPossibleMove
{

    public PossibleMove(Coordinates xy, Car car, boolean absoluteMove) {
        super(xy, car, absoluteMove);
    }

    @Override
    public void clickevent() {
        getCar().moveTo((int)this.getCenterX(), (int)this.getCenterY(), isAbsoluteMove());  
    }

    @Override
    public void setDesign() {
        this.setFill(getCar().getFill());
        this.setOpacity(0.4);
        this.setStroke(Color.BLACK);        
        this.setOnMousePressed((MouseEvent me) -> {
            clickevent();
            AppWindow.getGame().drawNewPossibleMoves();
        });                
        this.setOnMouseEntered((MouseEvent me) -> {
            this.setOpacity(1);
        });
        
        this.setOnMouseExited((MouseEvent me) -> {
            this.setOpacity(0.4);
        });
    }
}

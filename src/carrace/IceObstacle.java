package carrace;

import javafx.scene.paint.Color;

public class IceObstacle extends AbstractObstacle
{

    public IceObstacle(double d, double d1, double d2, double d3) {
        super(d, d1, d2, d3);
    }


    @Override
    public void design() {
        this.setFill(Color.LIGHTSKYBLUE);
        this.setOpacity(0.5);
        this.setStroke(Color.LIGHTSKYBLUE);
    }

    @Override
    public void collision(PossibleMoves movesToModify) {
        movesToModify.setDefaultProbabilityCoefficient(movesToModify.getDefaultProbabilityCoefficient()/2);
    }
}

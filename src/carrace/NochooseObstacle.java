package carrace;

import javafx.scene.paint.Color;

public class NochooseObstacle extends AbstractObstacle {

    
    public NochooseObstacle(double d, double d1, double d2, double d3) {
        super(d, d1, d2, d3);
    }
    
    @Override
    public void collision(PossibleMoves movesToModify) {
        int d = (int)(Math.random() * 3);
        movesToModify.setUp(1-d);
        movesToModify.setDown(movesToModify.getUp());
        d = (int)(Math.random() * 2.999);
        movesToModify.setRight(1-d);
        movesToModify.setLeft(movesToModify.getRight());
    }

    @Override
    public void design() {
        this.setFill(Color.SALMON);
        this.setOpacity(0.5);
        this.setStroke(Color.SALMON);
    }
    
}

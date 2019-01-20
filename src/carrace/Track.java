package carrace;

//import javafx.scene.control.Alert;
import java.util.ArrayList;
import javafx.scene.layout.Pane;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

public class Track extends Pane
{
    private StartLine startLine;
    private FinishLine finishLine;
    private Path allBorders;
    private ArrayList<AbstractObstacle> allObstacles = new ArrayList<>();
    private int numberOfPlayers;
    private TrackDescription trackDescription;
    
    public Track(int id)
    {
        try{
            this.trackDescription = new TrackDescription(AppWindow.ALL_TRACKS_DATABASE.get(id));
        }
        catch(IndexOutOfBoundsException ex)
        {
            /*Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Track database empty, of not found. Run track editor, if you want to create a new track.");
            alert.setTitle("Database problem");
            alert.showAndWait();*/
            return;
        }

        startLine = new StartLine(new Coordinates(trackDescription.getStartLineDescription()[0], trackDescription.getStartLineDescription()[1]), trackDescription.getStartLineDescription()[2], trackDescription.getStartLineDescription()[3]);
        finishLine = new FinishLine(trackDescription.getFinishLineDescription()[0], trackDescription.getFinishLineDescription()[1], trackDescription.getFinishLineDescription()[2], trackDescription.getFinishLineDescription()[3]);
        numberOfPlayers = trackDescription.getNumberOfPlayers();
        allBorders = new Path();
        
        if(!trackDescription.getBorderDescription().isEmpty())
        {
            ExtendedCoordinates<Boolean> xy = trackDescription.getBorderDescription().remove(0); 
            allBorders.getElements().add(new MoveTo(xy.getX(), xy.getY()));

            while(!trackDescription.getBorderDescription().isEmpty())
            {         
                xy = trackDescription.getBorderDescription().remove(0);
                if(xy.getCondition())
                    allBorders.getElements().add(new LineTo(xy.getX(), xy.getY()));
                else
                    allBorders.getElements().add(new MoveTo(xy.getX(), xy.getY()));
            }
        }  

        while(!trackDescription.getObstacleDescription().isEmpty())
        {   
            ExtendedCoordinates<Integer> startXY = trackDescription.getObstacleDescription().remove(0); 
            ExtendedCoordinates<Integer> endXY = trackDescription.getObstacleDescription().remove(0); 
            if(startXY.getCondition() == 1)
                allObstacles.add(new IceObstacle(startXY.getX(), startXY.getY(), endXY.getX(), endXY.getY()));
            else
                allObstacles.add(new IceObstacle(startXY.getX(), startXY.getY(), endXY.getX(), endXY.getY()));
        }
        
        //okrajovy ramecek
        allBorders.getElements().add(new MoveTo(0, 0)); //okrajovy ramecek
        allBorders.getElements().add(new LineTo(SquareGrid.WINDOW_WIDTH, 0));
        allBorders.getElements().add(new LineTo(SquareGrid.WINDOW_WIDTH, SquareGrid.WINDOW_HEIGHT));
        allBorders.getElements().add(new LineTo(0, SquareGrid.WINDOW_HEIGHT));
        allBorders.getElements().add(new LineTo(0, 0));
        
        this.getChildren().addAll(startLine, finishLine, allBorders);
        this.getChildren().addAll(allObstacles);
    }    

    public Path getAllBorders() {
        return allBorders;
    }

    public StartLine getStartLine() {
        return startLine;
    }

    public FinishLine getFinishLine() {
        return finishLine;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }
    
    public TrackDescription getTrackDescription() {
        return trackDescription;
    }

    /**
     * @return the allObstacles
     */
    public ArrayList<AbstractObstacle> getAllObstacles() {
        return allObstacles;
    }
}
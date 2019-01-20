
package carrace;

import java.util.ArrayList;

public class TrackDescription
{
    private int trackID;
    private String trackName;
    private int numberOfPlayers;        
    private int[] startLineDescription = new int[4];
    private int[] finishLineDescription = new int[4];
    private Result bestScore;
    private ArrayList<ExtendedCoordinates<Boolean>> borderDescription = new ArrayList<>();
    private ArrayList<ExtendedCoordinates<Integer>> obstacleDescription = new ArrayList<>();
    private ArrayList<Result> resultList = new ArrayList<>();

    public TrackDescription(){
    }
    
    public TrackDescription(TrackDescription copy) //kopirovaci konstruktor
    {
        this.trackID = copy.trackID;
        this.trackName = copy.trackName;
        this.numberOfPlayers = copy.numberOfPlayers;
        for(int i = 0; i < 4; i++)
        {
            this.startLineDescription[i] = copy.startLineDescription[i];
            this.finishLineDescription[i] = copy.finishLineDescription[i];
        }
        for(ExtendedCoordinates ex : copy.getBorderDescription())
            this.borderDescription.add(ex);
        for(Result res : copy.resultList)
            this.resultList.add(res);
        for(ExtendedCoordinates ex : copy.getObstacleDescription())
            this.obstacleDescription.add(ex);
    }   

    public ArrayList<ExtendedCoordinates<Boolean>> getBorderDescription() {
        return borderDescription;
    }

    public void addBorderDescription(ExtendedCoordinates oneBorderDescription) {
        getBorderDescription().add(oneBorderDescription);
    }
    
    public void addObstacleDescription(ExtendedCoordinates oneObstacleDescription) {
        getObstacleDescription().add(oneObstacleDescription);
    }

    public ArrayList<Result> getResultList() {
        return resultList;
    }

    public void setResultList(ArrayList<Result> aResultList) {
        resultList = aResultList;
    }
    
    public int getTrackID() {
        return trackID;
    }

    public void setTrackID(int trackID) {
        this.trackID = trackID;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }
    
    public void setNumberOfPlayers(StartLine startLine) {
        this.numberOfPlayers = startLine.getLength()/SquareGrid.SQUARE_SIZE - 1;
    }

    public int[] getStartLineDescription() {
        return startLineDescription;
    }

    public void setStartLineDescription(int x, int y, int length, int orientation) {        
        this.startLineDescription = new int[]{x, y, length, orientation};
    }
    
    public void setStartLineDescription(StartLine startLine) {
        
        this.startLineDescription = new int[]{(int)startLine.getStartX()/SquareGrid.SQUARE_SIZE,
                                              (int)startLine.getStartY()/SquareGrid.SQUARE_SIZE,
                                              (int)startLine.getLength()/SquareGrid.SQUARE_SIZE,
                                              startLine.getOrientation()};
    }

    public int[] getFinishLineDescription() {
        return finishLineDescription;
    }

    public void setFinishLineDescription(int sx, int sy, int ex, int ey) {
        this.finishLineDescription = new int[]{sx, sy, ex, ey};
    }
    
    public void setFinishLineDescription(FinishLine finishLine) {
        
        this.finishLineDescription = new int[]{(int)finishLine.getStartX()/SquareGrid.SQUARE_SIZE,
                                               (int)finishLine.getStartY()/SquareGrid.SQUARE_SIZE,
                                               (int)finishLine.getEndX()/SquareGrid.SQUARE_SIZE,
                                               (int)finishLine.getEndY()/SquareGrid.SQUARE_SIZE};
    }

    @Override
    public String toString() {
        return getTrackName() + " (max. " + numberOfPlayers + " players)";
    }

    public Result getBestScore() {
        return bestScore;
    }

    public void setBestScore(Result bestScore) {
        this.bestScore = bestScore;
    }

    public void setBorderDescription(ArrayList<ExtendedCoordinates<Boolean>> borderDescription) {
        this.borderDescription = borderDescription;
    }

    public ArrayList<ExtendedCoordinates<Integer>> getObstacleDescription() {
        return obstacleDescription;
    }

    public void setObstacleList(ArrayList<ExtendedCoordinates<Integer>> obstacleDescription) {
        this.obstacleDescription = obstacleDescription;
    }
    
    public void setObstacleDescriptions(ArrayList<AbstractObstacle> obstacleDescription) {
        for(AbstractObstacle abstObst : obstacleDescription)
        {
            ExtendedCoordinates extStart, extEnd;
            if(abstObst instanceof IceObstacle)
            {
                extStart = new ExtendedCoordinates((int)abstObst.getX(), (int)abstObst.getY(), 1);
                extEnd = new ExtendedCoordinates((int)(abstObst.getWidth()), (int)(abstObst.getHeight()), 1);
            }
            else
            {
                extStart = new ExtendedCoordinates((int)abstObst.getX(), (int)abstObst.getY(), 1);
                extEnd = new ExtendedCoordinates((int)(abstObst.getWidth()), (int)(abstObst.getHeight()), 1);
            }
            this.obstacleDescription.add(extStart);
            this.obstacleDescription.add(extEnd);
        }
    }
}

package carrace;

import java.util.NoSuchElementException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Game extends StackPane
{
    private PossibleMoves possibleMoves;
    public static AllCars allCars;
    private Pane activeLayer;
    private StartLine startLine;
    private FinishLine finishLine;
    private int activeCars = 1;
    private Track track;
    private SquareGrid squareGrid;
    private AllCarsPane allCarsPane;
    private int numberOfPlayers;
    private Label playerTurn = new Label();

    public Game(int id, int numberOfPlayers)
    {
        this.numberOfPlayers = numberOfPlayers;
        track = new Track(id);
        startLine = track.getStartLine();
        finishLine = track.getFinishLine();
        squareGrid = new SquareGrid();
        activeLayer = new Pane();
        this.getChildren().addAll(squareGrid);
                
        Label gameLabel = new Label("CAR RACE");
        Label gameVersion = new Label("version 1.0");
        Label gameAuthor = new Label("(c) Vladislav Matúš");
        gameLabel.setFont(Font.font(90));
        gameVersion.setFont(Font.font(20));
        VBox gameName = new VBox(gameLabel, gameVersion, gameAuthor);
        gameName.setAlignment(Pos.CENTER);
        this.getChildren().add(gameName);
    }   
    
    public Game(Game game)
    {
        this.getChildren().addAll(squareGrid);
        this.track = game.track;
        this.startLine = game.startLine;
        this.finishLine = game.finishLine;
        this.squareGrid = game.squareGrid;
        this.activeLayer = game.activeLayer;
        this.allCars = game.allCars;
        this.allCarsPane = game.allCarsPane;
    }
        
    public void startNewGame()
    {
        allCars = new AllCars(numberOfPlayers);        
        allCarsPane = new AllCarsPane(this);
        activeLayer.getChildren().clear(); 
        startLine.addPossibleMoves();
        Car.allTraces.getChildren().clear();
        this.getChildren().clear();        
        this.getChildren().addAll(squareGrid, track, Car.getAllTraces(), getPlayerTurn(), allCarsPane, activeLayer);
        this.playerTurn.setFont(Font.font(squareGrid.SQUARE_SIZE));         
        this.playerTurn.setTranslateY(- SquareGrid.WINDOW_HEIGHT/2 + SquareGrid.SQUARE_SIZE);
    }

    
    public void drawNewPossibleMoves()
    {
        getActiveLayer().getChildren().clear();
        Car car;
        try{
                car = getAllCars().remove();
                getAllCars().add(car);
        }
        catch(NoSuchElementException ex)
        {
            EndGameWindow endGameWindow = new EndGameWindow();
            return;
        }        
        
        if(activeCars == AllCars.NUMBER_OF_CARS)  //doplneni prvniho tahu pro posledni auticko
        {
            car.firstMove(startLine);
            car = getAllCars().remove();
            getAllCars().add(car);
            getTrack().getAllBorders().getElements().add(new MoveTo(startLine.getStartX(),startLine.getStartY()));
            getTrack().getAllBorders().getElements().add(new LineTo(startLine.getEndX(),startLine.getEndY()));
        }
        
        if(activeCars >= AllCars.NUMBER_OF_CARS)  //vsechny auta uz odstarovaly
        {               
            setPossibleMoves(new PossibleMoves(car));
            try{ 
                getPossibleMoves().draw(true);
            }  //v situaci, kdy je auto zablokovane, nehraje
            catch(NullPointerException ex){
                car.setMoves(car.getMoves()+1);
                drawNewPossibleMoves();
            }
            catch(IndexOutOfBoundsException ex){
                car.moveTo(car.getXY().getX()+car.getSpeed().getX(), car.getXY().getY()+car.getSpeed().getY(), true);
                drawNewPossibleMoves();
            }
        }
        else  //prvni tah ze startu
        {   
            car.firstMove(startLine);
            startLine.addPossibleMoves();
        }
        activeCars++;
    }

    public AllCars getAllCars() {
        return allCars;
    }

    public Track getTrack() {
        return track;
    }

    public Pane getActiveLayer() {
        return activeLayer;
    }    

    public void setPossibleMoves(PossibleMoves possibleMoves) {
        this.possibleMoves = possibleMoves;
    }

    public PossibleMoves getPossibleMoves() throws NullPointerException{
        if(possibleMoves.isEmpty())
            throw new NullPointerException();  //vsechny mozne tahy zablokovane
        
        boolean isInPane = false;
        for(Coordinates xy : possibleMoves)
        {
            if(xy.getX() >= 0 && xy.getY() >= 0 && xy.getX() <= SquareGrid.WINDOW_WIDTH && xy.getY() <= SquareGrid.WINDOW_HEIGHT)
                isInPane = true;
        }
        if(!isInPane)
            throw new IndexOutOfBoundsException(); //vsechny tahy jsou mimo obrazovku
        return possibleMoves;        
    }

    public void setPlayerTurnText(String playerTurn) {
        this.getPlayerTurn().setText(playerTurn);
    }
    
    public Label getPlayerTurn() {
        return playerTurn;
    }
}

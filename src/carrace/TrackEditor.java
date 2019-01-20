package carrace;

import java.util.ArrayList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TrackEditor extends Stage
{
    TrackDescription editedTrackDescription = new TrackDescription();
    int startX, startY;
    Path path;
    LineTo line;
    MoveTo move;
    StartLine startLine = new StartLine();
    FinishLine finishLine;
    ArrayList<AbstractObstacle> allObstacles = new ArrayList<>();
    boolean drawingStartLine = false;
    boolean drawingEndLine = false;
    boolean drawingObstacle = false;
    boolean settingOrientation = false;
    CheckBox snapToGrid;  
    TextField trackNameField = new TextField();
    ComboBox comboBox;
    
    public TrackEditor()
    {        
        path = new Path();        
        this.setTitle("Track Editor");
        this.setResizable(false);   
        
        ObservableList<String> options = 
            FXCollections.observableArrayList(
            "Ice Obstacle",
            "Hell Obstacle"
        );
        comboBox = new ComboBox(options);
        comboBox.setValue(options.get(0));
        
        
        VBox vBox = new VBox();
        StackPane stackPane = new StackPane();
        SquareGrid squareGrid = new SquareGrid();
        Pane drawingPane = new Pane();
        Pane activePane = new Pane();
        stackPane.getChildren().addAll(squareGrid, drawingPane, activePane);        
        drawingPane.getChildren().add(path);
        activePane.setOnMousePressed((MouseEvent me) -> {
            if(!settingOrientation)
            {
                startX = roundToSquares(me.getX());
                startY = roundToSquares(me.getY());
                if(!drawingStartLine && !drawingEndLine)
                {
                    move = new MoveTo(startX, startY);            
                    path.getElements().add(move);
                }
            }
        });
        activePane.setOnMouseReleased((MouseEvent me) -> {
            if(settingOrientation)
            {
                settingOrientation = false;
                activePane.getChildren().clear();
                return;
            }
            int endX = roundToSquares(me.getX());
            int endY = roundToSquares(me.getY());
            if(drawingEndLine)
            {
                drawingPane.getChildren().remove(finishLine);                
                finishLine = new FinishLine(startX / SquareGrid.SQUARE_SIZE, startY/SquareGrid.SQUARE_SIZE, endX/SquareGrid.SQUARE_SIZE, endY/SquareGrid.SQUARE_SIZE);  
                drawingPane.getChildren().add(finishLine);
            }            
            else if(drawingStartLine)
            {
                drawingPane.getChildren().remove(startLine);   
                int differenceX = Math.abs(startX-endX);
                int differenceY = Math.abs(startY-endY);
                startLine = new StartLine(new Coordinates(roundToSquares(startX)/squareGrid.SQUARE_SIZE, roundToSquares(startY)/squareGrid.SQUARE_SIZE),
                                            Math.max(differenceX,differenceY)/squareGrid.SQUARE_SIZE
                                            ,(differenceX > differenceY) ? 1 : 0);
                startLine.setStroke(Color.RED);
                settingOrientation = true;
                drawingPane.getChildren().add(startLine);
            }
            else if(drawingObstacle)
            {
                AbstractObstacle obstacle;
                switch(options.indexOf(comboBox.getValue()))
                {
                    case 0:
                        obstacle = new IceObstacle(startX, startY, endX - startX, endY - startY);
                        break;
                    default:
                        obstacle = new NochooseObstacle(startX, startY, endX - startX, endY - startY);                        
                }
                
                allObstacles.add(obstacle);
                drawingPane.getChildren().add(obstacle);
            }            
            else
            {
                line = new LineTo(endX, endY);                 
                path.getElements().add(line);
            }
            activePane.getChildren().clear();
        });
        activePane.setOnMouseDragged((MouseEvent me) -> {   
            if(settingOrientation)
                return;
            activePane.getChildren().clear();
            Shape temporaryShape;
            if(drawingObstacle)
                temporaryShape = new Rectangle(startX, startY, roundToSquares(me.getX()) - startX, roundToSquares(me.getY()) - startY);
            else
                temporaryShape = new Line(startX, startY, roundToSquares(me.getX()), roundToSquares(me.getY()));
            activePane.getChildren().add(temporaryShape);
        });   
        activePane.setOnMouseMoved((MouseEvent me) -> {
            if(settingOrientation)
            {
                activePane.getChildren().clear();
                int orientation = startLine.getOrientation();
                int cX = startLine.getCenterX();
                int cY = startLine.getCenterY();
                Path arrow = new Path();
                arrow.getElements().add(new MoveTo(cX, cY));
                if(orientation == 0 || orientation == 2)
                {
                    if(cX - me.getX() != 0)
                    {
                        double direction = (cX - me.getX())/Math.abs(cX - me.getX());
                        arrow.getElements().add(new LineTo(cX - direction * 2* SquareGrid.SQUARE_SIZE, cY));
                        arrow.getElements().add(new LineTo(cX - direction * 2* SquareGrid.SQUARE_SIZE + direction * 10, cY + direction * 10));
                        arrow.getElements().add(new MoveTo(cX - direction * 2* SquareGrid.SQUARE_SIZE, cY));
                        arrow.getElements().add(new LineTo(cX - direction * 2* SquareGrid.SQUARE_SIZE + direction * 10, cY - direction * 10));
                        startLine.setOrientation((int)(1 - direction));
                    }
                }
                else
                {
                    if(cY - me.getY() != 0)
                    {
                        double direction = (cY - me.getY())/Math.abs(cY - me.getY());
                        arrow.getElements().add(new LineTo(cX, cY - direction * 2* SquareGrid.SQUARE_SIZE));
                        arrow.getElements().add(new LineTo(cX + direction * 10, cY - direction * 2* SquareGrid.SQUARE_SIZE + direction * 10));
                        arrow.getElements().add(new MoveTo(cX, cY - direction * 2* SquareGrid.SQUARE_SIZE));
                        arrow.getElements().add(new LineTo(cX - direction * 10, cY - direction * 2* SquareGrid.SQUARE_SIZE + direction * 10));
                        startLine.setOrientation((int)(2 - direction));
                    }
                }
                activePane.getChildren().add(arrow);
            }
        });        
                
        ToggleButton startLineButton = new ToggleButton("Startline");
        startLineButton.setOnAction((ActionEvent e) -> {
            drawingEndLine = false;
            drawingStartLine = true;
            drawingObstacle = false;
        });
        
        ToggleButton finishLineButton = new ToggleButton("Endline");
        finishLineButton.setOnAction((ActionEvent e) -> {
            drawingEndLine = true;
            drawingStartLine = false;
            drawingObstacle = false;
        });
        
        ToggleButton bordersButton = new ToggleButton("Borders");
        bordersButton.setOnAction((ActionEvent e) -> {
            drawingEndLine = false;
            drawingStartLine = false;
            drawingObstacle = false;
        });
        
        ToggleButton obstacleButton = new ToggleButton("Obstacles");
        obstacleButton.setOnAction((ActionEvent e) -> {
            drawingEndLine = false;
            drawingStartLine = false;
            drawingObstacle = true;
        });
        
        bordersButton.setSelected(true);
        
        ToggleGroup toggleGroup = new ToggleGroup();
            toggleGroup.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> ov, Toggle toggle, Toggle new_toggle) -> {
                if(new_toggle == null)
                    bordersButton.setSelected(true);
        });
        startLineButton.setToggleGroup(toggleGroup);
        finishLineButton.setToggleGroup(toggleGroup);
        bordersButton.setToggleGroup(toggleGroup);
        obstacleButton.setToggleGroup(toggleGroup);
        
        Button exportButton = new Button("Export Track");
        exportButton.setOnAction((ActionEvent e) -> {
            if(!this.validate())
                return;
            this.export();
            this.close();
        });
        
        trackNameField.lengthProperty().addListener(new ChangeListener<Number>() { //from http://stackoverflow.com/questions/22714268/how-to-limit-the-amount-of-characters-a-javafx-textfield
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        if (newValue.intValue() > oldValue.intValue())
                            if (trackNameField.getText().length() >= 20)
                                trackNameField.setText(trackNameField.getText().substring(0, 20));
                    }
            });    
        snapToGrid = new CheckBox("Snap to grid (Borders)");
        snapToGrid.setSelected(true);
                
        HBox buttonBox = new HBox(startLineButton, finishLineButton, obstacleButton, bordersButton, snapToGrid, comboBox);
        buttonBox.setSpacing(10);
        buttonBox.setPadding(new Insets(10, 10, 10, 10));
        buttonBox.setAlignment(Pos.CENTER_LEFT);
        
        Label label = new Label("Name of track: ");
        HBox exportBox = new HBox(label, trackNameField, exportButton);
        exportBox.setSpacing(10);
        exportBox.setPadding(new Insets(10, 10, 10, 10));
        exportBox.setAlignment(Pos.CENTER_LEFT);
        
        vBox.getChildren().addAll(buttonBox, stackPane, exportBox);
        
        Scene scene = new Scene(vBox);       
                
        this.setScene(scene);
        this.initModality(Modality.APPLICATION_MODAL);       
        this.sizeToScene();
        this.show();
    }    
    
    private boolean validate()
    {        
        if(startLine == null)
        if(startLine.getLength() < SquareGrid.SQUARE_SIZE * 2)
            return false;
        if(finishLine == null)
            return false;
        Shape intersection = Shape.intersect(startLine,finishLine);        
        if(intersection.getBoundsInLocal().getWidth() != -1)
            return false;
        if(trackNameField.getText() == "")
            return false;
        return true;
    }
    
    private void export()
    {
        editedTrackDescription.setStartLineDescription(startLine);
        editedTrackDescription.setFinishLineDescription(finishLine);
        editedTrackDescription.setNumberOfPlayers(startLine);
        editedTrackDescription.setObstacleDescriptions(allObstacles);      
        ArrayList<ExtendedCoordinates<Boolean>> borderDescription = new ArrayList<>();        
        ExtendedCoordinates exc;
        ArrayList<PathElement> pathElementsList = new ArrayList<>(); //aby program nehazel varovani "no initial MoveTo in Path"
        pathElementsList.addAll(path.getElements());
        while(!pathElementsList.isEmpty())
        {
            PathElement pe = pathElementsList.remove(0);
           
            if(pe.getClass().equals(LineTo.class))
            {
                LineTo peLineTo = (LineTo)pe;
                exc = new ExtendedCoordinates((int)peLineTo.getX(), (int)peLineTo.getY(), true);
            }
            else //MoveTo
            {
                MoveTo peMoveTo = (MoveTo)pe;
                exc = new ExtendedCoordinates((int)peMoveTo.getX(), (int)peMoveTo.getY(), false);
            }      
            borderDescription.add(exc);
        }
        editedTrackDescription.setBorderDescription(borderDescription);
        editedTrackDescription.setTrackName(trackNameField.getText());
        editedTrackDescription.setTrackID(AppWindow.ALL_TRACKS_DATABASE.size());
        AppWindow.ALL_TRACKS_DATABASE.add(editedTrackDescription);
    }
            
    private int roundToSquares(double position)
    {
        double lastSquarePlace = 0;
        if(snapToGrid.isSelected())
        {
            lastSquarePlace = position % SquareGrid.SQUARE_SIZE;
            position -= lastSquarePlace;
            lastSquarePlace /= SquareGrid.SQUARE_SIZE;
            lastSquarePlace = Math.round(lastSquarePlace);
            lastSquarePlace *= SquareGrid.SQUARE_SIZE;
        }
        return (int)(lastSquarePlace + position);
    }
}

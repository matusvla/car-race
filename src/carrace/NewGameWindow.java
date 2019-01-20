package carrace;

import java.util.ArrayList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
//import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class NewGameWindow extends Stage
{
    private ArrayList<PlayerProperities> playerProperities;
    
    public NewGameWindow()
    {
        PlayerProperities.playerID = 0;
        this.setTitle("New Game");
        this.setResizable(false);   
        
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(25, 25, 25, 25));
        vBox.setSpacing(5);
        
        Scene scene = new Scene(vBox);
        
        VBox playerProperitiesBox = new VBox();
        playerProperitiesBox.setAlignment(Pos.CENTER);
        playerProperities = new ArrayList<>();
                             
        ChoiceBox<TrackDescription> choiceBox = new ChoiceBox<>();
        addItems(choiceBox);         
        choiceBox.getSelectionModel().selectFirst();
        addPlayerNames(playerProperitiesBox, choiceBox.getValue().getNumberOfPlayers());
        
        HBox numberOfPlayersBox = new HBox();
        TextField numberOfPlayers = new TextField();
        numberOfPlayers.setPrefColumnCount(2);
        numberOfPlayers.setText("" + choiceBox.getValue().getNumberOfPlayers());
        Text numberOfPlayersLabel = new Text("Number of players: ");
        numberOfPlayersBox.setAlignment(Pos.CENTER);
        numberOfPlayers.setOnKeyReleased((KeyEvent e) -> {
            try{
                int count = Integer.parseInt(numberOfPlayers.getText()); //muze hodit vyjimky, proto try-catch
                if(count > 0 && count <= choiceBox.getValue().getNumberOfPlayers())
                {
                    addPlayerNames(playerProperitiesBox, count);
                    scene.setRoot(vBox);
                    this.sizeToScene();
                }
            }catch(NumberFormatException ex){};
        });
        numberOfPlayersBox.getChildren().addAll(numberOfPlayersLabel, numberOfPlayers);                

        /*choiceBox.setOnAction((ActionEvent e) -> {
            int count = choiceBox.getValue().getNumberOfPlayers();
            numberOfPlayers.setText("" + count);
            addPlayerNames(playerProperitiesBox, count);
            scene.setRoot(vBox);
            this.sizeToScene();
        });*/

        
        Button submit = new Button("Start game");
        submit.setOnAction((ActionEvent e) -> {
            /*Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Problem occured");
            alert.setContentText("Invalid number of players for the selected track!");*/
                        
            try{
                int id = choiceBox.getValue().getTrackID();
                int nop = Integer.parseInt(numberOfPlayers.getText());
                if(nop <= choiceBox.getValue().getNumberOfPlayers() && nop > 0)
                {
                    AppWindow.setGame(id, nop);
                    this.close();
                }
                else
                    /*alert.showAndWait()*/;
            }
            catch(NumberFormatException ex){
                //alert.showAndWait();
                return;
            }            
        });
                
        vBox.getChildren().addAll(choiceBox, numberOfPlayersBox, playerProperitiesBox, submit);
        
        this.setScene(scene);
        this.initModality(Modality.APPLICATION_MODAL);       
        this.sizeToScene();
        this.show();
    }    
    
    private void addPlayerNames(VBox playerProperitiesBox, int count)
    {
        playerProperitiesBox.getChildren().clear();
        int difference = getPlayerProperities().size() - count;
        for (int i = 0; i < Math.abs(difference); i++)
        {
            if (difference > 0)
            {
                getPlayerProperities().remove(getPlayerProperities().size()-1);
                PlayerProperities.playerID--;
            }
            else
                getPlayerProperities().add(new PlayerProperities());                
        }       
        playerProperitiesBox.getChildren().addAll(getPlayerProperities());
    }
    
    private void addItems (ChoiceBox choiceBox)
    {
        for(TrackDescription trackDescription : AppWindow.ALL_TRACKS_DATABASE)   
            choiceBox.getItems().add(trackDescription);
    }

    public ArrayList<PlayerProperities> getPlayerProperities() {
        return playerProperities;
    }
        
    public String getPlayerName(int index)
    {
        return playerProperities.get(index).getPlayerName().getText();
    }
    
    public Color getPlayerColor(int index)
    {
        return playerProperities.get(index).getPlayerColor().getValue();
    }

    private static class PlayerProperities extends HBox
    {
        private static final ArrayList<Color> defaultCarColors = new ArrayList<>();
        static
        {
            defaultCarColors.add(Color.RED);
            defaultCarColors.add(Color.BLUE);
            defaultCarColors.add(Color.GREEN);
            defaultCarColors.add(Color.CORAL);
            defaultCarColors.add(Color.CHARTREUSE); 
            defaultCarColors.add(Color.BROWN);
            defaultCarColors.add(Color.VIOLET);
            defaultCarColors.add(Color.DARKOLIVEGREEN);     
            defaultCarColors.add(Color.SKYBLUE);
        }
        public static int playerID = 0;
        private TextField playerName = new TextField();
        private ColorPicker playerColor;

        public PlayerProperities()
        {
            playerName.lengthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
                if (newValue.intValue() > oldValue.intValue()) {
                    if (getPlayerName().getText().length() >= 20) {
                        getPlayerName().setText(getPlayerName().getText().substring(0, 20));
                    }
                }
            } //from http://stackoverflow.com/questions/22714268/how-to-limit-the-amount-of-characters-a-javafx-textfield
            );               
            playerColor = new ColorPicker(defaultCarColors.get(playerID%defaultCarColors.size()));
            playerColor.setStyle("-fx-color-label-visible: false ;");            
            this.getChildren().addAll(playerName, playerColor);
            playerID++;
            playerName.setText("Player " + playerID);
        }        

        public void setText(String string)
        {
            getPlayerName().setText(string);
        }

        public TextField getPlayerName() {
            return playerName;
        }

        public ColorPicker getPlayerColor() {
            return playerColor;
        }
    }
}

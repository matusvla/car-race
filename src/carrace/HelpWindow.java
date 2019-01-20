package carrace;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class HelpWindow extends Stage
{    
    public HelpWindow()
    {
        this.setTitle("Help");
        this.setResizable(false);   
        
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(25, 25, 25, 25));
        vBox.setSpacing(5);
        vBox.setMaxWidth(300);
        vBox.setMinHeight(500);
        
        Scene scene = new Scene(vBox);
        Label label = new Label("Help");
        label.setFont(Font.font(20));
        
        Label gameInfo = new Label(
                "Car Race is a multiplayer game, which imitates a classic car race game on a square paper."
              + "The main goal of this game is to drive your car from start to finish faster than the other players."
              + "You can run a new game in the game menu, set track, number of players, name them, choose the color of their car and run the game."
              + "Various settings from the Settings tab are described below."
              + "You can also edit new track in Track Editor menu and play them with friends, or delete your track using track manager from the same menu."
            );        
        gameInfo.setWrapText(true);
        gameInfo.setTextAlignment(TextAlignment.JUSTIFY);
        Label gameplayInfo = new Label(
                "At the beginning of a game every player chooses a place on the start line, where he wants to start. His first move is then made automatically."
                        + "Next moves can be made as follows: Previous move is coppied and the move can be made in the same manner, or to the nearest 8 squares."
                        + "In the case of collision your car is moved back to the track and you can't play for a number of turns specified in the settings menu."
                        + "You can choose choose from four mariants: 0, 1, 2, and proportional number of turns, where proportional maens that the number of turns"
                        + "you must wait equals your speed at the time of the collision."
            );
        gameplayInfo.setWrapText(true);
        gameplayInfo.setTextAlignment(TextAlignment.JUSTIFY);
        
        Label driftInfo = new Label(
                "It is possible to turn drifts on or off in the settings menu. This adds more possible moves for the player."
                        + "However, this moves have lower pobability to be succesfuly complete, the further it is from center, the lower the probability."
                        + "When a drift is not succesful, the car continues in the direction it was going."
            );
        driftInfo.setWrapText(true);
        driftInfo.setTextAlignment(TextAlignment.JUSTIFY);
        
        Label editorInfo = new Label(
                "Each track created in the editor must have a startline and a finishline, which automatically snap to grid."
                        + "When creating a start line, user has to choose startline position and direction of first move."
                        + "Number of players of a track is counted automatically and is equal to length of start line minus one."
                        + "When editing is done, you can name your tracks and export it."
            );
        editorInfo.setWrapText(true);
        editorInfo.setTextAlignment(TextAlignment.JUSTIFY);
        
        Label troubleshootingInfo = new Label(
                "When it is not possible run a new game, you have an empty or not accessible database of tracks."
                        + "In this case you have to create a new track to play."
            );
        troubleshootingInfo.setWrapText(true);
        troubleshootingInfo.setTextAlignment(TextAlignment.JUSTIFY);
        
        TitledPane gameInfoPane = new TitledPane("Game info", gameInfo);
        gameInfoPane.setMinHeight(300);        
        
        TitledPane gameplayInfoPane = new TitledPane("Game rules", gameplayInfo);
        gameplayInfoPane.setExpanded(false);
        gameplayInfoPane.setMinHeight(300);
        
        TitledPane driftInfoPane = new TitledPane("Drift", driftInfo);
        driftInfoPane.setExpanded(false);
        driftInfoPane.setMinHeight(300);
        
        TitledPane editorInfoPane = new TitledPane("Editor", editorInfo);
        editorInfoPane.setExpanded(false);
        editorInfoPane.setMinHeight(300);
        
        TitledPane troubleshootingInfoPane = new TitledPane("Troubleshooting", troubleshootingInfo);
        troubleshootingInfoPane.setExpanded(false);
        troubleshootingInfoPane.setMinHeight(300);
                
        Accordion accordion = new Accordion();
        accordion.getPanes().addAll(gameInfoPane, gameplayInfoPane, driftInfoPane, editorInfoPane, troubleshootingInfoPane);
        vBox.getChildren().addAll(label,accordion);
                
        this.setScene(scene);
        this.initModality(Modality.APPLICATION_MODAL);       
        this.show();
    }    
}

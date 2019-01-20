package carrace;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class EndGameWindow extends Stage
{
    public EndGameWindow()
    {
        this.setResizable(false);
        this.setTitle("Final Score");
        VBox vBox = new VBox();
        GridPane gridPane = new GridPane();
        Label title = new Label("Score Board");
        title.setFont(Font.font(25));
        
        int i = 1; 
               
        for(Result result : AppWindow.getGame().getAllCars().getResults())
        {
            Label scoreBoardPlayerItem = new Label(result.getName());
            Label scoreBoardScoreItem = new Label(Integer.toString(result.getScore()));  
            scoreBoardPlayerItem.setFont(Font.font(15));
            scoreBoardScoreItem.setFont(Font.font(15));
            gridPane.add(scoreBoardPlayerItem, 0, i);
            gridPane.add(scoreBoardScoreItem, 1, i);
            gridPane.setValignment(scoreBoardPlayerItem, VPos.CENTER);
            gridPane.setValignment(scoreBoardScoreItem, VPos.CENTER);
            i++;
        }       
        
        /*Button close = new Button("Close");                    
        close.setOnAction((ActionEvent e) -> {            
            this.close();
        });*/
        gridPane.setHgap(50); //horizontal gap in pixels => that's what you are asking for
        gridPane.setVgap(10); //vertical gap in pixels
        
        vBox.getChildren().addAll(title, gridPane);
        vBox.setAlignment(Pos.CENTER);        
        vBox.setPadding(new Insets(30, 30, 30, 30));
        vBox.setSpacing(30);
        
        Scene scene = new Scene(vBox);
        this.setScene(scene);
        this.initModality(Modality.APPLICATION_MODAL);       
        this.show();  
        this.sizeToScene();
    }    
}

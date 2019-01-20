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

public class AboutWindow extends Stage
{    
    public AboutWindow()
    {
        this.setTitle("About");
        this.setResizable(false);   
        
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(25, 25, 25, 25));
        vBox.setSpacing(5);
        vBox.setMaxWidth(300);
        
        Scene scene = new Scene(vBox);
        Label label = new Label("About");
        label.setFont(Font.font(20));
        
        Label description = new Label("CAR RACE version 1.0 \n Freeware \n (c) Vladislav Matus \n 2016");
        
        vBox.getChildren().addAll(label,description);
                
        this.setScene(scene);
        this.initModality(Modality.APPLICATION_MODAL);       
        this.show();
    }    
}


package carrace;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TrackManager extends Stage
{
    private final ObservableList<TrackDescription> data = FXCollections.observableArrayList();

    public TrackManager()
    {        
        TableView<TrackDescription> tableView = new TableView<>();
        tableView.setPlaceholder(new Label("No tracks available"));
        for(TrackDescription trackDescription : AppWindow.ALL_TRACKS_DATABASE)  //pridej vsechny data do tabulky
            data.add(trackDescription);
        tableView.setItems(data);   //a nastav data v tabulce
        TableColumn firstColumn = new TableColumn("Track Name");  //popisky a nastaveni obsahu       
        firstColumn.setCellValueFactory(
                new PropertyValueFactory<>("trackName"));
        TableColumn secondColumn = new TableColumn("Players");
        secondColumn.setCellValueFactory(
                new PropertyValueFactory<>("numberOfPlayers"));
        
        tableView.getColumns().addAll(firstColumn, secondColumn);   //pridani sloupcu     
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);   //aby sloupce vyplnovaly celou tabulku
        Button delete = new Button("Delete track"); //mazaci tlacitko
        delete.setOnAction((ActionEvent e) -> {
            for(TrackDescription deletedTrack : tableView.getSelectionModel().getSelectedItems())
            {
                AppWindow.ALL_TRACKS_DATABASE.remove(deletedTrack);
                data.remove(deletedTrack);
            }            
        });
        Label label = new Label("Track Manager");   //titulek
        label.setFont(Font.font(20));
        VBox vBox = new VBox(label,tableView,delete);
        vBox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vBox);       
                
        this.setScene(scene);
        this.initModality(Modality.APPLICATION_MODAL);       
        this.sizeToScene();
        this.show();
    }
}

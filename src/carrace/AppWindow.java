package carrace;
 
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
 
public class AppWindow extends Application {   
     
    private static Game game;
    private static MenuBar menuBar;
    public static NewGameWindow newGameWindow;
    private static VBox rootWindow;
    public static DatabaseManager ALL_TRACKS_DATABASE;
    private static int blockSetting = 0;
    private static int driftSetting = 0;
    private static int delaySetting = 3;
    private static MenuItem newGameItem;

    public static int getBlockSetting() {
        return blockSetting;
    }

    public static int getDriftSetting() {
        return driftSetting;
    }

    public static MenuItem getNewGameItem() {
        return newGameItem;
    }

    public static int getDelaySetting() {
        return delaySetting;
    }
            
    @Override
    public void start(Stage primaryStage) {
        
        primaryStage.setResizable(false);
        primaryStage.setTitle("Car Race");
        rootWindow = new VBox(); 
        menuBar = initializeMenubar();
        rootWindow.getChildren().add(menuBar);
        ALL_TRACKS_DATABASE = new DatabaseManager();         
        game = new Game(0,0);
        rootWindow.getChildren().add(game);         
        Scene scene = new Scene(rootWindow);
        primaryStage.setScene(scene);        
        primaryStage.show();    
        primaryStage.sizeToScene();
    }
    
    @Override
    public void stop()
    {
        ALL_TRACKS_DATABASE.update();
    }
    
    public static void main(String[] args) {
        launch(args);
    }    
       
    public MenuBar initializeMenubar()
    {
        menuBar = new MenuBar();
        Menu gameMenu = new Menu("Game");
        Menu settingsMenu = new Menu("Settings");
        Menu helpMenu = new Menu("Help");
        
        newGameItem = new MenuItem("New");
        getNewGameItem().setOnAction((ActionEvent e) -> {
            newGameWindow = new NewGameWindow();
        });
        
        Menu blockingFile= new Menu ("Blocking");
            ToggleGroup toggleGroup = new ToggleGroup();
            RadioMenuItem noneBlockingItem= new RadioMenuItem("None");
            noneBlockingItem.setOnAction((ActionEvent e) -> {
                blockSetting = 2;
            });
            RadioMenuItem mediumBlockingItem = new RadioMenuItem("Place only");
            mediumBlockingItem.setOnAction((ActionEvent e) -> {
                blockSetting = 1;
            });
            RadioMenuItem allBlockingItem = new RadioMenuItem("Place and pass");
            allBlockingItem.setOnAction((ActionEvent e) -> {
                blockSetting = 0;
            });
            noneBlockingItem.setToggleGroup(toggleGroup);
            mediumBlockingItem.setToggleGroup(toggleGroup);
            allBlockingItem.setToggleGroup(toggleGroup);
            allBlockingItem.setSelected(true);
            blockingFile.getItems().addAll(allBlockingItem, mediumBlockingItem, noneBlockingItem);
        Menu driftFile= new Menu ("Drift");
            ToggleGroup toggleGroup2 = new ToggleGroup();
            RadioMenuItem driftEnabledItem = new RadioMenuItem("Enabled");
            driftEnabledItem.setOnAction((ActionEvent e) -> {
                driftSetting = 1;
            });
            RadioMenuItem driftDisabledItem = new RadioMenuItem("Disabled");            
            driftDisabledItem.setOnAction((ActionEvent e) -> {
                driftSetting = 0;
            });
            driftEnabledItem.setToggleGroup(toggleGroup2);
            driftDisabledItem.setToggleGroup(toggleGroup2);
            driftDisabledItem.setSelected(true);
            driftFile.getItems().addAll(driftEnabledItem, driftDisabledItem);
        Menu delayFile= new Menu ("Delay after crash");
            ToggleGroup toggleGroup3 = new ToggleGroup();
            RadioMenuItem noneDelayItem = new RadioMenuItem("None");
            noneDelayItem.setOnAction((ActionEvent e) -> {
                delaySetting = 0;
            });
            RadioMenuItem smallDelayItem = new RadioMenuItem("One turn");
            smallDelayItem.setOnAction((ActionEvent e) -> {
                delaySetting = 1;
            });
            RadioMenuItem mediumDelayItem = new RadioMenuItem("Two turns");
            mediumDelayItem.setOnAction((ActionEvent e) -> {
                delaySetting = 2;
            });
            RadioMenuItem proportionalDelayItem = new RadioMenuItem("Proportional");
            proportionalDelayItem.setOnAction((ActionEvent e) -> {
                delaySetting = 3;
            });
            proportionalDelayItem.setSelected(true);
            noneDelayItem.setToggleGroup(toggleGroup3);
            smallDelayItem.setToggleGroup(toggleGroup3);
            mediumDelayItem.setToggleGroup(toggleGroup3);
            proportionalDelayItem.setToggleGroup(toggleGroup3);
            delayFile.getItems().addAll(noneDelayItem, smallDelayItem, mediumDelayItem, proportionalDelayItem);
            
        Menu editorMenu = new Menu("Track editor");
            MenuItem openEditorItem= new MenuItem("Open track editor");            
            openEditorItem.setOnAction((ActionEvent e) -> {
                TrackEditor trackEditor = new TrackEditor();
            });
            MenuItem manageTracksItem= new MenuItem("Manage tracks");
            manageTracksItem.setOnAction((ActionEvent e) -> {
                TrackManager tm = new TrackManager();
            });
            editorMenu.getItems().addAll(openEditorItem, manageTracksItem);
        MenuItem helpItem= new MenuItem("Help");
        helpItem.setOnAction((ActionEvent e) -> {
                HelpWindow helpWindow = new HelpWindow();
            });
        MenuItem aboutItem= new MenuItem("About");
        aboutItem.setOnAction((ActionEvent e) -> {
                AboutWindow aboutWindow = new AboutWindow();
            });
        
        menuBar.getMenus().addAll(gameMenu, settingsMenu, editorMenu, helpMenu);
        gameMenu.getItems().addAll(getNewGameItem());        
        settingsMenu.getItems().addAll(blockingFile, driftFile, delayFile);  
        helpMenu.getItems().addAll(helpItem,aboutItem);
        return menuBar;
    }

    public static Game getGame() {
        return game;
    }

    public static void setGame(int trackID, int numberOfPlayers) {        
        AppWindow.game = new Game(trackID, numberOfPlayers);        
        rootWindow.getChildren().clear();
        rootWindow.getChildren().addAll(menuBar,game);
        AppWindow.game.startNewGame();
    }
}
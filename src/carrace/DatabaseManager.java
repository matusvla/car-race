package carrace;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DatabaseManager extends ArrayList<TrackDescription>
{    
    private String host;
    private String username;
    private String password;
    private Connection connection;
    private String SQL;
    //doplnit prazdnou databazi - shodi program a vypise alert
    public DatabaseManager(String host, String username, String password)
    {
        this.host = host;
        this.username = username;
        this.password = password;
        try
        {
            connection = DriverManager.getConnection(host, username, password);
            Statement statement = connection.createStatement();
            SQL = "SELECT * FROM ALL_TRACKS";
            ResultSet resultSet = statement.executeQuery(SQL);
            TrackDescription trackDescription = new TrackDescription();
            int id = -1;
            while(resultSet.next())
            {         
                if(resultSet.getInt("ID") != id)
                {
                    trackDescription = new TrackDescription();
                    trackDescription.setTrackID(resultSet.getInt("ID"));
                    trackDescription.setTrackName(resultSet.getString("TRACK_NAME"));
                    trackDescription.setNumberOfPlayers(resultSet.getInt("NUMBER_OF_PLAYERS"));
                    trackDescription.setStartLineDescription(resultSet.getInt("STARTLINEX"),
                                                             resultSet.getInt("STARTLINEY"),
                                                             resultSet.getInt("STARTLINE_LENGTH"),
                                                             resultSet.getInt("STARTLINE_ORIENTATION"));
                    trackDescription.setFinishLineDescription(resultSet.getInt("FINISHLINE_STARTX"),
                                                              resultSet.getInt("FINISH_LINESTARTY"),
                                                              resultSet.getInt("FINISHLINE_ENDX"),
                                                              resultSet.getInt("FINISHLINE_ENDY"));
                    Result result = new Result(resultSet.getString("BEST_PLAYER"),
                                               resultSet.getInt("BEST_SCORE"));
                    trackDescription.setBestScore(result);                    
                }
                ExtendedCoordinates<Boolean> borderDescription = new ExtendedCoordinates(resultSet.getInt("BORDERX"),
                                                                                resultSet.getInt("BORDERY"),
                                                                                resultSet.getBoolean("CONTINUE_BORDER"));
                    trackDescription.addBorderDescription(borderDescription);
                
                ExtendedCoordinates<Integer> obstacleStartDescription = new ExtendedCoordinates(resultSet.getInt("OBSTACLE_STARTX"),
                                                                                resultSet.getInt("OBSTACLE_STARTY"),
                                                                                resultSet.getInt("OBSTACLE_TYPE"));
                ExtendedCoordinates<Integer> obstacleEndDescription = new ExtendedCoordinates(resultSet.getInt("OBSTACLE_ENDX"),
                                                                                resultSet.getInt("OBSTACLE_ENDY"),
                                                                                resultSet.getInt("OBSTACLE_TYPE"));
                if(obstacleStartDescription.getX() != 0 || obstacleStartDescription.getY() != 0 || obstacleEndDescription.getX() != 0 || obstacleEndDescription.getY() != 0)
                {
                    trackDescription.addObstacleDescription(obstacleStartDescription);
                    trackDescription.addObstacleDescription(obstacleEndDescription);
                }
                
                if(resultSet.getInt("ID") != id)
                    this.add(trackDescription);                    
                                    
                id = resultSet.getInt("ID");
            } 
        }
        catch(SQLException err) {
            System.out.println(err.getMessage());
        } 
        if(this.isEmpty())
            AppWindow.getNewGameItem().setDisable(true);
    }
    
    public DatabaseManager()
    {
        this( "jdbc:derby://localhost:1527/TracksDatabase",
              "administrator",
              "database" );
    }  

    @Override
    public boolean remove(Object o) {
        for(TrackDescription trackDescription : this)
            if(trackDescription.getTrackID() > ((TrackDescription)o).getTrackID())
                trackDescription.setTrackID(trackDescription.getTrackID() - 1);
        boolean result = super.remove(o);
        if(this.isEmpty())
            AppWindow.getNewGameItem().setDisable(true);
        return result;
    }
    
    
    
    void update()
    {       
        try
        {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery(SQL); 
            while(resultSet.next())
                resultSet.deleteRow();
            resultSet.first();            
            for(TrackDescription trackDescription : AppWindow.ALL_TRACKS_DATABASE)
            {
                resultSet.moveToInsertRow();                
                resultSet.updateString("TRACK_NAME", trackDescription.getTrackName());
                resultSet.updateInt("NUMBER_OF_PLAYERS", trackDescription.getNumberOfPlayers());
                resultSet.updateInt("STARTLINEX", trackDescription.getStartLineDescription()[0]);
                resultSet.updateInt("STARTLINEY", trackDescription.getStartLineDescription()[1]);
                resultSet.updateInt("STARTLINE_LENGTH", trackDescription.getStartLineDescription()[2]);
                resultSet.updateInt("STARTLINE_ORIENTATION", trackDescription.getStartLineDescription()[3]);
                resultSet.updateInt("FINISHLINE_STARTX", trackDescription.getFinishLineDescription()[0]);
                resultSet.updateInt("FINISH_LINESTARTY", trackDescription.getFinishLineDescription()[1]);
                resultSet.updateInt("FINISHLINE_ENDX", trackDescription.getFinishLineDescription()[2]);
                resultSet.updateInt("FINISHLINE_ENDY", trackDescription.getFinishLineDescription()[3]);
                if(trackDescription.getBorderDescription().isEmpty() && trackDescription.getObstacleDescription().isEmpty())
                {
                    resultSet.updateInt("ID", trackDescription.getTrackID());
                    resultSet.insertRow();
                    statement.close();
                    resultSet.close();
                    statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    resultSet = statement.executeQuery(SQL);
                }
                while((!trackDescription.getBorderDescription().isEmpty()) || (!trackDescription.getObstacleDescription().isEmpty()))
                {
                    resultSet.updateInt("ID", trackDescription.getTrackID());
                    try{
                        ExtendedCoordinates<Boolean> ex = trackDescription.getBorderDescription().remove(0);
                        resultSet.updateInt("BORDERX", ex.getX());
                        resultSet.updateInt("BORDERY", ex.getY());
                        resultSet.updateBoolean("CONTINUE_BORDER", ex.getCondition());                    
                    }catch(Exception ex){}
                    try{
                        ExtendedCoordinates<Integer> startEx = trackDescription.getObstacleDescription().remove(0);
                        ExtendedCoordinates<Integer> endEx = trackDescription.getObstacleDescription().remove(0);
                        resultSet.updateInt("OBSTACLE_STARTX", startEx.getX());
                        resultSet.updateInt("OBSTACLE_STARTY", startEx.getY());
                        resultSet.updateInt("OBSTACLE_ENDX", endEx.getX());
                        resultSet.updateInt("OBSTACLE_ENDY", endEx.getY());
                        resultSet.updateInt("CONTINUE_BORDER", startEx.getCondition());                    
                    }catch(Exception ex){}
                    
                    resultSet.insertRow();
                    statement.close();
                    resultSet.close();
                    statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    resultSet = statement.executeQuery(SQL);
                    resultSet.moveToInsertRow();
                }
            }
        }
        catch(SQLException err) {
            System.out.println(err.getMessage());
        }  
        if(this.isEmpty())
            AppWindow.getNewGameItem().setDisable(true);
    }

    @Override
    public boolean add(TrackDescription e) {
        AppWindow.getNewGameItem().setDisable(false);   //moznost pustit novou hru
        return super.add(e);
    }    
    
    @Override
    public String toString() {
        String allInfo = new String();
        int i = 0;
        for(TrackDescription t : this)
        {
            i++;
            allInfo += t.getTrackID() + " | ";
            allInfo += t.getTrackName() + " "; 
            allInfo += t.getStartLineDescription()[0] + " "; 
            allInfo += t.getStartLineDescription()[1] + " "; 
            allInfo += t.getStartLineDescription()[2] + " "; 
            allInfo += t.getStartLineDescription()[3] + " "; 
            allInfo += t.getFinishLineDescription() + " "; 
            allInfo += t.getBorderDescription() + " ";            
            allInfo += "\n";
        }
        return allInfo;
    }
}

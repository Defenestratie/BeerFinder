package com.beerfinder.beerfinder;



import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by School on 5-6-2015.
 */
public class Database {
    private Connection connection = null;
    private Statement statement = null;
    private ResultSet resultSet = null;

    public Database(){
        connectToDatabase();
        useDatabase();
    }//end of constructor

    private void connectToDatabase(){

        try {

            URL url = new URL("145.24.222.198");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            Class.forName("com.mysql.jdbc.Driver");

            connection = DriverManager.getConnection(
                    //The adres of the server
                    "jdbc:mysql://145.24.222.198:8001",
                    //username
                    "root",
                    //password
                    "blijdorp"
            );

            System.out.println("U heeft verbinding");
        }catch(SQLException ex){
            Log.i(getClass().toString(), "lala.");

        }catch(ClassNotFoundException ex){
            Log.i(getClass().toString(), "ClassNotFoundException..." + ex.getException());
        }catch(IOException ex){
            Log.i(getClass().toString(), ex.getCause().toString());

        }catch(Exception ex){
            Log.i(getClass().toString(), "Dit is vervelend! " +  ex.toString());

        }


    }//end of connectToDatabase()

    private void useDatabase(){
        try {
            statement = connection.createStatement();
            statement.executeUpdate("USE bierapp; ");
        }catch (SQLException ex){
        }finally{

        }

    }//end of useDatabase()


    public void closeDatabase(){
        try {
            if(!connection.isClosed()){
                connection.close();

            }
        }catch(SQLException ex){
            //TODO
        }
    }// end of closeDatabase()

    public void insertLocationIntoDatabase(Location location){
        try {
            statement = connection.createStatement();
            String sql = "INSERT INTO locaties"
                    + "(Locatie_ID, Naam,) "
                    + "VALUES ('"
                    + location.getID()
                    + "', '"
                    + location.getName()
                    + "');";
            statement.execute(sql);
        }catch(SQLException ex){
            Log.i(getClass().toString(), "Niet ingevoerd.");
        }

    }
}

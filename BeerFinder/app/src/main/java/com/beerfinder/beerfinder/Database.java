package com.beerfinder.beerfinder;



import android.os.StrictMode;
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
    private  static Connection connection = null;
    private  static Statement statement = null;

    public Database(){
        if(android.os.Build.VERSION.SDK_INT > 9){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        connectToDatabase();
        useDatabase();
    }//end of constructor

    private void connectToDatabase(){

        try {


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
            Log.i(getClass().toString(), "Een SQLException..." + ex.getMessage());

        }catch(ClassNotFoundException ex){
            Log.i(getClass().toString(), "ClassNotFoundException..." + ex.getMessage());
        }catch(Exception ex){
            Log.i(getClass().toString(), "An exception " +  ex.getMessage());
        }


    }//end of connectToDatabase()

    private  static void useDatabase(){
        try {
            statement = connection.createStatement();
            statement.executeUpdate("USE bierapp; ");
        }catch (SQLException ex){}


    }//end of useDatabase()


    public void closeDatabase(){
        try {
            if(!connection.isClosed()){
                connection.close();

            }
        }catch(SQLException ex){
            Log.i("", "A SQLException... " + ex.getMessage());
        }
    }// end of closeDatabase()

    public void insertLocationIntoDatabase(Location location){
        try {
            statement = connection.createStatement();
            String sql = "INSERT INTO locaties"
                    + "(Locatie_ID, Naam) "
                    + "VALUES ('"
                    + location.getID()
                    + "', '"
                    + location.getName()
                    + "');";
            statement.execute(sql);
        }catch(SQLException ex){
            Log.i(getClass().toString(), "Niet ingevoerd." + ex.getMessage());
        }

    }
}

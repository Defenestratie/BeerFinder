package com.beerfinder.beerfinder;



import android.os.AsyncTask;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Elize on 5-6-2015.
 */
public class Database extends AsyncTask<Void, Void, Void>{
    private static Connection connection = null;
    private static Statement statement = null;

    public Database(){

    }//end of constructor

    @Override
    protected Void doInBackground(Void... params) {
        connectToDatabase();
        useDatabase();
        return null;
    }

    private void connectToDatabase(){

        try {

            Log.d("database","connecting database start");
            Class.forName("com.mysql.jdbc.Driver");

            connection = DriverManager.getConnection(
                    //The adres of the server
                    "jdbc:mysql://145.24.222.189:8001",
                    //username
                    "Elize",
                    //password
                    "Bierapp"
            );

            Log.d("Database", "U heeft verbinding");
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


    public static void closeDatabase(){
        try {
            if(!connection.isClosed()){
                connection.close();

            }
        }catch(SQLException ex){
            Log.i("", "A SQLException... " + ex.getMessage());
        }catch(NullPointerException ex){
            Log.d("Tag", "Database is null");

        }
    }// end of closeDatabase()

    public void insertLocationIntoDatabase(Location location){
        try {

            statement = connection.createStatement();
            String sql = "INSERT INTO locaties"
                    + "(Locatie_ID, Naam, Adres, Type ) "
                    + "VALUES ('"
                    + location.getID()
                    + "', '"
                    + location.getName()
                    + "', '"
                    + location.getAddress()
                    + "', '"
                    + location.getType()
                    + "');";
            statement.execute(sql);
        }catch(SQLException ex){
            Log.i(getClass().toString(), "Niet ingevoerd." + ex.getMessage());
        }catch(NullPointerException ex){
            Log.i("Tag", "Data niet toegevoegd: Nullpointer exception");
        }

    }

    public void insertBeerIntoDatabase(String merk, String naam, int soort_ID){
        try {
            statement = connection.createStatement();
            String sql = "INSERT INTO bier"
                    + "(Merk, Soort_bier, Naam) "
                    + "VALUES ('"
                    + merk
                    + "', '"
                    + soort_ID
                    +"', '"
                    + naam
                    + "');";
            statement.execute(sql);
        }catch(SQLException ex){
            Log.d(getClass().toString(), "Niet ingevoerd." + ex.getMessage());
        }

    }

    public void insertLocationBeer(int BierID, Location location){
        try {
            statement = connection.createStatement();
            String sql = "INSERT INTO locaties_bier"
                    + "(Locatie_ID, Bier_ID) "
                    + "VALUES ('"
                    + location.getID()
                    + "', '"
                    + BierID
                    + "');";
            statement.execute(sql);
        }catch(SQLException ex){
            Log.d(getClass().toString(), "Niet ingevoerd." + ex.getMessage());
        }


    }
}

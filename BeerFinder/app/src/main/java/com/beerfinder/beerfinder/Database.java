package com.beerfinder.beerfinder;


import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Elize on 5-6-2015.
 */
public class Database extends AsyncTask<Void, Void, Void> {
    private static Connection connection = null;
    private static Statement statement = null;

    public Database() {

    }//end of constructor

    @Override
    protected Void doInBackground(Void... params) {
        connectToDatabase();
        useDatabase();
        return null;
    }

    private void connectToDatabase() {

        try {

            Log.d("database", "connecting database start");
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
        } catch (SQLException ex) {
            Log.i(getClass().toString(), "Een SQLException..." + ex.getMessage());

        } catch (ClassNotFoundException ex) {
            Log.i(getClass().toString(), "ClassNotFoundException..." + ex.getMessage());
        } catch (Exception ex) {
            Log.i(getClass().toString(), "An exception " + ex.getMessage());
        }


    }//end of connectToDatabase()

    private static void useDatabase() {
        try {
            statement = connection.createStatement();
            statement.executeUpdate("USE bierapp; ");
        } catch (SQLException ex) {
        }


    }//end of useDatabase()


    public void closeDatabase() {
        try {
            if (!connection.isClosed()) {
                connection.close();

            }
        } catch (SQLException ex) {
            Log.i("", "A SQLException... " + ex.getMessage());
        }
    }// end of closeDatabase()

    public void insertLocationIntoDatabase(Location location) {
        try {
            statement = connection.createStatement();
            String sql = "INSERT INTO locaties"
                    + "(Locatie_ID, Naam, Adres, Type) "
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
        } catch (SQLException ex) {
            Log.i(getClass().toString(), "Niet ingevoerd." + ex.getMessage());
        }

    }

    public int insertBeerIntoDatabase(String merk, String naam, int soort_ID) {
        int ID = 0;
        try {
            String sql = "INSERT INTO bier"
                    + "(Merk, Soort_bier, Naam) "
                    + "VALUES ('"
                    + merk
                    + "', '"
                    + soort_ID
                    + "', '"
                    + naam
                    + "');";
            PreparedStatement stmt = connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            stmt.executeUpdate();
            ResultSet res = stmt.getGeneratedKeys();
            while (res.next())
                ID = res.getInt(1);
        } catch (SQLException ex) {
            Log.d(getClass().toString(), "Niet ingevoerd." + ex.getMessage());
            try {
                statement = connection.createStatement();
                String sql = "SELECT Bier_ID FROM bier WHERE Merk = '"
                        + merk
                        + "' AND Naam = '"
                        + naam
                        + "' AND Soort_Bier = '"
                        + soort_ID
                        + "';" ;
                ResultSet resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    ID = resultSet.getInt("Bier_ID");
                }
            }catch(SQLException ex2){
                Log.d(getClass().toString(), "Niet opgehaald." + ex.getMessage());
            }
        }
        return ID;
    }

    public ArrayList getBeerTypes() {
        ArrayList<BeerType> list = new ArrayList<>();
        try {
            statement = connection.createStatement();
            String sql = "SELECT * FROM typen_bier";
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                int ID = result.getInt("BierSoort_ID");
                String name = result.getString("Naam");
                BeerType beerType = new BeerType(ID, name);
                list.add(beerType);
            }
        } catch (SQLException ex) {
            Log.i(getClass().toString(), "Niet Opgehaald." + ex.getMessage());
        }

        return list;

    }

    public void insertIntoBeerLocations(String Place_ID, int Beer_ID) {
        try {
            statement = connection.createStatement();
            String sql = "INSERT INTO locaties_bier"
                    + "(Locatie_ID, Bier_ID) "
                    + "VALUES ('"
                    + Place_ID
                    + "', '"
                    + Beer_ID
                    + "');";
            statement.execute(sql);
        } catch (SQLException ex) {
            Log.d(getClass().toString(), "Niet ingevoerd." + ex.getMessage());
        }

    }

    public ArrayList<String> getAllBeerTypesForLocation(String Location_ID) {
        ArrayList<Integer> listIDs = new ArrayList<>();
        ArrayList<String> list = new ArrayList<>();
        try {
            statement = connection.createStatement();
            String sql = "SELECT * FROM locaties_bier WHERE Locatie_ID = '" + Location_ID + "';";
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                int ID = result.getInt("Bier_ID");
                listIDs.add(ID);
            }

            statement = connection.createStatement();
            for (int id : listIDs) {
                String sql2 = "SELECT Naam FROM bier WHERE Bier_ID = " + id + "";
                ResultSet result2 = statement.executeQuery(sql2);
                while (result2.next()) {
                    list.add(result2.getString("Naam"));
                }
            }

        } catch (SQLException ex) {
            Log.i(getClass().toString(), "Niet Opgehaald." + ex.getMessage());
        }

        return list;

    }
}

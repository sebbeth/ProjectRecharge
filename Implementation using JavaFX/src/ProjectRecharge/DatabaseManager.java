package ProjectRecharge;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class DatabaseManager {


    private String databaseName = "";
    private String tableName = "Tasks";


    public DatabaseManager(String name) {


        databaseName = "jdbc:sqlite:" + name + ".db";


        // Test to see if the table Customers exists.

        try {

            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection(databaseName);


            DatabaseMetaData databaseMetaData = connection.getMetaData();

            // try to instantiate resultSet as a set of all tables.
            ResultSet resultSet = databaseMetaData.getTables(null,null,tableName,null);;

            // Now if resultSet is empty, that means our table needs to be created.

            if (!resultSet.next()) { // Create new table

                createTable(tableName);

            }



            resultSet.close();
            connection.close();

        } catch (Exception e) {

            System.err.println( e.getClass().getName() + ": " + e.getMessage() );


        }


    }









    public void createTable(String name) {
        /*
        Function that executes a query that creates an empty Customer table

         */

        try {
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection(databaseName);

            Statement statement = connection.createStatement();

            String query = "CREATE TABLE " + name + " " +
                    "(ID INT PRIMARY KEY     NOT NULL," +
                    " Title           TEXT, " +
                    " Frequency           TEXT, " +
                    " LastCompleted           TEXT, " +
                    " Notes           TEXT) ";


            statement.executeUpdate(query);

            // Important, connection must be closed or things will break.
            statement.close();
            connection.close();

        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }

    }



    public void createNewTask(TaskDataModel newTask) {

        try {

            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection(databaseName);
            connection.setAutoCommit(false);

            // First generate a new primary key

            int key = generateRandomIntForPK();



            Statement statement = connection.createStatement();
            // Now construct the query
            String query = "INSERT INTO " + tableName + " (ID,Title,Frequency,LastCompleted,Notes)" +
                    " VALUES ("+ key +",'"+
                    newTask.getTitle()+"','"
                    +newTask.getFrequency()+"','"+
                    newTask.getFormattedLastCompleted()+"','"+
                    newTask.getNotes() + "');";

            statement.executeUpdate(query);

            // Important, connection must be closed or things will break.
            statement.close();
            connection.commit();
            connection.close();

        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }



    }


    public void modifyExistingTask(TaskDataModel task) {


        try {
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection(databaseName);
            connection.setAutoCommit(false);


            Statement statement = connection.createStatement();
            String query = "UPDATE " + tableName +
                    " Set Title='" + task.getTitle() + "'," +
                    " Frequency='" + task.getFrequency() + "'," +
                    " LastCompleted='" + task.getFormattedLastCompleted() + "'," +
                    " Notes='" + task.getNotes() + "'" +
                    " WHERE ID=" + task.getId() + ";";

            statement.executeUpdate(query);

            // Important, connection must be closed or things will break.
            statement.close();
            connection.commit();
            connection.close();

        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }



    }

    public String queryDatabaseForTaskWithId(int id) {

        return "";
    }

    public List<TaskDataModel> queryDatabaseForAllTasks() {

        List output = new LinkedList();

        try {
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection(databaseName);
            connection.setAutoCommit(false);

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * " +
                    "FROM Tasks ;");

            while (resultSet.next()) {

                TaskDataModel newTask = new TaskDataModel(
                        resultSet.getInt("ID"),
                        resultSet.getString("Title"),
                        resultSet.getString("Frequency"),
                        resultSet.getString("Notes"),
                        new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(resultSet.getString("LastCompleted"))
                );

                output.add(newTask);

            }

            // Important, connection must be closed or things will break.
            resultSet.close();
            statement.close();
            connection.close();


        } catch ( Exception e ) {

            System.err.println( e.getClass().getName() + ": " + e.getMessage() );

        }



        return output;
    }









    private int generateRandomIntForPK() {

         /*

        A function that randomly generates a primary key for a data entry and then checks to see if that key is
        unique using a SQL query. The function calls itself recursively if the key is found to not be unique.
        OUT: Returns the key as an integer if it is found to be unique.

         */

        Random random = new Random();

        // Generate the candidate PK.
        int candidate = random.nextInt(999999999);

        int output = 0;

        // Now check to see if that candidate has already been used as a PK before

        try {
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection(databaseName);
            connection.setAutoCommit(false);

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery( "SELECT * " +
                    "FROM " + tableName +
                    " WHERE ID=" + candidate + ";" );

            if (resultSet.next()) {

                // A match was found, try again

                // Important, connection must be closed or things will break.
                resultSet.close();
                statement.close();
                connection.close();


                output = generateRandomIntForPK(); //Loop recursively.

            } else {


                // Key is unique, return it.

                // Important, connection must be closed or things will break.
                resultSet.close();
                statement.close();
                connection.close();

                output = candidate;

            }


        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }




        return output;


    }






}

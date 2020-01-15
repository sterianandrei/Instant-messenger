package server;

import java.sql.*;

import java.util.Date;
import java.util.Vector;

public class DatabaseConnection
{
    public static Connection connect = null;
    public static Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    public enum DB_RETURN  {
        WRONG_PASS,
        NO_USER,
        USER_ALREADY_CREATED,
        CONN_SUCCESS,
        REGISTER_SUCCESS,
        USER_INSERTED
    }

    private static final String host = "localhost";
    private static final String user = "root";
    private static final String pass = "1Inoxcrom1234@";
    private static final String dbname = "instantmessenger";


    DatabaseConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection("jdbc:mysql://"+host+"/"+dbname+"?user="+user+"&password="+pass);
            statement = connect.createStatement();
        }
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("Connected");
        }
    }

    public DB_RETURN findUser(String user, String pass) throws SQLException
    {
        resultSet = statement.executeQuery("SELECT * from users where user='"+user+"'");
        if (!resultSet.isBeforeFirst() ) {
            return DB_RETURN.NO_USER;
        }
        else {
            boolean last = resultSet.last();
            String username = resultSet.getString("user");
            boolean first = resultSet.first();
            if (username.equals(resultSet.getString("user"))) {
                if (pass.equals(resultSet.getString("pass")))
                    return DB_RETURN.CONN_SUCCESS;
                else
                    return DB_RETURN.WRONG_PASS;
            }
            return null;
        }

    }

    public static void insertUser(String user, String pass) throws SQLException
    {
        CallableStatement cStmt = connect.prepareCall("{call user_add(?, ?, ?)}");

        cStmt.setString(1, user);
        cStmt.setString(2, pass);
        cStmt.setString(3, "NEW_USER");

        try {
            cStmt.executeUpdate();
            System.out.println("Record is inserted into DBUSER table!");

        } catch (SQLException e) {

            System.out.println(e.getMessage());

        }
    }

    public static void insertInHistory(String user, String status) throws SQLException
    {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        String insertTableSQL = "INSERT INTO history"
                + "(user, status, time_modified) " + "VALUES"
                + "('" + user + "', '" + status + "', '" + currentTime + "')";

        try {
            System.out.println(insertTableSQL);
            statement.executeUpdate(insertTableSQL);

            System.out.println("Record is inserted into DBUSER table!");

        } catch (SQLException e) {

            System.out.println(e.getMessage());

        }
    }

    public static void insertInSent(String user, String message) throws SQLException
    {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        String insertTableSQL = "INSERT INTO messages_sent"
                + "(user, message, send_time) " + "VALUES"
                + "('" + user + "', '" + message + "', '" + currentTime + "')";

        try {
            System.out.println(insertTableSQL);
            statement.executeUpdate(insertTableSQL);

            System.out.println("Record is inserted into DBUSER table!");

        } catch (SQLException e) {

            System.out.println(e.getMessage());

        }
    }

    public static void insertInReceived(String user, String message) throws SQLException
    {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        String insertTableSQL = "INSERT INTO messages_received"
                + "(user, message, receive_time) " + "VALUES"
                + "('" + user + "', '" + message + "', '" + currentTime + "')";

        try {
            System.out.println(insertTableSQL);
            statement.executeUpdate(insertTableSQL);

            System.out.println("Record is inserted into DBUSER table!");

        } catch (SQLException e) {

            System.out.println(e.getMessage());

        }
    }

    public Vector<String> getFriends(String user) throws SQLException {
        Vector<String> rez = new Vector<String>();
        resultSet = statement.executeQuery("SELECT f.friend as friend FROM friends f, users u WHERE f.userid = u.id AND u.user LIKE '"+ user+"'");
        while(resultSet.next())
        {
            rez.add(resultSet.getString("friend"));
        }
        return rez;
    }

    public String getName(String user) throws SQLException {
        resultSet = statement.executeQuery("SELECT * from users where user='"+user+"'");
        if(!resultSet.isBeforeFirst())
            return null;
        resultSet.first();
        return resultSet.getString("name");
    }



}

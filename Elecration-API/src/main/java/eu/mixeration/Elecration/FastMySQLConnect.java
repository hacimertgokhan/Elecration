package eu.mixeration.Elecration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class FastMySQLConnect implements $Dc15a {

    public static Connection connection;

    public static Connection getConnection() {
        return connection;
    }

    public static void setConnection(Connection connection1) {
        connection = connection1;
    }

    @Override
    public boolean connect(String database, String username, int port, String host, String password) {
        try {
            if (connection != null && !connection.isClosed()) {
                return false;
            }
            Class.forName("com.mysql.jdbc.Driver");
            setConnection(DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password));
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }


}

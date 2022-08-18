package eu.mixeration.Elecration;

import java.sql.Connection;

public interface $Dc15a {

    /*

            MYSQL Connectator |START| - CREATED: /WHO/ > Mixeration

     */

    static Connection getConnection() {
        return null;
    }

    static void setConnection(Connection connection) {
    }

    boolean connect(String database, String username, int port, String host, String password);

    /*

            MYSQL Connectator |FINISH| - CREATED: /WHO/ > Mixeration

     */


}

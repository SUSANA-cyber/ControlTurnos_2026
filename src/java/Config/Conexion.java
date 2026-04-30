package Config;

import java.sql.Connection;
import java.sql.DriverManager;

public class Conexion {

    public static Connection getConexion() {
        Connection con = null;
        String usuario = "root";
        String password = "";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3308/control_turnos?useSSL=false&serverTimezone=America/Guatemala&allowPublicKeyRetrieval=true",
                    usuario,
                    password
            );

        } catch (Exception e) {
            e.printStackTrace();
        }

        return con;
    }
}



package com.sanvalero.trailrun.dao;

import com.sanvalero.trailrun.util.R;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Creado por @ author: Pedro Orós
 * el 20/11/2020
 */
public class BaseDAO {

    protected Connection conexion;

    public void conectar() throws ClassNotFoundException, SQLException, IOException {

        Properties configuration = new Properties();
        configuration.load(R.getProperties("database.properties"));
        String host = configuration.getProperty("host");
        String port = configuration.getProperty("port");
        String name = configuration.getProperty("name");
        String username = configuration.getProperty("username");
        String password = configuration.getProperty("password");
        //String driver = configuration.getProperty("drivermysql"); <--- ?????

        Class.forName("com.mysql.cj.jdbc.Driver"); /* <--- Busco en MVNrepository (en internet),
                                                        copio el xml de mysql connector y lo pego en el POM*/
        conexion = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + name + "?serverTimezone=UTC",
                username, password);
    }

    public void desconectar() throws SQLException {
        conexion.close();
        conexion = null;
    }

}

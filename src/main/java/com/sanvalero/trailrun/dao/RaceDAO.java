package com.sanvalero.trailrun.dao;

import com.sanvalero.trailrun.domain.Race;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Creado por @ author: Pedro Orós
 * el 20/11/2020
 */
public class RaceDAO extends BaseDAO{

    public void guardarRace(Race race) throws SQLException {
        String sql = "INSERT INTO races (nombre, lugar, fecha, distancia, desnivel, tipo) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement sentencia = null;

        sentencia = conexion.prepareStatement(sql);

        sentencia.setString(1, race.getNombre());
        sentencia.setString(2, race.getLugar());
        sentencia.setString(3, race.getFecha());
        sentencia.setInt(4, race.getDistancia());
        sentencia.setInt(5, race.getDesnivel());
        sentencia.setString(6, race.getTipo());

        sentencia.executeUpdate();

    }

    public void modificarRace(Race race) throws SQLException {
        String sql = "UPDATE races SET nombre = ?, lugar = ?, fecha = ?, distancia = ?, desnivel = ?, tipo = ? WHERE nombre = ?";
        PreparedStatement sentencia = null;

        sentencia = conexion.prepareStatement(sql);

        sentencia.setString(1, race.getNombre());
        sentencia.setString(2, race.getLugar());
        sentencia.setString(3, race.getFecha());
        sentencia.setInt(4, race.getDistancia());
        sentencia.setInt(5, race.getDesnivel());
        sentencia.setString(6, race.getTipo());
        sentencia.setString(7, race.getNombreViejo());

        sentencia.executeUpdate();

    }

    public void eliminarRace(Race race) throws SQLException {
        String sql = "DELETE FROM races WHERE nombre = ?";
        PreparedStatement sentencia = null;

        sentencia = conexion.prepareStatement(sql);

        sentencia.setString(1, race.getNombre());

        sentencia.executeUpdate();

    }

    public List<Race> listarRaces() throws SQLException {
        String sql = "SELECT * FROM races";
        Statement sentencia = conexion.createStatement();
        ResultSet resultado = sentencia.executeQuery(sql);

        List<Race> lista = new ArrayList<>();

        while (resultado.next()) {
            Race race = new Race(
                    resultado.getString(2),
                    resultado.getString(3),
                    resultado.getString(4),
                    resultado.getInt(5),
                    resultado.getInt(6),
                    resultado.getString(7)
            );
            lista.add(race);

        }

        return  lista;
    }
}

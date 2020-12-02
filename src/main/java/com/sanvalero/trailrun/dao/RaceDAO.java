package com.sanvalero.trailrun.dao;

import com.sanvalero.trailrun.domain.Race;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;

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
        sentencia.setDate(3, race.getFecha());
        sentencia.setInt(4, race.getDistancia());
        sentencia.setInt(5, race.getDesnivel());
        sentencia.setString(6, race.getTipo());

        sentencia.executeUpdate();

    }

    public void modificarRace(Race race) throws SQLException {
        String sql = "UPDATE races SET nombre = ?, lugar = ?, fecha = ?, distancia = ?, desnivel = ?, tipo = ? WHERE nombre = ? AND lugar = ? AND fecha = ?";
        PreparedStatement sentencia = null;

        sentencia = conexion.prepareStatement(sql);

        sentencia.setString(1, race.getNombre());
        sentencia.setString(2, race.getLugar());
        sentencia.setDate(3, race.getFecha());
        sentencia.setInt(4, race.getDistancia());
        sentencia.setInt(5, race.getDesnivel());
        sentencia.setString(6, race.getTipo());
        sentencia.setString(7, race.getNombreViejo());
        sentencia.setString(8, race.getLugarViejo());
        sentencia.setDate(9, race.getFechaVieja());

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
                    resultado.getDate(4),
                    resultado.getInt(5),
                    resultado.getInt(6),
                    resultado.getString(7)
            );
            lista.add(race);

        }

        return  lista;
    }

    public List<Race> filtroLugar(String lugar) throws SQLException {
        String sql = "SELECT * FROM races WHERE UPPER(lugar) = ?";
        PreparedStatement sentencia = conexion.prepareStatement(sql);
        sentencia.setString(1, lugar);
        ResultSet resultado = sentencia.executeQuery();

        List <Race> lista = new ArrayList<>();

        while (resultado.next()) {
            Race race = new Race(
                    resultado.getString(2),
                    resultado.getString(3),
                    resultado.getDate(4),
                    resultado.getInt(5),
                    resultado.getInt(6),
                    resultado.getString(7)
            );
            lista.add(race);
        }
        return lista;
    }

    public List<Race> filtroDistancia(int opcion) throws SQLException {
        String sql = "";
        switch (opcion) {
            case 1:
                sql = "SELECT * FROM races WHERE distancia < 5000";
                break;
            case 2:
                sql = "SELECT * FROM races WHERE distancia >= 5000 AND distancia <= 25000";
                break;
            case 3:
                sql = "SELECT * FROM races WHERE distancia > 25000 AND distancia <= 50000";
                break;
            case 4:
                sql = "SELECT * FROM races WHERE distancia > 50000";
                break;
        }

        Statement sentencia = conexion.createStatement();
        ResultSet resultado = sentencia.executeQuery(sql);

        List <Race> lista = new ArrayList<>();

        while (resultado.next()) {
            Race race = new Race(
                    resultado.getString(2),
                    resultado.getString(3),
                    resultado.getDate(4),
                    resultado.getInt(5),
                    resultado.getInt(6),
                    resultado.getString(7)
            );
            lista.add(race);
        }
        return lista;
    }

    public List<Race> filtroLugarDistancia(String lugar, int opcion) throws SQLException {
        String sql = "";
        switch (opcion) {
            case 1:
                sql = "SELECT * FROM races WHERE UPPER(lugar) = ? AND distancia < 5000";
                break;
            case 2:
                sql = "SELECT * FROM races WHERE UPPER(lugar) = ? AND distancia >= 5000 AND distancia <= 25000";
                break;
            case 3:
                sql = "SELECT * FROM races WHERE UPPER(lugar) = ? AND distancia > 25000 AND distancia <= 50000";
                break;
            case 4:
                sql = "SELECT * FROM races WHERE UPPER(lugar) = ? AND distancia > 50000";
                break;
        }

        PreparedStatement sentencia = conexion.prepareStatement(sql);
        sentencia.setString(1, lugar);
        ResultSet resultado = sentencia.executeQuery();

        List<Race> lista = new ArrayList<>();

        while (resultado.next()) {
            Race race = new Race(
                    resultado.getString(2),
                    resultado.getString(3),
                    resultado.getDate(4),
                    resultado.getInt(5),
                    resultado.getInt(6),
                    resultado.getString(7)
            );
            lista.add(race);
        }
        return lista;
    }

}

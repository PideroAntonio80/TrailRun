package com.sanvalero.trailrun.dao;

import com.sanvalero.trailrun.domain.Usuario;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Creado por @ author: Pedro Orós
 * el 30/11/2020
 */
public class InicioDAO extends BaseDAO{

    public void guardarUsuario(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuarios (usuario, nombre, email, password) VALUES (?, ?, ?, ?)";
        PreparedStatement sentencia = null;

        sentencia = conexion.prepareStatement(sql);

        sentencia.setString(1, usuario.getUsuario());
        sentencia.setString(2, usuario.getNombre());
        sentencia.setString(3, usuario.getEmail());
        sentencia.setString(4, usuario.getPassword());

        sentencia.executeUpdate();
    }

    public int iniciarSesion(Usuario usuario) throws SQLException {
        int opcion = 0;
        String sql = "SELECT usuario, password FROM usuarios WHERE usuario = ? AND password = ?";
        PreparedStatement sentencia =conexion.prepareStatement(sql);
        sentencia.setString(1, usuario.getUsuario());
        sentencia.setString(2, usuario.getPassword());
        ResultSet resultado = sentencia.executeQuery();

        if(resultado.next()) opcion = 1;
        return opcion;
    }

    public boolean existeUsuario(String nombreUser) throws SQLException {
        String sql = "SELECT usuario FROM usuarios WHERE usuario = ?";
        PreparedStatement sentencia = conexion.prepareStatement(sql);
        sentencia.setString(1, nombreUser);
        ResultSet resultado = sentencia.executeQuery();

        return resultado.next();
    }
}

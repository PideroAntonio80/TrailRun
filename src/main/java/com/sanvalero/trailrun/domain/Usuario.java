package com.sanvalero.trailrun.domain;

/**
 * Creado por @ author: Pedro Orós
 * el 30/11/2020
 */
public class Usuario {

    private String usuario;
    private String nombre;
    private String email;
    private String password;

    public Usuario(String usuario, String nombre, String email, String password) {
        this.usuario = usuario;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
    }

    public Usuario(String usuario, String password) {
        this.usuario = usuario;
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

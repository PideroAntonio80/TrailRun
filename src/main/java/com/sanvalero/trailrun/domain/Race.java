package com.sanvalero.trailrun.domain;

import javafx.scene.control.TextField;

import java.sql.Date;
import java.time.LocalDate;

/**
 * Creado por @ author: Pedro Orós
 * el 20/11/2020
 */
public class Race {
     private int id;
     private String nombre;
     private String lugar;
     private Date fecha;
     private int distancia;
     private int desnivel;
     private String tipo;
     private String nombreViejo;
     private String lugarViejo;
     private Date fechaVieja;

    public Race(int id, String nombre, String lugar, Date fecha, int distancia, int desnivel, String tipo) {
        this.id = id;
        this.nombre = nombre;
        this.lugar = lugar;
        this.fecha = fecha;
        this.distancia = distancia;
        this.desnivel = desnivel;
        this.tipo = tipo;
    }

    public Race(String nombre, String lugar, Date fecha, int distancia, int desnivel, String tipo) {
        this.nombre = nombre;
        this.lugar = lugar;
        this.fecha = fecha;
        this.distancia = distancia;
        this.desnivel = desnivel;
        this.tipo = tipo;
    }

    public Race(String nombreViejo, String lugarViejo, Date fechaVieja, String nombre, String lugar, Date fecha, int distancia, int desnivel, String tipo) {
        this.nombreViejo = nombreViejo;
        this.lugarViejo = lugarViejo;
        this.fechaVieja = fechaVieja;
        this.nombre = nombre;
        this.lugar = lugar;
        this.fecha = fecha;
        this.distancia = distancia;
        this.desnivel = desnivel;
        this.tipo = tipo;
    }

    public Race(String nombre) {
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getDistancia() {
        return distancia;
    }

    public void setDistancia(int distancia) {
        this.distancia = distancia;
    }

    public int getDesnivel() {
        return desnivel;
    }

    public void setDesnivel(int desnivel) {
        this.desnivel = desnivel;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNombreViejo() {
        return nombreViejo;
    }

    public void setNombreViejo(String nombreViejo) {
        this.nombreViejo = nombreViejo;
    }

    public String getLugarViejo() {
        return lugarViejo;
    }

    public void setLugarViejo(String lugarViejo) {
        this.lugarViejo = lugarViejo;
    }

    public Date getFechaVieja() {
        return fechaVieja;
    }

    public void setFechaVieja(Date fechaVieja) {
        this.fechaVieja = fechaVieja;
    }

}

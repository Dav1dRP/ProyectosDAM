package com.example.tarea4_davidramosdelpino;

public class Contacto {
    String nombre;
    int tieneTelefono;
    Long id;
    String telefono;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getTieneTelefono() {
        return tieneTelefono;
    }

    public void setTieneTelefono(int tieneTelefono) {
        this.tieneTelefono = tieneTelefono;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Contacto(String nombre, int tieneTelefono, Long id, String telefono) {
        this.nombre = nombre;
        this.tieneTelefono = tieneTelefono;
        this.id = id;
        this.telefono = telefono;
    }

    public Contacto() {

    }
}

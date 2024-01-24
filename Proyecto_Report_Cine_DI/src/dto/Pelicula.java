/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

/**
 *
 * @author Tarde
 */
public class Pelicula {
    private String titulo;
    private String genero;
    private Boolean vista;
    private Cine cine;

    public Pelicula(String titulo, String genero, Boolean vista, Cine cine) {
        this.titulo = titulo;
        this.genero = genero;
        this.vista = vista;
        this.cine = cine;
    }
        public Pelicula() {

    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public Boolean getVista() {
        return vista;
    }

    public void setVista(Boolean vista) {
        this.vista = vista;
    }

    public Cine getCine() {
        return cine;
    }

    public void setCine(Cine cine) {
        this.cine = cine;
    }
        
        
    
    


  
        
    
}

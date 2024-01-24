/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 *
 * @author Tarde
 */
public class Cine {
    private String nombre;
    private String ubicacion;
    private List listapeliculas=new ArrayList();

    public Cine(String nombre, String ubicacion, ArrayList listapeliculas) {
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.listapeliculas=listapeliculas;
    }
        public Cine() {

    }
         public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public List getListaPelicula() {
        return listapeliculas;
    }

    public void setListaPeliculas(List listapeliculas) {
        this.listapeliculas = listapeliculas;
    }   

   
    
    public JRDataSource getlistapeliculas(){
        return new JRBeanCollectionDataSource(listapeliculas);
 } 


    
    
    
}

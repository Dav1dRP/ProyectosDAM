/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import dto.Pelicula;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;


/**
 *
 * @author Tarde
 */
public class PeliculasTableModel extends AbstractTableModel{
    private ArrayList<Pelicula> listPelicula;
    private String[] columnas = {"Titulo","Género","Vista"};

    public PeliculasTableModel(ArrayList<Pelicula> peli) {
        this.listPelicula = peli;
    }
    

    @Override
    public int getRowCount() {
        return listPelicula.size();
    }

    @Override
    public int getColumnCount() {
        return columnas.length;
    }
    
    public void removeRow(int usu){
        this.listPelicula.remove(usu);
    }

    @Override
    public Object getValueAt(int indicefila, int indicecolumna) {
        switch(indicecolumna){
            case 0:
                return listPelicula.get(indicefila).getTitulo();
            case 1:
                return listPelicula.get(indicefila).getGenero();
            case 2:
                return listPelicula.get(indicefila).getVista();
        }
        return null;
    }

    @Override
    public String getColumnName(int i) {
        return columnas[i];
    }
    
    
    
    
}

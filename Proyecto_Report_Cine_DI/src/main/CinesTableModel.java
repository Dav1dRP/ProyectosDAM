/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import dto.Cine;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;


/**
 *
 * @author Tarde
 */
public class CinesTableModel extends AbstractTableModel{
    private ArrayList<Cine> listCine;
    private String[] columnas = {"Nombre","Dirección","Numero de Peliculas"};

    public CinesTableModel(ArrayList<Cine> cine) {
        this.listCine = cine;
    }
    

    @Override
    public int getRowCount() {
        return listCine.size();
    }

    @Override
    public int getColumnCount() {
        return columnas.length;
    }
    
    public void removeRow(int usu){
        this.listCine.remove(usu);
    }

    @Override
    public Object getValueAt(int indicefila, int indicecolumna) {
        switch(indicecolumna){
            case 0:
                return listCine.get(indicefila).getNombre();
            case 1:
                return listCine.get(indicefila).getUbicacion();
            case 2:
                return listCine.get(indicefila).getListaPelicula().size();
        }
        return null;
    }

    @Override
    public String getColumnName(int i) {
        return columnas[i];
    }
    
    
    
    
}

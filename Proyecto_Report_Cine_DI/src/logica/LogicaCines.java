/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica;

import dto.Cine;
import java.util.ArrayList;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 *
 * @author Tarde
 */
public class LogicaCines {
        private static ArrayList<Cine> listaCine = new ArrayList<>();
    
    public static void nuevoCine(Cine cine){
        listaCine.add(cine);
    }

    public static ArrayList<Cine> getListaCine() {
        return listaCine;
    }
}

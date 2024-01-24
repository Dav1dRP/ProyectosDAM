package com.example.practica2_davidramosdelpino;

import java.util.Random;

public class Tablero {
    private int columnas;
    private int filas;
    private int minas;
    private int [][] tablero;

    public int getColumnas() {
        return columnas;
    }

    public void setColumnas(int columnas) {
        this.columnas = columnas;
    }

    public int getFilas() {
        return filas;
    }

    public void setFilas(int filas) {
        this.filas = filas;
    }

    public int getMinas() {
        return minas;
    }

    public void setMinas(int minas) {
        this.minas = minas;
    }

    public int[][] getTablero() {
        return tablero;
    }

    public Tablero(int columnas, int filas, int minas) {
        this.columnas = columnas;
        this.filas = filas;
        this.minas = minas;
    }

    public void Inicializar(){
        tablero = new int[this.filas][this.columnas];
        for (int x = 0; x < this.filas; x++) {
            for (int y = 0; y < this.columnas; y++) {
                tablero[x][y]=0;
            }
        }
            PonerMinas();

    }
    public  void PonerMinas() {
        int minasColocadas=0;
        Random random = new Random();
        while(minasColocadas < minas) {
            int fila = random.nextInt(filas);
            int columna = random.nextInt(columnas);

            if (tablero[fila][columna] != -1) {
                // Si la celda no es una mina, coloca una mina (-1)
                tablero[fila][columna] = -1;
                minasColocadas++;
            }
        }

    }
    public boolean esMina(int x,int y){
        if (tablero[x][y]==-1){
            return true;
        }
        return false;
    }
    public int getMinasAlrededor(int x, int y) {
        int minasAlrededor = 0;

        // Posiciones posibles
        int[] px = { -1, -1, -1, 0, 0, 1, 1, 1 };
        int[] py = { -1, 0, 1, -1, 1, -1, 0, 1 };

        for (int i = 0; i < px.length; i++) {
            int nuevaX = x + px[i];
            int nuevaY = y + py[i];

            if (nuevaX >= 0 && nuevaX < filas && nuevaY >= 0 && nuevaY < columnas) {
                if (tablero[nuevaX][nuevaY] == -1) {
                    minasAlrededor++;
                }
            }
        }


        return minasAlrededor;
    }

}

package com.example.practica2_davidramosdelpino;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity  {
    private int filas=8;
    private int columnas=8;
    private int minas=10;

    private Tablero tablero;

    private GridLayout g;

    private ImageButton[][] botonesMina;

    private boolean[][] casillasReveladas;

    private int imagen;

    private int contadorMinas=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        g=findViewById(R.id.gPrincipal);
        g.post(new Runnable() {
            @Override
            public void run() {
                inicio();
            }});
    }
    public void inicio() {
        crearTablero(filas,columnas,minas);
    }

    private void ponerCasillas() {
        //Parámetros del Layout
        GridLayout.LayoutParams param=new GridLayout.LayoutParams();
        param.setMargins(0,0,0,0);
        param.height= ViewGroup.LayoutParams.MATCH_PARENT;
        param.width= ViewGroup.LayoutParams.MATCH_PARENT;
        g.setRowCount(this.filas);
        g.setColumnCount(this.columnas);
        g.setLayoutParams(param);

        //Parametros para los botones
        int height = g.getHeight();
        int width = g.getWidth();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width / columnas, height / filas);
        layoutParams.setMargins(0, 0, 0, 0);

        //Cambiar tamaño de las imagenes
        imagen= R.drawable.mina1;//Mina por defecto
        int nuevoAncho = 70;
        int nuevoAlto = 70;

        Bitmap originalBandera = BitmapFactory.decodeResource(getResources(), R.drawable.bandera);
        Bitmap bandera = Bitmap.createScaledBitmap(originalBandera, nuevoAncho, nuevoAlto, false);


        //Inicializar Matrices
        botonesMina = new ImageButton[this.columnas][this.filas];
        casillasReveladas = new boolean[this.columnas][this.filas];

        //Poner botones en el tablero
        for (int x = 0; x < this.columnas; x++) {
            for (int y = 0; y < this.filas; y++) {

                View view = new View(getApplicationContext());

                int ay= y;
                int ax=x;
                casillasReveladas[x][y] = false; // Al principio no tengo destapada ninguna
                if (tablero.esMina(x, y)) {
                    view = new ImageButton(getApplicationContext());
                    //ImageButton ib = new ImageButton(this);
                    ImageButton ib = (ImageButton)view;
                    ib.setLayoutParams(layoutParams);
                    ib.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            revelarTodasLasMinas();
                            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.explosion);
                            mp.start();
                            DialogoPerder dialogo=new DialogoPerder();
                            dialogo.show(getSupportFragmentManager(),"Dialogo Perder");
                        }
                    });
                    ib.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            ib.setImageBitmap(bandera);
                            contadorMinas++;
                            if(contadorMinas>=minas){
                                DialogoGanar dialogo=new DialogoGanar();
                                dialogo.show(getSupportFragmentManager(),"Dialogo Ganar");
                            }
                            return true;
                        }
                    });

                    g.addView(ib);

                    botonesMina[x][y] = ib;
                } else {
                    view = new Button(getApplicationContext());
                    Button b= (Button) view;
                    //Button b = new Button(this);
                    b.setLayoutParams(layoutParams);
                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int minasAlrededor=tablero.getMinasAlrededor(ax,ay);
                            b.setBackgroundColor(Color.RED);
                            if (minasAlrededor > 0) {
                                b.setText("" + minasAlrededor);
                            }else{
                                revelarCasillasVacias(ax,ay);
                            }

                        }
                    });
                    b.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            DialogoPerder dialogo=new DialogoPerder();
                            dialogo.show(getSupportFragmentManager(),"Dialogo Perder");
                            return false;
                        }
                    });
                    g.addView(b);
                }
                ponerColorBoton(view,x,y);
            }
        }
    }

    private void ponerColorBoton(View view, int x, int y) {
        if ((x + y) % 2 == 0) {
            view.setBackgroundColor(Color.BLUE);
        } else {
            view.setBackgroundColor(Color.YELLOW);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Momento en el que se crea el menú de opciones
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;//Mostrar el menu
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.instr){
            DialogoInstr dialogo=new DialogoInstr();
            dialogo.show(getSupportFragmentManager(),"Dialogo Instruciones");
        }
        else if(item.getItemId()==R.id.rej){
            crearTablero(8,8,10);
        }
        else if (item.getItemId() == R.id.cej) {
            DialogoCEJ dialogo=new DialogoCEJ();
            dialogo.show(getSupportFragmentManager(),"Dialogo Dificultad");
        }
        else if(item.getItemId()==R.id.skin){
            DialogoMinas dialogo=new DialogoMinas();
            dialogo.show(getSupportFragmentManager(),"Dialogo IMG Minas");
        }
        return super.onOptionsItemSelected(item);
    }


    public void crearTablero(int filas, int columnas, int minas) {
        g.removeAllViews();
        tablero = new Tablero(filas, columnas, minas);
        tablero.Inicializar();
        this.filas=filas;
        this.columnas=columnas;
        this.minas=minas;
        this.contadorMinas=0;
        ponerCasillas();
    }

    public void actualizarImagenMinas(int nuevaImagen) {
        for (int x = 0; x < this.columnas; x++) {
            for (int y = 0; y < this.filas; y++) {
                if (tablero.esMina(x, y) && casillasReveladas[x][y]) {
                    botonesMina[x][y].setImageResource(nuevaImagen); // Cambia la imagen de la mina
                    imagen=nuevaImagen;
                }
                else if (tablero.esMina(x, y) && !casillasReveladas[x][y]){
                    imagen=nuevaImagen;
                }
            }
        }
    }

    private void revelarCasillasVacias(int x, int y) {
        if (casillasReveladas[x][y] || tablero.esMina(x, y)) {
            return; // No hacemos nada si la casilla ya está revelada o es una mina
        }
        casillasReveladas[x][y] = true; // Marcamos la casilla como revelada

        Button boton = (Button) g.getChildAt(x * columnas + y);

        int minasAlrededor = tablero.getMinasAlrededor(x, y);

        if (minasAlrededor == 0) {
            boton.setBackgroundColor(Color.RED);
            int[] px = {-1, -1, -1, 0, 0, 1, 1, 1};
            int[] py = {-1, 0, 1, -1, 1, -1, 0, 1};

            for (int i = 0; i < px.length; i++) {
                int nuevaX = x + px[i];
                int nuevaY = y + py[i];

                if (nuevaX >= 0 && nuevaX < filas && nuevaY >= 0 && nuevaY < columnas) {
                    revelarCasillasVacias(nuevaX, nuevaY);
                }
            }
        }
    }

    private void revelarTodasLasMinas() {
        for (int x = 0; x < this.columnas; x++) {
            for (int y = 0; y < this.filas; y++) {
                if (tablero.esMina(x, y)) {
                    casillasReveladas[x][y] = true;
                    botonesMina[x][y].setImageResource(imagen);
                    botonesMina[x][y].setEnabled(false);
                }
            }
        }
    }


}
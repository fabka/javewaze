package fabi.javewaze;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import fabi.javewaze.R;

public class MainActivity extends AppCompatActivity implements Serializable {

    public static int NUMERO_MEDALLAS = 8;
    ImageView medalla1;
    ImageView medalla2;
    ImageView medalla3;
    ImageView medalla4;
    ImageView medalla5;
    ImageView medalla6;
    ImageView medalla7;
    ImageView medalla8;
    ImageView fotoPerfil;
    EditText nombre_editText;
    EditText carrera_editText;
    Spinner estado_spinner;
    public static Sistema sistema = new Sistema(null);
    GPSTracker gps;
    public NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        //persistir();
        try {
            leerArchivo();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        gps = GPSTracker.getInstance(this);
        /*Intent i = new Intent(this, EstatuaActivity.class );
        //Intent i = new Intent(this, EstatuaActivity.class );
        Intent i = new Intent(this, CafeteriaActivity.class );
        startActivity(i);*/

        medalla1 = (ImageView)findViewById(R.id.medalla1_imageView_main);
        medalla2 = (ImageView)findViewById(R.id.medalla2_imageView_main);
        medalla3 = (ImageView)findViewById(R.id.medalla3_imageView_main);
        medalla4 = (ImageView)findViewById(R.id.medalla4_imageView_main);
        medalla5 = (ImageView)findViewById(R.id.medalla5_imageView_main);
        medalla6 = (ImageView)findViewById(R.id.medalla6_imageView_main);
        medalla7 = (ImageView)findViewById(R.id.medalla7_imageView_main);
        medalla8 = (ImageView)findViewById(R.id.medalla8_imageView_main);

        medalla1.setImageResource(R.mipmap.medal_star);
        medalla2.setImageResource(R.mipmap.medal_star);
        medalla3.setImageResource(R.mipmap.medal_star);
        medalla4.setImageResource(R.mipmap.medal_star);
        medalla5.setImageResource(R.mipmap.medal_star);
        medalla6.setImageResource(R.mipmap.medal_star);
        medalla7.setImageResource(R.mipmap.medal_star);
        medalla8.setImageResource(R.mipmap.medal_star);

        fotoPerfil = (ImageView)findViewById(R.id.profilepicture_imageView_main);
        nombre_editText = (EditText)findViewById(R.id.nombre_editText_main);
        carrera_editText = (EditText)findViewById(R.id.carrera_editText_main);
        estado_spinner = (Spinner)findViewById(R.id.estado_spinner_main);
    }

    protected static Sistema getSistema(){
        return sistema;
    }

    protected void persistir(){
        //
        String san = "Representación de figura masculina en posición pedestre, viste habito Jesuita con casulla, " +
                "cabellos cortos peinados hacia atrás, con barba y bigote, su brazo derecho se encuentra doblada a la " +
                "altura del pecho, sujetando con su mano la parte frontal de la casulla, su brazo izquierdo lo lleva estirado " +
                "y con la mano sostiene un libro cerrado";
        String vel = "Escultura de bulto redondo. Composición en triángulos y tubo metálico de tonalidad rojiza, la obra se encuentra " +
                "anclada al piso con un elemento piramidal sujetado por ocho tornillos; sobre esta un tubo el cual sostiene en la parte " +
                "superior tres triángulos doblados que simulan una veleta.";
        Estatua e = new Estatua(1, "San Francisco Javier" , R.mipmap.francisco , "Fernando Montañez", "1994" , san);
        Estatua e2 = new Estatua(2, "Velas al viento" , R.mipmap.velas , "Mauricio Arango", "2000" , vel);

        Producto p1 = new Producto(1000 ,  "Té");
        Producto p2 = new Producto(2300 ,  "Pescadito");
        Producto p3 = new Producto(1800 ,  "Empanada");
        Producto p4 = new Producto(2100 ,  "Arepa");
        Producto p5 = new Producto(1300 ,  "Tinto Pequeño");

        Cafeteria c1 = new Cafeteria(1 , "Kiosko Ingeniería" ,  R.mipmap.kiosko );
        Cafeteria c2 = new Cafeteria(2 , "Pacera" ,  R.mipmap.pecera );

        c1.productos.add(p1);
        c1.productos.add(p2);
        c1.productos.add(p3);
        c1.productos.add(p4);
        c1.productos.add(p5);

        c2.productos.add(p4);
        c2.productos.add(p3);
        c2.productos.add(p2);
        c2.productos.add(p1);

        Obra o1 = new Obra(1 , "Obra Ingenieria" ,  R.mipmap.obrainge);
        Obra o2 = new Obra(1 , "Obra Cubos" ,  R.mipmap.obracubos);

        Medalla m1 = new Medalla("m1" , false);
        Medalla m2 = new Medalla("m2" , false);
        Medalla m3 = new Medalla("m3" , false);
        Medalla m4 = new Medalla("m4" , false);
        Medalla m5 = new Medalla("m5" , false);
        Medalla m6 = new Medalla("m6" , false);

        Persona p = new Persona(" " , 0 , " " , " ");
        p.medallas.add(m1);
        p.medallas.add(m2);
        p.medallas.add(m3);
        p.medallas.add(m4);
        p.medallas.add(m5);
        p.medallas.add(m6);

        Evento ev1 = new Evento(1, 1, 44.900, -53.847, 45.465, -52.388); //Francisco
        Evento ev2 = new Evento(2, 1, 38.995, -52.718, 39.265, -52.334); //Velas
        Evento ev3 = new Evento(1, 2, 35.902, -49.614, 37.308, -49.200); //Kiosko
        Evento ev4 = new Evento(2, 2, 41.070, -53.540, 41.831, -52.443); //Pecera
        Evento ev5 = new Evento(1, 3, 37.043, -52.141, 38.484, -50.026); //Ingenieria
        Evento ev6 = new Evento(2, 3, 38.298, -55.217, 40.777, -53.185); //Cubos

        sistema.estatuas.add(e);
        sistema.estatuas.add(e2);
        sistema.cafeterias.add(c1);
        sistema.cafeterias.add(c2);
        sistema.obras.add(o1);
        sistema.obras.add(o2);
        sistema.persona = p;
        sistema.eventos.add(ev1);
        sistema.eventos.add(ev2);
        sistema.eventos.add(ev3);
        sistema.eventos.add(ev4);
        sistema.eventos.add(ev5);
        sistema.eventos.add(ev6);

        try {
            File tarjeta = Environment.getExternalStorageDirectory();
            File file = new File(tarjeta.getAbsolutePath(), "persistencia.obj");
            ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(file));
            salida.writeObject(sistema);
            salida.flush();
            salida.close();
            Toast.makeText(this, "Los datos fueron grabados correctamente", Toast.LENGTH_SHORT).show();
        } catch (IOException ioe) {
        }

        //
    }

    protected void leerArchivo () throws IOException, ClassNotFoundException {
        File tarjeta = Environment.getExternalStorageDirectory();
        File file = new File(tarjeta.getAbsolutePath(), "persistencia.obj");
        ObjectInputStream entrada=new ObjectInputStream(new FileInputStream(file));
        Sistema obj1=(Sistema)entrada.readObject();
        entrada.close();
        sistema = obj1;

        //Toast.makeText(this," " + sistema.persona.edad+" "+ sistema.estatuas.get(1).nombre,Toast.LENGTH_LONG).show();

    }


    public class Estatua implements Serializable{
        public int id;
        public String nombre;
        public int foto;
        public String creador;
        public String fecha;
        public String info;

        public Estatua(int id, String nombre, int foto, String creador, String fecha, String info) {
            this.id = id;
            this.nombre = nombre;
            this.foto = foto;
            this.creador = creador;
            this.fecha = fecha;
            this.info = info;
        }
    }

    public class Producto implements Serializable{
        public int precio;
        public String nombre;

        public Producto(int precio, String nombre) {
            this.precio = precio;
            this.nombre = nombre;
        }
    }

    public class Cafeteria implements Serializable{
        public int id;
        public String nombre;
        public int foto;
        public List<Producto> productos;

        public Cafeteria(int id, String nombre, int foto) {
            this.id = id;
            this.nombre = nombre;
            this.foto = foto;
            this.productos = new ArrayList<Producto>();
        }
    }

    public class Obra implements Serializable{
        public int id;
        public String nombre;
        public int foto;

        public Obra(int id, String nombre, int foto) {
            this.id = id;
            this.nombre = nombre;
            this.foto = foto;
        }
    }

    public class Evento implements Serializable{
        public int id;
        public int tipo;
        public Double infizqlat;
        public Double infizqlon;
        public Double supderlat;
        public Double supderlon;

        public Evento(int id, int tipo, Double infizqlat, Double infizqlon, Double supderlat, Double supderlon) {
            this.id = id;
            this.tipo = tipo;
            this.infizqlat = infizqlat;
            this.infizqlon = infizqlon;
            this.supderlat = supderlat;
            this.supderlon = supderlon;
        }
    }


    public class Persona implements Serializable{
        public String nombre;
        public int edad;
        public String sexo;
        public String estado;
        List<Medalla> medallas;

        public Persona(String nombre, int edad, String sexo, String estado) {
            this.nombre = nombre;
            this.edad = edad;
            this.sexo = sexo;
            this.estado = estado;
            this.medallas = new ArrayList<Medalla>();
        }
    }

    public class Medalla implements Serializable{
        public String nombre;
        public boolean latiene;

        public Medalla(String nombre, boolean latiene) {
            this.nombre = nombre;
            this.latiene = latiene;
        }
    }

    public static class Sistema implements Serializable{
        List<Estatua> estatuas;
        List<Cafeteria> cafeterias;
        List<Obra> obras;
        Persona persona;
        List<Evento> eventos;

        public Sistema(Persona persona) {
            this.estatuas =  new ArrayList<Estatua>();
            this.cafeterias  = new ArrayList<Cafeteria>();
            this.obras = new ArrayList<Obra>();
            this.persona = persona;
            this.eventos = new ArrayList<Evento>();
        }

    }

}

package fabi.javewaze;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Serializable, AdapterView.OnItemSelectedListener {

    public static final String OBRA_INGENIERIA = "Obra Ingenieria";
    public static final String OBRA_CUBOS = "Obra Cubos";
    public static final String ESTATUA_VELASALVIENTO = "Velas al viento";
    public static final String ESTATUA_SANFRANCISCOJAVIER = "San Francisco Javier";
    public static final String CAFETERIA_KIOSCO = "Kiosko Ingeniería";
    public static final String CAFETERIA_PECERA = "Pecera";

    public static final String MODO_TODOTERRENO = "MODO_TODOTERRENO";
    public static final String MODO_MUSEO = "MODO_MUSEO";
    public static final String MODO_COMIDA = "MODO_COMIDA";



    public static final int NUMERO_MEDALLAS = 6;

    ImageView medalla1;
    ImageView medalla2;
    ImageView medalla3;
    ImageView medalla4;
    ImageView medalla5;
    ImageView medalla6;
    ImageView fotoPerfil;
    EditText nombre_editText;
    EditText carrera_editText;
    Spinner estado_spinner;
    Button editarButton;

    public static Sistema sistema = new Sistema(null);
    GPSTracker gps;
    public NotificationManager mNotificationManager;
    private static boolean activityVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        final String PREFS_NAME = "MyPrefsFile";
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        if (settings.getBoolean("my_first_time", true)) {
            Log.d("mainActivity","Entro por primera vez");
            settings.edit().putBoolean("my_first_time", false).commit();
            iniciarSistema();
            persistir();
        }

        try {
            leerArchivo();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("archivo ", e.toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        gps = GPSTracker.getInstance(this, mNotificationManager);
        /*Intent i = new Intent(this, EstatuaActivity.class );
        //Intent i = new Intent(this, EstatuaActivity.class );
        Intent i = new Intent(this, CafeteriaActivity.class );
        startActivity(i);*/

        medalla1 = (ImageView) findViewById(R.id.medalla1_imageView_main);
        medalla2 = (ImageView) findViewById(R.id.medalla2_imageView_main);
        medalla3 = (ImageView) findViewById(R.id.medalla3_imageView_main);
        medalla4 = (ImageView) findViewById(R.id.medalla4_imageView_main);
        medalla5 = (ImageView) findViewById(R.id.medalla5_imageView_main);
        medalla6 = (ImageView) findViewById(R.id.medalla6_imageView_main);

        actualizarMedallas();

        //fotoPerfil = (ImageView) findViewById(R.id.profilepicture_imageView_main);
        nombre_editText = (EditText) findViewById(R.id.nombre_editText_main);
        carrera_editText = (EditText) findViewById(R.id.carrera_editText_main);
        estado_spinner = (Spinner) findViewById(R.id.estado_spinner_main);
        estado_spinner.setOnItemSelectedListener(this);
        //editarButton = (Button) findViewById(R.id.editarfotoperfil_button_main);
    }

    public void estatuaTest(View v){
        Intent i = new Intent(this , CafeteriaActivity.class);
        i.putExtra("id" , 2);
        startActivity(i);
    }

    public void pruebaMedallas(){
        int pos = Integer.parseInt(carrera_editText.getText().toString());
        switch (pos){
            case 0:
                sistema.persona.cambiarEstadoMedalla(OBRA_INGENIERIA);
                break;
            case 1:
                sistema.persona.cambiarEstadoMedalla(OBRA_CUBOS);
                break;
            case 2:
                sistema.persona.cambiarEstadoMedalla(ESTATUA_VELASALVIENTO);
                break;
            case 3:
                sistema.persona.cambiarEstadoMedalla(ESTATUA_SANFRANCISCOJAVIER);
                break;
            case 4:
                sistema.persona.cambiarEstadoMedalla(CAFETERIA_KIOSCO);
                break;
            case 5:
                sistema.persona.cambiarEstadoMedalla(CAFETERIA_PECERA);
                break;
        }
        actualizarMedallas();
        persistir();
    }

    public void actualizarMedallas(){
        if (sistema.persona.medallas.get(0).latiene)
            medalla1.setImageResource(R.mipmap.medalla_obra_ingenieria);
        else
            medalla1.setImageResource(R.mipmap.medal_star);

        if (sistema.persona.medallas.get(1).latiene)
            medalla2.setImageResource(R.mipmap.medalla_obra_cubos);
        else
            medalla2.setImageResource(R.mipmap.medal_star);

        if (sistema.persona.medallas.get(2).latiene)
            medalla3.setImageResource(R.mipmap.medalla_estatua_velasalviento);
        else
            medalla3.setImageResource(R.mipmap.medal_star);

        if (sistema.persona.medallas.get(3).latiene)
            medalla4.setImageResource(R.mipmap.medalla_estatua_sanfranciscojavier);
        else
            medalla4.setImageResource(R.mipmap.medal_star);

        if (sistema.persona.medallas.get(4).latiene)
            medalla5.setImageResource(R.mipmap.medalla_cafeteria_kiosco);
        else
            medalla5.setImageResource(R.mipmap.medal_star);

        if (sistema.persona.medallas.get(5).latiene)
            medalla6.setImageResource(R.mipmap.medalla_cafeteria_pecera);
        else
            medalla6.setImageResource(R.mipmap.medal_star);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MainActivity.activityResumed();
        actualizarMedallas();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MainActivity.activityPaused();
        actualizarMedallas();
    }

    protected static Sistema getSistema() {
        return sistema;
    }

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }

    public static void persistir() {
        try {
            File tarjeta = Environment.getExternalStorageDirectory();
            File file = new File(tarjeta.getAbsolutePath(), "persistencia.obj");
            ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(file));
            Log.d("mainActivity","guardando");
            for( Medalla m: sistema.persona.medallas){
                if (m.latiene)
                    Log.d("mainActivity","tiene "+m.nombre);
                else
                    Log.d("mainActivity","No tiene "+m.nombre);
            }
            salida.writeObject(sistema);
            salida.flush();
            salida.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        //
    }

    public static void persistir( Context c) {
        try {
            File tarjeta = Environment.getExternalStorageDirectory();
            File file = new File(tarjeta.getAbsolutePath(), "persistencia.obj");
            ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(file));
            salida.writeObject(sistema);
            salida.flush();
            salida.close();
            //Toast.makeText(c,"Archivo Guardado",Toast.LENGTH_LONG).show();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        //
    }

    protected void leerArchivo() throws IOException, ClassNotFoundException {
        File tarjeta = Environment.getExternalStorageDirectory();
        File file = new File(tarjeta.getAbsolutePath(), "persistencia.obj");
        ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(file));
        Sistema obj1 = (Sistema) entrada.readObject();
        entrada.close();
        sistema = obj1;
        Log.d("mainActivity","leyendo");
        for( Medalla m: sistema.persona.medallas){
            if (m.latiene)
                Log.d("mainActivity","tiene "+m.nombre);
            else
                Log.d("mainActivity","No tiene "+m.nombre);
        }
        //Toast.makeText(this," " + sistema.persona.edad+" "+ sistema.estatuas.get(1).nombre,Toast.LENGTH_LONG).show();

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position){
            case 0:
                sistema.persona.estado = MODO_TODOTERRENO;
                break;
            case 1:
                sistema.persona.estado = MODO_COMIDA;
                break;
            case 2:
                sistema.persona.estado = MODO_MUSEO;
                break;
            default:
                sistema.persona.estado = MODO_TODOTERRENO;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void iniciarSistema(){

        String san = "Representación de figura masculina en posición pedestre, viste habito Jesuita con casulla, " +
                "cabellos cortos peinados hacia atrás, con barba y bigote, su brazo derecho se encuentra doblada a la " +
                "altura del pecho, sujetando con su mano la parte frontal de la casulla, su brazo izquierdo lo lleva estirado " +
                "y con la mano sostiene un libro cerrado";
        String vel = "Escultura de bulto redondo. Composición en triángulos y tubo metálico de tonalidad rojiza, la obra se encuentra " +
                "anclada al piso con un elemento piramidal sujetado por ocho tornillos; sobre esta un tubo el cual sostiene en la parte " +
                "superior tres triángulos doblados que simulan una veleta.";
        Estatua e = new Estatua(1, "San Francisco Javier", R.mipmap.francisco, "Fernando Montañez", "1994", san);
        Estatua e2 = new Estatua(2, "Velas al viento", R.mipmap.velas, "Mauricio Arango", "2000", vel);

        Producto p1 = new Producto(1000, "Te");
        Producto p2 = new Producto(2300, "Pescadito");
        Producto p3 = new Producto(1800, "Empanada");
        Producto p4 = new Producto(2100, "Arepa");
        Producto p5 = new Producto(1300, "Tinto Pequeño");

        Cafeteria c1 = new Cafeteria(1, "Kiosko Ingeniería", R.mipmap.kiosko);
        Cafeteria c2 = new Cafeteria(2, "Pecera", R.mipmap.pecera);

        c1.productos.add(p1);
        c1.productos.add(p2);
        c1.productos.add(p3);
        c1.productos.add(p4);
        c1.productos.add(p5);

        c2.productos.add(p4);
        c2.productos.add(p3);
        c2.productos.add(p2);
        c2.productos.add(p1);
        c2.productos.add(p5);

        Obra o1 = new Obra(1 , "Obra Ingenieria" ,  R.mipmap.obrainge);
        Obra o2 = new Obra(2 , "Obra Cubos" ,  R.mipmap.obracubos);

        Medalla m1 = new Medalla(OBRA_INGENIERIA, false);
        Medalla m2 = new Medalla(OBRA_CUBOS, false);
        Medalla m3 = new Medalla(ESTATUA_VELASALVIENTO, false);
        Medalla m4 = new Medalla(ESTATUA_SANFRANCISCOJAVIER, false);
        Medalla m5 = new Medalla(CAFETERIA_KIOSCO, false);
        Medalla m6 = new Medalla(CAFETERIA_PECERA, false);

        Persona p = new Persona(" " , 0 , " " , "todo");

        p.medallas.add(m1);
        p.medallas.add(m2);
        p.medallas.add(m3);
        p.medallas.add(m4);
        p.medallas.add(m5);
        p.medallas.add(m6);

        /*Evento ev1 = new Evento(1, 1, 44.900, -53.847, 45.465, -52.388); //Francisco
        Evento ev2 = new Evento(2, 1, 38.995, -52.718, 39.265, -52.334); //Velas
        Evento ev3 = new Evento(1, 2, 35.902, -49.614, 37.308, -49.200); //Kiosko
        Evento ev4 = new Evento(2, 2, 41.070, -53.540, 41.831, -52.443); //Pecera
        Evento ev5 = new Evento(1, 3, 37.043, -52.141, 38.484, -50.026); //Ingenieria
        Evento ev6 = new Evento(2, 3, 38.298, -55.217, 40.777, -53.185); //Cubos */

        Evento ev1 = new Evento(1, 1, 44.900, -53.8, 45.6, -53.0); //Francisco
        Evento ev2 = new Evento(2, 1, 38.5, -53.0, 40.0, -52.1); //Velas
        Evento ev3 = new Evento(1, 2, 35.5, -50.5, 39.5, -46.0); //Kiosko
        Evento ev4 = new Evento(2, 2, 40.2, -53.15, 41.9, -52.2); //Pecera
        Evento ev5 = new Evento(1, 3, 37.4, -53.2, 40.01, -48.5); //Ingenieria
        Evento ev6 = new Evento(2, 3, 38.32, -55.3, 40.2, -52.3); //Cubos

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
    }

    public class Estatua implements Serializable {
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

    public class Producto implements Serializable {
        public int precio;
        public String nombre;

        public Producto(int precio, String nombre) {
            this.precio = precio;
            this.nombre = nombre;
        }
    }

    public class Cafeteria implements Serializable {
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

    public class Obra implements Serializable {
        public int id;
        public String nombre;
        public int foto;

        public Obra(int id, String nombre, int foto) {
            this.id = id;
            this.nombre = nombre;
            this.foto = foto;
        }
    }

    public class Evento implements Serializable {
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


    public class Persona implements Serializable {
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

        public boolean cambiarEstadoMedalla(String nombre) {
            for (Medalla m : medallas) {
                if (m.nombre.equals(nombre)) {
                    m.latiene = true;
                    return true;
                }
            }
            return false;
        }
    }

    public class Medalla implements Serializable {
        public String nombre;
        public boolean latiene;

        public Medalla(String nombre, boolean latiene) {
            this.nombre = nombre;
            this.latiene = latiene;
        }
    }

    public static class Sistema implements Serializable {
        List<Estatua> estatuas;
        List<Cafeteria> cafeterias;
        List<Obra> obras;
        Persona persona;
        List<Evento> eventos;

        public Sistema(Persona persona) {
            this.estatuas = new ArrayList<Estatua>();
            this.cafeterias = new ArrayList<Cafeteria>();
            this.obras = new ArrayList<Obra>();
            this.persona = persona;
            this.eventos = new ArrayList<Evento>();
        }

    }

}

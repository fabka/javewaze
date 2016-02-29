package fabi.javewaze;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.List;
import fabi.javewaze.R;

public class MainActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        Intent i = new Intent(this, EstatuaActivity.class );
        startActivity(i);
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
}

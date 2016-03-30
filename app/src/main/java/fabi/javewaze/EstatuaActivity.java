package fabi.javewaze;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class EstatuaActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    public ImageView im, foto;
    private static boolean activityVisible = false;
    TextView creador , inf, nombreest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estatua);
        foto = (ImageView) findViewById(R.id.imageFoto_image_estatua );
        im = (ImageView) findViewById(R.id.imageEstatua_image_estatua );
        creador = (TextView) findViewById(R.id.textView2 );

        inf = (TextView) findViewById(R.id.textView5 );
        nombreest = (TextView) findViewById(R.id.textView );
        //foto.getLayoutParams().height=200;
        //foto.getLayoutParams().width=100;
        //im.getLayoutParams().height=200;
        //im.getLayoutParams().width=100;
        im.setScaleType(ImageView.ScaleType.FIT_XY);
        foto.setScaleType(ImageView.ScaleType.FIT_XY);
        Intent i = getIntent();
        Bundle b = i.getExtras();
        int id = b.getInt("id");
        for(MainActivity.Estatua e :  MainActivity.getSistema().estatuas){
            if(id == e.id){
                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                im.setImageResource(e.foto);
                im.getLayoutParams().height = (int) (size.y*0.25);
                foto.getLayoutParams().height = (int) (size.y*0.25);
                nombreest.setTextSize(18);
                nombreest.setText(e.nombre + "  (" + e.fecha + ")");
                creador.setTextSize(15);
                creador.setText("Creado por: " +e.creador);
                inf.setTextSize(12);
                inf.setText(e.info);
            }
        }

        //im.setImageResource(R.mipmap.im_biblio);
    }

    protected void onStart(){
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        EstatuaActivity.activityResumed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        EstatuaActivity.activityPaused();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void click (View v){ dispatchTakePictureIntent(); }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            foto.setImageBitmap(imageBitmap);
            foto.setMaxWidth(2000);
            if(this.nombreest.getText().equals("San Francisco Javier"))
                MainActivity.getSistema().persona.cambiarEstadoMedalla(MainActivity.ESTATUA_SANFRANCISCOJAVIER);
            else
                MainActivity.getSistema().persona.cambiarEstadoMedalla(MainActivity.ESTATUA_VELASALVIENTO);
            MainActivity.persistir();
        }
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

}

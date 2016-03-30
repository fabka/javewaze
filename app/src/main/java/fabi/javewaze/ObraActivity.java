package fabi.javewaze;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ObraActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    public ImageView im, foto;
    private static boolean activityVisible = false;
    TextView nombreobra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obra);
        foto = (ImageView) findViewById(R.id.imageFoto_image_obra );
        im = (ImageView) findViewById(R.id.imageEstatua_image_obra );
        nombreobra = (TextView) findViewById(R.id.textView4 );
        //foto.getLayoutParams().height=200;
        //foto.getLayoutParams().width=100;
        //im.getLayoutParams().height=200;
        //im.getLayoutParams().width=100;
        im.setScaleType(ImageView.ScaleType.FIT_XY);
        foto.setScaleType(ImageView.ScaleType.FIT_XY);
        Intent i = getIntent();
        Bundle b = i.getExtras();
        int id = b.getInt("id");
        for(MainActivity.Obra e :  MainActivity.getSistema().obras){
            if(id == e.id){
                im.setImageResource(e.foto);
                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                im.getLayoutParams().height = (int) (size.y*0.25);
                foto.getLayoutParams().height = (int) (size.y*0.25);
                nombreobra.setTextSize(18);
                nombreobra.setText(e.nombre);
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
        ObraActivity.activityResumed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ObraActivity.activityPaused();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void click3 (View v){ dispatchTakePictureIntent();  }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            foto.setImageBitmap(imageBitmap);
            foto.setMaxWidth(2000);
            if(this.nombreobra.getText().equals("Obra Ingeniería"))
                MainActivity.getSistema().persona.cambiarEstadoMedalla(MainActivity.OBRA_INGENIERIA);
            else
                MainActivity.getSistema().persona.cambiarEstadoMedalla(MainActivity.OBRA_CUBOS);
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

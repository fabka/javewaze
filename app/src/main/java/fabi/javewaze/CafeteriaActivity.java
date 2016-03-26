package fabi.javewaze;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class CafeteriaActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    public ImageView im, foto;
    private static boolean activityVisible = false;
    TextView nombrecaf, pro1 , pre1, pro2 , pre2, pro3 , pre3, pro4 , pre4, pro5 , pre5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("Cafetae" , "Llega");
        setContentView(R.layout.activity_cafeteria);
        foto = (ImageView) findViewById(R.id.imageFoto_image_cafeteria );
        im = (ImageView) findViewById(R.id.imageCafeteria_image_cafeteria );
        nombrecaf = (TextView) findViewById(R.id.textView3 );
        pro1 = (TextView) findViewById(R.id.tv_prod1 );
        pro2 = (TextView) findViewById(R.id.tv_prod2 );
        pro3 = (TextView) findViewById(R.id.tv_prod3 );
        pro4 = (TextView) findViewById(R.id.tv_prod4 );
        pro5 = (TextView) findViewById(R.id.tv_prod5 );
        pre1 = (TextView) findViewById(R.id.tv_precio1 );
        pre2 = (TextView) findViewById(R.id.tv_precio2 );
        pre3 = (TextView) findViewById(R.id.tv_precio3 );
        pre4 = (TextView) findViewById(R.id.tv_precio4 );
        pre5 = (TextView) findViewById(R.id.tv_precio5 );
        foto.getLayoutParams().height=200;
        foto.getLayoutParams().width=200;
        im.getLayoutParams().height=200;
        im.getLayoutParams().width=200;
        //im.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Intent i = getIntent();
        Bundle b = i.getExtras();
        int id = b.getInt("id");
        for(MainActivity.Cafeteria e :  MainActivity.getSistema().cafeterias){
            if(id == e.id){
                im.setImageResource(e.foto);
                nombrecaf.setTextSize(20);
                nombrecaf.setText(e.nombre);
                pro1.setText(e.productos.get(0).nombre);
                pro2.setText(e.productos.get(1).nombre);
                pro3.setText(e.productos.get(2).nombre);
                pro4.setText(e.productos.get(3).nombre);
                pro5.setText(e.productos.get(4).nombre);
                pre1.setText(e.productos.get(0).precio + " ");
                pre2.setText(e.productos.get(1).precio + " ");
                pre3.setText(e.productos.get(2).precio + " ");
                pre4.setText(e.productos.get(3).precio + " ");
                pre5.setText(e.productos.get(4).precio + " ");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        CafeteriaActivity.activityResumed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        CafeteriaActivity.activityPaused();
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void click2 (View v){
        dispatchTakePictureIntent();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            foto.setImageBitmap(imageBitmap);

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

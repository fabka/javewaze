package fabi.javewaze;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class EstatuaActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    public ImageView im, foto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estatua);
        foto = (ImageView) findViewById(R.id.imageFoto_image_estatua );
        im = (ImageView) findViewById(R.id.imageEstatua_image_estatua );
        foto.getLayoutParams().height=300;
        foto.getLayoutParams().width=300;
        im.getLayoutParams().height=300;
        im.getLayoutParams().width=300;
        //im.setScaleType(ImageView.ScaleType.CENTER_CROP);
        im.setImageResource(R.mipmap.im_biblio);
    }

    protected void onStart(){
        super.onStart();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void click (View v){
        dispatchTakePictureIntent();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            foto.setImageBitmap(imageBitmap);

        }
    }

}

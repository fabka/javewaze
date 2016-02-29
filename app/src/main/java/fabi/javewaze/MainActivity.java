package fabi.javewaze;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import fabi.javewaze.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        //Intent i = new Intent(this, EstatuaActivity.class );
        Intent i = new Intent(this, CafeteriaActivity.class );
        startActivity(i);

    }
}

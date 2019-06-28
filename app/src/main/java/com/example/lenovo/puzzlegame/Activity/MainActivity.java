package com.example.lenovo.puzzlegame.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.lenovo.puzzlegame.Utils.MyApp;
import com.example.lenovo.puzzlegame.R;

public class MainActivity extends AppCompatActivity {
    private MyApp myApp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myApp=(MyApp) getApplication();
        String username=myApp.getUsername();
        Toast.makeText(this, username+"！", Toast.LENGTH_SHORT).show();

    }
}

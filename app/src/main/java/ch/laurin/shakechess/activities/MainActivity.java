package ch.laurin.shakechess.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import ch.laurin.shakechess.R;

public class MainActivity extends AppCompatActivity {
    private Button startButton;
    private Button resumeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startButton = (Button) findViewById(R.id.startbutton);
        resumeButton = (Button) findViewById(R.id.resumebutton);
        startButton.setOnClickListener(v ->  {
            Intent myIntent = new Intent(MainActivity.this, GameActivity.class);
            MainActivity.this.startActivity(myIntent);
        });
    }



}
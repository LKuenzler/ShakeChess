package ch.laurin.shakechess.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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
        checkIfThereIsAGameToContinue();
        startButton.setOnClickListener(v ->  {
            Intent myIntent = new Intent(MainActivity.this, GameActivity.class);
            Bundle bundle = new Bundle();
            bundle.putBoolean("resumeGame", false);
            myIntent.putExtras(bundle);
            MainActivity.this.startActivity(myIntent);
        });
        resumeButton.setOnClickListener(v -> {
            Intent myIntent = new Intent(MainActivity.this, GameActivity.class);
            Bundle bundle = new Bundle();
            bundle.putBoolean("resumeGame", true);
            myIntent.putExtras(bundle);
            MainActivity.this.startActivity(myIntent);
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        checkIfThereIsAGameToContinue();
    }

    private boolean checkIfThereIsAGameToContinue() {
        SharedPreferences sharedPreferences = getSharedPreferences("shakeChess", MODE_PRIVATE);
        String lastGame = sharedPreferences.getString("lastGame", "");
        if(lastGame.equals("")) {
            resumeButton.setEnabled(false);
            return false;
        } else {
            resumeButton.setEnabled(true);
            return true;
        }
    }
}
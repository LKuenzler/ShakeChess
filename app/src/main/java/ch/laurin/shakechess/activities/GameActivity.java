package ch.laurin.shakechess.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import ch.laurin.shakechess.GameController;
import ch.laurin.shakechess.R;
import ch.laurin.shakechess.model.ChessBoard;
import ch.laurin.shakechess.services.ApiService;

public class GameActivity extends AppCompatActivity {
    //Sensors
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private float mAccel = 10f;
    private float mAccelCurrent = SensorManager.GRAVITY_EARTH;
    private float mAccelLast = SensorManager.GRAVITY_EARTH;
    //Game
    private HashMap<String, Button> viewChessBoard = new HashMap<>();
    private GameController gameController;
    private Button selectedField = null;
    //Service
    private ApiService apiService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        //Service
        Intent bindBMIServiceIntent = new Intent(this, ApiService.class);
        bindService(bindBMIServiceIntent, connection, Context.BIND_AUTO_CREATE);
        //Sensors
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(mSensorListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;

    }


    @Override
    protected void onPause() {
        sensorManager.unregisterListener(mSensorListener);
        SharedPreferences sharedPreferences = getSharedPreferences("shakeChess", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        if(!gameController.isGameFinished()) {
            Gson gson = new Gson();
            String json = gson.toJson(gameController.getChessBoard());
            prefsEditor.putString("lastGame", json);
            prefsEditor.apply();
        } else {
            prefsEditor.putString("lastGame", "");
            prefsEditor.apply();
        }
        super.onPause();
    }

    private void startGame() {
        Bundle bundle = getIntent().getExtras();
        if (bundle.getBoolean("resumeGame")) {
            SharedPreferences sharedPreferences = getSharedPreferences("shakeChess", MODE_PRIVATE);
            Gson gson = new Gson();
            ChessBoard chessBoard = gson.fromJson(sharedPreferences.getString("lastGame", ""), ChessBoard.class);
            loadChessBoard();
            gameController = new GameController(this, apiService, chessBoard);
        } else {
            loadChessBoard();
            gameController = new GameController(this, apiService);
        }
    }

    public void showText(String text) {
        runOnUiThread(() -> {
            TextView textView = (TextView) findViewById(R.id.text);
            textView.setText(text);
        });
    }

    public void updateViewChessBoard(ChessBoard board) {
        board.getBoard().forEach((position, chessField) -> {
            if (!chessField.isEmpty()) {
                Button viewField = viewChessBoard.get(position);
                viewField.setText(chessField.getPiece().getPieceName());
                if (chessField.getPiece().getColor().equals("white")) {
                    viewField.setTextColor(Color.WHITE);
                } else {
                    viewField.setTextColor(Color.BLACK);
                }
            } else {
                Button viewField = viewChessBoard.get(position);
                viewField.setText("");
            }
        });
    }

    private void loadChessBoard() {
        String[] columns = {"a", "b", "c", "d", "e", "f", "g", "h"};
        boolean fieldIsDark = true;
        for (int columnCounter = 0; columnCounter < 8; columnCounter++) {
            for (int row = 1; row <= 8; row++) {
                String id = columns[columnCounter] + row;
                Button field = (Button) findViewById(getIntId(id));
                if (fieldIsDark) {
                    field.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(190, 157, 106)));
                } else {
                    field.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(181, 101, 29)));
                }
                fieldIsDark = !fieldIsDark;
                field.setOnClickListener(v -> {
                    if (selectedField == null) {
                        selectedField = field;
                    } else {
                        gameController.makeMove(getStringId(selectedField.getId()), getStringId(field.getId()));
                        selectedField = null;
                    }
                });
                viewChessBoard.put(id, field);
            }
            fieldIsDark = !fieldIsDark;
        }
    }

    private int getIntId(String id) {
        return this.getResources().getIdentifier(id, "id", this.getPackageName());
    }

    private String getStringId(int id) {
        return getResources().getResourceEntryName(id);
    }

    private final SensorEventListener mSensorListener = new SensorEventListener() {

        public void onSensorChanged(SensorEvent se) {
            float x = se.values[0];
            float y = se.values[1];
            float z = se.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;
            if (mAccel > 2) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        gameController.takeBackLastMove();
                    }
                }, 2000);
            }
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

    };

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(mSensorListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }


    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            ApiService.ApiBinder binder = (ApiService.ApiBinder) iBinder;
            apiService = binder.getService();
            startGame();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            //TODO notify GameController
        }
    };

}
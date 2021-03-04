package ch.laurin.shakechess.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import ch.laurin.shakechess.GameController;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ApiService extends Service {

    private final IBinder binder = new ApiBinder();

    private GameController gameController;

    public class ApiBinder extends Binder {
        public ApiService getService() {
            return ApiService.this;
        }
    }

    private OkHttpClient client = new OkHttpClient();

    public void getComputerMove(String moves) {
        Request request = new Request.Builder()
                .url("http://chess-api.herokuapp.com/next_best/" + moves)
                .build();

        client.newCall(request).enqueue(new Callback() {
            public void onResponse(Call call, Response response) throws IOException {
                JSONObject body = null;
                try {
                    body = new JSONObject(response.body().string());
                    String move = body.getString("bestNext");
                    gameController.makeMove(move.substring(0, 2), move.substring(2, 4));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            public void onFailure(Call call, IOException e) {

            }
        });


    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}
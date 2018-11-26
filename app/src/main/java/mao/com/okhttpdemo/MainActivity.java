package mao.com.okhttpdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //主线程不能进行耗时操作
        new Thread(){
            @Override
            public void run() {
                super.run();
                /**
                 * 同步请求
                 */
                GetExample getexample = new GetExample();
                String syncresponse = null;
                try {
                    syncresponse = getexample.run("https://raw.github.com/square/okhttp/master/README.md");
                    System.out.println("同步请求返回参数"+syncresponse);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                /**
                 * 异步请求
                 */
                PostExample postexample = new PostExample();
                String json = postexample.bowlingJson("Jesse", "Jake");
                try {
                    String asynresponse = postexample.post("http://www.roundsapp.com/post", json);
                    System.out.println("异步请求返回参数"+asynresponse);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    class PostExample {
        final MediaType JSON = MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

        String post(String url, String json) throws IOException {
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                return response.body().string();
            }
        }

        String bowlingJson(String player1, String player2) {
            return "{'winCondition':'HIGH_SCORE',"
                    + "'name':'Bowling',"
                    + "'round':4,"
                    + "'lastSaved':1367702411696,"
                    + "'dateStarted':1367702378785,"
                    + "'players':["
                    + "{'name':'" + player1 + "','history':[10,8,6,7,8],'color':-13388315,'total':39},"
                    + "{'name':'" + player2 + "','history':[6,10,5,10,10],'color':-48060,'total':41}"
                    + "]}";
        }
    }

    class GetExample {
        OkHttpClient client = new OkHttpClient();

        String run(String url) throws IOException {
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                return response.body().string();
            }
        }
    }
}




package com.fivefivelike.mybaselibrary.http;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

/**
 * Created by 郭青枫 on 2018/8/22 0022.
 */

public abstract class OkWebsocket extends WebSocketListener {

    private static OkHttpClient client = new OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build();

    public static OkHttpClient getClient() {
        return client;
    }

    private static ScheduledExecutorService single = Executors.newSingleThreadScheduledExecutor();
    WebSocket webSocket;

    public WebSocket getWebSocket() {
        return webSocket;
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        this.webSocket = webSocket;
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        single.shutdown();
        this.webSocket = webSocket;
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        this.webSocket = webSocket;
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        this.webSocket = webSocket;
    }

    @Override
    public void onFailure(WebSocket w, Throwable t, Response response) {
        this.webSocket = w;
        single.schedule(
                new Runnable() {
                    @Override
                    public void run() {
                        client.newWebSocket(webSocket.request(), OkWebsocket.this);
                        loadHistory();
                    }
                }
                , 5, TimeUnit.SECONDS
        );
    }

    abstract void loadHistory();


    /**
     * 开始websocket连接
     */
    public void start(String url) {
        Request req = new Request.Builder().url(url).build();
        client.newWebSocket(req, this);
        loadHistory();
    }

    public void close() {
        webSocket.cancel();
    }

}

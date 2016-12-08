package org.zirco.application;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


public class App extends Application {

    private static RequestQueue queue;

    @Override
    public void onCreate() {
        super.onCreate();

        if (queue == null) {
            synchronized (App.class) {
                if (queue == null) {
                    queue = Volley.newRequestQueue(getApplicationContext());
                }
            }
        }
    }

    public static RequestQueue getQueue() {
        return queue;
    }

}

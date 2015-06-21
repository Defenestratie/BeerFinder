package com.beerfinder.beerfinder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

/**
 * Created by Florian on 21-6-2015.
 */
public class Splash extends Activity {
    private final int SPLASH_DURATION = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent splashIntent = new Intent(Splash.this, MapActivity.class);
                Splash.this.startActivity(splashIntent);
                Splash.this.finish();
            }
        }, SPLASH_DURATION);
    }
}

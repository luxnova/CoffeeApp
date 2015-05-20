package percolate.com.coffeeapp;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


/**
 * Created by JoshuaWilliams on 5/19/15.
 */
public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setUpStatusBar();
        startSplashTimer();
    }

    private void startSplashTimer() {
        new Handler().postDelayed(new Runnable() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                View sharedView = findViewById(R.id.bgView);
                String transitionName = getString(R.string.toolbar);
                ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this, sharedView, transitionName);
                startActivity(mainIntent, transitionActivityOptions.toBundle());
            }
        }, 1000);
    }



    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setUpStatusBar() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getResources().getColor(R.color.transparent));
    }
}

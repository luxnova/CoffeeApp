package percolate.com.coffeeapp.activities;

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

import percolate.com.coffeeapp.R;
import percolate.com.coffeeapp.util.Utils;


/**
 * Created by JoshuaWilliams on 5/19/15.
 *
 * @version 1.0
 * Splash activity showing the company logo for 1 second.
 */
public class SplashActivity extends AppCompatActivity {

    //Modify the length of the splash screen with this variable.
    private static final long SHOW_SPLASH_LENGTH = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Utils.setContext(getApplicationContext());
        setUpStatusBar();
        startSplashTimer();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    /**
     * Starts the handler that shows the Splash screen. Also sets up the Android-L animations if the
     * user is running 5.0.
     */
    private void startSplashTimer() {
        new Handler().postDelayed(new Runnable() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    View sharedView = findViewById(R.id.bgView);
                    String transitionName = getString(R.string.toolbar);
                    ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this, sharedView, transitionName);
                    startActivity(mainIntent, transitionActivityOptions.toBundle());
                }
                else{
                    startActivity(mainIntent);
                    overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_shrink_fade_out_from_bottom);
                }
            }
        }, SHOW_SPLASH_LENGTH);
    }


    /**
     * Turns the status bar transparent for the Android-L Users.
     */

    private void setUpStatusBar() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.transparent));
        }
    }
}

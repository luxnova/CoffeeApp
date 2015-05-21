package percolate.com.coffeeapp.activities;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import percolate.com.coffeeapp.models.Coffee;
import percolate.com.coffeeapp.adapters.CoffeeRecyclerAdapter;
import percolate.com.coffeeapp.R;
import percolate.com.coffeeapp.util.Utils;


/**
 * Created by JoshuaWilliams on 5/19/15.
 *
 * @version 1.0
 *
 * This activity is the main activity that hosts the timeline for coffee.
 */
public class MainActivity extends AppCompatActivity {

    private static final long ANIM_DURATION = 300; //how long the animation is displayed. 3/10 of a second.
    private static final String LOG_TAG = "MainActivity";

    private View view;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private boolean recyclerViewAttached = false;
    private ProgressBar progressBar;
    private ImageView statusBarBackground;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpLayout();
        setupEnterAnimations();
    }

    /**
     * Loads the coffee data for the recycler view.
     */
    private void loadData() {
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, getString(R.string.coffee_url), getResponseListener(), getErrorListener()){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", getString(R.string.api_key));
                return params;
            }
        };
        queue.add(stringRequest);

    }

    /**
     * This method finds and sets up views, sets up toolbar and colors status bar.
     */
    private void setUpLayout(){
        view = findViewById(R.id.bgView);
        setUpToolBar();
        
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View view) {
                setViewAttachment();
            }

            @Override
            public void onViewDetachedFromWindow(View view) {
                setViewAttachment();
            }
        });

        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Utils.setUpStatusBar(getWindow(), R.color.status_bar_color);
        }
        else{
            recyclerView.setVisibility(View.VISIBLE);
            view.setVisibility(View.GONE);
            RelativeLayout toolBarLayout = (RelativeLayout)findViewById(R.id.include);
            statusBarBackground = (ImageView)toolBarLayout.findViewById(R.id.statusbar);
            addListenerForStatusBar();
            setUpStatusBar();
            loadData();
        }


    }

    private void setUpStatusBar() {
        int statusBarHeight = Utils.getStatusBarHeight();
        android.view.ViewGroup.LayoutParams layoutParams = statusBarBackground.getLayoutParams();
        Log.i(LOG_TAG, "Status bar height - Original - " + statusBarBackground.getHeight());
        layoutParams.height = statusBarHeight;
        Log.i(LOG_TAG, "Status bar height - After - " + statusBarBackground.getHeight());
        statusBarBackground.setLayoutParams(layoutParams);
        statusBarBackground.setBackgroundColor(getResources().getColor(R.color.toolbar_color));
    }

    /**
     *
     * Android L guidleines allows the option for the status bar to now be colored. For pre 5.0 devices,
     * A simulated status bar color is provided this way. Since in the onCreate a view is not technically drawn yet,
     * it is rather cumbersome to find a way to set the color in the method. A work around is given here, first
     * in the styles xml the status bar is set to translucent which also makes it overlay the window.
     * The status bar height for the device is calculated then the image view is then set to those dimensions
     * and colored thus giving the status bar color. All of this is done through this listener which listens for when
     * the view is drawn on the screen.
     *
     */

    private void addListenerForStatusBar() {
        Log.i(LOG_TAG, "Listener added.");
        final ViewTreeObserver observer= statusBarBackground.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                setUpStatusBar();
                statusBarBackground.getViewTreeObserver().removeOnGlobalLayoutListener(this);

            }
        });
    }

    /**
     * Sets up the toolbar.
     */
    private void setUpToolBar() {
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
    }


    /**
     * Handles the callbacks for the Android-L transitions. Since Android-L supports shared views and circular reveals,
     * this method makes sure the animations look smooth. At the end, it loads the data. This is so that the animations do
     * not lag or skip.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setupEnterAnimations() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Transition enterTransition = getWindow().getSharedElementEnterTransition();
            enterTransition.addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(Transition transition) {
                    //This is Stransition start
                }

                @Override
                public void onTransitionEnd(Transition transition) {
                    view.setVisibility(View.GONE);
                    toolbar.setVisibility(View.VISIBLE);
                    animateRevealShow(recyclerView);
                    loadData();
                }

                @Override
                public void onTransitionCancel(Transition transition) {
                }

                @Override
                public void onTransitionPause(Transition transition) {
                }

                @Override
                public void onTransitionResume(Transition transition) {
                }
            });
        }
    }

    /**
     * Retrieves the Response Listener for the String Request.
     * Also handles the response for when the server returns a response.
     *
     * @return - Volley Response Listener.
     */
    private  Response.Listener<String> getResponseListener(){
        return new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                ObjectMapper objectMapper = new ObjectMapper();
                //convert json string to object
                try {
                    List<Coffee> coffee = objectMapper.readValue(response, new TypeReference<List<Coffee>>(){}); //creates coffee objects
                    CoffeeRecyclerAdapter coffeeRecyclerAdapter = new CoffeeRecyclerAdapter(MainActivity.this, coffee);
                    recyclerView.setAdapter(coffeeRecyclerAdapter);
                    progressBar.setVisibility(View.GONE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }


    /**
     * Retrieves the Volley Error Listener.
     * Shows toast if there is an error.
     *
     * @return - the Error Listener to handle errors.
     */
    public Response.ErrorListener getErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(MainActivity.this, getString(R.string.check_your_connection), Toast.LENGTH_SHORT).show();
                Log.d(LOG_TAG, "error => " + error.toString());
            }
        };
    }

    public void setViewAttachment() {
        recyclerViewAttached = !recyclerViewAttached;
    }

    /**
     * Circular reveal on the passed view. For Android-L
     *
     * @param viewRoot - the view that will be revealed.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void animateRevealShow(View viewRoot) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            int cx = (viewRoot.getLeft() + viewRoot.getRight()) / 2; //Middle horizontal of the view.
            int cy = (viewRoot.getTop()); //Top of the view.
            int finalRadius = Math.max(viewRoot.getWidth(), viewRoot.getHeight());

            Animator anim = ViewAnimationUtils.createCircularReveal(viewRoot, cx, cy, 0, finalRadius);
            viewRoot.setVisibility(View.VISIBLE);
            anim.setDuration(ANIM_DURATION);
            anim.start();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if(recyclerView.getVisibility() == View.INVISIBLE && recyclerViewAttached){
            setUpLayout();
            setUpToolBar();
            animateRevealShow(recyclerView);
            if(recyclerView.getAdapter() == null){
                loadData();
            }
        }
    }


    /**
     * Overriden backpressed callback method to return user to their home screen rather than
     * the Splash screen.
     */
    @Override
    public void onBackPressed(){
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }
}

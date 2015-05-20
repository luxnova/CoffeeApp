package percolate.com.coffeeapp;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.WindowManager;


public class MainActivity extends AppCompatActivity {

    private static final String apiKey = "WuVbkuUsCXHPx3hsQzus4SE";
    private static final String url = "https://coffeeapi.percolate.com/api/coffee/";
    private static final long ANIM_DURATION = 500;
    private View view;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private boolean recyclerViewAttached = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpLayout();
        setupEnterAnimations();

        /*
        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        ObjectMapper objectMapper = new ObjectMapper();
                        //convert json string to object
                        try {
                            List<Coffee> coffee = objectMapper.readValue(response, new TypeReference<List<Coffee>>(){});
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("ERROR", "error => " + error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", apiKey);
                return params;
            }
        };

        queue.add(stringRequest);
        */

    }

    private void setUpLayout(){
        view = findViewById(R.id.bgView);
        setUpToolBar();

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getResources().getColor(R.color.status_bar_color));

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
    }

    private void setUpToolBar() {
        toolbar = (Toolbar)findViewById(R.id.realtoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setupEnterAnimations() {

        Transition enterTransition = getWindow().getSharedElementEnterTransition();
        enterTransition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                //This is Stransition start
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                view.setVisibility(View.INVISIBLE);
                toolbar.setVisibility(View.VISIBLE);
                animateRevealShow(recyclerView);
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


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void animateRevealShow(View viewRoot) {
        int cx = (viewRoot.getLeft() + viewRoot.getRight()) / 2;
        int cy = (viewRoot.getTop());
        int finalRadius = Math.max(viewRoot.getWidth(), viewRoot.getHeight());

        Animator anim = ViewAnimationUtils.createCircularReveal(viewRoot, cx, cy, 0, finalRadius);
        viewRoot.setVisibility(View.VISIBLE);
        anim.setDuration(300);
        anim.start();
    }


    @Override
    public void onResume(){
        if(recyclerView.getVisibility() == View.INVISIBLE && recyclerViewAttached){
            setUpToolBar();
            animateRevealShow(recyclerView);
        }
        super.onResume();
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

    public void setViewAttachment() {
        recyclerViewAttached = !recyclerViewAttached;
    }
}

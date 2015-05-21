package percolate.com.coffeeapp.activities;


import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import percolate.com.coffeeapp.models.Coffee;
import percolate.com.coffeeapp.R;
import percolate.com.coffeeapp.util.Utils;

/**
 * Created by JoshuaWilliams on 5/19/15.
 * @version 1.0
 *
 * Activity that shows the details of a coffee selection from the recycler view.
 */
public class CoffeeDetailsActivity extends AppCompatActivity {
    private static final String LOG_TAG = "CoffeeDetailsActivity";
    RelativeLayout parent;
    private TextView coffeeDescription, updatedTV, coffeeNameTV, shareButton;
    private Toolbar toolbar;
    private ImageView statusBarBackground;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coffee_details);

        Bundle data = getIntent().getExtras();
        Coffee coffee = data.getParcelable("Coffee");
        setUpLayout(coffee);
        setUpToolbar();
        loadData(coffee);
    }

    /**
     * Sets up the toolbar.
     */
    private void setUpToolbar() {
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        parent = (RelativeLayout)findViewById(R.id.parent);

        RelativeLayout toolBarLayout = (RelativeLayout)findViewById(R.id.include);
        shareButton = (TextView)toolBarLayout.findViewById(R.id.share);
        shareButton.setVisibility(View.VISIBLE);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    /**
     * Onclick listener for the share button to enable simple sharing of the coffee name and its description.
     *
     * @param v - view which the method will operate on click.
     */
    public void shareOnClick(View v){
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Come drink this with me! " + coffeeDescription.getText());
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, coffeeNameTV.getText());
        startActivity(Intent.createChooser(sharingIntent, "Share using"));
    }
    /**
     * Sets up the layout, dynamic to the coffee that is selected.
     *
     * @param coffee - the coffee that was selected.
     */
    public void setUpLayout(Coffee coffee) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Utils.setUpStatusBar(getWindow(), R.color.status_bar_color);
        }
        else{
            RelativeLayout toolBarLayout = (RelativeLayout)findViewById(R.id.include);
            statusBarBackground = (ImageView)toolBarLayout.findViewById(R.id.statusbar);
            addListenerForStatusBar();
            setUpStatusBar();
        }

        ImageView coffeePicIV = (ImageView)findViewById(R.id.coffe_pic);
        coffeePicIV.setImageBitmap(coffee.getImage());

        if(coffee.getImage() == null){
            coffeePicIV.setVisibility(View.GONE);
        }

        coffeeNameTV = (TextView)findViewById(R.id.coffee_name);
        coffeeNameTV.setText(coffee.getName());

        coffeeDescription = (TextView)findViewById(R.id.coffee_description);
        updatedTV = (TextView)findViewById(R.id.updated_tv);

    }

    /**
     * Loads the coffee data.
     * The JSON data returned from making this call includes more information than needed, or a lot of repeated already given information.
     * I understand this could be used to make this activity a standalone activity, however it is more efficient and user friendly to only
     * "cherry-pick" the needed data. The rest of the data is already stored from the first api call.
     *
     * @param coffee - the coffee for which data needs to be queried.
     */
    private void loadData(Coffee coffee){
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, getString(R.string.coffee_url) + coffee.getId(), getResponseListener(), getErrorListener()){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(getString(R.string.authorization), getString(R.string.api_key)); //authorization header.
                return params;
            }
        };
        queue.add(stringRequest);
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
     * Retrieves the Volley Response Listener.
     *
     * @return - the Response Listener that handles operations once the response is given from the server.
     */
    private  Response.Listener<String> getResponseListener(){
        return new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                Log.i(LOG_TAG, response);
                ObjectMapper mapper = new ObjectMapper();
                try {
                    Map<String, String> map = mapper.readValue(response, Map.class); //turns jason string into a map of key value pairs.
                    String lastUpdatedTime = map.get("last_updated_at");
                    String description = map.get("desc");
                    Utils.getTimeDifference(lastUpdatedTime);

                    coffeeDescription = (TextView)findViewById(R.id.coffee_description);
                    coffeeDescription.setText(description);

                    updatedTV = (TextView)findViewById(R.id.updated_tv);
                    updatedTV.setText(Utils.getTimeDifference(lastUpdatedTime));

                    Log.i(LOG_TAG, map.toString());
                } catch (IOException | ParseException e) {
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
                Toast.makeText(CoffeeDetailsActivity.this, getString(R.string.check_your_connection), Toast.LENGTH_SHORT).show();
                Log.e(LOG_TAG, "error => " + error.toString());
            }
        };
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_coffee_details, menu);
        return true;
    }


    /**
     * Since I only have one menu action item, onBackPressed is guaranteed to be called anytime an menu action button is pressed.
     * If I wanted to add more, I will just diffentiate by the ID and perform operations appropriately.
     *
     * @param item - The item being pressed.
     * @return - If the item press was registered.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        onBackPressed();
        return true;
    }


    @Override
    public void onResume()
    {
        super.onResume();
        coffeeNameTV = (TextView)findViewById(R.id.coffee_name);
        coffeeDescription = (TextView)findViewById(R.id.coffee_description);
        updatedTV = (TextView)findViewById(R.id.updated_tv);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        RelativeLayout toolBarLayout = (RelativeLayout)findViewById(R.id.include);
        shareButton = (TextView)toolBarLayout.findViewById(R.id.share);

        Log.i(LOG_TAG, "On Resume");
    }
}

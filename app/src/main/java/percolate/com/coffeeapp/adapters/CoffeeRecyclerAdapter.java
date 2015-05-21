package percolate.com.coffeeapp.adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import percolate.com.coffeeapp.models.Coffee;
import percolate.com.coffeeapp.activities.CoffeeDetailsActivity;
import percolate.com.coffeeapp.R;
import percolate.com.coffeeapp.util.Utils;

/**
 * Created by JoshuaWilliams on 5/20/15.
 *
 * The Recycler Adapter for the Recycler view in the MainActivity.
 */
public class CoffeeRecyclerAdapter extends RecyclerView.Adapter<CoffeeRecyclerAdapter.CoffeeCard> implements View.OnClickListener {
    private List<Coffee> coffeeList;
    private Activity context;

    private static final String LOG_TAG = "Coffee Recycler Adapter";


    public CoffeeRecyclerAdapter(Activity context, List<Coffee> coffeeData) {
        this.coffeeList = coffeeData;
        this.context = context;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public CoffeeCard onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.coffee_card, parent, false);

        return new CoffeeCard((LinearLayout)v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final CoffeeCard coffeeCard, final int position) {
        Log.i(LOG_TAG, "Position - " + position);

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Coffee coffee = getItem(position);


        coffeeCard.coffeeTitleTV.setText(coffee.getName());
        coffeeCard.coffeeDescriptionTV.setText(coffee.getDesc());

        //For the scale animation: If the cell has not been animated then it will animated the cell.
        if(!coffee.getAnimated() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Log.i(LOG_TAG, "New view");
            startCardAnimation(coffeeCard);
            coffee.setAnimated(true); //Makes sure the cell does not perform the animation again.
        }
        else{
            coffeeCard.subLayout.setVisibility(View.VISIBLE);
        }

        if(coffee.getImage_url().equals("")){
            coffeeCard.coffeePicIV.setVisibility(View.GONE);
        }
        else{
            //Asynchronous loading for versions lower than 5.0
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
                Utils.getImageLoader().displayImage(coffee.getImage_url(), coffeeCard.coffeePicIV, Utils.getDisplayImageOptions());
            }
            else{
                coffeeCard.coffeePicIV.setImageBitmap(coffee.getImage());
            }
            coffeeCard.coffeePicIV.setVisibility(View.VISIBLE);
        }

        //Removes the divider if the cell is the last cell in the data set.
        if(position == getItemCount()-1){
            coffeeCard.divider.setVisibility(View.INVISIBLE);
        }
        else{
            coffeeCard.divider.setVisibility(View.VISIBLE);
        }

        coffeeCard.parentLayout.setOnClickListener(this);
        coffeeCard.parentLayout.setTag(coffee);

    }



    @Override
    public int getItemCount() {
        return coffeeList.size();
    }

    public Coffee getItem(int position)
    {
        return coffeeList.get(position);
    }


    /**
     * Animation to start the scaling
     * @param coffeeCard - the row that contains the views for the data. This is the row the animation will be performed on.
     */
    private void startCardAnimation(final CoffeeCard coffeeCard) {
        Log.i(LOG_TAG, "Starting Anim");

        coffeeCard.subLayout.setVisibility(View.INVISIBLE);
        Animation scale = AnimationUtils.loadAnimation(context, R.anim.scale_anim);
        scale.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.i(LOG_TAG, "Ending Anim");
                fadeAnimation(coffeeCard.subLayout);
                if(coffeeCard.subLayout.getVisibility() != View.VISIBLE){
                    coffeeCard.subLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

       coffeeCard.parentLayout.startAnimation(scale);
    }

    /**
     * Animation to start the fading in of the data
     *
     * @param v - the view on which the fade animation will be performed.
     */

    private void fadeAnimation(final View v)
    {
        Log.i(LOG_TAG, "Starting Fade ANim");

        Animation anim = new AlphaAnimation(0, 1);
        anim.setInterpolator(new AccelerateInterpolator());
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.i(LOG_TAG, "Ending Fade Anim");
                v.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        anim.setFillAfter(true);
        anim.setDuration(400);
        v.startAnimation(anim);
    }

    @Override
    public void onClick(View view) {
        Coffee coffee = (Coffee) view.getTag(); //Retrieves the coffee that was selected.

        Intent i = new Intent(context, CoffeeDetailsActivity.class);
        i.putExtra("Coffee", coffee);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            ImageView sharedView = (ImageView) view.findViewById(R.id.coffee_image);
            if(sharedView.getVisibility() != View.GONE){
                ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(context, sharedView, sharedView.getTransitionName());
                context.startActivity(i, transitionActivityOptions.toBundle());
            }
            else{
                context.startActivity(i);
            }
        }
        else{
            context.startActivity(i);
            context.overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_shrink_fade_out_from_bottom);
        }

    }

    /**
     * This class is the viewholder for thie RecylcerView. It holds each view so that when scrolling, if already created,
     * the system will not create the view again from scratch.
     */
    public static class CoffeeCard extends RecyclerView.ViewHolder {
        RelativeLayout subLayout;
        LinearLayout parentLayout;
        TextView coffeeTitleTV,  coffeeDescriptionTV;
        ImageView coffeePicIV, divider;

        public CoffeeCard(LinearLayout v) {
            super(v);
            parentLayout = (LinearLayout)v.findViewById(R.id.parent_layout);
            subLayout = (RelativeLayout)v.findViewById(R.id.sublayout);
            coffeeTitleTV = (TextView) v.findViewById(R.id.coffe_name_text);
            coffeeDescriptionTV = (TextView) v.findViewById(R.id.coffee_description);
            coffeePicIV = (ImageView) v.findViewById(R.id.coffee_image);
            divider = (ImageView) v.findViewById(R.id.divider);
        }
    }
}
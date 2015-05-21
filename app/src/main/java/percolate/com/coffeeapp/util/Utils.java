package percolate.com.coffeeapp.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by JoshuaWilliams on 5/20/15.
 *
 * @version 1.0
 *
 * Utility class for common and shared methods.
 */
public class Utils {
    private static final String LOG_TAG = "Utils";
    private static ImageLoader imageLoader;
    private static Context context;
    private static DisplayImageOptions options;




    /**
     * Method that calculates the status bar height for the specific device.
     *
     * @return - the height of the status bar.
     */
    public static int getStatusBarHeight() {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        Log.i(LOG_TAG, "Status Bar Height - " + result);
        return result;
    }


    /**
     * This method initiates the display image options for the universal imageloader.
     *
     * @return DisplayImageOptions - the options that goes with the imageloader to help efficiently load images.
     */
    public static DisplayImageOptions getDisplayImageOptions() {
        if(options == null){
            options = new DisplayImageOptions.Builder()
                    .cacheOnDisk(true)
                    .cacheInMemory(true)
                    .resetViewBeforeLoading(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                    .build();
        }
        return options;
    }

    /**
     * This class initiates the one imageloader that we will use through out the whole application.
     *
     * Credits : Sergey Tarasevich
     *
     * Copyright 2011-2015 Sergey Tarasevich

     * Licensed under the Apache License, Version 2.0 (the "License");
     * you may not use this file except in compliance with the License.
     * You may obtain a copy of the License at
     * http://www.apache.org/licenses/LICENSE-2.0
     * Unless required by applicable law or agreed to in writing, software
     * distributed under the License is distributed on an "AS IS" BASIS,
     * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     * See the License for the specific language governing permissions and
     * limitations under the License.
     */
    public static ImageLoader getImageLoader() {
        if(imageLoader == null){
            imageLoader = ImageLoader.getInstance();
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                    .threadPriority(Thread.NORM_PRIORITY - 2)
                    .denyCacheImageMultipleSizesInMemory()
                    .memoryCache(new WeakMemoryCache())
                    .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                    .tasksProcessingOrder(QueueProcessingType.LIFO)
                    .build();
            imageLoader.init(config);
        }

        return imageLoader;
    }

    /**
     * For Android-L
     * Generic method to turn the status bar the color of the resousrce color passed.
     *
     * @param window - The window of the activity.
     * @param resourceColor - The color resource.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setUpStatusBar(Window window, int resourceColor) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(context.getResources().getColor(resourceColor));
    }

    public static void setContext(Context mContext){context = mContext;}


    /**
     * Gets the time difference as a string from the string date passed. The format accepted is
     * 'yyyy-mm-dd hh-mm-ss'. The time is compared to the current date.
     *
     * @param time - the time being compared for the difference with the current date.
     * @return - the string with the time difference in the form of "Updated [time difference] [time period] ago."
     * @throws ParseException - If the time is not in the format accepted, this Exception will be thrown.
     */
    public static String getTimeDifference(String time) throws ParseException {
        time = time.substring(0, time.indexOf("."));

        SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

        Date startDate = format.parse(time);
        Date todaysDate = new Date();
        String formatedDate = format.format(todaysDate);
        Date t = format.parse(formatedDate);

        //in milliseconds
        long diff = t.getTime() - startDate.getTime();

        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000);


        StringBuilder builder = new StringBuilder("Updated ");
        if(diffDays > 30){
            int monthCount = 0;
            while(diffDays >= 30){
                monthCount++;
                diffDays = diffDays - 30;
            }
            builder.append(monthCount);
            builder.append(" ");
            builder.append("month(s) ago.");
            return builder.toString();
        }
        else if(diffDays > 7){
            int weekCount = 0;
            while(diffDays > 7){
                weekCount++;
                diffDays = diffDays - 7;
            }
            builder.append(weekCount);
            builder.append(" ");
            builder.append("week(s) ago.");
            return builder.toString();
        }
        else if(diffDays >= 1){
            builder.append(diffDays);
            builder.append(" ");
            builder.append("day(s) ago.");
            return builder.toString();
        }
        else if(diffHours >= 1){
            builder.append(diffHours);
            builder.append(" ");
            builder.append("hour(s) ago.");
            return  builder.toString();
        }
        else if(diffMinutes > 0){
            builder.append(diffMinutes);
            builder.append(" ");
            builder.append("minute(s) ago.");
            return  builder.toString();
        }
        else{
            builder.append(diffSeconds);
            builder.append(" ");
            builder.append("second(s) ago.");
            return  builder.toString();
        }

    }
}

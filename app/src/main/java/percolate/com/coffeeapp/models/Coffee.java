package percolate.com.coffeeapp.models;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nostra13.universalimageloader.core.ImageLoader;

import percolate.com.coffeeapp.util.Utils;

/**
 * Created by JoshuaWilliams on 5/18/15.
 * @version 1.0
 *
 * Coffee object holding the data for a coffee node from the database.
 * Implements Jackson friendly annotations and Parcellable so that the object can be serialized and passed through
 * activities.
 */

//Ignores properties at which the ObjectMapper cannot find appropriate data for.
@JsonIgnoreProperties(ignoreUnknown = true)
public class Coffee implements Parcelable {
    private String name;
    private String id;
    private String desc;
    private String image_url;
    private Bitmap image = null;
    private boolean animated;

    /********************************** Constructors *******************************************/
    public Coffee(){}

    public Coffee(String name, String id, String desc, String image_url){
        setName(name);
        setId(id);
        setDesc(desc);
        setImage_url(image_url);
        setAnimated(false);
    }

    // Parcelling Constructor
    public Coffee(Parcel in){
        String[] data = new String[4];
        in.readStringArray(data);
        setId(data[0]);
        setName(data[1]);
        setDesc(data[2]);
        setImage_url(data[3]);
    }

    /********************************** Constructors *******************************************/



    /********************************** Getters and Setters *******************************************/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage_url() { return image_url;}

    public void setImage_url(String image_url) {
        ImageLoader imageLoader = Utils.getImageLoader();
        setImage(imageLoader.loadImageSync(image_url));
        this.image_url = image_url;
    }

    @Override
    public String toString() {
        //String builder for efficient appending.
        StringBuilder stringBuilder = new StringBuilder("Coffee: ");
        stringBuilder.append(getId() + " ");
        stringBuilder.append(getName() + " ");
        stringBuilder.append(getDesc() + " ");
        stringBuilder.append(getImage_url());
        return stringBuilder.toString();
    }

    public boolean getAnimated() {
        return animated;
    }

    public void setAnimated(boolean animated) {
        this.animated = animated;
    }

    @JsonIgnore
    private void setImage(Bitmap image) {
        this.image = image;
    }

    @JsonIgnore
    public Bitmap getImage() {
        return image;
    }

    /********************************** Getters and Setters *******************************************/


    @Override
    public int describeContents() {
        return 0;
    }



    /************************************* Parcelable Interface *********************************/
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[] {this.id,
                this.name,
                this.desc,
                this.image_url});
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Coffee createFromParcel(Parcel in) {
            return new Coffee(in);
        }

        public Coffee[] newArray(int size) {
            return new Coffee[size];
        }
    };

    /************************************* Parcelable Interface *********************************/

}

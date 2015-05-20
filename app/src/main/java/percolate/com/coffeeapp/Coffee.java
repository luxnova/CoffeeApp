package percolate.com.coffeeapp;

/**
 * Created by JoshuaWilliams on 5/18/15.
 */
public class Coffee {
    String name;
    String id;
    String desc;
    String image_url;

    public Coffee(){}

    public Coffee(String name, String id, String desc, String image_url){
        setName(name);
        setId(id);
        setDesc(desc);
        setImage_url(image_url);
    }

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

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("Coffee: ");
        stringBuilder.append(getId() + " ");
        stringBuilder.append(getName() + " ");
        stringBuilder.append(getDesc() + " ");
        stringBuilder.append(getImage_url());
        return stringBuilder.toString();
    }
}

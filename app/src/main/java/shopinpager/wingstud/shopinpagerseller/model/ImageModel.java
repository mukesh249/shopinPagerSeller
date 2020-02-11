package shopinpager.wingstud.shopinpagerseller.model;

import java.io.Serializable;

public class ImageModel implements Serializable {

    String image,id;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

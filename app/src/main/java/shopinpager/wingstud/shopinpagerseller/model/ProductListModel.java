package shopinpager.wingstud.shopinpagerseller.model;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class ProductListModel implements Serializable {

    String id,name,category_name,sub_category_name,created_at,status,product_image,stock_in_out,seller_id,product_id;
    ArrayList<String> imageArray= new ArrayList<>();
    ArrayList<String> colorAry= new ArrayList<>();
    ArrayList<Product_item_Model> productitem = new ArrayList<>();
    JSONObject itemobje = new JSONObject();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getSub_category_name() {
        return sub_category_name;
    }

    public void setSub_category_name(String sub_category_name) {
        this.sub_category_name = sub_category_name;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public ArrayList<String> getImageArray() {
        return imageArray;
    }

    public void setImageArray(ArrayList<String> imageArray) {
        this.imageArray = imageArray;
    }

    public ArrayList<String> getColorAry() {
        return colorAry;
    }

    public void setColorAry(ArrayList<String> colorAry) {
        this.colorAry = colorAry;
    }

    public ArrayList<Product_item_Model> getProductitem() {
        return productitem;
    }

    public void setProductitem(ArrayList<Product_item_Model> productitem) {
        this.productitem = productitem;
    }

    public String getStock_in_out() {
        return stock_in_out;
    }

    public void setStock_in_out(String stock_in_out) {
        this.stock_in_out = stock_in_out;
    }

    public JSONObject getItemobje() {
        return itemobje;
    }

    public void setItemobje(JSONObject itemobje) {
        this.itemobje = itemobje;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }
}

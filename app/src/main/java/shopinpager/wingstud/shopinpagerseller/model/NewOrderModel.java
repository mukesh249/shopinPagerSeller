package shopinpager.wingstud.shopinpagerseller.model;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NewOrderModel implements Serializable {

    String id,order_id,order_date,num_of_pro,total_amount,product_image,assign,cancel,status,qty,shipping_charge,amount;;

    List<String> imageArray= new ArrayList<>();
    JSONObject address = new JSONObject();


    public String getShipping_charge() {
        return shipping_charge;
    }

    public void setShipping_charge(String shipping_charge) {
        this.shipping_charge = shipping_charge;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public JSONObject getAddress() {
        return address;
    }

    public void setAddress(JSONObject address) {
        this.address = address;
    }

    public List<String> getImageArray() {
        return imageArray;
    }

    public void setImageArray(List<String> imageArray) {
        this.imageArray = imageArray;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getNum_of_pro() {
        return num_of_pro;
    }

    public void setNum_of_pro(String num_of_pro) {
        this.num_of_pro = num_of_pro;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getAssign() {
        return assign;
    }

    public void setAssign(String assign) {
        this.assign = assign;
    }

    public String getCancel() {
        return cancel;
    }

    public void setCancel(String cancel) {
        this.cancel = cancel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

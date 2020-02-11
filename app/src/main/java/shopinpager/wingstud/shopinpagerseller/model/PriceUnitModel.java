package shopinpager.wingstud.shopinpagerseller.model;

import java.io.Serializable;

public class PriceUnitModel implements Serializable {

    String weight="",price="",offer="",qty="",id="";
    boolean plus,minus;

    public boolean isPlus() {
        return plus;
    }

    public void setPlus(boolean plus) {
        this.plus = plus;
    }

    public boolean isMinus() {
        return minus;
    }

    public void setMinus(boolean minus) {
        this.minus = minus;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }

    public String getQty() {
        return qty;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }
}



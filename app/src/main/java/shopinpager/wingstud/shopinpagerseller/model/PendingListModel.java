package shopinpager.wingstud.shopinpagerseller.model;

import java.io.Serializable;

public class PendingListModel implements Serializable {

    String order_date,total_commission,total_pending_amount,total_payable_amount,tcs;

    public String getTcs() {
        return tcs;
    }

    public void setTcs(String tcs) {
        this.tcs = tcs;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getTotal_commission() {
        return total_commission;
    }

    public void setTotal_commission(String total_commission) {
        this.total_commission = total_commission;
    }

    public String getTotal_pending_amount() {
        return total_pending_amount;
    }

    public void setTotal_pending_amount(String total_pending_amount) {
        this.total_pending_amount = total_pending_amount;
    }

    public String getTotal_payable_amount() {
        return total_payable_amount;
    }

    public void setTotal_payable_amount(String total_payable_amount) {
        this.total_payable_amount = total_payable_amount;
    }
}

package shopinpager.wingstud.shopinpagerseller.model;

import java.io.Serializable;

/**
 * Created by hemant on 25-08-2019.
 */

public class PaymentPerModel implements Serializable {
    String order_date,total_commission,amount,transaction_id,created_at;

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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}

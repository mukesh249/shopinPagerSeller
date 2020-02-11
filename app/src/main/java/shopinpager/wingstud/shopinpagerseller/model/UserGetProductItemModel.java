package shopinpager.wingstud.shopinpagerseller.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserGetProductItemModel {

    public class Datum {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("product_id")
        @Expose
        public Integer productId;
        @SerializedName("seller_id")
        @Expose
        public Integer sellerId;
        @SerializedName("weight")
        @Expose
        public String weight;
        @SerializedName("price")
        @Expose
        public Integer price;
        @SerializedName("offer")
        @Expose
        public Integer offer;
        @SerializedName("qty")
        @Expose
        public String qty;
        @SerializedName("sprice")
        @Expose
        public Integer sprice;
        @SerializedName("created_at")
        @Expose
        public String createdAt;
        @SerializedName("updated_at")
        @Expose
        public String updatedAt;

    }


        @SerializedName("status_code")
        @Expose
        public Integer statusCode;
        @SerializedName("message")
        @Expose
        public String message;
        @SerializedName("error_message")
        @Expose
        public String errorMessage;
        @SerializedName("data")
        @Expose
        public List<Datum> data = null;

}

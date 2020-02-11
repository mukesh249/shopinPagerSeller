package shopinpager.wingstud.shopinpagerseller.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductNameModel {

    public class Datum {

        @SerializedName("id")
        @Expose
        public Integer id;
        @SerializedName("name")
        @Expose
        public String name;

    }
        @SerializedName("status_code")
        @Expose
        public Integer statusCode;
        @SerializedName("message")
        @Expose
        public String message;
        @SerializedName("data")
        @Expose
        public List<Datum> data = null;

}

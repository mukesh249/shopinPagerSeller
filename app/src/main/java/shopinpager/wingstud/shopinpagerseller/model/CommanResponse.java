package shopinpager.wingstud.shopinpagerseller.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommanResponse {

    @SerializedName("status_code")
    @Expose
    public Integer statusCode;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("otp")
    @Expose
    public String otp;
}

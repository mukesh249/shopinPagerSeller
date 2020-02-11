package shopinpager.wingstud.shopinpagerseller.Api;


import com.google.gson.JsonObject;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiConfig {

    @Multipart
    @POST("api")
    Call<JsonObject> PostContract(
//                                  @Part List<MultipartBody.Part> photos,
            @Part MultipartBody.Part image,
            @Part("request") RequestBody name
    );

//    @Multipart
//    @POST("api/Requestjobs/createJob")
//    Call<JsonObject> postFixProblem(@Header("ln") String authorization,
//                                    @Part List<MultipartBody.Part> photos,
//                                    @Part MultipartBody.Part image,
//                                    @Part("data") Str name);
//

    @Multipart
    @POST("add-product")
    Call<JsonObject> productUploadMethod(
            @Part("user_id") RequestBody user_id,
            @Part("description") RequestBody description,
            @Part("p_gst") RequestBody p_gst,
            @Part("brand_id") RequestBody brand_id,
            @Part("category_id") RequestBody category_id,
            @Part("sub_category_id") RequestBody sub_category_id,
            @Part("name") RequestBody name,
            @Part("color") RequestBody color,
            @Part("price_unit") RequestBody price_unit,
            @Part ArrayList<MultipartBody.Part> photos

    );
    @Multipart
    @POST("upload-scheme-product")
    Call<JsonObject> uploadSchemeProduct(
            @Part("user_id") RequestBody user_id,
            @Part("cat_id") RequestBody cat_id,
            @Part("sub_cat_id") RequestBody sub_cat_id,
            @Part("product_id") RequestBody product_id,
            @Part("product_item_id") RequestBody product_item_id,
            @Part("scheme_name") RequestBody scheme_name,
            @Part MultipartBody.Part photos
    );
    @Multipart
    @POST("update-product")
    Call<JsonObject> productUpdateMethod(
            @Part("product_id") RequestBody product_id,
            @Part("user_id") RequestBody user_id,
            @Part("description") RequestBody description,
            @Part("p_gst") RequestBody p_gst,
            @Part("brand_id") RequestBody brand_id,
            @Part("category_id") RequestBody category_id,
            @Part("sub_category_id") RequestBody sub_category_id,
            @Part("name") RequestBody name,
            @Part("color") RequestBody color,
            @Part("price_unit") RequestBody price_unit,
            @Part ArrayList<MultipartBody.Part> photos

    );
    @Multipart
    @POST("update-seller-step-1")
    Call<JsonObject> ProfileComplete(
            @Part("username") RequestBody username,
            @Part("mobile") RequestBody mobile,
            @Part("email") RequestBody email,
            @Part("country_id") RequestBody country_id,
            @Part("state_id") RequestBody state_id,
            @Part("city_id") RequestBody city_id,
            @Part("pincode") RequestBody pincode,
            @Part("delivery_pincode") RequestBody delivery_pincode,
            @Part("address_1") RequestBody address_1,
            @Part("food_license_no") RequestBody food_license_no,
            @Part("business_reg_no") RequestBody business_reg_no,
            @Part("business_name") RequestBody business_name,
            @Part("address_2") RequestBody address_2,
            @Part("user_id") RequestBody user_id,
            @Part("latitude") RequestBody latitude,
            @Part("longitude") RequestBody longitude,
            @Part MultipartBody.Part profile_image,
            @Part MultipartBody.Part seller_image

    );

    @Multipart
    @POST("update-seller-step-2")
    Call<JsonObject> ProfileComplete2(
            @Part("account_number") RequestBody account_number,
            @Part("bank_name") RequestBody bank_name,
            @Part("ifsc_code") RequestBody ifsc_code,
            @Part("account_holder_name") RequestBody account_holder_name,
            @Part("pan_number") RequestBody pan_number,
            @Part("gst_number") RequestBody gst_number,
            @Part("user_id") RequestBody user_id,
            @Part MultipartBody.Part cancel_cheque,
            @Part MultipartBody.Part pan_image

    );
    @FormUrlEncoded
    @POST("get-duplicate-product")
    Call<JsonObject> searchProduct(
            @Field("user_id") String pincode,
            @Field("search_key") String key_words
    );

}

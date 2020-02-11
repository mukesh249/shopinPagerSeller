package shopinpager.wingstud.shopinpagerseller.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SchemeListModel {

    public class Datum {

        @SerializedName("id")
        @Expose
        public Integer id;
        @SerializedName("user_id")
        @Expose
        public Integer userId;
        @SerializedName("cat_id")
        @Expose
        public Integer catId;
        @SerializedName("sub_cat_id")
        @Expose
        public Integer subCatId;
        @SerializedName("product_id")
        @Expose
        public Integer productId;
        @SerializedName("product_item_id")
        @Expose
        public Integer productItemId;
        @SerializedName("offer_name")
        @Expose
        public String offerName;
        @SerializedName("image")
        @Expose
        public String image;
        @SerializedName("status")
        @Expose
        public Integer status;
        @SerializedName("created_at")
        @Expose
        public String createdAt;
        @SerializedName("updated_at")
        @Expose
        public String updatedAt;
        @SerializedName("get_product")
        @Expose
        public GetProduct getProduct;
        @SerializedName("get_product_item")
        @Expose
        public GetProductItem getProductItem;

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

    public class GetProduct {

        @SerializedName("id")
        @Expose
        public Integer id;
        @SerializedName("user_id")
        @Expose
        public Integer userId;
        @SerializedName("brand_id")
        @Expose
        public Integer brandId;
        @SerializedName("slug")
        @Expose
        public String slug;
        @SerializedName("sku")
        @Expose
        public String sku;
        @SerializedName("category_id")
        @Expose
        public Integer categoryId;
        @SerializedName("category_slug")
        @Expose
        public String categorySlug;
        @SerializedName("sub_category_id")
        @Expose
        public Integer subCategoryId;
        @SerializedName("sub_category_slug")
        @Expose
        public String subCategorySlug;
        @SerializedName("super_sub_category_id")
        @Expose
        public Integer superSubCategoryId;
        @SerializedName("super_sub_category_slug")
        @Expose
        public String superSubCategorySlug;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("commission")
        @Expose
        public String commission;
        @SerializedName("p_gst")
        @Expose
        public Integer pGst;
        @SerializedName("color")
        @Expose
        public String color;
        @SerializedName("city_id")
        @Expose
        public Integer cityId;
        @SerializedName("is_recommended")
        @Expose
        public Integer isRecommended;
        @SerializedName("is_today_offer")
        @Expose
        public Integer isTodayOffer;
        @SerializedName("is_best_selling")
        @Expose
        public Integer isBestSelling;
        @SerializedName("monthly_essentials")
        @Expose
        public Integer monthlyEssentials;
        @SerializedName("weather_special")
        @Expose
        public Integer weatherSpecial;
        @SerializedName("saving_pack")
        @Expose
        public Integer savingPack;
        @SerializedName("description")
        @Expose
        public String description;
        @SerializedName("type")
        @Expose
        public String type;
        @SerializedName("created_at")
        @Expose
        public String createdAt;
        @SerializedName("updated_at")
        @Expose
        public String updatedAt;
        @SerializedName("status")
        @Expose
        public Integer status;
        @SerializedName("is_admin_approved")
        @Expose
        public Integer isAdminApproved;
        @SerializedName("stock_status")
        @Expose
        public Integer stockStatus;
        @SerializedName("share_count")
        @Expose
        public Integer shareCount;
        @SerializedName("is_cod")
        @Expose
        public Integer isCod;
        @SerializedName("is_return")
        @Expose
        public Integer isReturn;
        @SerializedName("is_exchange")
        @Expose
        public Integer isExchange;
        @SerializedName("related_product")
        @Expose
        public String relatedProduct;
        @SerializedName("is_featured")
        @Expose
        public Integer isFeatured;

    }

    public class GetProductItem {

        @SerializedName("id")
        @Expose
        public Integer id;
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
        public Integer qty;
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
}

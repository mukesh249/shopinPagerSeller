package shopinpager.wingstud.shopinpagerseller.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

public class SearchModel implements Serializable {

//    String id,name;
//
//    JSONObject jsonObject = new JSONObject();
//
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public JSONObject getJsonObject() {
//        return jsonObject;
//    }
//
//    public void setJsonObject(JSONObject jsonObject) {
//        this.jsonObject = jsonObject;
//    }

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

    public class Datum {

        @SerializedName("id")
        @Expose
        public Integer id;
        @SerializedName("user_id")
        @Expose
        public Integer userId;
        @SerializedName("brand_id")
        @Expose
        public String brandId;
        @SerializedName("slug")
        @Expose
        public String slug;
        @SerializedName("sku")
        @Expose
        public String sku;
        @SerializedName("category_id")
        @Expose
        public String categoryId;
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
        public String pGst;
        @SerializedName("color")
        @Expose
        public String color;
        @SerializedName("city_id")
        @Expose
        public Integer cityId;
        @SerializedName("description")
        @Expose
        public String description;
        @SerializedName("type")
        @Expose
        public String type;
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
        @SerializedName("is_sunday_bazar")
        @Expose
        public Integer isSundayBazar;
        @SerializedName("is_grocito_exclusive")
        @Expose
        public Integer isGrocitoExclusive;
        @SerializedName("related_product")
        @Expose
        public String relatedProduct;
        @SerializedName("is_featured")
        @Expose
        public Integer isFeatured;
        @SerializedName("created_at")
        @Expose
        public String createdAt;
        @SerializedName("updated_at")
        @Expose
        public String updatedAt;
        @SerializedName("product_image")
        @Expose
        public List<ProductImage> productImage = null;
        @SerializedName("main_category")
        @Expose
        public MainCategory mainCategory;
        @SerializedName("sub_category")
        @Expose
        public SubCategory subCategory;
        @SerializedName("product_note")
        @Expose
        public List<Object> productNote = null;
        @SerializedName("product_item")
        @Expose
        public List<ProductItem> productItem = null;

    }



    public class MainCategory {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("slug")
        @Expose
        public String slug;
        @SerializedName("type")
        @Expose
        public String type;
        @SerializedName("description")
        @Expose
        public String description;
        @SerializedName("position")
        @Expose
        public Integer position;
        @SerializedName("is_special")
        @Expose
        public Integer isSpecial;
        @SerializedName("image")
        @Expose
        public String image;
        @SerializedName("app_icon")
        @Expose
        public String appIcon;
        @SerializedName("created_at")
        @Expose
        public String createdAt;
        @SerializedName("updated_at")
        @Expose
        public String updatedAt;
        @SerializedName("status")
        @Expose
        public Integer status;

    }

    public class ProductImage {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("product_id")
        @Expose
        public Integer productId;
        @SerializedName("image")
        @Expose
        public String image;
        @SerializedName("in_stock")
        @Expose
        public Integer inStock;
        @SerializedName("created_at")
        @Expose
        public String createdAt;
        @SerializedName("updated_at")
        @Expose
        public String updatedAt;

    }

    public class ProductItem {

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
        public Double price;
        @SerializedName("offer")
        @Expose
        public String offer;
        @SerializedName("qty")
        @Expose
        public Double qty;
        @SerializedName("sprice")
        @Expose
        public Double sprice;
        @SerializedName("created_at")
        @Expose
        public String createdAt;
        @SerializedName("updated_at")
        @Expose
        public String updatedAt;

    }
    public class SubCategory {

        @SerializedName("id")
        @Expose
        public Integer id;
        @SerializedName("category_id")
        @Expose
        public Integer categoryId;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("category_slug")
        @Expose
        public String categorySlug;
        @SerializedName("slug")
        @Expose
        public String slug;
        @SerializedName("description")
        @Expose
        public String description;
        @SerializedName("image")
        @Expose
        public String image;
        @SerializedName("created_at")
        @Expose
        public String createdAt;
        @SerializedName("updated_at")
        @Expose
        public String updatedAt;
        @SerializedName("status")
        @Expose
        public Integer status;

    }
}

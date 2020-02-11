package shopinpager.wingstud.shopinpagerseller.Api;


/**
 * Created by Advosoft2 mukesh on 6/28/2017.
 */

public class WebUrls {
    public static final String BASE_URL = "https://www.shopinpager.com/";
    public static final String BASE_URL2 = "https://www.shopinpager.com";
    public static final String ImageUrl = "https://www.shopinpager.com/public/admin/uploads/seller";
    public static final String SchemeImage = "public/admin/uploads/scheme_product/";
    public final static String VerifyForgotPasswordOtp= "verify-forgot-password-otp";
    public final static String Login = "seller-login";
    public final static String Registration = "add-seller";
    public final static String OtpApi= "r-check-otp";
    public final static String ResendOtpApi = "r-resend-otp";
    public final static String ProfileApi = "get-user";
    public final static String GetOrderList = "get-order-list";
    public final static String NotificationListApi = "notification-list";
    public final static String NotificationCountUpdateApi = "update-notification-status";
    public final static String OrderDetailList = "order-details";
    public final static String StateApi = "state";
    public final static String CityApi = "city";
    public final static String Delovery_Pincode = "delovery-pincode";
    public final static String PaymentOverView = "seller-payment";
    public final static String PaidPayment = "paid-payment-list";
    public final static String PaidPaymentDetails = "paid-payment-details";
    public final static String PendPayment = "pending-payment-list";
    public final static String PendPaymentDetails = "pending-payment-details";
    public final static String TodayPayment = "today-payment-list";
    public final static String TodayPaymentDetails = "today-payment-details";
    public final static String PaymentOrderDetails = "payment-order-details";
    public final static String Notice = "notice-list";
    public final static String Product_list = "product-list";
    public final static String GetSchemeProductList = "get-scheme-product-list";
    public final static String Brand_list = "brand-list";
    public final static String Cat_list = "cat-list";
    public final static String SubCat_list = "sub-cat-list";
    public final static String Color_list = "color-list";
    public final static String Inventory_in_stk_list = "inventory-in-stock-list";
    public final static String Inventory_in_stk_prod_list = "inventory-in-stock-product-list";
    public final static String Inventory_ot_stk_list = "inventory-out-of-stock-list";
    public final static String Inventory_ot_stk_prod_list = "inventory-out-of-stock-product-list";
    public final static String Dashboard_data = "dashboard-data";
    public final static String Delete_Product = "delete-product";
    public final static String Update_stock_status = "update-stock-status";
    public final static String ProfileUpdateStep3 = "update-seller-step-3";
    public final static String GetSearchItem = "get-duplicate-product";
    public final static String DuplicateProductList = "duplicate-product-list";
    public final static String DeleteDupItem = "delete-duplicate-product";
    public final static String OrderAssign = "order-assign-to-rider";
    public final static String OrderCancel = "order-cancel";
    public final static String ReturnOrderList = "return-order-list";
    public final static String ExchangeOrderList = "exchange-order-list";
    public final static String AddDuplicateProduct = "add-duplicate-product";
    public final static String GetProductNameForScheme = "get-product-name-for-scheme";
    public final static String GetProductItemForScheme = "get-product-item-for-scheme";
    public final static String DeleteSchemeProduct = "delete-scheme-product";
    public final static String SellerAddBrand = "seller-add-brand";
    public final static String UserGetProductItem = "user-get-product-item";
    public final static String SellerUpdateItemQty = "seller-update-item-qty";
    public final static String SellerForgotPassword = "seller-forgot-password";
    public final static String SellerChangePassword = "seller-change-password";
    //Google api for Trackinghttps:
    public static final String baseURL = "https://maps.googleapis.com";

    public static ApiConfig getGoogleAPI(){
        return RetrofitClient.getRetrofitClient(baseURL).create(ApiConfig.class);
    }

}

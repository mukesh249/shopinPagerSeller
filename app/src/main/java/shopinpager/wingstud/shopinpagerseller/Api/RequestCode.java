package shopinpager.wingstud.shopinpagerseller.Api;

/**
 * Created by suarebits on 3/12/15.
 */
public class RequestCode {
    public static final int CODE_Register = 1;
    public static final int CODE_Login = 2;
    public static final int CODE_OtpCheck = 3;
    public static final int CODE_ResendOtp = 4;
    public static final int CODE_ProfileById=5;
    public static final int CODE_Password_Reset = 6;
    public static final int CODE_VerifyForgotPasswordOtp = 7;
    public static final int CODE_GetOrderList = 8;
    public static final int CODE_NotificationList = 9;
    public static final int CODE_OrderDetailList = 10;

    public static final int CODE_State = 11;
    public static final int CODE_City=12;
    public static final int CODE_PaymentOverView=13;
    public static final int CODE_PaidPayment=14;
    public static final int CODE_PaidPaymentDetails = 15;
    public static final int CODE_PendPayment = 16;
    public static final int CODE_Notice = 17;
    public static final int CODE_Dashboard_data=18;
    public static final int CODE_Brand_list=19;
    public static final int CODE_Cat_list=20;
    public static final int CODE_SubCat_list=21;
    public static final int CODE_Color_list=22;
    public static final int CODE_In_Stock=23;
    public static final int CODE_Out_Stock=24;
    public static final int CODE_Delete_Product=25;
    public static final int CODE_Update_stock_status=26;
    public static final int CODE_Delovery_Pincode=27;
    public static final int CODE_ProfileUpdateStep3=28;
    public static final int CODE_NotifiCountUpdate= 29;
    public static final int CODE_GetSearchItem=30;
    public static final int CODE_DuplicateProductList =31;
    public static final int CODE_DeleteDupItem =32;
    public static final int CODE_OrderAssign =33;
    public static final int CODE_AddDuplicateProduct =34;
    public static final int CODE_GetSchemeProductList=35;
    public static final int CODE_GetProductNameForScheme=36;
    public static final int CODE_GetProductItemForScheme=37;
    public static final int CODE_DeleteSchemeProduct=38;
    public static final int CODE_SellerChangePassword=39;
    public static final int CODE_SellerUpdateItemQty=40;
    public static final int CODE_UserGetProductItem=41;
    public static final int CODE_SellerAddBrand=42;
    public static final int CODE_ForgetPassword=43;
    public static final int CODE_UpdateLabourProfile=42;
    public static final int CODE_getProfileInfo=43;
    public static final int CODE_createJob=44;
    public static final int CODE_GetCustomerAllJobs=45;
    public static final int CODE_GetSingleJob=46;
    public static final int CODE_GetLabourAllJobs=47;
    public static final int CODE_CreateBid=48;
    public static final int CODE_EditBid=49;
    public static final int CODE_getAllBid=50;
    public static final int CODE_getSingleBid=51;
    public static final int CODE_acceptBid=52;
    public static final int CODE_rejectBid=53;



    public static final int CODE_GetAllGroups=100;
    public static final int CODE_GetAllrequest=101;
    public static final int CODE_MakeRequest = 102;
    public static final int CODE_GetAllSentrequest=103;
    public static final int CODE_AcceptRequest=104;
    public static final int CODE_RejectRequest=105;
    public static final int CODE_GetConfirmedRequests=106;
    public static final int CODE_RemoveFromGroupList = 107;
    public static final int CODE_AddMember = 108;
    public static final int CODE_GetGroupData=109;
    public static final int CODE_RemoveMember = 110;
    public static final int CODE_CancelRequest = 111;
    public static final int CODE_GetGroupList = 112;
    public static final int CODE_LeftGroupList = 113;
    public static final int CODE_GetMyLabourList = 114;
    public static final int CODE_addOwnMember = 115;
    public static final int CODE_removeOwnMember = 116;
    public static final int Code_archiveAcceptBids = 117;
    public static final int Code_archiveRejectBids = 118;




    //Constants
    public static String SP_CURRENT_LAT = "lat";
    public static String SP_CURRENT_LONG = "lng";
    public static String SP_NEW_LAT = "newLat";
    public static String SP_NEW_LONG = "newLng";
    public static String SP_DriverStatus = "driverStatus";
    public static final String UserID = "userID";
    public static final String userType = "user_type";
    public static final String KEY_ANIM_TYPE="anim_type";
    public static final String KEY_TITLE="anim_title";
    public static final String LangId = "langid";

    public static int AUTOPLACE_CODE=80;


    public enum TransitionType{
        ExplodeJava,ExplodeXML,SlideJava,SlideXML,FadeJava,FadeXML;
    }

}

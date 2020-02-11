package shopinpager.wingstud.shopinpagerseller.util;

import android.content.Context;
import android.content.SharedPreferences;

import shopinpager.wingstud.shopinpagerseller.R;

/**
 * Created by Vishal on 28/07/2016.
 */

public class SharedPref {

    private static SharedPreferences mPref;
    private static Context mContext;

    public static void init(Context context) {
        mContext = context;
    }

    public static void setLogin(Context context, boolean login) {
        mPref = (SharedPreferences) context.getSharedPreferences(context.getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
        mPref.edit().putBoolean(Constants.FLD_IS_LOGIN, login).commit();
    }

    public static void setUserId(Context context, String userId) {
        mPref = (SharedPreferences) context.getSharedPreferences(context.getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
        mPref.edit().putString(Constants.FLD_USER_ID, userId).commit();
    }

//    public static void setDeviceToken(Context context,String key, String token) {
//        mPref = (SharedPreferences) context.getSharedPreferences(context.getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
//        mPref.edit().putString(key, token).commit();
//    }
//
//    public static void getDeviceToken(Context context,String key, String token) {
//        mPref = (SharedPreferences) context.getSharedPreferences(context.getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
//        mPref.edit().get(key, token).commit();
//    }

    public static boolean isLogin(Context context) {
        mPref = (SharedPreferences) context.getSharedPreferences(context.getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
        boolean isLogin = mPref.getBoolean(Constants.FLD_IS_LOGIN, false);
        return isLogin;
    }

    public static String getUserModelJSON(Context context) {
        mPref = (SharedPreferences) context.getSharedPreferences(context.getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
        String userModelJson = mPref.getString(Constants.FLD_USER_MODEL_JSON, "");
        return userModelJson;
    }

    public static void setUserModelJSON(Context context, String userModelJson) {
        mPref = (SharedPreferences) context.getSharedPreferences(context.getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
        mPref.edit().putString(Constants.FLD_USER_MODEL_JSON, userModelJson).commit();
    }


    public static void setScreenW(Context context, int screenW) {
        mPref = (SharedPreferences) context.getSharedPreferences(context.getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
        mPref.edit().putInt(Constants.FLD_SCREEN_WIDTH, screenW).commit();
    }


    public static void setBuildVersion(Context context, String deviceToken) {
        mPref = (SharedPreferences) context.getSharedPreferences(context.getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
        mPref.edit().putString(Constants.FLD_BUILD_VERSION, deviceToken).commit();
    }

}

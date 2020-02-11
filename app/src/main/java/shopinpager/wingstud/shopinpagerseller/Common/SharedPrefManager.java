package shopinpager.wingstud.shopinpagerseller.Common;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import shopinpager.wingstud.shopinpagerseller.R;
import shopinpager.wingstud.shopinpagerseller.util.Constants;

public class SharedPrefManager {

    public static SharedPreferences sp;
//    private static String islagChange;
    private static Context mCtx;
    private static SharedPrefManager mInstance;

    private SharedPrefManager(Context context) {
        mCtx = context;
        sp = mCtx.getSharedPreferences(context.getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null)
            mInstance = new SharedPrefManager(context);
        return mInstance;
    }

    public static void setLogin(String key, boolean login) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, login).apply();
    }
//    public static boolean isLogin(String key) {
//        return sp.getBoolean(Constrants.IsLogin, false);
//    }

    public static boolean isLogin(Context context) {
        sp = (SharedPreferences) context.getSharedPreferences(context.getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
        boolean isLogin = sp.getBoolean(Constrants.IsLogin, false);
        return isLogin;
    }

    public static void setBuildVersion(Context context, String deviceToken) {
        sp = (SharedPreferences) context.getSharedPreferences(context.getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
        sp.edit().putString(Constants.FLD_BUILD_VERSION, deviceToken).commit();
    }



    public static void setUserID(String type, String userId) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(type, userId);
        editor.commit();
    }

    public static String getUserID(String type) {
        String e = sp.getString(type, "");
        return e;
    }

    public static void setDeviceToken(String type, String userId) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(type, userId);
        editor.commit();
    }

    public static String getDeviceToken(String type) {
        String e = sp.getString(type, "");
        return e;
    }

    public static void setUserEmail(String type, String userId) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(type, userId);
        editor.commit();
    }

    public static String getUserEmail(String type) {
        String e = sp.getString(type, "");
        return e;
    }

    public static void setMobile(String key,String mobile){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key,mobile);
        editor.commit();
    }

    public static String getMobile(String key){
        return sp.getString(key,"");
    }

    public static void setProfilePic(String key ,String pic){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key,pic);
        editor.commit();
    }

    public static String getProfilePic(String key){
        return sp.getString(key,"");
    }

    public static void setUserName(String type, String userId) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(type, userId);
        editor.commit();
    }

    public static String getUserName(String type) {
        String e = sp.getString(type, "");
        return e;
    }
    public static void setGender(String type, String gender) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(type, gender);
        editor.commit();
    }

    public static String getGender(String type) {
        String e = sp.getString(type, "");
        return e;
    }

    public static void setAddress(String type, String value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(type, value);
        editor.apply();
    }

    public static String getAddress(String type) {
        String e = sp.getString(type, "");
        return e;
    }
    public static void setBusinessName(String type, String value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(type, value);
        editor.apply();
    }

    public static String getBusinessName(String type) {
        return sp.getString(type, "");
    }




    public static void setImagePath(String type, String value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(type, value);
        editor.apply();
    }

    public static String getImagePath(String type) {
        String e = sp.getString(type, "");
        return e;
    }
    public static void showMessage(Context ctx, String msg) {
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
    }

    public static void setCartItemCount(String key,int value){

        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key,value);
        editor.apply();

    }

    public static int getCartItemCount(String key){
        return sp.getInt(key,0);
    }

    public static boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public void storeIsLoggedIn(Boolean isLoggedIn) {
        SharedPreferences sharedPreferencesReg = mCtx.getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferencesReg.edit();
        editor.putBoolean("key", isLoggedIn);
        editor.commit();
    }

    public boolean getIsLoggedIn(boolean default_value) {
        SharedPreferences sharedPreferencesReg = mCtx.getSharedPreferences("login", Context.MODE_PRIVATE);
        return sharedPreferencesReg.getBoolean("key", default_value);
    }

    public void hideSoftKeyBoard(Activity activity) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

            //check if no view has focus.
            View v = activity.getCurrentFocus();
            if (v == null)
                return;
            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Boolean CheckPassword(String password) {
        String[] re = {"[a-z]", "[?=.*[@#$%!\\-_?&])(?=\\\\S+$]"};
        for (String r : re) {
            if (!Pattern.compile(r).matcher(password).find()) return false;
        }
        return true;
    }


}

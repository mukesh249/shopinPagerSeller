package shopinpager.wingstud.shopinpagerseller.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Environment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import shopinpager.wingstud.shopinpagerseller.R;
import shopinpager.wingstud.shopinpagerseller.activity.SplashActi;


/**
 * Created by wingstud on 09-04-2018.
 */

public class Utils {
    private static AlertDialog retryCustomAlert;
    private static Dialog apiCallingProgressDialog;
    private static JSONObject jsonObject1;
    private static String message;
    public static boolean isShown = false;

    public static String getRootDirPath(Context context) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File file = ContextCompat.getExternalFilesDirs(context.getApplicationContext(),
                    null)[0];
            return file.getAbsolutePath();
        } else {
            return context.getApplicationContext().getFilesDir().getAbsolutePath();
        }
    }


    public static void setDeviceSreenH(Activity act) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        act.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        SharedPref.setScreenW(act, width);
    }

    public static Drawable setTintColorIcon(Context context, int drawableIcon) {
        final Drawable icons = context.getResources().getDrawable(drawableIcon);
        icons.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
        return icons;
    }


    //to start any activity.
    public static void startActivityWithFinish(Context context, Class<?> class1) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setClass(context, class1);
        ((Activity) context).startActivity(intent);
        ((Activity) context).finish();
    }

    public static void startActivity(Context context, Class<?> class1) {
        Intent intent = new Intent();
        intent.setClass(context, class1);
        ((Activity) context).startActivity(intent);
    }

    public static void logout(Activity mContext) {
        SharedPref.setLogin(mContext, false);
        SharedPref.setUserModelJSON(mContext, "");
        SharedPref.setUserId(mContext, "");
        startActivityWithFinish(mContext, SplashActi.class);
        isShown = false;
    }

    public static void setImage(Context mContext, ImageView imageView, String imageUrl) {
        Glide.with(mContext)
                .load(imageUrl)
                .priority(Priority.IMMEDIATE)
//                .placeholder(R.drawable.profile_thumb)
//                .error(R.drawable.profile_thumb)
//                .fallback(R.drawable.profile_thumb)
                .into(imageView);
    }

    //will check whether device is connected to any internet connection or not.
    public static boolean isDeviceOnline(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo().isConnectedOrConnecting();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Bitmap createThumbnailFromPath(String filePath, int type){
        return ThumbnailUtils.createVideoThumbnail(filePath, type);
    }

    public static String getMACAddress(Context context) {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(Integer.toHexString(b & 0xFF) + ":");
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }

    public static String getUniqueId() {
        String m_szDevIDShort = "35" + //we make this look like a valid IMEI
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +
                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +
                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +
                Build.USER.length() % 10; //13 digits
        return m_szDevIDShort;
    }

    public static void setRadioGroup(RadioGroup radioGroup, String value) {
        try {
            int getChild = radioGroup.getChildCount();
            //   Log.d("getchild", "" + getChild);
            for (int i = 0; i < getChild; i++) {
                int selectedId = radioGroup.getChildAt(i).getId();
                RadioButton radioSexButton = (RadioButton) radioGroup.findViewById(selectedId);
                // Log.d("getchild", "" + radioSexButton.getText().toString());
                if (radioSexButton.getText().toString().equals(value)) {
                    radioSexButton.setChecked(true);
                }                // Log.d("getchild", "" + getChild);
            }
        } catch (NullPointerException e) {
            Log.d("Null_Pointer", e.toString());
        }
    }

    public static String getTextfromRadioGroup(RadioGroup radioGroup) {
        try {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            RadioButton radioSexButton = (RadioButton) radioGroup.findViewById(selectedId);
            return radioSexButton.getText().toString();
        } catch (NullPointerException e) {
            return "null";
        }
    }

    public static AlertDialog retryAlertDialog(Context mContext, String title, String
            msg, String firstButton, String SecondButton, View.OnClickListener secondButtonClick) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
        dialogBuilder.setCancelable(false);
        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.retry_alert, null);
        dialogBuilder.setView(dialogView);

        TextView txtRAMsg = (TextView) dialogView.findViewById(R.id.txtRAMsg);
        TextView txtRAFirst = (TextView) dialogView.findViewById(R.id.txtRAFirst);
        TextView txtRASecond = (TextView) dialogView.findViewById(R.id.txtRASecond);
        View deviderView = (View) dialogView.findViewById(R.id.deviderView);

        txtRAMsg.setText(msg);

        if (firstButton.length() == 0) {
            txtRAFirst.setVisibility(View.GONE);
            deviderView.setVisibility(View.GONE);
        } else {
            txtRAFirst.setVisibility(View.VISIBLE);
            txtRAFirst.setText(firstButton);
        }

        if (SecondButton.length() == 0) {
            txtRASecond.setVisibility(View.GONE);
            deviderView.setVisibility(View.GONE);
        } else {
            txtRASecond.setVisibility(View.VISIBLE);
            txtRASecond.setText(SecondButton);
        }

        txtRAFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retryCustomAlert.dismiss();
                isShown = false;
            }
        });

        if (secondButtonClick == null) {
            secondButtonClick = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    retryCustomAlert.dismiss();
                    isShown = false;
                }
            };
        }
        txtRASecond.setOnClickListener(secondButtonClick);

        if (!isShown) {

            retryCustomAlert = dialogBuilder.create();
            retryCustomAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            retryCustomAlert.show();
            isShown = true;
        }
        return retryCustomAlert;
    }

    public static void dismissRetryAlert() {
        if (retryCustomAlert != null) {
            retryCustomAlert.dismiss();
            isShown = false;
        }
    }

    public static AlertDialog isEmpty(Context act, String field,String message) {
       return retryAlertDialog(act, act.getResources().getString(R.string.app_name), message, act.getResources().getString(R.string.Ok), "", null);
    }

    public static boolean isValidMobileNumber(Context act, String phone) {
        boolean b = true;
        if (TextUtils.isEmpty(phone)) {
            retryAlertDialog(act, act.getResources().getString(R.string.app_name), act.getString(R.string.error_mobile_number_blank), act.getResources().getString(R.string.Ok), "", null);
            b = false;
        } else if (!Pattern.matches("[1-9][0-9]{9}", phone)) {
            retryAlertDialog(act, act.getResources().getString(R.string.app_name), act.getString(R.string.error_mobile_number_wrong), act.getResources().getString(R.string.Ok), "", null);
            b = false;
        }
        return b;
    }

    public static boolean isPassword(Context act, String value) {
        boolean b = true;
        if (TextUtils.isEmpty(value)) {
            retryAlertDialog(act, act.getResources().getString(R.string.app_name), act.getString(R.string.error_password_blank), act.getResources().getString(R.string.Ok), "", null);
            b = false;
        } else if (value.length()<6) {
            retryAlertDialog(act, act.getResources().getString(R.string.app_name), act.getString(R.string.error_password_pattern), act.getResources().getString(R.string.Ok), "", null);
            b = false;
        }
        return b;
    }

    public static boolean checkStringsContainsOnlySpecialChar(String inputString) {
        boolean found = false;
        try {
            String splChrs = "/^[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]*$/";
            found = inputString.matches("[" + splChrs + "]+");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return found;
    }

    public static boolean isNumeric(String str) {
        try {
            long d = Long.parseLong(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }


    //parse the response coming from server using gson library.
    public static Object getJsonToClassObject(String jsonStr, Class<?> classs) {
        try {
            Gson gson = new GsonBuilder().create();
            return (Object) gson.fromJson(jsonStr, classs);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void replaceFrg(FragmentActivity ctx, Fragment frag, boolean addToBackStack,
                                  int resId) {
        FragmentManager fm = ctx.getSupportFragmentManager();
        int addedFrgCount = fm.getBackStackEntryCount();
        FragmentTransaction ft = fm
                .beginTransaction();
        if (resId == Constants.DEFAULT_ID) {
            resId = R.id.fmContainer;
        }
        ft.replace(resId, frag, frag.getClass().getName());
        if (addToBackStack)
            ft.addToBackStack(frag.getClass().getName());
        ft.commit();
    }


    public static Address getAddress(final Activity acti, double lat, double lon) {
        Geocoder geocoder = new Geocoder(acti, Locale.ENGLISH);

        Address address = null;
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
            if (addresses != null) {
                address = addresses.get(0);
            } else {

            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
        return address;
    }

    //to show a center toast.
    public static void showCenterToast(Context context, String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static String getImagePathFromResp(String imagePath, String name) {
        String imageUrl = imagePath + name;
        return imageUrl;
    }

    public static void PrintMsg(String string){
        System.out.println(string);
    }

    public static boolean checkEmptyNull(String string){
        if (!TextUtils.isEmpty(string)&&!string.equals("null")){
            return true;
        }
        return false;
    }

    public static void Tosat(Activity activity,String string){
       Toast.makeText(activity,string,Toast.LENGTH_LONG).show();
    }

    public static void ImageSet(Context context,ImageView imageView,String url){
        Glide.with(context).load(url).into(imageView);
    }
    public static void recyclerView(RecyclerView recyclerView){
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
    }

    public static void shapeBackground(View view,String color){
        GradientDrawable bgshape = (GradientDrawable)view.getBackground();
        bgshape.setColor(Color.parseColor(color));
    }
    public static String FirstLatterCap(String string){
        String fl = string.substring(0,1).toUpperCase();
        return string.replaceFirst(string.substring(0,1),fl);
    }
}
